package ahtewlg7.utimer.ui;

import android.os.Bundle;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.base.Optional;
import com.trello.rxlifecycle2.LifecycleProvider;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.entity.MdElement;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.mvp.NoteMdEditMvpP;
import ahtewlg7.utimer.mvp.MdContextMvpP;
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.mvp.SimpleMdContextMvpV;
import io.reactivex.annotations.NonNull;

/**
 * Created by lw on 2017/12/27.
 */

public abstract class ANoteEditorActivity extends BaseBinderActivity
    implements NoteEditMvpP.INoteEditMvpV{
    public static final String TAG = ANoteEditorActivity.class.getSimpleName();

    protected abstract EditText getEditView();
    protected abstract void toInitView();

    protected NoteSimpleMdContextMvpV noteSimpleMdContextMvpV;
    protected NoteEditMvpP noteEditMvpP;
    protected NoteMdEditMvpP mdContextEditMvpP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toInitView();
        noteEditMvpP = new NoteEditMvpP(this);
        noteEditMvpP.toRegisterEventBus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noteEditMvpP.toUnregisterEventBus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        noteEditMvpP.toDoneNote();
    }

    //=======================================INoteEditMvpV=============================================
    @Override
    public void onLoadStart() {
        //to start processing
    }

    @Override
    public void onLoadUnexist() {
        ToastUtils.showShort(R.string.notebook_not_found);
        finish();
    }

    @Override
    public void onLoadSucc(@NonNull NoteEntity noteEntity) {
        Logcat.d(TAG,"onLoadSucc : " + noteEntity.toString());

        mdContextEditMvpP   = new NoteMdEditMvpP(noteSimpleMdContextMvpV);
        mdContextEditMvpP.toParseNoteEnity(noteEntity);

        initTextChangeListener();
    }

    @Override
    public void onLoadErr(Throwable e) {
        Logcat.d(TAG,"onLoadErr : " + e.getMessage());
        ToastUtils.showShort(R.string.note_load_err);
        finish();
    }

    @Override
    public void onLoadEnd() {
        //to end processing
    }

    @Override
    public void onNoteSaveSucc(NoteEntity noteEntity) {
        mdContextEditMvpP.toSaveNoteEntity(noteEntity);
    }

    @Override
    public void onNoteSaveFail(NoteEntity noteEntity) {
        Logcat.i(TAG,"onNoteSaveFail ： " + noteEntity.toString());
        // TODO: 2018/5/13
    }
    //=======================================IMdContextEditMvpV================================================
    private void initTextChangeListener() throws RuntimeException{
        /*RxView.clicks(getEditView()).
                .throttleFirst(600, TimeUnit.MILLISECONDS)
                .compose(this.<TextViewTextChangeEvent>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySimpleObserver<TextViewTextChangeEvent>(){
                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        noteEditMvpM.handleTextChange(noteEntity, textViewTextChangeEvent);
                        Logcat.i(TAG, "handleTextChange onNext, before = " + textViewTextChangeEvent.before() +
                                ", start = " + textViewTextChangeEvent.start() + ", text = " + textViewTextChangeEvent.text() +
                                ", count = " + textViewTextChangeEvent.count());
                        String context = textViewTextChangeEvent.text().toString();
                        noteEntity.setRawContext(context);
                        if(!TextUtils.isEmpty(context))//todo : check context if change
                            noteEntity.setContextChanged(true);
                        //todo : mdContext
                    }
                });*/
    }



    class NoteSimpleMdContextMvpV extends SimpleMdContextMvpV implements NoteMdEditMvpP.INoteMdEditMvpV {
        @Override
        public LifecycleProvider getRxLifeCycleBindView() {
            return ANoteEditorActivity.this;
        }

        @Override
        public Optional<MdContextMvpP> getMdContextMvpP() {
            return serviceBinderProxy != null ?
                    Optional.of(serviceBinderProxy.getMdContextMvpP()) : Optional.<MdContextMvpP>absent();
        }

        @Override
        public void onContextParsing(MdElement element) {
            getEditView().append(element.getMdCharSequence());
        }

        @Override
        public void onContextParseErr(Throwable t) {
            ToastUtils.showShort(R.string.md_parse_err);
            finish();
        }
    }
}

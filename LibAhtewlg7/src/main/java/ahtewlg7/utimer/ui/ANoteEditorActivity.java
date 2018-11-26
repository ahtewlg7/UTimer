package ahtewlg7.utimer.ui;

import android.content.ComponentName;
import android.os.Bundle;
import android.view.GestureDetector;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.base.Optional;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.ActivityEvent;

import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.mvp.MdFileMvpP;
import ahtewlg7.utimer.mvp.NoteEntityEditMvpP;
import ahtewlg7.utimer.mvp.NoteMdEditMvpP;
import ahtewlg7.utimer.mvp.SimpleMdContextMvpV;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySimpleObserver;
import ahtewlg7.utimer.view.md.MdEditText;
import io.reactivex.annotations.NonNull;

/**
 * Created by lw on 2017/12/27.
 */

public abstract class ANoteEditorActivity extends BaseBinderActivity
    implements NoteEntityEditMvpP.INoteEditMvpV{
    public static final String TAG = ANoteEditorActivity.class.getSimpleName();

    protected abstract @NonNull MdEditText getEditView();
    protected abstract void toInitView();

    protected TextViewTextChangeEvent preTextViewTextChangeEvent;

    protected NoteEntityEditMvpP noteEditMvpP;
    protected NoteMdEditMvpP mdContextEditMvpP;
    protected NoteSimpleMdContextMvpV noteSimpleMdContextMvpV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toInitView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        noteEditMvpP.toUnregisterEventBus();
        getEditView().setGestureListener(null);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        noteEditMvpP.toDoneNote();
    }

    @Override
    protected void onServiceBinderConnected(ComponentName name) {
        super.onServiceBinderConnected(name);

        noteEditMvpP = new NoteEntityEditMvpP(this);
        noteEditMvpP.toRegisterEventBus();
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
        initTextChangeListener();

        noteSimpleMdContextMvpV = new NoteSimpleMdContextMvpV();
        mdContextEditMvpP       = new NoteMdEditMvpP(noteSimpleMdContextMvpV);

        mdContextEditMvpP.toParseNoteEnity(noteEntity);
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
        Logcat.i(TAG,"onNoteSaveSucc ： " + noteEntity.toString());
        mdContextEditMvpP.toSaveMdFile(noteEntity);
    }

    @Override
    public void onNoteSaveFail(NoteEntity noteEntity) {
        Logcat.i(TAG,"onNoteSaveFail ： " + noteEntity.toString());
        // TODO: 2018/5/13
    }
    //=======================================IMdContextEditMvpV================================================
    private void initTextChangeListener() throws RuntimeException{
        RxTextView.textChangeEvents(getEditView())
                .throttleLast(600, TimeUnit.MILLISECONDS)
                .compose(this.<TextViewTextChangeEvent>bindUntilEvent(ActivityEvent.DESTROY))
                .subscribe(new MySimpleObserver<TextViewTextChangeEvent>(){
                    @Override
                    public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
                        super.onNext(textViewTextChangeEvent);
                        Logcat.i(TAG, "handleTextChange onNext, before = " + textViewTextChangeEvent.before() +
                                ", start = " + textViewTextChangeEvent.start() + ", text = " + textViewTextChangeEvent.text() +
                                ", count = " + textViewTextChangeEvent.count());
                        preTextViewTextChangeEvent = textViewTextChangeEvent;
                    }
                });
    }

    class NoteSimpleMdContextMvpV extends SimpleMdContextMvpV implements NoteMdEditMvpP.INoteMdEditMvpV {
        @Override
        public LifecycleProvider getRxLifeCycleBindView() {
            return ANoteEditorActivity.this;
        }

        @Override
        public Optional<MdFileMvpP> getMdContextMvpP() {
            return serviceBinderProxy != null ?
                    Optional.of(serviceBinderProxy.getMdContextMvpP()) : Optional.<MdFileMvpP>absent();
        }

        @Override
        public void registeEditViewEvent(@NonNull GestureDetector.OnGestureListener gestureListener) {
            getEditView().setGestureListener(gestureListener);
        }

        @Override
        public void onMdShowMode(EditElement mdElement) {
            Logcat.i(TAG,"onMdShowMode");
            if(getEditView().ifEditable())
                getEditView().enableEdit(false);
            preTextViewTextChangeEvent = null;
            ToastUtils.showShort(R.string.editor_disable);
            /*if(mdElement != null && !TextUtils.isEmpty(mdElement.getRawText()))
                getEditView().setText(mdElement.getRawText());*/ // TODO: 2018/6/23
            /*if(textChangeDisposable != null && !textChangeDisposable.isDisposed())
                textChangeDisposable.dispose();*/
        }

        @Override
        public void onRawEditMode(EditElement mdElement) {
            Logcat.i(TAG,"onRawEditMode");
            if(!getEditView().ifEditable())
                getEditView().enableEdit(true);
            preTextViewTextChangeEvent = null;
            ToastUtils.showShort(R.string.editor_enable);
            /*if(mdElement != null && !TextUtils.isEmpty(mdElement.getRawText()))
                getEditView().setText(mdElement.getRawText());*/ // TODO: 2018/6/23

//            initTextChangeListener();
        }

        @Override
        public int getCurrLine() {
            return getEditView().getCurrLineIndex();
        }

        @Override
        public Optional<TextViewTextChangeEvent> getPreTextChangeEvent() {
            return Optional.fromNullable(preTextViewTextChangeEvent);
        }

        @Override
        public void onContextParsing(EditElement element) {
            getEditView().append(element.getMdCharSequence());//todo
        }

        @Override
        public void onContextParseErr(Throwable t) {
            ToastUtils.showShort(R.string.md_parse_err);
            finish();
        }
    }
}

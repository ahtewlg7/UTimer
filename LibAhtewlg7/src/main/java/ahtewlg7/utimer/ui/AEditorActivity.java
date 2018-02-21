package ahtewlg7.utimer.ui;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;
import com.trello.rxlifecycle2.android.ActivityEvent;

import org.joda.time.DateTime;

import java.io.File;

import ahtewlg7.utimer.GTD.GtdEntityFactory;
import ahtewlg7.utimer.GTD.NoteEntityFactory;
import ahtewlg7.utimer.R;
import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.common.NoteContextFsAction;
import ahtewlg7.utimer.entity.IUtimerEntity;
import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.AGtdTaskEntity;
import ahtewlg7.utimer.enumtype.DbErrCode;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.exception.DataBaseException;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by lw on 2017/12/27.
 */

public abstract class AEditorActivity extends BaseBinderActivity {
    public static final String TAG = AEditorActivity.class.getSimpleName();

    public static final int ACTIVITY_START_RESULT = 2;

    protected abstract @NonNull TextView getEditTextView();
    protected abstract @NonNull String getNoteIdExtrKey();
    protected abstract @NonNull String getGtdIdExtrKey();

    protected String noteEntityId;
    protected String gtdEntityId;
    protected String noteContext;

    protected Observable<String> textChangeEventObservalbe;

    protected AGtdTaskEntity gtdTaskEntity;
    protected NoteEntity noteEntity;
    protected NoteEntityFactory noteEntityFactory;
    protected GtdEntityFactory gtdEntityFactory;
    protected NoteContextFsAction noteContextFsAction;

    @Override
    protected void onPause() {
        toSaveEntity();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        setStartResult();
        super.onBackPressed();
    }

    protected void initAcitity(){
        noteEntityFactory   = new NoteEntityFactory();
        gtdEntityFactory    = new GtdEntityFactory();
        noteContextFsAction = new NoteContextFsAction();
        handleIntent(getIntent());

        initTextObservable();
    }

    //it must be after the ButterKnife.bind()
    protected void initTextObservable(){
        textChangeEventObservalbe = getTextChangeEventObservalbe(getEditTextView());

        textChangeEventObservalbe.subscribe(new Consumer<String>() {
            @Override
            public void accept(String str) throws Exception {
                Logcat.i(TAG,"deferTextObservable str : " + str);
                noteContext = str;
            }
        });
    }

    protected Observable<String> getTextChangeEventObservalbe(@NonNull TextView editTextView){
        return RxTextView.textChangeEvents(editTextView)
                .map(new Function<TextViewTextChangeEvent, String>() {
                    @Override
                    public String apply(TextViewTextChangeEvent textViewTextChangeEvent) throws Exception {
                        Logcat.i(TAG,"textChangeEvents before:" + textViewTextChangeEvent.before() +
                                ",start:" + textViewTextChangeEvent.start() + ",text:" + textViewTextChangeEvent.text() +
                                ",count:" + textViewTextChangeEvent.count());
                        return textViewTextChangeEvent.text().toString();
                    }
                });
    }

    protected void handleIntent(Intent intent){
        Observable.mergeDelayError(onNoteInit(intent), onGtdInit(intent))
            .compose(this.<IUtimerEntity>bindUntilEvent(ActivityEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Consumer<IUtimerEntity>() {
                           @Override
                           public void accept(IUtimerEntity entity) throws Exception {
                               Logcat.i(TAG,"handleIntent onNext");
                           }
                       }, new Consumer<Throwable>() {
                           @Override
                           public void accept(Throwable throwable) throws Exception {
                               Logcat.i(TAG,"handleIntent onError");
                               if (noteEntity == null || gtdTaskEntity == null) {
                                   ToastUtils.showShort(R.string.entity_not_found);
                                   setStartResult();
                                   finish();
                               }
                           }
                       }
                    , new Action() {
                        @Override
                        public void run() throws Exception {
                            Logcat.i(TAG,"handleIntent onComplete");
                            readNoteContext(noteEntity);
                        }
                    });
    }

    protected void setStartResult(){
        Intent intent = new Intent();
        intent.putExtra(getGtdIdExtrKey(), gtdEntityId);
        intent.putExtra(getNoteIdExtrKey(), noteEntityId);
        setResult(ACTIVITY_START_RESULT, intent);
        Logcat.i(TAG,"setStartResult ACTIVITY_START_RESULT : gtdEntityId = " + gtdEntityId + ",noteEntityId = " + noteEntityId);
    }

    protected void toSaveEntity(){
        if(TextUtils.isEmpty(noteContext)/* || serviceBinderProxy == null*/) {
            Logcat.i(TAG,"toSaveEntity : cancel it");
            return;
        }
        DateTime now = DateTime.now();
        String tmpName  = new DateTimeAction().toFormat(now) + "_note";
        if(gtdTaskEntity.getTaskType() == GtdType.INBOX){
            if(TextUtils.isEmpty(gtdTaskEntity.getTitle()))
                gtdTaskEntity.setTitle(tmpName);
            else if(TextUtils.isEmpty(noteEntity.getNoteName()))
                noteEntity.setNoteName(gtdTaskEntity.getTitle());
    }

        String fileRPath   = new FileSystemAction().getDocNoteRPath() + gtdTaskEntity.getTitle() + File.separator;
        gtdTaskEntity.setFileRPath(fileRPath);
        noteEntity.setFileRPath(fileRPath);

        noteEntity.setLastModifyDateTime(now);

        gtdTaskEntity.addNoteEntity(noteEntity);
        gtdTaskEntity.setLastModifyDateTime(now);

        boolean noteSave = noteEntityFactory.saveNoteEntity(noteEntity);
        if(!noteSave) {
            Logcat.i(TAG,"save Note failed");
            return;
        }
        boolean gtdSave = gtdEntityFactory.saveGtdTaskEntity(gtdTaskEntity);
        if(!gtdSave) {
            Logcat.i(TAG,"save gtd failed");
            return;
        }
        writeNoteContext(noteEntity, noteContext);
    }

    protected Observable<NoteEntity> onNoteInit(@NonNull Intent intent){
        return Observable.just(intent)
                .flatMap(new Function<Intent, ObservableSource<NoteEntity>>() {
                    @Override
                    public ObservableSource<NoteEntity> apply(Intent intent) throws Exception {
                        noteEntityId = intent.getStringExtra(getNoteIdExtrKey());
                        Logcat.i(TAG,"onNoteInit ：noteEntityId = " + noteEntityId);
                        if(TextUtils.isEmpty(noteEntityId))
                            throw new DataBaseException(DbErrCode.ERR_DB_ID_EMPTY);
                        return noteEntityFactory.getNoteEntity(Observable.just(noteEntityId));
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof DataBaseException && ((DataBaseException) throwable).getErrCode() == DbErrCode.ERR_DB_ID_EMPTY) {
                            noteEntity   = noteEntityFactory.createNoteEntity();
                            noteEntityId = noteEntity.getId();
                            Logcat.i(TAG, "onNoteInit failed, to create one ：" + noteEntityId);
                        }
                    }
                })
                .doOnNext(new Consumer<NoteEntity>() {
                    @Override
                    public void accept(NoteEntity entity) throws Exception {
                        noteEntity   = entity;
                        noteEntityId = entity.getId();
                        Logcat.i(TAG, "onNoteInit doOnNext ：" + noteEntityId);
                    }
                });
    }
    protected Observable<AGtdEntity> onGtdInit(@NonNull Intent intent) {
        return Observable.just(intent)
                .flatMap(new Function<Intent, ObservableSource<AGtdEntity>>() {
                    @Override
                    public ObservableSource<AGtdEntity> apply(Intent intent) throws Exception {
                        gtdEntityId  = intent.getStringExtra(getGtdIdExtrKey());
                        Logcat.i(TAG,"handleIntent ：gtdEntityId = " + gtdEntityId);
                        if(TextUtils.isEmpty(gtdEntityId))
                            throw new DataBaseException(DbErrCode.ERR_DB_ID_EMPTY);
                        return gtdEntityFactory.getEntity(Observable.just(gtdEntityId));
                    }
                })
                .doOnError(new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        if (throwable instanceof DataBaseException && ((DataBaseException) throwable).getErrCode() == DbErrCode.ERR_DB_ID_EMPTY) {
                            gtdTaskEntity = (AGtdTaskEntity) gtdEntityFactory.createGtdEntity(GtdType.INBOX);
                            gtdEntityId   = gtdTaskEntity.getId();
                            Logcat.i(TAG, "onGtdInit failed, to create one ：" + gtdEntityId);
                        }
                    }
                })
                .doOnNext(new Consumer<AGtdEntity>() {
                    @Override
                    public void accept(AGtdEntity entity) throws Exception {
                        gtdTaskEntity   = (AGtdTaskEntity)entity;
                        gtdEntityId     = entity.getId();
                        Logcat.i(TAG, "onGtdInit doOnNext ：" + gtdEntityId);
                    }
                });
    }

    protected void writeNoteContext(final NoteEntity noteEntity, final String noteContext){
        Observable.just(1).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        boolean result = noteContextFsAction.writeNoteContext(noteEntity,noteContext);
                        Logcat.i(TAG,"saveNoteContext : result = " + result);
                    }
                });
    }

    protected void readNoteContext(final NoteEntity noteEntity){
        Observable.just(1).subscribeOn(Schedulers.io())
                .map(new Function<Object, String>() {
                    @Override
                    public String apply(Object o) throws Exception {
                        return noteContextFsAction.readNoteContext(noteEntity);
                    }
                })
                .compose(this.<String>bindUntilEvent(ActivityEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String s) throws Exception {
                        Logcat.i(TAG,"readNoteContext ");
                        if(!TextUtils.isEmpty(s)){
                            noteContext = s;
                            Logcat.i(TAG,"readNoteContext ：update to textView");
                            getEditTextView().setText(noteContext);
                        }
                    }
                });
    }
}

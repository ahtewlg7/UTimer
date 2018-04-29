package ahtewlg7.utimer.ui;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.components.RxActivity;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.busevent.NoteEditEvent;
import ahtewlg7.utimer.common.EventBusFatory;
import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.mvp.INoteEditMvpV;
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2017/12/27.
 */

public abstract class ANoteEditorActivity extends BaseBinderActivity
    implements INoteEditMvpV{
    public static final String TAG = ANoteEditorActivity.class.getSimpleName();

    protected abstract void toInitView();

    protected NoteEditMvpP noteEditMvpP;

    /*protected NoteEntityFactory noteEntityFactory;
    protected GtdEntityFactory gtdEntityFactory;
    protected NoteContextFsAction noteContextFsAction;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toInitView();
        noteEditMvpP = new NoteEditMvpP(this);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBusFatory.getInstance().getDefaultEventBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBusFatory.getInstance().getDefaultEventBus().unregister(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        noteEditMvpP.toDoneNote();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onNoteEditEvent(NoteEditEvent event) {
        noteEditMvpP.toLoadNote(event.getEoteEntityId());
    }

    @Override
    public void onContextShow(INoteEntity noteEntity) {
        getEditView().setText(noteEntity.getMdContext());
    }

    @Override
    public RxActivity getUiContext() {
        return this;
    }

    @Override
    public void onLoading() {
        //to start processing
    }

    @Override
    public void onLoadNull() {
        ToastUtils.showShort(R.string.entity_not_found);
        finish();
    }

    @Override
    public void onLoadUnexist() {
        ToastUtils.showShort(R.string.notebook_not_found);
        finish();
    }

    @Override
    public void onLoadErr(Throwable e) {
        Logcat.d(TAG,"onLoadErr : " + e.getMessage());
        ToastUtils.showShort(R.string.note_load_err);
        finish();
    }

    @Override
    public void onLoadComplete() {
        //to end processing
    }

    @Override
    public void onNoteDone(INoteEntity noteEntity) {
        String noteId = noteEntity != null ? noteEntity.getId() : null;
        EventBusFatory.getInstance().getDefaultEventBus().post(new NoteEditEvent(noteId));
    }

    @Override
    public void toSaveContext(INoteEntity noteEntity) {
        serviceBinderProxy.toSaveNote(noteEntity);
    }

    /*protected void toSaveEntity(){
        if(TextUtils.isEmpty(noteContext)*//* || serviceBinderProxy == null*//*) {
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

        String fileRPath   = new FileSystemAction().getNoteDocRPath() + gtdTaskEntity.getTitle() + File.separator;
        gtdTaskEntity.setFileRPath(fileRPath);
        noteEntity.setFileRPath(fileRPath);

        noteEntity.setLastModifyDateTime(now);

        gtdTaskEntity.addSubEntity(noteEntity);
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
    }*/

    /*protected Flowable<NoteEntity> onNoteInit(@NonNull Intent intent){
        return Flowable.just(intent)
                .flatMap(new Function<Intent, Publisher<NoteEntity>>() {
                    @Override
                    public Publisher<NoteEntity> apply(Intent intent) throws Exception {
                        noteEntityId = intent.getStringExtra(getNoteIdExtrKey());
                        Logcat.i(TAG,"onNoteInit ：noteEntityId = " + noteEntityId);
                        if(TextUtils.isEmpty(noteEntityId))
                            throw new DataBaseException(DbErrCode.ERR_DB_ID_EMPTY);
                        return noteEntityFactory.loadEntity(Flowable.just(noteEntityId));
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
    }*/
    /*protected Flowable<AGtdEntity> onGtdInit(@NonNull Intent intent) {
        return Flowable.just(intent)
                .flatMap(new Function<Intent, Publisher<AGtdEntity>>() {
                    @Override
                    public Publisher<AGtdEntity> apply(Intent intent) throws Exception {
                        gtdEntityId  = intent.getStringExtra(getGtdIdExtrKey());
                        Logcat.i(TAG,"handleIntent ：gtdEntityId = " + gtdEntityId);
                        if(TextUtils.isEmpty(gtdEntityId))
                            throw new DataBaseException(DbErrCode.ERR_DB_ID_EMPTY);
                        return gtdEntityFactory.loadEntity(Flowable.just(gtdEntityId));
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
    }*/

   /* protected void writeNoteContext(final NoteEntity noteEntity, final String noteContext){
        Observable.just(1).subscribeOn(Schedulers.io())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object object) throws Exception {
                        boolean result = noteContextFsAction.writeNoteContext(noteEntity,noteContext);
                        Logcat.i(TAG,"saveNoteContext : result = " + result);
                    }
                });
    }*/

   /*protected void readNoteContext(final NoteEntity noteEntity){
        noteEditMvpP.toReadNoteContext(noteEntity);*/
        /*Observable.just(1).subscribeOn(Schedulers.io())
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
    }*/
}

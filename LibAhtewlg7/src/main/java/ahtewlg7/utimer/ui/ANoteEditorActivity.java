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
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2017/12/27.
 */

public abstract class ANoteEditorActivity extends BaseBinderActivity
    implements NoteEditMvpP.INoteEditMvpV {
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
}

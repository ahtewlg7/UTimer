package ahtewlg7.utimer.ui;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.busevent.NoteEditEndEvent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        toInitView();
        noteEditMvpP = new NoteEditMvpP(this);

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

    //EventBus callback
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onNoteEditEvent(NoteEditEvent event) {
        Logcat.i(TAG,"onNoteEditEvent : " + event.toString());
        noteEditMvpP.toLoadNote(event.getEoteEntityId().or(""));
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
    public void onLoadSucc(INoteEntity noteEntity) {
        Logcat.d(TAG,"onLoadSucc : " + noteEntity.toString());
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
        Logcat.i(TAG,"onNoteDone : noteId = " + noteId);
        EventBusFatory.getInstance().getDefaultEventBus().postSticky(new NoteEditEndEvent(noteId));
    }

    @Override
    public void onNoteSaveFail(INoteEntity noteEntity) {
        Logcat.i(TAG,"onNoteSaveFail ： " + noteEntity.toString());
        // TODO: 2018/5/13
    }

    @Override
    public void toShowContext(INoteEntity noteEntity) {
        Logcat.i(TAG,"toShowContext ： " + noteEntity.getMdContext());
//        getEditView().setText(noteEntity.getMdContext());//todo
    }

    @Override
    public void toSaveContext(INoteEntity noteEntity) {
        serviceBinderProxy.toSaveNote(noteEntity);
    }
}

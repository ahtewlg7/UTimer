package ahtewlg7.utimer.ui;

import android.os.Bundle;

import com.blankj.utilcode.util.ToastUtils;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.entity.NoteEntity;
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
    public void onLoadSucc(NoteEntity noteEntity) {
        Logcat.d(TAG,"onLoadSucc : " + noteEntity.toString());
        getEditView().setText(noteEntity.getRawContext());//todo md Context
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
    public void onNoteSaveFail(NoteEntity noteEntity) {
        Logcat.i(TAG,"onNoteSaveFail ： " + noteEntity.toString());
        // TODO: 2018/5/13
    }

    @Override
    public void toShowContext(NoteEntity noteEntity) {
        Logcat.i(TAG,"toShowContext ： " + noteEntity.getMdContext());
//        getEditView().setText(noteEntity.getMdContext());//todo
    }

    @Override
    public void toSaveContext(NoteEntity noteEntity) {
        serviceBinderProxy.toSaveNote(noteEntity);
    }
}

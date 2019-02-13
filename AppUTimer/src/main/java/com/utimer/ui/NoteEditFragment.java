package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.mvp.AUtimerTxtEditMvpP;
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.BaseUtimerEidtView;
import butterknife.BindView;

public class NoteEditFragment extends ATxtEditFragment
        implements NoteEditMvpP.INoteEditMvpV, BaseUtimerEidtView.IUtimerAttachEditView{
    public static final String TAG = NoteEditFragment.class.getSimpleName();

    public static final String KEY_NOTE = "note";

    @BindView(R.id.fragment_note_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_note_edit_rv)
    BaseUtimerEidtView editRecyclerView;

    private NoteEditMvpP editMvpP;

    public static NoteEditFragment newInstance(NoteEntity entity) {
        Bundle args = new Bundle();
        if(entity != null)
            args.putSerializable(KEY_NOTE, entity);
        else {
            String now = new DateTimeAction().toFormatNow().toString();
            NoteEntity e = (NoteEntity)new NoteBuilder().setTitle(now).build();
            args.putSerializable(KEY_NOTE, e);
        }
        NoteEditFragment fragment = new NoteEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_note;
    }


    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_note);
    }

    @NonNull
    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void initToolbar() {
        toolbar.setTitle(getTitle());
    }

    /**********************************************AEditFragment**********************************************/
    @NonNull
    protected AUtimerTxtEditMvpP getEditMvpP() {
        if(editMvpP == null)
            editMvpP = new NoteEditMvpP(getUTimerEntity(), this);
        return editMvpP;
    }

    @Override
    protected NoteEntity getUTimerEntity(){
        return (NoteEntity) getArguments().getSerializable(KEY_NOTE);
    }

    @Override
    protected void toStartEdit() {
        editRecyclerView.setAttachEditView(this);
        editRecyclerView.setUTimerEntity(getUTimerEntity());
        editRecyclerView.toStartEdit();
    }
    @Override
    protected void toEndEdit() {
        int resultCode = RESULT_CANCELED;
        List<EditElement> elementList = editRecyclerView.getEditElementList();
        if(elementList != null){//maybe the entity is not loaded
            resultCode = RESULT_OK;
            ((NoteEntity)getArguments().getSerializable(KEY_NOTE)).appendDetail(elementList.get(0).getMdCharSequence().toString());
        }
        setFragmentResult(resultCode, getArguments());
    }
    /**********************************************IShorthandEditMvpV**********************************************/
    @Override
    public void onLoadStart() {
        ToastUtils.showShort("onLoadStart");
    }

    @Override
    public void onLoadSucc() {
        ToastUtils.showShort("onLoadSucc");
    }

    @Override
    public void onLoadErr(Throwable e) {
        ToastUtils.showShort("onLoadErr : " + e.getMessage());
    }

    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }
    /******************************************INoteEditMvpV**********************************************/
    @Override
    public void onSaveStart() {
        Logcat.i(TAG, "onSaveStart");
    }

    @Override
    public void onSaveErr(Throwable e) {
        Logcat.i(TAG, "onSaveErr");
    }

    @Override
    public void onSaveEnd() {
        Logcat.i(TAG, "onSaveEnd");
    }
}

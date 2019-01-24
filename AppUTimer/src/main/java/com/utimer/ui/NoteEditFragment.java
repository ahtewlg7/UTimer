package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;

import com.google.common.base.Optional;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.entity.md.EditMementoBean;
import ahtewlg7.utimer.mvp.AUtimerEditMvpP;
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class NoteEditFragment extends ATxtEditFragment implements NoteEditMvpP.INoteEditMvpV {
    public static final String TAG = NoteEditFragment.class.getSimpleName();

    public static final String KEY_NOTE = "note";

    @BindView(R.id.activity_utimer_toolbar)
    Toolbar toolbar;

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
        return R.layout.un_fragment_recycler;
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

    @Override
    protected Optional<EditText> getEditTextItem(int index) {
        return null;
    }

    @NonNull
    @Override
    protected AUtimerEditMvpP getEditMvpP() {
        if(editMvpP == null)
            editMvpP = new NoteEditMvpP(getUtimerEntity(), this);
        return editMvpP;
    }

    @Override
    protected NoteEntity getUtimerEntity(){
        return (NoteEntity) getArguments().getSerializable(KEY_NOTE);
    }


    @Override
    public void onLoadStart() {

    }

    @Override
    public void onLoadErr(Throwable e) {

    }

    @Override
    public void onLoadEnd(List<EditElement> mdElementList) {

    }

    @Override
    public void onParseStart() {

    }

    @Override
    public void onParseErr(Throwable e) {

    }

    @Override
    public void onParseSucc(int index, EditElement editElement) {

    }

    @Override
    public void onParseEnd() {

    }

    @Override
    public void onRestoreEnd(EditMementoBean MdMementoBean) {

    }

    @Override
    public void onSaveStart() {

    }

    @Override
    public void onSaveErr(Throwable e) {

    }

    @Override
    public void onSaveEnd() {

    }

    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }
}

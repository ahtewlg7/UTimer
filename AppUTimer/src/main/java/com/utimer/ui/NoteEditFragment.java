package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Optional;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.entity.md.EditMementoBean;
import ahtewlg7.utimer.mvp.AUtimerTxtEditMvpP;
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.EditLinerRecyclerView;
import ahtewlg7.utimer.view.md.MdEditText;
import butterknife.BindView;

public class NoteEditFragment extends ATxtEditFragment
        implements NoteEditMvpP.INoteEditMvpV , BaseQuickAdapter.OnItemChildClickListener {
    public static final String TAG = NoteEditFragment.class.getSimpleName();

    public static final String KEY_NOTE = "note";

    @BindView(R.id.fragment_note_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_note_edit_rv)
    EditLinerRecyclerView editReCyclerView;

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

    @Override
    protected Optional<EditText> getEditTextItem(int index) {
        MdEditText currEditText = null;
        try{
            currEditText = (MdEditText) editReCyclerView.getAdapter().getViewByPosition(index, R.id.view_md_edit_tv);
        }catch (Exception e){
            e.printStackTrace();
        }
        return Optional.fromNullable((EditText)currEditText);
    }

    @NonNull
    @Override
    protected AUtimerTxtEditMvpP getEditMvpP() {
        if(editMvpP == null)
            editMvpP = new NoteEditMvpP(getUtimerEntity(), this);
        return editMvpP;
    }

    @Override
    protected NoteEntity getUtimerEntity(){
        return (NoteEntity) getArguments().getSerializable(KEY_NOTE);
    }
    /**********************************************AEditFragment**********************************************/
    @Override
    protected void onEditEnd() {
        super.onEditEnd();

        int resultCode = RESULT_CANCELED;
        if(!getEditMvpP().ifEditElementEmpty()){
            resultCode = RESULT_OK;
            Optional<EditElement> elementOptional = editMvpP.getEditElement(0);
            if(elementOptional.isPresent())
                ((NoteEntity)getArguments().getSerializable(KEY_NOTE)).appendDetail(elementOptional.get().getMdCharSequence().toString());
        }
        setFragmentResult(resultCode, getArguments());
    }
    /******************************************OnItemChildClickListener**********************************************/
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        editMvpP.onEditViewClick(position);
    }

    /******************************************INoteEditMvpV**********************************************/
    @Override
    public void onLoadStart() {
        ToastUtils.showShort("onLoadStart");
    }

    @Override
    public void onLoadErr(Throwable e) {
        ToastUtils.showShort("onLoadErr : " + e.getMessage());
    }

    @Override
    public void onLoadEnd(List<EditElement> mdElementList) {
        ToastUtils.showShort("onLoadEnd size = " + mdElementList.size());
        editReCyclerView.init(getContext(), mdElementList, null,
                this,null, null,null,null);
    }

    @Override
    public void onParseStart() {
        ToastUtils.showShort("to start parse");
    }

    @Override
    public void onParseErr(Throwable e) {
        ToastUtils.showShort("parse err: " + e.getMessage());
    }

    @Override
    public void onParseSucc(int index, EditElement editElement) {
        ToastUtils.showShort("parse succ");
        editReCyclerView.resetData(index, editElement);
    }

    @Override
    public void onParseEnd() {
    }


    @Override
    public void onRestoreEnd(EditMementoBean MdMementoBean) {

    }

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

    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }
}

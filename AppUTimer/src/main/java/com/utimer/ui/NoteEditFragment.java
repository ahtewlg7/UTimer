package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.google.common.collect.Table;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.mvp.NoteEditMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.BaseUtimerEidtView;
import ahtewlg7.utimer.view.md.MdEditView;
import butterknife.BindView;
import io.reactivex.Flowable;

public class NoteEditFragment extends ATxtEditFragment
        implements NoteEditMvpP.INoteEditMvpV, BaseUtimerEidtView.IUtimerAttachEditView{
    public static final String TAG = NoteEditFragment.class.getSimpleName();

    public static final String KEY_NOTE = "note";

    @BindView(R.id.fragment_note_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_note_edit_view)
    MdEditView mdEditView;

    private NoteEditMvpP editMvpP;

    public static NoteEditFragment newInstance(NoteEntity entity) {
        Bundle args = new Bundle();
        if(entity != null)
            args.putSerializable(KEY_NOTE, entity);
        else {
            String now = new DateTimeAction().toFormatNow().toString();
            NoteEntity e = new NoteBuilder().setTitle(now).build();
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
        if(getUTimerEntity().ifValid())
            return getUTimerEntity().getTitle();
        return MyRInfo.getStringByID(R.string.title_note);
    }

    @Override
    protected int getMenuRid() {
        return R.menu.note_menu;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.note_menu_rename:
                toCreateRenameDialog();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    @Override
    protected void onEnvReady(View inflateView) {
        super.onEnvReady(inflateView);
        editMvpP = new NoteEditMvpP(getUTimerEntity(), this);
    }

    /**********************************************AEditFragment**********************************************/
    @Override
    protected NoteEntity getUTimerEntity(){
        return (NoteEntity) getArguments().getSerializable(KEY_NOTE);
    }

    @Override
    protected boolean ifTxtEditing() {
        return mdEditView != null && mdEditView.ifEditing();
    }

    @Override
    protected void toStartEdit() {
        mdEditView.toSetAttachEditView(this);
        mdEditView.setUTimerEntity(getUTimerEntity());
        mdEditView.toStartEdit();
    }

    @Override
    protected void toPauseEdit() {
        mdEditView.toPauseEdit();
    }

    @Override
    protected void toEndEdit() {
        mdEditView.toEndEdit();
        int resultCode = RESULT_CANCELED;
        List<EditElement> elementList = mdEditView.getEditElementList();
        Table<Integer, Integer, EditElement> editElementTable = mdEditView.getEditElementTable();
        if(elementList.size() > 0){//maybe the entity is not loaded
            resultCode = RESULT_OK;
            if(!TextUtils.isEmpty(mdEditView.getLastAccessEditElement().getMdCharSequence()))
                getUTimerEntity().setDetail(mdEditView.getLastAccessEditElement().getMdCharSequence().toString());
            editMvpP.toPostAction(editElementTable);
            editMvpP.toFinishEdit(Flowable.fromIterable(elementList));
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
    public boolean ifTxtChanged() {
        return mdEditView.ifTxtChanged();
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

    private void toCreateRenameDialog(){
        new MaterialDialog.Builder(getContext()).title(R.string.rename)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(MyRInfo.getStringByID(R.string.prompt_new_note_name), "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        //do nothing
                    }
                })
                .negativeText(R.string.no)
                .positiveText(R.string.yes)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String name = dialog.getInputEditText().getText().toString();
                        getUTimerEntity().setTitle(name);

                        /*
                        GtdProjectEntity entity = (GtdProjectEntity)new GtdProjectBuilder().setTitle(name).build();
                        startForResult(ProjectFragment.newInstance(entity), REQ_NEW_FRAGMENT);*/
                    }
                }).show();
    }
}

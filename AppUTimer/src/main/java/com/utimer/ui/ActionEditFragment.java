package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.MenuItem;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import org.joda.time.DateTime;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.gtd.GtdActionBuilder;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.mvp.ActionEditMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class ActionEditFragment extends ATxtEditFragment
        implements ActionEditMvpP.IActEditMvpV{
    public static final String TAG = ActionEditFragment.class.getSimpleName();

    public static final String KEY_ACTION = "ACTION";

    @BindView(R.id.fragment_action_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_action_edit_raw)
    EditText rawEditView;
    @BindView(R.id.fragment_action_edit_nlp)
    EditText nlpEditView;

    private ActionEditMvpP editMvpP;

    public static ActionEditFragment newInstance(GtdActionEntity entity) {
        Bundle args = new Bundle();
        if(entity != null)
            args.putSerializable(KEY_ACTION, entity);
        else {
            GtdActionBuilder builder  = new GtdActionBuilder()
                    .setCreateTime(DateTime.now())
                    .setUuid(new IdAction().getUUId());
            GtdActionEntity e = builder.build();
            e.setActionState(ActState.MAYBE);
            e.setLastModifyTime(DateTime.now());
            e.setLastAccessTime(DateTime.now());
            args.putSerializable(KEY_ACTION, e);
        }
        ActionEditFragment fragment = new ActionEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_action;
    }


    @Override
    protected String getTitle() {
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

    /**********************************************AEditFragment**********************************************/
    @Override
    protected GtdActionEntity getUTimerEntity(){
        return (GtdActionEntity) getArguments().getSerializable(KEY_ACTION);
    }

    @Override
    protected boolean ifTxtEditing() {
//        return rawEditView != null && rawEditView.ifEditing();
        return false;
    }

    @Override
    protected void toStartEdit() {
        if(editMvpP == null)
            editMvpP = new ActionEditMvpP(this,getUTimerEntity());
        editMvpP.toParseW5h2();
        if(getUTimerEntity().getDetail().isPresent())
            rawEditView.setText(getUTimerEntity().getDetail().get());
        /*rawEditView.toSetAttachEditView(this);
        rawEditView.setUTimerEntity(getUTimerEntity());
        rawEditView.toStartEdit();*/
    }

    @Override
    protected void toPauseEdit() {
//        rawEditView.toPauseEdit();
    }

    @Override
    protected void toEndEdit() {
        /*rawEditView.toEndEdit();
        int resultCode = RESULT_CANCELED;
        List<EditElement> elementList = rawEditView.getEditElementList();
        Table<Integer, Integer, EditElement> editElementTable = rawEditView.getEditElementTable();
        if(elementList.size() > 0){//maybe the entity is not loaded
            resultCode = RESULT_OK;
            if(!TextUtils.isEmpty(rawEditView.getLastAccessEditElement().getMdCharSequence()))
                getUTimerEntity().setDetail(rawEditView.getLastAccessEditElement().getMdCharSequence().toString());
            editMvpP.toPostAction(editElementTable);
            editMvpP.toFinishEdit(Flowable.fromIterable(elementList));
        }
        setFragmentResult(resultCode, getArguments());*/
    }

    /**********************************************IShorthandEditMvpV**********************************************/


    @Override
    public void onParseStart() {

    }

    @Override
    public void onParseEnd(String detail) {
        nlpEditView.setText(detail);
    }

    @Override
    public void onParseErr(Throwable t) {
        t.printStackTrace();
    }
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }


    /******************************************INoteEditMvpV**********************************************/
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

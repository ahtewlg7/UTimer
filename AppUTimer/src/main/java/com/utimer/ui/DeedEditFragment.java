package com.utimer.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.utimer.R;

import org.joda.time.DateTime;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.gtd.GtdDeedBuilder;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.mvp.un.DeedEditMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class DeedEditFragment extends ATxtEditFragment
        implements DeedEditMvpP.IActEditMvpV{
    public static final String TAG = DeedEditFragment.class.getSimpleName();

    public static final String KEY_ACTION = "DEED";

    @BindView(R.id.fragment_action_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_action_edit_raw)
    EditText rawEditView;
    @BindView(R.id.fragment_action_edit_nlp)
    EditText nlpEditView;

    private String preRawTxt;
    private MaterialDialog nlpWaitDialog;

    private DeedEditMvpP editMvpP;

    public static DeedEditFragment newInstance(GtdDeedEntity entity) {
        Bundle args = new Bundle();
        if(entity != null) {
            args.putSerializable(KEY_ACTION, entity);
            args.putInt(KEY_WORK_MODE, WORK_AS_EDIT);
        }else {
            GtdDeedBuilder builder  = new GtdDeedBuilder()
                    .setCreateTime(DateTime.now())
                    .setUuid(new IdAction().getUUId());
            GtdDeedEntity e = builder.build();
            e.setDeedState(DeedState.MAYBE);
            e.setLastModifyTime(DateTime.now());
            e.setLastAccessTime(DateTime.now());
            args.putSerializable(KEY_ACTION, e);
            args.putInt(KEY_WORK_MODE, WORK_AS_NEW);
        }
        DeedEditFragment fragment = new DeedEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*false means menu off; true means menu on*/
        setHasOptionsMenu(false);
    }

    @Override
    protected boolean ifEnvOk() {
        int workMode = getArguments().getInt(KEY_WORK_MODE);
        if(workMode == WORK_AS_EDIT)
            return getUTimerEntity().ifValid();
        return !TextUtils.isEmpty(getUTimerEntity().getUuid()) && getUTimerEntity().getDeedState() != null;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_deed_edit;
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

    /*@Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.note_menu_rename:
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }*/

    /**********************************************AEditFragment**********************************************/
    @Override
    protected GtdDeedEntity getUTimerEntity(){
        return (GtdDeedEntity) getArguments().getSerializable(KEY_ACTION);
    }

    @Override
    protected boolean ifTxtEditing() {
        return ifTxtEdited() || editMvpP.isNlping();
    }

    @Override
    protected void toStartEdit() {
        if(getUTimerEntity().getDetail().isPresent())
            rawEditView.setText(getUTimerEntity().getDetail().get());
        if(editMvpP == null)
            editMvpP = new DeedEditMvpP(this,getUTimerEntity());
        /*if(getArguments().getInt(KEY_WORK_MODE) == WORK_AS_EDIT)
            editMvpP.toParseW5h2();*/
    }

    @Override
    protected void toPauseEdit() {
        ToastUtils.showShort(R.string.prompt_nlp_wait);
        updateEntityDetail();
        editMvpP.toParseTimeElement();
//        editMvpP.toParseW5h2();
    }

    @Override
    protected void toEndEdit() {
        int resultCode = RESULT_OK;
        if(ifTxtEdited())
            getUTimerEntity().setTitle(rawEditView.getText().toString());
        editMvpP.toFinishEdit();
        /*else if(TextUtils.isEmpty(rawEditView.getText())){
            GtdMachine.getInstance().getCurrState(getUTimerEntity()).toTrash(getUTimerEntity());
        }*/
        setFragmentResult(resultCode, getArguments());
    }

    /**********************************************IShorthandEditMvpV**********************************************/
    @Override
    public void onParseStart() {
        onNlpStart();
    }

    @Override
    public void onParseEnd() {
        onNlpEnd();
        StringBuilder builder = new StringBuilder();
        /*BaseW5h2Entity w5h2Entity = getUTimerEntity().getW5h2Entity();
        if(w5h2Entity != null){
            if(w5h2Entity.getWho() != null && w5h2Entity.getWho().toTips().isPresent())
                builder.append(w5h2Entity.getWho().toTips().get()).append("\n");
            if(w5h2Entity.getWhat() != null && w5h2Entity.getWhat().toTips().isPresent())
                builder.append(w5h2Entity.getWhat().toTips().get()).append("\n");
            if(w5h2Entity.getWhen() != null && w5h2Entity.getWhen().toTips().isPresent())
                builder.append(w5h2Entity.getWhen().toTips().get()).append("\n");
            if(w5h2Entity.getWhere() != null && w5h2Entity.getWhere().toTips().isPresent())
                builder.append(w5h2Entity.getWhere().toTips().get()).append("\n");
        }else*/ if(getUTimerEntity().getWarningTimeList() != null) {
            builder.append(getUTimerEntity().getWarningTimeList().toString());
        }else{
            builder.append("Null");
        }
        nlpEditView.setText(builder.toString());
        preRawTxt = rawEditView.getText().toString();
    }

    @Override
    public void onParseErr(Throwable t) {
        onNlpEnd();
        t.printStackTrace();
        ToastUtils.showShort(t.getMessage());
    }
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }

    /******************************************INoteEditMvpV**********************************************/
    private void updateEntityDetail(){
        getUTimerEntity().setTitle(rawEditView.getText().toString());
        getUTimerEntity().setDetail(rawEditView.getText().toString());
    }
    private void onNlpStart(){
        if(nlpWaitDialog == null )
            toCreateAIWaitingDialog();
        if(!nlpWaitDialog.isShowing())
            nlpWaitDialog.show();
        nlpEditView.setHint(R.string.prompt_nlp_wait);
    }
    private void onNlpEnd(){
        if(nlpWaitDialog != null)
            nlpWaitDialog.dismiss();
        nlpEditView.setHint(null);
    }

    private boolean ifTxtEdited(){
        return rawEditView != null && !rawEditView.getText().toString().equals(preRawTxt);
    }
    private void toCreateAIWaitingDialog(){
        nlpWaitDialog = new MaterialDialog.Builder(getContext()).title(R.string.wait)
                .content(R.string.prompt_nlp_wait)
                .cancelable(true)
                .show();
    }
}

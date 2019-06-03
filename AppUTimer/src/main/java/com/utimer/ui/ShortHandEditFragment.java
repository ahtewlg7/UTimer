package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.collect.Table;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.ShortHandBuilder;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.exception.UtimerEditException;
import ahtewlg7.utimer.mvp.AUtimerTxtEditMvpP;
import ahtewlg7.utimer.mvp.ShorthandEditMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.BaseUtimerEidtView;
import ahtewlg7.utimer.view.md.MdEditView;
import butterknife.BindView;
import io.reactivex.Flowable;

import static ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode.ERR_EDIT_ATTACH_FILE_NOT_READY;

public class ShortHandEditFragment extends ATxtEditFragment
        implements BaseUtimerEidtView.IUtimerAttachEditView, AUtimerTxtEditMvpP.IUtimerEditMvpV {
    public static final String TAG = ShortHandEditFragment.class.getSimpleName();

    public static final String KEY_SHORTHAND = "shorthand";

    @BindView(R.id.fragment_shorthand_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_shorthand_editview)
    MdEditView mdEditView;

    private ShorthandEditMvpP editMvpP;

    public static ShortHandEditFragment newInstance(ShortHandEntity entity) {
        Bundle args = new Bundle();
        if(entity != null)
            args.putSerializable(KEY_SHORTHAND, entity);
        else {
            //todo:if the entity is create in one minute, they are the same
            String now = new DateTimeAction().toFormatNow();
            ShortHandEntity e = new ShortHandBuilder().setTitle(now).build();
            args.putSerializable(KEY_SHORTHAND, e);
        }
        ShortHandEditFragment fragment = new ShortHandEditFragment();
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
    public int getLayoutRid() {
        return R.layout.fragment_shorthand;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_shorthand);
    }

    @Override
    @MenuRes
    protected int getMenuRid() {
        return R.menu.tool_menu;
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
        Logcat.i(TAG,"onOptionsItemSelected " + item.getTitle());
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.tool_menu_add:
                Logcat.i(TAG, "to create shorthand");
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }*/

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
        if(e instanceof UtimerEditException && ((UtimerEditException)e).getErrCode() == ERR_EDIT_ATTACH_FILE_NOT_READY)
            return;
        ToastUtils.showShort("onLoadErr : " + e.getMessage());
    }

    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }


    /**********************************************AEditFragment**********************************************/
    @Override
    public void onEnvReady(View inflateView) {
        super.onEnvReady(inflateView);
        editMvpP = new ShorthandEditMvpP(getUTimerEntity(), this);
    }

    @Override
    protected @NonNull ShortHandEntity getUTimerEntity(){
        return (ShortHandEntity) getArguments().getSerializable(KEY_SHORTHAND);
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

    /**********************************************IUtimerEditMvpV**********************************************/
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
        ToastUtils.showShort("onSaveEnd");
    }
}

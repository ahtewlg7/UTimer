package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.entity.md.EditMementoBean;
import ahtewlg7.utimer.factory.ShortHandFactory;
import ahtewlg7.utimer.mvp.ShorthandEditMvpP;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.EditLinerRecyclerView;
import butterknife.BindView;
import io.reactivex.Observable;

public class ShortHandEditFragment extends AEditFragment implements ShorthandEditMvpP.IShorthandEditMvpV,
        BaseQuickAdapter.OnItemChildClickListener {
    public static final String TAG = ShortHandEditFragment.class.getSimpleName();

    private static final String KEY_SHORTHAND = "shorthand";

    @BindView(R.id.fragment_shorthand_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_shorthand_edit_rv)
    EditLinerRecyclerView editReCyclerView;

    private ShortHandEntity shorthandEntity;
    private ShorthandEditMvpP editMvpP;

    public static ShortHandEditFragment newInstance(ShortHandEntity entity) {
        Bundle args = new Bundle();
        if(entity != null)
            args.putSerializable(KEY_SHORTHAND, entity);
        else
            args.putSerializable(KEY_SHORTHAND, ShortHandFactory.getInstance().newValue());
        ShortHandEditFragment fragment = new ShortHandEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        shorthandEntity = getShorthandEntity();

        if(shorthandEntity == null || !shorthandEntity.ifValid()){
            Logcat.i(TAG,"shorthandEntity no valid");
            ToastUtils.showShort(R.string.entity_invalid);
            pop();
            return ;
        }
        initByArgument();
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


    @Override
    protected void onEditMode(int index, Observable<String> rawTxtRx) {
        Logcat.i(TAG,"onEditMode " + index);
        editMvpP.toModify(index, rawTxtRx);
    }

    @Override
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
    }

    /**********************************************BaseQuickAdapter**********************************************/
    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view,final int position) {
        Logcat.i(TAG,"onItemChildClick position = " + position + ",preEditPosition = " + preEditPosition);
        /*if(preEditPosition != position){
            Optional<EditViewBean> editViewBeanOptional = onEditViewLock(preEditPosition);
            if(editViewBeanOptional.isPresent()){
                editViewBeanOptional.get().setEditing(false);
                onEditViewLock(editViewBeanOptional.get());
            }

            MdEditText currEditText   = (MdEditText) adapter.getViewByPosition(position, R.id.view_md_edit_tv);
            EditViewBean editViewBean = new EditViewBean(position, currEditText);
            editViewBean.setEditing(true);
            onEditViewLock(editViewBean);
            editViewPublishSubject.onNext(editViewBean);
            preEditPosition = position;
        }*/
    }
    /**********************************************IShorthandEditMvpV**********************************************/
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
                this ,null, null);
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
    public void onParseSucc(EditMementoBean mdMementoBean) {
        ToastUtils.showShort("parse end");
        preEditMementoBean = mdMementoBean;
        editReCyclerView.resetData(mdMementoBean.getIndex(), mdMementoBean.getElementList());
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
    public void onActionCancel() {

    }

    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }

    private void initByArgument() {
        editMvpP   = new ShorthandEditMvpP(shorthandEntity, this);
        editMvpP.toLoadTxt();
    }
    private ShortHandEntity getShorthandEntity(){
        return (ShortHandEntity) getArguments().getSerializable(KEY_SHORTHAND);
    }
}

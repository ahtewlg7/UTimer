package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Optional;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.entity.md.EditMementoBean;
import ahtewlg7.utimer.factory.ShortHandFactory;
import ahtewlg7.utimer.mvp.AUtimerEditMvpP;
import ahtewlg7.utimer.mvp.ShorthandEditMvpP;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.EditLinerRecyclerView;
import ahtewlg7.utimer.view.md.MdEditText;
import butterknife.BindView;

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
            args.putSerializable(KEY_SHORTHAND, ShortHandFactory.getInstance().createBean());
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
    protected boolean ifEnvOk() {
        shorthandEntity = getShorthandEntity();
        return shorthandEntity != null && shorthandEntity.ifValid();
    }

    @NonNull
    @Override
    protected AUtimerEditMvpP getEditMvpP() {
        if(editMvpP == null)
            editMvpP = new ShorthandEditMvpP(shorthandEntity, this);
        return editMvpP;
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
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        editMvpP.onEditViewClick(position);
    }
    /**********************************************IShorthandEditMvpV**********************************************/
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

    /**********************************************IShorthandEditMvpV**********************************************/
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
    /**********************************************IShorthandEditMvpV**********************************************/
    @Override
    public void onRestoreEnd(EditMementoBean MdMementoBean) {

    }
    /**********************************************IShorthandEditMvpV**********************************************/
    @Override
    public void onSaveStart() {

    }

    @Override
    public void onSaveErr(Throwable e) {

    }

    @Override
    public void onSaveEnd() {
    }
    /**********************************************IShorthandEditMvpV**********************************************/
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }


    private ShortHandEntity getShorthandEntity(){
        return (ShortHandEntity) getArguments().getSerializable(KEY_SHORTHAND);
    }
}

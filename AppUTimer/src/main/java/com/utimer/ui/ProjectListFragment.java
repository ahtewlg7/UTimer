package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;
import com.utimer.view.ProjectRecyclerView;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.mvp.ShortHandListMvpP;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;
import io.reactivex.Flowable;

public class ProjectListFragment extends AToolbarBkFragment implements ShortHandListMvpP.IShorthandListMvpV {
    public static final String TAG = ProjectListFragment.class.getSimpleName();

    public static final int INIT_POSITION = -1;

    public static final int REQ_NEW_FRAGMENT  = 100;
    public static final int REQ_EDIT_FRAGMENT = 101;

    @BindView(R.id.fragment_project_list_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_project_list_recycler_view)
    ProjectRecyclerView shorthandRecyclerView;

    private int editPosition = -1;
    private ShortHandListMvpP shortHandListMvpP;
    private MyClickListener myClickListener;

    public static ProjectListFragment newInstance() {
        Bundle args = new Bundle();

        ProjectListFragment fragment = new ProjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        myClickListener = new MyClickListener();

        /*shorthandRecyclerView.init(getContext(), null, myClickListener, null,myClickListener,null,null,null);
        shortHandListMvpP = new ShortHandListMvpP(this);*/
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
//        shortHandListMvpP.toLoadAllItem();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        /*if (resultCode == RESULT_OK && data != null) {
            ShortHandEntity shorthandEntity = (ShortHandEntity) data.getSerializable(ShortHandEditFragment.KEY_SHORTHAND);
            if (shorthandEntity != null && requestCode == REQ_NEW_FRAGMENT)
                onItemCreate(shorthandEntity);
            else if (shorthandEntity != null && requestCode == REQ_EDIT_FRAGMENT) {
                onItemEdit(shorthandEntity);
            }
        }*/
    }

    /**********************************************AToolbarBkFragment**********************************************/

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_project_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_project_list);
    }

    @Override
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
        Logcat.i(TAG, "onOptionsItemSelected " + item.getTitle());
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.tool_menu_add:
                Log.i(TAG, "to create project");
                toCreateNewDialog();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    /**********************************************IShorthandListMvpV**********************************************/
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }

    /**********************************************IShorthandListMvpV**********************************************/
    @Override
    public void onItemLoadStart() {
        Logcat.i(TAG, "onItemLoadStart");
    }

    @Override
    public void onItemLoad(ShortHandEntity data) {
//        Logcat.i(TAG, "onItemLoad " + data.toString());
    }

    @Override
    public void onItemLoadErr(Throwable t) {
        Logcat.i(TAG, "onItemLoadErr : " + t.getMessage());
    }

    @Override
    public void onItemLoadEnd(List<ShortHandEntity> alldata) {
        Logcat.i(TAG, "onItemLoadEnd");
        /*if(alldata != null)
            shorthandRecyclerView.resetData(alldata);*/
    }

    /**********************************************IShorthandListMvpV**********************************************/
    @Override
    public void onItemCreate(ShortHandEntity data) {
//        shortHandListMvpP.onItemCreated(data);
    }

    @Override
    public void onItemEdit(ShortHandEntity data) {
        /*if (editPosition != INIT_POSITION)
            shortHandListMvpP.onItemEdited(editPosition, data);
        editPosition = INIT_POSITION;*/
    }

    /**********************************************IShorthandListMvpV**********************************************/
    @Override
    public void resetView(List<ShortHandEntity> dataList) {
//        shorthandRecyclerView.resetData(dataList);
    }

    @Override
    public void resetView(int index, ShortHandEntity entity) {
//        shorthandRecyclerView.resetData(index, entity);
    }

    /**********************************************IShorthandListMvpV**********************************************/
    @Override
    public void onDeleteSucc(int index , ShortHandEntity entity) {
//        ToastUtils.showShort(R.string.prompt_del_succ);
//        shorthandRecyclerView.removeData(index);
    }

    @Override
    public void onDeleteFail(ShortHandEntity entity) {
//        ToastUtils.showShort(R.string.prompt_del_fail);
    }

    @Override
    public void onDeleteErr(Throwable throwable) {
//        Logcat.i(TAG,"onDeleteErr : " + throwable.getMessage());
//        ToastUtils.showShort(R.string.prompt_del_err);
    }

    @Override
    public void onDeleteEnd() {
    }

    /**********************************************IShorthandListMvpV**********************************************/

    class MyClickListener implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//            editPosition = position;
//            startForResult(ShortHandEditFragment.newInstance((ShortHandEntity) adapter.getData().get(position)), REQ_EDIT_FRAGMENT);
        }

        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
//            ShortHandEntity viewEntity = (ShortHandEntity)adapter.getItem(position);
//            if(viewEntity != null)
//                toCreateDelDialog(viewEntity);
            return false;
        }
    }
    private void toCreateNewDialog(){
        new MaterialDialog.Builder(getContext()).title(R.string.create)
            .customView(R.layout.view_new_project_name,false)
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
                    startForResult(ProjectFragment.newInstance(), REQ_NEW_FRAGMENT);
                }
            }).show();
    }
}

package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.gc.materialdesign.widgets.Dialog;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;
import com.utimer.entity.ShorthandSectionEntity;
import com.utimer.view.ShorthandRecyclerView;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.mvp.ShortHandListMvpP;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class ShortHandListFragment extends AToolbarBkFragment implements ShortHandListMvpP.IShorthandListMvpV {
    public static final String TAG = ShortHandListFragment.class.getSimpleName();

    public static final int INIT_POSITION = -1;

    public static final int REQ_NEW_FRAGMENT = 100;
    public static final int REQ_EDIT_FRAGMENT = 101;

    @BindView(R.id.fragment_shorthand_list_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_shorthand_list_recycler_view)
    ShorthandRecyclerView shorthandRecyclerView;

    private List<ShorthandSectionEntity> sectionEntityList;

    private int editPosition = -1;
    private ShortHandListMvpP shortHandListMvpP;
    private MyClickListener myClickListener;

    public static ShortHandListFragment newInstance() {
        Bundle args = new Bundle();

        ShortHandListFragment fragment = new ShortHandListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        sectionEntityList = Lists.newArrayList();
        myClickListener = new MyClickListener();

        shorthandRecyclerView.init(getContext(), sectionEntityList, myClickListener, null, myClickListener, null);
        shortHandListMvpP = new ShortHandListMvpP(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        shortHandListMvpP.toLoadAllItem();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            ShortHandEntity shorthandEntity = (ShortHandEntity) data.getSerializable(ShortHandEditFragment.KEY_SHORTHAND);
            if (shorthandEntity != null && requestCode == REQ_NEW_FRAGMENT)
                onItemCreate(shorthandEntity);
            else if (shorthandEntity != null && requestCode == REQ_EDIT_FRAGMENT) {
                onItemEdit(shorthandEntity);
            }
        }
    }

    /**********************************************AToolbarBkFragment**********************************************/

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_shorthand_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_shorthand_list);
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
                Log.i(TAG, "to create shorthand");
                startForResult(ShortHandEditFragment.newInstance(null), REQ_NEW_FRAGMENT);
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
        sectionEntityList.add(new ShorthandSectionEntity(data));
    }

    @Override
    public void onItemLoadErr(Throwable t) {
        Logcat.i(TAG, "onItemLoadErr : " + t.getMessage());
    }

    @Override
    public void onItemLoadEnd() {
        Logcat.i(TAG, "onItemLoadEnd");
        shorthandRecyclerView.resetData(sectionEntityList);
    }

    @Override
    public void onItemCreate(ShortHandEntity data) {
        sectionEntityList.add(new ShorthandSectionEntity(data));
        shorthandRecyclerView.resetData(sectionEntityList);
    }

    @Override
    public void onItemEdit(ShortHandEntity data) {
        if (editPosition != INIT_POSITION) {
            sectionEntityList.set(editPosition, new ShorthandSectionEntity(data));
            shorthandRecyclerView.resetData(sectionEntityList);
        }
        editPosition = INIT_POSITION;
    }

    /**********************************************IShorthandListMvpV**********************************************/

    @Override
    public void resetView(List<ShortHandEntity> dataList) {

    }

    @Override
    public void onDeleteSucc(ShortHandEntity entity) {

    }

    @Override
    public void onDeleteFail(ShortHandEntity entity) {

    }

    @Override
    public void onDeleteErr(Throwable throwable, ShortHandEntity entity) {

    }

    /**********************************************IShorthandListMvpV**********************************************/

    class MyClickListener implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            if (sectionEntityList.get(position) != null && !sectionEntityList.get(position).isHeader) {
                editPosition = position;
                startForResult(ShortHandEditFragment.newInstance(sectionEntityList.get(position).t), REQ_EDIT_FRAGMENT);
            }
        }

        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            Dialog dialog = new Dialog(getContext(), MyRInfo.getStringByID(R.string.del), MyRInfo.getStringByID(R.string.prompt_del));
            dialog.setOnAcceptButtonClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                }
            });
            dialog.show();
            return false;
        }
    }
}

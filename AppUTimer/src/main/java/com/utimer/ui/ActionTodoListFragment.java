package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;
import com.utimer.view.GtdActionRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.mvp.ActionMaybeListMvpP;
import ahtewlg7.utimer.mvp.ActionTodoListMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;
import io.reactivex.Flowable;

public class ActionTodoListFragment extends AToolbarBkFragment implements ActionMaybeListMvpP.IGtdActionListMvpV {
    public static final int INIT_POSITION = -1;

    public static final int REQ_NEW_FRAGMENT = 100;
    public static final int REQ_EDIT_FRAGMENT = 101;

    @BindView(R.id.fragment_gtd_action_list_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_gtd_action_list_recycler_view)
    GtdActionRecyclerView recyclerView;

    private int editIndex = -1;
    private ActionTodoListMvpP listMvpP;
    private MyClickListener myClickListener;

    public static ActionTodoListFragment newInstance() {
        Bundle args = new Bundle();

        ActionTodoListFragment fragment = new ActionTodoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        myClickListener = new MyClickListener();

        recyclerView.init(getContext(), null, myClickListener, null,myClickListener,null,null,null);
        listMvpP = new ActionTodoListMvpP(null);

        EventBusFatory.getInstance().getDefaultEventBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBusFatory.getInstance().getDefaultEventBus().unregister(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        listMvpP.toLoadAllItem();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            GtdActionEntity shorthandEntity = (GtdActionEntity) data.getSerializable(ShortHandEditFragment.KEY_SHORTHAND);
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
        return R.layout.fragment_gtd_action_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_action_list);
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
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.tool_menu_add:
                startForResult(ShortHandEditFragment.newInstance(null), REQ_NEW_FRAGMENT);
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void onItemLoadStart() {
    }

    @Override
    public void onItemLoad(GtdActionEntity data) {
    }

    @Override
    public void onItemLoadErr(Throwable t) {
    }

    @Override
    public void onItemLoadEnd(List<GtdActionEntity> alldata) {
        if(alldata != null)
            recyclerView.resetData(alldata);
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void onItemCreate(GtdActionEntity data) {
        listMvpP.onItemCreated(data);
    }

    @Override
    public void onItemEdit(GtdActionEntity data) {
        if (editIndex != INIT_POSITION)
            listMvpP.onItemEdited(editIndex, data);
        editIndex = INIT_POSITION;
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void resetView(List<GtdActionEntity> dataList) {
        recyclerView.resetData(dataList);
    }

    @Override
    public void resetView(int index, GtdActionEntity entity) {
        recyclerView.resetData(index, entity);
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void onDeleteSucc(int index , GtdActionEntity entity) {
        ToastUtils.showShort(R.string.prompt_del_succ);
        if(index == INVALID_INDEX)
            recyclerView.removeData(entity);
        else
            recyclerView.removeData(index);
    }

    @Override
    public void onDeleteFail(GtdActionEntity entity) {
        ToastUtils.showShort(R.string.prompt_del_fail);
    }

    @Override
    public void onDeleteErr(Throwable throwable) {
        ToastUtils.showShort(R.string.prompt_del_err);
    }

    @Override
    public void onDeleteEnd() {
    }

    /**********************************************EventBus**********************************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionBusEvent(ActionBusEvent eventBus) {
        listMvpP.toHandleActionEvent(eventBus);
    }
    /**********************************************IGtdActionListMvpV**********************************************/

    class MyClickListener implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            editIndex = position;
//            startForResult(ShortHandEditFragment.newInstance((GtdActionEntity) adapter.getData().get(position)), REQ_EDIT_FRAGMENT);
        }

        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            GtdActionEntity viewEntity = (GtdActionEntity)adapter.getItem(position);
            if(viewEntity != null)
                toCreateDelDialog(viewEntity);
            return false;
        }
    }
    private void toCreateDelDialog(final GtdActionEntity entity){
        new MaterialDialog.Builder(getContext()).title(R.string.del)
            .content(R.string.prompt_del)
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
                    listMvpP.toDeleteItem(Flowable.just(entity));
                }
            }).show();
    }


}
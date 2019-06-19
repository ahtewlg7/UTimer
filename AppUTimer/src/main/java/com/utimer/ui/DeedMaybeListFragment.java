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
import com.utimer.view.GtdDeedRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.mvp.DeedMaybeListMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;
import io.reactivex.Flowable;

import static com.utimer.common.Constants.REQ_EDIT_FRAGMENT;
import static com.utimer.common.Constants.REQ_NEW_FRAGMENT;

public class DeedMaybeListFragment extends AToolbarBkFragment implements DeedMaybeListMvpP.IGtdActionListMvpV {
    public static final int INIT_POSITION = -1;

    @BindView(R.id.fragment_gtd_deed_list_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_gtd_deed_list_recycler_view)
    GtdDeedRecyclerView recyclerView;

    private int editIndex = -1;
    private DeedMaybeListMvpP listMvpP;
    private MyClickListener myClickListener;

    public static DeedMaybeListFragment newInstance() {
        Bundle args = new Bundle();

        DeedMaybeListFragment fragment = new DeedMaybeListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        myClickListener = new MyClickListener();

        recyclerView.init(getContext(), null,
                myClickListener, null,
                myClickListener,null,
                null,null);
        listMvpP = new DeedMaybeListMvpP(this);

        EventBusFatory.getInstance().getDefaultEventBus().register(this);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*false means menu off; true means menu on*/
        setHasOptionsMenu(false);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBusFatory.getInstance().getDefaultEventBus().unregister(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        listMvpP.toLoadAllMaybe();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            GtdDeedEntity entity = (GtdDeedEntity) data.getSerializable(DeedEditFragment.KEY_ACTION);
            if (entity != null && requestCode == REQ_NEW_FRAGMENT)
                onItemCreate(entity);
            else if (entity != null && requestCode == REQ_EDIT_FRAGMENT) {
                onItemEdit(entity);
            }
        }
    }

    /**********************************************AToolbarBkFragment**********************************************/

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_gtd_deed_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_deed_maybe_list);
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
        /*switch (item.getItemId()) {
            case R.id.tool_menu_add:
                startForResult(ShortHandEditFragment.newInstance(null), REQ_NEW_FRAGMENT);
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }*/
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
    public void onItemLoad(GtdDeedEntity data) {
    }

    @Override
    public void onItemLoadErr(Throwable t) {
    }

    @Override
    public void onItemLoadEnd(List<GtdDeedEntity> alldata) {
        if(alldata != null)
            recyclerView.resetData(alldata);
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void onItemCreate(GtdDeedEntity data) {
        listMvpP.onItemCreated(data);
    }

    @Override
    public void onItemEdit(GtdDeedEntity data) {
        onDeleteSucc(INVALID_INDEX, data);
        editIndex = INIT_POSITION;
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void resetView(List<GtdDeedEntity> dataList) {
        recyclerView.resetData(dataList);
    }

    @Override
    public void resetView(int index, GtdDeedEntity entity) {
        recyclerView.resetData(index, entity);
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void onDeleteSucc(int index , GtdDeedEntity entity) {
        if(recyclerView == null)
            return;
        ToastUtils.showShort(R.string.prompt_del_succ);
        if(index == INVALID_INDEX)
            recyclerView.removeData(entity);
        else
            recyclerView.removeData(index);
    }

    @Override
    public void onDeleteFail(GtdDeedEntity entity) {
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
    public void onActionBusEvent(DeedBusEvent eventBus) {
        listMvpP.toHandleActionEvent(eventBus);
    }
    /**********************************************IGtdActionListMvpV**********************************************/

    class MyClickListener implements BaseQuickAdapter.OnItemClickListener,
            BaseQuickAdapter.OnItemLongClickListener{
        //+++++++++++++++++++++++++++++++++++OnItemClickListener+++++++++++++++++++++++++++++++
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            editIndex = position;
            GtdDeedEntity item = (GtdDeedEntity)adapter.getItem(position);
            item.setDeedState(DeedState.MAYBE);
            startForResult(DeedEditFragment.newInstance(item), REQ_EDIT_FRAGMENT);

        }
        //+++++++++++++++++++++++++++++++++++OnItemLongClickListener+++++++++++++++++++++++++++++++
        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            GtdDeedEntity viewEntity = (GtdDeedEntity)adapter.getItem(position);
            if(viewEntity != null)
                toCreateDelDialog(viewEntity);
            return false;
        }
    }
    private void toCreateDelDialog(final GtdDeedEntity entity){
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

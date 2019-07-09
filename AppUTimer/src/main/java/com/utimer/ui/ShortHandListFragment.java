package com.utimer.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.utimer.R;
import com.utimer.view.ShorthandRecyclerView;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.mvp.ShortHandListMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;
import io.reactivex.Flowable;

import static com.utimer.common.Constants.REQ_EDIT_FRAGMENT;
import static com.utimer.common.Constants.REQ_NEW_FRAGMENT;

public class ShortHandListFragment extends AToolbarBkFragment implements ShortHandListMvpP.IShorthandListMvpV {
    public static final int INIT_POSITION = -1;

    @BindView(R.id.fragment_shorthand_list_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_shorthand_list_recycler_view)
    ShorthandRecyclerView shorthandRecyclerView;

    private int editIndex = -1;
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

        myClickListener = new MyClickListener();

        shorthandRecyclerView.init(getContext(), null, myClickListener, null,myClickListener,null,null,null);
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

    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
        shortHandListMvpP.toLoadAllItem();
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
    public void onItemLoad(ShortHandEntity data) {
    }

    @Override
    public void onItemLoadErr(Throwable t) {
    }

    @Override
    public void onItemLoadEnd(List<ShortHandEntity> alldata) {
        if(alldata != null)
            shorthandRecyclerView.resetData(alldata);
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void onItemCreate(ShortHandEntity data) {
        shortHandListMvpP.onItemCreated(data);
    }

    @Override
    public void onItemEdit(ShortHandEntity data) {
        if (editIndex != INIT_POSITION)
            shortHandListMvpP.onItemEdited(editIndex, data);
        editIndex = INIT_POSITION;
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void resetView(List<ShortHandEntity> dataList) {
        shorthandRecyclerView.resetData(dataList);
    }

    @Override
    public void resetView(int index, ShortHandEntity entity) {
        shorthandRecyclerView.resetData(index, entity);
    }

    /**********************************************IGtdActionListMvpV**********************************************/
    @Override
    public void onDeleteSucc(int index , ShortHandEntity entity) {
        ToastUtils.showShort(R.string.prompt_del_succ);
        shorthandRecyclerView.removeData(index);
    }

    @Override
    public void onDeleteFail(ShortHandEntity entity) {
        ToastUtils.showShort(R.string.prompt_del_fail);
    }

    @Override
    public void onDeleteErr(Throwable throwable) {
        ToastUtils.showShort(R.string.prompt_del_err);
    }

    @Override
    public void onDeleteEnd() {
    }

    /**********************************************IGtdActionListMvpV**********************************************/

    class MyClickListener implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            editIndex = position;
            startForResult(ShortHandEditFragment.newInstance((ShortHandEntity) adapter.getData().get(position)), REQ_EDIT_FRAGMENT);
        }

        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            ShortHandEntity viewEntity = (ShortHandEntity)adapter.getItem(position);
            if(viewEntity != null)
                toCreateDelDialog(viewEntity);
            return false;
        }
    }
    private void toCreateDelDialog(final ShortHandEntity entity){
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
                    shortHandListMvpP.toDeleteItem(Flowable.just(entity));
                }
            }).show();
    }
}

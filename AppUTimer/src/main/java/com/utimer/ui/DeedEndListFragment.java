package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.utimer.R;
import com.utimer.entity.span.DeedSpanMoreTag;
import com.utimer.view.DeedTagBottomSheetDialog;
import com.utimer.view.SimpleDeedRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Arrays;
import java.util.List;

import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.span.TextClickableSpan;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static ahtewlg7.utimer.enumtype.DeedState.DONE;
import static ahtewlg7.utimer.enumtype.DeedState.TRASH;

public class DeedEndListFragment extends AButterKnifeFragment implements BaseDeedListMvpP.IBaseDeedMvpV {
    public static final int INIT_POSITION = -1;

    @BindView(R.id.fragment_deed_simple_list_recycler_view)
    SimpleDeedRecyclerView recyclerView;

    private int editIndex = -1;
    private DeedState[] workState;
    private BaseDeedListMvpP listMvpP;
    private MyClickListener myClickListener;
    private DeedTagBottomSheetDialog bottomSheetDialog;

    public static DeedEndListFragment newInstance() {
        Bundle args = new Bundle();

        DeedEndListFragment fragment = new DeedEndListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        workState = new DeedState[]{DONE, TRASH};
        myClickListener = new MyClickListener();

        recyclerView.init(getContext(), null,
                null, null,
                null,null,
                null,null,
                myClickListener);
        listMvpP = new BaseDeedListMvpP(this);

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
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            listMvpP.toLoadDeedByState(workState);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        /*if (resultCode == RESULT_OK && data != null) {
            GtdDeedEntity entity = (GtdDeedEntity) data.getSerializable(DeedEditFragment.KEY_ACTION);
            if (entity != null && requestCode == REQ_NEW_FRAGMENT)
                onItemCreate(entity);
            else if (entity != null && requestCode == REQ_EDIT_FRAGMENT) {
                onItemEdit(entity);
            }
        }*/
    }

    /**********************************************AToolbarBkFragment**********************************************/

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_deed_simple_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_deed_list_end);
    }

    /**********************************************IBaseDeedMvpV**********************************************/
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }

    @Override
    public void onLoadStart() {
    }

    @Override
    public void onLoadSucc(List<GtdDeedEntity> entityList) {
        if(entityList != null)
            recyclerView.resetData(entityList);
    }

    @Override
    public void onLoadErr(Throwable err) {
    }


    @Override
    public void onTagStart(GtdDeedEntity entity, DeedState toState) {
    }

    @Override
    public void onTagSucc(GtdDeedEntity entity, DeedState toState,int position) {
        if(toState != TRASH &&  Arrays.asList(workState).contains(toState))
            recyclerView.resetData(position, entity);
        else
            recyclerView.removeData(entity);
    }

    @Override
    public void onTagFail(GtdDeedEntity entity, DeedState toState) {
        ToastUtils.showShort(R.string.prompt_tag_fail);
        if(bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
    }

    @Override
    public void onTagErr(GtdDeedEntity entity, DeedState toState, Throwable err) {
        ToastUtils.showShort(R.string.prompt_tag_fail);
        if(bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
    }

    @Override
    public void onTagEnd(GtdDeedEntity entity, DeedState toState) {
        if(bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
    }

    /**********************************************IGtdActionListMvpV**********************************************//*
    @Override
    public void onItemCreate(GtdDeedEntity data) {
        listMvpP.onItemCreated(data);
    }

    @Override
    public void onItemEdit(GtdDeedEntity data) {
        onDeleteSucc(INVALID_INDEX, data);
        editIndex = INIT_POSITION;
    }

    *//**********************************************IGtdActionListMvpV**********************************************//*
    @Override
    public void resetView(List<GtdDeedEntity> dataList) {
        recyclerView.resetData(dataList);
    }

    @Override
    public void resetView(int index, GtdDeedEntity entity) {
        recyclerView.resetData(index, entity);
    }
    /**********************************************EventBus**********************************************/
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onActionBusEvent(DeedBusEvent eventBus) {
        listMvpP.toHandleActionEvent(eventBus, workState);
    }
    /**********************************************IGtdActionListMvpV**********************************************/

    class MyClickListener implements TextClickableSpan.ITextSpanClickListener,
            DeedTagBottomSheetDialog.OnItemClickListener {
        //+++++++++++++++++++++++++++++++++++ITextSpanClickListener+++++++++++++++++++++++++++++++
        @Override
        public void onSpanClick(int position, Object o) {
            /*if(o instanceof GtdDeedEntity)
                startForResult(DeedEditFragment.newInstance((GtdDeedEntity)o), REQ_EDIT_FRAGMENT);
            else */if(o instanceof DeedSpanMoreTag)
                createBottomSheet(position);
        }
        //+++++++++++++++++++++++++++++++++++OnItemClickListener+++++++++++++++++++++++++++++++
        @Override
        public void onTagClick(int position, DeedState deedState) {
            listMvpP.toTagDeed((GtdDeedEntity) recyclerView.getAdapter().getItem(position), deedState, position);
        }
    }

    private void createBottomSheet(int position){
        if(position < 0 || position >= recyclerView.getAdapter().getItemCount()) {
            Logcat.d("Warning","createBottomSheet failed");
            return;
        }
        if (bottomSheetDialog == null) {
            bottomSheetDialog = new DeedTagBottomSheetDialog(getContext());
            bottomSheetDialog.setOnItemClickListener(myClickListener);
        }
        GtdDeedEntity currEntity = (GtdDeedEntity)recyclerView.getAdapter().getItem(position);
        bottomSheetDialog.toShow(listMvpP.getNextState(currEntity), position);
    }
}

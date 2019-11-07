package com.utimer.ui;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.common.base.Optional;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.utimer.R;

import java.util.Arrays;
import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;

import static com.utimer.common.TagInfoFactory.INVALID_TAG_RID;

public abstract class ABaseDeedListFragment extends ADeedListFragment<GtdDeedEntity, BaseViewHolder>
    implements BaseDeedListMvpP.IBaseDeedMvpV {

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
            getRecyclerView().resetData(entityList);
    }

    @Override
    public void onLoadSucc(GtdDeedEntity entity) {
        List<GtdDeedEntity> entityList = getRecyclerView().getData();
        if(entity != null && !entityList.contains(entity)) {
            entityList.add(0, entity);
            getRecyclerView().resetData(entityList);
        }else{
            int index = entityList.indexOf(entity);
            getRecyclerView().resetData(index, entity);
        }
    }

    @Override
    public void onLoadErr(Throwable err) {
    }


    @Override
    public void onTagStart(GtdDeedEntity entity, DeedState toState, int position) {
    }

    @Override
    public void onTagSucc(GtdDeedEntity entity, DeedState toState, int position) {
        if(getLoadDeedState() != null && Arrays.asList(getLoadDeedState()).contains(toState))
            getRecyclerView().resetData(position, entity);
        else if(getLoadDeedState() != null)
            getRecyclerView().removeData(entity);
        int strRid = tagInfoFactory.getTagDetailRid(toState);
        if(strRid != INVALID_TAG_RID)
            ToastUtils.showShort(strRid);
    }

    @Override
    public void onTagFail(GtdDeedEntity entity, DeedState toState, int position) {
        ToastUtils.showShort(R.string.prompt_tag_fail);
    }

    @Override
    public void onTagErr(GtdDeedEntity entity, DeedState toState, int position, Throwable err) {
        ToastUtils.showShort(R.string.prompt_tag_fail);
        if(bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
    }

    @Override
    public void onTagEnd(GtdDeedEntity entity, DeedState toState, int position) {
        getRecyclerView().toHighLight(Optional.absent());
        if(bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
    }
}

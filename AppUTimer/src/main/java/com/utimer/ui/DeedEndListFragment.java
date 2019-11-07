package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.collect.Lists;
import com.utimer.R;
import com.utimer.mvp.EndDeedListMvpP;
import com.utimer.view.EndDeedRecyclerView;

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.view.BaseSectionEntity;
import ahtewlg7.utimer.enumtype.DATE_MONTH;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;
import io.reactivex.disposables.Disposable;

import static ahtewlg7.utimer.enumtype.DeedState.DONE;
import static ahtewlg7.utimer.enumtype.DeedState.TRASH;
import static com.utimer.common.TagInfoFactory.INVALID_TAG_RID;

public class DeedEndListFragment extends ABaseDeedSectionListFragment implements EndDeedListMvpP.IEndDeedMvpV {
    @BindView(R.id.fragment_deed_section_list_recycler_view)
    EndDeedRecyclerView recyclerView;

    private Disposable disposable;
    private DeedState[] workState;
    private List<BaseSectionEntity> showSectionList;

    public static DeedEndListFragment newInstance() {
        Bundle args = new Bundle();

        DeedEndListFragment fragment = new DeedEndListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        showLifeInfo        = false;
        workState           = new DeedState[]{DONE, TRASH};
        showSectionList     = Lists.newArrayList();
    }

    /**********************************************AToolbarBkFragment**********************************************/
    @Override
    public int getLayoutRid() {
        return R.layout.fragment_deed_section_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_deed_list_end);
    }

    /**********************************************IBaseDeedMvpV**********************************************/
    @Override
    public void onSectionLoad(BaseSectionEntity<GtdDeedEntity> sectionEntity) {
    }

    @Override
    public void onSectionLoadSucc() {
        List<BaseSectionEntity> sectionList = ((EndDeedListMvpP)listMvpP).getSectionEntity(DATE_MONTH.valueOf(DateTime.now().getMonthOfYear()));
        recyclerView.resetData(sectionList);
    }

    @Override
    public void onLoadStart() {
        showSectionList.clear();
    }

    @Override
    public void onLoadErr(Throwable err) {
        if(disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    /*@Override
    public void onTagSucc(GtdDeedEntity entity, DeedState toState, int position) {
        *//*if(toState != TRASH &&  Arrays.asList(workState).contains(toState))
            recyclerView.resetData(position, entity);
        else
            recyclerView.removeData(entity);*//*
        int strRid = tagInfoFactory.getTagDetailRid(toState);
        if(strRid != INVALID_TAG_RID)
            ToastUtils.showShort(strRid);
    }*/
    /**********************************************ADeedListFragment**********************************************/
    @NonNull
    @Override
    protected DeedState[] getLoadDeedState() {
        return workState;
    }

    @NonNull
    @Override
    protected EndDeedListMvpP getDeedMvpP() {
        return new EndDeedListMvpP(this);
    }

    @NonNull
    @Override
    protected EndDeedRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void toLoadDeedOnShow() {
        if(getLoadDeedState() != null)
            ((EndDeedListMvpP)listMvpP).toLoadDeedByWeek(false, getLoadDeedState());
    }
}

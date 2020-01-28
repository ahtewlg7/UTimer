package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.utimer.R;
import com.utimer.mvp.EndDeedListMvpP;
import com.utimer.view.EndDeedRecyclerView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.Arrays;
import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.view.EndDeedSectionEntity;
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
    private LocalDate currSelectedDate;
    private List<LocalDate> showDateList;
    private SectionItemClickListener sectionItemClickListener;

    public static DeedEndListFragment newInstance() {
        Bundle args = new Bundle();

        DeedEndListFragment fragment = new DeedEndListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        showLifeInfo    = false;
        workState       = new DeedState[]{DONE, TRASH};
        showDateList    = Lists.newArrayList();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(disposable != null && !disposable.isDisposed())
            disposable.dispose();
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
    public void onSectionLoad(EndDeedSectionEntity<GtdDeedEntity> sectionEntity) {
    }

    @Override
    public void onSectionLoadSucc() {
        toUpdateSectionView(new LocalDate(DateTime.now().getYear(),DateTime.now().getMonthOfYear(), 1));
    }

    @Override
    public void onLoadStart() {
        showDateList.clear();
    }

    @Override
    public void onLoadErr(Throwable err) {
        if(disposable != null && !disposable.isDisposed())
            disposable.dispose();
    }

    @Override
    public void onTagStart(EndDeedSectionEntity<GtdDeedEntity> sectionEntity, DeedState toState, int position) {
    }

    @Override
    public void onTagSucc(EndDeedSectionEntity<GtdDeedEntity> entity, DeedState toState, int position) {
        if(toState != TRASH &&  Arrays.asList(workState).contains(toState))
            recyclerView.resetData(position, entity);
        else
            recyclerView.removeData(position);
        int strRid = tagInfoFactory.getTagDetailRid(toState);
        if(strRid != INVALID_TAG_RID)
            ToastUtils.showShort(strRid);
    }

    @Override
    public void onTagFail(EndDeedSectionEntity<GtdDeedEntity> sectionEntity, DeedState toState, int position) {
        ToastUtils.showShort(R.string.prompt_tag_fail);
    }

    @Override
    public void onTagErr(EndDeedSectionEntity<GtdDeedEntity> sectionEntity, DeedState toState, int position, Throwable err) {
        ToastUtils.showShort(R.string.prompt_tag_fail);
        if(bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
    }

    @Override
    public void onTagEnd(EndDeedSectionEntity<GtdDeedEntity> sectionEntity, DeedState toState, int position) {
        getRecyclerView().toHighLight(Optional.absent());
        if(bottomSheetDialog.isShowing())
            bottomSheetDialog.dismiss();
    }
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
    protected void toInitRecyclerView(){
        if(sectionItemClickListener == null)
            sectionItemClickListener    = new SectionItemClickListener();
        getRecyclerView().init(getContext(),  1, null,
                sectionItemClickListener,sectionItemClickListener,
                null,null);
        getRecyclerView().setSpanner(this);
    }

    @Override
    protected void toLoadDeedOnShow() {
        ((EndDeedListMvpP)listMvpP).toLoadDeedByWeek(false, getLoadDeedState());
    }
    private void toUpdateSectionView(LocalDate localDate){
        if(localDate == null || localDate == currSelectedDate)
            return;
        currSelectedDate = localDate;
        showDateList.clear();
        showDateList.add(localDate);
        recyclerView.resetData(((EndDeedListMvpP) listMvpP).getAllSectionEntity(showDateList));
    }
    class SectionItemClickListener implements BaseQuickAdapter.OnItemClickListener,
            BaseQuickAdapter.OnItemChildClickListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
           EndDeedSectionEntity entity = (EndDeedSectionEntity)adapter.getItem(position);
           if(entity != null && entity.isHeader)
               toUpdateSectionView(entity.getLocalDate());
        }

        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            EndDeedSectionEntity entity = (EndDeedSectionEntity)adapter.getItem(position);
            if(entity != null && !entity.isHeader)
                createBottomSheet(position);
        }
    }
}

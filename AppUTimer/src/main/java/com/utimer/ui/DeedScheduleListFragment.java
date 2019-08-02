package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.utimer.R;
import com.utimer.mvp.ScheduleDeedListMvpP;
import com.utimer.view.SimpleDeedRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;

import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static com.utimer.common.TagInfoFactory.INVALID_TAG_RID;

public class DeedScheduleListFragment extends ADeedListFragment
        implements ScheduleDeedListMvpP.IScheduleMvpV,CalendarView.OnCalendarSelectListener {
    @BindView(R.id.fragment_deed_calendar_calendarLayout)
    CalendarLayout mCalendarLayout;

    @BindView(R.id.fragment_deed_calendar_calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.fragment_deed_calendar_simple_list_recycler_view)
    SimpleDeedRecyclerView recyclerView;

    private List<GtdDeedEntity> deedEntityList;
    private Map<String, Calendar> deedCalendarMap;

    public static DeedScheduleListFragment newInstance() {
        Bundle args = new Bundle();

        DeedScheduleListFragment fragment = new DeedScheduleListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        mCalendarView.setWeekStarWithMon();
        mCalendarView.scrollToCurrent();
        mCalendarView.setOnCalendarSelectListener(this);

        showLifeInfo            = false;
        deedCalendarMap         = Maps.newHashMap();
        deedEntityList          = Lists.newArrayList();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        ((ScheduleDeedListMvpP)listMvpP).toLoadScheduleDate();
    }
    /**********************************************AToolbarBkFragment**********************************************/

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_deed_calendar;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_deed_list_schedule);
    }
    /**********************************************ADeedListFragment**********************************************/
    @Override
    protected DeedState[] getLoadDeedState() {
        return null;
    }

    @NonNull
    @Override
    protected BaseDeedListMvpP getDeedMvpP() {
        return new ScheduleDeedListMvpP(this);
    }

    @NonNull
    @Override
    protected SimpleDeedRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDeedDoneBusEvent(DeedDoneBusEvent eventBus) {
        listMvpP.toHandleBusEvent(eventBus, getLoadDeedState());
    }

    /**********************************************OnCalendarSelectListener**********************************************/
    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        deedEntityList.clear();
        ((ScheduleDeedListMvpP)listMvpP).toLoadDeedByDate(calendar);
    }
    /**********************************************IBaseDeedMvpV**********************************************/
    @Override
    public Calendar getCurrCalendar() {
        return mCalendarView.getSelectedCalendar();
    }

    @Override
    public void onLoadSucc(List<GtdDeedEntity> entityList) {
        deedEntityList.addAll(entityList);
        getRecyclerView().resetData(Lists.newArrayList(Sets.newLinkedHashSet(deedEntityList)));
    }

    @Override
    public void onLoadSucc(GtdDeedEntity entity) {
        deedEntityList.add(0, entity);
        getRecyclerView().resetData(Lists.newArrayList(Sets.newLinkedHashSet(deedEntityList)));
    }

    @Override
    public void onTagSucc(GtdDeedEntity entity, DeedState toState, int position) {
        if(toState == DeedState.TRASH && !GtdDeedByUuidFactory.getInstance().ifExist(entity)){
            deedEntityList.remove(entity);
            getRecyclerView().removeData(entity);
        } else
            getRecyclerView().resetData(position, entity);
        int strRid = tagInfoFactory.getTagDetailRid(toState);
        if(strRid != INVALID_TAG_RID)
            ToastUtils.showShort(strRid);
    }
    /**********************************************IScheduleMvpV**********************************************/
    @Override
    public void onScheduleDateLoadStart() {
        deedCalendarMap.clear();
    }

    @Override
    public void onScheduleDateAdd(Calendar calendar, boolean add) {
        if(!deedCalendarMap.containsValue(calendar) && add)
            deedCalendarMap.put(calendar.toString(), calendar);
        else if(!add)
            deedCalendarMap.remove(calendar.toString());
    }

    @Override
    public void onScheduleDateLoadErr(Throwable err) {
    }

    @Override
    public void onScheduleDateLoadSucc() {
        mCalendarView.setSchemeDate(deedCalendarMap);
        if(mCalendarView.getSelectedCalendar() != null)
            ((ScheduleDeedListMvpP)listMvpP).toLoadDeedByDate(mCalendarView.getSelectedCalendar());
    }

    @Override
    protected void toLoadDeedOnShow() {
        mCalendarView.scrollToCurrent();
        listMvpP.toLoadDeedByDate(LocalDate.now());
    }
}

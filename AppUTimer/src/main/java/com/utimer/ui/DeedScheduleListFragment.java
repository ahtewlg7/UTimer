package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.utimer.R;
import com.utimer.mvp.ScheduleDeedListMvpP;
import com.utimer.view.SimpleDeedRecyclerView;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static ahtewlg7.utimer.enumtype.DeedState.SCHEDULE;

public class DeedScheduleListFragment extends ADeedListFragment
        implements ScheduleDeedListMvpP.IScheduleMvpV,CalendarView.OnCalendarSelectListener {
    @BindView(R.id.fragment_deed_calendar_calendarLayout)
    CalendarLayout mCalendarLayout;

    @BindView(R.id.fragment_deed_calendar_calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.fragment_deed_calendar_simple_list_recycler_view)
    SimpleDeedRecyclerView recyclerView;

    private DeedState[] workState;
    private Multimap<LocalDate, GtdDeedEntity> dateDeedMap;

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

        showLifeInfo    = false;
        workState       = new DeedState[]{SCHEDULE};
        dateDeedMap     = LinkedHashMultimap.create();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        listMvpP.toLoadDeedByState(workState);
        ((ScheduleDeedListMvpP)listMvpP).toLoadScheduleDate();
    }
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden)
            listMvpP.toLoadDeedByDate(DateTime.now().toLocalDate());
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
    @NonNull
    @Override
    protected DeedState[] getLoadDeedState() {
        return workState;
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

    @Override
    public void onLoadSucc(List<GtdDeedEntity> entityList) {
        super.onLoadSucc(entityList);
    }
    /**********************************************OnCalendarSelectListener**********************************************/
    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        LocalDate localDate = new LocalDate(calendar.getYear(), calendar.getMonth(), calendar.getDay());
        listMvpP.toLoadDeedByDate(localDate);
    }

    /**********************************************IScheduleMvpV**********************************************/
    @Override
    public void onScheduleDateGetStart() {

    }

    @Override
    public void onScheduleDateGetSucc(Map<String, Calendar> calendarMap) {
        mCalendarView.setSchemeDate(calendarMap);
        listMvpP.toLoadDeedByDate(DateTime.now().toLocalDate());
    }

    @Override
    public void onScheduleDateGetErr(Throwable err) {

    }
}

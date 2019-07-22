package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.common.collect.Maps;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.utimer.R;
import com.utimer.common.CalendarSchemeFactory;
import com.utimer.mvp.ScheduleDeedListMvpP;
import com.utimer.view.SimpleDeedRecyclerView;

import org.joda.time.LocalDate;

import java.util.List;
import java.util.Map;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static ahtewlg7.utimer.enumtype.DeedState.INBOX;
import static ahtewlg7.utimer.enumtype.DeedState.MAYBE;
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
    private Map<String, Calendar> deedCalendarMap;
    private CalendarSchemeFactory calendarSchemeFactory;

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
        workState               = new DeedState[]{SCHEDULE,INBOX, MAYBE};
        calendarSchemeFactory   = new CalendarSchemeFactory();
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
            listMvpP.toLoadDeedByDate(LocalDate.now());
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
        listMvpP.toLoadDeedByDate(calendarSchemeFactory.getLocalDate(calendar));
    }

    /**********************************************IScheduleMvpV**********************************************/
    @Override
    public void onScheduleDateLoadStart() {
        deedCalendarMap.clear();
    }

    @Override
    public void onScheduleDateLoadSucc() {
        mCalendarView.setSchemeDate(deedCalendarMap);
        if(mCalendarView.getSelectedCalendar() == null)
            listMvpP.toLoadDeedByDate(LocalDate.now());
        else
            listMvpP.toLoadDeedByDate(calendarSchemeFactory.getLocalDate(mCalendarView.getSelectedCalendar()));
    }

    @Override
    public void onScheduleDateLoadErr(Throwable err) {
    }

    @Override
    public void onScheduleDateAdd(Calendar calendar) {
        if(!deedCalendarMap.containsValue(calendar))
            deedCalendarMap.put(calendar.toString(), calendar);
    }
}

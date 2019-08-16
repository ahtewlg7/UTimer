package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.utimer.R;
import com.utimer.common.CalendarSchemeFactory;
import com.utimer.mvp.ScheduleDeedListMvpP;
import com.utimer.view.SimpleDeedRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.LocalDate;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.DeedSchemeEntity;
import ahtewlg7.utimer.entity.gtd.DeedSchemeInfo;
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
        deedEntityList          = Lists.newArrayList();
        calendarSchemeFactory   = new CalendarSchemeFactory();
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
        listMvpP.toLoadDeedByDate(calendarSchemeFactory.getLocalDate(calendar));
    }
    /**********************************************IBaseDeedMvpV**********************************************/
    @Override
    public Optional<Boolean> ifAtSelectedDay(LocalDate localDate) {
        Optional<Boolean> ifAtSelectedDay = mCalendarView.getSelectedCalendar() == null ? Optional.absent() : Optional.of(false);
        if(ifAtSelectedDay.isPresent() && localDate != null)
            ifAtSelectedDay = Optional.of(mCalendarView.getSelectedCalendar().equals(calendarSchemeFactory.getCalendar(localDate)));

        return ifAtSelectedDay;
    }

    @Override
    public void onLoadSucc(List<GtdDeedEntity> entityList) {
        deedEntityList.addAll(entityList);
        getRecyclerView().resetData(Lists.newArrayList(Sets.newLinkedHashSet(deedEntityList)));
    }

    @Override
    public void onLoadSucc(GtdDeedEntity entity) {
        if(!deedEntityList.contains(entity)) {
            deedEntityList.add(0, entity);
            getRecyclerView().resetData(Lists.newArrayList(Sets.newLinkedHashSet(deedEntityList)));
        }
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
    public void onSchemeLoadStart() {
    }

    @Override
    public void onSchemeLoadSucc(DeedSchemeInfo schemeInfo) {
        Calendar calendar = getSchemeCalendar(schemeInfo);
        if(calendar.getSchemes() == null || calendar.getSchemes().isEmpty())
            mCalendarView.removeSchemeDate(calendar);
        else
            mCalendarView.addSchemeDate(calendar);
    }

    @Override
    public void onSchemeLoadErr(Throwable err) {
    }

    @Override
    public void onSchemeLoadEnd() {
        if(mCalendarView.getSelectedCalendar() != null)
            listMvpP.toLoadDeedByDate(calendarSchemeFactory.getLocalDate(mCalendarView.getSelectedCalendar()));
    }

    @Override
    protected void toLoadDeedOnShow() {
        mCalendarView.scrollToCurrent();
        listMvpP.toLoadDeedByDate(LocalDate.now());
    }
    public Calendar getSchemeCalendar(@NonNull DeedSchemeInfo schemeInfo){
        Calendar calendar = calendarSchemeFactory.getCalendar(schemeInfo.getLocalDate());
        if(schemeInfo.getDeedSchemeEntityList() == null)
            return calendar;
        for(DeedSchemeEntity deedSchemeEntity : schemeInfo.getDeedSchemeEntityList()){
            Calendar.Scheme scheme = new Calendar.Scheme();
            scheme.setShcemeColor(MyRInfo.getColorByID(R.color.colorAccent));
            Optional<String> schemeJson = calendarSchemeFactory.toJsonStr(deedSchemeEntity);
            if (schemeJson.isPresent())
                scheme.setScheme(schemeJson.get());
            calendar.addScheme(scheme);
        }
        return calendar;
    }
}

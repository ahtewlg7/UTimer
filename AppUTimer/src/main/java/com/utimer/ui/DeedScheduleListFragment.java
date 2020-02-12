package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.ColorRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.blankj.utilcode.util.ToastUtils;
import com.google.common.base.Optional;
import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Table;
import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.utimer.R;
import com.utimer.common.CalendarSchemeFactory;
import com.utimer.mvp.ScheduleDeedListMvpP;
import com.utimer.view.SimpleDeedRecyclerView;

import org.joda.time.LocalDate;
import org.reactivestreams.Subscription;

import java.util.Collection;
import java.util.List;

import ahtewlg7.utimer.entity.gtd.DeedSchemeEntity;
import ahtewlg7.utimer.entity.gtd.DeedSchemeInfo;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.span.SimpleMultiSpanTag;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.factory.DeedSchemeEntityFactory;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.AscII;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.util.MySafeSubscriber;
import ahtewlg7.utimer.util.TableAction;
import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

import static ahtewlg7.utimer.entity.gtd.DeedSchemeEntity.INVALID_PROGRESS;
import static com.utimer.common.TagInfoFactory.INVALID_TAG_RID;

public class DeedScheduleListFragment extends ABaseDeedListFragment
        implements ScheduleDeedListMvpP.IScheduleMvpV,CalendarView.OnCalendarSelectListener {

    @BindView(R.id.fragment_deed_calendar_calendarLayout)
    CalendarLayout mCalendarLayout;

    @BindView(R.id.fragment_deed_calendar_calendarView)
    CalendarView mCalendarView;

    @BindView(R.id.fragment_deed_calendar_simple_list_recycler_view)
    SimpleDeedRecyclerView recyclerView;

    private List<GtdDeedEntity> deedEntityList;
    private Table<LocalDate, String, DeedSchemeEntity> calendarDeedSchemeTodoTable;

    private CalendarSchemeFactory calendarSchemeFactory;
    private TableAction<LocalDate, String, DeedSchemeEntity> tableAction;

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

        showLifeInfo                = false;
        deedEntityList              = Lists.newArrayList();
        calendarDeedSchemeTodoTable = HashBasedTable.create();
        calendarSchemeFactory       = new CalendarSchemeFactory();
        tableAction                 = new TableAction<LocalDate, String, DeedSchemeEntity>(calendarDeedSchemeTodoTable);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        ((ScheduleDeedListMvpP)listMvpP).toLoadScheduleDate();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if(!hidden){
            mCalendarView.updateCurrentDate();
            mCalendarView.update();
        }
        super.onHiddenChanged(hidden);
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

    @Override
    protected void onDeedClick(int position) {
        GtdDeedEntity deedEntity = (GtdDeedEntity)recyclerView.getAdapter().getItem(position);
        if(deedEntity == null || deedEntity.getDeedState() == DeedState.DONE || deedEntity.getDeedState() == DeedState.TRASH)
            return;
        toCreateEditDialog(deedEntity);
    }

    /**********************************************OnCalendarSelectListener**********************************************/
    @Override
    public void onCalendarOutOfRange(Calendar calendar) {
    }

    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
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
    public void onLoadStart() {
        super.onLoadStart();
        deedEntityList.clear();
    }

    @Override
    public void onLoadSucc(List<GtdDeedEntity> entityList) {
        deedEntityList.addAll(entityList);
        getRecyclerView().resetData(Lists.newArrayList(Sets.newLinkedHashSet(deedEntityList)));
    }

    @Override
    public void onLoadSucc(GtdDeedEntity entity) {
        toAddDeedScheme(entity);
        if(!deedEntityList.contains(entity)) {
            deedEntityList.add(0, entity);
            getRecyclerView().resetData(Lists.newArrayList(Sets.newLinkedHashSet(deedEntityList)));
        }else{
            getRecyclerView().notifyDataSetChanged();
        }
    }

    @Override
    public void onTagSucc(GtdDeedEntity entity, DeedState toState, int position) {
        if(toState == DeedState.TRASH && !GtdDeedByUuidFactory.getInstance().ifExist(entity)){
            deedEntityList.remove(entity);
            getRecyclerView().removeData(entity);
        } else {
            toAddDeedScheme(entity);
            getRecyclerView().resetData(position, entity);
        }
        int strRid = tagInfoFactory.getTagDetailRid(toState);
        if(strRid != INVALID_TAG_RID)
            ToastUtils.showShort(strRid);
    }

    /**********************************************IScheduleMvpV**********************************************/
    @Override
    public void onSchemeLoadStart() {
        tableAction.clearAll();
        mCalendarView.clearSchemeDate();
    }

    @Override
    public void onSchemeLoadSucc(DeedSchemeInfo schemeInfo) {
        Calendar calendar = handleScheme(schemeInfo);
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
        Flowable.fromIterable(tableAction.getValue())
            .doOnNext(new Consumer<DeedSchemeEntity>() {
                @Override
                public void accept(DeedSchemeEntity deedSchemeEntity) throws Exception {
                    if(deedSchemeEntity.getProgress() != INVALID_PROGRESS && deedSchemeEntity.getProgress() < 100)
                        listMvpP.toUpdateScheme(deedSchemeEntity);
                }
            })
            .subscribeOn(Schedulers.computation())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<DeedSchemeEntity>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    mCalendarView.scrollToCurrent();
                }
                @Override
                public void onComplete() {
                    super.onComplete();
                    mCalendarView.update();
                    if(mCalendarView.getSelectedCalendar() != null)
                        listMvpP.toLoadDeedByDate(calendarSchemeFactory.getLocalDate(mCalendarView.getSelectedCalendar()));
                }
            });
    }

    @Override
    protected SimpleMultiSpanTag getTagInfo(@NonNull GtdDeedEntity item){
        SimpleMultiSpanTag multiSpanTag = new SimpleMultiSpanTag();
        char currTagAscii = tagInfoFactory.getTagIconAscii(item.getDeedState());
        multiSpanTag.appendTag(String.valueOf(currTagAscii));

        if(item.getDeedState() == DeedState.SCHEDULE) {
            LocalDate selectedDate = calendarSchemeFactory.getLocalDate(mCalendarView.getSelectedCalendar());
            if (tableAction.contain(selectedDate, item.getUuid())) {
                DeedSchemeEntity schemeEntity = tableAction.getValue(selectedDate, item.getUuid());
                if (schemeEntity != null && schemeEntity.getProgress() != INVALID_PROGRESS)
                    multiSpanTag.appendTag(String.valueOf(schemeEntity.getProgress()) + AscII.PercentSign());
            }
        }
        if(item.getWorkDateLifeDetail() != null && showLifeInfo)
            multiSpanTag.appendTag(item.getWorkDateLifeDetail());
        return multiSpanTag;
    }

    @Override
    protected @ColorRes int getSpanColor(@NonNull GtdDeedEntity item, @ColorRes int defaultColor){
        int color = defaultColor;
        LocalDate selectedDate = calendarSchemeFactory.getLocalDate(mCalendarView.getSelectedCalendar());
        if(tableAction.contain(selectedDate, item.getUuid()) && tableAction.getValue(selectedDate, item.getUuid()).getProgress() != INVALID_PROGRESS)
            color = R.color.colorAccent;
        else if(item.getDeedState() == DeedState.DONE || item.getDeedState() == DeedState.TRASH || item.getDeedState() == DeedState.USELESS)
            color = R.color.color_stand_c5;
        return color;
    }

    private Calendar handleScheme(@NonNull DeedSchemeInfo schemeInfo){
        Calendar calendar = calendarSchemeFactory.getCalendar(schemeInfo.getLocalDate());
        if(schemeInfo.getDeedSchemeEntityList() == null)
            return calendar;
        for(int index = 0; index < schemeInfo.getDeedSchemeEntityList().size() ; index++){
            DeedSchemeEntity deedSchemeEntity = schemeInfo.getDeedSchemeEntityList().get(index);
            if(deedSchemeEntity.getProgress() != INVALID_PROGRESS) {
                listMvpP.toUpdateScheme(deedSchemeEntity);
                tableAction.putValue(schemeInfo.getLocalDate(), deedSchemeEntity.getUuid(), deedSchemeEntity);
            }
            Calendar.Scheme scheme = new Calendar.Scheme();
            scheme.setShcemeColor(MyRInfo.getColorByID(R.color.colorAccent));
            Optional<String> schemeJson = calendarSchemeFactory.toJsonStr(deedSchemeEntity);
            if (schemeJson.isPresent())
                scheme.setScheme(schemeJson.get());
            calendar.addScheme(scheme);
        }
        return calendar;
    }
    private void toAddDeedScheme(GtdDeedEntity deedEntity){
        if(deedEntity == null || !deedEntity.ifValid()
                || deedEntity.getDeedState() == DeedState.TRASH || deedEntity.getDeedState() == DeedState.USELESS)
            return;
        Collection<DeedSchemeEntity> deedSchemeEntities = DeedSchemeEntityFactory.getInstacne().getDeedScheme(deedEntity);
        for(DeedSchemeEntity deedSchemeEntity : deedSchemeEntities){
            if(!tableAction.containsValue(deedSchemeEntity))
                tableAction.putValue(deedSchemeEntity.getDateTime().toLocalDate(), deedSchemeEntity.getUuid(), deedSchemeEntity);
        }
    }

    @Override
    protected void onMediaClick(int position){
        GtdDeedEntity deedEntity = (GtdDeedEntity)recyclerView.getAdapter().getItem(position);
        if(deedEntity != null)
            toOpenMedia(deedEntity);
    }
}

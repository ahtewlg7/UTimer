package com.utimer.mvp;


import android.graphics.Color;

import com.google.common.base.Optional;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.haibin.calendarview.Calendar;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;
import com.utimer.R;
import com.utimer.common.CalendarSchemeFactory;
import com.utimer.entity.CalendarSchemeInfo;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.joda.time.LocalDate;
import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DateLife;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2019/7/19.
 */
public class ScheduleDeedListMvpP extends BaseDeedListMvpP {
    private CalendarSchemeFactory calendarSchemeFactory;
    private DateTimeAction dateTimeAction;

    public ScheduleDeedListMvpP(IScheduleMvpV mvpV) {
        super(mvpV);
        calendarSchemeFactory = new CalendarSchemeFactory();
        dateTimeAction        = new DateTimeAction();
    }
    public void toLoadScheduleDate(){
        mvpM.loadScheduleDate()
            .map(new Function<LocalDate, Calendar>() {
                @Override
                public Calendar apply(LocalDate localDate) throws Exception {
                    Calendar calendar = calendarSchemeFactory.getCalendar(localDate);
                    calendar.setSchemeColor(Color.GREEN);//如果单独标记颜色、则会使用这个颜色
                    Optional<String> scheme = getSchemeJson();
                    if(scheme.isPresent())
                        calendar.setScheme(scheme.get());
                    return calendar;
                }
            })
            .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<Calendar>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Calendar>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onScheduleDateLoadStart();
                }

                @Override
                public void onNext(Calendar entity) {
                    super.onNext(entity);
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onScheduleDateAdd(entity, true);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onScheduleDateLoadErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onScheduleDateLoadSucc();
                }
            });
    }
    @Override
    public void toLoadDeedByState(final DeedState... deedState){
        toLoad(mvpM.toLoad(deedState).map(new Function<List<GtdDeedEntity>, List<GtdDeedEntity>>() {
            @Override
            public List<GtdDeedEntity> apply(List<GtdDeedEntity> gtdDeedEntities) throws Exception {
                return Lists.newArrayList(Collections2.filter(gtdDeedEntities, new Predicate<GtdDeedEntity>() {
                    @Override
                    public boolean apply(@NullableDecl GtdDeedEntity input) {
                        return ifLoadInCalendar(input);
                    }
                }));
            }
        }));
    }
    @Override
    public void toLoadDeedByDate(final LocalDate... localDates){
        toLoad(mvpM.toLoad(localDates).map(new Function<List<GtdDeedEntity>, List<GtdDeedEntity>>() {
            @Override
            public List<GtdDeedEntity> apply(List<GtdDeedEntity> gtdDeedEntities) throws Exception {
                return Lists.newArrayList(Collections2.filter(gtdDeedEntities, new Predicate<GtdDeedEntity>() {
                    @Override
                    public boolean apply(@NullableDecl GtdDeedEntity input) {
                        return input.ifValid() && input.getDeedState() != DeedState.TRASH;
                    }
                }));
            }
        }));
    }

    @Override
    public void toHandleBusEvent(DeedDoneBusEvent busEvent, DeedState... state){
        if(busEvent == null || !busEvent.ifValid() || busEvent.getEventType() != GtdBusEventType.SAVE)
            return;
        if(mvpV != null && busEvent.getDeedEntity().getFirstWorkTime().isPresent()) {
            LocalDate localDate = busEvent.getDeedEntity().getFirstWorkTime().get().toLocalDate();
            ((IScheduleMvpV) mvpV).onScheduleDateAdd(calendarSchemeFactory.getCalendar(localDate), busEvent.getDeedEntity().getDeedState() != DeedState.TRASH);
            ((IScheduleMvpV) mvpV).onScheduleDateLoadSucc();
        }else if(ifLoadInCalendar(busEvent.getDeedEntity())){
            mvpV.onLoadSucc(busEvent.getDeedEntity());
        }
    }
    private Optional<String> getSchemeJson(){
        CalendarSchemeInfo schemeInfo = new CalendarSchemeInfo();
        schemeInfo.setTip(MyRInfo.getStringByID(R.string.title_calendar_scheme));
        return new CalendarSchemeFactory().toJsonStr(schemeInfo);
    }
    private boolean ifLoadInCalendar(GtdDeedEntity input){
        if(input == null || !input.ifValid())
            return false;
        boolean result = false;
        switch (input.getDeedState()){
            case MAYBE:
            case INBOX:
                result = input.getCreateDateLife() == DateLife.TODAY;
                break;
            case SCHEDULE:
                result = dateTimeAction.isIn24Hour(input.getScheduleDate());
                break;
        }
        return result;
    }
    public interface IScheduleMvpV extends IBaseDeedMvpV{
        public void onScheduleDateLoadStart();
        public void onScheduleDateLoadSucc();
        public void onScheduleDateLoadErr(Throwable err);
        public void onScheduleDateAdd(Calendar calendar, boolean add);
    }
}

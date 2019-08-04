package com.utimer.mvp;


import com.google.common.base.Optional;
import com.haibin.calendarview.Calendar;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;
import com.utimer.R;
import com.utimer.common.CalendarSchemeFactory;
import com.utimer.entity.CalendarSchemeInfo;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2019/7/19.
 */
public class ScheduleDeedListMvpP extends BaseDeedListMvpP {
    private CalendarSchemeFactory calendarSchemeFactory;

    public ScheduleDeedListMvpP(IScheduleMvpV mvpV) {
        super(mvpV);
        calendarSchemeFactory = new CalendarSchemeFactory();
    }
    public void toLoadScheduleDate(){
        mvpM.loadCatalogueDate()
            .map(new Function<LocalDate, Calendar>() {
                @Override
                public Calendar apply(LocalDate localDate) throws Exception {
                    return getSchemeCalendar(localDate);
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
                public void onNext(Calendar calendar) {
                    super.onNext(calendar);
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onScheduleDateAdd(calendar, true);
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

    public void toLoadDeedByDate(final Calendar calendars){
        toLoadDeedByDate(calendarSchemeFactory.getLocalDate(calendars));
    }

    @Override
    public void toHandleBusEvent(DeedDoneBusEvent busEvent, DeedState... state){
        if(mvpV == null || busEvent == null || !busEvent.ifValid() || busEvent.getEventType() != GtdBusEventType.SAVE)
            return;
        Calendar currCalendar = ((IScheduleMvpV) mvpV).getCurrCalendar();

        Optional<Boolean> ifAtCurrCalendar = currCalendar == null ? Optional.absent() : Optional.of(false);
        DateTime workDateTime = busEvent.getDeedEntity().getWorkTime();
        if(ifAtCurrCalendar.isPresent() && workDateTime != null)
            ifAtCurrCalendar = Optional.of(currCalendar.equals(calendarSchemeFactory.getCalendar(workDateTime.toLocalDate())));

        List<DateTime> warningDateTime = busEvent.getDeedEntity().getWarningTimeList();
        if(warningDateTime != null)
            for(DateTime date : warningDateTime) {
                ((IScheduleMvpV) mvpV).onScheduleDateAdd(getSchemeCalendar(date.toLocalDate()), GtdDeedByUuidFactory.getInstance().getCatalogueDeedNum(date.toLocalDate()) > 0);
                if(ifAtCurrCalendar.isPresent() && !ifAtCurrCalendar.get())
                    ifAtCurrCalendar = Optional.of(currCalendar.equals(calendarSchemeFactory.getCalendar(date.toLocalDate())));
            }
        if(ifAtCurrCalendar.isPresent() && ifAtCurrCalendar.get())
            mvpV.onLoadSucc(busEvent.getDeedEntity());
    }
    private Calendar getSchemeCalendar(LocalDate localDate){
        Calendar calendar = calendarSchemeFactory.getCalendar(localDate);
        calendar.setSchemeColor(MyRInfo.getColorByID(R.color.colorAccent));//如果单独标记颜色、则会使用这个颜色

        CalendarSchemeInfo schemeInfo = new CalendarSchemeInfo();
        schemeInfo.setTip(MyRInfo.getStringByID(R.string.title_calendar_scheme));
        Optional<String> scheme = calendarSchemeFactory.toJsonStr(schemeInfo);
        if(scheme.isPresent())
            calendar.setScheme(scheme.get());
        return calendar;
    }
    public interface IScheduleMvpV extends IBaseDeedMvpV{
        public void onScheduleDateLoadStart();
        public void onScheduleDateAdd(Calendar calendar, boolean add);
        public void onScheduleDateLoadErr(Throwable err);
        public void onScheduleDateLoadSucc();

        public Calendar getCurrCalendar();
    }
}

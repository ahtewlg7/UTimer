package com.utimer.mvp;


import android.graphics.Color;

import com.google.common.collect.Maps;
import com.haibin.calendarview.Calendar;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.joda.time.LocalDate;
import org.reactivestreams.Subscription;

import java.util.Map;

import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2019/7/19.
 */
public class ScheduleDeedListMvpP extends BaseDeedListMvpP {
    public ScheduleDeedListMvpP(IScheduleMvpV mvpV) {
        super(mvpV);
    }
    public void toLoadScheduleDate(){
        mvpM.loadScheduleDate()
            .map(new Function<LocalDate, Calendar>() {
                @Override
                public Calendar apply(LocalDate localDate) throws Exception {
                    Calendar calendar = new Calendar();
                    calendar.setYear(localDate.getYear());
                    calendar.setMonth(localDate.getMonthOfYear());
                    calendar.setDay(localDate.getDayOfMonth());
                    calendar.setSchemeColor(Color.RED);//如果单独标记颜色、则会使用这个颜色
                    calendar.setScheme("事");
                    return calendar;
                }
            })
            .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<Calendar>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Calendar>() {
                Map<String, Calendar> allEntity = Maps.newLinkedHashMap();
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onScheduleDateGetStart();
                }

                @Override
                public void onNext(Calendar entity) {
                    super.onNext(entity);
                    allEntity.put(entity.toString(),entity);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onScheduleDateGetErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onScheduleDateGetSucc(allEntity);
                }
            });
    }
    public interface IScheduleMvpV extends IBaseDeedMvpV{
        public void onScheduleDateGetStart();
        public void onScheduleDateGetSucc(Map<String, Calendar> calendarMap);
        public void onScheduleDateGetErr(Throwable err);
    }
}

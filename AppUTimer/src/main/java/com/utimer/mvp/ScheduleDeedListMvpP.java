package com.utimer.mvp;


import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.DeedSchemeInfo;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by lw on 2019/7/19.
 */
public class ScheduleDeedListMvpP extends BaseDeedListMvpP {
    private boolean isSchemeLoading;

    public ScheduleDeedListMvpP(IScheduleMvpV mvpV) {
        super(mvpV);
    }

    public void toLoadScheduleDate(){
        if(!isSchemeLoading)
            toLoadScheme(mvpM.toLoadScheme());
    }
    protected void toLoadScheme(@NonNull Flowable<DeedSchemeInfo> loadRx){
        loadRx.compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<DeedSchemeInfo>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<DeedSchemeInfo>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    isSchemeLoading = true;
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onSchemeLoadStart();
                }

                @Override
                public void onNext(DeedSchemeInfo schemeInfo) {
                    super.onNext(schemeInfo);
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onSchemeLoadSucc(schemeInfo);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    isSchemeLoading = false;
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onSchemeLoadErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    isSchemeLoading = false;
                    if(mvpV != null)
                        ((IScheduleMvpV)mvpV).onSchemeLoadEnd();
                }
            });
    }
    @Override
    public void toHandleBusEvent(DeedBusEvent busEvent, DeedState... state){
        if(busEvent == null || !busEvent.ifValid())
            return;
        if(busEvent.getEventType() == GtdBusEventType.LOAD)
            toLoadScheduleDate();
    }
    @Override
    public void toHandleBusEvent(DeedDoneBusEvent busEvent, DeedState... state){
        if(mvpV == null || busEvent == null || !busEvent.ifValid() ||
            (busEvent.getEventType() != GtdBusEventType.CREATE && busEvent.getEventType() != GtdBusEventType.SAVE
            && busEvent.getEventType() != GtdBusEventType.EDIT && busEvent.getEventType() != GtdBusEventType.DELETE))
            return;
        GtdDeedEntity deedEntity = busEvent.getDeedEntity();

        List<LocalDate> deedDate = Lists.newArrayList();
        if(deedEntity.getStartTime() != null)
            deedDate.add(deedEntity.getStartTime().toLocalDate());

        Optional<Boolean> ifAtCurrCalendar = ((IScheduleMvpV) mvpV).ifAtSelectedDay(deedEntity.getWorkTime().toLocalDate());
        List<DateTime> warningDateTime = deedEntity.getWarningTimeList();
        if(warningDateTime != null) {
            for (DateTime date : warningDateTime) {
                deedDate.add(date.toLocalDate());
                if (ifAtCurrCalendar.isPresent() && !ifAtCurrCalendar.get())
                    ifAtCurrCalendar = ((IScheduleMvpV) mvpV).ifAtSelectedDay(date.toLocalDate());
            }
        }
        if(ifAtCurrCalendar.isPresent() && ifAtCurrCalendar.get() && (busEvent.getEventType() == GtdBusEventType.CREATE || busEvent.getEventType() == GtdBusEventType.EDIT))
            mvpV.onLoadSucc(busEvent.getDeedEntity());
        toReloadScheme(deedDate);
    }

    private void toReloadScheme(List<LocalDate> updateDateList){
        if(updateDateList == null || updateDateList.isEmpty())
            return;
        mvpM.toLoadScheme(Flowable.fromIterable(updateDateList))
                .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<DeedSchemeInfo>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<DeedSchemeInfo>() {
                    @Override
                    public void onNext(DeedSchemeInfo schemeInfo) {
                        super.onNext(schemeInfo);
                        if(mvpV != null)
                            ((IScheduleMvpV)mvpV).onSchemeLoadSucc(schemeInfo);
                    }
                });
    }
    public interface IScheduleMvpV extends BaseDeedListMvpP.IBaseDeedMvpV {
        public void onSchemeLoadStart();
        public void onSchemeLoadSucc(DeedSchemeInfo schemeInfo);
        public void onSchemeLoadErr(Throwable err);
        public void onSchemeLoadEnd();

        public Optional<Boolean> ifAtSelectedDay(LocalDate localDate);
    }
}

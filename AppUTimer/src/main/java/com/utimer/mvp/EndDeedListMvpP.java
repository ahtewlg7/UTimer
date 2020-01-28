package com.utimer.mvp;


import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ahtewlg7.utimer.comparator.ALocalDateComparator;
import ahtewlg7.utimer.comparator.DeedWarningTimeComparator;
import ahtewlg7.utimer.comparator.SimpleDateComparator;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.view.EndDeedSectionEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.mvp.ADeedListMvpP;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class EndDeedListMvpP extends ADeedListMvpP<EndDeedSectionEntity<GtdDeedEntity>> {
    private IEndDeedMvpV mvpV;
    private Multimap<LocalDate, EndDeedSectionEntity> dateSectionMap;
    protected DeedWarningTimeComparator comparator;

    public EndDeedListMvpP(IEndDeedMvpV mvpV){
        super();
        this.mvpV           = mvpV;
        dateSectionMap      = HashMultimap.create();
        comparator          = new DeedWarningTimeComparator();
    }

    @Override
    protected void toLoad(@NonNull Flowable<List<GtdDeedEntity>> loadRx){
        loadRx.flatMap(new Function<List<GtdDeedEntity>, Publisher<GtdDeedEntity>>() {
                    @Override
                    public Publisher<GtdDeedEntity> apply(List<GtdDeedEntity> gtdDeedEntities) throws Exception {
                        return Flowable.fromIterable(gtdDeedEntities);
                    }
                })
                .groupBy(new Function<GtdDeedEntity, LocalDate>() {
                    @Override
                    public LocalDate apply(GtdDeedEntity deedEntity) throws Exception {
                        DateTime endTime    = deedEntity.getEndTime();
                        return new LocalDate(endTime.getYear(), endTime.getMonthOfYear(),1);
                    }
                })
                .flatMap(new Function<GroupedFlowable<LocalDate, GtdDeedEntity>, Publisher<EndDeedSectionEntity<GtdDeedEntity>>>() {
                    @Override
                    public Publisher<EndDeedSectionEntity<GtdDeedEntity>> apply(GroupedFlowable<LocalDate, GtdDeedEntity> entityGroupRx) throws Exception {
                        return entityGroupRx.map(new Function<GtdDeedEntity, EndDeedSectionEntity<GtdDeedEntity>>() {
                                    @Override
                                    public EndDeedSectionEntity<GtdDeedEntity> apply(GtdDeedEntity deedEntity) throws Exception {
                                        return new EndDeedSectionEntity<GtdDeedEntity>(entityGroupRx.getKey(), deedEntity);
                                    }
                                })
                                .sorted(new EndDeedSectionEndTimeComparator().getDescOrder())
                                .startWith(new EndDeedSectionEntity<GtdDeedEntity>(true, entityGroupRx.getKey(),true))
                                .doOnNext(new Consumer<EndDeedSectionEntity<GtdDeedEntity>>() {
                                    @Override
                                    public void accept(EndDeedSectionEntity<GtdDeedEntity> sectionEntity) throws Exception {
                                        dateSectionMap.put(entityGroupRx.getKey(), sectionEntity);
                                    }
                                });
                    }
                })
                .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<EndDeedSectionEntity<GtdDeedEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<EndDeedSectionEntity<GtdDeedEntity>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        dateSectionMap.clear();
                        if(mvpV != null)
                            mvpV.onLoadStart();
                    }

                    @Override
                    public void onNext(EndDeedSectionEntity<GtdDeedEntity> sectionEntity) {
                        super.onNext(sectionEntity);
                        if(mvpV != null)
                            mvpV.onSectionLoad(sectionEntity);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if(mvpV != null)
                            mvpV.onLoadErr(t);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(mvpV != null)
                            mvpV.onSectionLoadSucc();
                    }
                });
    }

    public void toLoadDeedByWeek(boolean ascOrder, DeedState... deedState){
       toLoad(mvpM.toLoad(ascOrder, deedState));
    }

    public List<LocalDate> getAllEndMonth(){
       List<LocalDate> allEndMonthList = Lists.newArrayList(dateSectionMap.keySet());
       Collections.sort(allEndMonthList,new SimpleDateComparator().getDescOrder());
        return allEndMonthList;
    }
    public List<EndDeedSectionEntity> getAllSectionEntity(@NonNull List<LocalDate> localDateList){
        List<LocalDate> allMonth =  getAllEndMonth();
        if(allMonth == null)
            return null;
        List<EndDeedSectionEntity> list = Lists.newLinkedList();
        for(LocalDate  date : allMonth){
            Collection<EndDeedSectionEntity> sectionEntities = dateSectionMap.get(date);
            if(localDateList.contains(date)) {
                list.addAll(sectionEntities);
            }else if(!sectionEntities.isEmpty())
                list.add((EndDeedSectionEntity)sectionEntities.toArray()[0]);
        }
       return list;
    }

    @Override
    public void toHandleBusEvent(DeedDoneBusEvent busEvent, DeedState... state) {
    }

    @Override
    public void toTagDeed(EndDeedSectionEntity<GtdDeedEntity> entity, DeedState deedState, int position) {
        if(entity == null || entity.isHeader)
            return;
        mvpM.toTag(entity.t,deedState)
                .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<Optional<BaseEventBusBean>>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<Optional<BaseEventBusBean>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        if(mvpV != null)
                            mvpV.onTagStart(entity, deedState, position);
                    }

                    @Override
                    public void onNext(Optional<BaseEventBusBean> busBeanOptional) {
                        super.onNext(busBeanOptional);
                        if(mvpV == null)
                            return;
                        if(busBeanOptional.isPresent())
                            mvpV.onTagSucc(entity, deedState, position);
                        else
                            mvpV.onTagFail(entity, deedState, position);
                    }

                    @Override
                    public void onError(Throwable t) {
                        super.onError(t);
                        if(mvpV != null)
                            mvpV.onTagErr(entity,deedState, position, t);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if(mvpV != null)
                            mvpV.onTagEnd(entity,deedState, position);
                    }
                });
    }
    @Override
    protected Comparator<GtdDeedEntity> getDefaultAscComparator(){
        return comparator.getAscOrder();
    }
    @Override
    protected Comparator<GtdDeedEntity> getDefaultDescComparator(){
        return comparator.getDescOrder();
    }

    @Override
    protected GtdDeedEntity getDeedEntity(EndDeedSectionEntity<GtdDeedEntity> entity) {
        return entity.t;
    }

    class EndDeedSectionEndTimeComparator extends ALocalDateComparator<EndDeedSectionEntity<GtdDeedEntity>> {
        @Override
        protected Optional<LocalDate> getComparatorTime(EndDeedSectionEntity<GtdDeedEntity> e) {
            DateTime dateTime = e.t.getEndTime();
            return Optional.fromNullable(new LocalDate(dateTime.getYear(), dateTime.getMonthOfYear(),1));
        }
    }
    public interface IEndDeedMvpV extends IADeedMvpV,
            IADeedTagV<EndDeedSectionEntity<GtdDeedEntity>>{
        public void onSectionLoad(EndDeedSectionEntity<GtdDeedEntity> sectionEntity);
        public void onSectionLoadSucc();
    }
}

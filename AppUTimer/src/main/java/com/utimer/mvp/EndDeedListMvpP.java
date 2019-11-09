package com.utimer.mvp;


import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.joda.time.DateTime;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import ahtewlg7.utimer.comparator.ADateTimeComparator;
import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.view.EndDeedSectionEntity;
import ahtewlg7.utimer.enumtype.DATE_MONTH;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.mvp.ADeedListMvpP;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class EndDeedListMvpP extends ADeedListMvpP {
    private IEndDeedMvpV mvpV;
    private Multimap<DATE_MONTH, EndDeedSectionEntity> monthSectionMap;

    public EndDeedListMvpP(IEndDeedMvpV mvpV){
        super();
        this.mvpV           = mvpV;
        monthSectionMap     = HashMultimap.create();
    }

    @Override
    protected void toLoad(@NonNull Flowable<List<GtdDeedEntity>> loadRx){
        loadRx.flatMap(new Function<List<GtdDeedEntity>, Publisher<GtdDeedEntity>>() {
                    @Override
                    public Publisher<GtdDeedEntity> apply(List<GtdDeedEntity> gtdDeedEntities) throws Exception {
                        return Flowable.fromIterable(gtdDeedEntities);
                    }
                })
                .groupBy(new Function<GtdDeedEntity, DATE_MONTH>() {
                    @Override
                    public DATE_MONTH apply(GtdDeedEntity deedEntity) throws Exception {
                        return DATE_MONTH.valueOf(deedEntity.getEndTime().getMonthOfYear());
                    }
                })
                .flatMap(new Function<GroupedFlowable<DATE_MONTH, GtdDeedEntity>, Publisher<EndDeedSectionEntity<GtdDeedEntity>>>() {
                    @Override
                    public Publisher<EndDeedSectionEntity<GtdDeedEntity>> apply(GroupedFlowable<DATE_MONTH, GtdDeedEntity> entityGroupRx) throws Exception {
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
                                        monthSectionMap.put(entityGroupRx.getKey(), sectionEntity);
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
                        monthSectionMap.clear();
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

    public List<DATE_MONTH> getAllEndMonth(){
       Set<DATE_MONTH> allEndMonth = monthSectionMap.keySet();
       return allEndMonth == null ? null : Lists.newArrayList(allEndMonth);
    }
    public List<EndDeedSectionEntity> getAllSectionEntity(@NonNull List<DATE_MONTH> monthList){
        List<DATE_MONTH> allMonth =  getAllEndMonth();
        if(allMonth == null)
            return null;
        List<EndDeedSectionEntity> list = Lists.newLinkedList();
        for(DATE_MONTH  month : allMonth){
            Collection<EndDeedSectionEntity> sectionEntities = monthSectionMap.get(month);
            if(monthList.contains(month)) {
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
    public void toTagDeed(GtdDeedEntity deedEntity, DeedState deedState, int position) {
        
    }

    class EndDeedSectionEndTimeComparator extends ADateTimeComparator<EndDeedSectionEntity<GtdDeedEntity>> {
        @Override
        protected Optional<DateTime> getComparatorTime(EndDeedSectionEntity<GtdDeedEntity> e) {
            return Optional.fromNullable(e.t.getEndTime());
        }
    }
    public interface IEndDeedMvpV extends IADeedMvpV{
        public void onSectionLoad(EndDeedSectionEntity<GtdDeedEntity> sectionEntity);
        public void onSectionLoadSucc();
    }
}

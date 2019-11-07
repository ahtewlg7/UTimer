package com.utimer.mvp;


import androidx.annotation.NonNull;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscription;

import java.util.Collection;
import java.util.List;

import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.view.BaseSectionEntity;
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
    private Multimap<DATE_MONTH, BaseSectionEntity> monthSectionMap;

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
                .flatMap(new Function<GroupedFlowable<DATE_MONTH, GtdDeedEntity>, Publisher<BaseSectionEntity<GtdDeedEntity>>>() {
                    @Override
                    public Publisher<BaseSectionEntity<GtdDeedEntity>> apply(GroupedFlowable<DATE_MONTH, GtdDeedEntity> entityGroupRx) throws Exception {
                        return entityGroupRx.map(new Function<GtdDeedEntity, BaseSectionEntity<GtdDeedEntity>>() {
                            @Override
                            public BaseSectionEntity<GtdDeedEntity> apply(GtdDeedEntity deedEntity) throws Exception {
                                return new BaseSectionEntity<GtdDeedEntity>(deedEntity);
                            }
                        })
                                .startWith(new BaseSectionEntity<GtdDeedEntity>(true, entityGroupRx.getKey().getDetail(),true))
                                .doOnNext(new Consumer<BaseSectionEntity<GtdDeedEntity>>() {
                                    @Override
                                    public void accept(BaseSectionEntity<GtdDeedEntity> sectionEntity) throws Exception {
                                        monthSectionMap.put(entityGroupRx.getKey(), sectionEntity);
                                    }
                                });
                    }
                })
                .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<BaseSectionEntity<GtdDeedEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<BaseSectionEntity<GtdDeedEntity>>() {
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        monthSectionMap.clear();
                        if(mvpV != null)
                            mvpV.onLoadStart();
                    }

                    @Override
                    public void onNext(BaseSectionEntity<GtdDeedEntity> sectionEntity) {
                        super.onNext(sectionEntity);
                        if(mvpV != null)
                            ((IEndDeedMvpV)mvpV).onSectionLoad(sectionEntity);
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
                            ((IEndDeedMvpV)mvpV).onSectionLoadSucc();
                    }
                });
    }

    public void toLoadDeedByWeek(boolean ascOrder, DeedState... deedState){
       toLoad(mvpM.toLoad(ascOrder, deedState));
    }

    public List<BaseSectionEntity> getSectionEntity(DATE_MONTH month){
        Collection<BaseSectionEntity> monthSection = monthSectionMap.get(month);
       return monthSection == null ? null : Lists.newArrayList(monthSection);
    }

    @Override
    public void toHandleBusEvent(DeedDoneBusEvent busEvent, DeedState... state) {

    }

    @Override
    public void toTagDeed(GtdDeedEntity deedEntity, DeedState deedState, int position) {
        
    }

    public interface IEndDeedMvpV extends IADeedMvpV{
        public void onSectionLoad(BaseSectionEntity<GtdDeedEntity> sectionEntity);
        public void onSectionLoadSucc();
    }
}

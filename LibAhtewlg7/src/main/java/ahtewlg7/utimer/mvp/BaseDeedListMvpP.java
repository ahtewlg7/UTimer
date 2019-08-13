package ahtewlg7.utimer.mvp;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle3.LifecycleProvider;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import ahtewlg7.utimer.comparator.ABaseIntComparator;
import ahtewlg7.utimer.comparator.DeedEntityStateOrderComparator;
import ahtewlg7.utimer.comparator.DeedWarningTimeComparator;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.DeedSchemeInfo;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.DeedSchemeFactory;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.state.DeedStateGraph;
import ahtewlg7.utimer.state.GtdBaseState;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2019/6/25.
 */
public class BaseDeedListMvpP {
    protected IBaseDeedMvpV mvpV;
    protected BaseDeedMvpM mvpM;
    protected DeedStateGraph stateGraph;

    public BaseDeedListMvpP(IBaseDeedMvpV mvpV){
        this.mvpV           = mvpV;
        mvpM                = new BaseDeedMvpM();
        stateGraph          = new DeedStateGraph();
    }

    public Set<DeedState> getNextState(@NonNull GtdDeedEntity deedEntity){
        return stateGraph.getNextNodeList(deedEntity.getDeedState());
    }

    public void toLoadDeedByState(DeedState... deedState){
        toLoad(mvpM.toLoad(deedState));
    }
    public void toLoadDeedByState(boolean ascOrder, DeedState... deedState){
        toLoad(mvpM.toLoad(ascOrder, deedState));
    }
    public void toLoadDeedByDate(final LocalDate localDate){
        toLoadDateDeed(Flowable.mergeDelayError(mvpM.toLoad(localDate), mvpM.toLoad(DeedState.SCHEDULE, localDate.minusDays(1)))
                .reduce(new BiFunction<Map<DateTime, GtdDeedEntity>, Map<DateTime, GtdDeedEntity>, Map<DateTime, GtdDeedEntity>>() {
                    @Override
                    public Map<DateTime, GtdDeedEntity> apply(Map<DateTime, GtdDeedEntity> dateTimeGtdDeedEntityMap, Map<DateTime, GtdDeedEntity> dateTimeGtdDeedEntityMap2) throws Exception {
                        dateTimeGtdDeedEntityMap.putAll(dateTimeGtdDeedEntityMap2);
                        return dateTimeGtdDeedEntityMap;
                    }
                })
                .map(new Function<Map<DateTime, GtdDeedEntity>, DateDeedEntity>() {
                    @Override
                    public DateDeedEntity apply(Map<DateTime, GtdDeedEntity> dateTimeGtdDeedEntityMap) throws Exception {
                        DateDeedEntity dateDeedEntity = new DateDeedEntity(localDate, dateTimeGtdDeedEntityMap);
                        dateDeedEntity.parseMap();
                        return dateDeedEntity;
                    }
                })
                .toFlowable());
    }

    public void toTagDeed(final GtdDeedEntity deedEntity, final DeedState deedState, final int position){
        mvpM.toTag(deedEntity,deedState)
            .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<Optional<BaseEventBusBean>>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<Optional<BaseEventBusBean>>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(mvpV != null)
                        mvpV.onTagStart(deedEntity,deedState);
                }

                @Override
                public void onNext(Optional<BaseEventBusBean> busBeanOptional) {
                    super.onNext(busBeanOptional);
                    if(mvpV == null)
                        return;
                    if(busBeanOptional.isPresent())
                        mvpV.onTagSucc(deedEntity, deedState, position);
                    else
                        mvpV.onTagFail(deedEntity,deedState);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        mvpV.onTagErr(deedEntity,deedState, t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mvpV != null)
                        mvpV.onTagEnd(deedEntity,deedState);
                }
            });
    }
    public void toHandleBusEvent(DeedBusEvent busEvent, DeedState... state){
        if(busEvent == null || !busEvent.ifValid())
            return;
        if(busEvent.getEventType() == GtdBusEventType.LOAD)
            toLoadDeedByState(state);
    }
    public void toHandleBusEvent(DeedDoneBusEvent busEvent, DeedState... state){
        if(busEvent == null || !busEvent.ifValid())
            return;
        if((busEvent.getEventType() == GtdBusEventType.SAVE
            && Arrays.asList(state).contains(busEvent.getDeedEntity().getDeedState())
            && (busEvent.getDeedEntity().getDeedState() == DeedState.MAYBE
            || busEvent.getDeedEntity().getDeedState() == DeedState.INBOX)))
            toLoadDeedByState(state);
    }

    protected void toLoad(@NonNull Flowable<List<GtdDeedEntity>> loadRx){
        loadRx.compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<List<GtdDeedEntity>>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<List<GtdDeedEntity>>() {
                    List<GtdDeedEntity> allEntity = Lists.newArrayList();
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        if(mvpV != null)
                            mvpV.onLoadStart();
                    }

                    @Override
                    public void onNext(List<GtdDeedEntity> entityList) {
                        super.onNext(entityList);
                        allEntity.addAll(entityList);
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
                            mvpV.onLoadSucc(allEntity);
                    }
                });
    }
    protected void toLoadDateDeed(@NonNull Flowable<DateDeedEntity> loadRx){
        loadRx.compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<DateDeedEntity>bindUntilEvent(FragmentEvent.DESTROY))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new MySafeSubscriber<DateDeedEntity>() {
                    List<GtdDeedEntity> allEntity = Lists.newArrayList();
                    @Override
                    public void onSubscribe(Subscription s) {
                        super.onSubscribe(s);
                        if(mvpV != null)
                            mvpV.onLoadStart();
                    }

                    @Override
                    public void onNext(DateDeedEntity entityList) {
                        super.onNext(entityList);
                        allEntity.addAll(entityList.getDateDeedList());
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
                            mvpV.onLoadSucc(allEntity);
                    }
                });
    }

    public class BaseDeedMvpM{
        public Flowable<LocalDate> loadWorkDate(){
            return GtdDeedByUuidFactory.getInstance().getWorkDate();
        }
        public Flowable<List<GtdDeedEntity>> toLoad(DeedState... state) {
            return toLoad(true, state);
        }
        public Flowable<List<GtdDeedEntity>> toLoad(final boolean ascOrder, DeedState... state) {
            return GtdDeedByUuidFactory.getInstance().getEntityByState(state)
                    .doOnNext(new Consumer<List<GtdDeedEntity>>() {
                        @Override
                        public void accept(List<GtdDeedEntity> entityList) throws Exception {
                            if(ascOrder)
                                Collections.sort(entityList, new DeedWarningTimeComparator().getAscOrder());
                            else
                                Collections.sort(entityList, new DeedWarningTimeComparator().getDescOrder());
                        }
                    });
        }
        public Flowable<Map<DateTime, GtdDeedEntity>> toLoad(DeedState state, LocalDate... dates) {
            return GtdDeedByUuidFactory.getInstance().getEntityByDate(state,dates);
        }
        public Flowable<Map<DateTime, GtdDeedEntity>> toLoad(LocalDate... dates) {
            return GtdDeedByUuidFactory.getInstance().getEntityByDate(dates);
        }
        public Flowable<Optional<BaseEventBusBean>> toTag(GtdDeedEntity entity, DeedState state) {
            Optional<BaseEventBusBean> busBean = Optional.absent();
            if(entity == null || state == null)
                return Flowable.just(busBean);
            GtdBaseState currState = GtdMachine.getInstance().getCurrState(entity);
            switch (state){
                case SCHEDULE:
                    busBean = currState.toBeScheduleJob(entity);
                    break;
                case ONE_QUARTER:
                    busBean = currState.toBeQuarterJob(entity);
                    break;
                case DEFER:
                    busBean = currState.toBeDeferJob(entity);
                    break;
                case DELEGATE:
                    busBean = currState.toBeDelegateJob(entity);
                    break;
                case WISH:
                    busBean = currState.toBeWishJob(entity);
                    break;
                case REFERENCE:
                    busBean = currState.toBeReference(entity);
                    break;
                case PROJECT:
                    busBean = currState.toBeProject(entity);
                    break;
                case DONE:
                    busBean = currState.toBeDone(entity);
                    break;
                case USELESS:
                    busBean = currState.toBeUseless(entity);
                    break;
                case TRASH:
                    busBean = currState.toTrash(entity);
            }
            return Flowable.just(busBean);
        }
    }
    public interface IBaseDeedMvpV extends IRxLifeCycleBindView{
        public @NonNull LifecycleProvider getRxLifeCycleBindView();
        public void onLoadStart();
        public void onLoadSucc(List<GtdDeedEntity> entityList);
        public void onLoadSucc(GtdDeedEntity entity);
        public void onLoadErr(Throwable err);

        public void onTagStart(GtdDeedEntity entity,DeedState toState);
        public void onTagSucc(GtdDeedEntity entity, DeedState toState, int position);
        public void onTagFail(GtdDeedEntity entity, DeedState toState);
        public void onTagErr(GtdDeedEntity entity,  DeedState toState, Throwable err);
        public void onTagEnd(GtdDeedEntity entity,  DeedState toState);
    }
    public class DateDeedEntity{
        private LocalDate localDate;
        private DeedSchemeFactory deedSchemeFactory;

        private List<GtdDeedEntity> dateDeedList;
        private List<DeedSchemeInfo> schemeInfoList;
        private Map<DateTime, GtdDeedEntity> dateDeedMap;

        public DateDeedEntity(@NonNull LocalDate localDate, @NonNull Map<DateTime, GtdDeedEntity> dateDeedMap){
            this.localDate      = localDate;
            this.dateDeedMap    = dateDeedMap;
            dateDeedList        = Lists.newArrayList();
            deedSchemeFactory   = new DeedSchemeFactory();
        }

        public void parseMap(){
            Iterator<Map.Entry<DateTime, GtdDeedEntity>> entries = dateDeedMap.entrySet().iterator();
            while (entries.hasNext()){
                DateTime dateTime = entries.next().getKey();
                GtdDeedEntity deedEntity = entries.next().getValue();
                dateDeedList.add(deedEntity);
                Optional<DeedSchemeInfo> schemeInfoOptional = deedSchemeFactory.getScheme(deedEntity);
                if(schemeInfoOptional.isPresent())
                    schemeInfoList.add(schemeInfoOptional.get());

            }
            Collections.sort(dateDeedList, new DeedEntityStateOrderComparator().getAscOrder());
            Collections.sort(schemeInfoList, new SchemeProgressComparator().getDescOrder());
        }

        public LocalDate getLocalDate() {
            return localDate;
        }

        public List<GtdDeedEntity> getDateDeedList() {
            return dateDeedList;
        }
    }
    public class SchemeProgressComparator extends ABaseIntComparator<DeedSchemeInfo>{
        @Override
        protected int getComparatorInt(DeedSchemeInfo deedSchemeInfo) {
            return deedSchemeInfo.getProgress();
        }
    }
}

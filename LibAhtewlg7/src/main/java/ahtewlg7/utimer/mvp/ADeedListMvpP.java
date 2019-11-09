package ahtewlg7.utimer.mvp;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.trello.rxlifecycle3.LifecycleProvider;

import org.joda.time.LocalDate;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import ahtewlg7.utimer.comparator.DeedEntityStateOrderComparator;
import ahtewlg7.utimer.comparator.DeedWarningTimeComparator;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.DeedSchemeEntity;
import ahtewlg7.utimer.entity.gtd.DeedSchemeInfo;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.DeedSchemeEntityFactory;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.graphs.DeedStateGraph;
import ahtewlg7.utimer.state.GtdBaseState;
import ahtewlg7.utimer.state.GtdMachine;
import io.reactivex.Flowable;
import io.reactivex.functions.BiFunction;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2019/6/25.
 */
public abstract class ADeedListMvpP {
    protected abstract void toLoad(@NonNull Flowable<List<GtdDeedEntity>> loadRx);
    public abstract void toHandleBusEvent(DeedDoneBusEvent busEvent, DeedState... state);
    public abstract void toTagDeed(final GtdDeedEntity deedEntity, final DeedState deedState, final int position);

    protected BaseDeedMvpM mvpM;
    protected DeedStateGraph stateGraph;

    public ADeedListMvpP(){
        mvpM                = new BaseDeedMvpM();
        stateGraph          = new DeedStateGraph();
        stateGraph.initGraph();
        stateGraph.initNodes();
    }

    public Set<DeedState> getNextState(@NonNull GtdDeedEntity deedEntity){
        Optional<Set<DeedState>> deedStateOptional = stateGraph.getNextNodeList(deedEntity.getDeedState());
        if(deedStateOptional.isPresent())
            return deedStateOptional.get();
        return null;
    }

    public void toLoadDeedByState(DeedState... deedState){
        toLoad(mvpM.toLoad(deedState));
    }
    public void toLoadDeedByState(boolean ascOrder, DeedState... deedState){
        toLoad(mvpM.toLoad(ascOrder, deedState));
    }
    public void toLoadDeedByDate(final LocalDate localDate){
        toLoad(Flowable.mergeDelayError(mvpM.toLoad(localDate), mvpM.toLoad(DeedState.SCHEDULE, localDate.minusDays(1)))
                .reduce(new BiFunction<List<GtdDeedEntity>, List<GtdDeedEntity>, List<GtdDeedEntity>>() {
                    @Override
                    public List<GtdDeedEntity> apply(List<GtdDeedEntity> dateTimeGtdDeedEntityList, List<GtdDeedEntity> dateTimeGtdDeedEntityList2) throws Exception {
                        dateTimeGtdDeedEntityList.addAll(dateTimeGtdDeedEntityList2);
                        Collections.sort(dateTimeGtdDeedEntityList, new DeedEntityStateOrderComparator().getAscOrder());
                        return dateTimeGtdDeedEntityList;
                    }
                }).toFlowable());
    }

    public void toUpdateScheme(@NonNull DeedSchemeEntity deedSchemeEntity){
        mvpM.toUpdateScheme(deedSchemeEntity);
    }
    public void toHandleBusEvent(DeedBusEvent busEvent, DeedState... state){
        if(busEvent == null || !busEvent.ifValid())
            return;
        if(busEvent.getEventType() == GtdBusEventType.LOAD)
            toLoadDeedByState(state);
    }

    public class BaseDeedMvpM{
        public Flowable<DeedSchemeInfo> toLoadScheme(){
            return DeedSchemeEntityFactory.getInstacne().toLoadDateScheme();
        }
        public Flowable<DeedSchemeInfo> toLoadScheme(@NonNull Flowable<LocalDate> localDateRx){
            return DeedSchemeEntityFactory.getInstacne().toLoadDateScheme(localDateRx);
        }
        public void toUpdateScheme(@NonNull DeedSchemeEntity deedSchemeEntity){
            DeedSchemeEntityFactory.getInstacne().toUpdateProgress(deedSchemeEntity);
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
                                Collections.sort(entityList, getDefaultAscComparator());
                            else
                                Collections.sort(entityList, getDefaultDescComparator());
                        }
                    });
        }
        public Flowable<List<GtdDeedEntity>> toLoad(DeedState state, LocalDate... dates) {
            return GtdDeedByUuidFactory.getInstance().getEntityByDate(state,dates);
        }
        public Flowable<List<GtdDeedEntity>> toLoad(LocalDate... dates) {
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

    protected Comparator<GtdDeedEntity> getDefaultAscComparator(){
        return new DeedWarningTimeComparator().getAscOrder();
    }
    protected Comparator<GtdDeedEntity> getDefaultDescComparator(){
        return new DeedWarningTimeComparator().getDescOrder();
    }
    public interface IADeedMvpV extends IRxLifeCycleBindView {
        public @NonNull LifecycleProvider getRxLifeCycleBindView();

        public void onLoadStart();
        public void onLoadErr(Throwable err);
    }
}

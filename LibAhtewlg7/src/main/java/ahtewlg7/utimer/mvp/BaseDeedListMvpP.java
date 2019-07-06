package ahtewlg7.utimer.mvp;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.Collections;
import java.util.List;

import ahtewlg7.utimer.comparator.DeedWarningTimeComparator;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.state.BaseGtdState;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2019/6/25.
 */
public class BaseDeedListMvpP {
    private IBaseDeedMvpV mvpV;
    private BaseDeedMvpM mvpM;

    public BaseDeedListMvpP(IBaseDeedMvpV mvpV){
        this.mvpV = mvpV;
        mvpM      = new BaseDeedMvpM();
    }
    public void toLoadDeedByState(final DeedState deedState){
        mvpM.toLoad(deedState)
            .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<List<GtdDeedEntity>>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<List<GtdDeedEntity>>() {
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    if(mvpV != null)
                        mvpV.onLoadStart(deedState);
                }

                @Override
                public void onNext(List<GtdDeedEntity> entityList) {
                    super.onNext(entityList);
                    if(mvpV != null)
                        mvpV.onLoadSucc(deedState, entityList);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        mvpV.onLoadErr(deedState, t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mvpV != null)
                        mvpV.onLoadEnd(deedState);
                }
            });
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

    class BaseDeedMvpM{
        Flowable<List<GtdDeedEntity>> toLoad(DeedState... state) {
            return GtdDeedByUuidFactory.getInstance().getEntityByState(state)
                    .doOnNext(new Consumer<List<GtdDeedEntity>>() {
                        @Override
                        public void accept(List<GtdDeedEntity> entityList) throws Exception {
                            Collections.sort(entityList, new DeedWarningTimeComparator().getAscOrder());
                        }
                    });
        }
        Flowable<Optional<BaseEventBusBean>> toTag(GtdDeedEntity entity, DeedState state) {
            Optional<BaseEventBusBean> busBean = Optional.absent();
            if(entity == null || state == null)
                return Flowable.just(busBean);
            BaseGtdState currState = GtdMachine.getInstance().getCurrState(entity);
            switch (state){
                case TWO_MIN:
                    busBean = currState.toBe2MinJob(entity);
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
            return Flowable.just(busBean).doOnNext(new Consumer<Optional<BaseEventBusBean>>() {
                @Override
                public void accept(Optional<BaseEventBusBean> eventBusBeanOptional) throws Exception {
                    if(eventBusBeanOptional.isPresent())
                        EventBusFatory.getInstance().getDefaultEventBus().postSticky(eventBusBeanOptional.get());
                }
            });
        }
    }
    public interface IBaseDeedMvpV extends IRxLifeCycleBindView{
        public @NonNull LifecycleProvider getRxLifeCycleBindView();
        public void onLoadStart(DeedState state);
        public void onLoadSucc(DeedState state, List<GtdDeedEntity> entityList);
        public void onLoadErr(DeedState state, Throwable err);
        public void onLoadEnd(DeedState state);

        public void onTagStart(GtdDeedEntity entity,DeedState toState);
        public void onTagSucc(GtdDeedEntity entity, DeedState toState, int position);
        public void onTagFail(GtdDeedEntity entity, DeedState toState);
        public void onTagErr(GtdDeedEntity entity,  DeedState toState, Throwable err);
        public void onTagEnd(GtdDeedEntity entity,  DeedState toState);
    }

}

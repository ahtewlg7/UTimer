package ahtewlg7.utimer.mvp;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle3.android.FragmentEvent;
import com.trello.rxlifecycle3.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.Arrays;
import java.util.List;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * Created by lw on 2019/6/25.
 */
public class BaseDeedListMvpP extends ADeedListMvpP {
    protected IBaseDeedMvpV mvpV;

    public BaseDeedListMvpP(IBaseDeedMvpV mvpV){
        super();
        this.mvpV           = mvpV;
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
                        mvpV.onTagStart(deedEntity, deedState, position);
                }

                @Override
                public void onNext(Optional<BaseEventBusBean> busBeanOptional) {
                    super.onNext(busBeanOptional);
                    if(mvpV == null)
                        return;
                    if(busBeanOptional.isPresent())
                        mvpV.onTagSucc(deedEntity, deedState, position);
                    else
                        mvpV.onTagFail(deedEntity, deedState, position);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        mvpV.onTagErr(deedEntity,deedState, position, t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mvpV != null)
                        mvpV.onTagEnd(deedEntity,deedState, position);
                }
            });
    }
    public void toHandleBusEvent(DeedDoneBusEvent busEvent, DeedState... state){
        if(busEvent == null || !busEvent.ifValid())
            return;
        if((busEvent.getEventType() == GtdBusEventType.CREATE || busEvent.getEventType() == GtdBusEventType.SAVE || busEvent.getEventType() == GtdBusEventType.EDIT)
            && Arrays.asList(state).contains(busEvent.getDeedEntity().getDeedState()) && mvpV != null)
            mvpV.onLoadSucc(busEvent.getDeedEntity());
    }

    @Override
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
    public interface IBaseDeedMvpV extends IADeedMvpV{
        public void onLoadSucc(List<GtdDeedEntity> entityList);
        public void onLoadSucc(GtdDeedEntity entity);

        public void onTagStart(GtdDeedEntity entity,DeedState toState, int position);
        public void onTagSucc(GtdDeedEntity entity, DeedState toState, int position);
        public void onTagFail(GtdDeedEntity entity, DeedState toState, int position);
        public void onTagErr(GtdDeedEntity entity,  DeedState toState, int position, Throwable err);
        public void onTagEnd(GtdDeedEntity entity,  DeedState toState, int position);
    }
}

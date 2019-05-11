package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;

import static ahtewlg7.utimer.mvp.IAllItemListMvpV.INVALID_INDEX;

/**
 * Created by lw on 2018/12/9.
 */
public class DeedMaybeListMvpP {
    private IGtdActionListMvpV mvpV;
    private MaybeActListMvpM mvpM;

    public DeedMaybeListMvpP(IGtdActionListMvpV mvpV) {
        this.mvpV  = mvpV;
        mvpM       = new MaybeActListMvpM();
    }

    public void toHandleActionEvent(DeedBusEvent actionBusEvent){
        if(actionBusEvent == null || !actionBusEvent.ifValid())
            return;
        if(actionBusEvent.getEventType() == GtdBusEventType.LOAD)
            toLoadAllMaybe();
    }

    public void toLoadAllMaybe() {
        mvpM.loadMaybe()
            .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<GtdDeedEntity>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<GtdDeedEntity>() {
                List<GtdDeedEntity> entityList = Lists.newArrayList();
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    entityList.clear();
                    if(mvpV != null)
                        mvpV.onItemLoadStart();
                }

                @Override
                public void onNext(GtdDeedEntity entity) {
                    super.onNext(entity);
                    entityList.add(entity);
                }

                @Override
                public void onError(Throwable t) {
                    super.onError(t);
                    if(mvpV != null)
                        mvpV.onItemLoadErr(t);
                }

                @Override
                public void onComplete() {
                    super.onComplete();
                    if(mvpV != null)
                        mvpV.onItemLoadEnd(entityList);
                }
            });
    }

    public void toDeleteItem(@NonNull Flowable<GtdDeedEntity>  entityRx) {
        entityRx.map(new Function<GtdDeedEntity, Optional<BaseEventBusBean>>() {
                    @Override
                    public Optional<BaseEventBusBean> apply(GtdDeedEntity entity) throws Exception {
                        return GtdMachine.getInstance().getCurrState(entity).toTrash(entity);
                    }
                })
                .subscribe(new MySafeSubscriber<Optional<BaseEventBusBean>>() {
                    @Override
                    public void onNext(Optional<BaseEventBusBean> entity) {
                        super.onNext(entity);
                        if(mvpV != null && entity.isPresent()){
                            if(entity.get().isPerform())
                                mvpV.onDeleteSucc(INVALID_INDEX, ((DeedBusEvent)entity.get()).getActionEntity());
                            else
                                mvpV.onDeleteFail(((DeedBusEvent)entity.get()).getActionEntity());
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);
                        if (mvpV != null)
                            mvpV.onDeleteErr(e);
                    }

                    @Override
                    public void onComplete() {
                        super.onComplete();
                        if (mvpV != null)
                            mvpV.onDeleteEnd();
                    }
                });
    }

    public void onItemCreated(GtdDeedEntity entity) {
        /*entityList.add(entity);
        if(mvpV != null)
            mvpV.resetView(entityList);*/
    }

    public void onItemEdited(int index, GtdDeedEntity entity) {
        /*entityList.set(index, entity);
        if(mvpV != null)
            mvpV.resetView(index, entity);*/
    }

    class MaybeActListMvpM {
        Flowable<GtdDeedEntity> loadMaybe() {
            return GtdDeedByUuidFactory.getInstance().getEntityByState(ActState.MAYBE);
        }
    }

    public interface IGtdActionListMvpV extends IAllItemListMvpV<GtdDeedEntity> {
    }
}

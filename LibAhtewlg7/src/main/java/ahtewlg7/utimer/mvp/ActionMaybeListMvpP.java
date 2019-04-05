package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.components.support.RxFragment;

import org.reactivestreams.Subscription;

import java.util.List;

import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdActionByUuidFactory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;

import static ahtewlg7.utimer.mvp.IAllItemListMvpV.INVALID_INDEX;

/**
 * Created by lw on 2018/12/9.
 */
public class ActionMaybeListMvpP {
    private IGtdActionListMvpV mvpV;
    private MaybeActListMvpM mvpM;

    public ActionMaybeListMvpP(IGtdActionListMvpV mvpV) {
        this.mvpV  = mvpV;
        mvpM       = new MaybeActListMvpM();
    }

    public void toHandleActionEvent(ActionBusEvent actionBusEvent){
        if(actionBusEvent == null || !actionBusEvent.ifValid())
            return;
        if(actionBusEvent.getEventType() == GtdBusEventType.LOAD)
            toLoadAllItem();
    }

    public void toLoadAllItem() {
        mvpM.loadAllEntity()
            .compose(((RxFragment)mvpV.getRxLifeCycleBindView()).<GtdActionEntity>bindUntilEvent(FragmentEvent.DESTROY))
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySafeSubscriber<GtdActionEntity>() {
                List<GtdActionEntity> entityList = Lists.newArrayList();
                @Override
                public void onSubscribe(Subscription s) {
                    super.onSubscribe(s);
                    entityList.clear();
                    if(mvpV != null)
                        mvpV.onItemLoadStart();
                }

                @Override
                public void onNext(GtdActionEntity entity) {
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

    public void toDeleteItem(@NonNull Flowable<GtdActionEntity>  entityRx) {
        entityRx.subscribe(new MySafeSubscriber<GtdActionEntity>() {
                    @Override
                    public void onNext(GtdActionEntity entity) {
                        super.onNext(entity);
                        EventBusFatory.getInstance().getDefaultEventBus().postSticky(new ActionBusEvent(GtdBusEventType.DELETE, entity));

                        boolean delSucc = entity.ifValid() && GtdActionByUuidFactory.getInstance().remove(entity.getUuid()) != null;
                        if(mvpV != null && delSucc)
                            mvpV.onDeleteSucc(INVALID_INDEX, entity);
                        else if (mvpV != null)
                            mvpV.onDeleteFail(entity);
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

    public void onItemCreated(GtdActionEntity entity) {
        /*entityList.add(entity);
        if(mvpV != null)
            mvpV.resetView(entityList);*/
    }

    public void onItemEdited(int index, GtdActionEntity entity) {
        /*entityList.set(index, entity);
        if(mvpV != null)
            mvpV.resetView(index, entity);*/
    }

    class MaybeActListMvpM {
        Flowable<GtdActionEntity> loadAllEntity() {
            return GtdActionByUuidFactory.getInstance().getEntityByState(ActState.MAYBE);
        }
    }

    public interface IGtdActionListMvpV extends IAllItemListMvpV<GtdActionEntity> {
    }
}
package ahtewlg7.utimer.mvp.rw;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdActionByUuidFactory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/7.
 */
class TableActionRwMvpP extends AUtimerRwMvpP<GtdActionEntity, TableActionRwMvpM> {

    public TableActionRwMvpP(IDbMvpV mvpV){
        super(mvpV);
    }

    public void toHandleBusEvent(ActionBusEvent actionBusEvent){
        if(actionBusEvent == null || !actionBusEvent.ifValid())
            return;
        switch (actionBusEvent.getEventType()){
            case LOAD:
                break;
            case SAVE:
                toSave(Flowable.just(actionBusEvent.getActionEntity()));
                break;
            case DELETE:
                toDel(Flowable.just(actionBusEvent.getActionEntity()));
                break;
        }
    }

    @Override
    protected TableActionRwMvpM getMvpM() {
        return new TableActionRwMvpM();
    }

    @Override
    protected MySafeSubscriber<Boolean> getSaveSubscriber() {
        return new MySafeSubscriber<Boolean>();
    }

    @Override
    protected MySafeSubscriber<Boolean> getDelSubscriber() {
        return new MySafeSubscriber<Boolean>();
    }

    @Override
    protected MySafeSubscriber<GtdActionEntity> getLoadAllSubscriber() {
        return new MySafeSubscriber<GtdActionEntity>() {
            @Override
            public void onSubscribe(Subscription s) {
                super.onSubscribe(s);
                GtdActionByUuidFactory.getInstance().clearAll();
                if(mvpV != null)
                    mvpV.onAllLoadStarted();
            }

            @Override
            public void onNext(GtdActionEntity entity) {
                super.onNext(entity);
                GtdActionByUuidFactory.getInstance().add(entity.getUuid(), entity);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                isLoaded = true;
                toPostEndEvent();
                if(mvpV != null)
                    mvpV.onAllLoadEnd();
            }
        };
    }

    private void toPostEndEvent(){
        ActionBusEvent actionBusEvent = new ActionBusEvent(GtdBusEventType.LOAD);
        EventBusFatory.getInstance().getDefaultEventBus().post(actionBusEvent);
    }
}

package ahtewlg7.utimer.mvp.db;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.factory.GtdActionByUuidFactory;
import ahtewlg7.utimer.mvp.ADbMvpP;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/7.
 */
class TableActionMvpP extends ADbMvpP<GtdActionEntity, TableActionMvpM> {

    public TableActionMvpP(IDbMvpV mvpV){
        super(mvpV);
    }

    public void toHandleActionEvent(ActionBusEvent actionBusEvent){
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
    protected TableActionMvpM getMvpM() {
        return new TableActionMvpM();
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
            public void onError(Throwable t) {
                super.onError(t);
            }

            @Override
            public void onComplete() {
                super.onComplete();
                isLoaded = true;
                if(mvpV != null)
                    mvpV.onAllLoadEnd();
            }
        };
    }
}

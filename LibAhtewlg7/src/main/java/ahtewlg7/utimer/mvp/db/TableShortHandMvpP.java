package ahtewlg7.utimer.mvp.db;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.factory.ShortHandByUuidFactory;
import ahtewlg7.utimer.mvp.AUtimerRwMvpP;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/7.
 */
class TableShortHandMvpP extends AUtimerRwMvpP<ShortHandEntity, TableShortHandMvpM> {

    public TableShortHandMvpP(IDbMvpV mvpV){
        super(mvpV);
    }

    public void toHandleBusEvent(UTimerBusEvent busEvent){
        if(busEvent == null || !busEvent.ifValid())
            return;
        switch (busEvent.getEventType()){
            case LOAD:
                break;
            case SAVE:
                toSave(Flowable.just((ShortHandEntity)busEvent.getEntity()));
                break;
            case DELETE:
                toDel(Flowable.just((ShortHandEntity)busEvent.getEntity()));
                break;
        }
    }

    @Override
    protected TableShortHandMvpM getMvpM() {
        return new TableShortHandMvpM();
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
    protected MySafeSubscriber<ShortHandEntity> getLoadAllSubscriber() {
        return new MySafeSubscriber<ShortHandEntity>() {
            @Override
            public void onSubscribe(Subscription s) {
                super.onSubscribe(s);
                ShortHandByUuidFactory.getInstance().clearAll();
                if(mvpV != null)
                    mvpV.onAllLoadStarted();
            }

            @Override
            public void onNext(ShortHandEntity entity) {
                super.onNext(entity);
                ShortHandByUuidFactory.getInstance().add(entity.getUuid(), entity);
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

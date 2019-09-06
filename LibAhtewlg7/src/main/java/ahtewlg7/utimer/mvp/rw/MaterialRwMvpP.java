package ahtewlg7.utimer.mvp.rw;

import org.reactivestreams.Subscription;

import java.io.File;

import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.entity.gtd.MaterialEntity;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.functions.Action;

import static ahtewlg7.utimer.enumtype.GtdBusEventType.ON_LOAD;

/**
 * Created by lw on 2019/3/7.
 */
public class MaterialRwMvpP extends AUtimerRwMvpP<MaterialEntity, MaterialRwMvpM> {

    public MaterialRwMvpP(IDbMvpV mvpV){
        super(mvpV);
    }

    public void toHandleBusEvent(UTimerBusEvent busEvent){
        if(busEvent == null || !busEvent.ifValid())
            return;
        switch (busEvent.getEventType()){
            case LOAD:
                toLoadAll();
                break;
            case SAVE:
                toSave(Flowable.just((MaterialEntity)busEvent.getEntity()));
                break;
            case DELETE:
                toDel(Flowable.just((MaterialEntity)busEvent.getEntity()));
                break;
        }
    }

    @Override
    public void toLoadAll() {

    }

    @Override
    protected MaterialRwMvpM getMvpM() {
        return new MaterialRwMvpM();
    }

    @Override
    protected MySafeSubscriber<Boolean> getSaveSubscriber() {
        return new MySafeSubscriber<Boolean>();
    }

    @Override
    protected MySafeSubscriber<Boolean> getDelSubscriber() {
        return new MySafeSubscriber<Boolean>(){
            @Override
            public void onNext(Boolean aBoolean) {
                super.onNext(aBoolean);
            }
        };
    }

    @Override
    protected MySafeSubscriber<MaterialEntity> getLoadAllSubscriber() {
        return new MySafeSubscriber<MaterialEntity>() {
            @Override
            public void onSubscribe(Subscription s) {
                super.onSubscribe(s);
                if(mvpV != null)
                    mvpV.onAllLoadStarted();
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

    public void toLoad(File rootFile){
        mvpM.loadAll(rootFile).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                UTimerBusEvent busEvent = new UTimerBusEvent(ON_LOAD, null);
                EventBusFatory.getInstance().getDefaultEventBus().post(busEvent);
            }
        }).subscribe(getLoadAllSubscriber());
    }
}

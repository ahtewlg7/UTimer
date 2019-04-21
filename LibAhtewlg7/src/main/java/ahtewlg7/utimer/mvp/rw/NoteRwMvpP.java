package ahtewlg7.utimer.mvp.rw;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.NoteByUuidFactory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.functions.Action;

import static ahtewlg7.utimer.enumtype.GtdBusEventType.ON_LOAD;

/**
 * Created by lw on 2019/3/7.
 */
class NoteRwMvpP extends AUtimerRwMvpP<NoteEntity, NoteRwMvpM> {

    public NoteRwMvpP(IDbMvpV mvpV){
        super(mvpV);
    }

    public void toHandleBusEvent(UTimerBusEvent busEvent){
        if(busEvent == null || !busEvent.ifValid())
            return;
        switch (busEvent.getEventType()){
            case LOAD:
                toLoad((GtdProjectEntity) busEvent.getEntity());
                break;
            case SAVE:
                toSave(Flowable.just((NoteEntity)busEvent.getEntity()));
                break;
            case DELETE:
                toDel(Flowable.just((NoteEntity)busEvent.getEntity()));
                break;
        }
    }

    @Deprecated
    @Override
    public void toLoadAll() {
        //do nothing
    }

    @Override
    protected NoteRwMvpM getMvpM() {
        return new NoteRwMvpM();
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
    protected MySafeSubscriber<NoteEntity> getLoadAllSubscriber() {
        return new MySafeSubscriber<NoteEntity>() {
            @Override
            public void onSubscribe(Subscription s) {
                super.onSubscribe(s);
                if(mvpV != null)
                    mvpV.onAllLoadStarted();
            }

            @Override
            public void onNext(NoteEntity entity) {
                super.onNext(entity);
                NoteByUuidFactory.getInstance().update(entity.getUuid(), entity);
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

    private void toLoad(final GtdProjectEntity projectEntity){
        mvpM.loadAll(projectEntity).doOnComplete(new Action() {
            @Override
            public void run() throws Exception {
                UTimerBusEvent busEvent = new UTimerBusEvent(ON_LOAD, projectEntity);
                EventBusFatory.getInstance().getDefaultEventBus().post(busEvent);
            }
        }).subscribe(getLoadAllSubscriber());
    }
}

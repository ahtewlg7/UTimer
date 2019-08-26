package ahtewlg7.utimer.mvp.rw;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.busevent.DeedDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2019/3/7.
 */
class TableDeedRwMvpP extends AUtimerRwMvpP<GtdDeedEntity, TableDeedRwMvpM> {

    public TableDeedRwMvpP(IDbMvpV mvpV){
        super(mvpV);
    }

    public void toHandleBusEvent(DeedBusEvent actionBusEvent){
        if(actionBusEvent == null || !actionBusEvent.ifValid())
            return;
        switch (actionBusEvent.getEventType()){
            case LOAD:
                break;
            case CREATE:
                toSave(Flowable.just(actionBusEvent.getDeedEntity()).doOnNext(new Consumer<GtdDeedEntity>() {
                    @Override
                    public void accept(GtdDeedEntity entity) throws Exception {
                        GtdDeedByUuidFactory.getInstance().update(entity.getUuid(), entity);
                        toPostDoneEvent(GtdBusEventType.CREATE,entity);
                    }
                }));
                break;
            case SAVE:
                toSave(Flowable.just(actionBusEvent.getDeedEntity()).doOnNext(new Consumer<GtdDeedEntity>() {
                    @Override
                    public void accept(GtdDeedEntity entity) throws Exception {
                        GtdDeedByUuidFactory.getInstance().update(entity.getUuid(), entity);
                        toPostDoneEvent(GtdBusEventType.SAVE,entity);
                    }
                }));
                break;
            case EDIT:
                toSave(Flowable.just(actionBusEvent.getDeedEntity()).doOnNext(new Consumer<GtdDeedEntity>() {
                    @Override
                    public void accept(GtdDeedEntity entity) throws Exception {
                        GtdDeedByUuidFactory.getInstance().update(entity.getUuid(), entity);
                        toPostDoneEvent(GtdBusEventType.EDIT,entity);
                    }
                }));
                break;
            case DELETE:
                toDel(Flowable.just(actionBusEvent.getDeedEntity()).doOnNext(new Consumer<GtdDeedEntity>() {
                    @Override
                    public void accept(GtdDeedEntity entity) throws Exception {
                        GtdDeedByUuidFactory.getInstance().remove(entity.getUuid());
                        toPostDoneEvent(GtdBusEventType.DELETE,entity);
                    }
                }));
                break;
        }
    }

    @Override
    protected TableDeedRwMvpM getMvpM() {
        return new TableDeedRwMvpM();
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
    protected MySafeSubscriber<GtdDeedEntity> getLoadAllSubscriber() {
        return new MySafeSubscriber<GtdDeedEntity>() {
            @Override
            public void onSubscribe(Subscription s) {
                super.onSubscribe(s);
                GtdDeedByUuidFactory.getInstance().clearAll();
                if(mvpV != null)
                    mvpV.onAllLoadStarted();
            }

            @Override
            public void onNext(GtdDeedEntity entity) {
                super.onNext(entity);
                Logcat.i("TableActionRw", "onNext :" + entity.toString());
                GtdDeedByUuidFactory.getInstance().add(entity.getUuid(), entity);
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
        DeedBusEvent deedBusEvent = new DeedBusEvent(GtdBusEventType.LOAD);
        EventBusFatory.getInstance().getDefaultEventBus().post(deedBusEvent);
    }
    private void toPostDoneEvent(GtdBusEventType type, GtdDeedEntity entity){
        if(type == null || entity == null)
            return;
        DeedDoneBusEvent deedBusEvent = new DeedDoneBusEvent(type, entity);
        EventBusFatory.getInstance().getDefaultEventBus().post(deedBusEvent);
    }
}

package ahtewlg7.utimer.mvp.rw;

import org.reactivestreams.Subscription;

import ahtewlg7.utimer.entity.busevent.ProjectBusEvent;
import ahtewlg7.utimer.entity.busevent.ProjectDoneBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.ProjectByUuidFactory;
import ahtewlg7.utimer.util.MySafeSubscriber;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2019/3/7.
 */
class TableProjectRwMvpP extends AUtimerRwMvpP<GtdProjectEntity, TableProjectRwMvpM> {

    public TableProjectRwMvpP(IDbMvpV mvpV){
        super(mvpV);
    }

    public void toHandleBusEvent(ProjectBusEvent busEvent){
        if(busEvent == null || !busEvent.ifValid())
            return;
        switch (busEvent.getEventType()){
            case LOAD:
                break;
            case CREATE:
                toSave(Flowable.just(busEvent.getProjectEntity()).doOnNext(new Consumer<GtdProjectEntity>() {
                    @Override
                    public void accept(GtdProjectEntity entity) throws Exception {
                        ProjectByUuidFactory.getInstance().update(entity.getUuid(), entity);
                        toPostDoneEvent(GtdBusEventType.CREATE,entity);
                    }
                }));
                break;
            case SAVE:
                toSave(Flowable.just(busEvent.getProjectEntity()).doOnNext(new Consumer<GtdProjectEntity>() {
                    @Override
                    public void accept(GtdProjectEntity entity) throws Exception {
                        ProjectByUuidFactory.getInstance().update(entity.getUuid(), entity);
                        toPostDoneEvent(GtdBusEventType.SAVE,entity);
                    }
                }));
                break;
            case EDIT:
                toSave(Flowable.just(busEvent.getProjectEntity()).doOnNext(new Consumer<GtdProjectEntity>() {
                    @Override
                    public void accept(GtdProjectEntity entity) throws Exception {
                        ProjectByUuidFactory.getInstance().update(entity.getUuid(), entity);
                        toPostDoneEvent(GtdBusEventType.EDIT,entity);
                    }
                }));
                break;
            case DELETE:
                toDel(Flowable.just(busEvent.getProjectEntity()).doOnNext(new Consumer<GtdProjectEntity>() {
                    @Override
                    public void accept(GtdProjectEntity entity) throws Exception {
                        ProjectByUuidFactory.getInstance().remove(entity.getUuid());
                        toPostDoneEvent(GtdBusEventType.DELETE,entity);
                    }
                }));
                break;
        }
    }

    @Override
    protected TableProjectRwMvpM getMvpM() {
        return new TableProjectRwMvpM();
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
    protected MySafeSubscriber<GtdProjectEntity> getLoadAllSubscriber() {
        return new MySafeSubscriber<GtdProjectEntity>() {
            @Override
            public void onSubscribe(Subscription s) {
                super.onSubscribe(s);
                ProjectByUuidFactory.getInstance().clearAll();
                if(mvpV != null)
                    mvpV.onAllLoadStarted();
            }

            @Override
            public void onNext(GtdProjectEntity entity) {
                super.onNext(entity);
                ProjectByUuidFactory.getInstance().add(entity.getUuid(), entity);
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
        ProjectBusEvent deedBusEvent = new ProjectBusEvent(GtdBusEventType.LOAD);
        EventBusFatory.getInstance().getDefaultEventBus().post(deedBusEvent);
    }
    private void toPostDoneEvent(GtdBusEventType type, GtdProjectEntity entity){
        if(type == null || entity == null)
            return;
        ProjectDoneBusEvent deedBusEvent = new ProjectDoneBusEvent(type, entity);
        EventBusFatory.getInstance().getDefaultEventBus().post(deedBusEvent);
    }
}

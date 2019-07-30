package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;

/**
 * Created by lw on 2019/4/6.
 */
class DeedBaseState extends GtdBaseState {

    protected DeedBaseState(GtdMachine gtdMachine){
        super(gtdMachine);
    }

    protected Optional<BaseEventBusBean> removeState(@NonNull AUtimerEntity entity){
        if(!ifTrashable(entity))
            return Optional.absent();
        DeedBusEvent busEvent = new DeedBusEvent(GtdBusEventType.DELETE, (GtdDeedEntity) entity);
        Optional<BaseEventBusBean> eventOptional = toPostEvent(entity, busEvent);
        if(eventOptional.isPresent())
            GtdDeedByUuidFactory.getInstance().remove(entity.getUuid());
        return eventOptional;
    }
    protected Optional<BaseEventBusBean> updateAndPostState(@NonNull DeedState state , @NonNull AUtimerEntity entity){
        if(!ifGtdable(entity))
            return Optional.absent();
        DeedState preState = toResetState((GtdDeedEntity) entity, state);
        DeedBusEvent busEvent = new DeedBusEvent(GtdBusEventType.SAVE, (GtdDeedEntity) entity);
        Optional<BaseEventBusBean> eventOptional = toPostEvent(entity, busEvent);
        if(eventOptional.isPresent())
            GtdDeedByUuidFactory.getInstance().updateState(preState, (GtdDeedEntity) entity);
        return eventOptional;
    }
    protected void updateState(@NonNull DeedState state , @NonNull AUtimerEntity entity){
        if(!ifGtdable(entity))
            return;
        DeedState preState =  toResetState((GtdDeedEntity) entity, state);
        GtdDeedByUuidFactory.getInstance().updateState(preState, (GtdDeedEntity) entity);
    }

    protected Optional<BaseEventBusBean> toPostEvent(AUtimerEntity entity, BaseEventBusBean busEvent){
        if(entity == null || busEvent == null)
            return Optional.absent();
        if(!ifHandlable(entity))
            busEvent.setPerform(false);
        else{
            EventBusFatory.getInstance().getDefaultEventBus().postSticky(busEvent);
            busEvent.setPerform(true);
        }
        return Optional.of(busEvent);
    }
    protected boolean ifHandlable(AUtimerEntity entity){
        return entity != null && entity.ifValid();
    }

    protected boolean ifTrashable(AUtimerEntity entity){
        return true;
    }
    protected boolean ifGtdable(AUtimerEntity entity){
        return entity != null && entity.ifValid()
                && entity.getClass().isAssignableFrom(GtdDeedEntity.class) ;
    }
    protected DeedState toResetState(@NonNull GtdDeedEntity entity, @NonNull DeedState state){
        DeedState preState =  entity.getDeedState();
        entity.setDeedState(state);
        if(state == DeedState.SCHEDULE)
            entity.setStartTime(DateTime.now());
        else if(state == DeedState.DONE || state == DeedState.TRASH)
            entity.setEndTime(DateTime.now());
        return preState;
    }
}

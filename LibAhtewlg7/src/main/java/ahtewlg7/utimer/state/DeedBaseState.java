package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;

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
        return  toPostEvent(entity, busEvent);
    }

    protected Optional<BaseEventBusBean> updateState(@NonNull DeedState state , @NonNull AUtimerEntity entity){
        if(!ifGtdable(entity))
            return Optional.absent();
        DeedState preState =  ((GtdDeedEntity) entity).getDeedState();
        ((GtdDeedEntity) entity).setDeedState(state);
        DeedBusEvent busEvent = new DeedBusEvent(GtdBusEventType.SAVE, (GtdDeedEntity) entity);
        return  toPostEvent(entity, busEvent);
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
        return false;
    }
    protected boolean ifGtdable(AUtimerEntity entity){
        return entity != null && entity.ifValid()
                && entity.getClass().isAssignableFrom(GtdDeedEntity.class) ;
    }
}

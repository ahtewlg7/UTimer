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
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;

/**
 * Created by lw on 2019/4/6.
 */
class BaseJobState extends BaseGtdState {

    protected BaseJobState(GtdMachine gtdMachine){
        super(gtdMachine);
    }

    protected Optional<BaseEventBusBean> updateState(@NonNull DeedState state , @NonNull AUtimerEntity entity){
        DeedState preState =  ((GtdDeedEntity) entity).getDeedState();
        ((GtdDeedEntity) entity).setDeedState(state);
        DeedBusEvent busEvent = new DeedBusEvent(GtdBusEventType.SAVE, (GtdDeedEntity) entity);
        Optional<BaseEventBusBean> eventOptional = toPostEvent(entity, busEvent);
        if(eventOptional.isPresent())
            GtdDeedByUuidFactory.getInstance().updateState(preState, (GtdDeedEntity) entity);
        return eventOptional;
    }

    protected Optional<BaseEventBusBean> toPostEvent(AUtimerEntity entity, BaseEventBusBean busEvent){
        if(entity == null || busEvent == null)
            return Optional.absent();
        if(!ifActHandlable(entity))
            busEvent.setPerform(false);
        else{
            EventBusFatory.getInstance().getDefaultEventBus().postSticky(busEvent);
            busEvent.setPerform(true);
        }
        return Optional.of(busEvent);
    }
    protected boolean ifActHandlable(AUtimerEntity entity){
        return entity != null && entity.ifValid()
                && entity.getClass().isAssignableFrom(GtdDeedEntity.class) ;
    }

    protected boolean toTrashable(GtdDeedEntity entity){
        return false;
    }
    protected boolean toGtdable(GtdDeedEntity entity){
        return false;
    }
}

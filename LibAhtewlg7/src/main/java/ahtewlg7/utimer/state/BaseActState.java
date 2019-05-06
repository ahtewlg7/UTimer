package ahtewlg7.utimer.state;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.factory.EventBusFatory;

/**
 * Created by lw on 2019/4/6.
 */
class BaseActState extends BaseGtdState {

    protected BaseActState(GtdMachine gtdMachine){
        super(gtdMachine);
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
                && entity.getClass().isAssignableFrom(GtdActionEntity.class) ;
    }

    protected boolean toTrashable(GtdActionEntity entity){
        return false;
    }
    protected boolean toGtdable(GtdActionEntity entity){
        return false;
    }
}

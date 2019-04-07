package ahtewlg7.utimer.state;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.factory.GtdActionByUuidFactory;

/**
 * Created by lw on 2019/4/6.
 */
public class GtdActState extends BaseGtdState<ActionBusEvent> {

    @Override
    public Optional<ActionBusEvent> toTrash(@NonNull AUtimerEntity entity) {
            ActionBusEvent busEvent = new ActionBusEvent(GtdBusEventType.DELETE, (GtdActionEntity) entity);
            if(!(entity.getClass().isAssignableFrom(GtdActionEntity.class)) && !entity.ifValid())
                busEvent.setPerform(false);
            else{
                GtdActionByUuidFactory.getInstance().remove(entity.getUuid());
                EventBusFatory.getInstance().getDefaultEventBus().postSticky(busEvent);
                busEvent.setPerform(true);
            }
            return Optional.of(busEvent);
    }

    private void toTrash(GtdActionEntity entity){
        if(entity.getActionState() == ActState.MAYBE)
            entity.setActionState(ActState.ELIMINATE);
    }
}

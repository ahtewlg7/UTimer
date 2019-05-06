package ahtewlg7.utimer.state;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.GtdActionByUuidFactory;

/**
 * Created by lw on 2019/4/6.
 */
public class ActMaybeState extends BaseActState {

    ActMaybeState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Optional<BaseEventBusBean> toTrash(@NonNull AUtimerEntity entity) {
        if(!ifActHandlable(entity) || !toTrashable((GtdActionEntity)entity))
            return Optional.absent();
        ((GtdActionEntity)entity).setActionState(ActState.TRASH);
        ActionBusEvent busEvent = new ActionBusEvent(GtdBusEventType.DELETE, (GtdActionEntity) entity);
        Optional<BaseEventBusBean> eventOptional =  toPostEvent(entity, busEvent);
        if(eventOptional.isPresent())
            GtdActionByUuidFactory.getInstance().remove(entity.getUuid());
        return eventOptional;
    }

    @Override
    public Optional<BaseEventBusBean> toGtd(@NonNull AUtimerEntity entity) {
        if(!ifActHandlable(entity))
            return Optional.absent();
        return getGtdMachine().getActGtdState().toGtd(entity);
    }

    @Override
    protected boolean toTrashable(GtdActionEntity entity){
        return entity.getActionState() == ActState.MAYBE;
    }
}

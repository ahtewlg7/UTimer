package ahtewlg7.utimer.state;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;

/**
 * Created by lw on 2019/4/6.
 */
public class ActGtdState extends BaseActState{

    ActGtdState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Optional<BaseEventBusBean> toGtd(@NonNull AUtimerEntity entity) {
        if(!ifActHandlable(entity) || !toGtdable((GtdActionEntity)entity))
            return Optional.absent();
        ((GtdActionEntity) entity).setActionState(ActState.GTD);
        ActionBusEvent busEvent = new ActionBusEvent(GtdBusEventType.SAVE, (GtdActionEntity) entity);
        return toPostEvent(entity, busEvent);
    }

    @Override
    protected boolean toGtdable(GtdActionEntity entity){
        return entity.getActionState() == ActState.MAYBE;
    }
}

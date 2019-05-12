package ahtewlg7.utimer.state;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;

/**
 * Created by lw on 2019/4/6.
 */
public class ActGtdState extends BaseActState{

    ActGtdState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Optional<BaseEventBusBean> toGtd(@NonNull AUtimerEntity entity) {
        if(!ifActHandlable(entity) || !toGtdable((GtdDeedEntity)entity))
            return Optional.absent();
        DeedState preState =  ((GtdDeedEntity) entity).getDeedState();
        ((GtdDeedEntity) entity).setDeedState(DeedState.GTD);
        DeedBusEvent busEvent = new DeedBusEvent(GtdBusEventType.SAVE, (GtdDeedEntity) entity);
        Optional<BaseEventBusBean> eventOptional = toPostEvent(entity, busEvent);
        if(eventOptional.isPresent())
            GtdDeedByUuidFactory.getInstance().updateState(preState, (GtdDeedEntity)entity);
        return eventOptional;
    }

    @Override
    protected boolean toGtdable(GtdDeedEntity entity){
        return entity.getDeedState() == DeedState.MAYBE;
    }
}

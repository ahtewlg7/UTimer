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
public class ActMaybeState extends BaseActState {

    ActMaybeState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Optional<BaseEventBusBean> toTrash(@NonNull AUtimerEntity entity) {
        if(!ifActHandlable(entity) || !toTrashable((GtdDeedEntity)entity))
            return Optional.absent();
        ((GtdDeedEntity)entity).setDeedState(DeedState.TRASH);
        DeedBusEvent busEvent = new DeedBusEvent(GtdBusEventType.DELETE, (GtdDeedEntity) entity);
        Optional<BaseEventBusBean> eventOptional =  toPostEvent(entity, busEvent);
        if(eventOptional.isPresent())
            GtdDeedByUuidFactory.getInstance().remove(entity.getUuid());
        return eventOptional;
    }

    @Override
    public Optional<BaseEventBusBean> toGtd(@NonNull AUtimerEntity entity) {
        if(!ifActHandlable(entity))
            return Optional.absent();
        return getGtdMachine().getActGtdState().toGtd(entity);
    }

    @Override
    protected boolean toTrashable(GtdDeedEntity entity){
        return entity.getDeedState() == DeedState.MAYBE;
    }
}

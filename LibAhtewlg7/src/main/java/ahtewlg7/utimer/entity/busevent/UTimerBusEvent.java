package ahtewlg7.utimer.entity.busevent;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.IValidEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;

/**
 * Created by lw on 2019/3/13.
 */
public class UTimerBusEvent extends BaseEventBusBean implements IValidEntity {

    private GtdBusEventType eventType;
    private AUtimerEntity entity;

    public UTimerBusEvent(GtdBusEventType eventType){
        this.eventType      = eventType;
    }
    public UTimerBusEvent(GtdBusEventType eventType, AUtimerEntity entity) {
        this.eventType  = eventType;
        this.entity     = entity;
    }

    @Override
    public boolean ifValid() {
        return eventType != null && (eventType == GtdBusEventType.LOAD || entity != null);
    }

    public GtdBusEventType getEventType() {
        return eventType;
    }

    public AUtimerEntity getEntity() {
        return entity;
    }
}

package ahtewlg7.utimer.entity.busevent;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;

/**
 * Created by lw on 2019/3/13.
 */
public class DeedBusEvent extends BaseEventBusBean {

    private GtdBusEventType eventType;
    private GtdDeedEntity actionEntity;

    public DeedBusEvent(GtdBusEventType eventType){
        this.eventType      = eventType;
    }
    public DeedBusEvent(GtdBusEventType eventType, GtdDeedEntity actionEntity) {
        this.eventType      = eventType;
        this.actionEntity   = actionEntity;
    }

    public GtdBusEventType getEventType() {
        return eventType;
    }

    public GtdDeedEntity getActionEntity() {
        return actionEntity;
    }
}

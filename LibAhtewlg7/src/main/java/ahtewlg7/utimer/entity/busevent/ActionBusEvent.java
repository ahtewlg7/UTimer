package ahtewlg7.utimer.entity.busevent;

import ahtewlg7.utimer.entity.IEventBusBean;
import ahtewlg7.utimer.entity.IValidEntity;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;

/**
 * Created by lw on 2019/3/13.
 */
public class ActionBusEvent implements IEventBusBean , IValidEntity {
    private GtdBusEventType eventType;
    private GtdActionEntity actionEntity;

    public ActionBusEvent(GtdBusEventType eventType){
        this.eventType      = eventType;
    }
    public ActionBusEvent(GtdBusEventType eventType, GtdActionEntity actionEntity) {
        this.eventType      = eventType;
        this.actionEntity   = actionEntity;
    }

    @Override
    public boolean ifValid() {
        return eventType != null && (eventType == GtdBusEventType.LOAD || actionEntity != null);
    }

    public GtdBusEventType getEventType() {
        return eventType;
    }

    public GtdActionEntity getActionEntity() {
        return actionEntity;
    }
}

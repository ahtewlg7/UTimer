package ahtewlg7.utimer.entity.busevent;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;

/**
 * Created by lw on 2019/3/13.
 */
public class DeedDoneBusEvent extends BaseEventBusBean {
    private GtdBusEventType eventType;
    private GtdDeedEntity deedEntity;

    public DeedDoneBusEvent(GtdBusEventType eventType, GtdDeedEntity deedEntity) {
        this.eventType  = eventType;
        this.deedEntity = deedEntity;
    }

    public GtdBusEventType getEventType() {
        return eventType;
    }

    public GtdDeedEntity getDeedEntity() {
        return deedEntity;
    }

}

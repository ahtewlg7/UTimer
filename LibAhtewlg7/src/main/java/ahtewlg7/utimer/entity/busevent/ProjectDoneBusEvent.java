package ahtewlg7.utimer.entity.busevent;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;

/**
 * Created by lw on 2019/3/13.
 */
public class ProjectDoneBusEvent extends BaseEventBusBean {
    private GtdBusEventType eventType;
    private GtdProjectEntity entity;

    public ProjectDoneBusEvent(GtdBusEventType eventType, GtdProjectEntity entity) {
        this.eventType  = eventType;
        this.entity = entity;
    }

    public GtdBusEventType getEventType() {
        return eventType;
    }

    public GtdProjectEntity getEntity() {
        return entity;
    }

    @Override
    public boolean ifValid() {
        return eventType != null && entity != null ;
    }
}

package ahtewlg7.utimer.entity.busevent;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.enumtype.GtdBusEventType;

/**
 * Created by lw on 2019/3/13.
 */
public class ProjectBusEvent extends BaseEventBusBean {

    private GtdBusEventType eventType;
    private GtdProjectEntity projectEntity;

    public ProjectBusEvent(GtdBusEventType eventType){
        this.eventType      = eventType;
    }
    public ProjectBusEvent(GtdBusEventType eventType, GtdProjectEntity projectEntity) {
        this.eventType     = eventType;
        this.projectEntity = projectEntity;
    }

    public GtdBusEventType getEventType() {
        return eventType;
    }

    public GtdProjectEntity getProjectEntity() {
        return projectEntity;
    }
}

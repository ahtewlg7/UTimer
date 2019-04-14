package ahtewlg7.utimer.mvp.db;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.busevent.ActivityBusEvent;
import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.mvp.ADbMvpP;

/**
 * Created by lw on 2019/4/13.
 */
public class TableMvpP {
    private TableNextIdMvpP tableNextIdMvpP;
    private TableActionMvpP tableActionMvpP;
    private TableShortHandMvpP tableShortHandMvpP;

    public TableMvpP(ADbMvpP.IDbMvpV actionMvpV, ADbMvpP.IDbMvpV shortHandMvpV,
                     ADbMvpP.IDbMvpV nextIdMvpV){
        tableNextIdMvpP     = new TableNextIdMvpP(nextIdMvpV);
        tableActionMvpP     = new TableActionMvpP(actionMvpV);
        tableShortHandMvpP  = new TableShortHandMvpP(shortHandMvpV);
    }

    public void toLoadAllTable(){
        tableNextIdMvpP.toLoadAll();
        tableActionMvpP.toLoadAll();
    }

    public void toHandleBusEvent(BaseEventBusBean eventBusBean){
        if(eventBusBean == null || !eventBusBean.ifValid())
            return;
        if(eventBusBean instanceof ActivityBusEvent)
            toHandleBusEvent((ActivityBusEvent)eventBusBean);
        else if(eventBusBean instanceof ActionBusEvent)
            toHandleBusEvent((ActionBusEvent)eventBusBean);
        else if(eventBusBean instanceof UTimerBusEvent)
            toHandleBusEvent((UTimerBusEvent)eventBusBean);
    }
    private void toHandleBusEvent(ActivityBusEvent busEvent){
        if(busEvent.ifOnBackground())
            tableNextIdMvpP.toSaveAll();
    }
    private void toHandleBusEvent(ActionBusEvent busEvent){
        tableActionMvpP.toHandleBusEvent(busEvent);
    }
    private void toHandleBusEvent(UTimerBusEvent busEvent){
        if(busEvent.getEntity().getGtdType() == GtdType.SHORTHAND)
            tableShortHandMvpP.toHandleBusEvent(busEvent);
    }
}

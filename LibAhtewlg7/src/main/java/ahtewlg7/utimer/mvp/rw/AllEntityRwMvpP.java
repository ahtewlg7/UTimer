package ahtewlg7.utimer.mvp.rw;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.entity.busevent.ActivityBusEvent;
import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2019/4/13.
 */
public class AllEntityRwMvpP {
    private TableActionRwMvpP actionMvpP;
    private NoteRwMvpP noteRwMvpP;
    private ShortHandRwMvpP shortHandMvpP;

    public AllEntityRwMvpP(AUtimerRwMvpP.IDbMvpV actionMvpV, AUtimerRwMvpP.IDbMvpV shortHandMvpV,
                           AUtimerRwMvpP.IDbMvpV noteMvpV){
        actionMvpP      = new TableActionRwMvpP(actionMvpV);
        shortHandMvpP   = new ShortHandRwMvpP(shortHandMvpV);
        noteRwMvpP      = new NoteRwMvpP(noteMvpV);
    }

    public void toLoadAll(){
        actionMvpP.toLoadAll();
        shortHandMvpP.toLoadAll();
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
//        if(busEvent.ifOnBackground())
//            nextIdMvpP.toSaveAll();
    }
    private void toHandleBusEvent(ActionBusEvent busEvent){
        actionMvpP.toHandleBusEvent(busEvent);
    }
    private void toHandleBusEvent(UTimerBusEvent busEvent){
        if(busEvent.getEntity() == null)//todo
            return;
        if(busEvent.getEntity().getGtdType() == GtdType.SHORTHAND)
            shortHandMvpP.toHandleBusEvent(busEvent);
        else if(busEvent.getEntity().getGtdType() == GtdType.PROJECT)
            noteRwMvpP.toHandleBusEvent(busEvent);
    }
}

package ahtewlg7.utimer.mvp.rw;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.ActivityBusEvent;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;

/**
 * Created by lw on 2019/4/13.
 */
public class AllEntityRwMvpP {
    private TableDeedRwMvpP deedMvpP;
    private MaterialRwMvpP materialMvpP;

    public AllEntityRwMvpP(AUtimerRwMvpP.IDbMvpV actionMvpV, AUtimerRwMvpP.IDbMvpV shortHandMvpV,
                           AUtimerRwMvpP.IDbMvpV noteMvpV){
        deedMvpP        = new TableDeedRwMvpP(actionMvpV);
        materialMvpP    = new MaterialRwMvpP(shortHandMvpV);
    }

    public void toLoadAll(){
        deedMvpP.toLoadAll();
//        materialMvpP.toLoadAll();
    }

    public void toHandleBusEvent(BaseEventBusBean eventBusBean){
        if(eventBusBean == null || !eventBusBean.ifValid())
            return;
        if(eventBusBean instanceof ActivityBusEvent)
            toHandleBusEvent((ActivityBusEvent)eventBusBean);
        else if(eventBusBean instanceof DeedBusEvent)
            toHandleBusEvent((DeedBusEvent)eventBusBean);
        else if(eventBusBean instanceof UTimerBusEvent)
            toHandleBusEvent((UTimerBusEvent)eventBusBean);
    }
    private void toHandleBusEvent(ActivityBusEvent busEvent){
//        if(busEvent.ifOnBackground())
//            nextIdMvpP.toSaveAll();
    }
    private void toHandleBusEvent(DeedBusEvent busEvent){
        deedMvpP.toHandleBusEvent(busEvent);
    }
    private void toHandleBusEvent(UTimerBusEvent busEvent){
        if(busEvent.getEntity() == null)//todo
            return;
    }
}

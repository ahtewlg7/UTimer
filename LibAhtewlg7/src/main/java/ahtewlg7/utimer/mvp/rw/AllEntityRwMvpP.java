package ahtewlg7.utimer.mvp.rw;

import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.busevent.ActivityBusEvent;
import ahtewlg7.utimer.entity.busevent.DeedBusEvent;
import ahtewlg7.utimer.entity.busevent.ProjectBusEvent;
import ahtewlg7.utimer.entity.busevent.UTimerBusEvent;

/**
 * Created by lw on 2019/4/13.
 */
public class AllEntityRwMvpP {
    private TableDeedRwMvpP deedRwMvpP;
//    private TableProjectRwMvpP projectRwMvpP;

    public AllEntityRwMvpP(AUtimerRwMvpP.IDbMvpV deedMvpV, AUtimerRwMvpP.IDbMvpV projectMvpV,
                           AUtimerRwMvpP.IDbMvpV noteMvpV){
        deedRwMvpP    = new TableDeedRwMvpP(deedMvpV);
//        projectRwMvpP = new TableProjectRwMvpP(projectMvpV);
    }

    public void toLoadAll(){
        deedRwMvpP.toLoadAll();
//        projectRwMvpP.toLoadAll();
    }

    public void toHandleBusEvent(BaseEventBusBean eventBusBean){
        if(eventBusBean == null || !eventBusBean.ifValid())
            return;
        if(eventBusBean instanceof ActivityBusEvent)
            toHandleBusEvent((ActivityBusEvent)eventBusBean);
        else if(eventBusBean instanceof DeedBusEvent)
            toHandleBusEvent((DeedBusEvent)eventBusBean);
        else if(eventBusBean instanceof ProjectBusEvent)
            toHandleBusEvent((ProjectBusEvent)eventBusBean);
        else if(eventBusBean instanceof UTimerBusEvent)
            toHandleBusEvent((UTimerBusEvent)eventBusBean);
    }
    private void toHandleBusEvent(ActivityBusEvent busEvent){
//        if(busEvent.ifOnBackground())
//            nextIdMvpP.toSaveAll();
    }
    private void toHandleBusEvent(DeedBusEvent busEvent){
        deedRwMvpP.toHandleBusEvent(busEvent);
    }

    private void toHandleBusEvent(ProjectBusEvent busEvent){
//        projectRwMvpP.toHandleBusEvent(busEvent);
    }
    //todo
    private void toHandleBusEvent(UTimerBusEvent busEvent){
        if(busEvent.getEntity() == null)//todo
            return;
    }
}

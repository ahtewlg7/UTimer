package ahtewlg7.utimer.mvp.db;

import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.mvp.ADbMvpP;

/**
 * Created by lw on 2019/4/13.
 */
public class TableMvpP {
    private TableNextIdMvpP tableNextIdMvpP;
    private TableActionMvpP tableActionMvpP;

    private ADbMvpP.IDbMvpV actionMvpV;
    private ADbMvpP.IDbMvpV nextIdMvpV;

    public TableMvpP(ADbMvpP.IDbMvpV actionMvpV, ADbMvpP.IDbMvpV nextIdMvpV){
        this.actionMvpV     = actionMvpV;
        this.nextIdMvpV     = nextIdMvpV;

        tableNextIdMvpP     = new TableNextIdMvpP(nextIdMvpV);
        tableActionMvpP     = new TableActionMvpP(actionMvpV);
    }

    public void toLoadAllTable(){
        tableNextIdMvpP.toLoadAll();
        tableActionMvpP.toLoadAll();
    }
    public void toSaveNextIdTable(){
        tableNextIdMvpP.toSaveAll();
    }
    public void toHandleBusEvent(ActionBusEvent actionBusEvent){
        tableActionMvpP.toHandleActionEvent(actionBusEvent);
    }
}

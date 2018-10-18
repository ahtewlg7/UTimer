package ahtewlg7.utimer.gtd;

import com.google.common.base.Optional;

import java.util.Hashtable;

import ahtewlg7.utimer.entity.un.IUtimerEntity;
import ahtewlg7.utimer.enumtype.GtdType;

public class GtdMachine {
    public static final String TAG = GtdMachine.class.getSimpleName();

    private static GtdMachine instance;

    private Hashtable<GtdType, BaseGtdState> stateTable;
    private BaseGtdState inboxState;
    private BaseGtdState actionState;
    private BaseGtdState projectState;
    private BaseGtdState dailyState;

    private GtdMachine(){
        stateTable   = new Hashtable<GtdType, BaseGtdState>();

        inboxState   = new GtdInboxState(this);
        actionState  = new GtdActionState(this);
        projectState = new GtdProjectState(this);
        dailyState   = new DailyGtdState(this);

        stateTable.put(GtdType.INBOX, inboxState);
        stateTable.put(GtdType.PROJECT, projectState);
        stateTable.put(GtdType.ACTION, actionState);
        stateTable.put(GtdType.DAILY, dailyState);
    }

    public static GtdMachine getInstance(){
        if(instance == null)
            instance = new GtdMachine();
        return instance;
    }

    public Optional<BaseGtdState> getCurrState(IUtimerEntity utimerEntity){
        if(utimerEntity == null)
            return Optional.absent();

        BaseGtdState currGtdSate = null;
        switch (utimerEntity.getGtdType()){
            case INBOX:
                currGtdSate = getInboxState();
                break;
            case ACTION:
                currGtdSate = getActionState();
                break;
            case PROJECT:
                currGtdSate = getProjectState();
                break;
            case DAILY:
                currGtdSate = getDailyState();
                break;
        }
        return Optional.fromNullable(currGtdSate);
    }

    public BaseGtdState getInboxState() {
        return stateTable.get(GtdType.INBOX);
    }

    public BaseGtdState getActionState() {
        return stateTable.get(GtdType.ACTION);
    }

    public BaseGtdState getProjectState() {
        return stateTable.get(GtdType.PROJECT);
    }

    public BaseGtdState getDailyState() {
        return stateTable.get(GtdType.DAILY);
    }
}

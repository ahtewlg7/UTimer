package ahtewlg7.utimer.state;


import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdType;

public class GtdMachine {
    public static final String TAG = GtdMachine.class.getSimpleName();

    private static GtdMachine instance;

    private GtdBaseState baseState;
    private DeedMaybeState maybeState;
    private DeedInboxState workState;
    private DeedEndState endState;
    private DeedTrashState trashState;

    private GtdMachine(){
        baseState       = new GtdBaseState(this);
        maybeState      = new DeedMaybeState(this);
        workState       = new DeedInboxState(this);
        endState        = new DeedEndState(this);
        trashState      = new DeedTrashState(this);
    }

    public static GtdMachine getInstance(){
        if(instance == null)
            instance = new GtdMachine();
        return instance;
    }

    public GtdBaseState getCurrState(AUtimerEntity entity){
        if(entity == null || entity.getGtdType() != GtdType.DEED)
            return baseState;
        GtdBaseState state = baseState;
        DeedState deedState = ((GtdDeedEntity)entity).getDeedState();
        switch (deedState){
            case MAYBE:
                state = maybeState;
                break;
            case SCHEDULE:
            case INBOX:
            case ONE_QUARTER:
            case DEFER:
            case CALENDAR:
            case DELEGATE:
            case PROJECT:
            case WISH:
            case USELESS:
                state = workState;
                break;
            case DONE:
                state = endState;
                break;
            case TRASH:
                state = trashState;
                break;
            default:
                state = baseState;
                break;
            /*REFERENCE(10)*/
        }
        return state;
    }

    DeedMaybeState getMaybeState() {
        return maybeState;
    }

    DeedInboxState getWorkState() {
        return workState;
    }

    DeedEndState getEndState() {
        return endState;
    }

    DeedTrashState getTrashState() {
        return trashState;
    }
}

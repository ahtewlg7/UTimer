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
    private DeedInboxState inboxState;
    private DeedDoneState doneState;

    private GtdMachine(){
        baseState       = new GtdBaseState(this);
        maybeState      = new DeedMaybeState(this);
        inboxState      = new DeedInboxState(this);
        doneState       = new DeedDoneState(this);
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
            case INBOX:
            case TWO_MIN:
            case DEFER:
            case CALENDAR:
            case DELEGATE:
            case PROJECT:
                state = inboxState;
                break;
            case DONE:
                state = doneState;
                break;
            default:
                state = baseState;
                break;
            /*TRASH(1),
                    REFERENCE(10),
                    WISH(4),
                    DONE(3),
                    USELESS(11);*/
        }
        return state;
    }

    DeedMaybeState getMaybeState() {
        return maybeState;
    }

    DeedInboxState getInboxState() {
        return inboxState;
    }

    DeedDoneState getDoneState() {
        return doneState;
    }
}

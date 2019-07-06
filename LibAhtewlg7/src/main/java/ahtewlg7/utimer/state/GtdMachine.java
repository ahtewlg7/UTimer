package ahtewlg7.utimer.state;


import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.enumtype.GtdType;

public class GtdMachine {
    public static final String TAG = GtdMachine.class.getSimpleName();

    private static GtdMachine instance;

    private BaseGtdState baseState;
    private ActMaybeJobState actMaybeState;
    private ActInboxJobState actGtdState;

    private GtdMachine(){
        baseState       = new BaseGtdState(this);
        actMaybeState   = new ActMaybeJobState(this);
        actGtdState     = new ActInboxJobState(this);
    }

    public static GtdMachine getInstance(){
        if(instance == null)
            instance = new GtdMachine();
        return instance;
    }

    public BaseGtdState getCurrState(AUtimerEntity entity){
        if(entity == null )
            return baseState;
        if(entity.getGtdType() == GtdType.DEED
                && ((GtdDeedEntity)entity).getDeedState() == DeedState.MAYBE){
            return actMaybeState;
        }else if(entity.getGtdType() == GtdType.DEED){
            return actGtdState;
        }else
            return baseState;
    }

    ActMaybeJobState getActMaybeState() {
        return actMaybeState;
    }

    ActInboxJobState getActGtdState() {
        return actGtdState;
    }
}

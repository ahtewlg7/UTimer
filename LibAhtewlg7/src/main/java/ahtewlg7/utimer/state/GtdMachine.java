package ahtewlg7.utimer.state;


import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.enumtype.GtdType;

public class GtdMachine {
    public static final String TAG = GtdMachine.class.getSimpleName();

    private static GtdMachine instance;

    private BaseGtdState baseState;
    private ActMaybeState actMaybeState;
    private ActGtdState actGtdState;

    private GtdMachine(){
        baseState       = new BaseGtdState(this);
        actMaybeState   = new ActMaybeState(this);
        actGtdState     = new ActGtdState(this);
    }

    public static GtdMachine getInstance(){
        if(instance == null)
            instance = new GtdMachine();
        return instance;
    }

    public BaseGtdState getCurrState(AUtimerEntity entity){
        if(entity.getGtdType() == GtdType.DEED
                && ((GtdDeedEntity)entity).getActionState() == ActState.MAYBE){
            return actMaybeState;
        }else if(entity.getGtdType() == GtdType.DEED
                && ((GtdDeedEntity)entity).getActionState() == ActState.GTD){
            return actGtdState;
        }else
            return baseState;
    }

    ActMaybeState getActMaybeState() {
        return actMaybeState;
    }

    ActGtdState getActGtdState() {
        return actGtdState;
    }
}

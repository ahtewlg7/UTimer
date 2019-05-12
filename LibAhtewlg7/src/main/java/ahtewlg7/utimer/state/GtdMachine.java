package ahtewlg7.utimer.state;


import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
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
                && ((GtdDeedEntity)entity).getDeedState() == DeedState.MAYBE){
            return actMaybeState;
        }else if(entity.getGtdType() == GtdType.DEED
                && ((GtdDeedEntity)entity).getDeedState() == DeedState.GTD){
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

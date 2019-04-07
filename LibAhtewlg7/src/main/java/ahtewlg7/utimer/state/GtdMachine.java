package ahtewlg7.utimer.state;


import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.enumtype.GtdType;

public class GtdMachine {
    public static final String TAG = GtdMachine.class.getSimpleName();

    private static GtdMachine instance;

    private BaseGtdState baseState;
    private GtdActState actState;

    private GtdMachine(){
        baseState   = new BaseGtdState();
        actState    = new GtdActState();
    }

    public static GtdMachine getInstance(){
        if(instance == null)
            instance = new GtdMachine();
        return instance;
    }

    public BaseGtdState getCurrState(AUtimerEntity entity){
        BaseGtdState state = baseState;
        if(entity.getGtdType() == GtdType.ACTION){
            state = actState;
        }
        return state;
    }

    GtdActState getActState() {
        return actState;
    }
}

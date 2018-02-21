package ahtewlg7.utimer.GTD.state;

import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.exception.GtdMachineException;

/**
 * Created by lw on 2017/10/3.
 */

public class DoneGtdState extends GtdState {
    public static final String TAG = DoneGtdState.class.getSimpleName();

    public DoneGtdState(GtdStateMachine gtdStateMachine){
        super(gtdStateMachine);
    }

    @Override
    void toDone() throws RuntimeException{
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        else if(gtdEntity.isTrashed()){
            gtdEntity.setTrashed(false);
            gtdEntity.setHolded(true);
        }else {
            gtdEntity.setHolded(true);
        }
    }
}

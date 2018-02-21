package ahtewlg7.utimer.GTD.state;

import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.exception.GtdMachineException;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2017/10/3.
 */

class TrashGtdState extends GtdState {
    public static final String TAG = TrashGtdState.class.getSimpleName();

    TrashGtdState(GtdStateMachine gtdStateMachine){
        super(gtdStateMachine);
    }

    @Override
    void toDelete() throws RuntimeException {
        if(gtdEntity == null) {
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        }else if(!gtdEntity.isTrashed()){
            throw new GtdMachineException(GtdErrCode.ERR_DELETE_UNTRASHED);
        }else{
            // TODO: 2017/11/5
            Logcat.d(TAG,"to delete");
        }
    }

    @Override
    void toTrash() throws RuntimeException{
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        gtdEntity.setTrashed(true);
    }

    @Override
    void toUnTrash() throws RuntimeException{
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        gtdEntity.setTrashed(false);
    }
}

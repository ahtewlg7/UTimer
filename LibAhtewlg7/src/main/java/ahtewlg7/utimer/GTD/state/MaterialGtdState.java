package ahtewlg7.utimer.GTD.state;

import ahtewlg7.utimer.entity.gtd.GtdMaterialEntity;
import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.exception.GtdMachineException;
import ahtewlg7.utimer.exception.GtdTrashException;

/**
 * Created by lw on 2017/10/3.
 */

public class MaterialGtdState extends GtdState{
    public static final String TAG = MaterialGtdState.class.getSimpleName();

    public MaterialGtdState(GtdStateMachine gtdStateMachine){
        super(gtdStateMachine);
    }

    @Override
    void toTrash() throws RuntimeException{
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        else if(gtdEntity.getTaskType() != GtdType.MATERIAL)
            super.toTrash();
        else if(((GtdMaterialEntity)gtdEntity).getReferenceNum() > 0)
            throw new GtdTrashException(GtdErrCode.ERR_TRASH_DISABLE);
        else
            gtdStateMachine.getTrashState().setCurrGtdEntity(gtdEntity).toTrash();
    }

    @Override
    void toUnTrash() throws RuntimeException{
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        else if(gtdEntity.getTaskType() != GtdType.MATERIAL)
            super.toUnTrash();
        else if(((GtdMaterialEntity)gtdEntity).getReferenceNum() <= 0)
            throw new GtdTrashException(GtdErrCode.ERR_UNTRASH_DISABLE);
        else
            gtdStateMachine.getTrashState().setCurrGtdEntity(gtdEntity).toUnTrash();
    }

    @Override
    void toDelete() throws RuntimeException {
        toTrash();
        gtdStateMachine.getTrashState().setCurrGtdEntity(gtdEntity).toDelete();
    }
}

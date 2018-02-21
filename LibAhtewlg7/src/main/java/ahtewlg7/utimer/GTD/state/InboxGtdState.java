package ahtewlg7.utimer.GTD.state;

import ahtewlg7.utimer.GTD.priority.PriorityTaskStepAction;
import ahtewlg7.utimer.entity.TaskEnvisionBean;
import ahtewlg7.utimer.entity.TaskPricipleBean;
import ahtewlg7.utimer.entity.TaskPurposeBean;
import ahtewlg7.utimer.entity.TaskStepBean;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdNewEntity;
import ahtewlg7.utimer.entity.gtd.GtdOnlineEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.GtdWeekPreviewEntity;
import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.exception.GtdIncomingException;
import ahtewlg7.utimer.exception.GtdMachineException;
import io.reactivex.Observable;

/**
 * Created by lw on 2017/10/3.
 */

public class InboxGtdState extends TrashGtdState {
    public static final String TAG = InboxGtdState.class.getSimpleName();

    protected GtdInboxEntity currGtdInboxEntity;
    protected GtdProjectEntity gtdProjectEntity;
    protected GtdWeekPreviewEntity gtdWeekPreviewEntity;

    public InboxGtdState(GtdStateMachine gtdStateMachine){
        super(gtdStateMachine);
    }

    //to incoming collection container
    @Override
    GtdInboxEntity toIncoming() throws RuntimeException {
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        else if(gtdEntity.getTaskType() != GtdType.ONLINE || gtdEntity.getTaskType() != GtdType.NEW)
            super.toIncoming();
        else if(gtdEntity.getTaskType() == GtdType.ONLINE)
            currGtdInboxEntity = new GtdInboxEntity((GtdOnlineEntity)gtdEntity);
        else if(gtdEntity.getTaskType() == GtdType.NEW)
            currGtdInboxEntity = new GtdInboxEntity((GtdNewEntity) gtdEntity);
        return currGtdInboxEntity;
    }


    @Override
    GtdProjectEntity toDefineProject() throws RuntimeException {
        if(currGtdInboxEntity == null)
            toIncoming();
        gtdProjectEntity = gtdStateMachine.getProjectGtdState().setCurrGtdEntity(currGtdInboxEntity).toDefineProject();
        return gtdProjectEntity;
    }

    //to organize component, priority, sequence
    @Override
    Observable<TaskPurposeBean> toPurposeProject() throws RuntimeException {
        if(gtdProjectEntity == null)
            toDefineProject();
        return gtdStateMachine.getProjectGtdState().setCurrGtdEntity(currGtdInboxEntity).toPurposeProject();
    }

    @Override
    Observable<TaskPricipleBean> toPricipleProject() throws RuntimeException {
        if(gtdProjectEntity == null)
            toDefineProject();
        return gtdStateMachine.getProjectGtdState().setCurrGtdEntity(currGtdInboxEntity).toPricipleProject();
    }

    @Override
    Observable<TaskEnvisionBean> toEnvisionProject() throws RuntimeException{
        if(gtdProjectEntity == null)
            toDefineProject();
        return gtdStateMachine.getProjectGtdState().setCurrGtdEntity(currGtdInboxEntity).toEnvisionProject();
    }

    @Override
    GtdWeekPreviewEntity toPreviewWeekly() throws RuntimeException {
        if(gtdProjectEntity == null)
            toDefineProject();
        gtdWeekPreviewEntity = gtdStateMachine.getWeekPreviewState().setCurrGtdEntity(currGtdInboxEntity).toPreviewWeekly();
        return gtdWeekPreviewEntity;
    }

    @Override
    Observable<TaskStepBean> toStepAction() throws RuntimeException {
        if(currGtdInboxEntity == null)
            toIncoming();
        else if(currGtdInboxEntity.getTaskStepList().size() <= 0)
            throw new GtdIncomingException(GtdErrCode.ERR_INCOMING_STEP_NULL);
        return new PriorityTaskStepAction().toPriorityStep(currGtdInboxEntity);
    }

    @Override
    Observable<TaskStepBean> toDoList() throws RuntimeException{
        if(currGtdInboxEntity == null)
            toIncoming();
        else if(currGtdInboxEntity.getTaskStepList().size() <= 0)
            throw new GtdIncomingException(GtdErrCode.ERR_INCOMING_STEP_NULL);
        return gtdStateMachine.getTaskGtdState().setCurrGtdEntity(currGtdInboxEntity).toDoList();
    }

    @Override
    TaskStepBean toDoItNow() throws RuntimeException {
        if(currGtdInboxEntity == null)
            toIncoming();
        else if(currGtdInboxEntity.getTaskStepList().size() <= 0)
            throw new GtdIncomingException(GtdErrCode.ERR_INCOMING_STEP_NULL);
        return gtdStateMachine.getTaskGtdState().setCurrGtdEntity(currGtdInboxEntity).toDoItNow();
    }

    //to delegate it
    @Override
    Observable<TaskStepBean> toWaitSome() throws RuntimeException{
        if(currGtdInboxEntity == null)
            toIncoming();
        else if(currGtdInboxEntity.getTaskStepList().size() <= 0)
            throw new GtdIncomingException(GtdErrCode.ERR_INCOMING_STEP_NULL);
        return gtdStateMachine.getTaskGtdState().setCurrGtdEntity(currGtdInboxEntity).toWaitSome();
    }

    @Override
    void toHold() throws RuntimeException{
        if(currGtdInboxEntity == null)
            toIncoming();
        gtdStateMachine.getHoldState().setCurrGtdEntity(currGtdInboxEntity).toHold();
    }

    //to finish it
    @Override
    void toDone() throws RuntimeException{
        if(currGtdInboxEntity == null)
            toIncoming();
        gtdStateMachine.getDoneGtdState().setCurrGtdEntity(currGtdInboxEntity).toDone();
    }

    //it not need in the future
    @Override
    void toDelete() throws RuntimeException{
        if(currGtdInboxEntity == null)
            toIncoming();
        gtdStateMachine.getTrashState().setCurrGtdEntity(currGtdInboxEntity).toDelete();
    }
    @Override
    void toTrash() throws RuntimeException{
        if(currGtdInboxEntity == null)
            toIncoming();
        gtdStateMachine.getTrashState().setCurrGtdEntity(currGtdInboxEntity).toTrash();
    }
    @Override
    void toUnTrash() throws RuntimeException{
        if(currGtdInboxEntity == null)
            toIncoming();
        gtdStateMachine.getTrashState().setCurrGtdEntity(currGtdInboxEntity).toUnTrash();
    }
}

package ahtewlg7.utimer.GTD.state;

import ahtewlg7.utimer.GTD.priority.PriorityTaskStepAction;
import ahtewlg7.utimer.comparator.PriorityComparator;
import ahtewlg7.utimer.entity.TaskEnvisionBean;
import ahtewlg7.utimer.entity.TaskPricipleBean;
import ahtewlg7.utimer.entity.TaskPurposeBean;
import ahtewlg7.utimer.entity.TaskStepBean;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.exception.GtdMachineException;
import io.reactivex.Observable;

/**
 * Created by lw on 2017/11/8.
 */

public class ProjectGtdState extends GtdState {
    public static final String TAG = ProjectGtdState.class.getSimpleName();

    private GtdProjectEntity projectEntity;

    public ProjectGtdState(GtdStateMachine gtdStateMachine){
        super(gtdStateMachine);
    }

    @Override
    GtdProjectEntity toDefineProject() {
        if(gtdEntity == null)
            throw new GtdMachineException(GtdErrCode.ERR_GTDENTITY_NULL);
        else if(gtdEntity.getTaskType() != GtdType.INBOX || gtdEntity.getTaskType() == GtdType.PROJECT)
            super.toDefineProject();
        projectEntity = new GtdProjectEntity();
        projectEntity.setId(gtdEntity.getId());
        projectEntity.initByInbox((GtdInboxEntity)gtdEntity);
        return projectEntity;
    }
    @Override
    Observable<TaskPurposeBean> toPurposeProject(){
        if(projectEntity == null)
            toDefineProject();
        if(projectEntity != null && projectEntity.getPurposeList().size() <= 0)
            throw new GtdMachineException(GtdErrCode.ERR_PROJECT_PURPOSE_NULL);
        return Observable.fromIterable(projectEntity.getPurposeList())
                .sorted(new PriorityComparator<TaskPurposeBean>());
    }

    @Override
    Observable<TaskPricipleBean> toPricipleProject() throws RuntimeException {
        if(projectEntity == null)
            toDefineProject();
        if(projectEntity != null && projectEntity.getPricipleList().size() <= 0)
            throw new GtdMachineException(GtdErrCode.ERR_PROJECT_PRICIPLE_NULL);
        return Observable.fromIterable(projectEntity.getPricipleList())
                .sorted(new PriorityComparator<TaskPricipleBean>());
    }
    @Override
    Observable<TaskEnvisionBean> toEnvisionProject() throws RuntimeException{
        if(projectEntity == null)
            toDefineProject();
        if(projectEntity != null && projectEntity.getEnvisionList().size() <= 0)
            throw new GtdMachineException(GtdErrCode.ERR_PROJECT_ENVISION_NULL);
        return Observable.fromIterable(projectEntity.getEnvisionList())
                .sorted(new PriorityComparator<TaskEnvisionBean>());
    }

    @Override
    Observable<TaskStepBean> toStepAction() throws RuntimeException {
        if(projectEntity == null)
            toDefineProject();
        if(projectEntity != null && projectEntity.getPricipleList().size() <= 0)
            throw new GtdMachineException(GtdErrCode.ERR_PROJECT_STEP_NULL);
        return new PriorityTaskStepAction().toPriorityStep(projectEntity);
    }

    @Override
    Observable<TaskStepBean> toDoList() throws RuntimeException{
        if(projectEntity == null)
            toDefineProject();
        if(projectEntity != null && projectEntity.getPricipleList().size() <= 0)
            throw new GtdMachineException(GtdErrCode.ERR_PROJECT_STEP_NULL);
        return gtdStateMachine.getTaskGtdState().setCurrGtdEntity(projectEntity).toDoList();
    }

    @Override
    TaskStepBean toDoItNow() throws RuntimeException {
        if(projectEntity == null)
            toDefineProject();
        if(projectEntity != null && projectEntity.getPricipleList().size() <= 0)
            throw new GtdMachineException(GtdErrCode.ERR_PROJECT_STEP_NULL);
        return gtdStateMachine.getTaskGtdState().setCurrGtdEntity(projectEntity).toDoItNow();
    }

    //to delegate it
    @Override
    Observable<TaskStepBean> toWaitSome() throws RuntimeException{
        if(projectEntity == null)
            toDefineProject();
        if(projectEntity != null && projectEntity.getPricipleList().size() <= 0)
            throw new GtdMachineException(GtdErrCode.ERR_PROJECT_STEP_NULL);
        return gtdStateMachine.getTaskGtdState().setCurrGtdEntity(projectEntity).toWaitSome();
    }

    @Override
    void toHold() throws RuntimeException{
        if(projectEntity == null)
            toDefineProject();
        gtdStateMachine.getHoldState().setCurrGtdEntity(projectEntity).toHold();
    }

    //to finish it
    @Override
    void toDone() throws RuntimeException{
        if(projectEntity == null)
            toDefineProject();
        gtdStateMachine.getDoneGtdState().setCurrGtdEntity(projectEntity).toDone();
    }

    //it not need in the future
    @Override
    void toDelete() throws RuntimeException{
        if(projectEntity == null)
            toDefineProject();
        gtdStateMachine.getTrashState().setCurrGtdEntity(projectEntity).toDelete();
    }

    @Override
    void toTrash() throws RuntimeException{
        if(projectEntity == null)
            toDefineProject();
        gtdStateMachine.getTrashState().setCurrGtdEntity(projectEntity).toTrash();
    }

    @Override
    void toUnTrash() throws RuntimeException{
        if(projectEntity == null)
            toDefineProject();
        gtdStateMachine.getTrashState().setCurrGtdEntity(projectEntity).toUnTrash();
    }
}

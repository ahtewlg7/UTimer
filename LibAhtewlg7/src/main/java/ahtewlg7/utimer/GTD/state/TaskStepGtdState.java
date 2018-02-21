package ahtewlg7.utimer.GTD.state;

import ahtewlg7.utimer.energy.EnergyAction;
import ahtewlg7.utimer.entity.TaskStepBean;
import ahtewlg7.utimer.entity.gtd.AGtdTaskEntity;
import ahtewlg7.utimer.enumtype.DegreeLevel;
import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.enumtype.PriorityLevel;
import ahtewlg7.utimer.exception.GtdTaskException;
import ahtewlg7.utimer.taskContext.TaskContextAction;
import ahtewlg7.utimer.timemanager.TimeManagerAction;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

/**
 * Created by lw on 2017/10/3.
 */

public class TaskStepGtdState extends GtdState {
    public static final String TAG = TaskStepGtdState.class.getSimpleName();

    private TaskContextAction taskContextAction;

    public TaskStepGtdState(GtdStateMachine gtdStateMachine){
        super(gtdStateMachine);
        taskContextAction = new TaskContextAction();
    }

    @Override
    Observable<TaskStepBean> toDoList() throws RuntimeException{
        checkFirst();
        return Observable.fromIterable(((AGtdTaskEntity)gtdEntity).getTaskStepList())
                .filter(new Predicate<TaskStepBean>() {
                    @Override
                    public boolean test(TaskStepBean taskStepBean) throws Exception {
                        boolean contextOk   = taskStepBean.getTaskContext().isOk(taskContextAction);
                        boolean energyOk    = new EnergyAction().isTaskOk(taskStepBean.getEnergy());
                        boolean timeOk      = new TimeManagerAction().isTimeOk(taskStepBean.getTimeEntity().getTimeInterval());
                        boolean isFinished  = taskStepBean.isFinished();
                        return contextOk && energyOk && timeOk && !isFinished;
                    }
                });
    }

    @Override
    TaskStepBean toDoItNow() throws RuntimeException{
        checkFirst();
        return toDoList().groupBy(new Function<TaskStepBean, PriorityLevel>() {
                    @Override
                    public PriorityLevel apply(TaskStepBean taskStepBean) throws Exception {
                        return taskStepBean.getPriorityLevel();
                    }
                }).blockingSingle()
                .groupBy(new Function<TaskStepBean, DegreeLevel>() {
                    @Override
                    public DegreeLevel apply(TaskStepBean taskStepBean) throws Exception {
                        return taskStepBean.getImportant().getDegreeLevel();
                    }
                }).blockingSingle()
                .blockingFirst();
    }

    @Override
    Observable<TaskStepBean> toWaitSome() throws RuntimeException {
        checkFirst();
        return Observable.fromIterable(((AGtdTaskEntity)gtdEntity).getTaskStepList())
                .filter(new Predicate<TaskStepBean>() {
                    @Override
                    public boolean test(TaskStepBean taskStepBean) throws Exception {
                        boolean contextOk   = taskStepBean.getTaskContext() == null || taskStepBean.getTaskContext().isOk(taskContextAction);
                        boolean isFinished  = taskStepBean.isFinished();
                        return !contextOk && !isFinished;
                    }
                });
    }

    @Override
    void toDone() throws RuntimeException{
        checkFirst();
        Observable.fromIterable(((AGtdTaskEntity)gtdEntity).getTaskStepList())
                .filter(new Predicate<TaskStepBean>() {
                    @Override
                    public boolean test(TaskStepBean taskStepBean) throws Exception {
                        return !taskStepBean.isFinished();
                    }
                }).count()
            .subscribe(new Consumer<Long>() {
                @Override
                public void accept(Long aLong) throws Exception {
                    if(aLong > 0) {
                        ((AGtdTaskEntity) gtdEntity).setDone(false);
                        throw new GtdTaskException(GtdErrCode.ERR_TASK_STEP_UNDONE);
                    }
                    ((AGtdTaskEntity) gtdEntity).setDone(true);
                }
            });
    }

    private void checkFirst(){
        if(gtdEntity == null)
            throw new GtdTaskException(GtdErrCode.ERR_TASK_ENTITY_NULL);
        else if(((AGtdTaskEntity)gtdEntity).isDone())
            throw new GtdTaskException(GtdErrCode.ERR_TASK_DONE);
        else if(((AGtdTaskEntity)gtdEntity).getTaskStepList() == null || ((AGtdTaskEntity)gtdEntity).getTaskStepList().size() == 0)
            throw new GtdTaskException(GtdErrCode.ERR_TASK_STEP_NULL);
    }
}

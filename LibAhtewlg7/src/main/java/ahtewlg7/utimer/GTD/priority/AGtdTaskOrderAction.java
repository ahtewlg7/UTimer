package ahtewlg7.utimer.GTD.priority;


import ahtewlg7.utimer.entity.TaskStepBean;
import ahtewlg7.utimer.entity.gtd.AGtdTaskEntity;
import io.reactivex.Observable;

/**
 * Created by lw on 2017/11/21.
 */

public abstract class AGtdTaskOrderAction {
    public static final String TAG = AGtdTaskOrderAction.class.getSimpleName();

    public abstract Observable<TaskStepBean> toPriorityStep(AGtdTaskEntity gtdTaskEntity);
 }

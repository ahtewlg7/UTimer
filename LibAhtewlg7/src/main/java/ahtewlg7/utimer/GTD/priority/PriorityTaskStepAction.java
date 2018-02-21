package ahtewlg7.utimer.GTD.priority;

import ahtewlg7.utimer.comparator.PriorityComparator;
import ahtewlg7.utimer.entity.TaskStepBean;
import ahtewlg7.utimer.entity.gtd.AGtdTaskEntity;
import io.reactivex.Observable;

/**
 * Created by lw on 2017/11/21.
 */

public class PriorityTaskStepAction extends AGtdTaskOrderAction {
    public static final String TAG = PriorityTaskStepAction.class.getSimpleName();

    @Override
    public Observable<TaskStepBean> toPriorityStep(AGtdTaskEntity gtdTaskEntity) {
        return Observable.fromIterable(gtdTaskEntity.getTaskStepList())
                .sorted(new PriorityComparator<TaskStepBean>());
    }
}

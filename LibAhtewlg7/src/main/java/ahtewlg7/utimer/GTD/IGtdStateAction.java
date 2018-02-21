package ahtewlg7.utimer.GTD;

import android.support.annotation.NonNull;

import java.util.List;

import ahtewlg7.utimer.entity.TaskEnvisionBean;
import ahtewlg7.utimer.entity.TaskPricipleBean;
import ahtewlg7.utimer.entity.TaskPurposeBean;
import ahtewlg7.utimer.entity.TaskStepBean;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdOnlineEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.GtdWeekPreviewEntity;
import io.reactivex.Observable;

/**
 * Created by lw on 2017/10/31.
 */

public interface IGtdStateAction {
    Observable<GtdOnlineEntity> toWatch(@NonNull Observable<GtdOnlineEntity> gtdEntityObservable);
    Observable<GtdOnlineEntity> toStar(@NonNull Observable<GtdOnlineEntity> gtdEntityObservable);
    Observable<GtdOnlineEntity> toFork(@NonNull Observable<GtdOnlineEntity> gtdEntityObservable);

    Observable<GtdInboxEntity> toIncoming(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);

//    Observable<GtdMaterialEntity> toLink(@NonNull Observable<GtdMaterialEntity> gtdEntityObservable);
//    Observable<GtdMaterialEntity> toUnLink(@NonNull Observable<GtdMaterialEntity> gtdEntityObservable);
    Observable<GtdWeekPreviewEntity> toPreviewWeekly(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
    Observable<GtdProjectEntity> toDefineProject(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
    Observable<List<TaskPurposeBean>> toPurposeProject(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
    Observable<List<TaskPricipleBean>> toPricipleProject(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
    Observable<List<TaskEnvisionBean>> toEnvisionProject(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
    Observable<List<TaskStepBean>> toStepAction(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
    Observable<List<TaskStepBean>> toDoList(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
    Observable<TaskStepBean> toDoItNow(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
    Observable<List<TaskStepBean>> toWaitSome(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
//    Observable<? extends AGtdEntity> toBrainStrom(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
//    Observable<? extends AGtdEntity> toIdentifyAction(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
//    Observable<? extends AGtdEntity> toListAction(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
//    Observable<? extends AGtdEntity> toReminderAction(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);

    //to delegate it
    <T extends AGtdEntity> Observable<T> toHold(@NonNull Observable<T> gtdEntityObservable);
//    Observable<? extends AGtdEntity> toSchedueDatebook(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
//    Observable<? extends AGtdEntity> toDeferDoList(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable);
    <T extends AGtdEntity> Observable<T> toDone(@NonNull Observable<T> gtdEntityObservable);

    <T extends AGtdEntity> Observable<T> toDelete(@NonNull Observable<T> gtdEntityObservable);
    <T extends AGtdEntity> Observable<T> toTrash(@NonNull Observable<T> gtdEntityObservable);
    <T extends AGtdEntity> Observable<T> toUnTrash(@NonNull Observable<T> gtdEntityObservable);

}

package ahtewlg7.utimer.GTD.state;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.TaskEnvisionBean;
import ahtewlg7.utimer.entity.TaskPricipleBean;
import ahtewlg7.utimer.entity.TaskPurposeBean;
import ahtewlg7.utimer.entity.TaskStepBean;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.GtdWeekPreviewEntity;
import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.exception.GtdMachineException;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.Observable;

/**
 * Created by lw on 2017/10/3.
 */

class GtdState{
    public static final String TAG = GtdState.class.getSimpleName();

    AGtdEntity gtdEntity;
    GtdStateMachine gtdStateMachine;

    GtdState(@NonNull GtdStateMachine gtdStateMachine){
        this.gtdStateMachine = gtdStateMachine;
    }

    AGtdEntity getCurrGtdEntity() {
        return gtdEntity;
    }

    GtdState setCurrGtdEntity(AGtdEntity gtdEntity) {
        this.gtdEntity = gtdEntity;
        return this;
    }

    void toWatch() throws RuntimeException{
        Logcat.d(TAG,"toWatch cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    void toStar() throws RuntimeException{
        Logcat.d(TAG,"toStar cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    void toFork() throws RuntimeException{
        Logcat.d(TAG,"toFork cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    //to incoming collection container
    GtdInboxEntity toIncoming() throws RuntimeException {
        Logcat.d(TAG,"toIncoming cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    //it is action as a project
    GtdProjectEntity toDefineProject() throws RuntimeException {
        Logcat.d(TAG,"toDefineProject cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }
    //to organize component, priority, sequence
    Observable<TaskPurposeBean> toPurposeProject() throws RuntimeException {
        Logcat.d(TAG,"toPurposeProject cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }
    Observable<TaskPricipleBean> toPricipleProject() throws RuntimeException {
        Logcat.d(TAG,"toPricipleProject cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }
    Observable<TaskEnvisionBean> toEnvisionProject() throws RuntimeException{
        Logcat.d(TAG,"toPricipleProject cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    GtdWeekPreviewEntity toPreviewWeekly() throws RuntimeException {
        Logcat.d(TAG,"toPreviewWeekly cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    //identify all task
    Observable<TaskStepBean> toStepAction() throws RuntimeException{
        Logcat.d(TAG,"toStepProject cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    //filt the workable task from all taskes
    Observable<TaskStepBean> toDoList() throws RuntimeException{
        Logcat.d(TAG,"toDoList cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    //to filt the task just done in 2 minutes
    TaskStepBean toDoItNow() throws RuntimeException{
        Logcat.d(TAG,"toDoItNow cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    //to delegate it
    Observable<TaskStepBean> toWaitSome() throws RuntimeException{
        Logcat.d(TAG,"toWaitSome cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    /*
    //to organize component
    @Override
    public Observable<? extends AGtdEntity> toReminderAction(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable){
        Logcat.d(TAG,"toPreviewAction cancel");
        RuntimeException runtimeException = new GtdMachineException(GtdMachineException.EXCEPTION_CODE_CANCEL_ACTION);
        return throwError(gtdEntityObservable, runtimeException);
    }*/

    //it is maybe usefully
    void toHold() throws RuntimeException{
        Logcat.d(TAG,"toMaybe cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    //to finish it
    void toDone() throws RuntimeException{
        Logcat.d(TAG,"toDone cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }

    //it not need in the future
    void toDelete() throws RuntimeException{
        Logcat.d(TAG,"toDelete cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }
    void toTrash() throws RuntimeException{
        Logcat.d(TAG,"toTrash cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }
    void toUnTrash() throws RuntimeException{
        Logcat.d(TAG,"toUnTrash cancel");
        throw new GtdMachineException(GtdErrCode.ERR_ACTION_CANCEL);
    }
}

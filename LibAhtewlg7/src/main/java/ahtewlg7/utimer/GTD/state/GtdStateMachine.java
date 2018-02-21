package ahtewlg7.utimer.GTD.state;

import android.support.annotation.NonNull;

import java.util.List;

import ahtewlg7.utimer.GTD.IGtdStateAction;
import ahtewlg7.utimer.entity.TaskEnvisionBean;
import ahtewlg7.utimer.entity.TaskPricipleBean;
import ahtewlg7.utimer.entity.TaskPurposeBean;
import ahtewlg7.utimer.entity.TaskStepBean;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.entity.gtd.GtdOnlineEntity;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.GtdWeekPreviewEntity;
import ahtewlg7.utimer.enumtype.GtdErrCode;
import ahtewlg7.utimer.exception.GtdMachineException;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2017/10/3.
 */

public class GtdStateMachine implements IGtdStateAction {
    public static final String TAG = GtdStateMachine.class.getSimpleName();

    private GtdState onLineGtdState;
    private GtdState inboxGtdState;
    private GtdState referenceGtdState;
    private GtdState weekPreviewGtdState;
    private GtdState projectGtdState;
    private GtdState taskGtdState;
    private GtdState holdGtdState;
    private GtdState trashGtdState;
    private GtdState doneGtdState;

    @Override
    public Observable<GtdOnlineEntity> toWatch(@NonNull Observable<GtdOnlineEntity> gtdEntityObservable) {
        return gtdEntityObservable.doOnNext(new Consumer<GtdOnlineEntity>() {
            @Override
            public void accept(GtdOnlineEntity gtdOnlineEntity) throws Exception {
                getCurrState(gtdOnlineEntity).setCurrGtdEntity(gtdOnlineEntity).toWatch();
            }
        });
    }

    @Override
    public Observable<GtdOnlineEntity> toStar(@NonNull Observable<GtdOnlineEntity> gtdEntityObservable) {
        return gtdEntityObservable.doOnNext(new Consumer<GtdOnlineEntity>() {
            @Override
            public void accept(GtdOnlineEntity gtdOnlineEntity) throws Exception {
                getCurrState(gtdOnlineEntity).setCurrGtdEntity(gtdOnlineEntity).toStar();
            }
        });
    }

    @Override
    public Observable<GtdOnlineEntity> toFork(@NonNull Observable<GtdOnlineEntity> gtdEntityObservable) {
        return gtdEntityObservable.doOnNext(new Consumer<GtdOnlineEntity>() {
            @Override
            public void accept(GtdOnlineEntity gtdOnlineEntity) throws Exception {
                getCurrState(gtdOnlineEntity).setCurrGtdEntity(gtdOnlineEntity).toFork();
            }
        });
    }

    @Override
    public Observable<GtdInboxEntity> toIncoming(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable) {
        return gtdEntityObservable.map(new Function<AGtdEntity, GtdInboxEntity>() {
            @Override
            public GtdInboxEntity apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toIncoming();
            }
        });
    }

    @Override
    public Observable<GtdProjectEntity> toDefineProject(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable) {
        return gtdEntityObservable.map(new Function<AGtdEntity, GtdProjectEntity>() {
            @Override
            public GtdProjectEntity apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toDefineProject();
            }
        });
    }

    @Override
    public Observable<List<TaskPurposeBean>> toPurposeProject(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable) {
        return gtdEntityObservable.map(new Function<AGtdEntity, List<TaskPurposeBean>>() {
            @Override
            public List<TaskPurposeBean> apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toPurposeProject().toList().blockingGet();
            }
        });
    }

    @Override
    public Observable<List<TaskPricipleBean>> toPricipleProject(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable) {
        return gtdEntityObservable.map(new Function<AGtdEntity, List<TaskPricipleBean>>() {
            @Override
            public List<TaskPricipleBean> apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toPricipleProject().toList().blockingGet();
            }
        });
    }
    @Override
    public Observable<List<TaskEnvisionBean>> toEnvisionProject(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable){
        return gtdEntityObservable.map(new Function<AGtdEntity, List<TaskEnvisionBean>>() {
            @Override
            public List<TaskEnvisionBean> apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toEnvisionProject().toList().blockingGet();
            }
        });
    }

    @Override
    public Observable<GtdWeekPreviewEntity> toPreviewWeekly(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable) {
        return gtdEntityObservable.map(new Function<AGtdEntity, GtdWeekPreviewEntity>() {
            @Override
            public GtdWeekPreviewEntity apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toPreviewWeekly();
            }
        });
    }

    @Override
    public Observable<List<TaskStepBean>> toStepAction(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable){
        return gtdEntityObservable.map(new Function<AGtdEntity, List<TaskStepBean>>() {
            @Override
            public List<TaskStepBean> apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toStepAction().toList().blockingGet();
            }
        });
    }

    @Override
    public Observable<List<TaskStepBean>> toDoList(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable){
        return gtdEntityObservable.map(new Function<AGtdEntity, List<TaskStepBean>>() {
            @Override
            public List<TaskStepBean> apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toDoList().toList().blockingGet();
            }
        });
    }

    @Override
    public Observable<TaskStepBean> toDoItNow(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable){
        return gtdEntityObservable.map(new Function<AGtdEntity, TaskStepBean>() {
            @Override
            public TaskStepBean apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toDoItNow();
            }
        });
    }

    @Override
    public Observable<List<TaskStepBean>> toWaitSome(@NonNull Observable<? extends AGtdEntity> gtdEntityObservable){
        return gtdEntityObservable.map(new Function<AGtdEntity, List<TaskStepBean>>() {
            @Override
            public List<TaskStepBean> apply(AGtdEntity gtdEntity) throws Exception {
                return getCurrState(gtdEntity).setCurrGtdEntity(gtdEntity).toWaitSome().toList().blockingGet();
            }
        });
    }

    /*@Override
    public io.reactivex.Observable<? extends AGtdEntity> toBrainStrom(@android.support.annotation.NonNull io.reactivex.Observable<? extends AGtdEntity> gtdEntityObservable) {
        return null;
    }

    @Override
    public io.reactivex.Observable<? extends AGtdEntity> toIdentifyAction(@android.support.annotation.NonNull io.reactivex.Observable<? extends AGtdEntity> gtdEntityObservable) {
        return null;
    }

    @Override
    public io.reactivex.Observable<? extends AGtdEntity> toListAction(@android.support.annotation.NonNull io.reactivex.Observable<? extends AGtdEntity> gtdEntityObservable) {
        return null;
    }

    @Override
    public io.reactivex.Observable<? extends AGtdEntity> toReminderAction(@android.support.annotation.NonNull io.reactivex.Observable<? extends AGtdEntity> gtdEntityObservable) {
        return null;
    }*/


    @Override
    public <T extends AGtdEntity> Observable<T> toHold(@NonNull Observable<T> gtdEntityObservable) {
        return gtdEntityObservable.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                getCurrState(t).setCurrGtdEntity(t).toHold();
            }
        });
    }

    /*@Override
    public io.reactivex.Observable<? extends AGtdEntity> toSchedueDatebook(@android.support.annotation.NonNull io.reactivex.Observable<? extends AGtdEntity> gtdEntityObservable) {
        return null;
    }

    @Override
    public io.reactivex.Observable<? extends AGtdEntity> toDeferDoList(@android.support.annotation.NonNull io.reactivex.Observable<? extends AGtdEntity> gtdEntityObservable) {
        return null;
    }*/

    @Override
    public <T extends AGtdEntity> Observable<T>  toDone(@NonNull Observable<T> gtdEntityObservable) {
        return gtdEntityObservable.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                getCurrState(t).setCurrGtdEntity(t).toDone();
            }
        });
    }

    @Override
    public <T extends AGtdEntity> Observable<T> toTrash(@NonNull Observable<T> gtdEntityObservable) {
        return gtdEntityObservable.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                getCurrState(t).setCurrGtdEntity(t).toTrash();
            }
        });
    }

    @Override
    public <T extends AGtdEntity> Observable<T> toUnTrash(@NonNull Observable<T> gtdEntityObservable) {
        return gtdEntityObservable.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                getCurrState(t).setCurrGtdEntity(t).toUnTrash();
            }
        });
    }

    @Override
    public <T extends AGtdEntity> Observable<T> toDelete(@NonNull Observable<T> gtdEntityObservable) {
        return gtdEntityObservable.doOnNext(new Consumer<T>() {
            @Override
            public void accept(T t) throws Exception {
                getCurrState(t).setCurrGtdEntity(t).toDelete();
            }
        });
    }


    GtdState getCurrState(@NonNull AGtdEntity gtdEntity) throws RuntimeException{
        GtdState state = null;
        if(gtdEntity.isTrashed()){
            state = getTrashState();
            state.setCurrGtdEntity(gtdEntity);
            return state;
        }

        switch (gtdEntity.getTaskType()){
            case NEW:
            case ONLINE:
                state = getOnlineState();
                break;
            case MATERIAL:
                state = getReferenceState();
                break;
            case INBOX:
                state = getInboxGtdState();
                break;
            case PROJECT:
                state = getProjectGtdState();
                break;
            case WEEKPREVIEW:
                state = getWeekPreviewState();
                break;
            case JUSTDOIT:
            case TODO:
            case NEXTACTION:
            case WAITFOR:
            case DATEBOOK:
            case DELEGATE:
                state = getTaskGtdState();
                break;
            case MAYBE:
                state = getHoldState();
                break;
            case DONE:
                state = getDoneGtdState();
                break;
            case TRASH:
                state = getTrashState();
                break;
        }
        if(state == null)
            throw new GtdMachineException(GtdErrCode.ERR_STATE_NULL);
        state.setCurrGtdEntity(gtdEntity);
        return state;
    }

    GtdState getOnlineState(){
        if(onLineGtdState == null)
            onLineGtdState = new OnlineGtdState(this);
        return onLineGtdState;
    }

    GtdState getReferenceState(){
        if(referenceGtdState != null)
            referenceGtdState = new MaterialGtdState(this);
        return referenceGtdState;
    }

    GtdState getInboxGtdState(){
        if(inboxGtdState == null)
            inboxGtdState = new InboxGtdState(this);
        return inboxGtdState;
    }

    GtdState getProjectGtdState(){
        if(projectGtdState == null)
            projectGtdState = new ProjectGtdState(this);
        return inboxGtdState;
    }

    GtdState getWeekPreviewState(){
        if(weekPreviewGtdState != null)
            weekPreviewGtdState = new WeekPreviewGtdState(this);
        return referenceGtdState;
    }

    GtdState getTaskGtdState(){
        if(taskGtdState == null)
            taskGtdState = new TaskStepGtdState(this);
        return taskGtdState;
    }

    GtdState getDoneGtdState(){
        if(doneGtdState == null)
            doneGtdState = new DoneGtdState(this);
        return doneGtdState;
    }

    GtdState getHoldState(){
        if(holdGtdState == null)
            holdGtdState = new HoldGtdState(this);
        return holdGtdState;
    }

    GtdState getTrashState(){
        if(trashGtdState != null)
            trashGtdState = new TrashGtdState(this);
        return trashGtdState;
    }
}

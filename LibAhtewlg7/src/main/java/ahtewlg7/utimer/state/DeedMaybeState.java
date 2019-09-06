package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.BaseGtdEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;

import static ahtewlg7.utimer.enumtype.DeedState.INBOX;

/**
 * Created by lw on 2019/4/6.
 */
public class DeedMaybeState extends DeedBaseState {

    DeedMaybeState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Optional<BaseEventBusBean> toTrash(@NonNull BaseGtdEntity entity) {
        if(!ifHandlable(entity) || !ifTrashable((GtdDeedEntity)entity))
            return Optional.absent();
        return removeState(entity);
    }

    @Override
    public Optional<BaseEventBusBean> toActive(@NonNull BaseGtdEntity entity) {
        if(!ifGtdable((GtdDeedEntity)entity))
            return Optional.absent();
        return updateAndPostState(INBOX, entity);
    }

    @Override
    public Optional<BaseEventBusBean> toBeScheduleJob(@NonNull BaseGtdEntity entity){
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeScheduleJob(entity);
    }

    @Override
    public Optional<BaseEventBusBean> toBeDone(@NonNull BaseGtdEntity entity) {
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeDone(entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeReference(@NonNull BaseGtdEntity entity){
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeReference(entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeQuarterJob(@NonNull BaseGtdEntity entity) {
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeQuarterJob(entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeDeferJob(@NonNull BaseGtdEntity entity){
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeDeferJob(entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeDelegateJob(@NonNull BaseGtdEntity entity){
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeDelegateJob(entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeProject(@NonNull BaseGtdEntity entity){
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeProject(entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeCalendarJob(@NonNull BaseGtdEntity entity){
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeCalendarJob(entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeWishJob(@NonNull BaseGtdEntity entity){
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeWishJob(entity);
    }

    @Override
    public Optional<BaseEventBusBean> toBeUseless(@NonNull BaseGtdEntity entity) {
        if(!ifGtdable(entity))
            return Optional.absent();
        updateState(INBOX, entity);
        return getGtdMachine().getWorkState().toBeUseless(entity);
    }

    @Override
    protected boolean ifTrashable(BaseGtdEntity entity){
        return entity != null && entity.ifValid()
                && entity.getClass().isAssignableFrom(GtdDeedEntity.class) ;
    }
}

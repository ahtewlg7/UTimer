package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.BaseGtdEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;

import static ahtewlg7.utimer.enumtype.DeedState.SCHEDULE;

/**
 * Created by lw on 2019/4/6.
 */
public class DeedInboxState extends DeedBaseState {

    DeedInboxState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Optional<BaseEventBusBean> toBeScheduleJob(@NonNull BaseGtdEntity entity){
        Optional<BaseEventBusBean> eventBusBeanOptional = updateAndPostState(SCHEDULE, entity);
        if(eventBusBeanOptional.isPresent())
            ((GtdDeedEntity)entity).setStartTime(DateTime.now());
        return eventBusBeanOptional;
    }

    @Override
    public Optional<BaseEventBusBean> toBeDone(@NonNull BaseGtdEntity entity) {
        return updateAndPostState(DeedState.DONE, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeReference(@NonNull BaseGtdEntity entity){
        return updateAndPostState(DeedState.REFERENCE, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeQuarterJob(@NonNull BaseGtdEntity entity) {
        return updateAndPostState(DeedState.ONE_QUARTER, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeDeferJob(@NonNull BaseGtdEntity entity){
        return updateAndPostState(DeedState.DEFER, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeDelegateJob(@NonNull BaseGtdEntity entity){
        return updateAndPostState(DeedState.DELEGATE, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeProject(@NonNull BaseGtdEntity entity){
        return updateAndPostState(DeedState.PROJECT, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeCalendarJob(@NonNull BaseGtdEntity entity){
        return updateAndPostState(DeedState.CALENDAR, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeWishJob(@NonNull BaseGtdEntity entity){
        return updateAndPostState(DeedState.WISH, entity);
    }

    @Override
    public Optional<BaseEventBusBean> toBeUseless(@NonNull BaseGtdEntity entity) {
        return updateAndPostState(DeedState.USELESS, entity);
    }

    @Override
    public Optional<BaseEventBusBean> toTrash(@NonNull BaseGtdEntity entity) {
        return updateAndPostState(DeedState.TRASH, entity);
    }
}

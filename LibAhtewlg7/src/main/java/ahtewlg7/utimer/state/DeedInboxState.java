package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.enumtype.DeedState;

/**
 * Created by lw on 2019/4/6.
 */
public class DeedInboxState extends DeedBaseState {

    DeedInboxState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Optional<BaseEventBusBean> toBeDone(@NonNull AUtimerEntity entity) {
        return updateState(DeedState.DONE, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeReference(@NonNull AUtimerEntity entity){
        return updateState(DeedState.REFERENCE, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBe2MinJob(@NonNull AUtimerEntity entity) {
        return updateState(DeedState.TWO_MIN, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeDeferJob(@NonNull AUtimerEntity entity){
        return updateState(DeedState.DEFER, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeDelegateJob(@NonNull AUtimerEntity entity){
        return updateState(DeedState.DELEGATE, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeProject(@NonNull AUtimerEntity entity){
        return updateState(DeedState.PROJECT, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeReminderJob(@NonNull AUtimerEntity entity){
        return updateState(DeedState.CALENDAR, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBeWishJob(@NonNull AUtimerEntity entity){
        return updateState(DeedState.WISH, entity);
    }
}

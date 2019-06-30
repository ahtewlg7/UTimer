package ahtewlg7.utimer.state;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;

/**
 * Created by lw on 2019/4/6.
 */
public class ActInboxJobState extends BaseJobState {

    ActInboxJobState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Optional<BaseEventBusBean> toBeDone(@NonNull AUtimerEntity entity) {
        return updateState(DeedState.DONE, entity);
    }

    public Optional<BaseEventBusBean> toBeReference(@NonNull AUtimerEntity entity){
        return updateState(DeedState.REFERENCE, entity);
    }
    @Override
    public Optional<BaseEventBusBean> toBe2MinJob(@NonNull AUtimerEntity entity) {
        return updateState(DeedState.TWO_MIN, entity);
    }
    public Optional<BaseEventBusBean> toBeDeferJob(@NonNull AUtimerEntity entity){
        return updateState(DeedState.DEFER, entity);
    }
    public Optional<BaseEventBusBean> toBeDelegateJob(@NonNull AUtimerEntity entity){
        return updateState(DeedState.DELEGATE, entity);
    }
    public Optional<BaseEventBusBean> toBeProject(@NonNull AUtimerEntity entity){
        return updateState(DeedState.PROJECT, entity);
    }
    public Optional<BaseEventBusBean> toBeReminderJob(@NonNull AUtimerEntity entity){
        return updateState(DeedState.CALENDAR, entity);
    }
    public Optional<BaseEventBusBean> toBeWishJob(@NonNull AUtimerEntity entity){
        return updateState(DeedState.WISH, entity);
    }

    @Override
    protected boolean toGtdable(GtdDeedEntity entity){
        return entity.getDeedState() == DeedState.MAYBE;
    }
}

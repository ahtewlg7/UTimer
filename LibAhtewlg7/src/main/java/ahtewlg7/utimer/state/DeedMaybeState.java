package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
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
    public Optional<BaseEventBusBean> toTrash(@NonNull AUtimerEntity entity) {
        if(!ifHandlable(entity) || !ifTrashable((GtdDeedEntity)entity))
            return Optional.absent();
        return removeState(entity);
    }

    @Override
    public Optional<BaseEventBusBean> toActive(@NonNull AUtimerEntity entity) {
        if(!ifHandlable(entity) || !ifGtdable((GtdDeedEntity)entity))
            return Optional.absent();
        return updateState(INBOX, entity);
    }

    @Override
    protected boolean ifTrashable(AUtimerEntity entity){
        return entity != null && entity.ifValid()
                && entity.getClass().isAssignableFrom(GtdDeedEntity.class) ;
    }
}

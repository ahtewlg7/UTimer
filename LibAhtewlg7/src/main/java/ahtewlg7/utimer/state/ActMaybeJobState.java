package ahtewlg7.utimer.state;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;

import static ahtewlg7.utimer.enumtype.DeedState.INBOX;
import static ahtewlg7.utimer.enumtype.DeedState.TRASH;

/**
 * Created by lw on 2019/4/6.
 */
public class ActMaybeJobState extends BaseJobState {

    ActMaybeJobState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }

    @Override
    public Optional<BaseEventBusBean> toTrash(@NonNull AUtimerEntity entity) {
        if(!ifActHandlable(entity) || !toTrashable((GtdDeedEntity)entity))
            return Optional.absent();
        return updateState(TRASH, entity);
    }

    @Override
    public Optional<BaseEventBusBean> toActive(@NonNull AUtimerEntity entity) {
        if(!ifActHandlable(entity) || !toGtdable((GtdDeedEntity)entity))
            return Optional.absent();
        return updateState(INBOX, entity);
    }

    @Override
    protected boolean toTrashable(GtdDeedEntity entity){
        return entity.getDeedState() == DeedState.MAYBE;
    }
    @Override
    protected boolean toGtdable(GtdDeedEntity entity){
        return entity.getDeedState() == DeedState.MAYBE;
    }
}

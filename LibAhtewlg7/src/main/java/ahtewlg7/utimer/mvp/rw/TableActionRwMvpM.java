package ahtewlg7.utimer.mvp.rw;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/27.
 */
class TableActionRwMvpM extends AUtimerRwMvpM<GtdActionEntity> {

    public TableActionRwMvpM() {
        super();
    }

    @Override
    public Flowable<Boolean> toSave(@NonNull Flowable<GtdActionEntity> entityRx) {
        return dbActionFacade.saveActionEntity(entityRx);
    }

    @Override
    public Flowable<Boolean> toDel(@NonNull Flowable<GtdActionEntity> entityRx) {
        return dbActionFacade.deleteActionEntity(entityRx);
    }

    @Override
    public Flowable<GtdActionEntity> loadAll() {
        return dbActionFacade.loadAllActionEntity();
    }
}

package ahtewlg7.utimer.mvp.rw;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/27.
 */
class TableDeedRwMvpM extends AUtimerRwMvpM<GtdDeedEntity> {

    public TableDeedRwMvpM() {
        super();
    }

    @Override
    public Flowable<Boolean> toSave(@NonNull Flowable<GtdDeedEntity> entityRx) {
        return dbActionFacade.saveActionEntity(entityRx);
    }

    @Override
    public Flowable<Boolean> toDel(@NonNull Flowable<GtdDeedEntity> entityRx) {
        return dbActionFacade.deleteActionEntity(entityRx);
    }

    @Override
    public Flowable<GtdDeedEntity> loadAll() {
        return dbActionFacade.loadAllActionEntity();
    }
}

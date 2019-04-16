package ahtewlg7.utimer.mvp.db;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.mvp.AUtimerRwMvpM;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/27.
 */
class TableActionMvpM extends AUtimerRwMvpM<GtdActionEntity> {

    public TableActionMvpM() {
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

package ahtewlg7.utimer.mvp.db;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.mvp.AUtimerRwMvpM;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/27.
 */
class TableShortHandMvpM extends AUtimerRwMvpM<ShortHandEntity> {

    public TableShortHandMvpM() {
        super();
    }

    @Override
    public Flowable<Boolean> toSave(@NonNull Flowable<ShortHandEntity> entityRx) {
        return dbActionFacade.saveShortHandEntity(entityRx);
    }

    @Override
    public Flowable<Boolean> toDel(@NonNull Flowable<ShortHandEntity> entityRx) {
        return dbActionFacade.deleteShortHandEntity(entityRx);
    }

    @Override
    public Flowable<ShortHandEntity> loadAll() {
        return dbActionFacade.loadAllShortHandEntity();
    }
}

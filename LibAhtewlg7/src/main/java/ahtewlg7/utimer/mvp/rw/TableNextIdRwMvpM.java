package ahtewlg7.utimer.mvp.rw;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.db.entity.NextIdGdBean;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/27.
 */
class TableNextIdRwMvpM extends AUtimerRwMvpM<NextIdGdBean> {

    public TableNextIdRwMvpM() {
        super();
    }

    @Override
    public Flowable<Boolean> toSave(@NonNull Flowable<NextIdGdBean> entityRx) {
        return dbActionFacade.saveNextIdBean(entityRx);
    }

    @Override
    public Flowable<Boolean> toDel(@NonNull Flowable<NextIdGdBean> entityRx) {
        return dbActionFacade.delNextIdBean(entityRx);
    }

    @Override
    public Flowable<NextIdGdBean> loadAll() {
        return dbActionFacade.loadAllNextIdBean();
    }
}

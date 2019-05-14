package ahtewlg7.utimer.mvp.rw;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.factory.GtdDeedByUuidFactory;
import io.reactivex.Flowable;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2019/3/27.
 */
class TableDeedRwMvpM extends AUtimerRwMvpM<GtdDeedEntity> {

    public TableDeedRwMvpM() {
        super();
    }

    @Override
    public Flowable<Boolean> toSave(@NonNull Flowable<GtdDeedEntity> entityRx) {
        return dbActionFacade.saveActionEntity(entityRx.doOnNext(new Consumer<GtdDeedEntity>() {
            @Override
            public void accept(GtdDeedEntity entity) throws Exception {
                GtdDeedByUuidFactory.getInstance().update(entity.getUuid(), entity);
            }
        }));
    }

    @Override
    public Flowable<Boolean> toDel(@NonNull Flowable<GtdDeedEntity> entityRx) {
        return dbActionFacade.deleteActionEntity(entityRx.doOnNext(new Consumer<GtdDeedEntity>() {
            @Override
            public void accept(GtdDeedEntity entity) throws Exception {
                GtdDeedByUuidFactory.getInstance().remove(entity.getUuid());
            }
        }));
    }

    @Override
    public Flowable<GtdDeedEntity> loadAll() {
        return dbActionFacade.loadAllActionEntity();
    }
}

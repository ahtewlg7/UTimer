package ahtewlg7.utimer.gtd;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.db.DbActionFacade;
import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;

public class GtdActionListAction {
    protected DbActionFacade dbActionFacade;

    public GtdActionListAction(){
        dbActionFacade   = new DbActionFacade();
    }

    public Flowable<GtdActionEntity> loadAllEntity() {
        return dbActionFacade.loadAllUndoActionEntity()
                .filter(new Predicate<Optional<GtdActionEntity>>() {
                    @Override
                    public boolean test(Optional<GtdActionEntity> gtdActionEntityOptional) throws Exception {
                        return gtdActionEntityOptional.isPresent();
                    }
                })
                .map(new Function<Optional<GtdActionEntity>, GtdActionEntity>() {
                    @Override
                    public GtdActionEntity apply(Optional<GtdActionEntity> gtdActionEntityOptional) throws Exception {
                        return gtdActionEntityOptional.get();
                    }
                });
    }

    public Flowable<Boolean> saveEntity(Flowable<Optional<GtdActionEntity>> flowable) {
//        return dbActionFacade.saveShortHandEntity(flowable);
        return null;
    }

    public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<GtdActionEntity>> flowable) {
//        return dbActionFacade.deleteShortHandEntity(flowable);
        return null;
    }
}

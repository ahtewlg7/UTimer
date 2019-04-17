package ahtewlg7.utimer.mvp.rw;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.db.DbActionFacade;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/3/26.
 */
abstract class AUtimerRwMvpM<E> {
    protected DbActionFacade dbActionFacade;

    public AUtimerRwMvpM(){
        dbActionFacade = new DbActionFacade();
    }

    public abstract Flowable<Boolean> toSave(@NonNull Flowable<E> entityRx);
    public abstract Flowable<Boolean> toDel(@NonNull Flowable<E> entityRx);
    public abstract Flowable<E> loadAll();
}

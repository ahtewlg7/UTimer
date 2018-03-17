package ahtewlg7.utimer.mvp;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import io.reactivex.Flowable;
import io.reactivex.Observable;

/**
 * Created by lw on 2018/3/8.
 */

public interface IRecyclerViewMvpM<T> {
    public Flowable<T> loadAll();
//    public T getEntity(String id);
    public Observable<AGtdEntity> getEntity(Observable<String> idObservable);
}

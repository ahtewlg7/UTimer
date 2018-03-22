package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import io.reactivex.Flowable;

/**
 * Created by lw on 2018/3/8.
 */

public interface IRecyclerViewMvpM<T> {
    public Flowable<T> loadAll();
    public Flowable<T> loadEntity(@NonNull Flowable<String> idObservable);
}

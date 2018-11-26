package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import io.reactivex.Flowable;

/**
 * Created by lw on 2018/3/8.
 */

public interface IBaseListInfoMvpM<T> {
    public Flowable<Optional<T>> loadAllEntity();
    public Flowable<Optional<T>> loadEntity(@NonNull Flowable<Optional<String>> keyObservable);
    public T newEntity();
    public Flowable<Boolean> saveEntity(@NonNull Flowable<Optional<T>> flowable);
    public Flowable<Boolean> deleteEntity(@NonNull Flowable<Optional<T>> flowable);
}
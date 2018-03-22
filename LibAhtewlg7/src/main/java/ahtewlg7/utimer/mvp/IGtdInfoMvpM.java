package ahtewlg7.utimer.mvp;

import io.reactivex.Flowable;

/**
 * Created by lw on 2018/3/17.
 */

public interface IGtdInfoMvpM<T>{
    public Flowable<T> loadEntity(String id);
}
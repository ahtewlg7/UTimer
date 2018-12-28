package ahtewlg7.utimer.mvp;

import io.reactivex.Flowable;

/**
 * Created by lw on 2018/3/8.
 */

public interface IAllItemListMvpM<T> {
    public Flowable<T> loadAllEntity();
    public boolean toDelEntity(T t);
}

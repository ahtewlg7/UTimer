package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import com.trello.rxlifecycle2.components.RxFragment;

import ahtewlg7.utimer.entity.AUtimerEntity;

public interface IBaseGtdListMvpV <T extends AUtimerEntity>extends IBaseListInfoMvpV<T> {
    public @NonNull RxFragment getFragment();
    public void toEditItem(T t);
}

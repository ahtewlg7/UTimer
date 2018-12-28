package ahtewlg7.utimer.mvp;

import io.reactivex.Flowable;

public interface IAllItemListMvpP<T> {
    public void toLoadAllItem();
    public void toDeleteItem(Flowable<T> tRx);

    public void onItemCreated(T t);
    public void onItemEdited(int index, T t);
}

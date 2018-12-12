package ahtewlg7.utimer.mvp;

public interface IAllItemListMvpP<T> {
    public void toLoadAllItem();
    public void toDeleteItem(T t);
    public void toCreateItem();
}

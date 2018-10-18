package ahtewlg7.utimer.mvp;

public interface IBaseListInfoMvpP<T> {
    public void toLoadAllData();
    public void toDeleteItem(T t);
    public void toEditItem(T t);
    public void toCreateItem();
}

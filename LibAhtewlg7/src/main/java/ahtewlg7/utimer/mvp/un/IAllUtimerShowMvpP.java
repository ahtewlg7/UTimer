package ahtewlg7.utimer.mvp.un;

public interface IAllUtimerShowMvpP<T> {
    public void toLoadAllItem();
    public void toDeleteItem(T t);
    public void toEditItem(T t);
    public void toCreateItem();
}

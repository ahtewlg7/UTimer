package ahtewlg7.utimer.mvp;

import java.util.List;

/**
 * Created by lw on 2018/3/8.
 */

public interface IAllItemListMvpV<T> extends IRxLifeCycleBindView{
    public static final int INVALID_INDEX = -1;

    public void resetView(List<T> dataList);
    public void resetView(int index , T t);

    public void onItemLoadStart();
    public void onItemLoad(T data);
    public void onItemLoadErr(Throwable t);
    public void onItemLoadEnd(List<T> alldata);

    public void onItemCreate(T data);
    public void onItemEdit(T data);

    public void onDeleteSucc(int index, T t);
    public void onDeleteFail(T t);
    public void onDeleteErr(Throwable throwable);
    public void onDeleteEnd();
}

package ahtewlg7.utimer.mvp.un;

import java.util.List;

/**
 * Created by lw on 2018/3/8.
 */

public interface IBaseListInfoMvpV<T> {
    public void initView(List<T> dataList);
    public void resetView(List<T> dataList);

    public void onViewInitStart();
    public void onViewInitErr();

    public void onDeleteSucc(T t);
    public void onDeleteFail(T t);
    public void onDeleteErr(Throwable throwable, T t);
}

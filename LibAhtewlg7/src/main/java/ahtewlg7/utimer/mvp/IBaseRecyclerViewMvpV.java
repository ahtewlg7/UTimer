package ahtewlg7.utimer.mvp;

import java.util.List;

/**
 * Created by lw on 2018/3/8.
 */

public interface IBaseRecyclerViewMvpV<T> {
    public void initRecyclerView(List<T> dataList);
    public void resetRecyclerView(List<T> dataList);

    public void onRecyclerViewInitStart();
    public void onRecyclerViewInitErr();
}

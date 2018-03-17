package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

/**
 * Created by lw on 2018/3/8.
 */

public interface IRecyclerViewMvpP<T>{
    public void loadAllData();
    public void addData(@NonNull Object obj);
    public void onItemClick(T t);
}

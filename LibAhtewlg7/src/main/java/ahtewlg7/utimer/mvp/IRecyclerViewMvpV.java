package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import java.util.List;

import ahtewlg7.utimer.entity.view.BaseSectionEntity;
import io.reactivex.Flowable;

/**
 * Created by lw on 2018/3/8.
 */

public interface IRecyclerViewMvpV<T> {
    public void initRecyclerView(List<BaseSectionEntity> dataList);
    public void resetRecyclerView(List<BaseSectionEntity> dataList);

    public void onRecyclerViewInitStart();
    public void onRecyclerViewInitErr();
    public void onRecyclerViewInitEnd();

    public Flowable<BaseSectionEntity> mapBean(@NonNull Flowable<T> sourceBeanFlowable);
}

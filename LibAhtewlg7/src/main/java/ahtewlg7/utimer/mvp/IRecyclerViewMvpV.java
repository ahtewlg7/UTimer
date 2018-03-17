package ahtewlg7.utimer.mvp;

import android.support.annotation.NonNull;

import java.util.List;

import ahtewlg7.utimer.view.BaseSectionEntity;
import io.reactivex.Flowable;

/**
 * Created by lw on 2018/3/8.
 */

public interface IRecyclerViewMvpV<T> {
    public void initView(List<BaseSectionEntity> dataList);
    public void resetView(List<BaseSectionEntity> dataList);
    public void toStartNoteActivity(String gtdEntityId, String noteEntityId);

    public Flowable<BaseSectionEntity> mapBean(@NonNull Flowable<T> sourceBeanFlowable);
}

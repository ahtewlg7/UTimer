package ahtewlg7.utimer.mvp;

import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.view.BaseSectionEntity;

/**
 * Created by lw on 2018/3/23.
 */

public interface IGtdRecyclerViewMvpV<T> extends IRecyclerViewMvpV<T> {
    public void toStartGtdActivity(String gtdEntityId, GtdType gtdType);
    public void onHeadClick(BaseSectionEntity baseSectionEntity);
}

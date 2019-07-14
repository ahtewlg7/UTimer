package ahtewlg7.utimer.comparator;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;

/**
 * Created by lw on 2019/7/14.
 */
public class DeedStateOrderComparator extends ABaseIntComparator<GtdDeedEntity> {
    @Override
    protected int getComparatorInt(GtdDeedEntity entity) {
        return entity.getDeedState().order();
    }
}

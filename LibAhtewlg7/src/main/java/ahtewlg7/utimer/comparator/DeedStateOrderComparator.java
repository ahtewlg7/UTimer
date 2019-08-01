package ahtewlg7.utimer.comparator;

import ahtewlg7.utimer.enumtype.DeedState;

/**
 * Created by lw on 2019/7/14.
 */
public class DeedStateOrderComparator extends ABaseIntComparator<DeedState> {
    @Override
    protected int getComparatorInt(DeedState state) {
        return state.order();
    }
}

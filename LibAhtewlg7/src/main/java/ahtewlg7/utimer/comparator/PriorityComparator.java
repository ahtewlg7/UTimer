package ahtewlg7.utimer.comparator;


import java.util.Comparator;

import ahtewlg7.utimer.entity.degree.IPriorityBean;
import ahtewlg7.utimer.enumtype.ComparatorType;

/**
 * Created by lw on 2017/11/15.
 */

public class PriorityComparator<T extends IPriorityBean> implements Comparator<T> {
    public static final String TAG = PriorityComparator.class.getSimpleName();

    @Override
    public int compare(T o1, T o2) {
        if(o1.getPriorityLevel().value() >  o2.getPriorityLevel().value())
            return ComparatorType.GREATER.value();
        else if(o1.getPriorityLevel().value() == o2.getPriorityLevel().value())
            return ComparatorType.EQUAL.value();
        else
            return ComparatorType.LESS.value();
    }
}

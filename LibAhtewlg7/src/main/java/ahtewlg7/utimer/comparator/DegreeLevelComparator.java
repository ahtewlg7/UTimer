package ahtewlg7.utimer.comparator;


import java.util.Comparator;

import ahtewlg7.utimer.entity.degree.IDegreeLevelBean;
import ahtewlg7.utimer.enumtype.ComparatorType;

/**
 * Created by lw on 2017/11/15.
 */

public class DegreeLevelComparator<T extends IDegreeLevelBean> implements Comparator<T> {
    public static final String TAG = DegreeLevelComparator.class.getSimpleName();

    @Override
    public int compare(T o1, T o2) {
        if(o1.getDegreeLevel().value() >  o2.getDegreeLevel().value())
            return ComparatorType.TH2_FIRST.value();
        else if(o1.getDegreeLevel().value() == o2.getDegreeLevel().value())
            return ComparatorType.NO_MATTER.value();
        else
            return ComparatorType.TH1_FIRST.value();
    }
}

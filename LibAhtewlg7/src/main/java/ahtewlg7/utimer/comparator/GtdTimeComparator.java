package ahtewlg7.utimer.comparator;


import java.util.Comparator;

import ahtewlg7.utimer.enumtype.ComparatorType;
import ahtewlg7.utimer.util.DateTimeAction;

/**
 * Created by lw on 2017/11/15.
 */

public class GtdTimeComparator<T extends ITimeComparator> implements Comparator<T> {
    public static final String TAG = GtdTimeComparator.class.getSimpleName();

    private DateTimeAction dateTimeAction;

    public GtdTimeComparator() {
        dateTimeAction = new DateTimeAction();
    }
    //who close who first
    @Override
    public int compare(T o2, T o1) {
        int result = ComparatorType.GREATER.value();
        if(!o2.getComparatorTime().isPresent() && o1.getComparatorTime().isPresent())
            result = ComparatorType.LESS.value();
        else if(o2.getComparatorTime().isPresent() && !o1.getComparatorTime().isPresent())
            result = ComparatorType.GREATER.value();
        else if(!o2.getComparatorTime().isPresent() && !o1.getComparatorTime().isPresent())
            result = ComparatorType.EQUAL.value();
        else{
            if((o2.getComparatorTime().get()).isBefore(o1.getComparatorTime().get()))
                result = ComparatorType.GREATER.value();
            else if((o2.getComparatorTime().get()).isEqual(o1.getComparatorTime().get()))
                result = ComparatorType.EQUAL.value();
            else
                result = ComparatorType.LESS.value();
        }
        return result;
    }
}

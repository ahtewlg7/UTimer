package ahtewlg7.utimer.comparator;


import org.joda.time.DateTime;

import java.util.Comparator;

import ahtewlg7.utimer.entity.AGtdUtimerEntity;
import ahtewlg7.utimer.enumtype.ComparatorType;
import ahtewlg7.utimer.util.DateTimeAction;

/**
 * Created by lw on 2017/11/15.
 */

public class GtdTimeComparator<T extends AGtdUtimerEntity> implements Comparator<T> {
    public static final String TAG = GtdTimeComparator.class.getSimpleName();

    private DateTimeAction dateTimeAction;

    public GtdTimeComparator() {
        dateTimeAction = new DateTimeAction();
    }

    @Override
    public int compare(T o1, T o2) {
        int result = ComparatorType.GREATER.value();
        if(!o1.getFirstWorkTime().isPresent() && o2.getFirstWorkTime().isPresent())
            result = ComparatorType.LESS.value();
        else if(o1.getFirstWorkTime().isPresent() && !o2.getFirstWorkTime().isPresent())
            result = ComparatorType.GREATER.value();
        else if(!o1.getFirstWorkTime().isPresent() && !o2.getFirstWorkTime().isPresent())
            result = ComparatorType.EQUAL.value();
        else{
            if(((DateTime)o1.getFirstWorkTime().get()).isBefore((DateTime)o2.getFirstWorkTime().get()))
                result = ComparatorType.GREATER.value();
            else if(((DateTime)o1.getFirstWorkTime().get()).isEqual((DateTime)o2.getFirstWorkTime().get()))
                result = ComparatorType.EQUAL.value();
            else
                result = ComparatorType.LESS.value();
        }
        return result;
    }
}

package ahtewlg7.utimer.comparator;


import org.joda.time.LocalTime;

import java.util.Comparator;

import ahtewlg7.utimer.enumtype.ComparatorType;

/**
 * Created by lw on 2017/11/15.
 */

public class BaseLocalTimeComparator {
    protected Comparator<LocalTime> ascOrder;
    protected Comparator<LocalTime> descOrder;

    public BaseLocalTimeComparator(){
        ascOrder    = new AscOrder();
        descOrder   = new DescOrder();
    }

    public Comparator<LocalTime> getAscOrder() {
        return ascOrder;
    }

    public Comparator<LocalTime> getDescOrder() {
        return descOrder;
    }

    class DescOrder implements Comparator<LocalTime>{
        @Override
        public int compare(LocalTime o1, LocalTime o2) {
            int result = ComparatorType.TH2_FIRST.value();
            if(o1.isBefore(o2))
                result = ComparatorType.TH2_FIRST.value();
            else if(o1.isEqual(o2))
                result = ComparatorType.NO_MATTER.value();
            else
                result = ComparatorType.TH1_FIRST.value();
            return result;
        }
    }
    class AscOrder implements Comparator<LocalTime>{
        @Override
        public int compare(LocalTime o1, LocalTime o2) {
            int result = ComparatorType.TH2_FIRST.value();
            if(o1.isBefore(o2))
                result = ComparatorType.TH1_FIRST.value();
            else if(o1.isEqual(o2))
                result = ComparatorType.NO_MATTER.value();
            else
                result = ComparatorType.TH2_FIRST.value();
            return result;
        }
    }
}

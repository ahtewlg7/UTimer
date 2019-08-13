package ahtewlg7.utimer.comparator;


import com.google.common.base.Optional;

import org.joda.time.LocalTime;

import java.util.Comparator;

import ahtewlg7.utimer.enumtype.ComparatorType;

/**
 * Created by lw on 2017/11/15.
 */

public abstract class ALocalTimeComparator<T> {
    protected Comparator<T> ascOrder;
    protected Comparator<T> descOrder;

    protected abstract Optional<LocalTime> getComparatorTime(T t);

    protected ALocalTimeComparator(){
        ascOrder    = new AscOrder();
        descOrder   = new DescOrder();
    }

    public Comparator<T> getAscOrder() {
        return ascOrder;
    }

    public Comparator<T> getDescOrder() {
        return descOrder;
    }

    class DescOrder implements Comparator<T>{
        @Override
        public int compare(T o1, T o2) {
            int result = ComparatorType.TH2_FIRST.value();
            if(!getComparatorTime(o1).isPresent() && getComparatorTime(o2).isPresent())
                result = ComparatorType.TH2_FIRST.value();
            else if(getComparatorTime(o1).isPresent() && !getComparatorTime(o2).isPresent())
                result = ComparatorType.TH1_FIRST.value();
            else if(!getComparatorTime(o1).isPresent() && !getComparatorTime(o2).isPresent())
                result = ComparatorType.NO_MATTER.value();
            else{
                if((getComparatorTime(o1).get()).isBefore(getComparatorTime(o2).get()))
                    result = ComparatorType.TH2_FIRST.value();
                else if((getComparatorTime(o1).get()).isEqual(getComparatorTime(o2).get()))
                    result = ComparatorType.NO_MATTER.value();
                else
                    result = ComparatorType.TH1_FIRST.value();
            }
            return result;
        }
    }
    class AscOrder implements Comparator<T>{
        @Override
        public int compare(T o1, T o2) {
            int result = ComparatorType.TH2_FIRST.value();
            if(!getComparatorTime(o1).isPresent() && getComparatorTime(o2).isPresent())
                result = ComparatorType.TH2_FIRST.value();
            else if(getComparatorTime(o1).isPresent() && !getComparatorTime(o2).isPresent())
                result = ComparatorType.TH1_FIRST.value();
            else if(!getComparatorTime(o1).isPresent() && !getComparatorTime(o2).isPresent())
                result = ComparatorType.NO_MATTER.value();
            else{
                if((getComparatorTime(o1).get()).isBefore(getComparatorTime(o2).get()))
                    result = ComparatorType.TH1_FIRST.value();
                else if((getComparatorTime(o1).get()).isEqual(getComparatorTime(o2).get()))
                    result = ComparatorType.NO_MATTER.value();
                else
                    result = ComparatorType.TH2_FIRST.value();
            }
            return result;
        }
    }
}

package ahtewlg7.utimer.comparator;


import java.util.Comparator;

import ahtewlg7.utimer.enumtype.ComparatorType;

/**
 * Created by lw on 2017/11/15.
 */

public abstract class ABaseIntComparator<T> {
    protected Comparator<T> ascOrder;
    protected Comparator<T> descOrder;

    protected abstract int getComparatorInt(T t);

    protected ABaseIntComparator(){
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
            int result = ComparatorType.NO_MATTER.value();
            if(getComparatorInt(o1) < getComparatorInt(o2))
                result = ComparatorType.TH2_FIRST.value();
            else if(getComparatorInt(o1) > getComparatorInt(o2))
                result = ComparatorType.TH1_FIRST.value();
            return result;
        }
    }
    class AscOrder implements Comparator<T>{
        @Override
        public int compare(T o1, T o2) {
            int result = ComparatorType.NO_MATTER.value();
            if(getComparatorInt(o1) < getComparatorInt(o2))
                result = ComparatorType.TH1_FIRST.value();
            else if(getComparatorInt(o1) > getComparatorInt(o2))
                result = ComparatorType.TH2_FIRST.value();
            return result;
        }
    }
}

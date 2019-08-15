package ahtewlg7.utimer.comparator;

import java.util.Comparator;

import ahtewlg7.utimer.enumtype.ComparatorType;

public class BaseIntComparator {
    protected Comparator<Integer>  ascOrder;
    protected Comparator<Integer>  descOrder;

    public BaseIntComparator(){
        ascOrder    = new AscOrder();
        descOrder   = new DescOrder();
    }

    public Comparator<Integer> getAscOrder() {
        return ascOrder;
    }

    public Comparator<Integer> getDescOrder() {
        return descOrder;
    }

    class DescOrder implements Comparator<Integer>{
        @Override
        public int compare(Integer o1, Integer o2) {
            int result = ComparatorType.NO_MATTER.value();
            if(o1 < o2)
                result = ComparatorType.TH2_FIRST.value();
            else if(o1 > o2)
                result = ComparatorType.TH1_FIRST.value();
            return result;
        }
    }
    class AscOrder implements Comparator<Integer>{
        @Override
        public int compare(Integer o1,Integer o2) {
            int result = ComparatorType.NO_MATTER.value();
            if(o1 < o2)
                result = ComparatorType.TH1_FIRST.value();
            else if(o1 > o2)
                result = ComparatorType.TH2_FIRST.value();
            return result;
        }
    }
}

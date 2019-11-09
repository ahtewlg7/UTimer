package ahtewlg7.utimer.comparator;

import ahtewlg7.utimer.enumtype.DATE_MONTH;

public class DateMonthComparator extends ABaseIntComparator<DATE_MONTH> {
    @Override
    protected int getComparatorInt(DATE_MONTH dateMonth) {
        return dateMonth.value();
    }
}

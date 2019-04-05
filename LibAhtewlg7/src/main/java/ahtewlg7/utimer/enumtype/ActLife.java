package ahtewlg7.utimer.enumtype;

import com.google.common.collect.Lists;

import java.util.List;

public enum ActLife {
    TODAY,
    TOMORROW,
    WEEK,
    MONTH,
    QUARTER,
    YEAR;

    public static List<ActLife> getAll(){
        List<ActLife> list = Lists.newArrayList();
        list.add(TODAY);
        list.add(TOMORROW);
        list.add(WEEK);
        list.add(MONTH);
        list.add(QUARTER);
        list.add(YEAR);
        return list;
    }
}

package ahtewlg7.utimer.enumtype;

import com.google.common.collect.Lists;

import java.util.List;

public enum DeedState {
    MAYBE,//it may be parse by ai，but sometime it is not correct.
    TRASH,
    INBOX,
    REFERENCE,
    WISH,
    TWO_MIN,
    PROJECT,
    DEFER,
    CALENDAR,
    DELEGATE,
    DONE;

    public static List<DeedState> getActiveAll(){
        List<DeedState> list = Lists.newArrayList();
        list.add(INBOX);
        list.add(WISH);
        list.add(TWO_MIN);
        list.add(PROJECT);
        list.add(DEFER);
        list.add(CALENDAR);
        list.add(DELEGATE);
        return list;
    }
}

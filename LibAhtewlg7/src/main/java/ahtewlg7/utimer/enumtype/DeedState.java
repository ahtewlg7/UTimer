package ahtewlg7.utimer.enumtype;

import com.google.common.collect.Lists;

import java.util.List;

public enum DeedState {
    MAYBE(0),//it may be parse by ai，but sometime it is not correct.
    TRASH(1),
    INBOX(2),
    REFERENCE(10),
    WISH(4),
    ONE_QUARTER(5),
    PROJECT(6),
    DEFER(7),
    CALENDAR(8),
    DELEGATE(9),
    DONE(3),
    USELESS(11);

    private int value;

    DeedState(int value){
        this.value = value;
    }

    public static DeedState valueOf(int index){
        DeedState tmp = null;
        switch (index){
            default:
            case 0:
                tmp = MAYBE;
                break;
            case 1:
                tmp = TRASH;
                break;
            case 2:
                tmp = INBOX;
                break;
            case 3:
                tmp = DONE;
                break;
            case 4:
                tmp = WISH;
                break;
            case 5:
                tmp = ONE_QUARTER;
                break;
            case 6:
                tmp = PROJECT;
                break;
            case 7:
                tmp = DEFER;
                break;
            case 8:
                tmp = CALENDAR;
                break;
            case 9:
                tmp = DELEGATE;
                break;
            case 10:
                tmp = REFERENCE;
                break;
            case 11:
                tmp = USELESS;
                break;
        }
        return tmp;
    }

    public int value(){
        return value;
    }

    public static List<DeedState> getActiveAll(){
        List<DeedState> list = Lists.newArrayList();
        list.add(INBOX);
        list.add(WISH);
        list.add(ONE_QUARTER);
        list.add(PROJECT);
        list.add(DEFER);
        list.add(CALENDAR);
        list.add(DELEGATE);
        return list;
    }
}

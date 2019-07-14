package ahtewlg7.utimer.enumtype;

import com.google.common.collect.Lists;

import java.util.List;

public enum DeedState {
    MAYBE(0, 11),//it may be parse by ai，but sometime it is not correct.
    TRASH(1, 8),
    INBOX(2,10),
    REFERENCE(10, 5),
    WISH(4, 4),
    ONE_QUARTER(5, 0),
    PROJECT(6, 3),
    DEFER(7, 1),
    CALENDAR(8, 9),
    DELEGATE(9, 2),
    DONE(3, 6),
    USELESS(11, 7);

    private int value;
    private int order;

    DeedState(int value, int order){
        this.value = value;
        this.order = order;
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

    public static DeedState orderOf(int index){
        DeedState tmp = null;
        switch (index){
            default:
            case 0:
                tmp = ONE_QUARTER;
                break;
            case 1:
                tmp = DEFER;
                break;
            case 2:
                tmp = DELEGATE;
                break;
            case 3:
                tmp = PROJECT;
                break;
            case 4:
                tmp = WISH;
                break;
            case 5:
                tmp = REFERENCE;
                break;
            case 6:
                tmp = DONE;
                break;
            case 7:
                tmp = USELESS;
                break;
            case 8:
                tmp = TRASH;
                break;
            case 9:
                tmp = CALENDAR;
                break;
            case 10:
                tmp = INBOX;
                break;
            case 11:
                tmp = MAYBE;
                break;
        }
        return tmp;
    }

    public int value(){
        return value;
    }

    public int order() {
        return order;
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

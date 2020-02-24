package ahtewlg7.utimer.enumtype;

public enum DeedState {
    MAYBE(0, 8),//it may be parse by ai，but sometime it is not correct.
    TRASH(1, 11),
    INBOX(2,7),
    REFERENCE(10, 6),
    WISH(4, 5),
    ONE_QUARTER(5, 1),
    PROJECT(6, 4),
    DEFER(7, 2),
    CALENDAR(8, 12),//Deprecated： it is not used
    DELEGATE(9, 3),
    DONE(3, 9),
    USELESS(11, 10),
    SCHEDULE(12, 0);

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
            case 12:
                tmp = SCHEDULE;
                break;
        }
        return tmp;
    }

    public static DeedState orderOf(int index){
        DeedState tmp = null;
        switch (index){
            default:
            case 0:
                tmp = SCHEDULE;
                break;
            case 1:
                tmp = ONE_QUARTER;
                break;
            case 2:
                tmp = DEFER;
                break;
            case 3:
                tmp = DELEGATE;
                break;
            case 4:
                tmp = PROJECT;
                break;
            case 5:
                tmp = WISH;
                break;
            case 6:
                tmp = REFERENCE;
                break;
            case 7:
                tmp = INBOX;
                break;
            case 8:
                tmp = MAYBE;
                break;
            case 9:
                tmp = DONE;
                break;
            case 10:
                tmp = USELESS;
                break;
            case 11:
                tmp = TRASH;
                break;
            case 12:
                tmp = CALENDAR;
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
}

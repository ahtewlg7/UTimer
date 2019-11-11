package ahtewlg7.utimer.enumtype;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.util.MyRInfo;

public enum DATE_WEEK {
    SUN(1),
    MON(2),
    TUE(3),
    WED(4),
    THU(5),
    FRI(6),
    SAT(7);

    private final int value;

    DATE_WEEK(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }

    public String getDetail(){
        String tmp = null;
        switch (value){
            case 1:
                tmp = MyRInfo.getStringByID(R.string.Sun);
                break;
            case 2:
                tmp = MyRInfo.getStringByID(R.string.Mon);
                break;
            case 3:
                tmp = MyRInfo.getStringByID(R.string.Tue);
                break;
            case 4:
                tmp = MyRInfo.getStringByID(R.string.Wed);
                break;
            case 5:
                tmp = MyRInfo.getStringByID(R.string.Thu);
                break;
            case 6:
                tmp = MyRInfo.getStringByID(R.string.Fri);
                break;
            case 7:
                tmp = MyRInfo.getStringByID(R.string.Sat);
                break;
        }
        return tmp;
    }

    public static DATE_WEEK valueOf(int value){
        DATE_WEEK tmp = null;
        switch (value){
            case 1:
                tmp = SUN;
                break;
            case 2:
                tmp = MON;
                break;
            case 3:
                tmp = TUE;
                break;
            case 4:
                tmp = WED;
                break;
            case 5:
                tmp = THU;
                break;
            case 6:
                tmp = FRI;
                break;
            case 7:
                tmp = SAT;
                break;
        }
        return tmp;
    }
}

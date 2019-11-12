package ahtewlg7.utimer.enumtype;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.util.MyRInfo;


//to check the order by the DateTimeConstants.java of Joda_time
public enum DATE_WEEK {
    MON(1),
    TUE(2),
    WED(3),
    THU(4),
    FRI(5),
    SAT(6),
    SUN(7),;

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
                tmp = MyRInfo.getStringByID(R.string.Mon);
                break;
            case 2:
                tmp = MyRInfo.getStringByID(R.string.Tue);
                break;
            case 3:
                tmp = MyRInfo.getStringByID(R.string.Wed);
                break;
            case 4:
                tmp = MyRInfo.getStringByID(R.string.Thu);
                break;
            case 5:
                tmp = MyRInfo.getStringByID(R.string.Fri);
                break;
            case 6:
                tmp = MyRInfo.getStringByID(R.string.Sat);
                break;
            case 7:
                tmp = MyRInfo.getStringByID(R.string.Sun);
                break;
        }
        return tmp;
    }

    public static DATE_WEEK valueOf(int value){
        DATE_WEEK tmp = null;
        switch (value){
            case 1:
                tmp = MON;
                break;
            case 2:
                tmp = TUE;
                break;
            case 3:
                tmp = WED;
                break;
            case 4:
                tmp = THU;
                break;
            case 5:
                tmp = FRI;
                break;
            case 6:
                tmp = SAT;
                break;
            case 7:
                tmp = SUN;
                break;
        }
        return tmp;
    }
}

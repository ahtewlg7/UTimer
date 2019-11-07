package ahtewlg7.utimer.enumtype;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.util.MyRInfo;

public enum DATE_MONTH {
    JAN(1),
    FEB(2),
    MAR(3),
    APR(4),
    MAY(5),
    JUN(6),
    JUL(7),
    AUG(8),
    SEPT(9),
    OCT(10),
    NOV(11),
    DEC(12);

    private final int value;

    DATE_MONTH(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }

    public String getDetail(){
        String tmp = null;
        switch (value){
            case 1:
                tmp = MyRInfo.getStringByID(R.string.Jan);
                break;
            case 2:
                tmp = MyRInfo.getStringByID(R.string.Feb);
                break;
            case 3:
                tmp = MyRInfo.getStringByID(R.string.Mar);
                break;
            case 4:
                tmp = MyRInfo.getStringByID(R.string.Apr);
                break;
            case 5:
                tmp = MyRInfo.getStringByID(R.string.May);
                break;
            case 6:
                tmp = MyRInfo.getStringByID(R.string.Jun);
                break;
            case 7:
                tmp = MyRInfo.getStringByID(R.string.Jul);
                break;
            case 8:
                tmp = MyRInfo.getStringByID(R.string.Aug);
                break;
            case 9:
                tmp = MyRInfo.getStringByID(R.string.Sept);
                break;
            case 10:
                tmp = MyRInfo.getStringByID(R.string.Oct);
                break;
            case 11:
                tmp = MyRInfo.getStringByID(R.string.Nov);
                break;
            case 12:
                tmp = MyRInfo.getStringByID(R.string.Dec);
                break;
        }
        return tmp;
    }

    public static DATE_MONTH valueOf(int value){
        DATE_MONTH tmp = null;
        switch (value){
            case 1:
                tmp = JAN;
                break;
            case 2:
                tmp = FEB;
                break;
            case 3:
                tmp = MAR;
                break;
            case 4:
                tmp = APR;
                break;
            case 5:
                tmp = MAY;
                break;
            case 6:
                tmp = JUN;
                break;
            case 7:
                tmp = JUL;
                break;
            case 8:
                tmp = AUG;
                break;
            case 9:
                tmp = SEPT;
                break;
            case 10:
                tmp = OCT;
                break;
            case 11:
                tmp = NOV;
                break;
            case 12:
                tmp = DEC;
                break;
        }
        return tmp;
    }
}

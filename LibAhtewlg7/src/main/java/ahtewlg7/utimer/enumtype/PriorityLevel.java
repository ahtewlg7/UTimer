package ahtewlg7.utimer.enumtype;

/**
 * Created by lw on 2016/9/13.
 */
public enum PriorityLevel {
    A1(1),
    A2(2),
    A3(3),
    A4(4),
    B1(5),
    B2(6),
    B3(7),
    B4(8),
    C1(9),
    C2(10),
    C3(11),
    C4(12),
    D1(13),
    D2(14),
    D3(15),
    D4(16);

    public static final PriorityLevel DEFAULT_PRIORITY_LEVEL = B4;

    private final int value;

    PriorityLevel(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }

    public static PriorityLevel valueOf(int value){
        switch (value){
            case 1:
                return A1;
            case 2:
                return A2;
            case 3:
                return A3;
            case 4:
                return A4;
            case 5:
                return B1;
            case 6:
                return B2;
            case 7:
                return B3;
            case 8:
                return B4;
            case 9:
                return C1;
            case 10:
                return C2;
            case 11:
                return C3;
            case 12:
                return C4;
            case 13:
                return D1;
            case 14:
                return D2;
            case 15:
                return D3;
            case 16:
                return D4;
            default:
                return DEFAULT_PRIORITY_LEVEL;
        }
    }
}

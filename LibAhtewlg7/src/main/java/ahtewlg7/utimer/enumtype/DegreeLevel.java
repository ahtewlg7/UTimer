package ahtewlg7.utimer.enumtype;

/**
 * Created by lw on 2016/9/13.
 */
public enum DegreeLevel {
    DE1(1),
    DE2(2),
    DE3(3),
    DE4(4),
    DE5(5),
    DE6(6),
    DE7(7),
    DE8(8),
    DE9(9),
    DE10(10);

    private final int value;

    DegreeLevel(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }

    public static DegreeLevel valueOf(int value){
        switch (value){
            case 1:
                return DE1;
            case 2:
                return DE2;
            case 3:
                return DE3;
            case 4:
                return DE4;
            case 5:
                return DE5;
            case 6:
                return DE6;
            case 7:
                return DE7;
            case 8:
                return DE8;
            case 9:
                return DE9;
            case 10:
                return DE10;
            default:
                return DE5;
        }
    }
}

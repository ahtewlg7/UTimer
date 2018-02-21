package ahtewlg7.utimer.enumtype;

/**
 * Created by lw on 2016/9/13.
 */
public enum ComparatorType {
    LESS(-1),
    EQUAL(0),
    GREATER(1);

    private final int value;

    ComparatorType(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }

    public static ComparatorType valueOf(int value){
        switch (value){
            case -1:
                return LESS;
            case 0:
                return EQUAL;
            case 1:
                return GREATER;
            default:
                return EQUAL;
        }
    }
}

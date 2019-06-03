package ahtewlg7.utimer.enumtype;

/**
 * Created by lw on 2016/9/13.
 */
public enum ComparatorType {
    TH1_FIRST(-1),
    NO_MATTER(0),
    TH2_FIRST(1);

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
                return TH1_FIRST;
            case 0:
                return NO_MATTER;
            case 1:
                return TH2_FIRST;
            default:
                return NO_MATTER;
        }
    }
}

package ahtewlg7.utimer.enumtype;

/**
 * Created by lw on 2017/2/22.
 */
public enum StorageType {
    UNFILE(0),
    DIR(1),
    TXT(2),
    MD(3),
    AUDIO(4),
    VIDEO(5),
    IMAGE(6);

    private final int value;

    StorageType(int value){
        this.value = value;
    }

    public int value(){
        return value;
    }

    public static StorageType valueOf(int value){
        switch (value){
            case 0:
                return UNFILE;
            case 1:
                return DIR;
            case 2:
                return TXT;
            case 3:
                return MD;
            case 4:
                return AUDIO;
            case 5:
                return VIDEO;
            case 6:
                return IMAGE;
            default:
                return UNFILE;
        }
    }
}

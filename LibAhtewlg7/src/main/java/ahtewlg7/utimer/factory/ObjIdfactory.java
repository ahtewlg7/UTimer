package ahtewlg7.utimer.factory;

import java.util.UUID;

public class ObjIdfactory {
    public static final String TAgG = ObjIdfactory.class.getCanonicalName();

    public static String createId(){
        return UUID.randomUUID().toString();
    }
}

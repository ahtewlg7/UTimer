package ahtewlg7.utimer.common;

import java.util.UUID;

/**
 * Created by lw on 2017/12/28.
 */

public class IdAction {
    public static final String TAG = IdAction.class.getSimpleName();

    public String getUUId(){
        return UUID.randomUUID().toString();
    }

    public String getNoteId(){
        return getUUId();
    }
}

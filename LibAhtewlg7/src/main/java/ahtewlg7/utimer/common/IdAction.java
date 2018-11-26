package ahtewlg7.utimer.common;

import com.blankj.utilcode.util.PhoneUtils;

import org.joda.time.Instant;

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

    public String getGtdId(){
        String uuid         = UUID.randomUUID().toString();
        String dateTime     = Instant.now().toString();
        String deviceInfo   = PhoneUtils.getIMEI();

        return uuid;
    }
}

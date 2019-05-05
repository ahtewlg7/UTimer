package ahtewlg7.utimer.entity.context;

import android.text.TextUtils;

import org.joda.time.DateTime;

/**
 * Created by lw on 2019/5/4.
 */
public class TimeContext implements IContext{
    private String name;
    private DateTime dateTime;

    public TimeContext(String name, DateTime dateTime) {
        this.name = name;
        this.dateTime = dateTime;
    }

    public String getName() {
        return name;
    }

    public DateTime getDateTime() {
        return dateTime;
    }

    @Override
    public boolean ifValid() {
        return !TextUtils.isEmpty(name) && dateTime != null;
    }
}

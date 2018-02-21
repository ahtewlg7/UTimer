package ahtewlg7.utimer.entity.time;

import org.joda.time.Interval;

/**
 * Created by lw on 2017/11/29.
 */

public class TimeStampEntity {
    public static final String TAG = TimeStampEntity.class.getSimpleName();

    private Interval timeInterval;

    public Interval getTimeInterval() {
        return timeInterval;
    }

    public void setTimeInterval(Interval timeInterval) {
        this.timeInterval = timeInterval;
    }
}

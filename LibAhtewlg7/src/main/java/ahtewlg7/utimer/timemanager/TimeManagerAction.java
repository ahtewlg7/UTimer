package ahtewlg7.utimer.timemanager;

import androidx.annotation.NonNull;

import org.joda.time.Interval;

/**
 * Created by lw on 2017/11/26.
 */

public class TimeManagerAction {
    public static final String TAG = TimeManagerAction.class.getSimpleName();

    public Interval getNextFreeTime(){
        return null;
    }

    public boolean isTimeFree(@NonNull Interval timeInterval){
        return false;
    }

    public boolean isTimeOk(@NonNull Interval needTimeInterval){
        return isTimeFree(needTimeInterval);
    }
}

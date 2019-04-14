package ahtewlg7.utimer.entity.busevent;

import com.trello.rxlifecycle2.android.ActivityEvent;

import ahtewlg7.utimer.entity.BaseEventBusBean;

/**
 * Created by lw on 2019/3/13.
 */
public class ActivityBusEvent extends BaseEventBusBean{
    private ActivityEvent event;

    public ActivityBusEvent(ActivityEvent event) {
        this.event = event;
    }

    @Override
    public boolean ifValid() {
        return event != null;
    }

    public boolean ifOnBackground(){
        return ifValid() && (event == ActivityEvent.PAUSE || event == ActivityEvent.STOP);
    }

    public ActivityEvent getBusEvent() {
        return event;
    }
}

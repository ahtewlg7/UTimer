package ahtewlg7.utimer.factory;

import org.greenrobot.eventbus.EventBus;

public class EventBusFatory {
    public static final String TAG = EventBusFatory.class.getSimpleName();

    private static EventBusFatory instance;

    private EventBusFatory(){
    }

    public static EventBusFatory getInstance(){
        if(instance == null)
            instance = new EventBusFatory();
        return instance;
    }

    public EventBus getDefaultEventBus(){
        return EventBus.getDefault();
    }
}

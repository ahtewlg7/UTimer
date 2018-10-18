package ahtewlg7.utimer.mvp;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.busevent.EditEndBusEvent;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2018/10/14.
 */
public abstract class AUtimerListMvpP<T extends AUtimerEntity> implements IBaseListInfoMvpP<T> {
    public static final String TAG = AUtimerListMvpP.class.getSimpleName();

    protected EventBus eventBus;

    public AUtimerListMvpP() {
        eventBus   = EventBusFatory.getInstance().getDefaultEventBus();
    }

    //=======================================EventBus================================================
    public void toRegisterEventBus(){
        Logcat.i(TAG,"toRegisterEventBus");
        if(eventBus != null && !eventBus.isRegistered(this))
            eventBus.register(this);
    }
    public void toUnregisterEventBus(){
        Logcat.i(TAG,"toUnregisterEventBus");
        if(eventBus != null && eventBus.isRegistered(this))
            eventBus.unregister(this);
    }

    //EventBus callback
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEditEndBusEvent(EditEndBusEvent event) {
    }

}

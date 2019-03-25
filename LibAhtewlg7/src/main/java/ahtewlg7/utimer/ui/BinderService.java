package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ahtewlg7.utimer.entity.busevent.ActionBusEvent;
import ahtewlg7.utimer.enumtype.GtdBusEventType;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.mvp.GtdActionMvpP;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service{
    private MyGtdMvpV myGtdMvpV;
    private GtdActionMvpP gtdActionMvpP;

    @Override
    public void onCreate() {
        super.onCreate();

        myGtdMvpV       = new MyGtdMvpV();
        gtdActionMvpP   = new GtdActionMvpP(myGtdMvpV);

        EventBusFatory.getInstance().getDefaultEventBus().register(this);

        gtdActionMvpP.toLoadAllItem();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBusFatory.getInstance().getDefaultEventBus().unregister(this);
    }

    //+++++++++++++++++++++++++++++++++++++++++++Binder+++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public IBinder onBind(Intent arg0) {
        return new BaseServiceBinder();
    }

    public class BaseServiceBinder extends Binder {
        public BinderService getService() {
            return BinderService.this;
        }
    }
    //+++++++++++++++++++++++++++++++++++++++++++EventBus+++++++++++++++++++++++++++++++++++++++++++++++++++
    @Subscribe(threadMode = ThreadMode.MAIN, sticky=true)
    public void onGtdActionEntity(ActionBusEvent actionBusEvent) {
        gtdActionMvpP.toHandleActionEvent(actionBusEvent);
    }

    //+++++++++++++++++++++++++++++++++++++++++++GtdMpvV+++++++++++++++++++++++++++++++++++++++++++++++++++
    class MyGtdMvpV implements GtdActionMvpP.IGtdActionMvpV{
        @Override
        public void onActionAllLoaded() {
            ActionBusEvent actionBusEvent = new ActionBusEvent(GtdBusEventType.LOAD);
            EventBusFatory.getInstance().getDefaultEventBus().post(actionBusEvent);
        }
    }
}

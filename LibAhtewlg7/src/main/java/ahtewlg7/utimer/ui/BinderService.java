package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
        EventBusFatory.getInstance().getDefaultEventBus().register(this);
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
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onGtdActionEntity(GtdActionEntity actionEntity) {
        List<DateTime> dateTimes = actionEntity.getTimeList();
        for(DateTime date : dateTimes){
            String tmp = new DateTimeAction().toFormat(date);
            Logcat.i("test", "onGtdActionEntity : " + tmp);
        }
    }
}

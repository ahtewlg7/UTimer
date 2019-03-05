package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service{

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    @Override
    public IBinder onBind(Intent arg0) {
        return new BaseServiceBinder();
    }

    public class BaseServiceBinder extends Binder {
        public BinderService getService() {
            return BinderService.this;
        }
    }
}

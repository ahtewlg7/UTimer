package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ahtewlg7.utimer.GTD.state.GtdStateMachine;
import ahtewlg7.utimer.util.Logcat;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service{
    public static final String TAG = BinderService.class.getSimpleName();

    private GtdStateMachine gtdStateMachine;

    @Override
    public void onCreate() {
        super.onCreate();
        Logcat.i(TAG, "onCreate");

        gtdStateMachine = new GtdStateMachine();
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

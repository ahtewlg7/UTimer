package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ahtewlg7.utimer.mvp.MdFileMvpP;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.annotations.NonNull;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service{
    public static final String TAG = BinderService.class.getSimpleName();

    private MdFileMvpP mdContextMvpP;

    @Override
    public void onCreate() {
        super.onCreate();
        Logcat.i(TAG, "onCreate");

        mdContextMvpP   = new MdFileMvpP();
        mdContextMvpP.toRegisterEventBus();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logcat.i(TAG, "onDestroy");
        mdContextMvpP.toUnregisterEventBus();
    }

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public @NonNull
    MdFileMvpP getMdContextMvpP(){
        return mdContextMvpP;
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

package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.mvp.NoteSaveMvpP;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.annotations.NonNull;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service{
    public static final String TAG = BinderService.class.getSimpleName();

    private NoteSaveMvpP noteSaveMvpP;

    @Override
    public void onCreate() {
        super.onCreate();
        Logcat.i(TAG, "onCreate");

        noteSaveMvpP = new NoteSaveMvpP(null);
    }

    public void toSaveNote(@NonNull INoteEntity noteEntity){
        noteSaveMvpP.toSaveNoteContext(noteEntity);
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

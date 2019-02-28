package ahtewlg7.utimer.ui;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import ahtewlg7.utimer.nlp.HanlpAction;


/**
 * Created by lw on 2016/11/15.
 */

public class BinderService extends Service{

    private HanlpAction hanlpAction;

    @Override
    public void onCreate() {
        super.onCreate();
//        toInitNLP();
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


    protected void toInitNLP(){
        hanlpAction = new HanlpAction();
        hanlpAction.initNLP();
        hanlpAction.toTest();
    }

}

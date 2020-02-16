package ahtewlg7.utimer.ui;

import android.content.ComponentName;
import android.content.Context;
import android.os.IBinder;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ServiceUtils;

public class BaseBinderRxActivity extends BaseRxActivity {
    protected IBinder binderProxy;
    protected ServiceConnection serviceConn;

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    protected void onServiceBinderDisconnected(ComponentName name){
    }
    protected void onServiceBinderConnected(ComponentName name){
    }

    protected @NonNull Class<? extends BinderService> getBinderServiceClass(){
        return BinderService.class;
    }

    protected void toBindService(){
        try{
            if(serviceConn == null)
                serviceConn  = new ServiceConnection();

            if(binderProxy == null)
                ServiceUtils.bindService(getBinderServiceClass(), serviceConn, Context.BIND_AUTO_CREATE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void toUnbindService(){
        try{
            if(binderProxy != null && serviceConn != null)
                ServiceUtils.unbindService(serviceConn);
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected class ServiceConnection implements android.content.ServiceConnection {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            onServiceBinderDisconnected(name);
        }
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            binderProxy = service;
//			BinderService serviceBinderProxy = ((BinderService.BaseServiceBinder)service).getService();
            onServiceBinderConnected(name);
        }
    }
}

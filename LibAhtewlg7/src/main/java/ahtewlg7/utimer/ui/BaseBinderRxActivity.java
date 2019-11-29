package ahtewlg7.utimer.ui;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        serviceConn  = new ServiceConnection();
        toBindService(serviceConn);
    }
    @Override
    protected void onDestroy() {
        toUnbindService(serviceConn);
        super.onDestroy();
    }


    protected @NonNull Class<? extends BinderService> getBinderServiceClass(){
        return BinderService.class;
    }

    protected void toBindService(ServiceConnection serviceConn){
        try{
            if(serviceConn != null) {
                ServiceUtils.bindService(getBinderServiceClass(), serviceConn, Context.BIND_AUTO_CREATE);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected void toUnbindService(ServiceConnection serviceConn){
        try{
            if(serviceConn != null) {
                ServiceUtils.unbindService(serviceConn);
            }
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

package ahtewlg7.utimer.ui;

import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;

import com.blankj.utilcode.util.ServiceUtils;
import com.trello.rxlifecycle2.components.RxActivity;

import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2016/11/15.
 */


public class BaseBinderActivity extends RxActivity {
	public static final String TAG = BaseBinderActivity.class.getSimpleName();

	protected BinderService serviceBinderProxy;
	protected ServiceConnection serviceConn;

	//++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
	protected void onServiceBinderDisconnected(ComponentName name){
		Logcat.d(TAG,"unbind to " + name);
	}
	protected void onServiceBinderConnected(ComponentName name){
		Logcat.d(TAG,"bind to " + name);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		serviceConn  = new ServiceConnection();
        toBindService(serviceConn);
	}
	@Override
	protected void onDestroy() {
		Logcat.d(TAG, "onDestroy");
        toUnbindService(serviceConn);
		super.onDestroy();
	}

	protected void toBindService(ServiceConnection serviceConn){
	    try{
	    	if(serviceConn != null) {
	    	    Logcat.d(TAG,"toBindService");
                ServiceUtils.bindService(BinderService.class, serviceConn, Context.BIND_AUTO_CREATE);
            }
        }catch (Exception e){
	        e.printStackTrace();
        }
    }
    protected void toUnbindService(ServiceConnection serviceConn){
		try{
			if(serviceConn != null) {
			    Logcat.d(TAG,"unBindService");
                ServiceUtils.unbindService(serviceConn);
            }
		}catch (Exception e){
			e.printStackTrace();
		}
	}
	protected class ServiceConnection implements android.content.ServiceConnection {
		@Override
		public void onServiceDisconnected(ComponentName name) {
			Logcat.d(TAG,"onServiceDisconnected");
			onServiceBinderDisconnected(name);
		}
		@Override
		public void onServiceConnected(ComponentName name, IBinder service) {
			Logcat.d(TAG,"onServiceConnected");
			serviceBinderProxy = ((BinderService.BaseServiceBinder)service).getService();
			onServiceBinderConnected(name);
		}
	}
}

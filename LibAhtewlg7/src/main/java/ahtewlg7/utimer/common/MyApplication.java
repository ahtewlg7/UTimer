package ahtewlg7.utimer.common;

import android.app.Application;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.blankj.utilcode.util.Utils;

import ahtewlg7.utimer.db.GreenDaoAction;
import ahtewlg7.utimer.util.AppInfoAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.ProcessAction;
import ahtewlg7.utimer.verctrl.IBaseVersionControlFactory;
import ahtewlg7.utimer.verctrl.VcFactoryBuilder;


public abstract class MyApplication extends Application {
	public static final String TAG = MyApplication.class.getSimpleName();

	public abstract @NonNull IBaseVersionControlFactory getConfigFactory();

	@Override
	public void onCreate() {
		super.onCreate();

		//this is for multi-process add by "android:process" in AndroidManifest.xml
		Log.d(TAG,"onCreate");
		Utils.init(MyApplication.this);
		String currProcessName = ProcessAction.getProcessName(android.os.Process.myPid());
		if (!TextUtils.isEmpty(currProcessName) && currProcessName.equalsIgnoreCase(this.getPackageName())) {
			initLibContext();
			initVcFactory();//it is very important
            initDatabase();
			toStartIraiBinderService();
		}
	}

	protected void initLibContext(){
        Log.d(TAG,"onCreate ifDebug = " + AppInfoAction.ifDebug());
        LibContextInit.initLog(AppInfoAction.ifDebug(), false);
		LibContextInit.initWorkingFileSystem();
	}

	protected void initVcFactory(){
		VcFactoryBuilder.getInstance().setBaseConfigFactory(getConfigFactory());
	}

	protected void initDatabase(){
		GreenDaoAction.getInstance().init();
	}

	protected void toStartIraiBinderService(){
		Logcat.d(TAG,"to start BinderService");
		Intent bootIntent = new Intent(this, getConfigFactory().getBinderServiceClass());
		bootIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startService(bootIntent);
	}
}

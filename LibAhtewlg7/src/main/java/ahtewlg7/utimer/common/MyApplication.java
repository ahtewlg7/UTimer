package ahtewlg7.utimer.common;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.ServiceUtils;
import com.blankj.utilcode.util.Utils;

import org.acra.ACRA;
import org.acra.annotation.AcraCore;
import org.acra.data.StringFormat;

import ahtewlg7.utimer.BuildConfig;
import ahtewlg7.utimer.log.AcraReportSenderFactory;
import ahtewlg7.utimer.ui.BinderService;
import ahtewlg7.utimer.util.AppInfoAction;
import ahtewlg7.utimer.util.ProcessAction;
import ahtewlg7.utimer.verctrl.IBaseVersionControlFactory;
import ahtewlg7.utimer.verctrl.VcFactoryBuilder;
import me.yokeyword.fragmentation.Fragmentation;

@AcraCore(buildConfigClass = BuildConfig.class,
		reportFormat = StringFormat.JSON,
		reportSenderFactoryClasses = AcraReportSenderFactory.class)
public abstract class MyApplication extends Application {
	public abstract @NonNull IBaseVersionControlFactory getConfigFactory();

	@Override
	public void onCreate() {
		super.onCreate();

		//this is for multi-process add by "android:process" in AndroidManifest.xml
		Utils.init(MyApplication.this);
		String currProcessName = ProcessAction.getProcessName(android.os.Process.myPid());
		if (!TextUtils.isEmpty(currProcessName) && currProcessName.equalsIgnoreCase(this.getPackageName())) {
			initLogSystem();
			initVcFactory();//it is very important
			toStartBinderService();
		}
	}

	@Override
	protected void attachBaseContext(Context base) {
		super.attachBaseContext(base);
		ACRA.init(this);
	}

	protected void initLogSystem(){
        LibContextInit.initLog(AppInfoAction.ifDebug(), false);
	}

	protected void initVcFactory(){
		VcFactoryBuilder.getInstance().setBaseConfigFactory(getConfigFactory());
	}

	protected void toStartBinderService(){
		ServiceUtils.startService(BinderService.class);
	}

	protected final Fragmentation.FragmentationBuilder getFragmentationBuilder(){
		return Fragmentation.builder()
				.stackViewMode(Fragmentation.NONE)
				.debug(BuildConfig.DEBUG);
	}
}

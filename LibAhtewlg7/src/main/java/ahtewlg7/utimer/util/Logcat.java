package ahtewlg7.utimer.util;


import com.orhanobut.logger.Logger;

import ahtewlg7.utimer.log.MyLoggerAdapterFactory;

public class Logcat {
	private static boolean loged        = false;
	private static boolean saved        = false;

	public static boolean isLoged() {
		return loged;
	}

	public static boolean isSaved(){
		return saved;
	}

	public static void w(String tag, String content){
		if(loged)
			Logger.t(tag).w(content);
	}

	public static void d(String tag, String content){
		if(loged)
            Logger.t(tag).d(content);
	}
	public static void e(String tag, String content){
		if(loged)
            Logger.t(tag).e(content);
	}
	public static void i(String tag, String content){
		if(loged)
            Logger.t(tag).i(content);
	}
	public static void v(String tag, String content){
		if(loged)
            Logger.t(tag).v(content);
	}

	public static void enableSave(){
        loged = true;
        saved = true;
		Logger.clearLogAdapters();
		Logger.addLogAdapter(new MyLoggerAdapterFactory().getBaseAndroidLogAdapter(true));
		Logger.addLogAdapter(new MyLoggerAdapterFactory().getBaseDiskLogAdapter());
	}
    public static void enableLog(){
        loged = true;
        saved = false;
		Logger.clearLogAdapters();
		Logger.addLogAdapter(new MyLoggerAdapterFactory().getBaseAndroidLogAdapter(true));
	}
    public static void disableLog(){
        loged = false;
        saved = false;
		Logger.clearLogAdapters();
	}
}

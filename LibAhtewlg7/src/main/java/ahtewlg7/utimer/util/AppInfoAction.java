package ahtewlg7.utimer.util;

import ahtewlg7.utimer.BuildConfig;

/**
 * Created by lw on 2017/9/21.
 */

public class AppInfoAction {
    public static final String TAG = AppInfoAction.class.getSimpleName();

    public static String getAppName(){
        return BuildConfig.APPLICATION_ID;
    }
    public static int getVerCode(){
        return BuildConfig.VERSION_CODE;
    }
    public static String getVerName(){
        return BuildConfig.VERSION_NAME;
    }

    public static boolean ifDebug(){
        return BuildConfig.DEBUG;
    }
}
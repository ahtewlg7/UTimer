package ahtewlg7.utimer.util;

import android.app.ActivityManager;

import java.util.List;

public class ProcessAction {
    public static final String TAG = ProcessAction.class.getSimpleName();

    public static String getProcessName(int pid) {
        ActivityManager am = new AndrManagerFactory().getActivityManager();
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo procInfo : runningApps) {
            if (procInfo.pid == pid) {
                return procInfo.processName;
            }
        }
        return null;
    }
}

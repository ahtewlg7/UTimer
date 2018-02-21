package ahtewlg7.utimer.common;


import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2017/9/25.
 */

public class LibContextInit {
	private final static String TAG = LibContextInit.class.getSimpleName();

    public static void initLog(boolean loged , boolean saved){
        if(loged && !saved)
            Logcat.enableLog();
        else if(loged)
            Logcat.enableSave();
        else
            Logcat.disableLog();
    }

    public static void initWorkingFileSystem(){
		FileSystemAction fileSystemAction = new FileSystemAction();
		fileSystemAction.toInitWorkingEnv();
	}
}

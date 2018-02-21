package ahtewlg7.utimer.util;


import android.graphics.drawable.Drawable;

import com.blankj.utilcode.util.Utils;


/**
 * Created by 10139980 on 2016/11/15.
 */


public class MyRInfo {
    private static final String TAG = MyRInfo.class.getSimpleName();
    
	private MyRInfo() {
	}
    
    public static Drawable getDrawableByID(int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getDrawable(id);
    }
    public static String getStringByID(int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getString(id);
    }
    public static boolean getBooleanById(int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getBoolean(id);
    }
    public static int getIntegerByID(int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getInteger(id);
    }
}

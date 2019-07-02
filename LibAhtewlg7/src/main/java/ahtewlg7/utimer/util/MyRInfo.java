package ahtewlg7.utimer.util;


import android.graphics.drawable.Drawable;

import androidx.annotation.BoolRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.Utils;


/**
 * Created by 10139980 on 2016/11/15.
 */


public class MyRInfo {
    private static final String TAG = MyRInfo.class.getSimpleName();
    
	private MyRInfo() {
	}
    
    public static Drawable getDrawableByID(@DrawableRes int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getDrawable(id);
    }
    public static String getStringByID(@StringRes int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getString(id);
    }
    public static boolean getBooleanById(@BoolRes int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getBoolean(id);
    }
    public static int getIntegerByID(@IntegerRes int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getInteger(id);
    }

    public static int getColorByID(@ColorRes int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getColor(id,null);
    }
}

package ahtewlg7.utimer.util;


import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import androidx.annotation.BoolRes;
import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntegerRes;
import androidx.annotation.StringRes;

import com.blankj.utilcode.util.Utils;


public class MyRInfo {
	private MyRInfo() {
	}
    
    public static Drawable getDrawableByID(@DrawableRes int id) throws RuntimeException{
        return Utils.getApp().getApplicationContext().getResources().getDrawable(id);
    }
    public static Bitmap getBitmapById(@DrawableRes int id) throws RuntimeException{
        Resources res = Utils.getApp().getApplicationContext().getResources();
        return BitmapFactory.decodeResource(res, id);
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

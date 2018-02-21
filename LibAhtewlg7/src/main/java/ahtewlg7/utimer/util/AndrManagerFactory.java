package ahtewlg7.utimer.util;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.SearchManager;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.input.InputManager;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.blankj.utilcode.util.Utils;

/**
 * Created by lw on 2017/9/21.
 */


public class AndrManagerFactory {
	private static final String TAG = AndrManagerFactory.class.getSimpleName();
	
	public ActivityManager getActivityManager(){
        return (ActivityManager) Utils.getApp().getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
	}
	
	public WindowManager getWindowManager(){
        return (WindowManager) Utils.getApp().getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }
	public PackageManager getPackageManager(){
		return (PackageManager)Utils.getApp().getApplicationContext().getPackageManager();
	}
	
	public TelephonyManager getTelephonyManager(){
		return (TelephonyManager)Utils.getApp().getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
	}
	
	public ConnectivityManager getConnectivityManager(){
		return (ConnectivityManager) Utils.getApp().getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	public WifiManager getWifiManager(){
		return (WifiManager) Utils.getApp().getApplicationContext().getSystemService(Context.WIFI_SERVICE);
	}
	
	public NotificationManager getNotificationManager(){
		return (NotificationManager)Utils.getApp().getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	public InputMethodManager getInputMethodManager(){
		return (InputMethodManager)Utils.getApp().getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
	}
	
	public LayoutInflater getLayoutInflater(){
		return (LayoutInflater)Utils.getApp().getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public AudioManager getAudioManager(){
		return (AudioManager)Utils.getApp().getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
	}
	
	public SearchManager getSearchMananger(){
		return (SearchManager)Utils.getApp().getApplicationContext().getSystemService(Context.SEARCH_SERVICE);
	}
	
	public ClipboardManager getClipboardManager(){
		return (ClipboardManager)Utils.getApp().getApplicationContext().getSystemService(Context.CLIPBOARD_SERVICE);
	}

	public AlarmManager getAlarmManager(){
		return (AlarmManager)Utils.getApp().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
	}

	public InputManager getInputManager(){
        return (InputManager)Utils.getApp().getApplicationContext().getSystemService(Context.INPUT_SERVICE);
	}
}

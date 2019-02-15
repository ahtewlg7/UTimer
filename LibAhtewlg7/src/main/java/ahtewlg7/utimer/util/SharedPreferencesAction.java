package ahtewlg7.utimer.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.blankj.utilcode.util.Utils;
import com.google.common.base.Optional;

import ahtewlg7.utimer.verctrl.BaseConfig;
import ahtewlg7.utimer.verctrl.VcFactoryBuilder;


public class SharedPreferencesAction{
    private static final String TAG = SharedPreferencesAction.class.getSimpleName();

    private String preferenceName                    = null;
    private SharedPreferences sharedPreferences      = null;
    private SharedPreferences.Editor editor          = null;

    public SharedPreferencesAction() {
       Optional baseConfigOptional = VcFactoryBuilder.getInstance().getVcConfig();
       if(baseConfigOptional.isPresent()) {
           preferenceName = ((BaseConfig)baseConfigOptional.get()).getSimpleAppName();
       }else{
           preferenceName  = new BaseConfig().getSimpleAppName();
       }
        sharedPreferences = getLocalSharedPreferences();
        editor = getSharedPreferencesEditor(sharedPreferences);
    }
    
    public SharedPreferencesAction(String preferenceName) {
    	this.preferenceName = preferenceName;
        sharedPreferences = getLocalSharedPreferences();
        editor = getSharedPreferencesEditor(sharedPreferences);
    }

    //++++++++++++++++++++++++++++++(String,int)++++++++++++++++++++++++++++++++++++++++++
    public int getIntValue(String key , int defaultValue){
    	return sharedPreferences.getInt(key, defaultValue);
    }
    public void setKeyByInt(String key, int value){
        editor.putInt(key,value);
    }
    //++++++++++++++++++++++++++++++(String,boolean)++++++++++++++++++++++++++++++++++++++++++
    public boolean getBooleanValue(String key , boolean defaultValue){
    	return sharedPreferences.getBoolean(key, defaultValue);
    }
    public void setKeyByBoolean(String key, boolean value){
        editor.putBoolean(key,value);
    }
    //++++++++++++++++++++++++++++++(String,String)+++++++++++++++++++++++++++++++++++++++++++
    public String getStringValue(String key, String defaultValue){
    	return sharedPreferences.getString(key,defaultValue);
    }
    public void setKeyByString(String key, String value){
        editor.putString(key,value);
    }
    //++++++++++++++++++++++++++++++++commit++++++++++++++++++++++++++++++++++++++++++++++++++
    public void commitPreference(){
        editor.commit();
    }
    
    
    private SharedPreferences getLocalSharedPreferences(){
        return Utils.getApp().getSharedPreferences(preferenceName, Context.MODE_PRIVATE);
    }
    private SharedPreferences.Editor getSharedPreferencesEditor(SharedPreferences sharedPreferences){
        return sharedPreferences.edit();
    }
}

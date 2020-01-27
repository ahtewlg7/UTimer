package ahtewlg7.utimer.util;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.Utils;
import com.tencent.mmkv.MMKV;

public class MmkvAction {
    private MMKV mmkv;
    private boolean isInited = false;

    private MmkvAction(){
        initMmkv();
        mmkv = MMKV.defaultMMKV();
    }

    public static MmkvAction getInstance(){
        return Builder.instance;
    }

    public boolean isInited() {
        return isInited;
    }

    public void initMmkv(){
        if(isInited)
            return;
        String rootDir = MMKV.initialize(Utils.getApp());
        if(!TextUtils.isEmpty(rootDir))
            isInited = true;
    }
    public long getLong(String key){
        return mmkv.getLong(key,0);
    }
    public void putValue(String key, long value){
        mmkv.putLong(key, value);
    }

    public boolean getBoolean(String key){
        return mmkv.getBoolean(key,false);
    }
    public void putValue(String key, boolean value){
        mmkv.putBoolean(key, value);
    }

    public String getString(String key){
        return mmkv.getString(key,"");
    }

    public <T> T getObject(String key, Class<T> tClass){
        String tmp = getString(key);
        if(TextUtils.isEmpty(tmp))
            return null;
        return JSON.parseObject(tmp,tClass);
    }

    public void putObject(String key, Object obj){
        putValue(key, JSON.toJSONString(obj));
    }

    public void putValue(String key, String value){
        mmkv.putString(key, value);
    }

    private static class Builder{
        private static final MmkvAction instance = new MmkvAction();
    }
}

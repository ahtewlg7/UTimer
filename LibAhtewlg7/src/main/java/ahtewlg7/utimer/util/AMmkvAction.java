package ahtewlg7.utimer.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.Utils;
import com.google.common.base.Optional;
import com.tencent.mmkv.MMKV;
public abstract class AMmkvAction {
    protected static boolean isInited = false;

    protected abstract @NonNull MMKV createMmkv();

    protected MMKV mmkv;

    protected AMmkvAction(){
        initMmkv();
        mmkv        = createMmkv();
    }

    public boolean isInited() {
        return isInited;
    }

    public void initMmkv(){
        if(isInited)
            return;
        String rootDir = MMKV.initialize(Utils.getApp());
        if(!TextUtils.isEmpty(rootDir))
            isInited    = true;
    }

    public Optional<String> getId(){
        return isInited ? Optional.of(mmkv.mmapID()) : Optional.absent();
    }

    public Optional<Boolean> getBoolean(String key){
        return isInited ? Optional.of(mmkv.getBoolean(key,false)) : Optional.absent();
    }
    public boolean putValue(String key, boolean value){
        if(!isInited)
            return false;
        mmkv.putBoolean(key, value);
        return true;
    }

    public Optional<String> getString(String key){
        return isInited ? Optional.of(mmkv.getString(key,"")) : Optional.absent();
    }

    public <T> Optional<T> getObject(String key, Class<T> tClass){
        if(!isInited)
            return Optional.absent();
        Optional<String> tmp = getString(key);
        if(tmp.isPresent() && TextUtils.isEmpty(tmp.get()))
            return Optional.absent();
        return Optional.of(JSON.parseObject(tmp.get(),tClass));
    }

    public boolean putObject(String key, Object obj){
        if(!isInited)
            return false;
        return putValue(key, JSON.toJSONString(obj));
    }

    public boolean putValue(String key, String value){
        if(!isInited)
            return false;
        mmkv.putString(key, value);
        return true;
    }
}

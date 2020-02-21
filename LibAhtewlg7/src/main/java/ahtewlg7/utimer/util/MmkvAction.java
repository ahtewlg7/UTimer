package ahtewlg7.utimer.util;

import android.os.Parcelable;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.blankj.utilcode.util.Utils;
import com.tencent.mmkv.MMKV;

import java.util.Set;


public class MmkvAction {
    protected MMKV mmkv;
    protected boolean isInited = false;

    public MmkvAction(){
        initMmkv();
        toMakeDb(null, -1);
    }
    public MmkvAction(String mmId){
        initMmkv();
        toMakeDb(mmId, -1);
    }
    public MmkvAction(String mmId,int mode){
        initMmkv();
        toMakeDb(mmId,mode);
    }

    public boolean isInited() {
        return isInited;
    }

    public boolean ifContain(String key){
        return mmkv.contains(key);
    }

    public long decodeLong(String key){
        return mmkv.decodeLong(key,0);
    }
    public boolean encodeLong(String key, long value){
        return mmkv.encode(key, value);
    }

    public boolean decodeBoolean(String key){
        return mmkv.decodeBool(key,false);
    }
    public boolean encodeBoolean(String key, boolean value){
        return mmkv.encode(key, value);
    }

    public boolean encodeString(String key, String value){
        return mmkv.encode(key, value);
    }
    public String decodeString(String key){
        return mmkv.decodeString(key,"");
    }

    public boolean encodeStringSet(String key, Set<String> value){
        return mmkv.encode(key, value);
    }
    public Set<String> decodeStringSet(String key){
        return mmkv.decodeStringSet(key,null);
    }

    public <T> T decodeObjectByJson(String key, Class<T> tClass){
        String tmp = decodeString(key);
        if(TextUtils.isEmpty(tmp))
            return null;
        return JSON.parseObject(tmp,tClass);
    }
    public boolean  encodeObjectByJson(String key, Object obj){
        return mmkv.encode(key, JSON.toJSONString(obj));
    }

    public <T extends Parcelable> T decodeObj(String key, Class<T> tClass){
        return mmkv.decodeParcelable(key, tClass);
    }
    public boolean encodeObj(String key, Parcelable obj){
        return mmkv.encode(key, obj);
    }

    public void remove(String key){
        mmkv.removeValueForKey(key);
    }

    protected void initMmkv(){
        if(isInited)
            return;
        String rootDir = MMKV.initialize(Utils.getApp());
        if(!TextUtils.isEmpty(rootDir))
            isInited = true;
    }

    protected void toMakeDb(String mmId,int mode){
        if(TextUtils.isEmpty(mmId))
            mmkv = MMKV.defaultMMKV();
        else if(mode != MMKV.MULTI_PROCESS_MODE && mode != MMKV.SINGLE_PROCESS_MODE)
            mmkv = MMKV.mmkvWithID(mmId);
        else
            mmkv = MMKV.mmkvWithID(mmId, mode);
    }
}

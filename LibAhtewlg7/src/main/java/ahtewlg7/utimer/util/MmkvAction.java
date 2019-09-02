package ahtewlg7.utimer.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.tencent.mmkv.MMKV;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.enumtype.MmkvId;

public class MmkvAction {
    public static boolean isInited = false;

    private MMKV mmkv;
    private MmkvId mmkvId;

    public MmkvAction(@NonNull MmkvId mmkvId){
        this.mmkvId = mmkvId;
        createMmkv();
    }

    public static void initMmkv(){
        String rootDir = MMKV.initialize(new FileSystemAction().getDbDataAbsPath());
        if(!TextUtils.isEmpty(rootDir))
            isInited = true;
    }

    public void putValue(){
    }

    private void createMmkv(){
        switch (mmkvId){
            case DEFAULT:
            default:
                mmkv = MMKV.defaultMMKV();
                break;
        }
    }
}

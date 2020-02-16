package com.utimer.common;


import android.Manifest;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.blankj.utilcode.util.ServiceUtils;
import com.utimer.ui.BaseBinderService;
import com.utimer.verctrl.VersionControlFactory;

import ahtewlg7.utimer.common.MyApplication;
import ahtewlg7.utimer.verctrl.IBaseVersionControlFactory;

/**
 * Created by lw on 2017/4/2.
 */

public class UTimerApplication extends MyApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        getFragmentationBuilder().install();
    }

    @Override
    protected void toStartBinderService(){
        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            && ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
        ServiceUtils.startService(BaseBinderService.class);
    }

    @NonNull
    @Override
    public IBaseVersionControlFactory getConfigFactory() {
        return new VersionControlFactory();
    }
}

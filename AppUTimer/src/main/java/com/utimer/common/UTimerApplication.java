package com.utimer.common;


import android.support.annotation.NonNull;

import com.utimer.verctrl.VersionControlFactory;

import ahtewlg7.utimer.common.MyApplication;
import ahtewlg7.utimer.verctrl.IBaseVersionControlFactory;

/**
 * Created by lw on 2017/4/2.
 */

public class UTimerApplication extends MyApplication {
    public static final String TAG = UTimerApplication.class.getSimpleName();

    @Override
    public void onCreate() {
        super.onCreate();
        getFragmentationBuilder().install();
    }
    @NonNull
    @Override
    public IBaseVersionControlFactory getConfigFactory() {
        return new VersionControlFactory();
    }
}

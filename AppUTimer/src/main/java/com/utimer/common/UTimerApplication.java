package com.utimer.common;


import androidx.annotation.NonNull;

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
        ServiceUtils.startService(BaseBinderService.class);
    }

    @NonNull
    @Override
    public IBaseVersionControlFactory getConfigFactory() {
        return new VersionControlFactory();
    }
}

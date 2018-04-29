package com.utimer.verctrl;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.verctrl.BaseConfig;
import ahtewlg7.utimer.verctrl.IBaseVersionControlFactory;

/**
 * Created by lw on 2017/12/27.
 */

public class VersionControlFactory implements IBaseVersionControlFactory {
    public static final String TAG = VersionControlFactory.class.getSimpleName();

    @NonNull
    @Override
    public BaseConfig getBaseConfig() {
        return new ConfigFactory();
    }
}
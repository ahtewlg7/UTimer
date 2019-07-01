package com.utimer.verctrl;

import androidx.annotation.NonNull;

import ahtewlg7.utimer.verctrl.BaseConfig;
import ahtewlg7.utimer.verctrl.IBaseVersionControlFactory;

/**
 * Created by lw on 2017/12/27.
 */

public class VersionControlFactory implements IBaseVersionControlFactory {

    @NonNull
    @Override
    public BaseConfig getBaseConfig() {
        return new VcConfig();
    }
}
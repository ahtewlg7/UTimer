package com.utimer.verctrl;

import com.utimer.common.ShareXmlAction;

import ahtewlg7.utimer.verctrl.BaseConfig;

/**
 * Created by lw on 2017/12/27.
 */

public class VcConfig extends BaseConfig {

    @Override
    public boolean ifMdEditToastable() {
        ShareXmlAction shareXmlAction = new ShareXmlAction();
        return shareXmlAction.getMdEditToastable();
    }
}

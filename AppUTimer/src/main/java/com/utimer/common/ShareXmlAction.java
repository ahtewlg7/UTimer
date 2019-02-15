package com.utimer.common;

import com.utimer.R;

import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.util.SharedPreferencesAction;

/**
 * Created by lw on 2019/2/15.
 */
public class ShareXmlAction {
    private SharedPreferencesAction xmlAction;

    public ShareXmlAction() {
        xmlAction = new SharedPreferencesAction();
    }

    public boolean getMdEditToastable(){
        return xmlAction.getBooleanValue(MyRInfo.getStringByID(R.string.xml_mdEditToastable), true);
    }
}

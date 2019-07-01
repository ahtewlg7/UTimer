package com.utimer.entity;

import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;

import com.flyco.tablayout.listener.CustomTabEntity;

import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/6/30.
 */
public class TabLayoutEntity implements CustomTabEntity {
    private @StringRes int titleRid;
    private @DrawableRes int selectedIconRid;
    private @DrawableRes int unselectedIconRid;

    public TabLayoutEntity(@StringRes int titleRid, @DrawableRes int selectedIconRid, @DrawableRes int unselectedIconRid) {
        this.titleRid = titleRid;
        this.selectedIconRid = selectedIconRid;
        this.unselectedIconRid = unselectedIconRid;
    }

    @Override
    public String getTabTitle() {
        return MyRInfo.getStringByID(titleRid);
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIconRid;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unselectedIconRid;
    }
}

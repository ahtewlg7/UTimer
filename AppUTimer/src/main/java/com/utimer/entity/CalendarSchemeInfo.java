package com.utimer.entity;

import com.utimer.R;

import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/7/20.
 */
public class CalendarSchemeInfo {
    public static final int INVALID_PROGRESS = -1;

    private int progress =  INVALID_PROGRESS;
    private String tip   = MyRInfo.getStringByID(R.string.title_calendar_scheme);

    public CalendarSchemeInfo() {
        this.tip = tip;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getProgress() {
        return progress;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public String getTip() {
        return tip;
    }
}

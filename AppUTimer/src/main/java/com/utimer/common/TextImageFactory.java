package com.utimer.common;

import com.amulyakhare.textdrawable.TextDrawable;
import com.utimer.R;

import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2019/3/12.
 */
public class TextImageFactory {
    private TextImageFactory(){}

    public static TextImageFactory getInstance() {
        return TextImageHolder.INSTANCE;
    }

    public TextDrawable getGtdActionImage(){
        return TextDrawable.builder().buildRect("A", MyRInfo.getColorByID(R.color.colorPrimary));
    }
    public TextDrawable getGtdAllActionImage(){
        return TextDrawable.builder().buildRect("ALL", MyRInfo.getColorByID(R.color.colorPrimary));
    }
    public TextDrawable getGtdMaybeActionImage(){
        return TextDrawable.builder().buildRect("M", MyRInfo.getColorByID(R.color.colorPrimary));
    }
    public TextDrawable getActionInboxButtonImage(){
        return TextDrawable.builder().buildRect("Inbox", MyRInfo.getColorByID(R.color.colorPrimary));
    }
    public TextDrawable getGtdTodoDeedImage(){
        return TextDrawable.builder().buildRect("T", MyRInfo.getColorByID(R.color.colorPrimary));
    }
    public TextDrawable getGtdDoneDeedImage(){
        return TextDrawable.builder().buildRect("D", MyRInfo.getColorByID(R.color.colorPrimary));
    }

    public TextDrawable getShortHandImage(){
        return TextDrawable.builder().buildRect("S", MyRInfo.getColorByID(R.color.colorPrimary));
    }

    public TextDrawable getProjectImage(){
        return TextDrawable.builder().buildRect("P", MyRInfo.getColorByID(R.color.colorPrimary));
    }

    private static class TextImageHolder{
        private static final TextImageFactory INSTANCE = new TextImageFactory();
    }
}

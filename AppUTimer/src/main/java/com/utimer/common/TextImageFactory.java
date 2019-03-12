package com.utimer.common;

import android.graphics.Color;

import com.amulyakhare.textdrawable.TextDrawable;

/**
 * Created by lw on 2019/3/12.
 */
public class TextImageFactory {
    private static TextImageFactory instance;

    private TextImageFactory(){}

    public static TextImageFactory getInstance() {
        if(instance == null)
            instance = new TextImageFactory();
        return instance;
    }

    public TextDrawable getGtdActionImage(){
        return TextDrawable.builder().buildRect("A", Color.parseColor("#177bbd"));
    }

    public TextDrawable getShortHandImage(){
        return TextDrawable.builder().buildRect("S", Color.parseColor("#177bbd"));
    }

    public TextDrawable getProjectImage(){
        return TextDrawable.builder().buildRect("P", Color.parseColor("#177bbd"));
    }
}

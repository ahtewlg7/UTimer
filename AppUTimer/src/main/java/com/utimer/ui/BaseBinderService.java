package com.utimer.ui;

import android.app.Service;

import androidx.annotation.NonNull;

import com.utimer.mvp.BackgroundP;

import ahtewlg7.utimer.ui.BinderService;

/**
 * Created by lw on 2019/6/20.
 */
public class BaseBinderService extends BinderService implements BackgroundP.IBackgroundV {
    private BackgroundP p;

    @Override
    public void onCreate() {
        super.onCreate();

        p = new BackgroundP(this);
        p.toListenNewMedia();
        p.toStartNotify();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        p.toStopNotify();
    }

    @NonNull
    @Override
    public Service getAttchService() {
        return this;
    }
}

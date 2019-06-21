package com.utimer.ui;

import com.utimer.common.FastInboxNotifyAction;

import ahtewlg7.utimer.ui.BinderService;

/**
 * Created by lw on 2019/6/20.
 */
public class BaseBinderService extends BinderService {
    private FastInboxNotifyAction notifyAction;

    @Override
    public void onCreate() {
        super.onCreate();

        notifyAction = new FastInboxNotifyAction(this);
        notifyAction.toStartListen();
        notifyAction.toEnableFastInboxNotify();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        notifyAction.toStopListen();
        notifyAction.toDisableFastInboxNotify();
    }
}

package ahtewlg7.utimer.verctrl;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.ui.BinderService;

/**
 * Created by lw on 2017/2/6.
 */

public interface  IBaseVersionControlFactory {
    public @NonNull BaseConfig getBaseConfig();
    public @NonNull Class<? extends BinderService> getBinderServiceClass();
}

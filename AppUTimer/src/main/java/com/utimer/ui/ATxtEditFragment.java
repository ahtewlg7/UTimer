package com.utimer.ui;

import android.view.View;

/**
 * Created by lw on 2019/2/12.
 */
public abstract class ATxtEditFragment extends AEditFragment {

    protected abstract void toStartEdit();
    protected abstract void toEndEdit();

    @Override
    public void onStop() {
        super.onStop();
        if(ifEnvReady)
            toEndEdit();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(ifEnvReady && hidden)
            toEndEdit();
    }

    @Override
    protected void onEnvReady(View inflateView) {
        if(ifEnvReady)
            toStartEdit();
    }
}

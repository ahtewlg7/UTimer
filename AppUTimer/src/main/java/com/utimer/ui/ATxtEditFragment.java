package com.utimer.ui;

import android.view.KeyEvent;
import android.view.View;

/**
 * Created by lw on 2019/2/12.
 */
public abstract class ATxtEditFragment extends AEditFragment {

    protected abstract void toStartEdit();
    protected abstract void toPauseEdit();
    protected abstract void toEndEdit();
    protected abstract boolean ifTxtEditing();

    @Override
    public void onStop() {
        super.onStop();
        if(ifEnvReady)
            toEndEdit();
    }


    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && ifTxtEditing()) {
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && ifTxtEditing()) {
            toPauseEdit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

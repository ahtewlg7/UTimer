package com.utimer.ui;

import android.support.annotation.NonNull;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.utimer.R;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2018/11/8.
 */
public abstract class AEditFragment extends AToolbarBkFragment{
    public static final String TAG = AEditFragment.class.getSimpleName();

    protected boolean ifEnvReady;
    protected abstract @NonNull AUtimerEntity getUTimerEntity();
    protected abstract void onEnvReady(View inflateView);

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        if(!ifEnvOk()){
            Logcat.i(TAG,"the env is not ready , so pop it");
            ToastUtils.showShort(R.string.entity_invalid);
            pop();
            ifEnvReady = false;
            return;
        }
        ifEnvReady = true;
        onEnvReady(inflateView);
    }

    protected boolean ifEnvOk() {
        return getUTimerEntity().ifValid();
    }
}

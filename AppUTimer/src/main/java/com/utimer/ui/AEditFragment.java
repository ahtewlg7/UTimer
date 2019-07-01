package com.utimer.ui;

import androidx.annotation.NonNull;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.utimer.R;

import ahtewlg7.utimer.entity.AUtimerEntity;

/**
 * Created by lw on 2018/11/8.
 */
public abstract class AEditFragment extends AToolbarBkFragment{

    public static final String KEY_WORK_MODE        = "work";
    public static final int WORK_AS_NEW             = 1;
    public static final int WORK_AS_EDIT            = 2;

    protected boolean ifEnvReady;
    protected abstract @NonNull AUtimerEntity getUTimerEntity();
    protected abstract void onEnvReady(View inflateView);

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        if(!ifEnvOk()){
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

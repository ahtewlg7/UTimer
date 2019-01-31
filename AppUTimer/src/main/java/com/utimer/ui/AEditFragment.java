package com.utimer.ui;

import android.support.annotation.NonNull;
import android.view.View;

import com.blankj.utilcode.util.ToastUtils;
import com.utimer.R;

import ahtewlg7.utimer.mvp.IUtimerEditMvpP;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2018/11/8.
 */
public abstract class AEditFragment extends AToolbarBkFragment{
    public static final String TAG = AEditFragment.class.getSimpleName();

    protected abstract @NonNull IUtimerEditMvpP getEditMvpP();
    protected abstract boolean ifEnvOk();

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        if(!ifEnvOk()){
            Logcat.i(TAG,"the env is not ready , so pop it");
            ToastUtils.showShort(R.string.entity_invalid);
            pop();
            return;
        }

        getEditMvpP().toLoad();
    }

    @Override
    public void onStop() {
        super.onStop();
        onEditEnd();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(hidden)
            onEditEnd();
    }

    protected void onEditEnd(){
        getEditMvpP().toFinishEdit();
    }
}

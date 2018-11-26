package com.utimer.ui;

import android.os.Bundle;

import ahtewlg7.utimer.ui.BaseRxActivity;
import ahtewlg7.utimer.util.Logcat;
import butterknife.ButterKnife;

/**
 * Created by lw on 2018/10/17.
 */
public abstract class AButterKnifeActivity extends BaseRxActivity {
    public static final String TAG = AButterKnifeActivity.class.getSimpleName();

    protected abstract int getContentViewLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());

        Logcat.i(TAG, "onCreate");
        ButterKnife.bind(this);
    }

    @Override
    public AButterKnifeFragment getTopFragment() {
        return (AButterKnifeFragment) super.getTopFragment();
    }
}

package com.utimer.ui;

import android.os.Bundle;
import androidx.annotation.LayoutRes;

import ahtewlg7.utimer.ui.BaseBinderRxActivity;
import butterknife.ButterKnife;

/**
 * Created by lw on 2018/10/17.
 */
public abstract class AButterKnifeActivity extends BaseBinderRxActivity {
    protected abstract @LayoutRes int getContentViewLayout();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentViewLayout());

        ButterKnife.bind(this);
    }

    @Override
    public AButterKnifeFragment getTopFragment() {
        return (AButterKnifeFragment) super.getTopFragment();
    }
}

package com.utimer.ui;

import android.view.View;

import ahtewlg7.utimer.ui.ABaseRxFragement;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class AButterKnifeFragment extends ABaseRxFragement{

    protected abstract String getTitle();

    protected Unbinder unbinder;

    @Override
    public void onViewCreated(View inflateView) {
        unbinder  = ButterKnife.bind(this, inflateView);
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    @Override
    public AButterKnifeFragment getTopFragment() {
        return (AButterKnifeFragment)super.getTopFragment();
    }

    @Override
    public AButterKnifeFragment getTopChildFragment() {
        return (AButterKnifeFragment)super.getTopChildFragment();
    }
}

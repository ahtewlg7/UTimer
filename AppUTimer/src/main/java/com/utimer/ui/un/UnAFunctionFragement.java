package com.utimer.ui.un;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

import ahtewlg7.utimer.util.Logcat;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by lw on 2016/9/11.
 */
public abstract class UnAFunctionFragement extends RxFragment {
    public static final String TAG = UnAFunctionFragement.class.getSimpleName();

    public abstract void initLayoutView(View inflateView);
    public abstract @NonNull String getIndicateTitle();
    public abstract int getIndicateIconRid();
    public abstract int getLayoutRid();

    protected Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Logcat.i(TAG, "onCreateView");
        View view = inflater.inflate(getLayoutRid(), container, false);
        unbinder  = ButterKnife.bind(this, view);
        initLayoutView(view);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}

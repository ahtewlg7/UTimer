package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;


public abstract class AToolbarBkFragment extends AButterKnifeFragment{

    @NonNull
    protected abstract Toolbar getToolbar();
    protected abstract void initToolbar();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*false means menu off; true means menu on*/
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        getAttachActivity().getFloatingActionMenu().close(false);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        getAttachActivity().getFloatingActionMenu().close(false);
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);
        initToolbar();
        ((AppCompatActivity) getActivity()).setSupportActionBar(getToolbar());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if (getChildFragmentManager().getBackStackEntryCount() == 0 && getMenuRid() != 0) {
            getToolbar().getMenu().clear();
            getToolbar().inflateMenu(getMenuRid());
        }
    }

    @MenuRes
    protected int getMenuRid(){
        return 0;
    }

    public UTimerActivity getAttachActivity(){
        return (UTimerActivity)getActivity();
    }
}

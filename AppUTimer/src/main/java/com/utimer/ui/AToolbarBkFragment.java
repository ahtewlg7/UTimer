package com.utimer.ui;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

import androidx.annotation.MenuRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


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

        if(ifHomeButtonShowing()) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            ((AppCompatActivity) getActivity()).getSupportActionBar().setHomeButtonEnabled(true);
        }
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

    //Default: the "UP" button of toolbar (back to parent) is showed ;
    protected boolean ifHomeButtonShowing(){
        return true;
    }
}

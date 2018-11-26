package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.utimer.R;

import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class AboutFragment extends AToolbarBkFragment {
    public static final String TAG = AboutFragment.class.getSimpleName();

    @BindView(R.id.activity_utimer_toolbar)
    Toolbar toolbar;

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();

        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.un_fragment_recycler;
    }

    @Override
    protected int getMenuRid() {
        return R.menu.tool_menu;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_about);
    }

    @NonNull
    @Override
    protected Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    protected void initToolbar() {
        toolbar.setTitle(getTitle());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Logcat.i(TAG,"onOptionsItemSelected " + item.getTitle());
        return super.onOptionsItemSelected(item);
    }
}

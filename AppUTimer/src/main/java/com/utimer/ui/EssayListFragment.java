package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.utimer.R;

import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class EssayListFragment extends AToolbarBkFragment {
    public static final String TAG = EssayListFragment.class.getSimpleName();

    @BindView(R.id.fragment_shorthand_list_toolbar)
    Toolbar toolbar;

    public static EssayListFragment newInstance() {
        Bundle args = new Bundle();

        EssayListFragment fragment = new EssayListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_shorthand_list;
    }

    @Override
    protected int getMenuRid() {
        return R.menu.tool_menu;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_note_list);
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

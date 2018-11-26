package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.utimer.R;

import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class ShortHandListFragment extends AToolbarBkFragment {
    public static final String TAG = ShortHandListFragment.class.getSimpleName();

    @BindView(R.id.fragment_shorthand_list_toolbar)
    Toolbar toolbar;

    public static ShortHandListFragment newInstance() {
        Bundle args = new Bundle();

        ShortHandListFragment fragment = new ShortHandListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_shorthand_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_shorthand_list);
    }

    @Override
    protected int getMenuRid() {
        return R.menu.tool_menu;
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
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.tool_menu_add:
                Log.i(TAG, "to create shorthand");
                showHideFragment(ShortHandEditFragment.newInstance(null));
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }
}

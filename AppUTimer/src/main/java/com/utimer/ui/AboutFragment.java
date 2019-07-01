package com.utimer.ui;

import android.os.Bundle;
import androidx.annotation.Nullable;

import com.utimer.R;

import ahtewlg7.utimer.util.MyRInfo;

public class AboutFragment extends AButterKnifeFragment {
    public static final String TAG = AboutFragment.class.getSimpleName();

    public static AboutFragment newInstance() {
        Bundle args = new Bundle();

        AboutFragment fragment = new AboutFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*false means menu off; true means menu on*/
        setHasOptionsMenu(false);
    }

    @Override
    public int getLayoutRid() {
        return R.layout.layout_not_ready;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_about);
    }
}

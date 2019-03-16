package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.utimer.R;

import ahtewlg7.utimer.util.MyRInfo;

public class GtdFragment extends AButterKnifeFragment {
    public static final String TAG = GtdFragment.class.getSimpleName();

    public static GtdFragment newInstance() {
        Bundle args = new Bundle();

        GtdFragment fragment = new GtdFragment();
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
        return MyRInfo.getStringByID(R.string.title_note_list);
    }
}

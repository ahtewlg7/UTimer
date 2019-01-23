package com.utimer.ui;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.utimer.R;

import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class NotebookEditFragment extends AButterKnifeFragment {
    public static final String TAG = NotebookEditFragment.class.getSimpleName();

    @BindView(R.id.activity_utimer_toolbar)
    Toolbar toolbar;

    public static NotebookEditFragment newInstance() {
        Bundle args = new Bundle();

        NotebookEditFragment fragment = new NotebookEditFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.un_fragment_recycler;
    }


    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_note);
    }
}

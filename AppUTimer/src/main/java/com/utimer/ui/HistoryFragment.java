package com.utimer.ui;

import android.support.annotation.NonNull;

import com.utimer.R;
import com.utimer.view.recyclerview.GtdSectionRecylerView;

import butterknife.BindView;

/**
 * Created by lw on 2018/1/24.
 */

public class HistoryFragment extends AFunctionFragement {
    public static final String TAG = HistoryFragment.class.getSimpleName();

    @BindView(R.id.fragment_note_recyclerview)
    GtdSectionRecylerView sectionRecylerView;

    @Override
    @NonNull
    public String getIndicateTitle() {
        return "History";
    }

    @Override
    public int getIndicateIconRid() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_note;
    }
}

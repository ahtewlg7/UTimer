package com.utimer.ui;

import android.support.annotation.NonNull;
import android.view.View;

import com.utimer.R;

/**
 * Created by lw on 2018/1/24.
 */

public class UnHistoryFragment extends UnAFunctionFragement {
    public static final String TAG = UnHistoryFragment.class.getSimpleName();

    /*@BindView(R.id.fragment_note_recyclerview)
    GtdBaseSectionRecyclerView sectionRecylerView;*/

    @Override
    @NonNull
    public String getIndicateTitle() {
        return "History";
    }

    @Override
    public int getIndicateIconRid() {
        return R.drawable.page_indicator;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.un_fragment_recycler;
    }

    @Override
    public void initLayoutView(View inflateView) {

    }
}

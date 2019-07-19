package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.utimer.R;
import com.utimer.view.SimpleDeedRecyclerView;

import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static ahtewlg7.utimer.enumtype.DeedState.DONE;
import static ahtewlg7.utimer.enumtype.DeedState.TRASH;

public class DeedEndListFragment extends ADeedListFragment implements BaseDeedListMvpP.IBaseDeedMvpV {
    @BindView(R.id.fragment_deed_simple_list_recycler_view)
    SimpleDeedRecyclerView recyclerView;

    private DeedState[] workState;

    public static DeedEndListFragment newInstance() {
        Bundle args = new Bundle();

        DeedEndListFragment fragment = new DeedEndListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        workState = new DeedState[]{DONE, TRASH};
    }
    /**********************************************AToolbarBkFragment**********************************************/
    @Override
    public int getLayoutRid() {
        return R.layout.fragment_deed_simple_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_deed_list_end);
    }

    /**********************************************ADeedListFragment**********************************************/
    @NonNull
    @Override
    protected DeedState[] getLoadDeedState() {
        return workState;
    }

    @NonNull
    @Override
    protected SimpleDeedRecyclerView getRecyclerView() {
        return recyclerView;
    }
}

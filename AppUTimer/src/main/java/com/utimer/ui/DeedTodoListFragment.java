package com.utimer.ui;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.utimer.R;
import com.utimer.view.SimpleDeedRecyclerView;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.mvp.BaseDeedListMvpP;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static ahtewlg7.utimer.enumtype.DeedState.DEFER;
import static ahtewlg7.utimer.enumtype.DeedState.DELEGATE;
import static ahtewlg7.utimer.enumtype.DeedState.INBOX;
import static ahtewlg7.utimer.enumtype.DeedState.MAYBE;
import static ahtewlg7.utimer.enumtype.DeedState.ONE_QUARTER;
import static ahtewlg7.utimer.enumtype.DeedState.SCHEDULE;

public class DeedTodoListFragment extends ADeedListFragment implements BaseDeedListMvpP.IBaseDeedMvpV {
    @BindView(R.id.fragment_deed_simple_list_recycler_view)
    SimpleDeedRecyclerView recyclerView;

    private DeedState[] workState;

    public static DeedTodoListFragment newInstance() {
        Bundle args = new Bundle();

        DeedTodoListFragment fragment = new DeedTodoListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        workState       = new DeedState[]{SCHEDULE, INBOX, MAYBE, ONE_QUARTER, DEFER, DELEGATE};
    }
    /**********************************************AToolbarBkFragment**********************************************/
    @Override
    public int getLayoutRid() {
        return R.layout.fragment_deed_simple_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_deed_list_todo);
    }
    /**********************************************ADeedListFragment**********************************************/
    @NonNull
    @Override
    protected DeedState[] getLoadDeedState() {
        return workState;
    }

    @NonNull
    @Override
    protected BaseDeedListMvpP getDeedMvpP() {
        return new BaseDeedListMvpP(this);
    }

    @NonNull
    @Override
    protected SimpleDeedRecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected void onDeedClick(int position) {
        GtdDeedEntity deedEntity = (GtdDeedEntity)recyclerView.getAdapter().getItem(position);
        if(deedEntity != null)
            toCreateEditDialog(deedEntity);
    }
}

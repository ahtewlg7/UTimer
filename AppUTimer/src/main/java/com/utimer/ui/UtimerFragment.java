package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.jakewharton.rxbinding2.view.RxView;
import com.utimer.R;

import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

public class UtimerFragment extends AToolbarBkFragment {
    public static final String TAG = UtimerFragment.class.getSimpleName();

    @BindView(R.id.activity_utimer_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_utimer_grd_main_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.fragment_utimer_grd_main_shorthand)
    Button shorthandBn;

    private Disposable shorthandDisposable;

    private RecyclerView.Adapter sampleAdapter;

    public static UtimerFragment newInstance() {
        Bundle args = new Bundle();

        UtimerFragment fragment = new UtimerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);
        recyclerView.setVisibility(View.GONE);

        /*itemAdapter = new RecyclerView.Adapter(context, entityList);
        itemAdapter.setOnItemClickListener(itemClickListener);
        itemAdapter.setOnItemChildClickListener(itemChildClickListener);
        itemAdapter.bindToRecyclerView(this);
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(itemAdapter);*/

        shorthandDisposable = RxView.clicks(shorthandBn).throttleFirst(3,TimeUnit.SECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(Object o) throws Exception {
                        ((MainFragment)getParentFragment()).start(ShortHandListFragment.newInstance());
                    }
                });
    }

    @Override
    protected int getMenuRid() {
        return R.menu.tool_menu;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_utimer_gtd_main;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_gtd);
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
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }
}

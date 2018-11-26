package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.utimer.R;

import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class MainFragment extends AButterKnifeFragment {
    public static final String TAG = MainFragment.class.getSimpleName();

    @BindView(R.id.fragment_main_fragment_container)
    FrameLayout fragmentContainerLayout;
    @BindView(R.id.fragment_main_navigation)
    BottomNavigationView navigationView;

    private int prePosition = 0;

    private AButterKnifeFragment[] fragments = new AButterKnifeFragment[4];
    private NavigationLinstener navigationLinstener;

    public static MainFragment newInstance() {
        Bundle args = new Bundle();

        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);
        navigationLinstener = new NavigationLinstener();
        navigationView.setOnNavigationItemSelectedListener(navigationLinstener);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        loadFragment();
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_main;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.app_name);
    }

    private void loadFragment(){
        AButterKnifeFragment firstFragment = findChildFragment(MsgFragment.class);
        if (firstFragment == null) {
            fragments[0] = MsgFragment.newInstance();
            fragments[1] = UtimerFragment.newInstance();
            fragments[2] = NotebookFragment.newInstance();
            fragments[3] = AboutFragment.newInstance();

            loadMultipleRootFragment(R.id.fragment_main_fragment_container, prePosition,
                    fragments[0],fragments[1],fragments[2],fragments[3]);
        } else {
            fragments[0] = firstFragment;
            fragments[1] = findChildFragment(UtimerFragment.class);
            fragments[2] = findChildFragment(NotebookFragment.class);
            fragments[3] = findChildFragment(AboutFragment.class);
        }
    }

    private class NavigationLinstener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean result = false;
            switch (item.getItemId()) {
                case R.id.navigation_msg:
                    showHideFragment(fragments[0], fragments[prePosition]);
                    prePosition = 0;
                    result = true;
                    break;
                case R.id.navigation_gtd:
                    showHideFragment(fragments[1], fragments[prePosition]);
                    prePosition = 1;
                    result = true;
                    break;
                case R.id.navigation_note:
                    showHideFragment(fragments[2], fragments[prePosition]);
                    prePosition = 2;
                    result = true;
                    break;
                case R.id.navigation_about:
                    showHideFragment(fragments[3], fragments[prePosition]);
                    prePosition = 3;
                    result = true;
                    break;
            }
            return result;
        }
    }
}

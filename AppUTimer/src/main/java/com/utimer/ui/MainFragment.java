package com.utimer.ui;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.utimer.R;

import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static com.utimer.common.Constants.REQ_NEW_FRAGMENT;
import static com.utimer.ui.MainFragment.MAIN_INDEX.DEED;
import static com.utimer.ui.MainFragment.MAIN_INDEX.NOTE;

public class MainFragment extends AButterKnifeFragment {
    public static final int REQ_NEW_SHORTHAND_FRAGMENT = REQ_NEW_FRAGMENT;
    public static final int REQ_NEW_DEED_FRAGMENT      = REQ_NEW_FRAGMENT + 1;

    @BindView(R.id.fragment_main_fragment_container)
    FrameLayout fragmentContainerLayout;
    @BindView(R.id.fragment_main_navigation)
    BottomNavigationView navigationView;

    private int prePosition = DEED.value;

    private AButterKnifeFragment[] fragments = new AButterKnifeFragment[1];
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
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
//            if (requestCode == REQ_NEW_SHORTHAND_FRAGMENT)
//                start(ShortHandListFragment.newInstance(),SINGLETASK);
//            else if(requestCode == REQ_NEW_DEED_FRAGMENT)
//                start(DeedTodoListFragment.newInstance(),SINGLETASK);
        }
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_main;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.app_name);
    }

    /*public void toNewShortHand(){
        startForResult(ShortHandEditFragment.newInstance(null), REQ_NEW_SHORTHAND_FRAGMENT);
    }
    public void toNewDeed(){
        startForResult(DeedEditFragment.newInstance(null), REQ_NEW_DEED_FRAGMENT);
    }*/
    private void loadFragment(){
        AButterKnifeFragment firstFragment = findChildFragment(MsgFragment.class);
        if (firstFragment == null) {
            fragments[DEED.value()]  = DeedsFragment.newInstance();
//            fragments[NOTE.value()]  = NoteFragment.newInstance();
//            fragments[MSG.value()]   = MsgFragment.newInstance();
//            fragments[ABOUT.value()] = AboutFragment.newInstance();

            loadMultipleRootFragment(R.id.fragment_main_fragment_container, prePosition, fragments);
        } else {
            fragments[DEED.value()]  = findChildFragment(DeedsFragment.class);
//            fragments[NOTE.value()]  = findChildFragment(NoteFragment.class);
//            fragments[MSG.value()]   = firstFragment;
//            fragments[ABOUT.value()] = findChildFragment(AboutFragment.class);
        }
    }

    private class NavigationLinstener implements BottomNavigationView.OnNavigationItemSelectedListener {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            boolean result = false;
            switch (item.getItemId()) {
                case R.id.navigation_gtd:
                    showHideFragment(fragments[DEED.value()], fragments[prePosition]);
                    prePosition = DEED.value();
                    result = true;
                    break;
                case R.id.navigation_note:
                    showHideFragment(fragments[NOTE.value()], fragments[prePosition]);
                    prePosition = NOTE.value();
                    result = true;
                    break;
                /*case R.id.navigation_msg:
                    showHideFragment(fragments[MSG.value()], fragments[prePosition]);
                    prePosition = MSG.value();
                    result = true;
                    break;
                case R.id.navigation_about:
                    showHideFragment(fragments[ABOUT.value()], fragments[prePosition]);
                    prePosition = ABOUT.value();
                    result = true;
                    break;*/
            }
            return result;
        }
    }

    enum MAIN_INDEX{
        DEED(0),
        NOTE(1),
        MSG(2),
        ABOUT(3);

        private int value;
        MAIN_INDEX(int value) {
            this.value = value;
        }

        static MAIN_INDEX valueOf(int index){
            MAIN_INDEX tmp = null;
            switch (index){
                case 0:
                    tmp = DEED;
                    break;
                case 1:
                    tmp = NOTE;
                    break;
                case 2:
                    tmp = MSG;
                    break;
                case 3:
                    tmp = ABOUT;
                    break;
            }
            return tmp;
        }

        int value(){
            return value;
        }
    }
}

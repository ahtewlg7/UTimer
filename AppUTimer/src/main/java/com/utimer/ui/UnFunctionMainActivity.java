package com.utimer.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Window;

import com.trello.rxlifecycle2.components.support.RxFragmentActivity;
import com.utimer.R;
import com.viewpagerindicator.IconPagerAdapter;

import ahtewlg7.utimer.util.Logcat;
import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by lw on 2016/9/10.
 */
public class UnFunctionMainActivity extends RxFragmentActivity {
    public static final String TAG = UnFunctionMainActivity.class.getSimpleName();

    @BindView(R.id.activity_main_viewpager)
    ViewPager activityMainViewpager;
    @BindView(R.id.activity_main_indicator)
    com.viewpagerindicator.TabPageIndicator activityMainIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.un_activity_main);

        Logcat.i(TAG, "onCreate");
        ButterKnife.bind(this);

        FunctionPagerAdapter pagerAdapter = new FunctionPagerAdapter(getSupportFragmentManager());

        activityMainViewpager.setAdapter(pagerAdapter);
        activityMainIndicator.setViewPager(activityMainViewpager);

        activityMainIndicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                Logcat.d(TAG,"onPageSelected : " + arg0);
            }
            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                Logcat.d(TAG,"onPageScrolled : " + arg0);
            }
            @Override
            public void onPageScrollStateChanged(int arg0) {
                Logcat.d(TAG,"onPageScrollStateChanged : " + arg0);
            }
        });
    }

    class FunctionPagerAdapter extends FragmentPagerAdapter implements IconPagerAdapter {
        FragmentFactory fragmentFactory;

        public FunctionPagerAdapter(FragmentManager fragmentManager){
            super(fragmentManager);
            fragmentFactory = new FragmentFactory();
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentFactory.getFragment(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Logcat.d(TAG,"getPageTitle = " + ((UnAFunctionFragement)getItem(position)).getIndicateTitle());
            return ((UnAFunctionFragement)getItem(position)).getIndicateTitle();
        }

        @Override
        public int getCount() {
            Logcat.d(TAG,"getCount = " + fragmentFactory.getFragmentCount());
            return fragmentFactory.getFragmentCount();
        }

        @Override
        public int getIconResId(int position) {
            return ((UnAFunctionFragement)getItem(position)).getIndicateIconRid();
        }
    }

    class FragmentFactory {
        UnAFunctionFragement[] fragments;

        FragmentFactory(){
            fragments = new UnAFunctionFragement[getFragmentCount()];
        }

        int getFragmentCount(){
            return 3;
        }

        UnAFunctionFragement getFragment(int position){
            if(position < 0 || position >= getFragmentCount())
                return null;
            if(fragments[position] != null)
                return fragments[position];

            switch(position){
                case 0:
                    fragments[0] = new UnHistoryFragment();
                    break;
                case 1:
                    fragments[1] = new UnNoteFragment();
                    break;
                case 2:
                    fragments[2] = new UnGtdFragment();
                    break;
            }
            return fragments[position];
        }
    }
}

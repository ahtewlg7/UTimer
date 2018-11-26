package com.utimer.ui;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.utimer.R;

import butterknife.BindView;

public class UTimerActivity extends AButterKnifeActivity{
    public static final String TAG = UTimerActivity.class.getSimpleName();

    @BindView(R.id.activity_utimer_container)
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (findFragment(MainFragment.class) == null)
            loadRootFragment(R.id.activity_utimer_fragment_container, MainFragment.newInstance());
    }

    @Override
    protected int getContentViewLayout(){
        return R.layout.activity_utimer;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(getTopFragment().onKeyUp(keyCode, event))
            return true;
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(getTopFragment().onKeyDown(keyCode, event))
            return true;
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(getTopFragment().onTouchEvent(event))
            return true;
        return super.onTouchEvent(event);
    }
}

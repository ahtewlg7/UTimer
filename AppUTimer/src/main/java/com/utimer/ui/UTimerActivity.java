package com.utimer.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;

import com.tbruyelle.rxpermissions2.RxPermissions;
import com.utimer.R;

import ahtewlg7.utimer.common.LibContextInit;
import ahtewlg7.utimer.db.GreenDaoAction;
import ahtewlg7.utimer.util.MySimpleObserver;
import butterknife.BindView;

public class UTimerActivity extends AButterKnifeActivity{
    public static final String TAG = UTimerActivity.class.getSimpleName();

    @BindView(R.id.activity_utimer_container)
    ConstraintLayout constraintLayout;

    private RxPermissions rxPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxPermissions = new RxPermissions(this);
        initFileSystem();

        if (findFragment(MainFragment.class) == null)
            loadRootFragment(R.id.activity_utimer_fragment_container, MainFragment.newInstance());
    }

    @Override
    protected @LayoutRes int getContentViewLayout(){
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
    private void initFileSystem(){
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
//          .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySimpleObserver<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                    if(aBoolean) {
                        LibContextInit.initWorkingFileSystem();
                        GreenDaoAction.getInstance().init();
                    }
                }
            });
    }
}

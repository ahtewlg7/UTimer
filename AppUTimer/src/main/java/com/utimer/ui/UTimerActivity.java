package com.utimer.ui;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.constraint.ConstraintLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.utimer.R;

import ahtewlg7.utimer.entity.busevent.ActivityBusEvent;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.util.MySimpleObserver;
import butterknife.BindView;

public class UTimerActivity extends AButterKnifeActivity{
    @BindView(R.id.activity_utimer_container)
    ConstraintLayout constraintLayout;

    @BindView(R.id.activity_utimer_fragment_menu)
    FloatingActionMenu floatingActionMenu;
    @BindView(R.id.activity_utimer_fragment_bn_shorthand)
    FloatingActionButton shortHandActionButton;
    @BindView(R.id.activity_utimer_fragment_bn_deed)
    FloatingActionButton deedActionButton;

    private RxPermissions rxPermissions;
    private MenuButtonClickListener menuButtonClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxPermissions = new RxPermissions(this);
//        initFileSystem();
        initFloatMenu();

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
    protected void onStop() {
        super.onStop();
        EventBusFatory.getInstance().getDefaultEventBus().post(new ActivityBusEvent(ActivityEvent.STOP));
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

    public FloatingActionMenu getFloatingActionMenu(){
        return floatingActionMenu;
    }

    public FloatingActionButton getDeedActionButton(){
        return deedActionButton;
    }

    public void toVisibleFloatingMenu(boolean visible){
        floatingActionMenu.toggleMenuButton(visible);
    }

    private void initFloatMenu(){
        menuButtonClickListener = new MenuButtonClickListener();
        shortHandActionButton.setOnClickListener(menuButtonClickListener);
        deedActionButton.setOnClickListener(menuButtonClickListener);

        floatingActionMenu.setClosedOnTouchOutside(true);
        /*floatingActionMenu.setOnMenuToggleListener(new FloatingActionMenu.OnMenuToggleListener() {
            @Override
            public void onMenuToggle(boolean opened) {

            }
        });*/
    }

    class MenuButtonClickListener implements  View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_utimer_fragment_bn_shorthand:
                    if(findFragment(MainFragment.class) != null)
                        findFragment(MainFragment.class).toNewShortHand();
                    break;
                case R.id.activity_utimer_fragment_bn_deed:
                    if(findFragment(MainFragment.class) != null)
                        findFragment(MainFragment.class).toNewDeed();
                    break;
            }
        }
    }

    private void initFileSystem(){
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
//          .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new MySimpleObserver<Boolean>(){
                @Override
                public void onNext(Boolean aBoolean) {
                    super.onNext(aBoolean);
                    /*if(aBoolean) {
                        LibContextInit.initWorkingFileSystem();
                        GreenDaoAction.getInstance().init();
                    }*/
                }
            });
    }
}

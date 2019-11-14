package com.utimer.ui;

import android.Manifest;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle3.android.ActivityEvent;
import com.utimer.R;

import ahtewlg7.utimer.common.LibContextInit;
import ahtewlg7.utimer.db.GreenDaoAction;
import ahtewlg7.utimer.entity.busevent.ActivityBusEvent;
import ahtewlg7.utimer.factory.EventBusFatory;
import ahtewlg7.utimer.state.GtdMachine;
import ahtewlg7.utimer.ui.BinderService;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.util.MySimpleObserver;
import butterknife.BindView;

public class UTimerActivity extends AButterKnifeActivity{
    @BindView(R.id.activity_utimer_container)
    ConstraintLayout constraintLayout;

    @BindView(R.id.activity_utimer_fragment_menu)
    FloatingActionMenu floatingActionMenu;
    @BindView(R.id.activity_utimer_fragment_bn_video)
    FloatingActionButton videoActionButton;
    @BindView(R.id.activity_utimer_fragment_bn_audio)
    FloatingActionButton audioActionButton;
    @BindView(R.id.activity_utimer_fragment_bn_picture)
    FloatingActionButton pictureActionButton;
    @BindView(R.id.activity_utimer_fragment_bn_deed)
    FloatingActionButton deedActionButton;

    private RxPermissions rxPermissions;
    private MenuButtonClickListener menuButtonClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rxPermissions  = new RxPermissions(this);
        initFileSystem();
        initFloatMenu();

        if (findFragment(MainFragment.class) == null)
            loadRootFragment(R.id.activity_utimer_fragment_container, DeedsFragment.newInstance());
//            loadRootFragment(R.id.activity_utimer_fragment_container, MainFragment.newInstance());//todo
    }

    @Override
    protected @LayoutRes int getContentViewLayout(){
        return R.layout.activity_utimer;
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBusFatory.getInstance().getDefaultEventBus().post(new ActivityBusEvent(ActivityEvent.STOP));
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

    @NonNull
    protected Class<? extends BinderService> getBinderServiceClass() {
        return BaseBinderService.class;
    }

    public FloatingActionMenu getFloatingActionMenu(){
        return floatingActionMenu;
    }

    public FloatingActionButton getDeedActionButton(){
        return deedActionButton;
    }

    public RxPermissions getRxPermissions(){
        return rxPermissions;
    }

    public void toShowFloatMenu(boolean show){
        if(!show)
            floatingActionMenu.close(false);
        else
            floatingActionMenu.open(false);
    }

    public void toVisibleFloatingMenu(boolean visible){
        floatingActionMenu.toggleMenuButton(visible);
    }

    private void initFloatMenu(){
        menuButtonClickListener = new MenuButtonClickListener();
        videoActionButton.setOnClickListener(menuButtonClickListener);
        audioActionButton.setOnClickListener(menuButtonClickListener);
        pictureActionButton.setOnClickListener(menuButtonClickListener);
        deedActionButton.setOnClickListener(menuButtonClickListener);

        floatingActionMenu.setClosedOnTouchOutside(true);
    }

    private void initFileSystem(){
        rxPermissions.request(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
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
    private void toCreateInboxDialog(){
        new MaterialDialog.Builder(UTimerActivity.this).title(R.string.create)
                .inputType(InputType.TYPE_CLASS_TEXT)
                .input(MyRInfo.getStringByID(R.string.prompt_inbox_msg), "", false, new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        //do nothing
                    }
                })
                .negativeText(R.string.no)
                .positiveText(R.string.yes)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        String title = dialog.getInputEditText().getText().toString();
                        GtdMachine.getInstance().getCurrState(null).toInbox(title, title);
                    }
                }).show();
    }

    class MenuButtonClickListener implements  View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.activity_utimer_fragment_bn_video:
                    break;
                case R.id.activity_utimer_fragment_bn_audio:
                    break;
                case R.id.activity_utimer_fragment_bn_picture:
                    /*if(findFragment(MainFragment.class) != null)
                        findFragment(MainFragment.class).toNewShortHand();*/
//                    PictureSelector.create(UTimerActivity.this).themeStyle(R.style.picture_default_style).openExternalPreview(position, selectList);
                    PictureSelector.create(UTimerActivity.this).openGallery(PictureMimeType.ofImage())
                            .isGif(true).enablePreviewAudio(true)
                            .forResult(PictureConfig.CHOOSE_REQUEST);
                    break;
                case R.id.activity_utimer_fragment_bn_deed:
                    toShowFloatMenu(false);
                    toCreateInboxDialog();
                    break;
            }
        }
    }
}

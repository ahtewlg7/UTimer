package com.utimer.ui;

import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ServiceUtils;
import com.utimer.R;

import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.ui.BaseRxActivity;
import ahtewlg7.utimer.util.Logcat;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observable;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2017/12/27.
 */

public class WelcomeActivity extends BaseRxActivity {
    public static final String TAG = WelcomeActivity.class.getSimpleName();

    @BindView(R.id.fullscreen_content)
    TextView fullscreenContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_welcome);

        Logcat.i(TAG,"onCreate");
        ButterKnife.bind(this);

        ServiceUtils.startService(BaseBinderService.class);
        toDelayFinish();
    }

    private void toDelayFinish(){
        Observable.timer(3, TimeUnit.SECONDS)
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        ActivityUtils.startActivity(UTimerActivity.class);
                        WelcomeActivity.this.finish();
                    }
                });
    }
}

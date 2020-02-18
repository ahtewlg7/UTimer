package com.utimer.ui;


import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.blankj.utilcode.util.ActivityUtils;
import com.utimer.R;
import com.utimer.mvp.WelcomeP;

import butterknife.BindView;

public class WelcomeActivity extends AButterKnifeActivity
        implements WelcomeP.IWelcomeV {
    @BindView(R.id.fullscreen_content_detail)
    TextView detailTv;

    private WelcomeP p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        p = new WelcomeP(this);
        p.initWorkContext();
    }

    @Override
    protected int getContentViewLayout() {
        return R.layout.activity_welcome;
    }

    @NonNull
    @Override
    public FragmentActivity getAttachAtivity() {
        return this;
    }

    @Override
    public void onPermissionRequest(boolean result) {
        if(result) {
            ActivityUtils.startActivity(UTimerActivity.class);
            finish();
        }else{
            detailTv.setVisibility(View.VISIBLE);
            detailTv.setText(R.string.prompt_permission_rw_deny);
        }
    }
}

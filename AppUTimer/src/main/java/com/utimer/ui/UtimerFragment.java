package com.utimer.ui;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.collect.Lists;
import com.utimer.R;
import com.utimer.view.UtimerFuncRecyclerView;

import java.util.List;

import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class UtimerFragment extends AToolbarBkFragment {
    public static final String TAG = UtimerFragment.class.getSimpleName();

    @BindView(R.id.fragment_utimer_main_func_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_utimer_main_func_recyclerview)
    UtimerFuncRecyclerView utimerFuncRecyclerView;

    private MyClickListener myClickListener;
    private List<UtimerFuncRecyclerView.FuncViewEntity> funcViewEntityList;

    public static UtimerFragment newInstance() {
        Bundle args = new Bundle();

        UtimerFragment fragment = new UtimerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        initFuncList();

        myClickListener    = new MyClickListener();
        utimerFuncRecyclerView.init(getContext(), funcViewEntityList, myClickListener, null,null,null,null,null);
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_utimer_main_func;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_gtd);
    }

    @Override
    protected int getMenuRid() {
        return 0;
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

    private void initFuncList(){
        funcViewEntityList = Lists.newArrayList();

        Drawable shortHandDrawable = TextDrawable.builder().buildRect("S",Color.parseColor("#177bbd"));
        String shortHandTitle      = getResources().getString(R.string.title_shorthand);
        UtimerFuncRecyclerView.FuncViewEntity shorthandViewEntity = new UtimerFuncRecyclerView.FuncViewEntity(shortHandDrawable, shortHandTitle);

        Drawable projectDrawable = TextDrawable.builder().buildRect("P",Color.parseColor("#177bbd"));
        String projectTitle      = getResources().getString(R.string.title_project);
        UtimerFuncRecyclerView.FuncViewEntity projectViewEntity = new UtimerFuncRecyclerView.FuncViewEntity(projectDrawable, projectTitle);

        funcViewEntityList.add(shorthandViewEntity);
        funcViewEntityList.add(projectViewEntity);
;    }
    class MyClickListener implements BaseQuickAdapter.OnItemClickListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            switch (position){
                case 0:
                    Logcat.i(TAG,"onItemClick : start ShortHand list");
                    ((MainFragment)getParentFragment()).start(ShortHandListFragment.newInstance());
                    break;
                case 1:
                    Logcat.i(TAG,"onItemClick : start Project list");
                    ((MainFragment)getParentFragment()).start(ProjectListFragment.newInstance());
                    break;
            }
        }
    }
}

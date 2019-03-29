package com.utimer.ui;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.collect.Lists;
import com.utimer.R;
import com.utimer.common.TextImageFactory;
import com.utimer.view.UtimerFuncRecyclerView;

import java.util.List;

import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class GtdFragment extends AToolbarBkFragment {

    @BindView(R.id.fragment_utimer_main_func_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_utimer_main_func_recyclerview)
    UtimerFuncRecyclerView utimerFuncRecyclerView;

    private MyClickListener myClickListener;
    private List<UtimerFuncRecyclerView.FuncViewEntity> funcViewEntityList;

    public static GtdFragment newInstance() {
        Bundle args = new Bundle();

        GtdFragment fragment = new GtdFragment();
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

        Drawable actionDrawable = TextImageFactory.getInstance().getGtdMaybeActionImage();
        String actionTitle      = getResources().getString(R.string.title_action_maybe);
        UtimerFuncRecyclerView.FuncViewEntity actionViewEntity = new UtimerFuncRecyclerView.FuncViewEntity(actionDrawable, actionTitle);

        funcViewEntityList.add(actionViewEntity);
;    }
    class MyClickListener implements BaseQuickAdapter.OnItemClickListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            switch (position){
                case 0:
                    ((MainFragment)getParentFragment()).start(GtdActionListFragment.newInstance());
                    break;
            }
        }
    }
}

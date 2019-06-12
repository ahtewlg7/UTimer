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

    @BindView(R.id.fragment_gtd_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_gtd_recyclerview)
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
        return R.layout.fragment_gtd;
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

    @Override
    protected boolean ifHomeButtonShowing() {
        return false;
    }

    private void initFuncList(){
        funcViewEntityList = Lists.newArrayList();

        Drawable maybeDrawable = TextImageFactory.getInstance().getGtdMaybeActionImage();
        String maybeTitle      = getResources().getString(R.string.title_deed_maybe);
        UtimerFuncRecyclerView.FuncViewEntity maybeViewEntity = new UtimerFuncRecyclerView.FuncViewEntity(maybeDrawable, maybeTitle);

        Drawable todoDrawable = TextImageFactory.getInstance().getGtdTodoDeedImage();
        String todoTitle      = getResources().getString(R.string.title_deed_todo);
        UtimerFuncRecyclerView.FuncViewEntity todoViewEntity = new UtimerFuncRecyclerView.FuncViewEntity(todoDrawable, todoTitle);

        Drawable doneDrawable = TextImageFactory.getInstance().getGtdDoneDeedImage();
        String doneTitle      = getResources().getString(R.string.title_deed_done);
        UtimerFuncRecyclerView.FuncViewEntity doneViewEntity = new UtimerFuncRecyclerView.FuncViewEntity(doneDrawable, doneTitle);

        funcViewEntityList.add(maybeViewEntity);
        funcViewEntityList.add(todoViewEntity);
        funcViewEntityList.add(doneViewEntity);
;    }
    class MyClickListener implements BaseQuickAdapter.OnItemClickListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            switch (position){
                case 0:
                    ((MainFragment)getParentFragment()).start(DeedMaybeListFragment.newInstance());
                    break;
                case 1:
                    ((MainFragment)getParentFragment()).start(DeedTodoListFragment.newInstance());
                    break;
                case 2:
                    ((MainFragment)getParentFragment()).start(DeedDoneListFragment.newInstance());
                    break;
            }
        }
    }
}

package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.common.collect.Lists;
import com.utimer.R;
import com.utimer.entity.TabLayoutEntity;

import java.util.ArrayList;

import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static com.utimer.common.Constants.REQ_NEW_FRAGMENT;
import static com.utimer.ui.DeedsFragment.DEED_INDEX.DONE;
import static com.utimer.ui.DeedsFragment.DEED_INDEX.INBOX;
import static com.utimer.ui.DeedsFragment.DEED_INDEX.MAYBE;
import static com.utimer.ui.DeedsFragment.DEED_INDEX.TODO;

public class DeedsFragment extends AToolbarBkFragment{
    @BindView(R.id.fragment_deeds_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_deeds_tabLayout)
    CommonTabLayout tabLayout;

    private int prePosition = 0;
    private ArrayList<CustomTabEntity> tabEntityList;
    private AButterKnifeFragment[] fragments = new AButterKnifeFragment[4];

    public static DeedsFragment newInstance() {
        Bundle args = new Bundle();

        DeedsFragment fragment = new DeedsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        initTabLayout();
        loadFragment();
//        EventBusFatory.getInstance().getDefaultEventBus().register(this);
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*false means menu off; true means menu on*/
        setHasOptionsMenu(false);
    }

    @Override
    public void onStop() {
        super.onStop();
//        EventBusFatory.getInstance().getDefaultEventBus().unregister(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    public void onNewBundle(Bundle args) {
        super.onNewBundle(args);
//        listMvpP.toLoadAllItem();
    }

    /**********************************************AToolbarBkFragment**********************************************/

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_deeds;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_deed);
    }

    @Override
    protected int getMenuRid() {
        return R.menu.tool_menu;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.tool_menu_add:
                startForResult(ShortHandEditFragment.newInstance(null), REQ_NEW_FRAGMENT);
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    private void initTabLayout(){
        if(tabEntityList == null)
            tabEntityList = Lists.newArrayList();
        tabEntityList.add(INBOX.value(),new TabLayoutEntity(R.string.title_deed_inbox, 0,0));
        tabEntityList.add(TODO.value(),new TabLayoutEntity(R.string.title_deed_todo, 0,0));
        tabEntityList.add(MAYBE.value(),new TabLayoutEntity(R.string.title_deed_maybe, 0,0));
        tabEntityList.add(DONE.value(),new TabLayoutEntity(R.string.title_deed_done, 0,0));

        tabLayout.setTabData(tabEntityList);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                showHideFragment(fragments[position], fragments[prePosition]);
                prePosition = position;
            }

            @Override
            public void onTabReselect(int position) {
                //do nothing
            }
        });
    }

    private void loadFragment(){
        AButterKnifeFragment firstFragment = findChildFragment(DeedMaybeListFragment.class);
        if (firstFragment == null) {
            fragments[INBOX.value()]  = GtdFragment.newInstance();
            fragments[TODO.value()]   = DeedTodoListFragment.newInstance();
            fragments[MAYBE.value()]  = DeedMaybeListFragment.newInstance();
            fragments[DONE.value()]   = DeedDoneListFragment.newInstance();

            loadMultipleRootFragment(R.id.fragment_deeds_fragment_container, prePosition,
                    fragments[INBOX.value()],
                    fragments[TODO.value()],
                    fragments[MAYBE.value()],
                    fragments[DONE.value()]);
        } else {
            fragments[INBOX.value()] = firstFragment;
            fragments[TODO.value()]  = findChildFragment(DeedTodoListFragment.class);
            fragments[MAYBE.value()] = findChildFragment(DeedMaybeListFragment.class);
            fragments[DONE.value()]  = findChildFragment(DeedDoneListFragment.class);
        }
    }
    enum DEED_INDEX{
        INBOX(0),
        TODO(1),
        MAYBE(2),
        DONE(3);

        private int value;
        DEED_INDEX(int value) {
            this.value = value;
        }

        static DEED_INDEX valueOf(int index){
            DEED_INDEX tmp = null;
            switch (index){
                case 0:
                    tmp = INBOX;
                    break;
                case 1:
                    tmp = TODO;
                    break;
                case 2:
                    tmp = MAYBE;
                    break;
                case 3:
                    tmp = DONE;
                    break;
            }
            return tmp;
        }

        int value(){
            return value;
        }
    }
}

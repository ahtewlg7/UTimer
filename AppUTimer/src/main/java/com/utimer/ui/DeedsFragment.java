package com.utimer.ui;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.google.common.collect.Lists;
import com.utimer.R;
import com.utimer.entity.TabLayoutEntity;

import java.util.ArrayList;

import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static com.utimer.ui.DeedsFragment.DEED_INDEX.I_END;
import static com.utimer.ui.DeedsFragment.DEED_INDEX.I_INBOX;
import static com.utimer.ui.DeedsFragment.DEED_INDEX.I_MARK;
import static com.utimer.ui.DeedsFragment.DEED_INDEX.I_REFERENCE;
import static com.utimer.ui.DeedsFragment.DEED_INDEX.I_SCHEDULE;
import static com.utimer.ui.DeedsFragment.DEED_INDEX.I_TODO;

public class DeedsFragment extends AToolbarBkFragment{
    @BindView(R.id.fragment_deeds_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_deeds_tabLayout)
    CommonTabLayout tabLayout;

    private int prePosition = 0;
    private ArrayList<CustomTabEntity> tabEntityList;
    private AButterKnifeFragment[] fragments = new AButterKnifeFragment[6];

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
        loadFragment();
    }

//    @Override
//    public void onNewBundle(Bundle args) {
//        super.onNewBundle(args);
////        listMvpP.toLoadAllItem();
//    }

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
    protected boolean ifHomeButtonShowing() {
        return false;//not to show the "UP" button of toolbar
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.tool_menu_add:
//                startForResult(ShortHandEditFragment.newInstance(null), REQ_NEW_FRAGMENT);
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && prePosition != I_SCHEDULE.value()){
            showHideFragment(fragments[I_SCHEDULE.value()], fragments[prePosition]);
            prePosition = I_SCHEDULE.value();
            tabLayout.setCurrentTab(prePosition);
            return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK && prePosition != I_SCHEDULE.value())
            return true;
        return super.onKeyDown(keyCode, event);
    }

    private void initTabLayout(){
        if(tabEntityList == null)
            tabEntityList = Lists.newArrayList();
        tabEntityList.clear();
        tabEntityList.add(I_SCHEDULE.value(), new TabLayoutEntity(R.string.title_deed_list_schedule, 0,0));
        tabEntityList.add(I_TODO.value(), new TabLayoutEntity(R.string.title_deed_list_todo, 0,0));
        tabEntityList.add(I_INBOX.value(), new TabLayoutEntity(R.string.title_deed_list_new, 0,0));
        tabEntityList.add(I_MARK.value(), new TabLayoutEntity(R.string.title_deed_list_mark, 0,0));
        tabEntityList.add(I_REFERENCE.value(), new TabLayoutEntity(R.string.title_deed_list_reference, 0,0));
        tabEntityList.add(I_END.value(),  new TabLayoutEntity(R.string.title_deed_list_end, 0,0));

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
        AButterKnifeFragment firstFragment = findChildFragment(DeedScheduleListFragment.class);
        if (firstFragment == null) {
            fragments[I_SCHEDULE.value()]         = DeedScheduleListFragment.newInstance();
            fragments[I_TODO.value()]             = DeedTodoListFragment.newInstance();
            fragments[I_INBOX.value()]            = DeedInboxListFragment.newInstance();
            fragments[I_MARK.value()]             = DeedMarkListFragment.newInstance();
            fragments[I_REFERENCE.value()]        = DeedReferenceListFragment.newInstance();
            fragments[I_END.value()]              = DeedEndListFragment.newInstance();

            loadMultipleRootFragment(R.id.fragment_deeds_fragment_container, prePosition,
                    fragments[I_SCHEDULE.value()], fragments[I_TODO.value()], fragments[I_INBOX.value()],
                    fragments[I_MARK.value()], fragments[I_REFERENCE.value()], fragments[I_END.value()]);
        } else {
            fragments[I_SCHEDULE.value()]     = firstFragment;
            fragments[I_TODO.value()]         = findChildFragment(DeedTodoListFragment.class);
            fragments[I_INBOX.value()]        = findChildFragment(DeedInboxListFragment.class);
            fragments[I_MARK.value()]         = findChildFragment(DeedMarkListFragment.class);
            fragments[I_REFERENCE.value()]    = findChildFragment(DeedReferenceListFragment.class);
            fragments[I_END.value()]          = findChildFragment(DeedEndListFragment.class);
        }
    }

    enum DEED_INDEX{
        I_SCHEDULE(0),
        I_TODO(1),
        I_INBOX(2),
        I_MARK(3),
        I_REFERENCE(4),
        I_END(5)
        /*,
        DEFER(3),
        DELEGATE(4),
        ONE_QUARTER(5),
        PROJECT(6),
        WISH(7),
        MAYBE(9),
        USELESS(10),
        TRASH(11),
        DONE(12)*/;

        private int value;
        DEED_INDEX(int value) {
            this.value = value;
        }

        static DEED_INDEX valueOf(int index){
            DEED_INDEX tmp = null;
            switch (index){
                case 0:
                    tmp = I_SCHEDULE;
                    break;
                case 1:
                    tmp = I_TODO ;
                    break;
                case 2:
                    tmp = I_INBOX;
                    break;
                case 3:
                    tmp = I_MARK;
                    break;
                case 4:
                    tmp = I_REFERENCE;
                    break;
                case 5:
                    tmp =  I_END;
                    break;
                /*case 4:
                    tmp = DELEGATE;
                    break;
                case 5:
                    tmp = ONE_QUARTER;
                    break;
                case 6:
                    tmp = PROJECT;
                    break;
                case 7:
                    tmp = WISH;
                    break;
                case 9:
                    tmp = MAYBE;
                    break;
                case 10:
                    tmp = USELESS;
                    break;
                case 11:
                    tmp = TRASH;
                    break;
                case 12:
                    tmp = DONE;
                    break;*/
            }
            return tmp;
        }

        int value(){
            return value;
        }
    }
}

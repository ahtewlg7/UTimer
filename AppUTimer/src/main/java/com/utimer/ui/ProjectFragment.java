package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.utimer.R;

import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.mvp.AUtimerEditMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class ProjectFragment extends AEditFragment {
    public static final String TAG = ProjectFragment.class.getSimpleName();

    public static final int REQ_NEW_NOTE_FRAGMENT   = 100;
    public static final int REQ_EDIT_NOTE_FRAGMENT  = 101;

    public static final String KEY_GTD_PROJECT = "project";

    protected GtdProjectEntity gtdProjectEntity;

    @BindView(R.id.activity_utimer_toolbar)
    Toolbar toolbar;

    public static ProjectFragment newInstance(GtdProjectEntity entity) {
        Bundle args = new Bundle();
        if(entity != null)
            args.putSerializable(KEY_GTD_PROJECT, entity);
        ProjectFragment fragment = new ProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.un_fragment_recycler;
    }

    @Override
    protected int getMenuRid() {
        return R.menu.project_menu;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_project);
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
        Logcat.i(TAG,"onOptionsItemSelected " + item.getTitle());
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.project_menu_add_note:
                Log.i(TAG, "to create new note");
                String now = new DateTimeAction().toFormatNow().toString();
                NoteEntity entity = (NoteEntity)new NoteBuilder().setProjectEntity(gtdProjectEntity).setTitle(now).build();
                startForResult(NoteEditFragment.newInstance(entity), REQ_NEW_NOTE_FRAGMENT);
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    protected AUtimerEditMvpP getEditMvpP() {
        return null;
    }

    @Override
    protected boolean ifEnvOk() {
        gtdProjectEntity = getGtdEntity();
        return gtdProjectEntity != null && gtdProjectEntity.ifValid() && gtdProjectEntity.ensureAttachFileExist();
    }

    private GtdProjectEntity getGtdEntity(){
        return (GtdProjectEntity) getArguments().getSerializable(KEY_GTD_PROJECT);
    }

    //todo: delete
    protected void onEditEnd(){
    }
}

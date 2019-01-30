package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.entity.gtd.NoteBuilder;
import ahtewlg7.utimer.entity.gtd.NoteEntity;
import ahtewlg7.utimer.mvp.IUtimerEditMvpP;
import ahtewlg7.utimer.mvp.ProjectEditMvpP;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

public class ProjectFragment extends AEditFragment
    implements ProjectEditMvpP.IProjectEditMvpV {
    public static final String TAG = ProjectFragment.class.getSimpleName();

    public static final int REQ_NEW_NOTE_FRAGMENT   = 100;
    public static final int REQ_EDIT_NOTE_FRAGMENT  = 101;

    public static final String KEY_GTD_PROJECT = "project";

    @BindView(R.id.activity_utimer_toolbar)
    Toolbar toolbar;

    protected GtdProjectEntity gtdProjectEntity;
    protected ProjectEditMvpP projectEditMvpP;

    public static ProjectFragment newInstance(GtdProjectEntity entity) {
        Bundle args = new Bundle();
        if(entity != null)
            args.putSerializable(KEY_GTD_PROJECT, entity);
        ProjectFragment fragment = new ProjectFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);


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

    /*++++++++++++++++++++++++++++++++++++++++++AToolbarBkFragment++++++++++++++++++++++++++++++++++++++++*/
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
    /*++++++++++++++++++++++++++++++++++++++++++ProjectEditMvpP.IProjectEditMvpV++++++++++++++++++++++++++++++++++++++++*/
    @Override
    public void resetView(List<NoteEntity> dataList) {

    }

    @Override
    public void resetView(int index, NoteEntity noteEntity) {

    }

    @Override
    public void onItemLoadStart() {

    }

    @Override
    public void onItemLoad(NoteEntity data) {

    }

    @Override
    public void onItemLoadErr(Throwable t) {

    }

    @Override
    public void onItemLoadEnd(List<NoteEntity> alldata) {

    }

    @Override
    public void onItemCreate(NoteEntity data) {

    }

    @Override
    public void onItemEdit(NoteEntity data) {

    }

    @Override
    public void onDeleteSucc(int index, NoteEntity noteEntity) {

    }

    @Override
    public void onDeleteFail(NoteEntity noteEntity) {

    }

    @Override
    public void onDeleteErr(Throwable throwable) {

    }

    @Override
    public void onDeleteEnd() {

    }

    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }
    /*++++++++++++++++++++++++++++++++++++++++++AEditFragment++++++++++++++++++++++++++++++++++++++++*/

    @NonNull
    @Override
    protected IUtimerEditMvpP getEditMvpP() {
        if(projectEditMvpP == null)
            projectEditMvpP = new ProjectEditMvpP(this, getGtdEntity());;
        return projectEditMvpP;
    }

    @Override
    protected boolean ifEnvOk() {
        gtdProjectEntity = getGtdEntity();
        return gtdProjectEntity != null && gtdProjectEntity.ifValid() && gtdProjectEntity.ensureAttachFileExist();
    }

    private GtdProjectEntity getGtdEntity(){
        return (GtdProjectEntity) getArguments().getSerializable(KEY_GTD_PROJECT);
    }
}

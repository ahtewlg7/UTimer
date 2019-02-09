package com.utimer.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.common.collect.Lists;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;
import com.utimer.entity.ProjectInfoSectionViewEntity;
import com.utimer.view.ProjectInfoSectionRecyclerView;

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

    public static final int INIT_POSITION           = -1;
    public static final int REQ_NEW_NOTE_FRAGMENT   = 100;
    public static final int REQ_EDIT_NOTE_FRAGMENT  = 101;

    public static final String KEY_GTD_PROJECT = "project";

    @BindView(R.id.fragment_project_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_project_info_rv)
    ProjectInfoSectionRecyclerView projectRecyclerView;

    private int editIndex = -1;
    protected GtdProjectEntity gtdProjectEntity;
    protected ProjectEditMvpP projectEditMvpP;
    protected MyClickListener myClickListener;

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

        myClickListener = new MyClickListener();
        projectRecyclerView.init(getContext(), 0, null, myClickListener, null, myClickListener,null);
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            NoteEntity entity = (NoteEntity) data.getSerializable(NoteEditFragment.KEY_NOTE);
            if (entity != null && requestCode == REQ_NEW_NOTE_FRAGMENT)
                onItemCreate(entity);
            else if (entity != null && requestCode == REQ_EDIT_NOTE_FRAGMENT) {
                onItemEdit(entity);
            }
        }
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_project;
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
        toReloadEntity(dataList);
    }

    @Override
    public void resetView(int index, NoteEntity noteEntity) {
        ProjectInfoSectionViewEntity projectInfoSectionViewEntity = new ProjectInfoSectionViewEntity(noteEntity);
        projectRecyclerView.resetData(index, projectInfoSectionViewEntity);
    }

    @Override
    public void onItemLoadStart() {
        Logcat.i(TAG, "onItemLoadStart");
    }

    @Override
    public void onItemLoad(NoteEntity data) {
        Logcat.i(TAG, "onItemLoad " + data.toString());

    }

    @Override
    public void onItemLoadErr(Throwable t) {
        Logcat.i(TAG, "onItemLoadErr : " + t.getMessage());
    }

    @Override
    public void onItemLoadEnd(List<NoteEntity> alldata) {
        toReloadEntity(alldata);
    }

    @Override
    public void onItemCreate(NoteEntity data) {
        projectEditMvpP.onNoteCreated(data);
    }

    @Override
    public void onItemEdit(NoteEntity data) {
        if (editIndex != INIT_POSITION)
            projectEditMvpP.onNoteEdited(editIndex, data);
        editIndex = INIT_POSITION;
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
    protected IUtimerEditMvpP getEditMvpP() {
        if(projectEditMvpP == null)
            projectEditMvpP = new ProjectEditMvpP(this, getUTimerEntity());;
        return projectEditMvpP;
    }

    @Override
    protected boolean ifEnvOk() {
        gtdProjectEntity = getUTimerEntity();
        return gtdProjectEntity != null && gtdProjectEntity.ifValid() && gtdProjectEntity.ensureAttachFileExist();
    }

    @Override
    protected GtdProjectEntity getUTimerEntity() {
        return (GtdProjectEntity) getArguments().getSerializable(KEY_GTD_PROJECT);
    }
    @Override
    protected void toStartEdit() {
    }

    @Override
    protected void toEndEdit() {

    }

    private void toReloadEntity(List<NoteEntity> alldata){
        if(alldata == null || alldata.isEmpty()){
            Logcat.i(TAG, "toReloadEntity cancel");
            return ;
        }
        List<ProjectInfoSectionViewEntity> sectionViewEntityList = Lists.newArrayList();

        String noteheaderName = MyRInfo.getStringByID(R.string.title_note_list);
        ProjectInfoSectionViewEntity projectInfoSectionHeaderEntity = new ProjectInfoSectionViewEntity(true, noteheaderName,true);
        sectionViewEntityList.add(projectInfoSectionHeaderEntity);

        for(NoteEntity data : alldata){
            ProjectInfoSectionViewEntity projectInfoSectionViewEntity = new ProjectInfoSectionViewEntity(data);
            sectionViewEntityList.add(projectInfoSectionViewEntity);
        }

        projectRecyclerView.resetData(sectionViewEntityList);
    }

    class MyClickListener implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            ProjectInfoSectionViewEntity viewEntity = (ProjectInfoSectionViewEntity) adapter.getData().get(position);
            editIndex = position;
            if(!viewEntity.isHeader) {
                NoteEntity noteEntity = (NoteEntity) viewEntity.t;
                startForResult(NoteEditFragment.newInstance(noteEntity), REQ_EDIT_NOTE_FRAGMENT);
            }
        }

        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            NoteEntity viewEntity = (NoteEntity)adapter.getItem(position);
            /*if(viewEntity != null)
                toCreateDelDialog(viewEntity);*/
            return false;
        }
    }
}

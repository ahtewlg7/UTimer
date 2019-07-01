package com.utimer.ui;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.trello.rxlifecycle2.LifecycleProvider;
import com.utimer.R;
import com.utimer.view.ProjectRecyclerView;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdProjectBuilder;
import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.mvp.ProjectListMvpP;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import butterknife.BindView;

import static com.utimer.common.Constants.REQ_EDIT_FRAGMENT;
import static com.utimer.common.Constants.REQ_NEW_FRAGMENT;
import static com.utimer.ui.ProjectFragment.KEY_GTD_PROJECT;

public class ProjectListFragment extends AToolbarBkFragment implements ProjectListMvpP.IProjectListMvpV {
    public static final String TAG = ProjectListFragment.class.getSimpleName();

    public static final int INIT_POSITION = -1;

    @BindView(R.id.fragment_project_list_toolbar)
    Toolbar toolbar;
    @BindView(R.id.fragment_project_list_recycler_view)
    ProjectRecyclerView recyclerView;

    private int editPosition = -1;
    private ProjectListMvpP projectListMvpP;
    private MyClickListener myClickListener;

    public static ProjectListFragment newInstance() {
        Bundle args = new Bundle();

        ProjectListFragment fragment = new ProjectListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View inflateView) {
        super.onViewCreated(inflateView);

        myClickListener = new MyClickListener();

        recyclerView.init(getContext(), null, myClickListener, null,myClickListener,null,null,null);
        projectListMvpP = new ProjectListMvpP(this);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        projectListMvpP.toLoadAllItem();
    }

    @Override
    public void onFragmentResult(int requestCode, int resultCode, Bundle data) {
        super.onFragmentResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            GtdProjectEntity entity = (GtdProjectEntity) data.getSerializable(KEY_GTD_PROJECT);
            if (entity != null && requestCode == REQ_NEW_FRAGMENT)
                onItemCreate(entity);
            else if (entity != null && requestCode == REQ_EDIT_FRAGMENT) {
                onItemEdit(entity);
            }
        }
    }

    /**********************************************AToolbarBkFragment**********************************************/

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_project_list;
    }

    @Override
    protected String getTitle() {
        return MyRInfo.getStringByID(R.string.title_project_list);
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
        Logcat.i(TAG, "onOptionsItemSelected " + item.getTitle());
        boolean result = false;
        switch (item.getItemId()) {
            case R.id.tool_menu_add:
                Log.i(TAG, "to create project");
                toCreateNewDialog();
                break;
            default:
                result = super.onOptionsItemSelected(item);
                break;
        }
        return result;
    }

    /**********************************************IProjectListMvpV**********************************************/
    @Override
    public LifecycleProvider getRxLifeCycleBindView() {
        return this;
    }

    /**********************************************IProjectListMvpV**********************************************/
    @Override
    public void onItemLoadStart() {
        Logcat.i(TAG, "onItemLoadStart");
    }

    @Override
    public void onItemLoad(GtdProjectEntity data) {
//        Logcat.i(TAG, "onItemLoad " + data.toString());
    }

    @Override
    public void onItemLoadErr(Throwable t) {
        Logcat.i(TAG, "onItemLoadErr : " + t.getMessage());
    }

    @Override
    public void onItemLoadEnd(List<GtdProjectEntity> alldata) {
        Logcat.i(TAG, "onItemLoadEnd");
        if(alldata != null)
            recyclerView.resetData(alldata);
    }

    /**********************************************IProjectListMvpV**********************************************/
    @Override
    public void onItemCreate(GtdProjectEntity data) {
        projectListMvpP.onItemCreated(data);
    }

    @Override
    public void onItemEdit(GtdProjectEntity data) {
        if (editPosition != INIT_POSITION)
            projectListMvpP.onItemEdited(editPosition, data);
        editPosition = INIT_POSITION;
    }

    /**********************************************IProjectListMvpV**********************************************/
    @Override
    public void resetView(List<GtdProjectEntity> dataList) {
        recyclerView.resetData(dataList);
    }

    @Override
    public void resetView(int index, GtdProjectEntity entity) {
        recyclerView.resetData(index, entity);
    }

    /**********************************************IProjectListMvpV**********************************************/
    @Override
    public void onDeleteSucc(int index , GtdProjectEntity entity) {
        ToastUtils.showShort(R.string.prompt_del_succ);
        recyclerView.removeData(index);
    }

    @Override
    public void onDeleteFail(GtdProjectEntity entity) {
        ToastUtils.showShort(R.string.prompt_del_fail);
    }

    @Override
    public void onDeleteErr(Throwable throwable) {
        Logcat.i(TAG,"onDeleteErr : " + throwable.getMessage());
        ToastUtils.showShort(R.string.prompt_del_err);
    }

    @Override
    public void onDeleteEnd() {
    }

    /**********************************************IProjectListMvpV**********************************************/

    class MyClickListener implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            editPosition = position;
            startForResult(ProjectFragment.newInstance((GtdProjectEntity) adapter.getData().get(position)), REQ_EDIT_FRAGMENT);
        }

        @Override
        public boolean onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            toCreateDelDialog();
            return false;
        }
    }
    private void toCreateNewDialog(){
        new MaterialDialog.Builder(getContext()).title(R.string.create)
            .inputType(InputType.TYPE_CLASS_TEXT)
            .input(MyRInfo.getStringByID(R.string.prompt_new_project_name), "", false, new MaterialDialog.InputCallback() {
                @Override
                public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                    //do nothing
                }
            })
            .negativeText(R.string.no)
            .positiveText(R.string.yes)
            .onNegative(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    dialog.dismiss();
                }
            })
            .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    String name = dialog.getInputEditText().getText().toString();
                    Logcat.i(TAG,"to create project : " + name);
                    GtdProjectEntity entity = (GtdProjectEntity)new GtdProjectBuilder().setTitle(name).build();
                    startForResult(ProjectFragment.newInstance(entity), REQ_NEW_FRAGMENT);
                }
            }).show();
    }
    private void toCreateDelDialog(){
        new MaterialDialog.Builder(getContext()).title(R.string.del)
                .content(R.string.prompt_del_cancel)
                .negativeText(R.string.cancel)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }
}

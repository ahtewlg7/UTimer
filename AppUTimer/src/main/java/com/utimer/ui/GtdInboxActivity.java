package com.utimer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

import com.blankj.utilcode.util.ToastUtils;
import com.utimer.R;
import com.utimer.entity.NoteSectionEntity;
import com.utimer.mvp.GtdInfoMvpPresenter;
import com.utimer.mvp.NoteRecylerMvpPresenter;
import com.utimer.view.GtdBaseSectionRecyclerView;

import java.util.List;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.entity.gtd.GtdInboxEntity;
import ahtewlg7.utimer.mvp.IGtdInfoMvpV;
import ahtewlg7.utimer.mvp.INoteRecyclerViewMvpV;
import ahtewlg7.utimer.ui.BaseBinderActivity;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.BaseSectionEntity;
import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Flowable;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2018/2/23.
 */

public class GtdInboxActivity extends BaseBinderActivity implements IGtdInfoMvpV,INoteRecyclerViewMvpV<NoteEntity> {
    public static final String TAG = GtdInboxActivity.class.getSimpleName();

    public static final String EXTRA_KEY_GTD_ID  = MyRInfo.getStringByID(R.string.extra_gtd_id);
    public static final String EXTRA_KEY_NOTE_ID = MyRInfo.getStringByID(R.string.extra_note_id);

    @BindView(R.id.activity_gtdinfo_layout_title_tv)
    TextView gtdInfoTitle;

    @BindView(R.id.activity_gtdinfo_layout_recylerview)
    GtdBaseSectionRecyclerView sectionRecylerView;

    private AGtdEntity gtdEntity;
    private GtdInfoMvpPresenter gtdInfoMvpPresenter;
    private NoteRecylerMvpPresenter noteRecylerMvpPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_gtdinfo);//todo

        Logcat.i(TAG,"onCreate");
        ButterKnife.bind(this);

        gtdInfoMvpPresenter      = new GtdInfoMvpPresenter(this);
        noteRecylerMvpPresenter  = new NoteRecylerMvpPresenter(this);

        handleIntent(getIntent());
    }

    //+++++++++++++++++++++++++++++++++++++IGtdInfoMvpV+++++++++++++++++++++++++++++++++++++++++++
    @Override
    public void updateView(AGtdEntity gtdEntity) {
        this.gtdEntity      = gtdEntity;

        StringBuilder title = new StringBuilder();
        title.append("title : " + gtdEntity.getTitle() + "\n");
        title.append("detail : " + gtdEntity.getDetail() + "\n");
        title.append("createdDateTime : " + gtdEntity.getCreatedDateTime() + "\n");
        title.append("lastModifyDateTime : " + gtdEntity.getLastModifyDateTime() + "\n");
        gtdInfoTitle.setText(title.toString());

        noteRecylerMvpPresenter.initNoteIdList(((GtdInboxEntity)gtdEntity).getNoteEntityList());
        noteRecylerMvpPresenter.loadAllData();
    }

    @Override
    public void onIdEnptyErr() {
        Logcat.d(TAG,"Gtd Id is empty, so finish");
        ToastUtils.showShort(R.string.err_id_empty);
        finish();
    }

    @Override
    public void onInfoStart() {
        //to start progress
    }

    @Override
    public void onInfoErr() {
        //to end progress
    }

    @Override
    public void onInfoEnd() {
        //to end progress
    }

    //+++++++++++++++++++++++++++++++++++++IRecyclerViewMvpV+++++++++++++++++++++++++++++++++++++++++++
    @Override
    public void initRecyclerView(List<BaseSectionEntity> dataList) {
        sectionRecylerView.init(GtdInboxActivity.this,dataList);
    }

    @Override
    public void resetRecyclerView(List<BaseSectionEntity> dataList) {
        sectionRecylerView.resetNewData(dataList);
    }

    @Override
    public void toStartNoteActivity(String noteEntityId) {
        if(gtdEntity == null || TextUtils.isEmpty(gtdEntity.getId()) || TextUtils.isEmpty(noteEntityId)){
            ToastUtils.showShort("GtdEntityId or noteEntityId are empty");
            return;
        }
        Intent intent = new Intent(this, MdEditorActivity.class);
        intent.putExtra(MdEditorActivity.EXTRA_KEY_GTD_ID, gtdEntity.getId());
        intent.putExtra(MdEditorActivity.EXTRA_KEY_NOTE_ID, noteEntityId);
        startActivityForResult(intent, MdEditorActivity.ACTIVITY_START_RESULT);
    }

    @Override
    public void onRecyclerViewInitStart() {

    }

    @Override
    public void onRecyclerViewInitErr() {

    }

    @Override
    public void onRecyclerViewInitEnd() {

    }

    @Override
    public Flowable<BaseSectionEntity> mapBean(@NonNull Flowable<NoteEntity> sourceBeanFlowable) {
        return sourceBeanFlowable.map(new Function<NoteEntity, BaseSectionEntity>() {
            @Override
            public BaseSectionEntity apply(NoteEntity noteEntity) throws Exception {
                return new NoteSectionEntity(noteEntity);
            }
        });
    }

    protected void handleIntent(@NonNull Intent intent){
        String gtdId = intent.getStringExtra(EXTRA_KEY_GTD_ID);
        if(TextUtils.isEmpty(gtdId)) {
            Logcat.i(TAG,"handleIntent : gtdId is empty, so finish");
            finish();
            return;
        }
        gtdInfoMvpPresenter.laodGtdEntity(gtdId);
    }
}

package com.utimer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.utimer.R;
import com.utimer.entity.GtdSectionEntity;
import com.utimer.mvp.GtdRecylerMvpPresenter;
import com.utimer.view.GtdBaseSectionRecyclerView;

import org.reactivestreams.Publisher;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.mvp.IGtdRecyclerViewMvpV;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.BaseSectionEntity;
import butterknife.BindView;
import io.reactivex.Flowable;
import io.reactivex.flowables.GroupedFlowable;
import io.reactivex.functions.Function;

/**
 * Created by lw on 2018/1/24.
 */

public class GtdFragment extends AFunctionFragement implements IGtdRecyclerViewMvpV<AGtdEntity> {
    public static final String TAG = GtdFragment.class.getSimpleName();

    public static final String EXTRA_KEY_GTD_ID  = MyRInfo.getStringByID(R.string.extra_gtd_id);
    public static final String EXTRA_KEY_NOTE_ID = MyRInfo.getStringByID(R.string.extra_note_id);

    private String clickedEntityId;
    private GtdRecylerMvpPresenter gtdRecylerMvpPresenter;

    @BindView(R.id.fragment_note_recyclerview)
    GtdBaseSectionRecyclerView sectionRecylerView;

    private SectionItemClickListener sectionItemClickListener;
    private SectionItemChildClickListener sectionItemChildClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        gtdRecylerMvpPresenter          = new GtdRecylerMvpPresenter(this);
        sectionItemClickListener        = new SectionItemClickListener();
        sectionItemChildClickListener   = new SectionItemChildClickListener();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = super.onCreateView(inflater,container,savedInstanceState);
        gtdRecylerMvpPresenter.loadAllData();
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String noteId   = intent.getStringExtra(EXTRA_KEY_NOTE_ID);
        String entityId = intent.getStringExtra(EXTRA_KEY_GTD_ID);
        Logcat.i(TAG,"onActivityResult noteId = " + noteId + ", entityId = " + entityId);
        /*if(!TextUtils.isEmpty(noteId))
            gtdRecylerMvpPresenter.addData(noteId);
        else */if(!TextUtils.isEmpty(entityId) && !entityId.equalsIgnoreCase(clickedEntityId))
            gtdRecylerMvpPresenter.addData(entityId);
    }

    @Override
    @NonNull
    public String getIndicateTitle() {
        return "Gtd";
    }

    @Override
    public int getIndicateIconRid() {
        return R.mipmap.ic_launcher;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.fragment_note;
    }


    @Override
    public void initRecyclerView(List<BaseSectionEntity> dataList) {
        sectionRecylerView.init(GtdFragment.this.getActivity(), dataList,
                sectionItemClickListener , sectionItemChildClickListener);
    }

    @Override
    public void resetRecyclerView(List<BaseSectionEntity> dataList) {
        sectionRecylerView.resetNewData(dataList);
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
    public Flowable<BaseSectionEntity> mapBean(@NonNull Flowable<AGtdEntity> sourceBeanFlowable) {
        return sourceBeanFlowable.groupBy(new Function<AGtdEntity, GtdType>() {
                    @Override
                    public GtdType apply(AGtdEntity gtdEntityGdBean) throws Exception {
                        return gtdEntityGdBean.getTaskType();
                    }
                })
                .flatMap(new Function<GroupedFlowable<GtdType, AGtdEntity>, Publisher<BaseSectionEntity>>() {
                    @Override
                    public Publisher<BaseSectionEntity> apply(GroupedFlowable<GtdType, AGtdEntity> groupedFlowable) throws Exception {
                        return groupedFlowable.map(new Function<AGtdEntity, BaseSectionEntity>() {
                            @Override
                            public BaseSectionEntity apply(AGtdEntity gtdEntity) throws Exception {
                                return new GtdSectionEntity(gtdEntity);
                            }
                        })
                        .startWith(new GtdSectionEntity(true,groupedFlowable.getKey(),true));
                    }
                });
    }

    @Override
    public void toStartGtdActivity(String gtdEntityId, GtdType gtdType){
        if(!toStartGtdInfoActivity(gtdEntityId, gtdType))
            ToastUtils.showShort("GtdEntityId is empty");
        else
            clickedEntityId = gtdEntityId;
    }

    @Override
    public void onHeadClick(BaseSectionEntity baseSectionEntity) {
        Logcat.i(TAG,"onHeadClick baseSectionEntity = " + baseSectionEntity.toString());
        if(!baseSectionEntity.isMore())
            toStartNoteActivity();
    }

    private Class getTargetClass(GtdType gtdType){
        Class target = null;
        switch (gtdType){
            case INBOX:
                target = GtdInboxActivity.class;
                break;
        }
        return target;
    }

    private void toStartNoteActivity(){
        Intent intent = new Intent(GtdFragment.this.getActivity(), MdEditorActivity.class);
        startActivity(intent);
    }
    private boolean toStartGtdInfoActivity(String gtdEntityId, GtdType gtdType){
        if(TextUtils.isEmpty(gtdEntityId)){
            Logcat.i(TAG,"GtdEntityId is empty");
            return false;
        }

        Intent intent = new Intent(GtdFragment.this.getActivity(), getTargetClass(gtdType));
        intent.putExtra(MdEditorActivity.EXTRA_KEY_GTD_ID, gtdEntityId);
        startActivity(intent);
        return true;
    }


    class SectionItemClickListener implements BaseQuickAdapter.OnItemClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            GtdSectionEntity mySectionEntity = (GtdSectionEntity)adapter.getData().get(position);
            gtdRecylerMvpPresenter.onItemClick(mySectionEntity);
        }
    }
    class SectionItemChildClickListener implements BaseQuickAdapter.OnItemChildClickListener{
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            Logcat.i(TAG,"onItemChildClick : position = " + position);
        }
    }
}

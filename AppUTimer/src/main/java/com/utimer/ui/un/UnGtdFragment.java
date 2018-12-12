package com.utimer.ui.un;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2018/1/24.
 */

public class UnGtdFragment extends UnAFunctionFragement {
    public static final String TAG = UnGtdFragment.class.getSimpleName();

    private String clickedEntityId;

    private SectionItemClickListener sectionItemClickListener;
    private SectionItemChildClickListener sectionItemChildClickListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sectionItemClickListener        = new SectionItemClickListener();
        sectionItemChildClickListener   = new SectionItemChildClickListener();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        String noteId   = null/*intent.getStringExtra(EXTRA_KEY_NOTE_ID)*/;
        String entityId = null/*intent.getStringExtra(EXTRA_KEY_GTD_ID)*/;
        Logcat.i(TAG,"onActivityResult noteId = " + noteId + ", entityId = " + entityId);
        /*if(!TextUtils.isEmpty(noteId))
            gtdRecylerMvpPresenter.addData(noteId);
        else if(!TextUtils.isEmpty(entityId) && !entityId.equalsIgnoreCase(clickedEntityId))
            gtdRecylerMvpPresenter.addData(entityId);*/
    }

    @Override
    @NonNull
    public String getIndicateTitle() {
        return "Gtd";
    }

    @Override
    public int getIndicateIconRid() {
        return R.drawable.page_indicator;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.un_fragment_recycler;
    }

    @Override
    public void initLayoutView(View inflateView) {
//        gtdRecylerMvpPresenter.loadAllData();
    }


    public void initRecyclerView(List/*<AGtdEntity>*/ dataList) {
        /*sectionRecylerView.init(UnGtdFragment.this.getActivity(), dataList,
                sectionItemClickListener , sectionItemChildClickListener);*/
    }

    /*public Flowable<BaseSectionEntity> mapBean(@NonNull Flowable<AGtdEntity> sourceBeanFlowable) {
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
    }*/

    /*@Override
    public void toStartGtdActivity(String gtdEntityId, GtdType gtdType){
        if(!toStartGtdInfoActivity(gtdEntityId, gtdType))
            ToastUtils.showShort("GtdEntityId is empty");
        else
            clickedEntityId = gtdEntityId;
    }

    @Override
    public void onHeadClick(BaseSectionEntity baseSectionEntity) {
        Logcat.i(TAG,"onHeadClick baseSectionEntity = " + baseSectionEntity.toString());
        *//*if(((GtdSectionEntity)baseSectionEntity).getGtdType() == GtdType.INBOX)
            toStartNoteActivity();*//*
    }*/

    /*private Class getTargetClass(GtdType gtdType){
        Class target = null;
        switch (gtdType){
            case INBOX:
                target = GtdInboxActivity.class;
                break;
        }
        return target;
    }*/

    private void toStartNoteActivity(){
        Intent intent = new Intent(UnGtdFragment.this.getActivity(), UnMdEditorActivity.class);
        startActivity(intent);
    }
    private boolean toStartGtdInfoActivity(String gtdEntityId, GtdType gtdType){
        if(TextUtils.isEmpty(gtdEntityId)){
            Logcat.i(TAG,"GtdEntityId is empty");
            return false;
        }

        /*Intent intent = new Intent(UnGtdFragment.this.getActivity(), getTargetClass(gtdType));
        intent.putExtra(UnMdEditorActivity.EXTRA_KEY_GTD_ID, gtdEntityId);
        startActivity(intent);*/
        return true;
    }


    class SectionItemClickListener implements BaseQuickAdapter.OnItemClickListener {
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            /*GtdSectionEntity mySectionEntity = (GtdSectionEntity)adapter.getData().get(position);
            gtdRecylerMvpPresenter.onItemClick(mySectionEntity);*/
        }
    }
    class SectionItemChildClickListener implements BaseQuickAdapter.OnItemChildClickListener{
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
            Logcat.i(TAG,"onItemChildClick : position = " + position);
        }
    }
}

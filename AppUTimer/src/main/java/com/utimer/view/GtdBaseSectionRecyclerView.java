package com.utimer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.view.ABaseSectionRecyclerView;
import ahtewlg7.utimer.view.BaseSectionEntity;

/**
 * Created by lw on 2018/3/12.
 */

public class GtdBaseSectionRecyclerView extends ABaseSectionRecyclerView{
    public static final String TAG = GtdBaseSectionRecyclerView.class.getSimpleName();

    private int columnNum = 2;
    private boolean isInited;
    private GtdBaseSectionAdapter sectionAdapter;
    private List<BaseSectionEntity> sectionEntityList;

    public GtdBaseSectionRecyclerView(Context context) {
        super(context);
    }

    public GtdBaseSectionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GtdBaseSectionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_gtd_section_item;
    }

    @Override
    public int getViewHeadLayout() {
        return R.layout.view_gtd_section_head;
    }

    @Override
    public void init(Context context, List<BaseSectionEntity> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener){
        sectionEntityList = entityList;
        sectionAdapter = new GtdBaseSectionAdapter(sectionEntityList);
        setLayoutManager(new GridLayoutManager(context, columnNum));
        if(itemClickListener != null)
            sectionAdapter.setOnItemClickListener(itemClickListener);
        if(itemChildClickListener != null)
            sectionAdapter.setOnItemChildClickListener(itemChildClickListener);
        setAdapter(sectionAdapter);
        isInited = true;
    }

    @Override
    public void init(Context context, List<BaseSectionEntity> entityList){
        init(context, entityList, null, null);
    }


    public int getColumnNum() {
        return columnNum;
    }

    public void setColumnNum(int columnNum) {
        this.columnNum = columnNum;
    }

    public void resetNewData(List<BaseSectionEntity> entityList){
        sectionAdapter.setNewData(entityList);
    }

    public class GtdBaseSectionAdapter extends BaseSectionAdapter<BaseSectionEntity>{
        public GtdBaseSectionAdapter(List<BaseSectionEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, BaseSectionEntity item) {
            helper.setText(R.id.view_gtd_header, item.header);
            helper.setVisible(R.id.view_gtd_more, item.isMore());
            helper.addOnClickListener(R.id.view_gtd_more);
        }

        @Override
        protected void convert(BaseViewHolder helper, BaseSectionEntity item) {
            AGtdEntity gtdEntity = (AGtdEntity)item.t;
            switch (helper.getLayoutPosition() % 2) {
                case 0:
                    helper.setImageResource(R.id.view_gtd_iv, R.mipmap.ic_launcher);
                    break;
                case 1:
                    helper.setImageResource(R.id.view_gtd_iv, R.mipmap.ic_launcher);
                    break;

            }
            helper.setText(R.id.view_gtd_tv, gtdEntity.getTitle());
        }
    }
}

package com.utimer.view.recyclerview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.entity.SectionEntity;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.AGtdEntity;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2018/2/1.
 */

public class GtdSectionRecylerView extends RecyclerView {
    public static final String TAG = GtdSectionRecylerView.class.getSimpleName();

    private boolean isInited;
    private GtdSectionAdapter sectionAdapter;
    private List<GtdSectionEntity> sectionEntityList;

    public GtdSectionRecylerView(Context context) {
        super(context);
    }

    public GtdSectionRecylerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GtdSectionRecylerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void init(final Context context, List<GtdSectionEntity> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener){
        sectionEntityList = entityList;
        sectionAdapter = new GtdSectionAdapter(sectionEntityList);
        setLayoutManager(new GridLayoutManager(context, 2));
        if(itemClickListener != null)
            sectionAdapter.setOnItemClickListener(itemClickListener);
        if(itemChildClickListener != null)
            sectionAdapter.setOnItemChildClickListener(itemChildClickListener);
        setAdapter(sectionAdapter);
        isInited = true;
    }
    public void init(Context context, List<GtdSectionEntity> entityList){
        init(context, entityList, null, null);
    }

    public void resetNewData(List<GtdSectionEntity> entityList){
        sectionAdapter.setNewData(entityList);
    }

    public static class GtdSectionEntity extends SectionEntity<AGtdEntity>{
        private boolean isMore;
        private GtdType gtdType;

        public GtdSectionEntity(boolean isHeader, GtdType gtdType, boolean isMore) {
            super(isHeader, gtdType.name());
            this.gtdType = gtdType;
            this.isMore  = isMore;
        }

        public GtdSectionEntity(AGtdEntity gtdEntity){
            super(gtdEntity);
            gtdType = gtdEntity.getTaskType();
        }


        public GtdType getGtdType() {
            return gtdType;
        }
        public void setGtdType(GtdType gtdType) {
            this.gtdType = gtdType;
        }

        public boolean isMore() {
            return isMore;
        }

        public void setMore(boolean more) {
            isMore = more;
        }

        @Override
        public String toString() {
            String tmp = "GtdSectionEntity{gtdType = " + gtdType.name() + ",isMore = " + isMore;
            if(t != null)
                tmp += ", gtdEntity =" + t.toString() +"}";
            return tmp;
        }
    }


    class GtdSectionAdapter extends BaseSectionQuickAdapter<GtdSectionEntity,BaseViewHolder> {

        public GtdSectionAdapter(List<GtdSectionEntity> data) {
            super(R.layout.view_gtd_section_item, R.layout.view_gtd_section_head, data);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, GtdSectionEntity item) {
            helper.setText(R.id.view_gtd_header, item.header);
            helper.setVisible(R.id.view_gtd_more, item.isMore());
            helper.addOnClickListener(R.id.view_gtd_more);
        }

        @Override
        protected void convert(BaseViewHolder helper, GtdSectionEntity item) {
            AGtdEntity gtdEntity = item.t;
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

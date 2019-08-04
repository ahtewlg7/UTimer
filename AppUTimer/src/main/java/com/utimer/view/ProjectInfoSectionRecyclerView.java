package com.utimer.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.utimer.R;
import com.utimer.common.TextImageFactory;
import com.utimer.entity.ProjectInfoSectionViewEntity;

import java.util.List;

import ahtewlg7.utimer.view.ABaseSectionRecyclerView;

public class ProjectInfoSectionRecyclerView extends ABaseSectionRecyclerView<ProjectInfoSectionViewEntity> {
    public static final String TAG = ProjectInfoSectionRecyclerView.class.getSimpleName();

    public ProjectInfoSectionRecyclerView(Context context) {
        super(context);
    }

    public ProjectInfoSectionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProjectInfoSectionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_shorthand_list_item;
    }

    @Override
    public int getViewHeadLayout() {
        return R.layout.view_gtd_section_head;
    }

    @NonNull
    @Override
    public BaseSectionAdapter createAdapter(List<ProjectInfoSectionViewEntity> entityList) {
        return new ProjectInfoAdapter(entityList);
    }

    @Override
    public void init(Context context, int columnNum, List<ProjectInfoSectionViewEntity> entityList, BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener, BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener) {
        super.init(context, columnNum, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    class ProjectInfoAdapter extends BaseSectionAdapter{
        public ProjectInfoAdapter(List<ProjectInfoSectionViewEntity> dataList){
            super(dataList);
        }
        @Override
        protected void convertHead(BaseViewHolder helper, ProjectInfoSectionViewEntity item) {
            helper.setText(R.id.view_gtd_header, item.header);
            helper.setVisible(R.id.view_gtd_more, item.isMore());
            helper.addOnClickListener(R.id.view_gtd_more);
        }

        @Override
        protected void convert(BaseViewHolder helper, ProjectInfoSectionViewEntity item) {
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder detailBuilder = new StringBuilder();

            if(item.getTitle().isPresent() && !TextUtils.isEmpty(item.getTitle().get()))
                titleBuilder.append(item.getTitle().get());
            /*if(item.getRPath().isPresent() && !TextUtils.isEmpty(item.getRPath().get()))
                builder.append(item.getRPath().get());
            if(item.getLastAccessTime() != null)
                titleBuilder.append("\n").append(item.getLastAccessTime().toDateTime());*/
            if(item.getDetail().isPresent() && !TextUtils.isEmpty(item.getDetail().get()))
                detailBuilder.append(item.getDetail().get());
            helper.setText(R.id.view_shorthand_list_item_title, titleBuilder.toString())
                .setText(R.id.view_shorthand_list_item_detail, detailBuilder.toString())
                .setImageDrawable(R.id.view_shorthand_list_item_image, TextImageFactory.getInstance().getShortHandImage());
        }
    }
}

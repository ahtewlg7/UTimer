package com.utimer.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.view.BaseSectionEntity;
import ahtewlg7.utimer.view.ABaseSectionRecyclerView;

public class EndDeedRecyclerView extends ABaseSectionRecyclerView<BaseSectionEntity, BaseViewHolder> {
    public EndDeedRecyclerView(Context context) {
        super(context);
    }

    public EndDeedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EndDeedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewHeadLayout() {
        return R.layout.view_section_item;
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_section_item;
    }

    @NonNull
    @Override
    public BaseQuickAdapter<BaseSectionEntity, BaseViewHolder> createAdapter(List<BaseSectionEntity> entityList) {
        return new EndDeedItemAdapter(entityList);
    }

    @Override
    public void init(Context context, List<BaseSectionEntity> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener) {
        super.init(context,1, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener);
    }

    @Override
    public void removeData(BaseSectionEntity baseSectionEntity) {

    }

    class EndDeedItemAdapter extends BaseSectionAdapter{
        EndDeedItemAdapter(List<BaseSectionEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, BaseSectionEntity item) {
            helper.setText(R.id.view_section_item_name, item.header);
        }

        @Override
        protected void convert(BaseViewHolder helper, BaseSectionEntity item) {
            helper.setText(R.id.view_section_item_name, ((GtdDeedEntity)(item.t)).getTitle());
        }
    }
}

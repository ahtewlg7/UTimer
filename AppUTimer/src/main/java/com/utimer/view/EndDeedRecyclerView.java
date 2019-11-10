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

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.view.EndDeedSectionEntity;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.view.ABaseSectionRecyclerView;

public class EndDeedRecyclerView extends ABaseSectionRecyclerView<EndDeedSectionEntity, BaseViewHolder> {
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
        return R.layout.view_section_header;
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_section_item;
    }

    @NonNull
    @Override
    public BaseQuickAdapter<EndDeedSectionEntity, BaseViewHolder> createAdapter(List<EndDeedSectionEntity> entityList) {
        return new EndDeedItemAdapter(entityList);
    }

    @Override
    public void init(Context context, List<EndDeedSectionEntity> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener) {
        super.init(context,1, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener);
    }

    @Override
    public void removeData(EndDeedSectionEntity baseSectionEntity) {

    }

    class EndDeedItemAdapter extends BaseSectionAdapter{
        DateTimeAction dateTimeAction;
        EndDeedItemAdapter(List<EndDeedSectionEntity> dataList){
            super(dataList);
            dateTimeAction = new DateTimeAction();
        }

        @Override
        protected void convertHead(BaseViewHolder helper, EndDeedSectionEntity item) {
            helper.setText(R.id.view_section_header_name, item.header);
        }

        @Override
        protected void convert(BaseViewHolder helper, EndDeedSectionEntity item) {
            helper.setText(R.id.view_section_item_name, toSpan((GtdDeedEntity)(item.t), helper.getLayoutPosition()))
                .setText(R.id.view_section_item_createtime, getFormatDate(((GtdDeedEntity)(item.t)).getCreateTime()))
                .setText(R.id.view_section_item_endtime, getFormatDate(((GtdDeedEntity)(item.t)).getEndTime()));
        }
        private String getFormatDate(DateTime dateTime){
            return dateTimeAction.toFormat(dateTime);
        }
    }
}

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
import com.utimer.common.WeekFactory;

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

    @Deprecated
    @Override
    public void removeData(EndDeedSectionEntity baseSectionEntity) {
        //do nothing , this adapter is not supported
    }

    class EndDeedItemAdapter extends BaseSectionAdapter{
        WeekFactory weekFactory;
        DateTimeAction dateTimeAction;

        EndDeedItemAdapter(List<EndDeedSectionEntity> dataList){
            super(dataList);
            weekFactory    = new WeekFactory();
            dateTimeAction = new DateTimeAction();
        }

        @Override
        protected void convertHead(BaseViewHolder helper, EndDeedSectionEntity item) {
            helper.setText(R.id.view_section_header_name, item.header);
        }

        @Override
        protected void convert(BaseViewHolder helper, EndDeedSectionEntity item) {
            helper.setText(R.id.view_section_item_name, toSpan((GtdDeedEntity)(item.t), helper.getLayoutPosition()))
                .addOnClickListener(R.id.view_section_item_name)
                .setText(R.id.view_section_item_createtime, getFormatDetail(((GtdDeedEntity)(item.t)).getCreateTime()))
                .setText(R.id.view_section_item_endtime, getFormatDetail(((GtdDeedEntity)(item.t)).getEndTime()));
        }
        private String getFormatDetail(DateTime dateTime){
            return dateTime == null ? " " : dateTimeAction.toFormat(dateTime) + " " + weekFactory.getWeekName(dateTime);
        }
    }
}

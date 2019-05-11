package com.utimer.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.google.common.base.Optional;
import com.utimer.R;
import com.utimer.common.TextImageFactory;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.gtd.GtdLifeCycleAction;
import ahtewlg7.utimer.view.ABaseLinearRecyclerView;

public class GtdDeedRecyclerView extends ABaseLinearRecyclerView<GtdDeedEntity> {
    private GtdLifeCycleAction gtdActLifeCycleAction;

    public GtdDeedRecyclerView(Context context) {
        super(context);
        gtdActLifeCycleAction = new GtdLifeCycleAction();
    }

    public GtdDeedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        gtdActLifeCycleAction = new GtdLifeCycleAction();
    }

    public GtdDeedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        gtdActLifeCycleAction = new GtdLifeCycleAction();
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_shorthand_list_item;
    }

    @NonNull
    @Override
    public BaseItemAdapter<GtdDeedEntity> createAdapter(List<GtdDeedEntity> entityList) {
        return new ActionItemAdapter(entityList);
    }

    @Override
    public void init(Context context, List<GtdDeedEntity> entityList, BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener, BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener, OnItemSwipeListener itemSwipeListener, OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    class ActionItemAdapter extends BaseItemAdapter<GtdDeedEntity>{
        ActionItemAdapter(List<GtdDeedEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, GtdDeedEntity item) {
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder detailBuilder = new StringBuilder();

            /*if(!TextUtils.isEmpty(item.getTitle()))
                titleBuilder.append(item.getTitle());*/
            if(item.getDetail().isPresent() && !TextUtils.isEmpty(item.getDetail().get()))
                detailBuilder.append(item.getDetail().get());
            if(item.getGtdLife() != null)
                titleBuilder.append(gtdActLifeCycleAction.getActLifeDetail(item.getGtdLife())).append("\n");
            Optional<String> warningTime = item.getWarningTimeInfo();
            if(warningTime.isPresent())
                titleBuilder.append("at:").append(warningTime.get());
            helper.setText(R.id.view_shorthand_list_item_title, titleBuilder.toString())
                .setText(R.id.view_shorthand_list_item_detail, detailBuilder.toString())
                .setImageDrawable(R.id.view_shorthand_list_item_image, TextImageFactory.getInstance().getGtdActionImage());
        }
    }
}

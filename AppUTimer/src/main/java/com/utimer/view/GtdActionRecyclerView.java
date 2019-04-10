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
import com.utimer.R;
import com.utimer.common.TextImageFactory;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdActionEntity;
import ahtewlg7.utimer.enumtype.ActLife;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.ABaseLinearRecyclerView;

public class GtdActionRecyclerView extends ABaseLinearRecyclerView<GtdActionEntity> {
    public static final String TAG = GtdActionRecyclerView.class.getSimpleName();

    public GtdActionRecyclerView(Context context) {
        super(context);
    }

    public GtdActionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GtdActionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_shorthand_list_item;
    }

    @NonNull
    @Override
    public BaseItemAdapter<GtdActionEntity> createAdapter(List<GtdActionEntity> entityList) {
        return new ActionItemAdapter(entityList);
    }

    @Override
    public void init(Context context, List<GtdActionEntity> entityList, BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener, BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener, OnItemSwipeListener itemSwipeListener, OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    class ActionItemAdapter extends BaseItemAdapter<GtdActionEntity>{
        ActionItemAdapter(List<GtdActionEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, GtdActionEntity item) {
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder detailBuilder = new StringBuilder();

            if(!TextUtils.isEmpty(item.getTitle()))
                titleBuilder.append(item.getTitle());
            /*if(item.getRPath().isPresent() && !TextUtils.isEmpty(item.getRPath().get()))
                builder.append(item.getRPath().get());*/
            /*if(item.getLastAccessTime() != null)
                titleBuilder.append("\n").append(item.getLastAccessTime().toDateTime());*/
            if(item.getDetail().isPresent() && !TextUtils.isEmpty(item.getDetail().get()))
                detailBuilder.append(item.getDetail().get());
            if(item.getActLife() != null)
                titleBuilder.append("\n").append(getActLifeDetail(item.getActLife()));
            if(item.getWorkTimeInfo().isPresent())
                titleBuilder.append("\nat:").append(item.getWorkTimeInfo().get());
            helper.setText(R.id.view_shorthand_list_item_title, titleBuilder.toString())
                .setText(R.id.view_shorthand_list_item_detail, detailBuilder.toString())
                .setImageDrawable(R.id.view_shorthand_list_item_image, TextImageFactory.getInstance().getGtdActionImage());
        }
    }

    private String getActLifeDetail(ActLife actLife){
        String detail = null;
        switch (actLife){
            case TODAY:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_today);
                break;
            case TOMORROW:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_tomorrow);
                break;
            case WEEK:
                detail = MyRInfo.getStringByID(R.string.prompt_action_life_week);
                break;
        }
        return detail;
    }
}

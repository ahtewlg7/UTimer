package com.utimer.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdProjectEntity;
import ahtewlg7.utimer.view.ABaseLinearRecyclerView;

public class ProjectRecyclerView extends ABaseLinearRecyclerView<GtdProjectEntity> {
    public static final String TAG = ProjectRecyclerView.class.getSimpleName();

    public ProjectRecyclerView(Context context) {
        super(context);
    }

    public ProjectRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ProjectRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_shorthand_list_item;
    }

    @NonNull
    @Override
    public BaseItemAdapter<GtdProjectEntity> createAdapter(List<GtdProjectEntity> entityList) {
        return new ProjectItemAdapter(entityList);
    }

    @Override
    public void init(Context context, List<GtdProjectEntity> entityList, BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     OnItemSwipeListener itemSwipeListener, OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    class ProjectItemAdapter extends BaseItemAdapter<GtdProjectEntity>{
        ProjectItemAdapter(List<GtdProjectEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, GtdProjectEntity item) {
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder detailBuilder = new StringBuilder();

            if(!TextUtils.isEmpty(item.getTitle()))
                titleBuilder.append(item.getTitle());
            /*if(item.getRPath().isPresent() && !TextUtils.isEmpty(item.getRPath().get()))
                titleBuilder.append(item.getRPath().get());
            if(item.getLastAccessTime() != null)
                titleBuilder.append("\n").append(item.getLastAccessTime().toDateTime());*/
            if(item.getDetail().isPresent() && !TextUtils.isEmpty(item.getDetail().get()))
                detailBuilder.append(item.getDetail().get());
            helper.setText(R.id.view_shorthand_list_item_title, titleBuilder.toString())
                .setText(R.id.view_shorthand_list_item_detail, detailBuilder.toString())
                .setImageDrawable(R.id.view_shorthand_list_item_image, TextDrawable.builder().buildRect("P", Color.parseColor("#177bbd")));
        }
    }
}

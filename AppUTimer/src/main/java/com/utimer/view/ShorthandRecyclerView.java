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

import ahtewlg7.utimer.entity.gtd.ShortHandEntity;
import ahtewlg7.utimer.view.ABaseLinearRecyclerView;

public class ShorthandRecyclerView extends ABaseLinearRecyclerView<ShortHandEntity> {
    public static final String TAG = ShorthandRecyclerView.class.getSimpleName();

    public ShorthandRecyclerView(Context context) {
        super(context);
    }

    public ShorthandRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ShorthandRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_shorthand_list_item;
    }

    @NonNull
    @Override
    public BaseItemAdapter<ShortHandEntity> createAdapter(List<ShortHandEntity> entityList) {
        return new ShorthandItemAdapter(entityList);
    }

    @Override
    public void init(Context context, List<ShortHandEntity> entityList, BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener, BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener, OnItemSwipeListener itemSwipeListener, OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    class ShorthandItemAdapter extends BaseItemAdapter<ShortHandEntity>{
        ShorthandItemAdapter(List<ShortHandEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, ShortHandEntity item) {
            StringBuilder titleBuilder = new StringBuilder();
            StringBuilder detailBuilder = new StringBuilder();

            if(!TextUtils.isEmpty(item.getTitle()))
                titleBuilder.append(item.getTitle());
            /*if(item.getRPath().isPresent() && !TextUtils.isEmpty(item.getRPath().get()))
                builder.append(item.getRPath().get());*/
            if(item.getLastAccessTime() != null)
                titleBuilder.append("\n").append(item.getLastAccessTime().toDateTime());
            if(item.toTips().isPresent() && !TextUtils.isEmpty(item.toTips().get()))
                detailBuilder.append(item.toTips().get());
            helper.setText(R.id.view_shorthand_list_item_title, titleBuilder.toString())
                .setText(R.id.view_shorthand_list_item_detail, detailBuilder.toString())
                .setImageDrawable(R.id.view_shorthand_list_item_image, TextDrawable.builder().buildRect("S", Color.parseColor("#177bbd")));
        }
    }
}

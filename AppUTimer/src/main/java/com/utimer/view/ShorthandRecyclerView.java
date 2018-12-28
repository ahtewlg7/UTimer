package com.utimer.view;

import android.content.Context;
import android.graphics.Color;
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

    private ShorthandItemAdapter itemAdapter;

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

    @Override
    public void init(Context context, List<ShortHandEntity> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener) {
        itemAdapter = new ShorthandItemAdapter(context, entityList);
        itemAdapter.toSetOnItemClickListener(itemClickListener);
        itemAdapter.toSetOnItemChildClickListener(itemChildClickListener);
        itemAdapter.toSetOnItemLongClickListener(itemLongClickListener);
        itemAdapter.toSetItemChildLongClickListener(itemChildLongClickListener);
        itemAdapter.toSetOnItemSwipeListener(itemSwipeListener);
        itemAdapter.toSetOnItemDragListener(itemDragListener);
        itemAdapter.bindToRecyclerView(this);
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(itemAdapter);
    }

    @Override
    public void init(Context context, List<ShortHandEntity> entityList) {
        init(context, entityList, null, null, null,null, null,null);
    }

    @Override
    public void resetData(List<ShortHandEntity> entityList) {
        if(itemAdapter != null)
            itemAdapter.replaceData(entityList);
    }

    @Override
    public void resetData(int index, ShortHandEntity entity) {
        if(itemAdapter != null)
            itemAdapter.setData(index, entity);
    }

    @Override
    public void resetData(int index, List<ShortHandEntity> entityList) {
    }

    @Override
    public void removeData(int index) {
        if(itemAdapter != null)
            itemAdapter.remove(index);
    }

    class ShorthandItemAdapter extends BaseItemAdapter<ShortHandEntity>{
        ShorthandItemAdapter(Context context, List<ShortHandEntity> dataList){
            super(context, dataList);
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
            if(item.getDetail().isPresent() && !TextUtils.isEmpty(item.getDetail().get()))
                detailBuilder.append(item.getDetail().get());
            helper.setText(R.id.view_shorthand_list_item_title, titleBuilder.toString())
                .setText(R.id.view_shorthand_list_item_detail, detailBuilder.toString())
                .setImageDrawable(R.id.view_shorthand_list_item_image, TextDrawable.builder().buildRect("S", Color.parseColor("#ff0099cc")));
        }
    }
}

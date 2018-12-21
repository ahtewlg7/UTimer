package com.utimer.view;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.amulyakhare.textdrawable.TextDrawable;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.utimer.R;
import com.utimer.entity.ShorthandSectionEntity;

import java.util.List;

import ahtewlg7.utimer.view.ABaseSectionRecyclerView;

public class ShorthandRecyclerView extends ABaseSectionRecyclerView<ShorthandSectionEntity>{
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
        return R.layout.view_gtd_section_item;
    }

    @Override
    public int getViewHeadLayout() {
        return R.layout.view_gtd_section_head;
    }

    @Override
    public void init(Context context, List<ShorthandSectionEntity> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener) {
        itemAdapter = new ShorthandItemAdapter(context, entityList);
        itemAdapter.setOnItemClickListener(itemClickListener);
        itemAdapter.setOnItemChildClickListener(itemChildClickListener);
        itemAdapter.bindToRecyclerView(this);
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(itemAdapter);
    }

    @Override
    public void init(Context context, List<ShorthandSectionEntity> entityList) {
        init(context, entityList, null, null);
    }

    @Override
    public void resetData(List<ShorthandSectionEntity> entityList) {
        if(itemAdapter != null)
            itemAdapter.replaceData(entityList);
    }

    public class ShorthandItemAdapter extends BaseSectionAdapter{

        public ShorthandItemAdapter(Context context, List<ShorthandSectionEntity> dataList){
            super(context, dataList);
        }

        @Override
        protected void convertHead(BaseViewHolder helper, final ShorthandSectionEntity item) {
            if(!item.isMore())
                helper.setText(R.id.view_gtd_header, item.header)
                        .getView(R.id.view_gtd_more).setVisibility(View.INVISIBLE);
            else
                helper.setText(R.id.view_gtd_header, item.header)
                        .addOnClickListener(R.id.view_gtd_more)
                        .getView(R.id.view_gtd_more).setVisibility(View.VISIBLE);
        }

        @Override
        protected void convert(BaseViewHolder helper, ShorthandSectionEntity item) {
//            Logcat.i(TAG,"convert : " + item.toString());
            StringBuilder builder = new StringBuilder();
            if(item.getRPath().isPresent() && !TextUtils.isEmpty(item.getRPath().get()))
                builder.append(item.getRPath().get());
            /*if(item.getTitle().isPresent() && !TextUtils.isEmpty(item.getTitle().get()))
                    builder.append(item.getTitle().get());*/
            if(item.getDetail().isPresent() && !TextUtils.isEmpty(item.getDetail().get()))
                    builder.append("\n").append(item.getDetail().get());
            if(item.getLastAccessTime().isPresent() && !TextUtils.isEmpty(item.getLastAccessTime().get()))
                builder.append("\n").append(item.getLastAccessTime().get());
            helper.setText(R.id.view_gtd_tv, builder.toString())
                .setImageDrawable(R.id.view_gtd_iv, TextDrawable.builder().buildRect("A", Color.parseColor("#ff0099cc")));
        }
    }
}

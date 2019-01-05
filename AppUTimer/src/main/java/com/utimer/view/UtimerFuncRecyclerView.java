package com.utimer.view;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.view.ABaseLinearRecyclerView;

public class UtimerFuncRecyclerView extends ABaseLinearRecyclerView<UtimerFuncRecyclerView.FuncViewEntity> {
    public static final String TAG = UtimerFuncRecyclerView.class.getSimpleName();

    public UtimerFuncRecyclerView(Context context) {
        super(context);
    }

    public UtimerFuncRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UtimerFuncRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_utimer_item;
    }

    @NonNull
    @Override
    public BaseItemAdapter<FuncViewEntity> createAdapter(List<FuncViewEntity> entityList) {
        return new UtimerFuncItemAdapter(entityList);
    }

    @Override
    public void init(Context context, List<FuncViewEntity> entityList, BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener, BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener, OnItemSwipeListener itemSwipeListener, OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        addItemDecoration(new SpacesItemDecoration(50));
        setLayoutManager(new GridLayoutManager(context, 2));
    }

    class UtimerFuncItemAdapter extends BaseItemAdapter<FuncViewEntity> {
        UtimerFuncItemAdapter(List<FuncViewEntity> dataList) {
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, FuncViewEntity item) {
            helper.setText(R.id.view_utimer_item_name, item.getFunTitle())
                    .setImageDrawable(R.id.view_utimer_item_img, item.getFunDrawable());
        }
    }

    public static class FuncViewEntity {
        private Drawable funDrawable;
        private String funTitle;

        public FuncViewEntity(@NonNull Drawable funDrawable, @NonNull String funTitle) {
            this.funDrawable = funDrawable;
            this.funTitle = funTitle;
        }

        public Drawable getFunDrawable() {
            return funDrawable;
        }

        public String getFunTitle() {
            return funTitle;
        }
    }


    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left   = space;
            outRect.right  = space;
            outRect.bottom = space;
            outRect.top    = space;
        }
    }
}

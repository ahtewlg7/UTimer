package com.utimer.view;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.view.ABaseLinearRecyclerView;

public class SimpleDeedRecyclerView extends ABaseLinearRecyclerView<GtdDeedEntity> {

    public SimpleDeedRecyclerView(Context context) {
        super(context);
    }

    public SimpleDeedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleDeedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_recycler_simple_deed_item;
    }

    @NonNull
    @Override
    public BaseItemAdapter<GtdDeedEntity> createAdapter(List<GtdDeedEntity> entityList) {
        return new SimpleDeedItemAdapter(entityList);
    }

    @Override
    public void init(Context context, List<GtdDeedEntity> entityList, BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener, BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener, OnItemSwipeListener itemSwipeListener, OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    class SimpleDeedItemAdapter extends BaseItemAdapter<GtdDeedEntity>{
        SimpleDeedItemAdapter(List<GtdDeedEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, GtdDeedEntity item) {
            ClickableSpan clickSpan = new MyClickableSpan();
            SpannableStringBuilder builder = new SpannableStringBuilder(item.getTitle().trim());
            builder.append("more");

            builder.setSpan(clickSpan, builder.length() - 4, builder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            helper.setText(R.id.view_recycler_simple_deed_title, builder);
            ((EditText)helper.getView(R.id.view_recycler_simple_deed_title)).setMovementMethod(LinkMovementMethod.getInstance());
        }
    }
    class MyClickableSpan extends ClickableSpan{
        @Override
        public void onClick(@NonNull View widget) {
            //do nothing
            int i = 0;
        }
    }
}

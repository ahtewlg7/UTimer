package com.utimer.view;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.util.AttributeSet;
import android.widget.TextView;

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
    private IDeedSpanner spanner;

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
    public void init(Context context, List<GtdDeedEntity> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    public void setSpanner(IDeedSpanner spanner) {
        this.spanner = spanner;
    }

    class SimpleDeedItemAdapter extends BaseItemAdapter<GtdDeedEntity>{
        SimpleDeedItemAdapter(List<GtdDeedEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, GtdDeedEntity item) {
            ((TextView)helper.getView(R.id.view_recycler_simple_deed_title)).setMovementMethod(LinkMovementMethod.getInstance());

            int position  = helper.getLayoutPosition();
            SpannableStringBuilder tmp = new SpannableStringBuilder(item.getTitle().trim());
            if(spanner != null)
                tmp = spanner.toSpan(position, item);
            helper.setText(R.id.view_recycler_simple_deed_title, tmp);
        }
    }

    public interface IDeedSpanner{
        public @NonNull SpannableStringBuilder toSpan(int position, @NonNull GtdDeedEntity item);
    }
}

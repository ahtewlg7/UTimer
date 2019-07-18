package com.utimer.view;

import android.content.Context;
import android.graphics.Color;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.binaryfork.spanny.Spanny;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.google.common.base.Optional;
import com.utimer.R;
import com.utimer.common.TagInfoFactory;
import com.utimer.entity.span.DeedSpanMoreTag;

import java.util.List;

import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.entity.span.SimpleMultiSpanTag;
import ahtewlg7.utimer.span.TextClickableSpan;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.ABaseLinearRecyclerView;

public class SimpleDeedRecyclerView extends ABaseLinearRecyclerView<GtdDeedEntity> {
    private boolean showLifeInfo;
    private TagInfoFactory tagInfoFactory;
    private TextClickableSpan.ITextSpanClickListener spanClickListener;

    public SimpleDeedRecyclerView(Context context) {
        super(context);
        tagInfoFactory = new TagInfoFactory();
    }

    public SimpleDeedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        tagInfoFactory = new TagInfoFactory();
    }

    public SimpleDeedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        tagInfoFactory = new TagInfoFactory();
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

    @Deprecated
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
    public void init(Context context, List<GtdDeedEntity> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener,
                     TextClickableSpan.ITextSpanClickListener spanClickListener) {
        init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        this.spanClickListener = spanClickListener;
    }

    public boolean isShowLifeInfo() {
        return showLifeInfo;
    }

    public void setShowLifeInfo(boolean showLifeInfo) {
        this.showLifeInfo = showLifeInfo;
    }

    class SimpleDeedItemAdapter extends BaseItemAdapter<GtdDeedEntity>{
        SimpleDeedItemAdapter(List<GtdDeedEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, GtdDeedEntity item) {
            ((TextView)helper.getView(R.id.view_recycler_simple_deed_title)).setMovementMethod(LinkMovementMethod.getInstance());

            int position  = helper.getLayoutPosition();

            SimpleMultiSpanTag multiSpanTag = getTagInfo(item);
            DeedSpanMoreTag moreTag         = new DeedSpanMoreTag(item);
            multiSpanTag.setShowBracket(true);
            moreTag.setShowBracket(true);

            Spanny spanny = new Spanny();
            if(multiSpanTag.getTagTitle().isPresent())
                spanny.append(multiSpanTag.getTagTitle().get(), new ForegroundColorSpan(Color.GREEN));
            spanny.append(item.getTitle().trim(), new TextClickableSpan(item, spanClickListener, Color.WHITE,false, position));
            if(moreTag.getTagTitle().isPresent())
                spanny.append(moreTag.getTagTitle().get(), new TextClickableSpan(moreTag, spanClickListener, MyRInfo.getColorByID(R.color.pink),false, position));
            helper.setText(R.id.view_recycler_simple_deed_title, spanny);
        }

        private SimpleMultiSpanTag getTagInfo(@NonNull GtdDeedEntity item){
            SimpleMultiSpanTag multiSpanTag = new SimpleMultiSpanTag();
            Optional<String> currTagOptional = tagInfoFactory.getTagTitle(item.getDeedState());
            if(currTagOptional.isPresent())
                multiSpanTag.appendTag(currTagOptional.get());
            if(item.getGtdLifeDetail().isPresent() && showLifeInfo)
                multiSpanTag.appendTag(item.getGtdLifeDetail().get());
            return multiSpanTag;
        }
    }
}

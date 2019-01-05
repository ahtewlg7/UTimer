package ahtewlg7.utimer.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import java.util.List;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.entity.md.EditElement;

public class EditLinerRecyclerView extends ABaseLinearRecyclerView<EditElement> {
    public static final String TAG = EditLinerRecyclerView.class.getSimpleName();

    public EditLinerRecyclerView(Context context) {
        super(context);
        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
    }

    public EditLinerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
    }

    public EditLinerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDescendantFocusability(FOCUS_BEFORE_DESCENDANTS);
    }

    @Override
    public @LayoutRes int getViewItemLayout() {
        return R.layout.view_md_edit;
    }

    @Override
    public void init(Context context, List<EditElement> entityList, BaseQuickAdapter.OnItemClickListener itemClickListener, BaseQuickAdapter.OnItemChildClickListener itemChildClickListener, BaseQuickAdapter.OnItemLongClickListener itemLongClickListener, BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener, OnItemSwipeListener itemSwipeListener, OnItemDragListener itemDragListener) {
        super.init(context, entityList, itemClickListener, itemChildClickListener, itemLongClickListener, itemChildLongClickListener, itemSwipeListener, itemDragListener);
        setLayoutManager(new LinearLayoutManager(context));
    }

    @NonNull
    @Override
    public BaseItemAdapter<EditElement> createAdapter(List<EditElement> entityList) {
        return new ElementLinearItemAdapter(entityList);
    }

    public class ElementLinearItemAdapter extends BaseItemAdapter<EditElement>{
        public ElementLinearItemAdapter(List<EditElement> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, EditElement item) {
            helper.setText(R.id.view_md_edit_tv, item.getMdCharSequence())
                .addOnClickListener(R.id.view_md_edit_tv);
        }
    }
}

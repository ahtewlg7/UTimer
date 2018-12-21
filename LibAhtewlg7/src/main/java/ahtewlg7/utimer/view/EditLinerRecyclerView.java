package ahtewlg7.utimer.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
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

    private ElementLinearItemAdapter recyclerViewAdapter;

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
    public void init(Context context, List<EditElement> entityList) {
        init(context,entityList,null,null,null,null);
    }

    @Override
    public void init(Context context, List<EditElement> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener) {
        recyclerViewAdapter = new ElementLinearItemAdapter(context,entityList);
        recyclerViewAdapter.toSetOnItemClickListener(itemClickListener);
        recyclerViewAdapter.toSetOnItemChildClickListener(itemChildClickListener);
        recyclerViewAdapter.toSetOnItemSwipeListener(itemSwipeListener);
        recyclerViewAdapter.toSetOnItemDragListener(this, itemDragListener);
        recyclerViewAdapter.bindToRecyclerView(this);
        setLayoutManager(new LinearLayoutManager(context));
        setAdapter(recyclerViewAdapter);
    }

    @Override
    public void resetData(List<EditElement> entityList) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.setNewData(entityList);
    }

    @Override
    public void resetData(int index, EditElement entity) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.setData(index, entity);
    }

    @Override
    public void resetData(int index, List<EditElement> entityList) {
        if(recyclerViewAdapter != null){
            recyclerViewAdapter.remove(index);
            recyclerViewAdapter.addData(index, entityList);
        }
    }

    public class ElementLinearItemAdapter extends BaseItemAdapter<EditElement>{
        public ElementLinearItemAdapter(Context context, List<EditElement> dataList){
            super(context, dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, EditElement item) {
            helper.setText(R.id.view_md_edit_tv, item.getMdCharSequence())
                .addOnClickListener(R.id.view_md_edit_tv);
        }
    }
}

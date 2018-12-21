package ahtewlg7.utimer.view;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.callback.ItemDragAndSwipeCallback;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;

import java.util.List;

/**
 * Created by lw on 2018/3/12.
 *
 * O : The Original Entity
 * S : The SectionView Entity
 */

public abstract class ABaseLinearRecyclerView<T> extends RecyclerView{
    public static final String TAG = ABaseLinearRecyclerView.class.getSimpleName();

    public abstract @LayoutRes int getViewItemLayout();
    public abstract void init(Context context, List<T> entityList);
    public abstract void init(Context context, List<T> entityList,
                              BaseQuickAdapter.OnItemClickListener itemClickListener,
                              BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                              OnItemSwipeListener itemSwipeListener,
                              OnItemDragListener itemDragListener);
    public abstract void resetData(List<T> entityList);
    public abstract void resetData(int index, T entity);
    public abstract void resetData(int index, List<T> entityList);

    public ABaseLinearRecyclerView(Context context) {
        super(context);
    }

    public ABaseLinearRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ABaseLinearRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public BaseItemAdapter getAdapter() {
        return (BaseItemAdapter)super.getAdapter();
    }

    public abstract class BaseItemAdapter<K> extends BaseItemDraggableAdapter<K,BaseViewHolder> {
        protected ItemDragAndSwipeCallback mItemDragAndSwipeCallback;
        protected ItemTouchHelper mItemTouchHelper;

        public BaseItemAdapter(Context context, List<K> dataList){
            super(getViewItemLayout(), dataList);
        }

        public void toSetOnItemClickListener(BaseQuickAdapter.OnItemClickListener itemClickListener){
            if(itemClickListener != null)
                setOnItemClickListener(itemClickListener);
        }
        public void toSetOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener itemChildClickListener){
            if(itemChildClickListener != null)
                setOnItemChildClickListener(itemChildClickListener);
        }
        public void toSetOnItemSwipeListener(OnItemSwipeListener itemSwipeListener){
            if(itemSwipeListener != null) {
                enableSwipeItem();
                setOnItemSwipeListener(itemSwipeListener);
            }
        }
        public void toSetOnItemDragListener(RecyclerView recyclerView,OnItemDragListener itemDragListener){
            if(itemDragListener != null) {
                mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(this);
                mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
                mItemTouchHelper.attachToRecyclerView(recyclerView);
                enableDragItem(mItemTouchHelper);
                setOnItemDragListener(itemDragListener);
            }
        }
    }
}

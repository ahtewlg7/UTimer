package ahtewlg7.utimer.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;

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

public abstract class ABaseLinearRecyclerView<T,K extends BaseViewHolder> extends ABaseDeedRecyclerView<T,K> {
    protected BaseItemAdapter recyclerViewAdapter;

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
    @Override
    public void init(Context context, List<T> entityList) {
        init(context,entityList,null,null,null,null,null,null,null);
    }
    @Override
    public void init(Context context, List<T> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener) {
        recyclerViewAdapter = (BaseItemAdapter)createAdapter(entityList);
        recyclerViewAdapter.toSetOnItemClickListener(itemClickListener);
        recyclerViewAdapter.toSetOnItemChildClickListener(itemChildClickListener);
        recyclerViewAdapter.toSetOnItemLongClickListener(itemLongClickListener);
        recyclerViewAdapter.toSetOnItemChildLongClickListener(itemChildLongClickListener);
        recyclerViewAdapter.toSetOnItemSwipeListener(itemSwipeListener);
        recyclerViewAdapter.toSetOnItemDragListener(itemDragListener);
        recyclerViewAdapter.bindToRecyclerView(this);
        setAdapter(recyclerViewAdapter);
    }
    public void init(Context context, List<T> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     IOnItemTouchListener itemTouchListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener) {
        recyclerViewAdapter = (BaseItemAdapter)createAdapter(entityList);
        recyclerViewAdapter.toSetOnItemClickListener(itemClickListener);
        recyclerViewAdapter.toSetOnItemChildClickListener(itemChildClickListener);
        recyclerViewAdapter.toSetOnItemLongClickListener(itemLongClickListener);
        recyclerViewAdapter.toSetOnItemChildLongClickListener(itemChildLongClickListener);
        recyclerViewAdapter.toSetOnItemTouchListener(itemTouchListener);
        recyclerViewAdapter.toSetOnItemSwipeListener(itemSwipeListener);
        recyclerViewAdapter.toSetOnItemDragListener(itemDragListener);
        recyclerViewAdapter.bindToRecyclerView(this);
        setAdapter(recyclerViewAdapter);
    }

    @Override
    public List<T> getData() {
        if(recyclerViewAdapter == null)
            return null;
        return recyclerViewAdapter.getData();
    }

    @Override
    public T getItem(int index) {
        if(recyclerViewAdapter == null)
            return null;
        return recyclerViewAdapter.getItem(index);
    }

    @Override
    public void notifyDataSetChanged(){
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.notifyDataSetChanged();
    }
    @Override
    public void resetData(List<T> entityList) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.setNewData(entityList);
    }
    @Override
    public void resetData(int index, T entity) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.setData(index, entity);
    }
    @Override
    public void resetData(int index, List<T> entityList) {
        if(recyclerViewAdapter != null){
            recyclerViewAdapter.remove(index);
            recyclerViewAdapter.addData(index, entityList);
        }
    }
    @Override
    public void removeData(int index) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.remove(index);
    }
    @Override
    public void removeData(T t) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.remove(t);
    }

    public abstract class BaseItemAdapter extends BaseItemDraggableAdapter<T,K> {
        protected ItemTouchHelper mItemTouchHelper;
        protected IOnItemTouchListener mItemTouchListener;
        protected ItemDragAndSwipeCallback mItemDragAndSwipeCallback;

        public BaseItemAdapter(List<T> dataList){
            super(getViewItemLayout(), dataList);
        }

        public void remove(T t){
            List<T> dataList = getData();
            if(dataList.contains(t))
                remove(dataList.indexOf(t));
        }

        public void toSetOnItemClickListener(BaseQuickAdapter.OnItemClickListener itemClickListener){
            if(itemClickListener != null)
                setOnItemClickListener(itemClickListener);
        }
        public void toSetOnItemChildClickListener(BaseQuickAdapter.OnItemChildClickListener itemChildClickListener){
            if(itemChildClickListener != null)
                setOnItemChildClickListener(itemChildClickListener);
        }
        public void toSetOnItemLongClickListener(BaseQuickAdapter.OnItemLongClickListener itemLongClickListener){
            if(itemLongClickListener != null)
                setOnItemLongClickListener(itemLongClickListener);
        }
        public void toSetOnItemChildLongClickListener(BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener){
            if(itemChildLongClickListener != null)
                setOnItemChildLongClickListener(itemChildLongClickListener);
        }
        public void toSetOnItemTouchListener(IOnItemTouchListener itemTouchListener){
            if(itemTouchListener != null)
                mItemTouchListener = itemTouchListener;
        }
        public void toSetOnItemSwipeListener(OnItemSwipeListener itemSwipeListener){
            if(itemSwipeListener != null) {
                enableSwipeItem();
                setOnItemSwipeListener(itemSwipeListener);
            }
        }

        public void toSetOnItemDragListener(OnItemDragListener itemDragListener){
            if(itemDragListener != null) {
                mItemDragAndSwipeCallback = new ItemDragAndSwipeCallback(this);
                mItemTouchHelper = new ItemTouchHelper(mItemDragAndSwipeCallback);
                mItemTouchHelper.attachToRecyclerView(getRecyclerView());
                enableDragItem(mItemTouchHelper);
                setOnItemDragListener(itemDragListener);
            }
        }
    }

    public interface IOnItemTouchListener {
        void onItemTouch(View view, int position);
    }
}

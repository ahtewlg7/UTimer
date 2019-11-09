package ahtewlg7.utimer.view;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import ahtewlg7.utimer.entity.view.BaseSectionEntity;

/**
 * Created by lw on 2018/3/12.
 *
 * O : The Original Entity
 * S : The SectionView Entity
 */

public abstract class ABaseSectionRecyclerView<T extends BaseSectionEntity, K extends BaseViewHolder> extends ABaseDeedRecyclerView<T, K> {
    public abstract int getViewHeadLayout();

    protected BaseSectionAdapter recyclerViewAdapter;

    public ABaseSectionRecyclerView(Context context) {
        super(context);
    }

    public ABaseSectionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ABaseSectionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void init(Context context, List<T> entityList) {
        init(context,0, entityList,null,null,null,null);
    }

    public void init(Context context, int columnNum, List<T> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener) {
        recyclerViewAdapter = (BaseSectionAdapter)createAdapter(entityList);
        recyclerViewAdapter.toSetOnItemClickListener(itemClickListener);
        recyclerViewAdapter.toSetOnItemChildClickListener(itemChildClickListener);
        recyclerViewAdapter.toSetOnItemLongClickListener(itemLongClickListener);
        recyclerViewAdapter.toSetOnItemChildLongClickListener(itemChildLongClickListener);
        recyclerViewAdapter.bindToRecyclerView(this);
        setLayoutManager(new GridLayoutManager(context, columnNum == 0 ? 1 : columnNum));
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
    public BaseSectionAdapter getAdapter() {
        return (BaseSectionAdapter)super.getAdapter();
    }

    public abstract class BaseSectionAdapter extends BaseSectionQuickAdapter<T, K>{
        public BaseSectionAdapter(List<T> dataList){
            super(getViewItemLayout(), getViewHeadLayout(), dataList);
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
    }
}

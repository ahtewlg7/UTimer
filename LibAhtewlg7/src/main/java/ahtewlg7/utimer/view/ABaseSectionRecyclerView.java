package ahtewlg7.utimer.view;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.AttributeSet;

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

public abstract class ABaseSectionRecyclerView<K extends BaseSectionEntity> extends RecyclerView{
    public static final String TAG = ABaseSectionRecyclerView.class.getSimpleName();

    protected BaseSectionAdapter recyclerViewAdapter;

    public abstract int getViewItemLayout();
    public abstract int getViewHeadLayout();
    public abstract @NonNull BaseSectionAdapter createAdapter(List<K> entityList);

    public ABaseSectionRecyclerView(Context context) {
        super(context);
    }

    public ABaseSectionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ABaseSectionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }
    public void init(Context context, List<K> entityList) {
        init(context,0, entityList,null,null,null,null);
    }

    public void init(Context context, int columnNum, List<K> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener) {
        recyclerViewAdapter = createAdapter(entityList);
        recyclerViewAdapter.toSetOnItemClickListener(itemClickListener);
        recyclerViewAdapter.toSetOnItemChildClickListener(itemChildClickListener);
        recyclerViewAdapter.toSetOnItemLongClickListener(itemLongClickListener);
        recyclerViewAdapter.toSetOnItemChildLongClickListener(itemChildLongClickListener);
        recyclerViewAdapter.bindToRecyclerView(this);
        setLayoutManager(new GridLayoutManager(context, columnNum == 0 ? 2 : columnNum));
        setAdapter(recyclerViewAdapter);
    }

    public void resetData(List<K> entityList) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.setNewData(entityList);
    }

    public void resetData(int index, K entity) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.setData(index, entity);
    }

    public void resetData(int index, List<K> entityList) {
        if(recyclerViewAdapter != null){
            recyclerViewAdapter.remove(index);
            recyclerViewAdapter.addData(index, entityList);
        }
    }

    public void removeData(int index) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.remove(index);
    }

    @Override
    public BaseSectionAdapter getAdapter() {
        return (BaseSectionAdapter)super.getAdapter();
    }

    public abstract class BaseSectionAdapter extends BaseSectionQuickAdapter<K,BaseViewHolder>{
        public BaseSectionAdapter(List<K> dataList){
            this(dataList, 0);
        }

        public BaseSectionAdapter(List<K> dataList, int columnNum){
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

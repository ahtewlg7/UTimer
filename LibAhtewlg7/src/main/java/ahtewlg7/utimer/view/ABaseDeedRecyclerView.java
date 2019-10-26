package ahtewlg7.utimer.view;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.google.common.base.Optional;

import java.util.List;

/**
 * Created by lw on 2018/3/12.
 *
 * O : The Original Entity
 * S : The SectionView Entity
 */

public abstract class ABaseDeedRecyclerView<T,K extends BaseViewHolder> extends RecyclerView{
    protected IDeedSpanner spanner;
    protected Optional<Integer> highLightPosition;

    public ABaseDeedRecyclerView(Context context) {
        super(context);
    }

    public ABaseDeedRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ABaseDeedRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public abstract int getViewItemLayout();
    public abstract @NonNull BaseQuickAdapter<T, K> createAdapter(List<T> entityList);
    public abstract void init(Context context, List<T> entityList);
    public abstract void init(Context context, List<T> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     BaseQuickAdapter.OnItemLongClickListener itemLongClickListener,
                     BaseQuickAdapter.OnItemChildLongClickListener itemChildLongClickListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener);

    public abstract List<T> getData();
    public abstract T getItem(int index);
    public abstract void notifyDataSetChanged();
    public abstract void resetData(List<T> entityList);
    public abstract void resetData(int index, T entity);
    public abstract void resetData(int index, List<T> entityList);
    public abstract void removeData(int index);
    public abstract void removeData(T t);


    public void setSpanner(IDeedSpanner spanner) {
        this.spanner = spanner;
    }

    public void toHighLight(Optional<Integer> highLightPosition) {
        this.highLightPosition = highLightPosition;
        notifyDataSetChanged();
    }
    protected SpannableStringBuilder toSpan(T item, int position){
        SpannableStringBuilder tmp = null;
        if(spanner != null){
            if(highLightPosition != null && highLightPosition.isPresent() && highLightPosition.get() == position)
                tmp = spanner.toSpan(position, true, item);
            else
                tmp = spanner.toSpan(position, false, item);
        }
        return tmp;
    }

    public interface IDeedSpanner<T>{
        public @NonNull SpannableStringBuilder toSpan(int position, boolean highLight, @NonNull T item);
    }
}

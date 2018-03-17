package ahtewlg7.utimer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseSectionQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * Created by lw on 2018/3/12.
 *
 * O : The Original Entity
 * S : The SectionView Entity
 */

public abstract class ABaseSectionRecyclerView extends RecyclerView{
    public static final String TAG = ABaseSectionRecyclerView.class.getSimpleName();

    public abstract int getViewItemLayout();
    public abstract int getViewHeadLayout();
    public abstract void init(Context context, List<BaseSectionEntity> entityList,
                              BaseQuickAdapter.OnItemClickListener itemClickListener,
                              BaseQuickAdapter.OnItemChildClickListener itemChildClickListener);
    public abstract void init(Context context, List<BaseSectionEntity> entityList);

    public ABaseSectionRecyclerView(Context context) {
        super(context);
    }

    public ABaseSectionRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ABaseSectionRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public abstract class BaseSectionAdapter<K extends BaseSectionEntity> extends BaseSectionQuickAdapter<K,BaseViewHolder>{
        public BaseSectionAdapter(List<K> dataList){
            super(getViewItemLayout(), getViewHeadLayout(), dataList);
        }
    }
}

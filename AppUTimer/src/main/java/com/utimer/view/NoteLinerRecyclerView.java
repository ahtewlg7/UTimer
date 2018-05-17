package com.utimer.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.ABaseLinearRecyclerView;

public class NoteLinerRecyclerView extends ABaseLinearRecyclerView<INoteEntity> {
    public static final String TAG = NoteLinerRecyclerView.class.getSimpleName();

    private List<INoteEntity> noteEntityList;

    private NoteLinearItemAdapter recyclerViewAdapter;

    public NoteLinerRecyclerView(Context context) {
        super(context);
    }

    public NoteLinerRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NoteLinerRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public int getViewItemLayout() {
        return R.layout.view_recycler_note_item;
    }

    @Override
    public void init(Context context, List<INoteEntity> entityList) {
        init(context,entityList,null,null,null,null);
    }

    @Override
    public void init(Context context, List<INoteEntity> entityList,
                     BaseQuickAdapter.OnItemClickListener itemClickListener,
                     BaseQuickAdapter.OnItemChildClickListener itemChildClickListener,
                     OnItemSwipeListener itemSwipeListener,
                     OnItemDragListener itemDragListener) {
        noteEntityList = entityList;
        recyclerViewAdapter = new NoteLinearItemAdapter(context,entityList);
        recyclerViewAdapter.toSetOnItemClickListener(itemClickListener);
        recyclerViewAdapter.toSetOnItemChildClickListener(itemChildClickListener);
        recyclerViewAdapter.toSetOnItemSwipeListener(itemSwipeListener);
        recyclerViewAdapter.toSetOnItemDragListener(this,itemDragListener);
        setAdapter(recyclerViewAdapter);
    }

    @Override
    public void resetData(List<INoteEntity> entityList) {
        if(recyclerViewAdapter != null)
            recyclerViewAdapter.setNewData(entityList);
    }

    public class NoteLinearItemAdapter extends BaseItemAdapter<INoteEntity>{
        public NoteLinearItemAdapter(Context context, List<INoteEntity> dataList){
            super(context, dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, INoteEntity item) {
            helper.setText(R.id.view_recycler_note_title, MyRInfo.getStringByID(R.string.note_title) + item.getTitle());
            helper.setText(R.id.view_recycler_note_name, MyRInfo.getStringByID(R.string.note_name) + item.getFileRPath() + item.getNoteName());
            helper.setText(R.id.view_recycler_note_detail, MyRInfo.getStringByID(R.string.note_detail) + item.getDetail());
        }
    }
}

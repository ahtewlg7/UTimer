package com.utimer.view.un;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.chad.library.adapter.base.BaseViewHolder;
import com.utimer.R;

import java.util.List;

import ahtewlg7.utimer.entity.NoteEntity;
import ahtewlg7.utimer.util.MyRInfo;
import ahtewlg7.utimer.view.ABaseLinearRecyclerView;

public class NoteLinerRecyclerView extends ABaseLinearRecyclerView<NoteEntity> {
    public static final String TAG = NoteLinerRecyclerView.class.getSimpleName();

    private List<NoteEntity> noteEntityList;

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

    @NonNull
    @Override
    public BaseItemAdapter<NoteEntity> createAdapter(List<NoteEntity> entityList) {
        return new NoteLinearItemAdapter(entityList);
    }

    public class NoteLinearItemAdapter extends BaseItemAdapter<NoteEntity>{
        public NoteLinearItemAdapter(List<NoteEntity> dataList){
            super(dataList);
        }

        @Override
        protected void convert(BaseViewHolder helper, NoteEntity item) {
            helper.setText(R.id.view_recycler_note_title, MyRInfo.getStringByID(R.string.note_title) + item.getTitle());
            helper.setText(R.id.view_recycler_note_name, MyRInfo.getStringByID(R.string.note_name) + item.getFileRPath() + item.getNoteName());
            helper.setText(R.id.view_recycler_note_detail, MyRInfo.getStringByID(R.string.note_detail) + item.getDetail());
        }
    }
}

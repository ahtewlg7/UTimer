package com.utimer.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.StringRes;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.common.collect.Lists;
import com.utimer.R;

import java.util.List;
import java.util.Set;

import ahtewlg7.utimer.enumtype.DeedState;
import ahtewlg7.utimer.util.AndrManagerFactory;

/**
 * Created by lw on 2019/7/3.
 */
public class DeedTagBottomSheetDialog extends BottomSheetDialog {
    public static final int DEFAULT_SPAN_COUNT = 5;

    private int entityIndex;
    private TagAdapter tagAdapter;
    private OnItemClickListener onItemClickListener;

    public DeedTagBottomSheetDialog(@NonNull Context context) {
        super(context);
        initView();
    }

    public DeedTagBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
        initView();
    }

    public DeedTagBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void toShow(@NonNull Set<DeedState> deedStateSet, int entityIndex){
        this.entityIndex = entityIndex;

        View view = new AndrManagerFactory().getLayoutInflater().inflate(R.layout.layout_bottom_sheet_more,null);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.layout_bottom_sheet_more_recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), DEFAULT_SPAN_COUNT));
        tagAdapter = new TagAdapter(getContext(), getTagView(deedStateSet));
        recyclerView.setAdapter(tagAdapter);
        setContentView(view);
        show();
    }

    private void initView(){
        setCancelable(true);
        setCanceledOnTouchOutside(true);
    }
    private List<TagViewEntity> getTagView(@NonNull Set<DeedState> deedStateSet){
        List<TagViewEntity> tagViewEntityList = Lists.newArrayList();
        for(DeedState state : deedStateSet)
            tagViewEntityList.add(new TagViewEntity(state));
        return tagViewEntityList;
    }
    class TagAdapter extends RecyclerView.Adapter{
        private Context mContext;
        private List<TagViewEntity> mEntityList;

        public TagAdapter(Context context, List<TagViewEntity> entityList){
            this.mContext = context;
            this.mEntityList = entityList;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(mContext).inflate(R.layout.view_recycler_item_gtd_tag, parent, false);
            return new TagViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
            TagViewEntity entity = mEntityList.get(position);
            ((TagViewHolder)holder).titleView.setText(entity.getStrRid());
            ((TagViewHolder)holder).titleView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null)
                        onItemClickListener.onTagClick(entityIndex, entity.getDeedState());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mEntityList.size();
        }
    }
    class TagViewHolder extends RecyclerView.ViewHolder{
        private TextView titleView;

        public TagViewHolder(View itemView) {
            super(itemView);
            titleView = (TextView) itemView.findViewById(R.id.view_recycler_item_gtd_tag_title);
        }
    }

    class TagViewEntity {
        DeedState deedState;
        @StringRes int strRid;

        TagViewEntity(DeedState deedState) {
            this.deedState = deedState;
            this.strRid    = getStrRid(deedState);
        }
        TagViewEntity(DeedState deedState, int strRid) {
            this.deedState = deedState;
            this.strRid = strRid;
        }

        DeedState getDeedState() {
            return deedState;
        }

        @StringRes int getStrRid() {
            return strRid;
        }

        @StringRes int getStrRid(DeedState deedState){
            int tmp = 0;
            switch(deedState){
                case TWO_MIN:
                    tmp = R.string.title_deed_tag_two_min;
                    break;
                case DEFER:
                    tmp = R.string.title_deed_tag_defer;
                    break;
                case DELEGATE:
                    tmp = R.string.title_deed_tag_delegate;
                    break;
                case WISH:
                    tmp = R.string.title_deed_tag_wish;
                    break;
                case REFERENCE:
                    tmp = R.string.title_deed_tag_reference;
                    break;
                case PROJECT:
                    tmp = R.string.title_deed_tag_project;
                    break;
                case DONE:
                    tmp = R.string.title_deed_tag_done;
                    break;
                case USELESS:
                    tmp = R.string.title_deed_tag_useless;
                    break;
                case TRASH:
                    tmp = R.string.title_deed_tag_trash;
                    break;
                default:
                    //todo
                    break;
            }
            return tmp;
        }
    }

    public interface OnItemClickListener {
        void onTagClick(int position, DeedState deedState);
    }
}


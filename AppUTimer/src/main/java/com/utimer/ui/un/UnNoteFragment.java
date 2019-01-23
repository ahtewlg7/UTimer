package com.utimer.ui.un;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemDragListener;
import com.chad.library.adapter.base.listener.OnItemSwipeListener;
import com.jakewharton.rxbinding2.view.RxView;
import com.trello.rxlifecycle2.components.support.RxFragment;
import com.utimer.R;
import com.utimer.view.un.NoteLinerRecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.entity.gtd.un.NoteEntity;
import ahtewlg7.utimer.mvp.un.NoteRecyclerViewMvpP;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.MySimpleItemDragListener;
import ahtewlg7.utimer.util.MySimpleItemSwipeListener;
import ahtewlg7.utimer.util.MySimpleObserver;
import butterknife.BindView;

/**
 * Created by lw on 2018/1/24.
 */

public class UnNoteFragment extends UnAFunctionFragement
        implements NoteRecyclerViewMvpP.INoteRecyclerViewMvpV {
    public static final String TAG = UnNoteFragment.class.getSimpleName();

    @BindView(R.id.fragment_note_new)
    Button newBtn;
    @BindView(R.id.fragment_note_recyclerview)
    NoteLinerRecyclerView noteLinerRecyclerView;

    private NoteRecyclerViewMvpP noteRecylerViewMvpP;
    private NoteOnItemClickListener noteOnItemClickListener;
    private NoteOnItemChildClickListener noteOnItemChildClickListener;
    private OnItemSwipeListener noteOnItemSwipeListener;
    private OnItemDragListener noteOnItemDragListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteRecylerViewMvpP             = new NoteRecyclerViewMvpP(this);
        noteOnItemClickListener         = new NoteOnItemClickListener();
        noteOnItemChildClickListener    = new NoteOnItemChildClickListener();
        noteOnItemSwipeListener         = new NoteOnItemSwipeListener();
        noteOnItemDragListener          = new MySimpleItemDragListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        noteRecylerViewMvpP.toUnregisterEventBus();
    }

    private void initNewBtn(){
        RxView.clicks(newBtn)
            .throttleFirst(3, TimeUnit.SECONDS)
            .subscribe(new MySimpleObserver<Object>(){
                @Override
                public void onNext(Object object) {
                    ToastUtils.showShort("to create a new note!");
                    noteRecylerViewMvpP.toCreateNote();
                }
            });
    }
    //=======================================UnAFunctionFragement================================================
    @Override
    @NonNull
    public String getIndicateTitle() {
        return "note";
    }

    @Override
    public int getIndicateIconRid() {
        return R.drawable.page_indicator;
    }

    @Override
    public int getLayoutRid() {
        return R.layout.un_fragment_note;
    }

    @Override
    public void initLayoutView(View inflateView) {
        initNewBtn();

        noteRecylerViewMvpP.toRegisterEventBus();
        noteRecylerViewMvpP.loadAllData();
    }

    //=======================================INoteRecylerViewMvpV================================================
    @NonNull
    @Override
    public RxFragment getUiContext() {
        return this;
    }

    @Override
    public void toStartNoteEditActivity() {
        Logcat.i(TAG,"toStartNoteEditActivity");
        ActivityUtils.startActivity(UnMdEditorActivity.class);
    }

    @Override
    public void onNoteDeleteSucc() {
        Logcat.i(TAG,"onNoteDeleteSucc");
        ToastUtils.showShort(R.string.note_delete_succ);
    }

    @Override
    public void onNoteDeleteFail() {
        Logcat.i(TAG,"onNoteDeleteFail");
        ToastUtils.showShort(R.string.note_delete_fail);
    }

    @Override
    public void onNoteDeleteErr() {
        Logcat.i(TAG,"onNoteDeleteErr");
    }

    @Override
    public void initRecyclerView(List<NoteEntity> dataList) {
        noteLinerRecyclerView.init(getActivity(), dataList,
                noteOnItemClickListener, noteOnItemChildClickListener,null,null,
                noteOnItemSwipeListener, noteOnItemDragListener);
    }

    @Override
    public void resetRecyclerView(List<NoteEntity> dataList) {
        if(dataList == null){
            Logcat.i(TAG,"resetRecyclerView : dataList null");
            return;
        }

        noteLinerRecyclerView.resetData(dataList);
    }

    @Override
    public void onRecyclerViewInitStart() {
        //todo
    }

    @Override
    public void onRecyclerViewInitErr() {
        //todo
    }

    private class NoteOnItemClickListener implements BaseQuickAdapter.OnItemClickListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            noteRecylerViewMvpP.toEditNote((NoteEntity) adapter.getItem(position));
        }
    }
    private class NoteOnItemChildClickListener implements BaseQuickAdapter.OnItemChildClickListener{
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        }
    }

    private class NoteOnItemSwipeListener extends MySimpleItemSwipeListener {
        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
            Logcat.i(TAG,"onItemSwiped pos = " + pos);
            noteRecylerViewMvpP.toDeleteNoteItem(pos);
        }
    }
}

package com.utimer.ui;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.utimer.view.NoteLinerRecyclerView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;
import java.util.concurrent.TimeUnit;

import ahtewlg7.utimer.busevent.NoteEditEndEvent;
import ahtewlg7.utimer.busevent.NoteEditEvent;
import ahtewlg7.utimer.common.EventBusFatory;
import ahtewlg7.utimer.entity.INoteEntity;
import ahtewlg7.utimer.mvp.NoteRecylerViewMvpP;
import ahtewlg7.utimer.util.Logcat;
import butterknife.BindView;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by lw on 2018/1/24.
 */

public class NoteFragment extends AFunctionFragement
        implements NoteRecylerViewMvpP.INoteRecylerViewMvpV {
    public static final String TAG = NoteFragment.class.getSimpleName();

    @BindView(R.id.fragment_note_new)
    Button newBtn;
    @BindView(R.id.fragment_note_recyclerview)
    NoteLinerRecyclerView noteLinerRecyclerView;

    private Disposable newBtnDisposable;

    private NoteRecylerViewMvpP noteRecylerViewMvpP;
    private NoteOnItemClickListener noteOnItemClickListener;
    private NoteOnItemChildClickListener noteOnItemChildClickListener;
    private NoteOnItemSwipeListener noteOnItemSwipeListener;
    private NoteOnItemDragListener noteOnItemDragListener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        noteRecylerViewMvpP             = new NoteRecylerViewMvpP(this);
        noteOnItemClickListener         = new NoteOnItemClickListener();
        noteOnItemChildClickListener    = new NoteOnItemChildClickListener();
        noteOnItemSwipeListener         = new NoteOnItemSwipeListener();
        noteOnItemDragListener          = new NoteOnItemDragListener();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Logcat.i(TAG,"to unregister EventBus");
        EventBusFatory.getInstance().getDefaultEventBus().unregister(this);
    }

    //EventBus callback
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onNoteEditEndEvent(NoteEditEndEvent event) {
        Logcat.i(TAG,"onNoteEditEndEvent  : noteEditEndEvent = " + event.toString());
        if(event.getEoteEntityId().isPresent() && !TextUtils.isEmpty(event.getEoteEntityId().get()))
            noteRecylerViewMvpP.onNoteNew(event.getEoteEntityId().get());
    }

    private void initNewBtn(){
        newBtnDisposable = RxView.clicks(newBtn)
            .throttleFirst(3, TimeUnit.SECONDS)
            .subscribe(new Consumer<Object>() {
                @Override
                public void accept(Object o) throws Exception {
                    ToastUtils.showShort("to create a new note!");
                    noteRecylerViewMvpP.toNewNote();
                }
            });
    }
    //=======================================AFunctionFragement================================================
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
        return R.layout.fragment_note;
    }

    @Override
    public void initLayoutView(View inflateView) {
        initNewBtn();

        Logcat.i(TAG,"to register EventBus");
        EventBusFatory.getInstance().getDefaultEventBus().register(this);

        noteRecylerViewMvpP.loadAllData();
    }

    //=======================================INoteRecylerViewMvpV================================================
    @NonNull
    @Override
    public RxFragment getUiContext() {
        return this;
    }

    @Override
    public void onNoteNewStart() {
        ActivityUtils.startActivity(MdEditorActivity.class);
        EventBusFatory.getInstance().getDefaultEventBus().postSticky(new NoteEditEvent());
    }

    @Override
    public void initRecyclerView(List<INoteEntity> dataList) {
        noteLinerRecyclerView.init(getActivity(), dataList,
                noteOnItemClickListener, noteOnItemChildClickListener,
                noteOnItemSwipeListener, noteOnItemDragListener);
    }

    @Override
    public void resetRecyclerView(List<INoteEntity> dataList) {
        if(dataList == null){
            Logcat.i(TAG,"resetRecyclerView : dataList null");
            return;
        }
        noteLinerRecyclerView.resetData(dataList);
    }

    @Override
    public void onRecyclerViewInitStart() {
    }

    @Override
    public void onRecyclerViewInitErr() {

    }

    @Override
    public void onRecyclerViewInitEnd() {

    }

    private class NoteOnItemClickListener implements BaseQuickAdapter.OnItemClickListener{
        @Override
        public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

        }
    }
    private class NoteOnItemChildClickListener implements BaseQuickAdapter.OnItemChildClickListener{
        @Override
        public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {

        }
    }

    private class NoteOnItemSwipeListener implements OnItemSwipeListener {
        @Override
        public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {

        }

        @Override
        public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {

        }

        @Override
        public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {

        }

        @Override
        public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {

        }
    }
    private class NoteOnItemDragListener implements OnItemDragListener{
        @Override
        public void onItemDragStart(RecyclerView.ViewHolder viewHolder, int pos) {

        }

        @Override
        public void onItemDragMoving(RecyclerView.ViewHolder source, int from, RecyclerView.ViewHolder target, int to) {

        }

        @Override
        public void onItemDragEnd(RecyclerView.ViewHolder viewHolder, int pos) {

        }
    }
}

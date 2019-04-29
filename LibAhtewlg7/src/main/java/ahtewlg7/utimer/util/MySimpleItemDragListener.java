package ahtewlg7.utimer.util;

import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemDragListener;

public class MySimpleItemDragListener implements OnItemDragListener {
    public static final String TAG = MySimpleItemDragListener.class.getSimpleName();

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

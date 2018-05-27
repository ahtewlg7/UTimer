package ahtewlg7.utimer.util;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

import com.chad.library.adapter.base.listener.OnItemSwipeListener;

public class MySimpleItemSwipeListener implements OnItemSwipeListener {
    public static final String TAG = MySimpleItemSwipeListener.class.getSimpleName();

    @Override
    public void onItemSwipeStart(RecyclerView.ViewHolder viewHolder, int pos) {
//            Logcat.i(TAG,"onItemSwipeStart");
    }

    @Override
    public void clearView(RecyclerView.ViewHolder viewHolder, int pos) {
//            Logcat.i(TAG,"clearView");
    }

    @Override
    public void onItemSwiped(RecyclerView.ViewHolder viewHolder, int pos) {
//            Logcat.i(TAG,"onItemSwiped");
    }

    @Override
    public void onItemSwipeMoving(Canvas canvas, RecyclerView.ViewHolder viewHolder, float dX, float dY, boolean isCurrentlyActive) {
//            Logcat.i(TAG,"onItemSwipeMoving");
    }
}

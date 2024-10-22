package ahtewlg7.utimer.md;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import ahtewlg7.utimer.span.ClickableImageSpan;

/**
 * Created by lw on 2016/6/6.
 */
public class ClickableMovementMethod extends LinkMovementMethod{
    public static final String TAG = ClickableMovementMethod.class.getSimpleName();

    private static ClickableMovementMethod instance;

    private ClickableMovementMethod(){
    }

    public static ClickableMovementMethod getInstance(){
        if(instance == null)
            instance = new ClickableMovementMethod();
        return instance;
    }

    @Override
    public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
        int action = event.getAction();

        if (action == MotionEvent.ACTION_UP ||
                action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();

            x -= widget.getTotalPaddingLeft();
            y -= widget.getTotalPaddingTop();

            x += widget.getScrollX();
            y += widget.getScrollY();

            Layout layout = widget.getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);

            ClickableSpan[] link = buffer.getSpans(off, off, ClickableSpan.class);
            ClickableImageSpan[] imageSpans = buffer.getSpans(off, off, ClickableImageSpan.class);

            if (link.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    link[0].onClick(widget);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(link[0]),
                            buffer.getSpanEnd(link[0]));
                }

                return true;
            } else if (imageSpans.length != 0) {
                if (action == MotionEvent.ACTION_UP) {
                    imageSpans[0].onClick(widget);
                } else if (action == MotionEvent.ACTION_DOWN) {
                    Selection.setSelection(buffer, buffer.getSpanStart(imageSpans[0]),buffer.getSpanEnd(imageSpans[0]));
                }
                return true;
            } else {
                Selection.removeSelection(buffer);
            }
        }
        return super.onTouchEvent(widget, buffer, event);
    }
}

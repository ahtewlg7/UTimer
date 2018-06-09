package ahtewlg7.utimer.view.md;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

/**
 * Created by lw on 2016/6/6.
 */
public abstract class ClickableImageSpan extends ImageSpan {
    public static final String TAG = ClickableImageSpan.class.getSimpleName();

    public ClickableImageSpan(Drawable b) {
        super(b);
    }

    public abstract void onClick(View view);
}

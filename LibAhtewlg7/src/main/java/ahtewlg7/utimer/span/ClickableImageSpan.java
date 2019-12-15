package ahtewlg7.utimer.span;

import android.graphics.drawable.Drawable;
import android.text.style.ImageSpan;
import android.view.View;

import androidx.annotation.NonNull;

/**
 * Created by lw on 2016/6/6.
 */
public abstract class ClickableImageSpan extends ImageSpan {
    public ClickableImageSpan(Drawable b) {
        super(b);
    }

    public ClickableImageSpan(@NonNull Drawable drawable, int verticalAlignment) {
        super(drawable, verticalAlignment);
    }

    public abstract void onClick(View view);
}

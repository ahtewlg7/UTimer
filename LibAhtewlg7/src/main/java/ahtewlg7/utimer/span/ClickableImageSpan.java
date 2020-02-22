package ahtewlg7.utimer.span;

import android.graphics.Bitmap;
import android.text.style.ImageSpan;
import android.view.View;

import com.blankj.utilcode.util.Utils;

/**
 * Created by lw on 2016/6/6.
 */
public abstract class ClickableImageSpan extends ImageSpan {
    public ClickableImageSpan(Bitmap b) {
        super(Utils.getApp(),b);
    }

    public abstract void onClick(View view);
}

package ahtewlg7.utimer.span;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;

import static android.widget.AdapterView.INVALID_POSITION;


/**
 * Created by lw on 2019/7/2.
 */
public class TextClickableSpan extends ClickableSpan {
    private @ColorInt Integer linkColor;
    private int position = INVALID_POSITION;
    private Object target;
    private Boolean underlineFlag;
    private ITextSpanClickListener spanClickListener;

    public TextClickableSpan(ITextSpanClickListener spanClickListener) {
        this.spanClickListener = spanClickListener;
    }

    public TextClickableSpan(Object target, ITextSpanClickListener spanClickListener) {
        this.target = target;
        this.spanClickListener = spanClickListener;
    }
    public TextClickableSpan(Object target, ITextSpanClickListener spanClickListener,
                             @ColorInt Integer linkColor, Boolean underlineFlag) {
        this.target            = target;
        this.spanClickListener = spanClickListener;
        this.linkColor         = linkColor;
        this.underlineFlag     = underlineFlag;
    }
    public TextClickableSpan(Object target, ITextSpanClickListener spanClickListener,
                             @ColorInt Integer linkColor, Boolean underlineFlag,
                             int position) {
        this.target            = target;
        this.spanClickListener = spanClickListener;
        this.linkColor         = linkColor;
        this.underlineFlag     = underlineFlag;
        this.position          = position;
    }

    public Object getTarget() {
        return target;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        if(linkColor != null)
            ds.setColor(linkColor);
        if(underlineFlag != null)
            ds.setUnderlineText(underlineFlag);
    }

    @Override
    public void onClick(@NonNull View widget) {
        if(spanClickListener != null)
            spanClickListener.onSpanClick(position, target);
    }

    public interface ITextSpanClickListener{
        public void onSpanClick(int position, Object o);
    }
}

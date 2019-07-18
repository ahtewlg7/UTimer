package ahtewlg7.utimer.entity.span;

import android.text.TextUtils;

import com.google.common.base.Optional;

/**
 * Created by lw on 2019/7/2.
 */
public abstract class ASpanTag {
    protected boolean showBracket;

    public abstract String getTagName();

    public boolean isShowBracket() {
        return showBracket;
    }

    public void setShowBracket(boolean showBracket) {
        this.showBracket = showBracket;
    }

    //91 means '[', 93 means ']'
    public Optional<String> getTagTitle(){
        if(TextUtils.isEmpty(getTagName()))
            return Optional.absent();
        if(showBracket)
            return Optional.of((char)91 +getTagName() + (char)93);
        return Optional.of(getTagName());
    }
}

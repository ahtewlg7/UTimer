package ahtewlg7.utimer.entity.span;

import androidx.annotation.StringRes;

/**
 * Created by lw on 2019/7/2.
 */
public abstract class ASpanTag {
    public abstract @StringRes int getTagRid();
    public abstract String getTagName();
    public String getTagTitle(){
        return (char)91 +getTagName() + (char)93;
    }
}

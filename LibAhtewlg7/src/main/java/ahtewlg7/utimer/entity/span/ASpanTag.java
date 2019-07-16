package ahtewlg7.utimer.entity.span;

import android.text.TextUtils;

import com.google.common.base.Optional;

/**
 * Created by lw on 2019/7/2.
 */
public abstract class ASpanTag {
    public abstract String getTagName();

    //91 means '[', 93 means ']'
    public Optional<String> getTagTitle(){
        if(TextUtils.isEmpty(getTagName()))
            return Optional.absent();
        return Optional.of((char)91 +getTagName() + (char)93);
    }
}

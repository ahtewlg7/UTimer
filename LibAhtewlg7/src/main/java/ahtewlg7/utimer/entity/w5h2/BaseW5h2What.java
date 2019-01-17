package ahtewlg7.utimer.entity.w5h2;

import android.text.TextUtils;

/**
 * Created by lw on 2019/1/16.
 */
public class BaseW5h2What implements AW5h2What {
    public static final String TAG = BaseW5h2What.class.getSimpleName();

    private String target;

    public BaseW5h2What() {
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }


    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TAG).append("{");
        if(!TextUtils.isEmpty(target))
            builder.append("target = "+ target);
        return builder.append("}").toString();
    }
}

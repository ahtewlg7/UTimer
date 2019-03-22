package ahtewlg7.utimer.entity.w5h2;

import android.text.TextUtils;

/**
 * Created by lw on 2019/1/16.
 */
public class W5h2What {
    private String target;
    private String behavior;

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getBehavior() {
        return behavior;
    }

    public void setBehavior(String behavior) {
        this.behavior = behavior;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if(!TextUtils.isEmpty(target))
            builder.append(",").append(target);
        if(!TextUtils.isEmpty(behavior))
            builder.append(",").append(behavior);
        return builder.append("}").toString();
    }
}

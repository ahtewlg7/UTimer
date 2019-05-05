package ahtewlg7.utimer.entity.context;

import android.text.TextUtils;

/**
 * Created by lw on 2019/5/4.
 */
public class DeedContext implements IContext{
    private String deed;

    public DeedContext(String deed) {
        this.deed = deed;
    }

    public String getName() {
        return deed;
    }

    @Override
    public boolean ifValid() {
        return !TextUtils.isEmpty(deed);
    }
}

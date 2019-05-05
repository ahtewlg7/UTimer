package ahtewlg7.utimer.entity.context;

import android.text.TextUtils;

/**
 * Created by lw on 2019/4/11.
 */
public class ContactContext implements IContext{
    private String name;
    private String alias;

    public ContactContext(String name) {
        this.name = name;
    }

    public ContactContext(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }

    @Override
    public boolean ifValid() {
        return !TextUtils.isEmpty(name);
    }
}

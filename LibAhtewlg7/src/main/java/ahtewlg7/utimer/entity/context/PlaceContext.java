package ahtewlg7.utimer.entity.context;

import android.text.TextUtils;

/**
 * Created by lw on 2019/5/4.
 */
public class PlaceContext implements IContext{
    protected String name;
    protected boolean isOrganization;

    public PlaceContext(String name){
        this.name = name;
    }

    public PlaceContext(String name, boolean isOrganization) {
        this.name = name;
        this.isOrganization = isOrganization;
    }

    public String getName() {
        return name;
    }

    public boolean isOrganization() {
        return isOrganization;
    }

    @Override
    public boolean ifValid() {
        return !TextUtils.isEmpty(name);
    }
}

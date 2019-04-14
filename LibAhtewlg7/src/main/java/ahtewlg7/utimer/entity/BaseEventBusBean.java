package ahtewlg7.utimer.entity;

/**
 * Created by lw on 2019/3/13.
 */
public class BaseEventBusBean implements IValidEntity {
    private boolean perform;

    public boolean isPerform() {
        return perform;
    }

    public void setPerform(boolean perform) {
        this.perform = perform;
    }

    @Override
    public boolean ifValid() {
        return true;
    }
}

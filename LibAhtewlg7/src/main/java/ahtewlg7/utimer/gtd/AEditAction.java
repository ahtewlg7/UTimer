package ahtewlg7.utimer.gtd;

import ahtewlg7.utimer.entity.ABaseMaterialEntity;

/**
 * Created by lw on 2018/10/24.
 */
public abstract class AEditAction<T extends ABaseMaterialEntity> {
    public static final String TAG = AEditAction.class.getSimpleName();

    public abstract boolean ifReady();
    public abstract boolean toSave(String rawTxt);

    protected T t;

    public AEditAction(T t){
        this.t = t;
    }
}

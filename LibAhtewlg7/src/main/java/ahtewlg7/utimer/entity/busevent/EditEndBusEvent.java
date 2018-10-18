package ahtewlg7.utimer.entity.busevent;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.IValidEntity;
import ahtewlg7.utimer.enumtype.EditLoadType;

/**
 * Created by lw on 2018/10/14.
 */
public class EditEndBusEvent implements IValidEntity {
    public static final String TAG = EditEndBusEvent.class.getSimpleName();

    private EditLoadType loadType;
    private AUtimerEntity utimerEntity;

    public EditEndBusEvent(EditLoadType loadType, AUtimerEntity utimerEntity) {
        this.loadType = loadType;
        this.utimerEntity = utimerEntity;
    }

    @Override
    public boolean ifValid() {
        return utimerEntity != null && utimerEntity.ifValid();
    }

    public EditLoadType getLoadType() {
        return loadType;
    }

    public AUtimerEntity getUtimerEntity() {
        return utimerEntity;
    }
}

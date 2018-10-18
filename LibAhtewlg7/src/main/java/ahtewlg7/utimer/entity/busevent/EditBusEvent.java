package ahtewlg7.utimer.entity.busevent;

import ahtewlg7.utimer.entity.AUtimerEntity;

/**
 * Created by lw on 2018/10/14.
 */
public class EditBusEvent {
    public static final String TAG = EditBusEvent.class.getSimpleName();

    private AUtimerEntity utimerEntity;

    public EditBusEvent(AUtimerEntity utimerEntity) {
        this.utimerEntity = utimerEntity;
    }


    public AUtimerEntity getUtimerEntity() {
        return utimerEntity;
    }
}

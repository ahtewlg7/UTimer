package ahtewlg7.utimer.gtd;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.md.EditElement;
import io.reactivex.Flowable;

/**
 * Created by lw on 2018/10/24.
 */
public abstract class AEditAction {
    public static final String TAG = AEditAction.class.getSimpleName();

    public abstract boolean ifReady();
    public abstract boolean toSave(String rawTxt);
    public abstract Flowable<Optional<EditElement>> toLoad();

    protected AUtimerEntity utimerEntity;

    public AEditAction(AUtimerEntity utimerEntity){
        this.utimerEntity = utimerEntity;
    }
}

package ahtewlg7.utimer.state;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import ahtewlg7.utimer.entity.gtd.GtdDeedEntity;
import ahtewlg7.utimer.enumtype.DeedState;

/**
 * Created by lw on 2019/4/6.
 */
public class DeedTrashState extends DeedBaseState {

    DeedTrashState(GtdMachine gtdMachine) {
        super(gtdMachine);
    }
    @Override
    public Optional<BaseEventBusBean> toEdit(@NonNull GtdDeedEntity entity, String title, String detail){
        return Optional.absent();
    }
    @Override
    public Optional<BaseEventBusBean> toTrash(@NonNull AUtimerEntity entity) {
        return removeState(entity);
    }

    @Override
    protected boolean ifTrashable(AUtimerEntity entity) {
        return super.ifTrashable(entity)
            && ((GtdDeedEntity)entity).getDeedState() == DeedState.TRASH;
    }
}

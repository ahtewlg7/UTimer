package ahtewlg7.utimer.state;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;

/**
 * Created by lw on 2019/1/19.
 */
public class BaseGtdState {
    protected GtdMachine gtdMachine;

    BaseGtdState(@NonNull GtdMachine gtdMachine){
        this.gtdMachine = gtdMachine;
    }

    public Optional<BaseEventBusBean> toTrash(@NonNull AUtimerEntity entityRx){
        return Optional.absent();
    }
    public Optional<BaseEventBusBean> toGtd(@NonNull AUtimerEntity entityRx){
        return Optional.absent();
    }

    protected GtdMachine getGtdMachine(){
        return gtdMachine;
    }
}

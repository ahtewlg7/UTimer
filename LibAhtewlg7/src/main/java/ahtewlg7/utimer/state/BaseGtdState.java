package ahtewlg7.utimer.state;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;

/**
 * Created by lw on 2019/1/19.
 */
public class BaseGtdState<E extends BaseEventBusBean> {
    public Optional<E> toTrash(@NonNull AUtimerEntity entityRx){
        return Optional.absent();
    }
}

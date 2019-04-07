package ahtewlg7.utimer.state;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.BaseEventBusBean;
import io.reactivex.Flowable;

/**
 * Created by lw on 2019/4/6.
 */
public interface IGtdState<E extends BaseEventBusBean>  {

    public Flowable<Optional<E>> toTrash(@NonNull Flowable<AUtimerEntity> entityRx);
}

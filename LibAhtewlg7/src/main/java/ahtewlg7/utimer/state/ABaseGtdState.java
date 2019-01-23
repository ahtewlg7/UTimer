package ahtewlg7.utimer.state;

import android.support.annotation.Nullable;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.util.Logcat;
import io.reactivex.annotations.NonNull;

/**
 * Created by lw on 2019/1/19.
 */
public abstract class ABaseGtdState implements IGtdState {
    public static final String TAG = ABaseGtdState.class.getSimpleName();

    private AUtimerEntity uTimerEntity;

    public ABaseGtdState(@NonNull AUtimerEntity uTimerEntity) {
        this.uTimerEntity = uTimerEntity;
    }

    public AUtimerEntity getUTimerEntity(){
        return uTimerEntity;
    }

    @Override
    public Optional<AUtimerEntity> toDailyCheck(AUtimerEntity entity) {
        Logcat.i(TAG,"toDailyCheck cancel");
        return Optional.absent();
    }

    @Override
    public Optional<AUtimerEntity> toNote(@Nullable AUtimerEntity entity) {
        Logcat.i(TAG,"toNote cancel");
        return Optional.absent();
    }
}

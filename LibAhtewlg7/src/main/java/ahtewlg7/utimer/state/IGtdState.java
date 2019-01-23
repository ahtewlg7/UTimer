package ahtewlg7.utimer.state;

import android.support.annotation.Nullable;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;

/**
 * Created by lw on 2019/1/19.
 */
public interface IGtdState {
    public Optional<AUtimerEntity> toDailyCheck(AUtimerEntity entity);

    public Optional<AUtimerEntity> toNote(@Nullable AUtimerEntity entity);
}

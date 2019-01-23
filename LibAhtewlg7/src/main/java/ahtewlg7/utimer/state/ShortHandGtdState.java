package ahtewlg7.utimer.state;

import android.support.annotation.Nullable;

import com.google.common.base.Optional;

import ahtewlg7.utimer.entity.AUtimerEntity;

/**
 * Created by lw on 2019/1/19.
 */
public class ShortHandGtdState extends ABaseGtdState {
    public static final String TAG = ShortHandGtdState.class.getSimpleName();

    public ShortHandGtdState(AUtimerEntity uTimerEntity) {
        super(uTimerEntity);
    }

    @Override
    public Optional<AUtimerEntity> toNote(@Nullable AUtimerEntity entity) {
        return super.toNote(entity);
    }
}

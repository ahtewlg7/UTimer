package ahtewlg7.utimer.entity.gtd.un;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.AUtimerBuilder;

/**
 * Created by lw on 2018/10/26.
 */
public class TipsBuilder extends AUtimerBuilder<TipsEntity, TipsBuilder> {
    @NonNull
    @Override
    public TipsEntity build() {
        return new TipsEntity(this);
    }
}

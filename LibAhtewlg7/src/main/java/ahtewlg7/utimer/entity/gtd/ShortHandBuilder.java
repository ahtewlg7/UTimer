package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.db.entity.ShortHandEntityGdBean;
import ahtewlg7.utimer.entity.AUtimerBuilder;

/**
 * Created by lw on 2018/10/26.
 */
public class ShortHandBuilder extends AUtimerBuilder<ShortHandEntity, ShortHandBuilder> {
    public static final String TAG = ShortHandBuilder.class.getSimpleName();

    protected ShortHandEntityGdBean gdBean;

    public ShortHandBuilder setGbBean(ShortHandEntityGdBean gdBean){
        this.gdBean = gdBean;
        return this;
    }

    @NonNull
    @Override
    public ShortHandEntity build() {
        return new ShortHandEntity(this);
    }
}

package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;

import ahtewlg7.utimer.entity.AGtdUtimerBuilder;

/**
 * Created by lw on 2018/10/26.
 */
public class GtdProjectBuilder extends AGtdUtimerBuilder<GtdProjectEntity> {
    public static final String TAG = GtdProjectBuilder.class.getSimpleName();

    /*protected ShortHandEntityGdBean gdBean;

    public GtdProjectBuilder setGbBean(ShortHandEntityGdBean gdBean){
        this.gdBean = gdBean;
        return this;
    }*/

    @NonNull
    @Override
    public GtdProjectEntity build() {
        return new GtdProjectEntity(this);
    }
}

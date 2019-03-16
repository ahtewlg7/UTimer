package ahtewlg7.utimer.entity;

import ahtewlg7.utimer.entity.w5h2.AW5h2Entity;

/**
 * Created by lw on 2019/1/5.
 */
public abstract class AGtdUtimerBuilder<E extends AGtdUtimerEntity, K extends AGtdUtimerBuilder>
        extends AUtimerBuilder<E, K>  {
    public static final String TAG = AGtdUtimerBuilder.class.getSimpleName();

    protected AW5h2Entity w5h2Entity;

    public AGtdUtimerBuilder setW5h2Entity(AW5h2Entity w5h2Entity) {
        this.w5h2Entity = w5h2Entity;
        return this;
    }

    public AW5h2Entity getW5h2Entity() {
        return w5h2Entity;
    }
}

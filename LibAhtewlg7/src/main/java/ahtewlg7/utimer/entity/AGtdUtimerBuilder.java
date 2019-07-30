package ahtewlg7.utimer.entity;

import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;

/**
 * Created by lw on 2019/1/5.
 */
public abstract class AGtdUtimerBuilder<E extends AGtdUtimerEntity, K extends AUtimerBuilder>
        extends AUtimerBuilder<E, K>  {
    protected BaseW5h2Entity w5h2Entity;

    public K setW5h2Entity(BaseW5h2Entity w5h2Entity) {
        this.w5h2Entity = w5h2Entity;
        return (K)this;
    }

    public BaseW5h2Entity getW5h2Entity() {
        return w5h2Entity;
    }
}

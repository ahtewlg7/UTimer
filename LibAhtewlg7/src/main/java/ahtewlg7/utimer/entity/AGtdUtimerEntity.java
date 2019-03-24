package ahtewlg7.utimer.entity;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;


public abstract class AGtdUtimerEntity<T extends AGtdUtimerBuilder> extends AUtimerEntity<T>  {
    protected BaseW5h2Entity w5h2Entity;

    protected AGtdUtimerEntity(@Nonnull T builder) {
        super(builder);
        if(builder.getW5h2Entity() != null)
            w5h2Entity = builder.getW5h2Entity();
    }

    public BaseW5h2Entity getW5h2Entity(){
        return w5h2Entity;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        if(w5h2Entity != null)
            builder.append(w5h2Entity.toString());
        return builder.toString();
    }

    @Override
    public void update(IMergerEntity entity) {
        w5h2Entity = ((AGtdUtimerEntity)entity).getW5h2Entity();
    }
}

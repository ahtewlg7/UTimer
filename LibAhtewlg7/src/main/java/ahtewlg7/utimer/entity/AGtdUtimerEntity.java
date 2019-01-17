package ahtewlg7.utimer.entity;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.entity.w5h2.AW5h2Entity;


public abstract class AGtdUtimerEntity<T extends AGtdUtimerBuilder> extends AUtimerEntity<T>  {
    public static final String TAG = AGtdUtimerEntity.class.getSimpleName();

    protected AW5h2Entity w5h2Entity;

    protected AGtdUtimerEntity(@Nonnull T builder) {
        super(builder);
        if(builder.getW5h2Entity() != null)
            w5h2Entity = builder.getW5h2Entity();
    }

    public AW5h2Entity getW5h2Entity(){
        return w5h2Entity;
    }
}

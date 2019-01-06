package ahtewlg7.utimer.entity;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.entity.gtd.W5h2Entity;


public abstract class AGtdUtimerEntity<T extends AGtdUtimerBuilder> extends AUtimerEntity<T>  {
    public static final String TAG = AGtdUtimerEntity.class.getSimpleName();

    protected W5h2Entity w5h2Entity;

    protected AGtdUtimerEntity(@Nonnull T builder) {
        super(builder);
        if(builder.getW5h2Entity() != null)
            w5h2Entity = builder.getW5h2Entity();
        else
            w5h2Entity = new W5h2Entity();
    }

    public W5h2Entity getW5h2Entity(){
        return w5h2Entity;
    }
}

package ahtewlg7.utimer.entity;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;
import ahtewlg7.utimer.entity.w5h2.W5h2HowMuch;
import ahtewlg7.utimer.entity.w5h2.W5h2What;
import ahtewlg7.utimer.entity.w5h2.W5h2When;
import ahtewlg7.utimer.entity.w5h2.W5h2Who;


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

    public Optional<W5h2When> getWhen(){
        return w5h2Entity == null ? Optional.<W5h2When>absent() : Optional.fromNullable(w5h2Entity.getWhen());
    }
    public Optional<W5h2What> getWhat(){
        return w5h2Entity == null ? Optional.<W5h2What>absent() : Optional.fromNullable(w5h2Entity.getWhat());
    }
    public Optional<W5h2HowMuch> getWhy(){
        return w5h2Entity == null ? Optional.<W5h2HowMuch>absent() : Optional.fromNullable(w5h2Entity.getHowMuch());
    }
    public Optional<W5h2Who> getWho(){
        return w5h2Entity == null ? Optional.<W5h2Who>absent() : Optional.fromNullable(w5h2Entity.getWho());
    }

    public Optional<DateTime> getFirstWorkTime() {
        return getWhen().isPresent() ? getWhen().get().getFirstWorkTime():Optional.<DateTime>absent();
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

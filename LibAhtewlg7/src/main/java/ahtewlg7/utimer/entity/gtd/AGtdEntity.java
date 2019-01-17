package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import ahtewlg7.utimer.entity.ITipsEntity;
import ahtewlg7.utimer.entity.gtd.un.W5h2Entity;
import ahtewlg7.utimer.entity.un.IUtimerEntity;

public abstract class AGtdEntity implements IUtimerEntity, ITipsEntity {
    public static final String TAG = AGtdEntity.class.getSimpleName();

    protected String id;
    protected String title;
    protected String detail;
    protected W5h2Entity w5h2Entity;

    public abstract @NonNull String toJson();
    public abstract boolean isDone();

    @Override
    public boolean ifValid() {
        return !TextUtils.isEmpty(title);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public W5h2Entity getW5h2Entity() {
        return w5h2Entity;
    }

    public void setW5h2Entity(W5h2Entity w5h2Entity) {
        this.w5h2Entity = w5h2Entity;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(!TextUtils.isEmpty(id))
            builder.append("，id" ).append(id);
        if(!TextUtils.isEmpty(title))
            builder.append("，title" ).append(title);
        if(!TextUtils.isEmpty(detail))
            builder.append("，detail" ).append(detail);
        builder.append("，gtdType = " ).append(getGtdType().name());
        builder.append("，isValid = " ).append(ifValid());
        builder.append("，isDone = " ).append(isDone());
        if(w5h2Entity != null && w5h2Entity.toTips().isPresent())
            builder.append("，w5h2Entity = " ).append(w5h2Entity.toTips().get());
        return builder.toString();
    }
}

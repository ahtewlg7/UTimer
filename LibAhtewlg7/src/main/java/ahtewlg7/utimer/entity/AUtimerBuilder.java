package ahtewlg7.utimer.entity;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.material.AAttachFile;

/**
 * Created by lw on 2018/10/26.
 */
public abstract class AUtimerBuilder<E extends AUtimerEntity> {
    public static final String TAG = AUtimerBuilder.class.getSimpleName();

    protected String id;
    protected String uuid;
    protected String title;
    protected AAttachFile attachFile;
    protected DateTime createTime;
    protected E entity;

    @NonNull
    public abstract E build();

    public AUtimerBuilder setId(String id){
        this.id = id;
        return this;
    }

    public AUtimerBuilder setUuid(String uuid) {
        this.uuid = uuid;
        return this;
    }

    public AUtimerBuilder setTitle(String title){
        this.title = title;
        return this;
    }

    public AUtimerBuilder setCreateTime(DateTime createTime){
        this.createTime = createTime;
        return this;
    }

    public AUtimerBuilder setCopyEntity(E entity){
        this.entity = entity;
        return this;
    }

    public AUtimerBuilder setAttachFile(AAttachFile attachFile){
        this.attachFile = attachFile;
        return this;
    }
}

package ahtewlg7.utimer.entity;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.material.AAttachFile;

/**
 * Created by lw on 2018/10/26.
 */
public abstract class AUtimerBuilder<E extends AUtimerEntity, K extends AUtimerBuilder> {
    protected String id;
    protected String uuid;
    protected String title;
    protected AAttachFile attachFile;
    protected DateTime createTime;
    protected E entity;

    @NonNull
    public abstract E build();

    public K setId(String id){
        this.id = id;
        return (K)this;
    }

    public K setUuid(String uuid) {
        this.uuid = uuid;
        return (K)this;
    }

    public K setTitle(String title){
        this.title = title;
        return (K)this;
    }

    public K setCreateTime(DateTime createTime){
        this.createTime = createTime;
        return (K)this;
    }

    public K setCopyEntity(E entity){
        this.entity = entity;
        return (K)this;
    }

    public K setAttachFile(AAttachFile attachFile){
        this.attachFile = attachFile;
        return (K)this;
    }
}

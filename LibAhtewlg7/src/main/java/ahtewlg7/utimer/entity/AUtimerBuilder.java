package ahtewlg7.utimer.entity;

import android.support.annotation.NonNull;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.material.AAttachFile;

import static ahtewlg7.utimer.common.Constants.INVALID_NEXT_ID_INDEX;

/**
 * Created by lw on 2018/10/26.
 */
public abstract class AUtimerBuilder<E extends AUtimerEntity, K extends AUtimerBuilder> {

    protected long id = INVALID_NEXT_ID_INDEX;
    protected String uuid;
    protected String title;
    protected String detail;
    protected AAttachFile attachFile;
    protected DateTime createTime;
    protected E entity;

    @NonNull
    public abstract E build();

    public K setId(long id){
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
    public K setDetail(String detail){
        this.detail = detail;
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

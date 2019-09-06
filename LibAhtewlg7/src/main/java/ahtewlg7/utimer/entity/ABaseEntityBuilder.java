package ahtewlg7.utimer.entity;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;

/**
 * Created by lw on 2018/10/26.
 */
public abstract class ABaseEntityBuilder<E extends ABaseEntity, K extends ABaseEntityBuilder> {
    protected String uuid;
    protected String title;
    protected String detail;
    protected DateTime createTime;
    protected DateTime lastModifyTime;

    @NonNull
    public abstract E build();

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

    public K setLastModifyTime(DateTime lastModifyTime){
        this.lastModifyTime = lastModifyTime;
        return (K)this;
    }
}

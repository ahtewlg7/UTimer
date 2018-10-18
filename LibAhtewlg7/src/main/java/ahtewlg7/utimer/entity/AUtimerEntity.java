package ahtewlg7.utimer.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.File;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.enumtype.GtdType;

public abstract class AUtimerEntity<T extends AUtimerEntity.Builder> implements IMergerEntity{
    public static final String TAG = AUtimerEntity.class.getSimpleName();

    @NonNull
    public abstract GtdType getGtdType();
    public abstract Optional<String> getDetail();
    protected abstract void initByAttachFile(File attachFile);

    protected int accessTimes;
    protected File attachFile;
    protected String id;
    protected String uuid;
    protected String title;
    protected DateTime createTime;
    protected DateTime lastAccessTime;
    protected DateTime lastModifyTime;

    protected AUtimerEntity(@Nonnull T t){
        //merge first
        if(t.entity != null)
            merge(t.entity);
        id   = t.id;
        uuid = TextUtils.isEmpty(t.uuid) ? new IdAction().getUUId() : t.uuid;
        createTime = t.createTime;
        if(t.attachFile != null){
            attachFile = t.attachFile;
            initByAttachFile(attachFile);
        }
    }

    public String getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    public File getAttachFile() {
        return attachFile;
    }

    public int getAccessTimes() {
        return accessTimes;
    }

    public void setAccessTimes(int accessTimes) {
        this.accessTimes = accessTimes;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public DateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(DateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public DateTime getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(DateTime lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public boolean ifValid() {
        return !TextUtils.isEmpty(title) && attachFile != null && attachFile.exists();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id").append(id);
        builder.append(",uuid").append(uuid);
        builder.append(",ifValid").append(ifValid());
        builder.append(",accessTimes").append(accessTimes);
        if(!TextUtils.isEmpty(title))
            builder.append(",title").append(title);
        if(getDetail().isPresent() && !TextUtils.isEmpty(getDetail().get()))
            builder.append(",detail").append(getDetail().get());
        if(createTime != null)
            builder.append(",createTime").append(createTime.toString());
        if(lastAccessTime != null)
            builder.append(",lastAccessTime").append(lastAccessTime.toString());
        if(lastModifyTime != null)
            builder.append(",lastModifyTime").append(lastModifyTime.toString());
        if(attachFile != null)
            builder.append(",attachFile").append(attachFile.getName());
        return builder.toString();
    }

    public static abstract class Builder<E extends AUtimerEntity>{
        protected String id;
        protected String uuid;
        protected File attachFile;
        protected DateTime createTime;
        protected E entity;

        @NonNull
        public abstract E build();

        public Builder setId(String id){
            this.id = id;
            return this;
        }

        public Builder setUuid(String uuid) {
            this.uuid = uuid;
            return this;
        }
        public Builder setCreateTime(DateTime createTime){
            this.createTime = createTime;
            return this;
        }

        public Builder setCopyEntity(E entity){
            this.entity = entity;
            return this;
        }

        public Builder setAttachFile(File attachFile){
            this.attachFile = attachFile;
            return this;
        }
    }
}

package ahtewlg7.utimer.entity;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.enumtype.DateLife;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.gtd.DateLifeCycleAction;

public abstract class ABaseEntity<T extends ABaseEntityBuilder> {
    @NonNull
    public abstract GtdType getGtdType();

    protected int accessTimes;
    protected String uuid;
    protected String title;
    protected String detail;
    protected DateTime createTime;
    protected DateTime lastAccessTime;
    protected DateTime lastModifyTime;

    protected DateLifeCycleAction dateLifeCycleAction;

    protected ABaseEntity(@Nonnull T t){
        title      = t.title;
        detail     = t.detail;
        uuid       = TextUtils.isEmpty(t.uuid) ? new IdAction().getUUId() : t.uuid;
        createTime = t.createTime;
        dateLifeCycleAction = new DateLifeCycleAction();
    }

    public boolean ifValid() {
        return !TextUtils.isEmpty(title) && !TextUtils.isEmpty(uuid);
    }

    public boolean ifRawReadable(){
        return ifValid();
    }

    public String getUuid() {
        return uuid;
    }

    public DateTime getCreateTime() {
        return createTime;
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

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public Optional<String> getDetail() {
        return TextUtils.isEmpty(detail) ? Optional.<String>absent() : Optional.of(detail);
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

    public DateLife getCreateDateLife(){
        return dateLifeCycleAction.getLife(createTime);
    }
    public String getCreateLifeDetail(){
        return dateLifeCycleAction.getLifeDetail(getCreateDateLife());
    }

    public DateLife getLastModifyDateLife(){
        return dateLifeCycleAction.getLife(lastModifyTime);
    }
    public String getLastModifyLifeDetail(){
        return dateLifeCycleAction.getLifeDetail(getLastModifyDateLife());
    }

    protected void toMakeEntityOk(){
        if(TextUtils.isEmpty(uuid))
            uuid = new IdAction().getUUId();
        if(createTime == null)
            createTime = DateTime.now();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(",uuid=").append(uuid);
        builder.append(",ifValid=").append(ifValid());
        builder.append(",accessTimes=").append(accessTimes);
        if(!TextUtils.isEmpty(title))
            builder.append(",title=").append(title);
        if(getDetail().isPresent() && !TextUtils.isEmpty(getDetail().get()))
            builder.append(",detail=").append(getDetail().get());
        if(createTime != null)
            builder.append(",createTime=").append(createTime.toString());
        if(lastAccessTime != null)
            builder.append(",lastAccessTime=").append(lastAccessTime.toString());
        return builder.toString();
    }
}

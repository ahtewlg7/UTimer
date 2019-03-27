package ahtewlg7.utimer.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.material.AAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;

public abstract class AUtimerEntity<T extends AUtimerBuilder> implements ITipsEntity, IMergerEntity{

    public static final long INVALID_INDEX  = -1;
    @NonNull
    public abstract GtdType getGtdType();
    public abstract Optional<String> getDetail();
    public abstract boolean ensureAttachFileExist();

    protected int accessTimes;
    protected long id = INVALID_INDEX;
    protected boolean isGtdActived;

    protected String uuid;
    protected String title;
    protected String detail;
    protected DateTime createTime;
    protected DateTime lastAccessTime;
    protected DateTime lastModifyTime;
    protected AAttachFile attachFile;

    protected AUtimerEntity(@Nonnull T t){
        //update first
        if(t.entity != null)
            update(t.entity);
        id         = t.id;
        title      = t.title;
        detail     = t.detail;
        uuid       = TextUtils.isEmpty(t.uuid) ? new IdAction().getUUId() : t.uuid;
        createTime = t.createTime;
        if(t.attachFile != null){
            attachFile = t.attachFile;
            initByAttachFile(attachFile);
        }
    }

    public boolean ifValid() {
        return !TextUtils.isEmpty(title);
    }

    public void destory(){
        if(attachFile != null && attachFile.ifValid())
            attachFile.deleteOnExit();
    }

    public long getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    public AAttachFile getAttachFile() {
        return attachFile;
    }

    public Optional<String> getAttachFileRPath(){
        return attachFile != null ? attachFile.getRPath() : Optional.<String>absent();
    }

    public boolean isGtdActived() {
        return isGtdActived;
    }

    public void setGtdActived(boolean gtdActived) {
        isGtdActived = gtdActived;
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

    protected void initByAttachFile(AAttachFile attachFile){
        Optional<String> title = attachFile.getTitle();
        if(title.isPresent())
            setTitle(title.get());
        if(attachFile.ifValid()) {
            createTime     = attachFile.getCreateTime();
            lastAccessTime = attachFile.getLassAccessTime();
            lastModifyTime = attachFile.getLassModifyTime();
        }
    }
    protected void toMakeUuidOk(){
        if(TextUtils.isEmpty(uuid))
            uuid = new IdAction().getUUId();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("id=").append(id);
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
        if(lastModifyTime != null)
            builder.append(",lastModifyTime=").append(lastModifyTime.toString());
        if(attachFile != null && attachFile.getTitle().isPresent())
            builder.append(",attachFile=").append(attachFile.getTitle().get());
        return builder.toString();
    }
}

package ahtewlg7.utimer.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.material.AAttachFile;
import ahtewlg7.utimer.enumtype.GtdLife;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.gtd.GtdLifeCycleAction;

public abstract class AUtimerEntity<T extends AUtimerBuilder> implements ITipsEntity, IMergerEntity{
    @NonNull
    public abstract GtdType getGtdType();
    public abstract Optional<String> getDetail();
    public abstract boolean ensureAttachFileExist();

    protected int accessTimes;
    protected boolean isGtdActived;

    protected String uuid;
    protected String title;
    protected String detail;
    protected DateTime createTime;
    protected DateTime lastAccessTime;
    protected DateTime lastModifyTime;
    protected AAttachFile attachFile;

    protected GtdLifeCycleAction lifeCycleAction;

    protected AUtimerEntity(@Nonnull T t){
        //update first
        if(t.entity != null)
            update(t.entity);
        title      = t.title;
        detail     = t.detail;
        uuid       = TextUtils.isEmpty(t.uuid) ? new IdAction().getUUId() : t.uuid;
        createTime = t.createTime;
        if(t.attachFile != null){
            attachFile = t.attachFile;
            updateAttachFileInfo(attachFile);
        }
    }

    public boolean ifValid() {
        return !TextUtils.isEmpty(title);
    }

    public boolean ifRawReadable(){
        return ifValid();
    }

    public boolean ifAttachFileValid(){
        return attachFile != null && attachFile.ifValid();
    }

    public void destory(){
        if(attachFile != null && attachFile.ifValid())
            attachFile.delete();
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

    public GtdLife getGtdLife() {
        if(lifeCycleAction == null)
            lifeCycleAction = new GtdLifeCycleAction();
        return lifeCycleAction.getLife(getLifeCycleTime());
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

    public void updateAttachFileInfo(AAttachFile attachFile){
        this.attachFile = attachFile;
        Optional<String> titleOptional = attachFile.getTitle();
        if(titleOptional.isPresent() && TextUtils.isEmpty(title))
            setTitle(titleOptional.get());
        if(attachFile.ifValid()) {
            createTime     = attachFile.getCreateTime();
            lastAccessTime = attachFile.getLassAccessTime();
            lastModifyTime = attachFile.getLassModifyTime();
        }
    }

    protected DateTime getLifeCycleTime(){
        return createTime;
    }

    protected void toMakeEntityOk(){
        if(TextUtils.isEmpty(uuid))
            uuid = new IdAction().getUUId();
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
        builder.append(",gtdLife=").append(getGtdLife().name());
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

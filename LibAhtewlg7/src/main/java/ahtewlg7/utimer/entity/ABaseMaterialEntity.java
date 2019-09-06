package ahtewlg7.utimer.entity;

import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.enumtype.StorageType;

public abstract class ABaseMaterialEntity<T extends ABaseMaterialEntityBuilder> extends ABaseEntity<T> {
    public abstract StorageType getStorageType();
    public abstract boolean ensureAttachFileExist();

    protected AAttachFile attachFile;

    protected ABaseMaterialEntity(@Nonnull T t){
        super(t);
        if(t.attachFile != null){
            attachFile = t.attachFile;
            updateAttachFileInfo(attachFile);
        }
    }

    public boolean ifAttachFileValid(){
        return attachFile != null && attachFile.ifValid();
    }

    public void destory(){
        if(attachFile != null && attachFile.ifValid())
            attachFile.delete();
    }

    public AAttachFile getAttachFile() {
        return attachFile;
    }

    public Optional<String> getAttachFileRPath(){
        return attachFile != null ? attachFile.getRPath() : Optional.<String>absent();
    }
    public Optional<String> getAttachFileAbsPath(){
        return attachFile != null ? attachFile.getAbsPath() : Optional.<String>absent();
    }

    public void updateAttachFileInfo(AAttachFile attachFile){
        this.attachFile = attachFile;
        Optional<String> titleOptional = attachFile.getTitle();
        if(titleOptional.isPresent() && TextUtils.isEmpty(title))
            setTitle(titleOptional.get());
        if(!attachFile.ifValid())
            return;
        if(attachFile.getCreateTime() != null)
            createTime     = attachFile.getCreateTime();
        if(attachFile.getLassAccessTime() != null)
            lastAccessTime = attachFile.getLassAccessTime();
        if(attachFile.getLassModifyTime() != null)
            lastModifyTime = attachFile.getLassModifyTime();
    }

    @Override
    public boolean ifValid() {
        return super.ifValid() && ifAttachFileValid();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(super.toString());
        if(attachFile != null && attachFile.getTitle().isPresent())
            builder.append(",attachFile=").append(attachFile.getTitle().get());
        if(lastModifyTime != null)
            builder.append(",lastModifyTime=").append(lastModifyTime.toString());
        return builder.toString();
    }
}

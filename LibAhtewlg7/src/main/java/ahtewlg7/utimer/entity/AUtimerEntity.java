package ahtewlg7.utimer.entity;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.common.IdAction;
import ahtewlg7.utimer.entity.material.AAttachFile;
import ahtewlg7.utimer.entity.material.DirAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;

public abstract class AUtimerEntity<T extends AUtimerBuilder>
        implements ITipsEntity, IMergerEntity{
    public static final String TAG = AUtimerEntity.class.getSimpleName();

    @NonNull
    public abstract GtdType getGtdType();
    public abstract Optional<String> getDetail();

    protected int accessTimes;
    protected AAttachFile attachFile;
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
        id         = t.id;
        title      = t.title;
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

    public String getId() {
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

    public boolean ensureAttachFileExist() {
        if(attachFile == null){
            String fileName = !TextUtils.isEmpty(getTitle()) ? getTitle() : new DateTimeAction().toFormatNow().toString();
            String filePath = new FileSystemAction().getProjectGtdAbsPath();
            attachFile = new DirAttachFile(filePath, fileName);
        }
        boolean result = attachFile.createOrExist();
        Logcat.i(TAG,"ensureAttachFileExist result = " + result);
        return result;
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
            builder.append(",attachFile").append(attachFile.getTitle());
        return builder.toString();
    }
}

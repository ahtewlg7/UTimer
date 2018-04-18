package ahtewlg7.utimer.entity.gtd;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.IUtimerEntity;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2017/9/27.
 */

public abstract class AGtdEntity implements IUtimerEntity{
    public static final String TAG = AGtdEntity.class.getSimpleName();

    public abstract GtdType getTaskType();

    protected String id;
    protected String title;
    protected String detail;
    protected String fileRPath;

    protected boolean trashed;
    protected boolean holded;

    protected DateTime createdDateTime;
    protected DateTime lastModifyDateTime;
    protected AGtdEntity parentTaskEntity;

    @Override
    public boolean isValid(){
        return true;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getId(){
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getFileRPath() {
        return fileRPath;
    }

    public void setFileRPath(String fileRPath) {
        this.fileRPath = fileRPath;
    }

    public boolean isTrashed() {
        return trashed;
    }

    public void setTrashed(boolean trashed) {
        this.trashed = trashed;
    }

    public boolean isHolded() {
        return holded;
    }

    public void setHolded(boolean holded) {
        this.holded = holded;
    }

    public DateTime getCreatedDateTime() {
        return createdDateTime;
    }

    public void setCreatedDateTime(DateTime createdDateTime) {
        this.createdDateTime = createdDateTime;
    }

    public DateTime getLastModifyDateTime() {
        return lastModifyDateTime;
    }

    public void setLastModifyDateTime(DateTime lastModifyDateTime) {
        this.lastModifyDateTime = lastModifyDateTime;
    }

    public AGtdEntity getParentTaskEntity() {
        return parentTaskEntity;
    }

    public void setParentTaskEntity(AGtdEntity parentTaskEntity) {
        this.parentTaskEntity = parentTaskEntity;
    }

    @Override
    public String toString() {
        StringBuilder builder =  new StringBuilder();
        String tmp =  "{id = " + id + ", taskType = " + getTaskType().name() + ", title = " + title + ", detail = " + detail
                + ", fileRPath = " + fileRPath + ", trashed = " + trashed;
        builder.append(tmp);
        if(parentTaskEntity != null) {
            tmp = "，parentTaskEntity = " + parentTaskEntity.toString();
            builder.append(tmp);
        }
        if(createdDateTime != null) {
            tmp = "createdDateTime = " + createdDateTime.toString();
            builder.append(tmp);
        }
        if(lastModifyDateTime != null) {
            tmp = ", lastModifyDateTime = " + lastModifyDateTime.toString();
            builder.append(tmp);
        }
        return builder.toString();
    }
}

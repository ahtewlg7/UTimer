package ahtewlg7.utimer.entity;

import android.text.TextUtils;

import org.joda.time.DateTime;

import ahtewlg7.utimer.enumtype.LoadType;

/**
 * Created by lw on 2017/9/27.
 */

public class NoteEntity implements IUtimerEntity {
    public static final String TAG = NoteEntity.class.getSimpleName();

    private boolean contextChanged    = false;
    private boolean noteFielExist     = false;

    private LoadType loadType;
    private String id;
    private String title;
    private String detail;
    private String noteName;
    private String fileRPath;
    private String rawContext;
    private String mdContext;
    private String lastModifyContext;

    private DateTime createTime;
    private DateTime lastAccessTime;
    private IAccessPosition lastAccessPosition;

    @Override
    public boolean isValid(){
        return !TextUtils.isEmpty(rawContext);
    }

    public boolean isContextChanged() { return contextChanged; }
    public void setContextChanged(boolean contextChanged) { this.contextChanged = contextChanged; }

    public boolean isNoteFielExist() {
        return noteFielExist;
    }
    public void setNoteFielExist(boolean noteFielExist) {
        this.noteFielExist = noteFielExist;
    }

    public LoadType getLoadType() { return loadType; }
    public void setLoadType(LoadType loadType) { this.loadType = loadType; }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDetail() {
        return detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getNoteName() {
        return noteName;
    }
    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getFileRPath() {
        return fileRPath;
    }
    public void setFileRPath(String fileRPath) {
        this.fileRPath = fileRPath;
    }

    public String getRawContext() {
        return rawContext;
    }
    public void setRawContext(String rawContext) {
        this.rawContext = rawContext;
    }

    public String getMdContext() {
        return mdContext;
    }
    public void setMdContext(String mdContext) {
        this.mdContext = mdContext;
    }

    public String getLastModifyContext() {
        return lastModifyContext;
    }
    public void setLastModifyContext(String lastModifyContext) {
        this.lastModifyContext = lastModifyContext;
    }

    @Override
    public DateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public DateTime getLastAccessTime() {
        return lastAccessTime;
    }
    public void setLastAccessTime(DateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public IAccessPosition getLastAccessPosition() {
        return lastAccessPosition;
    }
    public void setLastAccessPosition(IAccessPosition lastAccessPosition) {
        this.lastAccessPosition = lastAccessPosition;
    }

    @Override
    public String toString() {
        String tmp = "id = " + id + ", fileRPath = " + fileRPath;
        if(createTime != null)
            tmp += ", createDateTime = " + createTime.toString();
        if(lastAccessTime != null)
            tmp += ", lastModifyDateTime = " + lastAccessTime.toString();
        return tmp;
    }
}

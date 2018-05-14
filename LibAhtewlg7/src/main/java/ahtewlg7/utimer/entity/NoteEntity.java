package ahtewlg7.utimer.entity;

import org.joda.time.DateTime;

/**
 * Created by lw on 2017/9/27.
 */

public class NoteEntity implements INoteEntity {
    public static final String TAG = NoteEntity.class.getSimpleName();

    private boolean created = false;

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

    public NoteEntity(){
    }

    @Override
    public boolean isValid(){
        return true;
    }

    @Override
    public boolean ifFileExist() {
        return false;
    }

    @Override
    public boolean isCreated() {
        return created;
    }

    @Override
    public void setIsCreated(boolean created) {
        this.created = created;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    @Override
    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public String getNoteName() {
        return noteName;
    }

    @Override
    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    @Override
    public String getFileRPath() {
        return fileRPath;
    }

    @Override
    public void setFileRPath(String fileRPath) {
        this.fileRPath = fileRPath;
    }

    @Override
    public String getRawContext() {
        return rawContext;
    }

    @Override
    public void setRawContext(String rawContext) {
        this.rawContext = rawContext;
    }

    @Override
    public String getMdContext() {
        return mdContext;
    }

    @Override
    public void setMdContext(String mdContext) {
        this.mdContext = mdContext;
    }

    @Override
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

    @Override
    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    @Override
    public DateTime getLastAccessTime() {
        return lastAccessTime;
    }

    @Override
    public void setLastAccessTime(DateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    @Override
    public IAccessPosition getLastAccessPosition() {
        return lastAccessPosition;
    }

    @Override
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

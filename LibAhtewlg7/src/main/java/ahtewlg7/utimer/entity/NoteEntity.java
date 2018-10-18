package ahtewlg7.utimer.entity;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.taskContext.IPosition;
import ahtewlg7.utimer.entity.un.IUtimerEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.enumtype.UnLoadType;

/**
 * Created by lw on 2017/9/27.
 */

public class NoteEntity implements IUtimerEntity {
    public static final String TAG = NoteEntity.class.getSimpleName();

    private boolean contextChanged      = false;
    private boolean noteFileExist       = false;

    private UnLoadType loadType;
    private String id;
    private String title;
    private String detail;
    private String lastModifyContext;
    private String noteName;
    private String fileRPath; //utimer relative path
    private String fileAbsPath; //utimer abs path
    private boolean valid = true;

    private DateTime createTime;
    private DateTime lastAccessTime;
    private IPosition lastAccessPosition;

    @Override
    public GtdType getGtdType() {
        return GtdType.NOTE;
    }

    @Override
    public boolean ifValid(){
        return valid;
    }
    public void setValid(boolean valid){ this.valid = valid; }

    public boolean isContextChanged() { return contextChanged; }
    public void setContextChanged(boolean contextChanged) { this.contextChanged = contextChanged; }

    public boolean isNoteFileExist() {
        return noteFileExist;
    }
    public void setNoteFileExist(boolean noteFileExist) {
        this.noteFileExist = noteFileExist;
    }

    public UnLoadType getLoadType() { return loadType; }
    public void setLoadType(UnLoadType loadType) { this.loadType = loadType; }

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

    public String getLastModifyContext() {
        return lastModifyContext;
    }
    public void setLastModifyContext(String lastModifyContext) {
        this.lastModifyContext = lastModifyContext;
    }

    public String getNoteName() {
        return noteName;
    }
    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getFileAbsPath() {
        return fileAbsPath;
    }
    public void setFileAbsPath(String fileAbsPath) {
        this.fileAbsPath = fileAbsPath;
    }

    public String getFileRPath() {
        return fileRPath;
    }
    public void setFileRPath(String fileRPath) {
        this.fileRPath = fileRPath;
    }

    public DateTime getCreateTime() {
        return createTime;
    }
    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public DateTime getLastAccessTime() {
        return lastAccessTime;
    }
    public void setLastAccessTime(DateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public IPosition getLastAccessPosition() {
        return lastAccessPosition;
    }
    public void setLastAccessPosition(IPosition lastAccessPosition) {
        this.lastAccessPosition = lastAccessPosition;
    }

    @Override
    public String toString() {
        String tmp = "id = " + id + ", fileRPath = " + fileRPath + ", fileAbsPath = " + fileAbsPath;
        if(createTime != null)
            tmp += ", createDateTime = " + createTime.toString();
        if(lastAccessTime != null)
            tmp += ", lastModifyDateTime = " + lastAccessTime.toString();
        return tmp;
    }
}

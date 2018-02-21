package ahtewlg7.utimer.entity;

import org.joda.time.DateTime;

/**
 * Created by lw on 2017/9/27.
 */

public class NoteEntity implements IUtimerEntity {
    public static final String TAG = NoteEntity.class.getSimpleName();

    private String id;
    private String noteName;
    private String fileRPath;

    private DateTime createDateTime;
    private DateTime lastModifyDateTime;

    public NoteEntity(){
    }
    public void setId(String id){
        this.id = id;
    }
    public String getId() {
        return id;
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

    public DateTime getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(DateTime createDateTime) {
        this.createDateTime = createDateTime;
    }

    public DateTime getLastModifyDateTime() {
        return lastModifyDateTime;
    }

    public void setLastModifyDateTime(DateTime lastModifyDateTime) {
        this.lastModifyDateTime = lastModifyDateTime;
    }

    @Override
    public String toString() {
        String tmp = "id = " + id + ", fileRPath = " + fileRPath;
        if(createDateTime != null)
            tmp += ", createDateTime = " + createDateTime.toString();
        if(lastModifyDateTime != null)
            tmp += ", lastModifyDateTime = " + lastModifyDateTime.toString();
        return tmp;
    }
}

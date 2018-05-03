package ahtewlg7.utimer.entity;


import org.joda.time.DateTime;

public interface INoteEntity extends IUtimerEntity{
    public boolean ifFileExist();

    @Override
    public String getId();
    public void setId(String id);

    public String getTitle();
    public void setTitle(String title);

    public String getDetail();
    public void setDetail(String detail);

    public String getNoteName();
    public void setNoteName(String noteName);

    public String getFileRPath();
    public void setFileRPath(String fileRPath);

    public String getRawContext();
    public void setRawContext(String rawContext);

    public String getMdContext();
    public void setMdContext(String mdContext);

    public String getLastModifyContext();
    public void setLastModifyContext(String lastModifyContext);

    public DateTime getCreateTime();
    public void setCreateTime(DateTime dateTIme);

    public DateTime getLastAccessTime();
    public void setLastAccessTime(DateTime dateTime);

    public IAccessPosition getLastAccessPosition();
    public void setLastAccessPosition(IAccessPosition accessPosition);
}

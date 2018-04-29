package ahtewlg7.utimer.entity;


import org.joda.time.DateTime;

public interface INoteEntity {
    public boolean ifFileExist();

    public String getId();
    public String getTitle();
    public String getDetail();

    public int getLassAccessIndex();
    public String getLassModifyContext();

    public DateTime getCreateTime();
    public DateTime getLastAccessTime();

    public void setRawContext(String rawContext);
    public String getRawContext();

    public void setMdContext(String mdContext);
    public String getMdContext();


}

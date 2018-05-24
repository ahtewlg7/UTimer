package ahtewlg7.utimer.entity;

import org.joda.time.DateTime;

/**
 * Created by lw on 2018/1/9.
 */

public interface IUtimerEntity {
    public String getId();
    public boolean isValid();
    public String getTitle();
    public String getDetail();
    public DateTime getCreateTime();
    public DateTime getLastAccessTime();
}

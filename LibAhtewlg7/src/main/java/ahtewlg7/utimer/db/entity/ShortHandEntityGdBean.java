package ahtewlg7.utimer.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.joda.time.DateTime;

import ahtewlg7.utimer.db.converter.DateTimeTypeConverter;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "SHORTHAND_ENTITY"
)
public class ShortHandEntityGdBean {
    // KEEP FIELDS - put your custom fields here
    public static final String TAG = ShortHandEntityGdBean.class.getSimpleName();
    //KEEP FIELDS END

    @Id(autoincrement = true)
    private long id;
    private int accessTimes;
    @Index(unique = true)
    private String title;
    private String uuid;
    private String value;
    private boolean isActived;
    @Convert(converter = DateTimeTypeConverter.class, columnType = String.class)
    private DateTime createTime;
    @Convert(converter = DateTimeTypeConverter.class, columnType = String.class)
    private DateTime lastAccessTime;
    @Convert(converter = DateTimeTypeConverter.class, columnType = String.class)
    private DateTime lastModifyTime;

    @Generated(hash = 666411087)
    public ShortHandEntityGdBean(long id, int accessTimes, String title,
            String uuid, String value, boolean isActived, DateTime createTime,
            DateTime lastAccessTime, DateTime lastModifyTime) {
        this.id = id;
        this.accessTimes = accessTimes;
        this.title = title;
        this.uuid = uuid;
        this.value = value;
        this.isActived = isActived;
        this.createTime = createTime;
        this.lastAccessTime = lastAccessTime;
        this.lastModifyTime = lastModifyTime;
    }

    @Generated(hash = 283632925)
    public ShortHandEntityGdBean() {
    }

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TAG);
        builder.append("{").append(id);
        builder.append(",isActived=").append(isActived);
        if(!TextUtils.isEmpty(title))
            builder.append(",title").append(title);

        return builder.append("}").toString();
    }
    // KEEP METHODS END

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getAccessTimes() {
        return this.accessTimes;
    }

    public void setAccessTimes(int accessTimes) {
        this.accessTimes = accessTimes;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getIsActived() {
        return this.isActived;
    }

    public void setIsActived(boolean isActived) {
        this.isActived = isActived;
    }

    public DateTime getCreateTime() {
        return this.createTime;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public DateTime getLastAccessTime() {
        return this.lastAccessTime;
    }

    public void setLastAccessTime(DateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public DateTime getLastModifyTime() {
        return this.lastModifyTime;
    }

    public void setLastModifyTime(DateTime lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }
}

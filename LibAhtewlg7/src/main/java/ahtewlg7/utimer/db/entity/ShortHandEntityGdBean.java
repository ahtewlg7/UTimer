package ahtewlg7.utimer.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.joda.time.DateTime;

import ahtewlg7.utimer.db.converter.DateTimeTypeConverter;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "SHORTHAND"
)
public class ShortHandEntityGdBean {
    // KEEP FIELDS - put your custom fields here
    //KEEP FIELDS END

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    @Index(unique = true)
    private String uuid;
    private int accessTimes;
    private String title;
    private String detail;
    private String attachFileRPath;
    @Convert(converter = DateTimeTypeConverter.class, columnType = Long.class)
    private DateTime createTime;
    @Convert(converter = DateTimeTypeConverter.class, columnType = Long.class)
    private DateTime lastAccessTime;

    @Generated(hash = 283632925)
    public ShortHandEntityGdBean() {
    }

    @Generated(hash = 1563219067)
    public ShortHandEntityGdBean(Long id, @NotNull String uuid, int accessTimes,
            String title, String detail, String attachFileRPath,
            DateTime createTime, DateTime lastAccessTime) {
        this.id = id;
        this.uuid = uuid;
        this.accessTimes = accessTimes;
        this.title = title;
        this.detail = detail;
        this.attachFileRPath = attachFileRPath;
        this.createTime = createTime;
        this.lastAccessTime = lastAccessTime;
    }

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{");
        if(!TextUtils.isEmpty(title))
            builder.append(",title").append(title);

        return builder.append("}").toString();
    }
    // KEEP METHODS END

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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttachFileRPath() {
        return this.attachFileRPath;
    }

    public void setAttachFileRPath(String attachFileRPath) {
        this.attachFileRPath = attachFileRPath;
    }


    public String getDetail() {
        return this.detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

}

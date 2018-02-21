package ahtewlg7.utimer.db.entity;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;
import org.joda.time.DateTime;

import ahtewlg7.utimer.db.converter.DateTimeTypeConverter;
import ahtewlg7.utimer.db.converter.GtdTypeConverter;
import ahtewlg7.utimer.enumtype.GtdType;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lw on 2018/1/5.
 */

@Entity(
    nameInDb = "GTD_ENTITY",
    generateConstructors = false
)
public class GtdEntityGdBean{
    @Unique
    private String key;
    private String value;

    @Convert(converter = GtdTypeConverter.class, columnType = Integer.class)
    private GtdType gtdType;

    @Convert(converter = DateTimeTypeConverter.class, columnType = String.class)
    private DateTime createTime;

    @Convert(converter = DateTimeTypeConverter.class, columnType = String.class)
    private DateTime lastAccessTime;

    public GtdEntityGdBean() {
    }

    public String getKey() {
        return this.key;
    }
    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public GtdType getGtdType() {
        return gtdType;
    }
    public void setGtdType(GtdType gtdType) {
        this.gtdType = gtdType;
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
}

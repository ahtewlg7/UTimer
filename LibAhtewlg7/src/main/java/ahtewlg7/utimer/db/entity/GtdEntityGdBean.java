package ahtewlg7.utimer.db.entity;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.joda.time.DateTime;

import ahtewlg7.utimer.db.converter.DateTimeTypeConverter;
import ahtewlg7.utimer.db.converter.GtdTypeConverter;
import ahtewlg7.utimer.enumtype.GtdType;

/**
 * Created by lw on 2018/1/5.
 */

@Entity(
    nameInDb = "GTD_ENTITY",
    generateConstructors = false
)
public class GtdEntityGdBean{
    @Id
    private long id;
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

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

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

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("key = " + key + ", value = " + value + ", gtdType = " + gtdType.name());
        if(createTime != null)
            stringBuilder.append(", createTime = " + createTime.toString());
        if(lastAccessTime != null)
            stringBuilder.append(", lastAccessTime = " + lastAccessTime.toString());
        return stringBuilder.toString();
    }
    // KEEP METHODS END
}

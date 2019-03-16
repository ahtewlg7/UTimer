package ahtewlg7.utimer.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lw on 2018/1/5.
 */

@Entity(
    nameInDb = "TASK"
)
public class TaskEntityGdBean {
    // KEEP FIELDS - put your custom fields here
    //KEEP FIELDS END

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{").append(id);
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
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDetail() {
        return this.detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }


    @Id(autoincrement = true)
    private long id;
    @Index(unique = true)
    private String uuid;
    private String title;
    private String detail;

    @Generated(hash = 1188380601)
    public TaskEntityGdBean(long id, String uuid, String title, String detail) {
        this.id = id;
        this.uuid = uuid;
        this.title = title;
        this.detail = detail;
    }
    @Generated(hash = 1604574495)
    public TaskEntityGdBean() {
    }
}

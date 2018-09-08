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
    nameInDb = "TASK_ENTITY"
)
public class TaskEntityGdBean {
    // KEEP FIELDS - put your custom fields here
    public static final String TAG = TaskEntityGdBean.class.getSimpleName();
    //KEEP FIELDS END

    @Id(autoincrement = true)
    private long id;
    @Index(unique = true)
    private String title;
    private String value;
    private boolean active;

    @Generated(hash = 64805106)
    public TaskEntityGdBean(long id, String title, String value, boolean active) {
        this.id = id;
        this.title = title;
        this.value = value;
        this.active = active;
    }

    @Generated(hash = 1604574495)
    public TaskEntityGdBean() {
    }

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TAG);
        builder.append("{").append(id);
            builder.append(",active=").append(active);
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

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean getActive() {
        return this.active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}

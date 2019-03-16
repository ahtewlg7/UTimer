package ahtewlg7.utimer.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "NOTE",
    generateConstructors = false
)
public class  NoteEntityGdBean{
    @Id
    private long id;
    @Index(unique = true)
    private String uuid;
    private String title;
    private String detail;

    public NoteEntityGdBean() {
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        return "NoteEntityGdBean{id = " + id + ", title = " + title + ", detail = " + detail + "}";
    }
    // KEEP METHODS END

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
}

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
    private Long id;
    @Index(unique = true)
    private String uuid;
    private String title;
    private String detail;

    public NoteEntityGdBean() {
    }

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        return "NoteEntityGdBean{title = " + title + ", detail = " + detail + "}";
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

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

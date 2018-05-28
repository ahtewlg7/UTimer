package ahtewlg7.utimer.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "NOTE_ENTITY",
    generateConstructors = false
)
public class  NoteEntityGdBean{
    @Id
    private long id;
    @Unique
    private String key;
    private String value;

    public NoteEntityGdBean() {
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

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        return "NoteEntityGdBean{id = " + id + ", key = " + key + ", value = " + value + "}";
    }
    // KEEP METHODS END
}

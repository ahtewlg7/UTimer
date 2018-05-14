package ahtewlg7.utimer.db.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Unique;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "NOTE_ENTITY",
    generateConstructors = false
)
public class  NoteEntityGdBean{
    @Unique
    private String key;
    private String value;

    public NoteEntityGdBean() {
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

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("key = " + key + ", value = " + value);
        return stringBuilder.toString();
    }
    // KEEP METHODS END
}

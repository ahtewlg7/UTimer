package ahtewlg7.utimer.entity.context;

/**
 * Created by lw on 2019/4/11.
 */
public class Contact {
    private String name;
    private String alias;

    public Contact(String name) {
        this.name = name;
    }

    public Contact(String name, String alias) {
        this.name = name;
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public String getAlias() {
        return alias;
    }
}

package ahtewlg7.utimer.entity.span;

/**
 * Created by lw on 2019/7/8.
 */
public class SimpleSpanTag extends ASpanTag {
    private String name;

    public SimpleSpanTag(String name) {
        this.name = name;
    }

    @Deprecated
    @Override
    public int getTagRid() {
        return 0;
    }

    @Override
    public String getTagName() {
        return name;
    }
}

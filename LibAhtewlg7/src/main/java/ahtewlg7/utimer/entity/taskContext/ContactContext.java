package ahtewlg7.utimer.entity.taskContext;

import java.net.URL;

import ahtewlg7.utimer.enumtype.TaskContextType;
import ahtewlg7.utimer.taskContext.TaskContextAction;

/**
 * Created by lw on 2017/11/22.
 */

public class ContactContext extends ATaskContext {
    public static final String TAG = ContactContext.class.getSimpleName();

    private int id;
    private String name;
    private String phone;
    private String sortKey;
    private Long photoId;
    private String lookUpKey;
    private String pinyin;
    private URL url;

    @Override
    public TaskContextType getContextType() {
        return TaskContextType.CONTACT;
    }

    @Override
    public boolean isOk(TaskContextAction taskContextAction) {
        return false;
    }

    @Override
    public String toString() {
        return super.toString() +" ，id = " + id + ", name = " + name + ", phone = " + phone + super.toString();
    }
}

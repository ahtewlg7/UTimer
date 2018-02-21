package ahtewlg7.utimer.entity.taskContext;

import ahtewlg7.utimer.enumtype.TaskContextType;
import ahtewlg7.utimer.taskContext.TaskContextAction;

/**
 * Created by lw on 2017/11/22.
 */

public class EquipmentContext extends ATaskContext {
    public static final String TAG = EquipmentContext.class.getSimpleName();

    private String name;
    private String detail;

    public void setName(String name) {
        this.name = name;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    @Override
    public TaskContextType getContextType() {
        return TaskContextType.EQUIPMENT;
    }

    @Override
    public boolean isOk(TaskContextAction taskContextAction) {
        return false;
    }

    @Override
    public String toString() {
        return super.toString() + ", name = " + name + ", detail = " + detail + super.toString();
    }
}

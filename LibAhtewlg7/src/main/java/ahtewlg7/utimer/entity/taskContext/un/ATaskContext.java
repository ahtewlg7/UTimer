package ahtewlg7.utimer.entity.taskContext.un;

import ahtewlg7.utimer.enumtype.TaskContextType;
import ahtewlg7.utimer.taskContext.TaskContextAction;

/**
 * Created by lw on 2017/11/22.
 */

public abstract class ATaskContext {
    public static final String TAG = ATaskContext.class.getSimpleName();

    public abstract TaskContextType getContextType();
    public abstract boolean isOk(TaskContextAction taskContextAction)/* throws TaskContextException*/;

    @Override
    public String toString() {
        return "contextType = "+ getContextType();
    }
}

package ahtewlg7.utimer.entity.taskContext;

import org.joda.time.Instant;

import ahtewlg7.utimer.enumtype.TaskContextType;
import ahtewlg7.utimer.exception.TaskContextException;
import ahtewlg7.utimer.taskContext.TaskContextAction;

/**
 * Created by lw on 2017/12/6.
 */

public class TimeInstantContext extends ATaskContext {
    public static final String TAG = TimeInstantContext.class.getSimpleName();

    private Instant timeInstants;

    public Instant getTimeInstants() {
        return timeInstants;
    }

    public void setTimeInstants(Instant timeInstants) {
        this.timeInstants = timeInstants;
    }

    @Override
    public TaskContextType getContextType() {
        return TaskContextType.TIME_INSTANTS;
    }

    @Override
    public boolean isOk(TaskContextAction taskContextAction) throws TaskContextException {
        return taskContextAction.isContextOk(this);
    }
}

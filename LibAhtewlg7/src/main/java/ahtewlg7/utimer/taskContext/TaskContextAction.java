package ahtewlg7.utimer.taskContext;

import org.joda.time.Instant;

import ahtewlg7.utimer.entity.taskContext.TimeInstantContext;
import ahtewlg7.utimer.enumtype.MdContextErrCode;
import ahtewlg7.utimer.exception.TaskContextException;

/**
 * Created by lw on 2017/12/6.
 */

public class TaskContextAction {
    public static final String TAG = TaskContextAction.class.getSimpleName();

    public boolean isContextOk(TimeInstantContext timeInstantContext) throws TaskContextException{
        Instant timeInstant = timeInstantContext.getTimeInstants();
        if( timeInstant== null)
            throw new TaskContextException(MdContextErrCode.ERR_CONTEXT_NULL);
        return timeInstant.isEqualNow() || timeInstant.isAfterNow();
    }
}

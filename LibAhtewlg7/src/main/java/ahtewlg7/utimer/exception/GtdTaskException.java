package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.GtdErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class GtdTaskException extends RuntimeException {
    public GtdTaskException(GtdErrCode errCode){
        this(getMessage(errCode));
    }

    public GtdTaskException(String message){
        super(message);
    }

    private static String getMessage(GtdErrCode code){
        String message = "Task error";
        switch (code){
            case ERR_TASK_ENTITY_NULL:
                message = "TaskEntity null";
                break;
            case ERR_TASK_STEP_NULL:
                message = "Task step null";
                break;
            case ERR_TASK_HOLDED:
                message = "this task has been holded";
                break;
            case ERR_TASK_DONE:
                message = "this task has been done";
                break;
            default:
                break;
        }
        return message;
    }
}

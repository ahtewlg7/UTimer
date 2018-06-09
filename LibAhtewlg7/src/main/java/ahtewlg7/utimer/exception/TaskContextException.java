package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.MdContextErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class TaskContextException extends RuntimeException {
    public TaskContextException(MdContextErrCode errCode){
        this(getMessage(errCode));
    }

    public TaskContextException(String message){
        super(message);
    }

    private static String getMessage(MdContextErrCode code){
        String message = "context error";
        switch (code){
            case ERR_CONTEXT_NULL:
                message = "context is null";
                break;
            default:
                break;
        }
        return message;
    }
}

package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.GtdErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class GtdIncomingException extends GtdMachineException {
    public static final String TAG = GtdIncomingException.class.getSimpleName();

    public GtdIncomingException(GtdErrCode errCode){
        this(getMessage(errCode));
    }

    public GtdIncomingException(String message){
        super(message);
    }

    static String getMessage(GtdErrCode code){
        String message = "incoming error";
        switch (code){
            case ERR_INCOMING_DISABLE:
                message = "this gtd Entity can't to incoming";
                break;
            case ERR_INCOMING_STEP_NULL:
                message = "the taskStep of this gtd Entity null";
                break;
            default:
                message = GtdMachineException.getMessage(code);
                break;
        }
        return message;
    }
}

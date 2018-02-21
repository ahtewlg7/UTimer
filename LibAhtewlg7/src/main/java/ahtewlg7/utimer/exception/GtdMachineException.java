package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.GtdErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class GtdMachineException extends RuntimeException {
    public static final String TAG = GtdMachineException.class.getSimpleName();

    public GtdMachineException(GtdErrCode errCode){
        this(getMessage(errCode));
    }

    public GtdMachineException(String message){
        super(message);
    }


    static String getMessage(GtdErrCode errCode){
        String message = "Gtd Action error";
        switch (errCode){
            case ERR_ACTION_CANCEL:
                message = "this action is canceled";
                break;
            case ERR_STATE_NULL:
                message = "curr state is null";
                break;
            case ERR_GTDENTITY_NULL:
                message = "curr GtdEntity is null";
                break;
            default:
                break;
        }
        return message;
    }
}

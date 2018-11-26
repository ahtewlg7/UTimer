package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class UtimerEditException extends RuntimeException {
    public UtimerEditException(NoteEditErrCode errCode){
        this(getMessage(errCode));
    }

    public UtimerEditException(String message){
        super(message);
    }

    private static String getMessage(NoteEditErrCode code){
        String message = "note edit error";
        switch (code){
            case ERR_EDIT_ENTITY_INVALID:
                message = "UtimerEntity is null";
                break;
            case ERR_EDIT_VIEW_NULL:
                message = "view is null";
                break;
            default:
                break;
        }
        return message;
    }
}

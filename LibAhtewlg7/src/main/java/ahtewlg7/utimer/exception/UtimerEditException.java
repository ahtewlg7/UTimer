package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class UtimerEditException extends RuntimeException {
    private NoteEditErrCode errCode;

    public UtimerEditException(NoteEditErrCode errCode){
        this(getMessage(errCode));
        this.errCode = errCode;
    }

    public UtimerEditException(String message){
        super(message);
    }

    public NoteEditErrCode getErrCode(){
        return errCode;
    }

    private static String getMessage(NoteEditErrCode code){
        String message = "edit error";
        switch (code){
            case ERR_EDIT_ENTITY_NOT_READY:
                message = "UtimerEntity is null";
                break;
            case ERR_EDIT_ATTACH_VIEW_NOT_READY:
                message = "view is null";
                break;
            case ERR_EDIT_ALL_READY_GO:
                message = "is editing , no need to do it again";
                break;
            default:
                break;
        }
        return message;
    }
}

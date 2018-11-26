package ahtewlg7.utimer.exception.un;

import ahtewlg7.utimer.enumtype.errcode.NoteEditErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class NoteEditException extends RuntimeException {
    public NoteEditException(NoteEditErrCode errCode){
        this(getMessage(errCode));
    }

    public NoteEditException(String message){
        super(message);
    }

    private static String getMessage(NoteEditErrCode code){
        String message = "note edit error";
        switch (code){
            case ERR_EDIT_VIEW_NULL:
                message = "edit view is null";
                break;
            default:
                break;
        }
        return message;
    }
}
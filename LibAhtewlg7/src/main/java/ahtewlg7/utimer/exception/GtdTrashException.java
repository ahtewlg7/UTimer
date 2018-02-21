package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.GtdErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class GtdTrashException extends RuntimeException {
    public GtdTrashException(GtdErrCode errCode){
        this(getMessage(errCode));
    }

    public GtdTrashException(String message){
        super(message);
    }

    private static String getMessage(GtdErrCode code){
        String message = "Trash error";
        switch (code){
            case ERR_TRASH_DISABLE:
                message = "Link non null , can't put in trash";
                break;
            case ERR_UNTRASH_DISABLE:
                message = "Link null , can't go out of trash";
                break;
            default:
                break;
        }
        return message;
    }
}

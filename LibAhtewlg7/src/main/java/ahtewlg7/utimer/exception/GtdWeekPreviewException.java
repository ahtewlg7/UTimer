package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.GtdErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class GtdWeekPreviewException extends RuntimeException {
    public GtdWeekPreviewException(GtdErrCode errCode){
        this(getMessage(errCode));
    }

    public GtdWeekPreviewException(String message){
        super(message);
    }

    private static String getMessage(GtdErrCode code){
        String message = "Trash error";
        switch (code){
            case ERR_WEEK_PREVIEW_DISABLE:
                message = "the GtdEntity is not need to preview weekly";
                break;
            case ERR_WEEK_PREVIEW_DUPLICATE:
                message = "the week is previewed , no need to do it again";
                break;
            default:
                break;
        }
        return message;
    }
}

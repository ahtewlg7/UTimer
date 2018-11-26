package ahtewlg7.utimer.exception.un;

import ahtewlg7.utimer.enumtype.MdContextErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class MdContextException extends RuntimeException {
    public MdContextException(MdContextErrCode errCode){
        this(getMessage(errCode));
    }

    public MdContextException(String message){
        super(message);
    }

    private static String getMessage(MdContextErrCode code){
        String message = "md context error";
        switch (code){
            case ERR_CONTEXT_FILEPATH_INVALID:
                message = "the mdContext filePath is invalid";
                break;
            case ERR_CONTEXT_FILE_NO_EXIST:
                message = "the mdContext file is not existed";
                break;
            default:
                break;
        }
        return message;
    }
}

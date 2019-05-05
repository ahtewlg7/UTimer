package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.errcode.NlpErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class NlpException extends RuntimeException {
    private NlpErrCode errCode;

    public NlpException(NlpErrCode errCode){
        this(getMessage(errCode));
        this.errCode = errCode;
    }

    public NlpException(String message){
        super(message);
    }

    public NlpErrCode getErrCode(){
        return errCode;
    }

    private static String getMessage(NlpErrCode code){
        String message = "NLP error";
        switch (code){
            case ERR_NLP_ABSENT:
                message = "NLP ";
                break;
            default:
                break;
        }
        return message;
    }
}

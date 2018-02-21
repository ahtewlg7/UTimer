package ahtewlg7.utimer.exception;

import ahtewlg7.utimer.enumtype.DbErrCode;

/**
 * Created by lw on 2017/10/5.
 */

public class DataBaseException extends RuntimeException {
    private DbErrCode errCode;

    public DataBaseException(DbErrCode errCode){
        this(getMessage(errCode));
        this.errCode = errCode;
    }

    public DataBaseException(String message){
        super(message);
    }

    public DbErrCode getErrCode(){
        return errCode;
    }

    private static String getMessage(DbErrCode code){
        String message = "DataBase error";
        switch (code){
            case ERR_DB_ID_EMPTY:
                message = "DB id is is empty";
                break;
            case ERR_DB_BEAN_NULL:
                message = "this bean is not exist in db";
                break;
            default:
                break;
        }
        return message;
    }
}

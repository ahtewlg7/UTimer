package ahtewlg7.utimer.gtd;

import com.google.common.io.CharSink;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.md.MyBypass;
import ahtewlg7.utimer.util.FileIOAction;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2018/12/9.
 */
public class BaseTxtEditAction<T extends AUtimerEntity>  extends AEditAction<T> {
    private MyBypass myBypass;
    private FileIOAction fileIOAction;

    public BaseTxtEditAction(T t) {
        super(t);
        myBypass = new MyBypass();
    }

    @Override
    public boolean ifReady(){
        return t != null && t.ifValid()
                && t.getAttachFile() != null && t.ensureAttachFileExist();
    }

    @Override
    public boolean toSave(String rawTxt){
        return toSave(rawTxt, true);
    }

    public boolean toSave(String rawTxt, boolean append){
        if(rawTxt == null || !ifReady()) {
            Logcat.i(TAG,"toSave cancel");
            return false;
        }
        boolean result = false;
        try{
            if(fileIOAction == null)
                fileIOAction = new FileIOAction(t.getAttachFile());
            CharSink writer = fileIOAction.getCharWriter();
            if(append)
                writer = fileIOAction.getAppendCharWriter();
            writer.write(rawTxt);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }
}

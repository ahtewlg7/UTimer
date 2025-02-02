package ahtewlg7.utimer.gtd;

import android.text.TextUtils;

import com.google.common.io.CharSink;

import ahtewlg7.utimer.entity.ABaseMaterialEntity;
import ahtewlg7.utimer.util.FileIOAction;
import ahtewlg7.utimer.util.Logcat;

/**
 * Created by lw on 2018/12/9.
 */
public class BaseTxtEditAction<T extends ABaseMaterialEntity>  extends AEditAction<T> {
    private FileIOAction fileIOAction;

    public BaseTxtEditAction(T t) {
        super(t);
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
            String tmp = rawTxt;
            if(!TextUtils.isEmpty(rawTxt))
                tmp = rawTxt + System.getProperty("line.separator");
            writer.write(tmp);
            result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }

    public boolean toRename(String newName){
        return ifReady() && t.getAttachFile().renameFile(newName);
    }
}

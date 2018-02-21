package ahtewlg7.utimer.util;


import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;
import com.snatik.storage.Storage;

import java.io.File;


/**
 * Created by lw on 2016/4/28.
 */
public class StorageAction {
    public static final String TAG = StorageAction.class.getSimpleName();

    protected Storage storage;
    protected static StorageAction fileSystemAction;

    protected StorageAction() {
        storage    = new Storage(Utils.getApp().getApplicationContext());
    }

    public static StorageAction getInstance(){
        if(fileSystemAction == null){
            fileSystemAction = new StorageAction();
        }
        return fileSystemAction;
    }

    public boolean ifStorageReady(){
        return storage.isExternalWritable();
    }

    public String getExStoragePath(){
        return storage.getExternalStorageDirectory() + File.separator;
    }

    public Storage getStorage(){
        return storage;
    }

    //must use the absFilePath
    public boolean createExRelDir(String relPath){
        if(!ifStorageReady() || TextUtils.isEmpty(relPath)){
            Logcat.d(TAG,"createDir cancel");
            return false;
        }
        return FileUtils.createOrExistsDir(relPath);
    }
}

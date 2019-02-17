package ahtewlg7.utimer.db;

import android.content.ContextWrapper;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.blankj.utilcode.util.Utils;

import java.io.File;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.StorageAction;

/**
 * Created by lw on 2018/1/5.
 */

public class ExDatabaseContext extends ContextWrapper {
    public static final String TAG = ExDatabaseContext.class.getSimpleName();

    public ExDatabaseContext() {
        super(Utils.getApp().getApplicationContext());
    }

    @Override
    public File getDatabasePath(String name) {
        if(!StorageAction.getInstance().ifStorageReady()){
            Logcat.i(TAG,"getDatabasePath : exStorage is not ready");
            return null;
        }
        if(TextUtils.isEmpty(name)){
            Logcat.i(TAG,"getDatabasePath : database name is empty");
            return null;
        }
        String dbFilePath = new FileSystemAction().getDbDataAbsPath() + name;
        boolean create = FileUtils.createOrExistsFile(dbFilePath);
        return FileUtils.getFileByPath(dbFilePath);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }

    @Override
    public SQLiteDatabase openOrCreateDatabase(String name, int mode, SQLiteDatabase.CursorFactory factory, DatabaseErrorHandler errorHandler) {
        return SQLiteDatabase.openOrCreateDatabase(getDatabasePath(name), factory);
    }
}

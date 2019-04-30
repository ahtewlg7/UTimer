package ahtewlg7.utimer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import ahtewlg7.utimer.db.autogen.ActionEntityGdBeanDao;
import ahtewlg7.utimer.db.autogen.NoteEntityGdBeanDao;
import ahtewlg7.utimer.db.autogen.ShortHandEntityGdBeanDao;


/**
 * Created by lw on 2016/9/8.
 */
class GreenDaoOpenHelper extends DatabaseOpenHelper {
    private static final int DBV_0_0_12  = 3;
    private static final int DBV_0_0_13  = 4;

    //the DB_VERSION must be same with schemaVersion of greenDao in LibAhtewlg7 build.gradle
    public static int DB_VERSION = DBV_0_0_13;

    public GreenDaoOpenHelper(Context context, String name) {
        super(context, name, DB_VERSION);
    }

    public GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        ShortHandEntityGdBeanDao.createTable(wrap(db),true);
        NoteEntityGdBeanDao.createTable(wrap(db),true);
        ActionEntityGdBeanDao.createTable(wrap(db),true);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion){
            return;
        }
        if( oldVersion == DBV_0_0_12){
            db.execSQL("ALTER TABLE " + ActionEntityGdBeanDao.TABLENAME + " ADD COLUMN "
                    + ActionEntityGdBeanDao.Properties.WarningTimeList.columnName + " TEXT");
//                    + ActionEntityGdBeanDao.Properties.TimeElementList.columnName + " INTEGER");
            oldVersion = DBV_0_0_13;
        }
    }
}

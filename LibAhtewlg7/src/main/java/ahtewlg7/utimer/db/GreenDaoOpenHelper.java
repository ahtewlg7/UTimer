package ahtewlg7.utimer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import ahtewlg7.utimer.db.autogen.ActionEntityGdBeanDao;
import ahtewlg7.utimer.db.autogen.DeedEntityGdBeanDao;
import ahtewlg7.utimer.db.autogen.NoteEntityGdBeanDao;
import ahtewlg7.utimer.db.autogen.ShortHandEntityGdBeanDao;


/**
 * Created by lw on 2016/9/8.
 */
class GreenDaoOpenHelper extends DatabaseOpenHelper {
    private static final int DBV_0_0_12  = 3;
    private static final int DBV_0_0_13  = 4;
    private static final int DBV_0_0_14  = 5;
    private static final int DBV_0_0_17  = 6;
    private static final int DBV_0_0_18  = 7;

    //the DB_VERSION must be same with schemaVersion of greenDao in LibAhtewlg7 build.gradle
    public static int DB_VERSION = DBV_0_0_18;

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
        DeedEntityGdBeanDao.createTable(wrap(db),true);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion){
            return;
        }
        if(oldVersion == DBV_0_0_12){
            db.execSQL("ALTER TABLE " + DeedEntityGdBeanDao.TABLENAME + " ADD COLUMN "
                    + DeedEntityGdBeanDao.Properties.WarningTimeList.columnName + " TEXT");
            oldVersion = DBV_0_0_13;
        }
        if(oldVersion == DBV_0_0_13){
            db.execSQL("ALTER TABLE " + ActionEntityGdBeanDao.TABLENAME + " RENAME TO " + DeedEntityGdBeanDao.TABLENAME);
            oldVersion = DBV_0_0_14;
        }
        if(oldVersion == DBV_0_0_14){
            db.execSQL("delete from " + ShortHandEntityGdBeanDao.TABLENAME);
            db.execSQL("delete from " + NoteEntityGdBeanDao.TABLENAME);
            oldVersion = DBV_0_0_17;
        }
    }
}

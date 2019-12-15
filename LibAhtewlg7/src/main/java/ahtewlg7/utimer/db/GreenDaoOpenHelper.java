package ahtewlg7.utimer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseOpenHelper;

import ahtewlg7.utimer.db.autogen.DeedEntityGdBeanDao;
import ahtewlg7.utimer.db.autogen.MaterialEntityGdBeanDao;
import ahtewlg7.utimer.db.autogen.ProjectEntityGdBeanDao;
import ahtewlg7.utimer.util.Logcat;


/**
 * Created by lw on 2016/9/8.
 * if you want to upgrade db by greendao , you just need to ALTER TABLE in "onUpgrade",
 * no need to modify the schemaVersion of greendao in build.gradle
 */
class GreenDaoOpenHelper extends DatabaseOpenHelper {
    private static final int DBV_0_0_12   = 3;
    private static final int DBV_0_0_13   = 4;
    private static final int DBV_0_0_14   = 5;
    private static final int DBV_0_0_17   = 6;
    private static final int DBV_0_0_18   = 7;
    private static final int DBV_0_01_07  = 8;
    private static final int DBV_0_02_01  = 9;
    private static final int DBV_0_02_03  = 10;

    //the DB_VERSION must be same with schemaVersion of greenDao in LibAhtewlg7 build.gradle
    public static int DB_VERSION = DBV_0_02_03;

    public GreenDaoOpenHelper(Context context, String name) {
        super(context, name, DB_VERSION);
    }

    public GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        DeedEntityGdBeanDao.createTable(wrap(db),true);
        MaterialEntityGdBeanDao.createTable(wrap(db),true);
        ProjectEntityGdBeanDao.createTable(wrap(db),true);
    }
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Logcat.d("onUpgrade","onUpgrade oldVersion = " + oldVersion + ", newVersion =" +newVersion);
        if(oldVersion >= newVersion){
            return;
        }
        if(oldVersion == DBV_0_0_12){
            db.execSQL("ALTER TABLE " + DeedEntityGdBeanDao.TABLENAME + " ADD COLUMN "
                    + DeedEntityGdBeanDao.Properties.WarningTimeList.columnName + " TEXT");
            oldVersion = DBV_0_0_13;
        }
        if(oldVersion == DBV_0_0_13){
            db.execSQL("ALTER TABLE ACTION RENAME TO " + DeedEntityGdBeanDao.TABLENAME);
            oldVersion = DBV_0_0_14;
        }
        if(oldVersion == DBV_0_0_14){
            db.execSQL("delete from SHORTHAND");
            db.execSQL("delete from NOTE");
            oldVersion = DBV_0_0_17;
        }
        if(oldVersion == DBV_0_0_17){
            //just for test
            oldVersion = DBV_0_0_18;
        }
        if(oldVersion == DBV_0_0_18){
            db.execSQL("ALTER TABLE " + DeedEntityGdBeanDao.TABLENAME + " ADD COLUMN " + DeedEntityGdBeanDao.Properties.CreateTime.columnName + " TEXT");
            db.execSQL("ALTER TABLE " + DeedEntityGdBeanDao.TABLENAME + " ADD COLUMN " + DeedEntityGdBeanDao.Properties.StartTime.columnName + " TEXT");
            db.execSQL("ALTER TABLE " + DeedEntityGdBeanDao.TABLENAME + " ADD COLUMN " + DeedEntityGdBeanDao.Properties.EndTime.columnName + " TEXT");
            db.execSQL("CREATE TABLE IF NOT EXISTS tmp( " + DeedEntityGdBeanDao.Properties.Id.columnName + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
                    + DeedEntityGdBeanDao.Properties.Uuid.columnName + " TEXT NOT NULL , "
                    + DeedEntityGdBeanDao.Properties.Title.columnName + " TEXT NOT NULL , "
                    + DeedEntityGdBeanDao.Properties.Detail.columnName + " TEXT NOT NULL ,"
                    + DeedEntityGdBeanDao.Properties.AttachFileRPath.columnName + " TEXT, "
                    + DeedEntityGdBeanDao.Properties.ActionState.columnName + " INTEGER, "
                    + DeedEntityGdBeanDao.Properties.CreateTime.columnName + " INTEGER, "
                    + DeedEntityGdBeanDao.Properties.StartTime.columnName + " INTEGER, "
                    + DeedEntityGdBeanDao.Properties.EndTime.columnName + " INTEGER, "
                    + DeedEntityGdBeanDao.Properties.WarningTimeList.columnName + " TEXT);");
            db.execSQL("INSERT INTO tmp SELECT " + DeedEntityGdBeanDao.Properties.Id.columnName + ", " + DeedEntityGdBeanDao.Properties.Uuid.columnName
                    + ", " + DeedEntityGdBeanDao.Properties.Title.columnName + ", " + DeedEntityGdBeanDao.Properties.Detail.columnName
                    + ", " + DeedEntityGdBeanDao.Properties.AttachFileRPath.columnName + ", " + DeedEntityGdBeanDao.Properties.ActionState.columnName
                    + ", " + DeedEntityGdBeanDao.Properties.CreateTime.columnName + ", " + DeedEntityGdBeanDao.Properties.StartTime.columnName
                    + ", " + DeedEntityGdBeanDao.Properties.EndTime.columnName + ", " + DeedEntityGdBeanDao.Properties.WarningTimeList.columnName
                    + " FROM " + DeedEntityGdBeanDao.TABLENAME);
            db.execSQL("DROP TABLE IF EXISTS " + DeedEntityGdBeanDao.TABLENAME);
            db.execSQL("ALTER TABLE tmp RENAME TO " + DeedEntityGdBeanDao.TABLENAME);
            db.execSQL("CREATE UNIQUE INDEX " + DeedEntityGdBeanDao.TABLENAME + "IDX_DEED_UUID ON \"DEED\"" + " (\"UUID\" ASC);");
            oldVersion = DBV_0_01_07;
        }
        if(oldVersion == DBV_0_01_07){
            db.execSQL("DROP TABLE IF EXISTS SHORTHAND");
            db.execSQL("DROP TABLE IF EXISTS NOTE");
            MaterialEntityGdBeanDao.createTable(wrap((SQLiteDatabase) db.getRawDatabase()),true);
            ProjectEntityGdBeanDao.createTable(wrap((SQLiteDatabase) db.getRawDatabase()),true);
            oldVersion = DBV_0_02_01;
        }
        if(oldVersion == DBV_0_02_01){
            db.execSQL("ALTER   " + DeedEntityGdBeanDao.TABLENAME + " ADD COLUMN " + DeedEntityGdBeanDao.Properties.IsLink.columnName + " BOOLEAN");
            oldVersion = DBV_0_02_03;
        }
    }
}

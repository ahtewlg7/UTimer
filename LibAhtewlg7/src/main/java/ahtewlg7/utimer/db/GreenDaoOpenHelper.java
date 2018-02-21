package ahtewlg7.utimer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import ahtewlg7.utimer.db.autogen.DaoMaster;
import ahtewlg7.utimer.db.autogen.GtdEntityGdBeanDao;
import ahtewlg7.utimer.util.Logcat;


/**
 * Created by lw on 2016/9/8.
 */
class GreenDaoOpenHelper extends DaoMaster.DevOpenHelper {
    public static final String TAG = GreenDaoOpenHelper.class.getSimpleName();

    private static final int DBV_0_0_1 = 1;
    private static final int DBV_0_0_2 = 2;
    private static final int DBV_0_0_3 = 3;

    public GreenDaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    public GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        Logcat.i(TAG, "onUpgrade oldVersion = " + oldVersion + ", newVersion" + newVersion);
        if(oldVersion >= newVersion){
            Logcat.i(TAG, "no need to update database");
            return;
        }
        if( oldVersion == DBV_0_0_1){
            Logcat.i(TAG,"Doing upgrade from " + DBV_0_0_1+ " to " + DBV_0_0_2
                    + ", add " + GtdEntityGdBeanDao.Properties.GtdType.columnName
                    + ", add " + GtdEntityGdBeanDao.Properties.LastAccessTime.columnName);
            db.execSQL("ALTER TABLE " + GtdEntityGdBeanDao.TABLENAME + " ADD COLUMN "
                    + GtdEntityGdBeanDao.Properties.GtdType.columnName + " INTEGER");
            db.execSQL("ALTER TABLE " + GtdEntityGdBeanDao.TABLENAME + " ADD COLUMN "
                    + GtdEntityGdBeanDao.Properties.LastAccessTime.columnName + " TEXT");
            oldVersion = DBV_0_0_2;
        }
        if(oldVersion == DBV_0_0_2){
            Logcat.i(TAG,"Doing upgrade from " + DBV_0_0_2+ " to " + DBV_0_0_3);
            db.execSQL("ALTER TABLE " + GtdEntityGdBeanDao.TABLENAME + " ADD COLUMN "
                    + GtdEntityGdBeanDao.Properties.CreateTime.columnName + " TEXT");
            oldVersion = DBV_0_0_3;
        }
    }
}

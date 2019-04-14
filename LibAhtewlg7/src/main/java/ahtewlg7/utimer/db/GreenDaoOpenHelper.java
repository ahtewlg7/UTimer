package ahtewlg7.utimer.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import ahtewlg7.utimer.db.autogen.DaoMaster;
import ahtewlg7.utimer.db.autogen.ShortHandEntityGdBeanDao;


/**
 * Created by lw on 2016/9/8.
 */
class GreenDaoOpenHelper extends DaoMaster.DevOpenHelper {
    private static final int DBV_0_0_1 = 1;
    private static final int DBV_0_0_9 = 2;

    public GreenDaoOpenHelper(Context context, String name) {
        super(context, name);
    }

    public GreenDaoOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion){
            return;
        }
        if( oldVersion == DBV_0_0_1){
            db.execSQL("ALTER TABLE " + ShortHandEntityGdBeanDao.TABLENAME + " ADD COLUMN "
                    + ShortHandEntityGdBeanDao.Properties.AttachFileRPath.columnName + " INTEGER");
            oldVersion = DBV_0_0_9;
        }
    }
}

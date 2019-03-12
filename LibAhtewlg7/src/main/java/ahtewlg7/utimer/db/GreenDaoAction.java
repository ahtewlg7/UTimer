package ahtewlg7.utimer.db;

import ahtewlg7.utimer.db.autogen.DaoMaster;
import ahtewlg7.utimer.db.autogen.DaoSession;


/**
 * Created by lw on 2016/9/6.
 */
public class GreenDaoAction {
    public static final String TAG = GreenDaoAction.class.getSimpleName();

    public static final String DATABASE_NAME = "transaction";
    public static GreenDaoAction instance;

    private GreenDaoOpenHelper openHelper;
    private DaoMaster daoMaster;

    private GreenDaoAction(){
        openHelper = new GreenDaoOpenHelper(new ExDatabaseContext(), DATABASE_NAME,null);
//        openHelper = new GreenDaoOpenHelper(Utils.getApp().getApplicationContext(), DATABASE_NAME,null);
    }

    public static GreenDaoAction getInstance(){
        if(instance == null)
            instance = new GreenDaoAction();
        return instance;
    }

    public void init(){
        daoMaster = new DaoMaster(openHelper.getWritableDatabase());
    }

    public DaoSession getDaoSession(){
        return daoMaster.newSession();
    }
}

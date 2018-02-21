package ahtewlg7.utimer.common;


import java.io.File;

import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.StorageAction;
import ahtewlg7.utimer.verctrl.BaseConfig;


/**
 * Created by lw on 2016/4/28.
 */
public class FileSystemAction {
    public static final String TAG = FileSystemAction.class.getSimpleName();

    private BaseConfig baseConfig;

    public FileSystemAction(){
        baseConfig = new BaseConfig();
    }

    public void toInitWorkingEnv(){
        boolean ifStorageReady = StorageAction.getInstance().ifStorageReady();
        Logcat.d(TAG,"ifStorageInited = " + ifStorageReady);
        if(!ifStorageReady)
            return;
        initAppWorkingFsDir();
    }

    public String getSdcardPath(){
        return StorageAction.getInstance().getExStoragePath();
    }

    public String getAppWorkingRPath(){
        return baseConfig.getSimpleAppName() + File.separator;
    }
    public String getAppWorkingAbsPath(){
        return getSdcardPath() + getAppWorkingRPath();
    }

    public String getWorkingDataRPath(){
        return getAppWorkingRPath() + baseConfig.getWorkingDataDir() + File.separator;
    }
    public String getWorkingDataAbsPath(){
        return getSdcardPath() + getWorkingDataRPath();
    }

    public String getWorkingNoteRPath(){
        return getAppWorkingRPath() + baseConfig.getWorkingNoteDir() + File.separator;
    }
    public String getWorkingNoteAbsPath(){
        return getSdcardPath() + getWorkingNoteRPath();
    }
    //============================================================================================

    public String getWorkingBackUpRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingBackupDir() + File.separator;
    }
    public String getWorkingBackUpAbsPath(){
        return getSdcardPath() + getWorkingBackUpRPath();
    }

    public String getWorkingLogRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingLogDir() + File.separator;
    }
    public String getWorkingLogAbsPath(){
        return getSdcardPath() + getWorkingLogRPath();
    }

    public String getWorkingCacheRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingCacheDir() + File.separator;
    }
    public String getWorkingCacheAbsPath(){
        return getSdcardPath() + getWorkingCacheRPath();
    }

    public String getWorkingFontRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingFontDir() + File.separator;
    }
    public String getWorkingFontAbsPath(){
        return getSdcardPath() + getWorkingFontRPath();
    }

    public String getWorkingSaveRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingSaveDir() + File.separator;
    }
    public String getWorkingSaveAbsPath(){
        return getSdcardPath() + getWorkingSaveRPath();
    }

    public String getWorkingDbRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingDbDir() + File.separator;
    }
    public String getWorkingDbAbsPath(){
        return getSdcardPath() + getWorkingDbRPath();
    }

    public String getWorkingTmpRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingTmpDir() + File.separator;
    }
    public String getWorkingTmpAbsPath(){
        return getSdcardPath() + getWorkingTmpRPath();
    }

    public String getWorkingPluginRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingPluginDir() + File.separator;
    }
    public String getWorkingPluginAbsPath(){
        return getSdcardPath() + getWorkingPluginRPath();
    }

    //============================================================================================
    public String getDocNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getDocNoteDir() + File.separator;
    }
    public String getDocNoteAbsPath(){
        return getSdcardPath() + getDocNoteRPath();
    }


    public String getInboxNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getInboxNoteDir() + File.separator;
    }
    public String getInboxNoteAbsPath(){
        return getSdcardPath() + getInboxNoteRPath();
    }

    public String getProjectNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getProjectNoteDir() + File.separator;
    }
    public String getProjectNoteAbsPath(){
        return getSdcardPath() + getProjectNoteRPath();
    }

    public String getTrashNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getTrashNoteDir() + File.separator;
    }
    public String getTrashNoteAbsPath(){
        return getSdcardPath() + getTrashNoteRPath();
    }

    public String getReferenceNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getReferenceNoteDir() + File.separator;
    }
    public String getReferenceNoteAbsPath(){
        return getSdcardPath() + getReferenceNoteRPath();
    }

    public String getIncubatorNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getIncubatorNoteDir() + File.separator;
    }
    public String getIncubatorNoteAbsPath(){
        return getSdcardPath() + getIncubatorNoteRPath();
    }

    public String getDoneNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getDoneNoteDir() + File.separator;
    }
    public String getDoneNoteAbsPath(){
        return getSdcardPath() + getDoneNoteRPath();
    }

    public String getDoItNowNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getDoItNowNoteDir() + File.separator;
    }
    public String getDoItNowNoteAbsPath(){
        return getSdcardPath() + getDoItNowNoteRPath();
    }

    public String getDelegateNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getDelegateNoteDir() + File.separator;
    }
    public String getDelegateNoteAbsPath(){
        return getSdcardPath() + getDelegateNoteRPath();
    }

    public String getDateBookNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getDateBookNoteDir() + File.separator;
    }
    public String getDateBookNoteAbsPath(){
        return getSdcardPath() + getDateBookNoteRPath();
    }

    public String getToDoListNoteRPath(){
        return getWorkingNoteRPath() + baseConfig.getToDoListNoteDir() + File.separator;
    }
    public String getToDoListNoteAbsPath(){
        return getSdcardPath() + getToDoListNoteRPath();
    }

    //============================================================================================

    private void initAppWorkingFsDir(){
        //==========================================UTIMER========================================
        StorageAction.getInstance().createExRelDir(getAppWorkingAbsPath());
        //==========================================WORKING=======================================
        StorageAction.getInstance().createExRelDir(getWorkingDataAbsPath());
        StorageAction.getInstance().createExRelDir(getWorkingNoteAbsPath());

        //============================================DATA========================================
        StorageAction.getInstance().createExRelDir(getWorkingDbAbsPath());
        StorageAction.getInstance().createExRelDir(getWorkingLogAbsPath());
        StorageAction.getInstance().createExRelDir(getWorkingBackUpAbsPath());
        StorageAction.getInstance().createExRelDir(getWorkingFontAbsPath());
        StorageAction.getInstance().createExRelDir(getWorkingSaveAbsPath());
        StorageAction.getInstance().createExRelDir(getWorkingTmpAbsPath());
        StorageAction.getInstance().createExRelDir(getWorkingPluginAbsPath());

        //============================================NOTE========================================
        StorageAction.getInstance().createExRelDir(getTrashNoteAbsPath());
        StorageAction.getInstance().createExRelDir(getDocNoteAbsPath());
    }
}

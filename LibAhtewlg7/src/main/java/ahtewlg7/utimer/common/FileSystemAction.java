package ahtewlg7.utimer.common;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import java.io.File;

import ahtewlg7.utimer.util.Logcat;
import ahtewlg7.utimer.util.StorageAction;
import ahtewlg7.utimer.verctrl.BaseConfig;
import ahtewlg7.utimer.verctrl.VcFactoryBuilder;


/**
 * Created by lw on 2016/4/28.
 */
public class FileSystemAction {
    public static final String TAG = FileSystemAction.class.getSimpleName();

    private BaseConfig baseConfig;

    public FileSystemAction(){
        Optional<? extends BaseConfig> configOptional = VcFactoryBuilder.getInstance().getVcConfig();
        if(configOptional.isPresent())
            baseConfig = configOptional.get();
        else
            baseConfig = new BaseConfig();
    }

    public void toInitWorkingEnv(){
        boolean ifStorageReady = StorageAction.getInstance().ifStorageReady();
        Logcat.d(TAG,"ifStorageInited = " + ifStorageReady);
        if(!ifStorageReady)
            return;
        initAppWorkingFsDir();
    }
    public String getRPath(@NonNull File file){
        String path   = file.getAbsolutePath();
        try{
            if(path.contains(getAppWorkingAbsPath()))
                path =  baseConfig.getSimpleAppName() + File.separator + path.split(getAppWorkingAbsPath())[1];
        }catch (Exception e){
            e.printStackTrace();
        }
        return path;
    }
    public Optional<String> getAbsPath(String rPath){
        if(TextUtils.isEmpty(rPath))
            return Optional.absent();
        String absPath = getAppWorkingAbsPath() + rPath;
        return Optional.of(absPath);
    }
    //========================================SDcard==============================================
    public String getSdcardPath(){
        return StorageAction.getInstance().getExStoragePath();
    }

    //========================================UTimer==============================================
    public String getAppWorkingRPath(){
        return baseConfig.getSimpleAppName() + File.separator;
    }
    public String getAppWorkingAbsPath(){
        return getSdcardPath() + getAppWorkingRPath();
    }

    //========================================Data/Doc============================================
    public String getWorkingDataRPath(){
        return getAppWorkingRPath() + baseConfig.getWorkingDataDir() + File.separator;
    }
    public String getWorkingDataAbsPath(){
        return getSdcardPath() + getWorkingDataRPath();
    }

    public String getWorkingDocRPath(){
        return getAppWorkingRPath() + baseConfig.getDocNoteDir() + File.separator;
    }
    public String getWorkingDocAbsPath(){
        return getSdcardPath() + getWorkingDocRPath();
    }
    //========================================DATA================================================
    public String getBackUpDataRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingBackupDir() + File.separator;
    }
    public String getBackUpDataAbsPath(){
        return getSdcardPath() + getBackUpDataRPath();
    }

    public String getNlpDataRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingNlpDir() + File.separator;
    }
    public String getNlpDataAbsPath(){
        return getSdcardPath() + getNlpDataRPath();
    }

    public String getLogDataRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingLogDir() + File.separator;
    }
    public String getLogDataAbsPath(){
        return getSdcardPath() + getLogDataRPath();
    }

    public String getCacheDataRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingCacheDir() + File.separator;
    }
    public String getCacheDataAbsPath(){
        return getSdcardPath() + getCacheDataRPath();
    }

    public String getFontDataRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingFontDir() + File.separator;
    }
    public String getFontDataAbsPath(){
        return getSdcardPath() + getFontDataRPath();
    }

    public String getSaveDataRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingSaveDir() + File.separator;
    }
    public String getSaveDataAbsPath(){
        return getSdcardPath() + getSaveDataRPath();
    }

    public String getDbDataRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingDbDir() + File.separator;
    }
    public String getDbDataAbsPath(){
        return getSdcardPath() + getDbDataRPath();
    }

    public String getTmpDataRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingTmpDir() + File.separator;
    }
    public String getTmpDataAbsPath(){
        return getSdcardPath() + getTmpDataRPath();
    }

    public String getPluginDataRPath(){
        return getWorkingDataRPath() + baseConfig.getWorkingPluginDir() + File.separator;
    }
    public String getPluginDataAbsPath(){
        return getSdcardPath() + getPluginDataRPath();
    }

    //========================================DOC=================================================
    public String getNoteDocRPath(){
        return getWorkingDocRPath() + baseConfig.getWorkingNoteDir() + File.separator;
    }
    public String getNoteDocAbsPath(){
        return getSdcardPath() + getNoteDocRPath();
    }

    public String getGtdDocRPath(){
        return getWorkingDocRPath() + baseConfig.getGtdNoteDir() + File.separator;
    }
    public String getGtdDocAbsPath(){
        return getSdcardPath() + getGtdDocRPath();
    }

    public String getTrashDocRPath(){
        return getWorkingDocRPath() + baseConfig.getTrashNoteDir() + File.separator;
    }
    public String getTrashDocAbsPath(){
        return getSdcardPath() + getTrashDocRPath();
    }
    //========================================GTD================================================
    public String getInboxGtdRPath(){
        return getGtdDocRPath() + baseConfig.getInboxNoteDir() + File.separator;
    }
    public String getInboxGtdAbsPath(){
        return getSdcardPath() + getInboxGtdRPath();
    }

    public String getProjectGtdRPath(){
        return getGtdDocRPath() + baseConfig.getProjectNoteDir() + File.separator;
    }
    public String getProjectGtdAbsPath(){
        return getSdcardPath() + getProjectGtdRPath();
    }

    public String getReferenceGtdRPath(){
        return getGtdDocRPath() + baseConfig.getReferenceNoteDir() + File.separator;
    }
    public String getReferenceGtdAbsPath(){
        return getSdcardPath() + getReferenceGtdRPath();
    }

    public String getIncubatorGtdRPath(){
        return getGtdDocRPath() + baseConfig.getIncubatorNoteDir() + File.separator;
    }
    public String getIncubatorGtdAbsPath(){
        return getSdcardPath() + getIncubatorGtdRPath();
    }

    public String getDoneGtdRPath(){
        return getGtdDocRPath() + baseConfig.getDoneNoteDir() + File.separator;
    }
    public String getDoneGtdAbsPath(){
        return getSdcardPath() + getDoneGtdRPath();
    }

    public String getDoItNowGtdRPath(){
        return getGtdDocRPath() + baseConfig.getDoItNowNoteDir() + File.separator;
    }
    public String getDoItNowGtdAbsPath(){
        return getSdcardPath() + getDoItNowGtdRPath();
    }

    public String getDelegateGtdRPath(){
        return getGtdDocRPath() + baseConfig.getDelegateNoteDir() + File.separator;
    }
    public String getDelegateGtdAbsPath(){
        return getSdcardPath() + getDelegateGtdRPath();
    }

    public String getDateBookGtdRPath(){
        return getGtdDocRPath() + baseConfig.getDateBookNoteDir() + File.separator;
    }
    public String getDateBookGtdAbsPath(){
        return getSdcardPath() + getDateBookGtdRPath();
    }

    public String getToDoListGtdRPath(){
        return getGtdDocRPath() + baseConfig.getToDoListNoteDir() + File.separator;
    }
    public String getToDoListGtdAbsPath(){
        return getSdcardPath() + getToDoListGtdRPath();
    }

    //============================================================================================

    private void initAppWorkingFsDir(){
        //==========================================/UTimer========================================
        StorageAction.getInstance().createExRelDir(getAppWorkingAbsPath());
        //=======================================/UTimer/***=======================================
        StorageAction.getInstance().createExRelDir(getWorkingDataAbsPath());
        StorageAction.getInstance().createExRelDir(getWorkingDocAbsPath());

        //======================================/UTimer/Data/=======================================
        StorageAction.getInstance().createExRelDir(getDbDataAbsPath());
        StorageAction.getInstance().createExRelDir(getNlpDataAbsPath());
        StorageAction.getInstance().createExRelDir(getLogDataAbsPath());
        StorageAction.getInstance().createExRelDir(getBackUpDataAbsPath());
        StorageAction.getInstance().createExRelDir(getCacheDataAbsPath());
        StorageAction.getInstance().createExRelDir(getFontDataAbsPath());
        StorageAction.getInstance().createExRelDir(getSaveDataAbsPath());
        StorageAction.getInstance().createExRelDir(getTmpDataAbsPath());
        StorageAction.getInstance().createExRelDir(getPluginDataAbsPath());

        //======================================/UTimer/Doc/========================================
        StorageAction.getInstance().createExRelDir(getTrashDocAbsPath());
        StorageAction.getInstance().createExRelDir(getNoteDocAbsPath());
        StorageAction.getInstance().createExRelDir(getGtdDocAbsPath());

        //======================================/UTimer/Doc/GTD====================================
        StorageAction.getInstance().createExRelDir(getInboxGtdAbsPath());
        StorageAction.getInstance().createExRelDir(getProjectGtdAbsPath());
    }
}

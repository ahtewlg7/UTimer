package ahtewlg7.utimer.verctrl;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2017/9/25.
 */

public class BaseConfig {
    public static final String TAG = BaseConfig.class.getSimpleName();

    public String getSimpleAppName(){
        return MyRInfo.getStringByID(R.string.configs_simple_app_name);
    }

    public String getSimpleDateTimeFormat(){
        return MyRInfo.getStringByID(R.string.config_datetime_format);
    }
    public String getSimpleDateFormat(){
        return MyRInfo.getStringByID(R.string.config_date_format);
    }

    public String getMdFileSuffix(){
        return MyRInfo.getStringByID(R.string.config_md_file_suffix);
    }
    public String getManagerFileSubffix(){
        return MyRInfo.getStringByID(R.string.config_manager_file_suffix);
    }
    public String getShortHandManagerFile(){
        return MyRInfo.getStringByID(R.string.config_shortHand_manager_file_name) + getManagerFileSubffix();
    }
    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public String getWorkingDataDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_data);
    }
    public String getWorkingDbDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_db);
    }
    public String getWorkingBackupDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_backup);
    }
    public String getWorkingNlpDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_nlp);
    }
    public String getWorkingFontDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_font);
    }
    public String getWorkingSaveDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_save);
    }
    public String getWorkingTmpDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_tmp);
    }
    public String getWorkingPluginDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_plugin);
    }
    public String getWorkingLogDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_log);
    }
    public String getWorkingCacheDir(){
        return MyRInfo.getStringByID(R.string.configs_app_working_dir_cache);
    }
    public String getDocNoteDir(){
        return MyRInfo.getStringByID(R.string.config_note_doc_dir_name);
    }

    public boolean ifMdEditToastable(){
        return true;
    }
}

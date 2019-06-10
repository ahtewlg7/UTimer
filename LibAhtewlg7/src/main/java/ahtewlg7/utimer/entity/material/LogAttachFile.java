package ahtewlg7.utimer.entity.material;

import android.support.annotation.NonNull;

import java.io.File;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.enumtype.StorageType;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2018/10/30.
 */
public class LogAttachFile extends AAttachFile {
    public LogAttachFile(File file) {
        super(file);
    }

    public LogAttachFile(String fileAbsPath) {
        super(fileAbsPath);
    }
    public LogAttachFile(String filePath, String fileName) {
        super(filePath, fileName);
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.LOG;
    }

    @NonNull
    @Override
    public String getFileSuffix() {
        return MyRInfo.getStringByID(R.string.config_log_file_name);
    }
}

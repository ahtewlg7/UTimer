package ahtewlg7.utimer.entity.material;

import androidx.annotation.NonNull;

import java.io.File;

import ahtewlg7.utimer.R;
import ahtewlg7.utimer.enumtype.StorageType;
import ahtewlg7.utimer.util.MyRInfo;

/**
 * Created by lw on 2018/10/30.
 */
public class MdAttachFile extends AAttachFile {
    public MdAttachFile(File file) {
        super(file);
    }

    public MdAttachFile(String fileAbsPath) {
        super(fileAbsPath);
    }
    public MdAttachFile(String filePath, String fileName) {
        super(filePath, fileName);
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.MD;
    }

    @NonNull
    @Override
    public String getFileSuffix() {
        return MyRInfo.getStringByID(R.string.config_note_file_suffix);
    }
}

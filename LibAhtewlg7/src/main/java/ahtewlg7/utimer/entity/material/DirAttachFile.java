package ahtewlg7.utimer.entity.material;

import androidx.annotation.NonNull;

import java.io.File;

import ahtewlg7.utimer.enumtype.StorageType;

/**
 * Created by lw on 2018/10/30.
 */
public class DirAttachFile extends AAttachFile {
    public static final String TAG = DirAttachFile.class.getSimpleName();

    public DirAttachFile(File file) {
        super(file);
    }

    public DirAttachFile(String filePath, String fileName) {
        super(filePath, fileName);
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.DIR;
    }

    @NonNull
    @Override
    public String getFileSuffix() {
        return "";
    }
}

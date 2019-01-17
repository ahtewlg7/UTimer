package ahtewlg7.utimer.entity.material;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.blankj.utilcode.util.FileUtils;
import com.google.common.base.Optional;
import com.google.common.io.Files;

import org.joda.time.DateTime;

import java.io.File;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.enumtype.StorageType;
import ahtewlg7.utimer.util.FileAttrAction;

/**
 * Created by lw on 2018/10/29.
 */
public abstract class AAttachFile {
    public static final String TAG = AAttachFile.class.getSimpleName();

    public abstract StorageType getStorageType();
    public abstract @NonNull String getFileSuffix();

    protected File file;
    protected String fileDir;
    protected String filename;
    protected FileAttrAction fileAttrAction;

    protected AAttachFile(File file){
        this.file = file;
        fileAttrAction = new FileAttrAction(file);
    }

    protected AAttachFile(String filePath, String fileName){
        if(!TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(fileName))
            file = new File(filePath, fileName + getFileSuffix().trim());
        fileAttrAction = new FileAttrAction(file);
    }

    public boolean createOrExist(){
        if(getStorageType() == StorageType.DIR)
            return FileUtils.createOrExistsDir(file);
        return FileUtils.createOrExistsFile(file);
    }

    public File getFile(){
        return file;
    }

    public Optional<String> getTitle() {
        if(ifValid())
            return Optional.of(Files.getNameWithoutExtension(file.getName()));
        return Optional.absent();
    }

    public void deleteOnExit(){
        if(ifValid())
            file.deleteOnExit();
    }

    public Optional<String> getMimeType(){
        return ifValid() ? Optional.of(new FileAttrAction(file).getMimeType()) : Optional.<String>absent();
    }

    public Optional<String> getAbsPath(){
        return ifValid() ? Optional.of(file.getAbsolutePath()) : Optional.<String>absent();
    }

    public Optional<String> getRPath(){
        return ifValid() ? Optional.of(new FileSystemAction().getRPath(file)) : Optional.<String>absent();
    }

    public boolean ifValid() {
        return file != null && file.exists();
    }

    public DateTime getCreateTime(){
        return fileAttrAction.getCreateTime();
    }
    public DateTime getLassAccessTime(){
        return fileAttrAction.getLassAccessTime();
    }
    public DateTime getLassModifyTime(){
        return fileAttrAction.getLassModifyTime();
    }
}

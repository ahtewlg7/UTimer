package ahtewlg7.utimer.entity;

import android.text.TextUtils;

import androidx.annotation.NonNull;

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
    protected AAttachFile(String fileAbsPath){
        if(!TextUtils.isEmpty(fileAbsPath)) {
            file = new File(fileAbsPath);
            fileAttrAction = new FileAttrAction(file);
        }
    }

    protected AAttachFile(String filePath, String fileName){
        if(!TextUtils.isEmpty(filePath) && !TextUtils.isEmpty(fileName)) {
            file = new File(filePath, fileName + getFileSuffix().trim());
            fileAttrAction = new FileAttrAction(file);
        }
    }

    public boolean renameFile(String fileName){
        return file != null && renameFile(file.getParentFile(), fileName);
    }

    public boolean renameFile(File parentFile, String fileName){
        if(parentFile == null || TextUtils.isEmpty(fileName))
            return false;
        return renameFile(parentFile.getPath(), fileName);
    }

    public boolean renameFile(String filePath, String fileName){
        if(TextUtils.isEmpty(filePath) || TextUtils.isEmpty(fileName))
            return false;
        String tmp = fileName + getFileSuffix().trim();
        boolean result = false;
        try{
            if(ifValid() && !tmp.equals(file.getName())) {
                Files.move(file, new File(filePath, tmp));
                file = new File(filePath, fileName + getFileSuffix().trim());
                fileAttrAction = new FileAttrAction(file);
                result = true;
            }else if(tmp.equals(file.getName()))
                result = true;
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
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

    public boolean delete(){
        return ifValid() && file.delete();
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

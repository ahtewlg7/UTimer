package ahtewlg7.utimer.entity.material;

import com.google.common.base.Optional;

import java.io.File;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.entity.ITipsEntity;
import ahtewlg7.utimer.entity.un.IUtimerEntity;
import ahtewlg7.utimer.enumtype.StorageType;
import ahtewlg7.utimer.util.FileAttrAction;

public abstract class AMaterialEntity implements IUtimerEntity, ITipsEntity{
    public static final String TAG = AMaterialEntity.class.getSimpleName();

    public abstract StorageType getStorageType();

    protected File file;
    protected String id;
    protected String title;
    protected String detail;

    public AMaterialEntity(File file){
        this.file = file;
    }
    public AMaterialEntity(String detail){
        this.detail = detail;
    }
    public AMaterialEntity(String filePath, String fileName){
        file = new File(filePath, fileName);
    }

    @Override
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
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

    @Override
    public boolean ifValid() {
        return file != null && file.exists();
    }
}

package ahtewlg7.utimer.entity.gtd;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.io.Serializable;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.db.entity.ProjectEntityGdBean;
import ahtewlg7.utimer.entity.ABaseMaterialEntity;
import ahtewlg7.utimer.entity.material.DirAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.enumtype.StorageType;
import ahtewlg7.utimer.util.DateTimeAction;


public class GtdProjectEntity extends ABaseMaterialEntity<GtdProjectBuilder> implements Serializable {
    protected GtdProjectEntity(@Nonnull GtdProjectBuilder builder) {
        super(builder);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
    }

    @NonNull
    @Override
    public GtdType getGtdType() {
        return GtdType.PROJECT;
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.DIR;
    }

    @Override
    public boolean ensureAttachFileExist() {
        if(attachFile == null){
            String fileName = !TextUtils.isEmpty(getTitle()) ? getTitle() : new DateTimeAction().toFormatNow();
            String filePath = new FileSystemAction().getNoteDocAbsPath();
            attachFile = new DirAttachFile(filePath, fileName);
        }
        return attachFile.createOrExist();
    }

    private void  initByGbBean(ProjectEntityGdBean gdBean) {
        uuid            = gdBean.getUuid();
        title           = gdBean.getTitle();
        detail          = gdBean.getDetail();
        createTime      = gdBean.getCreateTime();
        accessTimes     = gdBean.getAccessTimes();
        lastAccessTime  = gdBean.getLastAccessTime();
        lastModifyTime  = gdBean.getLastAccessTime();
    }
}

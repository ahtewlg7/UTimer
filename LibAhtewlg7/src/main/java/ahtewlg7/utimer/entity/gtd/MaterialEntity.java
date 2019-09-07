package ahtewlg7.utimer.entity.gtd;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.Serializable;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.db.entity.MaterialEntityGdBean;
import ahtewlg7.utimer.entity.ABaseMaterialEntity;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.enumtype.StorageType;
import ahtewlg7.utimer.util.DateTimeAction;

/**
 * Created by lw on 2019/1/23.
 */
public class MaterialEntity extends ABaseMaterialEntity<MaterialEntityBuilder>
        implements Serializable {
    private String absPath;

    protected MaterialEntity(@Nonnull MaterialEntityBuilder builder) {
        super(builder);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
        if(builder.projectEntity != null)
            initByProjectEntity(builder.projectEntity);
        if(!TextUtils.isEmpty(title) && attachFile == null){
            String filePath = new FileSystemAction().getWorkingDocAbsPath();
            attachFile      = new MdAttachFile(filePath, title);
        }
    }

    @NonNull
    @Override
    public GtdType getGtdType() {
        return GtdType.MATERIAL;
    }

    @Override
    public StorageType getStorageType() {
        return null;
    }

    @Override
    public Optional<String> getDetail() {
        return TextUtils.isEmpty(detail)? Optional.<String>absent() : Optional.of(detail);
    }

    @Override
    public boolean ifRawReadable() {
        return super.ifRawReadable() && ensureAttachFileExist();
    }

    @Override
    public boolean ensureAttachFileExist() {
        createTime = DateTime.now();
        return attachFile.createOrExist();
    }

    public String getAbsPath() {
        return absPath;
    }

    //++++++++++++++++++++++++++++++++++++++ITimeComparator++++++++++++++++++++++++++++++++++++
    private void initByProjectEntity(GtdProjectEntity projectEntity){
        if(projectEntity == null || !projectEntity.ifValid()){
            return;
        }
        if(TextUtils.isEmpty(title))
            title = new DateTimeAction().toFormatNow();

        if(attachFile == null){
            String filePath = new FileSystemAction().getWorkingDocAbsPath() + projectEntity.getTitle();
            attachFile      = new MdAttachFile(filePath, title);
        }
    }

    private void  initByGbBean(MaterialEntityGdBean gdBean){
        uuid            = gdBean.getUuid();
        title           = gdBean.getTitle();
        detail          = gdBean.getDetail();
        accessTimes     = gdBean.getAccessTimes();
        createTime      = gdBean.getCreateTime();
        lastAccessTime  = gdBean.getLastAccessTime();
        absPath         = gdBean.getAbsFilePath();
    }
}

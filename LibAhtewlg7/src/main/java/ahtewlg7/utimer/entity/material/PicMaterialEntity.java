package ahtewlg7.utimer.entity.material;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import java.io.File;

import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.enumtype.StorageType;

public class PicMaterialEntity extends AMaterialEntity {
    public static final String TAG = PicMaterialEntity.class.getSimpleName();

    public PicMaterialEntity(File file) {
        super(file);
    }
    public PicMaterialEntity(String detail) {
        super(detail);
    }
    public PicMaterialEntity(String filePath, String fileName) {
        super(filePath, fileName);
    }

    @NonNull
    @Override
    public GtdType getGtdType() {
        return GtdType.MATERIAL;
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.IMAGE;
    }
    @Override
    public Optional<String> toTips() {
        return Optional.absent();
    }
}

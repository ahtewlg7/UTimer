package ahtewlg7.utimer.entity.material;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import java.io.File;

import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.enumtype.StorageType;

public class VedioMaterialEntity extends AMaterialEntity {
    public static final String TAG = VedioMaterialEntity.class.getSimpleName();

    public VedioMaterialEntity(File file) {
        super(file);
    }
    public VedioMaterialEntity(String detail) {
        super(detail);
    }
    public VedioMaterialEntity(String filePath, String fileName) {
        super(filePath, fileName);
    }

    @NonNull
    @Override
    public GtdType getGtdType() {
        return GtdType.MATERIAL;
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.VIDEO;
    }
    @Override
    public Optional<String> toTips() {
        return Optional.absent();
    }
}

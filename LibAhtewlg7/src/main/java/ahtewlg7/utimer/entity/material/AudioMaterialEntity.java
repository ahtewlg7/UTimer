package ahtewlg7.utimer.entity.material;

import androidx.annotation.NonNull;

import com.google.common.base.Optional;

import java.io.File;

import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.enumtype.StorageType;

public class AudioMaterialEntity extends AMaterialEntity {
    public static final String TAG = AudioMaterialEntity.class.getSimpleName();

    public AudioMaterialEntity(File file) {
        super(file);
    }
    public AudioMaterialEntity(String detail) {
        super(detail);
    }
    public AudioMaterialEntity(String filePath, String fileName) {
        super(filePath, fileName);
    }

    @NonNull
    @Override
    public GtdType getGtdType() {
        return GtdType.MATERIAL;
    }

    @Override
    public StorageType getStorageType() {
        return StorageType.AUDIO;
    }

    @Override
    public Optional<String> toTips() {
        return Optional.absent();
    }
}

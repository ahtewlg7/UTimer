package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.entity.AGtdUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.AAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;


public class GtdProjectEntity extends AGtdUtimerEntity<GtdProjectBuilder> {
    public static final String TAG = GtdProjectEntity.class.getSimpleName();

    protected GtdProjectEntity(@Nonnull GtdProjectBuilder builder) {
        super(builder);
    }

    public @NonNull String toJson() {
        return "";
    }

    public boolean isDone() {
        return false;
    }

    @Override
    public GtdType getGtdType() {
        return GtdType.PROJECT;
    }

    public Optional<String> toTips() {
        return Optional.absent();
    }

    @Override
    public Optional<String> getDetail() {
        return null;
    }

    @Override
    public boolean ensureAttachFileExist() {
        return false;
    }

    @Override
    protected void initByAttachFile(AAttachFile attachFile) {

    }

    @Override
    public IMergerEntity merge(IMergerEntity entity) {
        return null;
    }
}

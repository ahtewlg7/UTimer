package ahtewlg7.utimer.entity.gtd;

import com.google.common.base.Optional;

import java.io.Serializable;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.entity.AGtdUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.enumtype.GtdType;


public class GtdProjectEntity extends AGtdUtimerEntity<GtdProjectBuilder> implements Serializable {
    public static final String TAG = GtdProjectEntity.class.getSimpleName();

    private StringBuilder detailBuilder;

    protected GtdProjectEntity(@Nonnull GtdProjectBuilder builder) {
        super(builder);
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
        return detailBuilder == null ? Optional.<String>absent() : Optional.of(detailBuilder.toString());
    }

    //todo
    @Override
    public IMergerEntity merge(IMergerEntity entity) {
        return entity;
    }
}

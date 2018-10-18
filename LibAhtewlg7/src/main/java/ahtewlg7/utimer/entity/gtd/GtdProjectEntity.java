package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import ahtewlg7.utimer.enumtype.GtdType;


public class GtdProjectEntity extends AGtdEntity {
    public static final String TAG = GtdProjectEntity.class.getSimpleName();

    private TipsEntity tipsEntity;
    private ShortHandEntity shortHandEntity;

    public GtdProjectEntity(){

    }
    public GtdProjectEntity(TipsEntity tipsEntity){

    }
    public GtdProjectEntity(ShortHandEntity shortHandEntity){

    }

    @Override
    public @NonNull String toJson() {
        return "";
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public GtdType getGtdType() {
        return GtdType.PROJECT;
    }

    @Override
    public Optional<String> toTips() {
        return Optional.absent();
    }
}

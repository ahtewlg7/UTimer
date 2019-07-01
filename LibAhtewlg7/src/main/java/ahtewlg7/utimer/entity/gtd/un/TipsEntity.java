package ahtewlg7.utimer.entity.gtd.un;

import com.google.common.base.Optional;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.AAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;

public class TipsEntity extends AUtimerEntity<TipsBuilder> {
    public static final String TAG = TipsEntity.class.getSimpleName();

    protected TipsEntity(TipsBuilder builder){
        super(builder);
    }

    @Nonnull
    @Override
    public GtdType getGtdType() {
        return GtdType.TIPS;
    }

    @Override
    public boolean ifValid() {
        return false;
    }

    @Override
    public void updateAttachFileInfo(AAttachFile attachFile) {

    }

    @Override
    public Optional<String> toTips() {
        return null;
    }

    @Override
    public Optional<String> getDetail() {
        return null;
    }

    @Override
    public void update(IMergerEntity entity) {
    }

    @Override
    public boolean ensureAttachFileExist() {
        return false;
    }

    private AGtdEntity attachGtdEntity;

    public AGtdEntity getAttachGtdEntity() {
        return attachGtdEntity;
    }

    public void setAttachGtdEntity(AGtdEntity attachGtdEntity) {
        this.attachGtdEntity = attachGtdEntity;
    }
}

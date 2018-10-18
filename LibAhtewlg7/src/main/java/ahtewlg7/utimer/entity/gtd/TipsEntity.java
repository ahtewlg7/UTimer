package ahtewlg7.utimer.entity.gtd;

import android.support.annotation.NonNull;

import com.google.common.base.Optional;

import java.io.File;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.enumtype.GtdType;

public class TipsEntity extends AUtimerEntity<TipsEntity.Builder> {
    public static final String TAG = TipsEntity.class.getSimpleName();

    private TipsEntity(Builder builder){
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
    protected void initByAttachFile(File attachFile) {

    }

    @Override
    public Optional<String> getDetail() {
        return null;
    }

    @Override
    public File getAttachFile() {
        return super.getAttachFile();
    }

    @Override
    public IMergerEntity merge(IMergerEntity entity) {
        return null;
    }

    private AGtdEntity attachGtdEntity;

    public AGtdEntity getAttachGtdEntity() {
        return attachGtdEntity;
    }

    public void setAttachGtdEntity(AGtdEntity attachGtdEntity) {
        this.attachGtdEntity = attachGtdEntity;
    }
    public static class Builder extends AUtimerEntity.Builder<TipsEntity>{

        @NonNull
        @Override
        public TipsEntity build() {
            return new TipsEntity(this);
        }
    }
}

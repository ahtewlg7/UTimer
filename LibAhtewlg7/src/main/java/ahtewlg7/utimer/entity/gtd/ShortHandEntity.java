package ahtewlg7.utimer.entity.gtd;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.File;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.db.entity.ShortHandEntityGdBean;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.AMaterialEntity;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.FileAttrAction;
import ahtewlg7.utimer.util.Logcat;

public class ShortHandEntity extends AUtimerEntity<ShortHandEntity.Builder>{
    public static final String TAG = ShortHandEntity.class.getSimpleName();

    private boolean isActived;
    private StringBuilder detailBuilder;

    private ShortHandEntity(@Nonnull Builder builder) {
        super(builder);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
    }

    @Override
    public Optional<String> getDetail() {
        return detailBuilder == null ? Optional.<String>absent()
                : Optional.of(detailBuilder.toString());
    }

    public void appendDetail(String append){
        if(TextUtils.isEmpty(append)){
            Logcat.i(TAG,"append String empty");
            return;
        }
        if(detailBuilder == null)
            detailBuilder = new StringBuilder(append);
        else
            detailBuilder.append(append);
    }
    public void appendDetail(AMaterialEntity materialEntity){
        if(materialEntity == null || !materialEntity.ifValid()) {
            Logcat.i(TAG,"append material cancel");
            return;
        }
        Optional<String> tips = materialEntity.toTips();
        if(tips.isPresent())
            appendDetail(tips.get());
    }

    public boolean isActived() {
        return isActived;
    }

    public void setActived(boolean actived) {
        isActived = actived;
    }

    public DateTime getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(DateTime lassModifyTime) {
        this.lastModifyTime = lassModifyTime;
    }

    @Override
    @NonNull
    public GtdType getGtdType() {
        return GtdType.SHORTHAND;
    }

    @Override
    public IMergerEntity merge(IMergerEntity entity) {
        return this;
    }

    protected void initByAttachFile(File attachFile){
        setTitle(attachFile.getName().split(".txt")[0]);
        if(attachFile != null && attachFile.exists()) {
            FileAttrAction fileAttrAction = new FileAttrAction(attachFile);
            createTime     = fileAttrAction.getCreateTime();
            lastAccessTime = fileAttrAction.getLassAccessTime();
            lastModifyTime = fileAttrAction.getLassModifyTime();
        }
    }

    private void  initByGbBean(ShortHandEntityGdBean inboxEntityGdBean){
        id              = String.valueOf(inboxEntityGdBean.getId());
        uuid            = inboxEntityGdBean.getUuid();
        title           = inboxEntityGdBean.getTitle();
        accessTimes     = inboxEntityGdBean.getAccessTimes();
        isActived       = inboxEntityGdBean.getIsActived();
        createTime      = inboxEntityGdBean.getCreateTime();
        lastAccessTime  = inboxEntityGdBean.getLastAccessTime();
        lastModifyTime  = inboxEntityGdBean.getLastModifyTime();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TAG);
        builder.append(super.toString());
        builder.append(",isActived").append(isActived);
        return builder.toString();
    }

    public static class Builder extends AUtimerEntity.Builder<ShortHandEntity>{
        protected ShortHandEntityGdBean gdBean;

        public Builder setGbBean(ShortHandEntityGdBean gdBean){
            this.gdBean = gdBean;
            return this;
        }

        @NonNull
        @Override
        public ShortHandEntity build() {
            return new ShortHandEntity(this);
        }
    }
}
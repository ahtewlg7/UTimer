package ahtewlg7.utimer.entity.gtd;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.Serializable;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.db.entity.ShortHandEntityGdBean;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.AAttachFile;
import ahtewlg7.utimer.entity.material.AMaterialEntity;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;

public class ShortHandEntity extends AUtimerEntity<ShortHandBuilder> implements Serializable {
    public static final String TAG = ShortHandEntity.class.getSimpleName();

    private boolean isActived;
    private StringBuilder detailBuilder;

    protected ShortHandEntity(@Nonnull ShortHandBuilder builder) {

        super(builder);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
        if(!TextUtils.isEmpty(title) && attachFile == null){
            String filePath = new FileSystemAction().getInboxGtdAbsPath();
            attachFile  = new MdAttachFile(filePath, title);
        }
    }

    @Override
    public Optional<String> getDetail() {
        return detailBuilder == null ? Optional.<String>absent() : Optional.of(detailBuilder.toString());
    }

    @Override
    public boolean ensureAttachFileExist() {
        if(attachFile == null){
            String fileName = !TextUtils.isEmpty(getTitle()) ? getTitle() : new DateTimeAction().toFormatNow().toString();
            String filePath = new FileSystemAction().getInboxGtdAbsPath();
            attachFile = new MdAttachFile(filePath, fileName);
        }
        boolean result = attachFile.createOrExist();
        Logcat.i(TAG,"ensureAttachFileExist result = " + result);
        return result;
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

    protected void initByAttachFile(AAttachFile attachFile){
        Optional<String> title = attachFile.getTitle();
        if(title.isPresent())
            setTitle(title.get());
        if(attachFile.ifValid()) {
            createTime     = attachFile.getCreateTime();
            lastAccessTime = attachFile.getLassAccessTime();
            lastModifyTime = attachFile.getLassModifyTime();
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
        builder.append(",").append(super.toString());
        builder.append(",isActived").append(isActived);
        return builder.toString();
    }
}

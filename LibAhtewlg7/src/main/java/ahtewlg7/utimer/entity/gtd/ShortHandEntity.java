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
import ahtewlg7.utimer.entity.material.MdAttachFile;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;

public class ShortHandEntity extends AUtimerEntity<ShortHandBuilder> implements Serializable {
    private EditElement lastModifyElement;

    protected ShortHandEntity(@Nonnull ShortHandBuilder builder) {
        super(builder);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
        if(!TextUtils.isEmpty(title) && attachFile == null){
            String filePath = new FileSystemAction().getInboxGtdAbsPath();
            attachFile      = new MdAttachFile(filePath, title);
        }
    }

    @Override
    public Optional<String> getDetail() {
        return TextUtils.isEmpty(detail)? Optional.<String>absent() : Optional.of(detail);
    }

    public EditElement getLastModifyElement() {
        return lastModifyElement;
    }

    public void setLastModifyElement(EditElement lastModifyElement) {
        this.lastModifyElement = lastModifyElement;
    }


    public DateTime getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(DateTime lassModifyTime) {
        this.lastModifyTime = lassModifyTime;
    }

    @Override
    public Optional<String> toTips() {
        return getDetail();
    }

    @Override
    @NonNull
    public GtdType getGtdType() {
        return GtdType.SHORTHAND;
    }

    @Override
    public void update(IMergerEntity entity) {

    }

    @Override
    public boolean ensureAttachFileExist() {
        if(attachFile == null){
            String fileName = !TextUtils.isEmpty(getTitle()) ? getTitle() : new DateTimeAction().toFormatNow().toString();
            String filePath = new FileSystemAction().getInboxGtdAbsPath();
            attachFile = new MdAttachFile(filePath, fileName);
        }
        return attachFile.createOrExist();
    }

    private void  initByGbBean(ShortHandEntityGdBean inboxEntityGdBean){
        id              = inboxEntityGdBean.getId();
        uuid            = inboxEntityGdBean.getUuid();
        title           = inboxEntityGdBean.getTitle();
        accessTimes     = inboxEntityGdBean.getAccessTimes();
        createTime      = inboxEntityGdBean.getCreateTime();
        lastAccessTime  = inboxEntityGdBean.getLastAccessTime();
    }
}

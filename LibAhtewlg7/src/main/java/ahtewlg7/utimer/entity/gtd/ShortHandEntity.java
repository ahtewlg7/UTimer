package ahtewlg7.utimer.entity.gtd;


import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.Serializable;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.comparator.ITimeComparator;
import ahtewlg7.utimer.db.entity.ShortHandEntityGdBean;
import ahtewlg7.utimer.entity.AUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.AAttachFile;
import ahtewlg7.utimer.entity.material.MdAttachFile;
import ahtewlg7.utimer.entity.md.EditElement;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;

public class ShortHandEntity extends AUtimerEntity<ShortHandBuilder>
        implements ITimeComparator,Serializable {
    private String rPath;
    private EditElement lastModifyElement;

    protected ShortHandEntity(@Nonnull ShortHandBuilder builder) {
        super(builder);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
        if(!TextUtils.isEmpty(title) && attachFile == null){
            String filePath         = new FileSystemAction().getInboxGtdAbsPath();
            attachFile              = new MdAttachFile(filePath, title);
        }
        updateRPath(attachFile);
    }

    @Override
    public Optional<String> getDetail() {
        return TextUtils.isEmpty(detail)? Optional.<String>absent() : Optional.of(detail);
    }

    @Override
    public boolean ifRawReadable() {
        return super.ifRawReadable() && ensureAttachFileExist();
    }

    //++++++++++++++++++++++++++++++++++++++ITimeComparator++++++++++++++++++++++++++++++++++++
    @Override
    public Optional<DateTime> getComparatorTime() {
        return Optional.fromNullable(lastAccessTime);
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
            FileSystemAction fileSystemAction = new FileSystemAction();
            Optional<String> absPath = fileSystemAction.getAbsPath(rPath);
            if(absPath.isPresent()) {
                attachFile = new MdAttachFile(absPath.get());
            }else{
                String fileName = !TextUtils.isEmpty(getTitle()) ? getTitle() : new DateTimeAction().toFormatNow().toString();
                String filePath = fileSystemAction.getInboxGtdAbsPath();
                attachFile = new MdAttachFile(filePath, fileName);
                updateRPath(attachFile);
            }
        }
        createTime = DateTime.now();
        return attachFile.createOrExist();
    }

    private void  initByGbBean(ShortHandEntityGdBean inboxEntityGdBean){
        uuid            = inboxEntityGdBean.getUuid();
        title           = inboxEntityGdBean.getTitle();
        detail          = inboxEntityGdBean.getDetail();
        accessTimes     = inboxEntityGdBean.getAccessTimes();
        createTime      = inboxEntityGdBean.getCreateTime();
        lastAccessTime  = inboxEntityGdBean.getLastAccessTime();
        rPath           = inboxEntityGdBean.getAttachFileRPath();
    }
    private void updateRPath(AAttachFile attachFile){
        if(attachFile == null || !attachFile.ifValid())
            return;
        Optional<String> tmp = attachFile.getRPath();
        if(tmp.isPresent())
            rPath = tmp.get();
    }
}

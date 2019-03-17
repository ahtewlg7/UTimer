package ahtewlg7.utimer.entity.gtd;

import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.db.entity.ActionEntityGdBean;
import ahtewlg7.utimer.entity.AGtdUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.material.DirAttachFile;
import ahtewlg7.utimer.enumtype.GtdActionType;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;
import ahtewlg7.utimer.util.Logcat;


public class GtdActionEntity extends AGtdUtimerEntity<GtdActionBuilder> implements Serializable {
    public static final String TAG = GtdActionEntity.class.getSimpleName();

    private int warningTimes;
    private List<DateTime> timeList;
    private GtdActionType actionType;

    protected GtdActionEntity(@Nonnull GtdActionBuilder builder) {
        super(builder);
        if(builder.timeList != null)
            timeList = builder.timeList;
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
        toMakeUuidOk();
    }

    @Override
    public boolean ifValid() {
        return super.ifValid() && !TextUtils.isEmpty(uuid);
    }

    @Override
    public GtdType getGtdType() {
        return GtdType.ACTION;
    }

    @Override
    public Optional<String> toTips() {
        return Optional.absent();
    }

    @Override
    public Optional<String> getDetail() {
        return TextUtils.isEmpty(detail) ? Optional.<String>absent() : Optional.of(detail);
    }

    public GtdActionType getActionType() {
        return actionType;
    }

    public void setActionType(GtdActionType actionType) {
        this.actionType = actionType;
    }

    public int getWarningTimes() {
        return warningTimes;
    }

    public void setWarningTimes(int warningTimes) {
        this.warningTimes = warningTimes;
    }

    //todo
    @Override
    public IMergerEntity merge(IMergerEntity entity) {
        return entity;
    }

    @Override
    public boolean ensureAttachFileExist() {
        if(attachFile == null){
            String fileName = !TextUtils.isEmpty(getTitle()) ? getTitle() : new DateTimeAction().toFormatNow().toString();
            String filePath = new FileSystemAction().getProjectGtdAbsPath();
            attachFile = new DirAttachFile(filePath, fileName);
        }
        boolean result = attachFile.createOrExist();
        Logcat.i(TAG,"ensureAttachFileExist result = " + result);
        return result;
    }

    public List<DateTime> getTimeList() {
        return timeList;
    }

    private void  initByGbBean(ActionEntityGdBean gdBean){
        uuid            = gdBean.getUuid();
        title           = gdBean.getTitle();
        detail          = gdBean.getDetail();
        timeList        = gdBean.getTimeList();
        createTime      = gdBean.getCreateTime();
        lastAccessTime  = gdBean.getCreateTime();
    }
}

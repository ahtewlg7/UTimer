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
import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;
import ahtewlg7.utimer.entity.w5h2.W5h2HowMuch;
import ahtewlg7.utimer.entity.w5h2.W5h2What;
import ahtewlg7.utimer.entity.w5h2.W5h2When;
import ahtewlg7.utimer.enumtype.GtdActionType;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;


public class GtdActionEntity extends AGtdUtimerEntity<GtdActionBuilder> implements Serializable {
    private GtdActionType actionType;

    protected GtdActionEntity(@Nonnull GtdActionBuilder builder) {
        super(builder);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
        toMakeUuidOk();
    }

    @Override
    public boolean ifValid() {
        return super.ifValid() && !toTips().isPresent()
                && !TextUtils.isEmpty(uuid) && !TextUtils.isEmpty(detail);
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

    //todo
    @Override
    public void update(IMergerEntity entity) {
        BaseW5h2Entity baseW5h2Entity = ((GtdActionEntity)entity).getW5h2Entity();
        super.update(entity);
        updateWhen(baseW5h2Entity.getWhen());
        updateWhat(baseW5h2Entity.getWhat());
        updateHowMuch(baseW5h2Entity.getHowMuch());
    }

    @Override
    public boolean ensureAttachFileExist() {
        if(attachFile == null){
            String fileName = !TextUtils.isEmpty(getTitle()) ? getTitle() : new DateTimeAction().toFormatNow().toString();
            String filePath = new FileSystemAction().getProjectGtdAbsPath();
            attachFile = new DirAttachFile(filePath, fileName);
        }
        return attachFile.createOrExist();
    }

    @Override
    public String toString() {
        if(actionType != null)
            return new StringBuilder(super.toString()).append(actionType.name()).toString();
         return super.toString();
    }

    public Optional<String> getWorkTimeInfo(){
        if(getW5h2Entity().getWhen() == null || getW5h2Entity().getWhen().getWorkTime() == null )
            return Optional.absent();
        StringBuilder builder   = new StringBuilder();
        List<DateTime> workTime = getW5h2Entity().getWhen().getWorkTime();
        for(DateTime dateTime : workTime)
            builder.append(new DateTimeAction().toFormat(dateTime)).append(",");
        return Optional.of(builder.toString());
    }

    private void  initByGbBean(ActionEntityGdBean gdBean){
        id              = gdBean.getId();
        uuid            = gdBean.getUuid();
        title           = gdBean.getTitle();
        detail          = gdBean.getDetail();

        if(w5h2Entity  == null)
            w5h2Entity  = new BaseW5h2Entity();
        updateWhen(gdBean.getW5h2When());
        updateWhat(gdBean.getW5h2What());
        updateHowMuch(gdBean.getW5h2HowMuch());
    }
    private void updateWhat(W5h2What what){
        if(what != null)
            w5h2Entity.setWhat(what);
    }
    private void updateWhen(W5h2When when){
        if(when != null){
            w5h2Entity.setWhen(when);
            createTime      = when.getCreateTime();
            lastAccessTime  = when.getLastAccessTime();
        }
    }
    private void updateHowMuch(W5h2HowMuch howMuch){
        if(howMuch != null)
            w5h2Entity.setHowMuch(howMuch);
    }
}

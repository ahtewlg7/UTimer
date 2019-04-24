package ahtewlg7.utimer.entity.gtd;

import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.common.FileSystemAction;
import ahtewlg7.utimer.comparator.ITimeComparator;
import ahtewlg7.utimer.db.entity.ActionEntityGdBean;
import ahtewlg7.utimer.entity.AGtdUtimerEntity;
import ahtewlg7.utimer.entity.IMergerEntity;
import ahtewlg7.utimer.entity.context.Contact;
import ahtewlg7.utimer.entity.material.DirAttachFile;
import ahtewlg7.utimer.entity.w5h2.BaseW5h2Entity;
import ahtewlg7.utimer.entity.w5h2.W5h2HowMuch;
import ahtewlg7.utimer.entity.w5h2.W5h2What;
import ahtewlg7.utimer.entity.w5h2.W5h2When;
import ahtewlg7.utimer.enumtype.ActState;
import ahtewlg7.utimer.enumtype.GtdType;
import ahtewlg7.utimer.util.DateTimeAction;


public class GtdActionEntity extends AGtdUtimerEntity<GtdActionBuilder>
        implements ITimeComparator, Serializable {
    private ActState actionState;

    protected GtdActionEntity(@Nonnull GtdActionBuilder builder) {
        super(builder);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
        toMakeEntityOk();
    }

    @Override
    protected DateTime getLifeCycleTime() {
        if(getFirstWorkTime().isPresent())
            return getFirstWorkTime().get();
        return super.getCreateTime();
    }

    @Override
    public boolean ifValid() {
        return super.ifValid() && actionState != null
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

    @Override
    protected void toMakeEntityOk() {
        super.toMakeEntityOk();
        if(actionState == null)
            actionState = ActState.MAYBE;
    }

    //++++++++++++++++++++++++++++++++++++++ITimeComparator++++++++++++++++++++++++++++++++++++
    @Override
    public Optional<DateTime> getComparatorTime() {
        return getFirstWorkTime();
    }

    public ActState getActionState() {
        return actionState;
    }

    public void setActionState(ActState actionState) {
        this.actionState = actionState;
    }

    //todo
    @Override
    public void update(IMergerEntity entity) {
        super.update(entity);
        BaseW5h2Entity baseW5h2Entity = ((GtdActionEntity)entity).getW5h2Entity();
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
        if(actionState != null)
            return new StringBuilder(super.toString()).append(actionState.name()).toString();
         return super.toString();
    }

    public Optional<String> getWorkTimeInfo(){
        if(getW5h2Entity().getWhen() == null || getW5h2Entity().getWhen().getWorkTime() == null )
            return Optional.absent();
        StringBuilder builder   = new StringBuilder();
        List<DateTime> workTime = getW5h2Entity().getWhen().getWorkTime();
        DateTimeAction dateTimeAction = new DateTimeAction();
        for(DateTime dateTime : workTime)
            builder.append(dateTimeAction.toFormat(dateTime)).append("\t");
        return Optional.of(builder.toString());
    }

    public Optional<String> getWhoInfo(){
        if(getW5h2Entity().getWho() == null || getW5h2Entity().getWho().getContactList() == null )
            return Optional.absent();
        StringBuilder builder   = new StringBuilder();
        List<Contact> contactList = getW5h2Entity().getWho().getContactList();
        for(Contact contact : contactList)
            builder.append(contact.getName()).append(",");
        return Optional.of(builder.toString());
    }

    private void  initByGbBean(ActionEntityGdBean gdBean){
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

package ahtewlg7.utimer.entity.gtd;

import android.text.TextUtils;

import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.List;

import javax.annotation.Nonnull;

import ahtewlg7.utimer.db.entity.DeedEntityGdBean;
import ahtewlg7.utimer.entity.BaseGtdEntity;
import ahtewlg7.utimer.enumtype.DateLife;
import ahtewlg7.utimer.enumtype.DeedState;


public class GtdDeedEntity extends BaseGtdEntity<GtdDeedBuilder> implements Serializable {
    private boolean isLink;
    private DeedState deedState;
    private DateTime startTime;
    private DateTime endTime;
    private List<DateTime> warningTimeList;

    protected GtdDeedEntity(@Nonnull GtdDeedBuilder builder) {
        super(builder);
        this.isLink = builder.isLink;
        if(builder.warningTimeList != null)
            setWarningTimeList(builder.warningTimeList);
        if(builder.gdBean != null)
            initByGbBean(builder.gdBean);
        if(builder.deedState != null)
            this.deedState = builder.deedState;
        toMakeEntityOk();
    }

    @Override
    public boolean ifValid() {
        return super.ifValid() && deedState != null && !TextUtils.isEmpty(detail);
    }

    @Override
    public Optional<String> getDetail() {
        return TextUtils.isEmpty(detail) ? Optional.<String>absent() : Optional.of(detail);
    }

    @Override
    protected void toMakeEntityOk() {
        super.toMakeEntityOk();
        if(deedState == null)
            deedState = DeedState.MAYBE;
    }

    public boolean isLink() {
        return isLink;
    }

    public void setLink(boolean link) {
        isLink = link;
    }

    public Optional<DateTime> getFirstWorkTime() {
        if(warningTimeList != null && !warningTimeList.isEmpty())
            return Optional.of(warningTimeList.get(0));
        return Optional.absent();
    }
    public List<DateTime> getWarningTimeList() {
        return warningTimeList;
    }
    public void setWarningTimeList(List<DateTime> warningTimeList) {
        this.warningTimeList = warningTimeList;
    }

    public DateLife getWorkDateLife(){
        Optional<DateTime> dateTimeOptional = getFirstWorkTime();
        if(dateTimeOptional.isPresent())
            return dateLifeCycleAction.getLife(dateTimeOptional.get());
        return null;
    }

    public String getWorkDateLifeDetail(){
        if(getWorkDateLife() == null)
            return null;
        return dateLifeCycleAction.getLifeDetail(getWorkDateLife());
    }

    public DeedState getDeedState() {
        return deedState;
    }
    public void setDeedState(DeedState deedState) {
        this.deedState = deedState;
    }

    public DateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(DateTime startTime) {
        this.startTime = startTime;
    }

    public DateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(DateTime endTime) {
        this.endTime = endTime;
    }

    public DateTime getWorkTime(){
        DateTime workTime = createTime;
        if(endTime != null)
           workTime = endTime;
        else if(startTime != null)
            workTime = startTime;
        return workTime;
    }

    @Override
    public String toString() {
        if(deedState != null)
            return new StringBuilder(super.toString()).append(deedState.name()).toString();
         return super.toString();
    }

    private void  initByGbBean(DeedEntityGdBean gdBean){
        uuid            = gdBean.getUuid();
        title           = gdBean.getTitle();
        detail          = gdBean.getDetail();
        isLink          = gdBean.isLink();
        deedState       = gdBean.getActionState();
        createTime      = gdBean.getCreateTime();
        startTime       = gdBean.getStartTime();
        endTime         = gdBean.getEndTime();
        warningTimeList = gdBean.getWarningTimeList();

        /*if(w5h2Entity  == null)
            w5h2Entity  = new BaseW5h2Entity();
        updateWhen(gdBean.getW5h2When());
        updateWhat(gdBean.getW5h2What());
        updateHowMuch(gdBean.getW5h2HowMuch());*/
    }
    /*private void updateWhat(W5h2What what){
        if(what != null)
            w5h2Entity.setWhat(what);
    }
    private void updateWhen(W5h2When when){
        if(when != null){
            w5h2Entity.setWhen(when);
            createTime      = when.getCreateTime().getDateTime();
            lastAccessTime  = when.getLastAccessTime().getDateTime();
        }
    }
    private void updateHowMuch(W5h2HowMuch howMuch){
        if(howMuch != null)
            w5h2Entity.setHowMuch(howMuch);
    }
    private void updateWho(W5h2Who who){
        if(who != null)
            w5h2Entity.setWho(who);
    }
    private void updateWhere(W5h2Where where){
        if(where != null)
            w5h2Entity.setWhere(where);
    }*/
}

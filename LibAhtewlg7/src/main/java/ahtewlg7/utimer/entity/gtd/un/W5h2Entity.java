package ahtewlg7.utimer.entity.gtd.un;


import android.text.TextUtils;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import java.util.List;

import ahtewlg7.utimer.entity.ITipsEntity;
import ahtewlg7.utimer.entity.degree.DegreeEntity;
import ahtewlg7.utimer.entity.taskContext.AddrContext;
import ahtewlg7.utimer.entity.taskContext.ContactContext;
import ahtewlg7.utimer.entity.taskContext.IHowMuchContext;
import ahtewlg7.utimer.entity.taskContext.IPurposeContext;
import ahtewlg7.utimer.entity.taskContext.ITimeContext;

public class W5h2Entity implements ITipsEntity {
    public static final String TAG = W5h2Entity.class.getSimpleName();

    private W5h2What what;
    private W5h2Why why;
    private W5h2Who who;
    private W5h2When when;
    private W5h2Where where;
    private W5h2How how;
    private W5h2HowMuch howMuch;

    public W5h2What getWhat() {
        return what;
    }

    public void setWhat(W5h2What what) {
        this.what = what;
    }

    public W5h2Why getWhy() {
        return why;
    }

    public void setWhy(W5h2Why why) {
        this.why = why;
    }

    public W5h2Who getWho() {
        return who;
    }

    public void setWho(W5h2Who who) {
        this.who = who;
    }

    public W5h2When getWhen() {
        return when;
    }

    public void setWhen(W5h2When when) {
        this.when = when;
    }

    public W5h2Where getWhere() {
        return where;
    }

    public void setWhere(W5h2Where where) {
        this.where = where;
    }

    public W5h2How getHow() {
        return how;
    }

    public void setHow(W5h2How how) {
        this.how = how;
    }

    public W5h2HowMuch getHowMuch() {
        return howMuch;
    }

    public void setHowMuch(W5h2HowMuch howMuch) {
        this.howMuch = howMuch;
    }

    @Override
    public Optional<String> toTips() {
        return null;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        if(what != null)
            builder.append(what.toString());
        if(why != null)
            builder.append("," + why.toString());
        if(who != null)
            builder.append("," + who.toString());
        if(when != null)
            builder.append("," + when.toString());
        if(where != null)
            builder.append("," + where.toString());
        if(how != null)
            builder.append("," + how.toString());
        if(howMuch != null)
            builder.append("," + howMuch.toString());
        return builder.toString();
    }

    /********************************************What***********************************************/
    public static class W5h2What implements ITipsEntity{
        public final String TAG = W5h2What.class.getSimpleName();

        private String target;
        private String need;
        private String warning;
        private String against;
        private W5h2What mirror;

        public W5h2What() {
        }

        public W5h2What(W5h2What mirror) {
            this.mirror = mirror;
        }

        public String getTarget() {
            return (!TextUtils.isEmpty(target) || mirror == null)? target : mirror.getTarget();
        }

        public void setTarget(String target) {
            this.target = target;
        }

        public String getNeed() {
            return (!TextUtils.isEmpty(need) || mirror == null)? need : mirror.getNeed();
        }

        public void setNeed(String need) {
            this.need = need;
        }

        public String getWarning() {
            return (!TextUtils.isEmpty(warning) || mirror == null)? warning : mirror.getWarning();
        }

        public void setWarning(String warning) {
            this.warning = warning;
        }

        public String getAgainst() {
            return (!TextUtils.isEmpty(against) || mirror == null)? against : mirror.getAgainst();
        }

        public void setAgainst(String against) {
            this.against = against;
        }

        @Override
        public Optional<String> toTips() {
            return null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(TAG).append("{");
            if(!TextUtils.isEmpty(target))
                builder.append("target = "+ target);
            if(!TextUtils.isEmpty(need))
                builder.append(",ready = "+ need);
            if(!TextUtils.isEmpty(warning))
                builder.append(",warning = "+ warning);
            if(!TextUtils.isEmpty(against))
                builder.append(",against = "+ against);
            return builder.append("}").toString();
        }
    }

    /********************************************Why************************************************/
    public static class W5h2Why implements ITipsEntity{
        public  final String TAG = W5h2Why.class.getSimpleName();

        private IPurposeContext purpose;
        private boolean ifNecessary;

        private W5h2Why mirror;

        public W5h2Why() {
        }
        public W5h2Why(W5h2Why mirror) {
            this.mirror = mirror;
        }

        public IPurposeContext getPurpose() {
            return ((purpose != null && purpose.ifValid()) || mirror == null)? purpose : mirror.getPurpose();
        }

        public void setPurpose(IPurposeContext purpose) {
            this.purpose = purpose;
        }

        public boolean isIfNecessary() {
            return ifNecessary;
        }

        public void setIfNecessary(boolean ifNecessary) {
            this.ifNecessary = ifNecessary;
        }

        @Override
        public Optional<String> toTips() {
            return null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(TAG).append("{");
            builder.append("ifNecessary = "+ ifNecessary);
            if(purpose != null)
                builder.append(",purpose = "+ purpose.toString());
            return builder.append("}").toString();
        }
    }

    /********************************************Who************************************************/
    public static class W5h2Who implements ITipsEntity{
        public  final String TAG = W5h2Who.class.getSimpleName();

        public static final int RANK_NUM = 6;

        private ContactContext owner;

        private List<ContactContext> rank1List = Lists.newArrayList();//this is the delegate person
        private List<ContactContext> rank2List = Lists.newArrayList();
        private List<ContactContext> rank3List = Lists.newArrayList();
        private List<ContactContext> rank4List = Lists.newArrayList();
        private List<ContactContext> rank5List = Lists.newArrayList();
        private List<ContactContext> rank6List = Lists.newArrayList();

        private W5h2Who mirror;

        public W5h2Who() {
        }

        public W5h2Who(W5h2Who mirror) {
            this.mirror  = mirror;
        }

        public ContactContext getOwner() {
            return ((owner != null && owner.ifValid()) || mirror == null)? owner : mirror.getOwner();
        }

        public void setOwner(ContactContext owner) {
            this.owner = owner;
        }

        public int getRankNum(){
            return RANK_NUM;
        }

        public boolean isDelegated(){
            return rank1List.isEmpty();
        }

        public List<ContactContext> getRank1List() {
            return rank1List;
        }

        public List<ContactContext> getRank2List() {
            return rank2List;
        }

        public List<ContactContext> getRank3List() {
            return rank3List;
        }

        public List<ContactContext> getRank4List() {
            return rank4List;
        }

        public List<ContactContext> getRank5List() {
            return rank5List;
        }

        public List<ContactContext> getRank6List() {
            return rank6List;
        }

        @Override
        public Optional<String> toTips() {
            return null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(TAG).append("{");
            if(owner != null)
                builder.append("owner = " + owner.toString());
            for(ContactContext contactContext : rank1List)
                builder.append("，rank1 = " + contactContext.toString());
            for(ContactContext contactContext : rank2List)
                builder.append("，rank2 = " + contactContext.toString());
            for(ContactContext contactContext : rank3List)
                builder.append("，rank3 = " + contactContext.toString());
            for(ContactContext contactContext : rank4List)
                builder.append("，rank4 = " + contactContext.toString());
            for(ContactContext contactContext : rank5List)
                builder.append("，rank5 = " + contactContext.toString());
            for(ContactContext contactContext : rank6List)
                builder.append("，rank6 = " + contactContext.toString());
            return builder.append("}").toString();
        }
    }

    /********************************************When***********************************************/
    public static class W5h2When implements ITipsEntity{
        public  final String TAG = W5h2When.class.getSimpleName();

        private ITimeContext startTime;
        private ITimeContext endTime;
        private ITimeContext lastEyeOnTime;
        private ITimeContext warningTime;
        private ITimeContext costTime;
        private W5h2When mirror;

        public W5h2When() {
        }

        public W5h2When(W5h2When mirror) {
            this.mirror = mirror;
        }

        public ITimeContext getStartTime() {
            return (startTime != null || mirror == null)? startTime : mirror.getStartTime();
        }

        public void setStartTime(ITimeContext startTime) {
            this.startTime = startTime;
        }

        public ITimeContext getEndTime() {
            return (endTime != null || mirror == null)? endTime : mirror.getEndTime();
        }

        public void setEndTime(ITimeContext endTime) {
            this.endTime = endTime;
        }

        public ITimeContext getLastEyeOnTime() {
            return lastEyeOnTime;
        }

        public void setLastEyeOnTime(ITimeContext lastEyeOnTime) {
            this.lastEyeOnTime = lastEyeOnTime;
        }

        public ITimeContext getWarningTime() {
            return (warningTime != null || mirror == null)? warningTime : mirror.getWarningTime();
        }

        public void setWarningTime(ITimeContext warningTime) {
            this.warningTime = warningTime;
        }

        public ITimeContext getCostTime() {
            return (costTime != null || mirror == null)? costTime : mirror.getCostTime();
        }

        public void setCostTime(ITimeContext costTime) {
            this.costTime = costTime;
        }

        @Override
        public Optional<String> toTips() {
            return null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(TAG).append("{");
            if(startTime != null)
                builder.append("startTime = " + startTime.toString());
            if(endTime != null)
                builder.append(",endTime = " + endTime.toString());
            if(lastEyeOnTime != null)
                builder.append(",lastEyeOnTime = " + lastEyeOnTime.toString());
            if(warningTime != null)
                builder.append(",warningTime = " + warningTime.toString());
            if(costTime != null)
                builder.append(",costTime = " + costTime.toString());
            return builder.append("}").toString();
        }
    }

    /********************************************Where**********************************************/
    public static class W5h2Where implements ITipsEntity{
        public final String TAG = W5h2Where.class.getSimpleName();

        private AddrContext workAddr;
        private AddrContext readyAddr;
        private AddrContext doneAddr;

        private W5h2Where mirror;

        public W5h2Where() {
        }
        public W5h2Where(W5h2Where mirror) {
            this.mirror = mirror;
        }

        public AddrContext getWorkAddr() {
            return ((workAddr != null && workAddr.ifValid()) || mirror == null)? workAddr : mirror.getWorkAddr();
        }

        public void setWorkAddr(AddrContext workAddr) {
            this.workAddr = workAddr;
        }

        public AddrContext getReadyAddr() {
            return ((readyAddr != null && readyAddr.ifValid()) || mirror == null)? readyAddr : mirror.getReadyAddr();
        }

        public void setReadyAddr(AddrContext readyAddr) {
            this.readyAddr = readyAddr;
        }

        public AddrContext getDoneAddr() {
            return ((doneAddr != null && doneAddr.ifValid()) || mirror == null)? doneAddr : mirror.getDoneAddr();
        }

        public void setDoneAddr(AddrContext doneAddr) {
            this.doneAddr = doneAddr;
        }

        @Override
        public Optional<String> toTips() {
            return null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(TAG).append("{");
            if(workAddr != null)
                builder.append("workAddr = " + workAddr.toString());
            if(readyAddr != null)
                builder.append(",readyAddr = " + readyAddr.toString());
            if(doneAddr != null)
                builder.append(",doneAddr = " + doneAddr.toString());
            return builder.append("}").toString();
        }
    }

    /********************************************How************************************************/
    public static class W5h2How implements ITipsEntity{
        public final String TAG = W5h2How.class.getSimpleName();

        private String planMethod;
        private W5h2How mirror;

        public W5h2How() {
        }

        public W5h2How(W5h2How mirror) {
            this.mirror = mirror;
        }

        public String getPlanMethod() {
            return (!TextUtils.isEmpty(planMethod) || mirror == null)? planMethod : mirror.getPlanMethod();
        }

        public void setPlanMethod(String planMethod) {
            this.planMethod = planMethod;
        }

        @Override
        public Optional<String> toTips() {
            return null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(TAG).append("{");
            if(!TextUtils.isEmpty(planMethod))
                builder.append("planMethod = " + planMethod);
            return builder.append("}").toString();
        }
    }

    /********************************************HowMuch********************************************/
    public static class W5h2HowMuch implements ITipsEntity {
        public final String TAG = W5h2HowMuch.class.getSimpleName();

        private int planPercent;
        private boolean isDone;
        private IHowMuchContext planCostNum;
        private IHowMuchContext doneCostNum;
        private DegreeEntity importLevel;

        private W5h2HowMuch mirror;

        public W5h2HowMuch() {
            importLevel = new DegreeEntity();
        }
        public W5h2HowMuch(W5h2HowMuch mirror) {
            this.mirror = mirror;
        }


        public int getPlanPercent() {
            int tmp = planPercent;
            if(planPercent < 0 )
                tmp = 0;
            else if(planPercent > 100 )
                tmp = 100;
            return (tmp != 0 || mirror == null)? tmp: mirror.getPlanPercent();
        }

        public void setPlanPercent(int planPercent) {
            this.planPercent = planPercent;
        }

        public boolean isDone() {
            return isDone;
        }

        public void setDone(boolean done) {
            isDone = done;
        }

        public IHowMuchContext getPlanCostNum() {
            return (planCostNum.ifValid() || mirror == null)? planCostNum : mirror.getPlanCostNum();
        }

        public void setPlanCostNum(IHowMuchContext planCostNum) {
            this.planCostNum = planCostNum;
        }

        public IHowMuchContext getDoneCostNum() {
            return (doneCostNum.ifValid() || mirror == null)? doneCostNum : mirror.getDoneCostNum();
        }

        public void setDoneCostNum(IHowMuchContext doneCostNum) {
            this.doneCostNum = doneCostNum;
        }

        public DegreeEntity getImportLevel() {
            return importLevel;
        }

        public void setImportLevel(DegreeEntity importLevel) {
            this.importLevel = importLevel;
        }

        @Override
        public Optional<String> toTips() {
            return null;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(TAG).append("{");
            builder.append("planPercent = " + planPercent);
            if(planCostNum != null)
                builder.append(", planCostNum = " + planCostNum.toString());
            if(doneCostNum != null)
                builder.append(", doneCostNum = " + doneCostNum.toString());
            if(importLevel != null)
                builder.append(", importLevel = " + importLevel.toString());
            return builder.append("}").toString();
        }
    }
}

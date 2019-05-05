package ahtewlg7.utimer.entity.w5h2;

import com.google.common.base.Optional;
import com.google.common.collect.Lists;

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.entity.ITipsEntity;
import ahtewlg7.utimer.entity.IValidEntity;
import ahtewlg7.utimer.entity.context.TimeContext;
import ahtewlg7.utimer.util.DateTimeAction;

/**
 * Created by lw on 2019/3/20.
 */
public class W5h2When implements ITipsEntity, IValidEntity {
    private TimeContext createTime;
    private TimeContext lastAccessTime;
    private TimeContext nextWarningTime;
    private List<TimeContext> workTimeList;

    public W5h2When(){
        workTimeList = Lists.newArrayList();
    }

    @Override
    public boolean ifValid() {
        return workTimeList != null && !workTimeList.isEmpty();
    }

    public TimeContext getCreateTime() {
        return createTime;
    }

    public void setCreateTime(TimeContext createTime) {
        this.createTime = createTime;
    }

    public TimeContext getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(TimeContext lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public TimeContext getNextWarningTime() {
        return nextWarningTime;
    }

    public void setNextWarningTime(TimeContext nextWarningTime) {
        this.nextWarningTime = nextWarningTime;
    }

    public Optional<DateTime> getFirstWorkTime() {
        if(workTimeList != null && !workTimeList.isEmpty() && workTimeList.get(0).ifValid())
            return Optional.of(workTimeList.get(0).getDateTime());
        return Optional.absent();
    }

    public void addWorkTime(TimeContext timeContext){
        workTimeList.add(timeContext);
    }
    public void removeWorkTime(TimeContext timeContext){
        workTimeList.remove(timeContext);
    }
    public void clearWorkTime(){
        workTimeList.clear();
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if(createTime != null)
            builder.append(",").append(new DateTimeAction().toFormat(createTime.getDateTime()));
        if(lastAccessTime != null)
            builder.append(",").append(new DateTimeAction().toFormat(lastAccessTime.getDateTime()));
        if(nextWarningTime != null)
            builder.append(",").append(new DateTimeAction().toFormat(nextWarningTime.getDateTime()));
        if(workTimeList != null){
            for(TimeContext dateTime : workTimeList)
                builder.append(",").append(new DateTimeAction().toFormat(dateTime.getDateTime()));
        }
        return builder.append("}").toString();
    }

    @Override
    public Optional<String> toTips() {
        StringBuilder builder   = new StringBuilder("When：");
        for(TimeContext timeContext : workTimeList)
            builder.append(timeContext.getName()).append(",");
        return Optional.of(builder.toString());
    }
}

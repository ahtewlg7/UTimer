package ahtewlg7.utimer.entity.w5h2;

import com.alibaba.fastjson.annotation.JSONField;
import com.google.common.base.Optional;

import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.entity.IValidEntity;
import ahtewlg7.utimer.json.DateTimeFastjson;
import ahtewlg7.utimer.json.DateTimeListFastjson;
import ahtewlg7.utimer.util.DateTimeAction;

/**
 * Created by lw on 2019/3/20.
 */
public class W5h2When implements IValidEntity {
    @JSONField(serializeUsing = DateTimeFastjson.class, deserializeUsing = DateTimeFastjson.class)
    private DateTime createTime;
    @JSONField(serializeUsing = DateTimeFastjson.class, deserializeUsing = DateTimeFastjson.class)
    private DateTime lastAccessTime;
    @JSONField(serializeUsing = DateTimeFastjson.class, deserializeUsing = DateTimeFastjson.class)
    private DateTime nextWarningTime;
    @JSONField(serializeUsing = DateTimeListFastjson.class, deserializeUsing = DateTimeListFastjson.class)
    private List<DateTime> workTime;

    @Override
    public boolean ifValid() {
        return workTime != null && !workTime.isEmpty();
    }

    public DateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }

    public DateTime getLastAccessTime() {
        return lastAccessTime;
    }

    public void setLastAccessTime(DateTime lastAccessTime) {
        this.lastAccessTime = lastAccessTime;
    }

    public DateTime getNextWarningTime() {
        return nextWarningTime;
    }

    public void setNextWarningTime(DateTime nextWarningTime) {
        this.nextWarningTime = nextWarningTime;
    }

    public Optional<DateTime> getFirstWorkTime() {
        if(workTime != null && !workTime.isEmpty())
            return Optional.of(workTime.get(0));
        return Optional.absent();
    }
    public List<DateTime> getWorkTime() {
        return workTime;
    }

    public void setWorkTime(List<DateTime> workTime) {
        this.workTime = workTime;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("{");
        if(createTime != null)
            builder.append(",").append(new DateTimeAction().toFormat(createTime));
        if(lastAccessTime != null)
            builder.append(",").append(new DateTimeAction().toFormat(lastAccessTime));
        if(nextWarningTime != null)
            builder.append(",").append(new DateTimeAction().toFormat(nextWarningTime));
        if(workTime != null){
            for(DateTime dateTime : workTime)
                builder.append(",").append(new DateTimeAction().toFormat(dateTime));
        }
        return builder.append("}").toString();
    }
}

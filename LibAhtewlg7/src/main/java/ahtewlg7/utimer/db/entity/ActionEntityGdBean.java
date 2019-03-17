package ahtewlg7.utimer.db.entity;

import android.text.TextUtils;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Index;
import org.joda.time.DateTime;

import java.util.List;

import ahtewlg7.utimer.db.converter.DateTimeListTypeConverter;
import ahtewlg7.utimer.db.converter.DateTimeTypeConverter;
import ahtewlg7.utimer.db.converter.GtdActionTypeConverter;
import ahtewlg7.utimer.enumtype.GtdActionType;

/**
 * Created by lw on 2018/1/6.
 */

@Entity(
    nameInDb = "ACTION"
)
public class ActionEntityGdBean {
    // KEEP FIELDS - put your custom fields here
    public static final String TAG = ActionEntityGdBean.class.getSimpleName();
    //KEEP FIELDS END

    // KEEP METHODS - put your custom methods here
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder(TAG);
        builder.append("{");
        if(!TextUtils.isEmpty(uuid))
            builder.append(",uuid=").append(uuid);
        if(!TextUtils.isEmpty(title))
            builder.append(",title=").append(title);
        if(!TextUtils.isEmpty(detail))
            builder.append(",detail=").append(detail);
        builder.append(",warningCount=").append(warningCount);
        if(actionType != null)
            builder.append(",actionType=").append(actionType.name());
        return builder.append("}").toString();
    }
    // KEEP METHODS END
    public int getWarningCount() {
        return this.warningCount;
    }
    public void setWarningCount(int warningCount) {
        this.warningCount = warningCount;
    }
    public String getUuid() {
        return this.uuid;
    }
    public void setUuid(String uuid) {
        this.uuid = uuid;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDetail() {
        return this.detail;
    }
    public void setDetail(String detail) {
        this.detail = detail;
    }
    public DateTime getCreateTime() {
        return this.createTime;
    }
    public void setCreateTime(DateTime createTime) {
        this.createTime = createTime;
    }
    public GtdActionType getActionType() {
        return this.actionType;
    }
    public void setActionType(GtdActionType actionType) {
        this.actionType = actionType;
    }
    public List<DateTime> getTimeList() {
        return this.timeList;
    }
    public void setTimeList(List<DateTime> timeList) {
        this.timeList = timeList;
    }

    private int warningCount;
    @Index(unique = true)
    private String uuid;
    private String title;
    private String detail;
    @Convert(converter = DateTimeTypeConverter.class, columnType = Long.class)
    private DateTime createTime;
    @Convert(converter = GtdActionTypeConverter.class, columnType = Integer.class)
    private GtdActionType actionType;
    @Convert(converter = DateTimeListTypeConverter.class, columnType = String.class)
    private List<DateTime> timeList;

    @Generated(hash = 1198511598)
    public ActionEntityGdBean(int warningCount, String uuid, String title,
            String detail, DateTime createTime, GtdActionType actionType,
            List<DateTime> timeList) {
        this.warningCount = warningCount;
        this.uuid = uuid;
        this.title = title;
        this.detail = detail;
        this.createTime = createTime;
        this.actionType = actionType;
        this.timeList = timeList;
    }
    @Generated(hash = 1154478005)
    public ActionEntityGdBean() {
    }

}

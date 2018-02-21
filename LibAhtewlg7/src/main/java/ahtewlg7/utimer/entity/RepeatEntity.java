package ahtewlg7.utimer.entity;

import org.joda.time.DateTime;

import ahtewlg7.utimer.enumtype.TaskRepeatPeriodType;

/**
 * Created by lw on 2017/9/27.
 */

public class RepeatEntity {
    public static final String TAG = RepeatEntity.class.getSimpleName();

    private int repeatCell;
    private TaskRepeatPeriodType repeatPeriodType;

    private DateTime dateTimeFrom;
    private DateTime dateTimeEnd;

    public int getRepeatCell() {
        return repeatCell;
    }

    public void setRepeatCell(int repeatAmount) {
        this.repeatCell = repeatCell;
    }

    public TaskRepeatPeriodType getRepeatPeriodType() {
        return repeatPeriodType;
    }

    public void setRepeatPeriodType(TaskRepeatPeriodType repeatPeriodType) {
        this.repeatPeriodType = repeatPeriodType;
    }

    public DateTime getDateTimeFrom() {
        return dateTimeFrom;
    }

    public void setDateTimeFrom(DateTime dateTimeFrom) {
        this.dateTimeFrom = dateTimeFrom;
    }

    public DateTime getDateTimeEnd() {
        return dateTimeEnd;
    }

    public void setDateTimeEnd(DateTime dateTimeEnd) {
        this.dateTimeEnd = dateTimeEnd;
    }

    @Override
    public String toString() {
        return "repeatCell = " + repeatCell + ", repeatPeriodType = " + repeatPeriodType.name()
                + ", dateTimeFrom = " + dateTimeFrom.toString() + ", dateTimeEnd = " + dateTimeEnd.toString();
    }
}

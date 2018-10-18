package ahtewlg7.utimer.entity.taskContext;

import org.joda.time.DateTime;

import ahtewlg7.utimer.entity.IValidEntity;

public interface ITimeContext extends IValidEntity {
    public DateTime toDateTime();
}

package ahtewlg7.utimer.db.converter;


import org.greenrobot.greendao.converter.PropertyConverter;
import org.joda.time.DateTime;

import ahtewlg7.utimer.util.DateTimeAction;

/**
 * Created by lw on 2016/9/6.
 */
public class DateTimeTypeConverter implements PropertyConverter<DateTime,String> {
    public static final String TAG = DateTimeTypeConverter.class.getSimpleName();

    private DateTimeAction dateTimeAction;

    public DateTimeTypeConverter(){
        dateTimeAction = new DateTimeAction();
    }

    @Override
    public DateTime convertToEntityProperty(String databaseValue) {
        return dateTimeAction.toDateTime(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(DateTime entityProperty) {
        return dateTimeAction.toFormat(entityProperty);
    }
}

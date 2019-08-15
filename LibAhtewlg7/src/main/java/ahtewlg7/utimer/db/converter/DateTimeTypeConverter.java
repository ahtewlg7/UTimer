package ahtewlg7.utimer.db.converter;


import org.greenrobot.greendao.converter.PropertyConverter;
import org.joda.time.DateTime;

/**
 * Created by lw on 2016/9/6.
 */
public class DateTimeTypeConverter implements PropertyConverter<DateTime,Long> {
    @Override
    public DateTime convertToEntityProperty(Long databaseValue) {
        return new DateTime(databaseValue);
    }

    @Override
    public Long convertToDatabaseValue(DateTime entityProperty) {
        return entityProperty.getMillis();
    }
}

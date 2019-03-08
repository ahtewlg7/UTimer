package ahtewlg7.utimer.db.converter;


import com.google.common.base.Splitter;
import com.google.common.collect.Lists;

import org.greenrobot.greendao.converter.PropertyConverter;
import org.joda.time.DateTime;

import java.util.List;

/**
 * Created by lw on 2016/9/6.
 */
public class DateTimeListTypeConverter implements PropertyConverter<List<DateTime>,String> {
    public static final String TAG = DateTimeListTypeConverter.class.getSimpleName();

    public static final String DATE_TIME_SPLIT          = ";";

    @Override
    public List<DateTime> convertToEntityProperty(String databaseValue) {
        List<DateTime> list = Lists.newArrayList();
        List<String> timeList = Splitter.on(DATE_TIME_SPLIT).trimResults().omitEmptyStrings().splitToList(databaseValue);
        for(String time : timeList){
            try{
                Long t = Long.parseLong(time);
                list.add(new DateTime(t));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public String convertToDatabaseValue(List<DateTime> entityProperty) {
        StringBuilder timeList = new StringBuilder();
        for(DateTime dateTime : entityProperty)
            timeList.append(dateTime.getMillis()).append(DATE_TIME_SPLIT);
        return timeList.toString();
    }


}

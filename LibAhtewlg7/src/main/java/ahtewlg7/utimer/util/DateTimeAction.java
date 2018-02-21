package ahtewlg7.utimer.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ahtewlg7.utimer.verctrl.VcFactoryBuilder;

/**
 * Created by lw on 2016/9/5.
 */
public class DateTimeAction {
    public static final String TAG = DateTimeAction.class.getSimpleName();

    private String defaultFormat;

    public DateTimeAction() {
        defaultFormat = VcFactoryBuilder.getInstance().getVersionControlFactory().getBaseConfig().getDateTimeFormat();
    }

    public DateTime toNow(){
        return DateTime.now();
    }

    public String getDefaultFormat(){
        return defaultFormat;
    }

    public String toFormatNow(){
        return toFormatNow(defaultFormat);
    }

    public String toFormatNow(String format){
        return toFormat(DateTime.now(),format);
    }

    public String toFormat(DateTime dateTime){
        return toFormat(dateTime, defaultFormat);
    }

    public String toFormat(DateTime dateTime, String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        return dateTimeFormatter.print(dateTime);
    }

    public DateTime toDateTime(String dateTime) {
        DateTimeFormatter format = DateTimeFormat.forPattern(defaultFormat);
        return DateTime.parse(dateTime, format);
    }
    public DateTime toDateTime(String dateTime, String timeFormat) {
        DateTimeFormatter format = DateTimeFormat.forPattern(timeFormat);
        return DateTime.parse(dateTime, format);
    }
    public DateTime toDateTime(String dateTime, DateTimeFormatter format){
        return DateTime.parse(dateTime, format);
    }
}

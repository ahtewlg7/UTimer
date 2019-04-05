package ahtewlg7.utimer.util;

import com.google.common.base.Optional;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ahtewlg7.utimer.verctrl.BaseConfig;
import ahtewlg7.utimer.verctrl.VcFactoryBuilder;

/**
 * Created by lw on 2016/9/5.
 */
public class DateTimeAction {
    public static final String TAG = DateTimeAction.class.getSimpleName();

    private String defaultFormat;

    public DateTimeAction() {
        Optional<? extends BaseConfig> configOptional = VcFactoryBuilder.getInstance().getVcConfig();
        defaultFormat = new BaseConfig().getSimpleDateTimeFormat();
        if(configOptional.isPresent())
            defaultFormat = configOptional.get().getSimpleDateTimeFormat();
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

    public boolean isInToday(DateTime dateTime){
        return dateTime != null && dateTime.isAfter(DateTime.now().withTimeAtStartOfDay());
    }
    public boolean isInTomorrow(DateTime dateTime){
        return dateTime != null && dateTime.isAfter(DateTime.now().plusDays(1).withTimeAtStartOfDay());
    }
    public boolean isInWeek(DateTime dateTime){
        return dateTime != null && dateTime.isBefore(DateTime.now().dayOfWeek().withMaximumValue());
    }
    public boolean isInMonth(DateTime dateTime){
        return dateTime != null && dateTime.isBefore(DateTime.now().dayOfMonth().withMaximumValue());
    }
    public boolean isInQuarter(DateTime dateTime){
        return dateTime != null && dateTime.isBefore(DateTime.now().plusMonths(3).dayOfMonth().withMaximumValue());
    }
    public boolean isInYear(DateTime dateTime){
        return dateTime != null && dateTime.isBefore(DateTime.now().dayOfYear().withMaximumValue());
    }
}

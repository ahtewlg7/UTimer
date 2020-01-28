package ahtewlg7.utimer.util;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ahtewlg7.utimer.verctrl.BaseConfig;

/**
 * Created by lw on 2016/9/5.
 */
public class DateTimeAction {
    public static final String TAG = DateTimeAction.class.getSimpleName();

    private String defaultDateTimeFormat;
   private String defaultDateFormat;

    public DateTimeAction() {
        defaultDateTimeFormat = new BaseConfig().getSimpleDateTimeFormat();
        defaultDateFormat     = new BaseConfig().getSimpleDateFormat();
    }

    public DateTime toNow(){
        return DateTime.now();
    }

    public boolean isBefore(@NonNull DateTime dateTime1, @NonNull DateTime dateTime2){
        return dateTime1.isBefore(dateTime2.getMillis());
    }
    public boolean isAfter(@NonNull DateTime dateTime1, @NonNull DateTime dateTime2){
        return dateTime1.isAfter(dateTime2.getMillis());
    }
    public boolean isEqual(@NonNull DateTime dateTime1, @NonNull DateTime dateTime2){
        return dateTime1.isEqual(dateTime2.getMillis());
    }

    public String getDefaultDateTimeFormat(){
        return defaultDateTimeFormat;
    }
    public String getDefaultDateFormat(){
        return defaultDateFormat;
    }

    public String toFormatNow(){
        return toFormatNow(defaultDateTimeFormat);
    }

    public String toFormatNow(String format){
        return toFormat(DateTime.now(),format);
    }

    public String toFormat(DateTime dateTime){
        return toFormat(dateTime, defaultDateTimeFormat);
    }

    public String toFormat(LocalDate localDate){
        return toFormat(localDate, defaultDateFormat);
    }

    public String toFormat(LocalDate localDate, String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        return dateTimeFormatter.print(localDate);
    }
    public String toFormat(DateTime dateTime, String format){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(format);
        return dateTimeFormatter.print(dateTime);
    }

    public DateTime toDateTime(String dateTime) {
        DateTimeFormatter format = DateTimeFormat.forPattern(defaultDateTimeFormat);
        return DateTime.parse(dateTime, format);
    }
    public DateTime toDateTime(String dateTime, String timeFormat) {
        DateTimeFormatter format = DateTimeFormat.forPattern(timeFormat);
        return DateTime.parse(dateTime, format);
    }
    public DateTime toDateTime(String dateTime, DateTimeFormatter format){
        return DateTime.parse(dateTime, format);
    }

    public boolean isInPast(DateTime dateTime){
        return dateTime != null && dateTime.isBefore(DateTime.now().withTimeAtStartOfDay());
    }
    public boolean isIn24Hour(DateTime dateTime){
        return dateTime != null && DateTime.now().isBefore(dateTime.plusHours(24));
    }
    public boolean isInToday(DateTime dateTime){
        return dateTime != null && dateTime.isBefore(DateTime.now().plusDays(1).withTimeAtStartOfDay());
    }
    public boolean isInTomorrow(DateTime dateTime){
        return dateTime != null && !isInToday(dateTime) && dateTime.isBefore(DateTime.now().plusDays(2).withTimeAtStartOfDay());
    }
    public boolean isInWeek(DateTime dateTime){
        return dateTime != null && !isInTomorrow(dateTime) && dateTime.isBefore(DateTime.now().plusWeeks(1).dayOfWeek().withMinimumValue().withTimeAtStartOfDay());
    }
    public boolean isInNextWeek(DateTime dateTime){
        return dateTime != null && !isInWeek(dateTime) && dateTime.isBefore(DateTime.now().plusWeeks(2).dayOfWeek().withMinimumValue().withTimeAtStartOfDay());
    }
    public boolean isInMonth(DateTime dateTime){
        return dateTime != null && !isInNextWeek(dateTime) && dateTime.isBefore(DateTime.now().plusMonths(1).dayOfMonth().withMinimumValue().withTimeAtStartOfDay());
    }
    public boolean isInNextMonth(DateTime dateTime){
        return dateTime != null && !isInMonth(dateTime) && dateTime.isBefore(DateTime.now().plusMonths(2).dayOfMonth().withMinimumValue().withTimeAtStartOfDay());
    }
    public boolean isInQuarter(DateTime dateTime){
        return dateTime != null && !isInNextMonth(dateTime) && dateTime.isBefore(DateTime.now().plusMonths(3).dayOfMonth().withMinimumValue().withTimeAtStartOfDay());
    }
    public boolean isInHalfYear(DateTime dateTime){
        return dateTime != null && !isInQuarter(dateTime) && dateTime.isBefore(DateTime.now().plusMonths(6).dayOfMonth().withMinimumValue().withTimeAtStartOfDay());
    }
    public boolean isInYear(DateTime dateTime){
        return dateTime != null && !isInQuarter(dateTime)  && dateTime.isBefore(DateTime.now().plusYears(1).dayOfYear().withMinimumValue().withTimeAtStartOfDay());
    }
}

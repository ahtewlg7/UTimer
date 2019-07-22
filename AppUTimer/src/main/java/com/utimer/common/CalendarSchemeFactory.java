package com.utimer.common;

import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.haibin.calendarview.Calendar;
import com.utimer.entity.CalendarSchemeInfo;

import org.joda.time.LocalDate;

/**
 * Created by lw on 2019/7/20.
 */
public class CalendarSchemeFactory {
    public Optional<CalendarSchemeInfo> toObject(String json) {
        Optional<CalendarSchemeInfo> calendar = Optional.absent();
        try{
            if(!TextUtils.isEmpty(json))
                calendar = Optional.fromNullable(JSON.parseObject(json, CalendarSchemeInfo.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return calendar;
    }
    public Optional<String> toJsonStr(CalendarSchemeInfo obj) {
        Optional<String> json = Optional.absent();
        try{
            if(obj != null)
                json = Optional.fromNullable(JSON.toJSONString(obj));
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    public LocalDate getLocalDate(Calendar calendar){
        return new LocalDate(calendar.getYear(), calendar.getMonth(), calendar.getDay());
    }
    public Calendar getCalendar(LocalDate localDate){
        Calendar calendar = new Calendar();
        calendar.setYear(localDate.getYear());
        calendar.setMonth(localDate.getMonthOfYear());
        calendar.setDay(localDate.getDayOfMonth());
        return calendar;
    }
}

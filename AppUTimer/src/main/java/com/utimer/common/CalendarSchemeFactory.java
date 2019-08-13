package com.utimer.common;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.google.common.base.Optional;
import com.haibin.calendarview.Calendar;
import ahtewlg7.utimer.entity.gtd.DeedSchemeInfo;

import org.joda.time.LocalDate;

/**
 * Created by lw on 2019/7/20.
 */
public class CalendarSchemeFactory {
    public Optional<DeedSchemeInfo> toObject(String json) {
        Optional<DeedSchemeInfo> calendar = Optional.absent();
        try{
            if(!TextUtils.isEmpty(json))
                calendar = Optional.fromNullable(JSON.parseObject(json, DeedSchemeInfo.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return calendar;
    }
    public Optional<String> toJsonStr(DeedSchemeInfo obj) {
        Optional<String> json = Optional.absent();
        try{
            if(obj != null)
                json = Optional.fromNullable(JSON.toJSONString(obj));
        }catch (Exception e){
            e.printStackTrace();
        }
        return json;
    }

    public LocalDate getLocalDate(@NonNull Calendar calendar){
        return new LocalDate(calendar.getYear(), calendar.getMonth(), calendar.getDay());
    }
    public Calendar getCalendar(@NonNull LocalDate localDate){
        Calendar calendar = new Calendar();
        calendar.setYear(localDate.getYear());
        calendar.setMonth(localDate.getMonthOfYear());
        calendar.setDay(localDate.getDayOfMonth());
        return calendar;
    }
    public Calendar getCalendar(long Instant){
        return getCalendar(new LocalDate(Instant));
    }
    public long getInstant(@NonNull Calendar calendar){
        return calendar.getTimeInMillis();
    }
}

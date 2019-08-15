package com.utimer.common;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.google.common.base.Optional;
import com.haibin.calendarview.Calendar;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import ahtewlg7.utimer.entity.gtd.DeedSchemeEntity;
import ahtewlg7.utimer.json.DateTimeFastjson;

/**
 * Created by lw on 2019/7/20.
 */
public class CalendarSchemeFactory {

    public CalendarSchemeFactory(){
        SerializeConfig.getGlobalInstance().put(DateTime .class, new DateTimeFastjson());
        ParserConfig.getGlobalInstance().putDeserializer(DateTime.class, new DateTimeFastjson());
    }

    public Optional<DeedSchemeEntity> toObject(String json) {
        Optional<DeedSchemeEntity> calendar = Optional.absent();
        try{
            if(!TextUtils.isEmpty(json))
                calendar = Optional.fromNullable(JSON.parseObject(json, DeedSchemeEntity.class));
        }catch (Exception e){
            e.printStackTrace();
        }
        return calendar;
    }
    public Optional<String> toJsonStr(DeedSchemeEntity obj) {
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

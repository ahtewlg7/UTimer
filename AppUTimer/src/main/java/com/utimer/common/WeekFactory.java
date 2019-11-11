package com.utimer.common;

import androidx.annotation.NonNull;

import org.joda.time.DateTime;

import ahtewlg7.utimer.enumtype.DATE_WEEK;

public class WeekFactory {
    public String getWeekName(@NonNull DateTime dateTime){
        return DATE_WEEK.valueOf(dateTime.getDayOfWeek()).getDetail();
    }
}

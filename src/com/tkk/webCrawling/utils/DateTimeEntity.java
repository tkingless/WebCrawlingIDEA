package com.tkk.webCrawling.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tkingless on 6/25/16.
 */
public class DateTimeEntity {

    static SimpleDateFormat default_dayFormat = new SimpleDateFormat("dd-MM-yyyy");
    static SimpleDateFormat default_timeFormat = new SimpleDateFormat("hh:mm:ss");

    SimpleDateFormat dayFormat;
    SimpleDateFormat timeFormat;

    Date instant;
    //default timezone is the one where the program runs
    TimeZone timezone;

    public static String GetCurrentTime() {
        return default_timeFormat.format(new Date());
    }

    public static String GetToday() {
        return default_dayFormat.format(new Date());
    }

    public DateTimeEntity(Date anInstant) {
        timezone = TimeZone.getDefault();
        instant = anInstant;
    }

    public DateTimeEntity() {
    }

    public void SetDayFormat(SimpleDateFormat format) {
        this.dayFormat = default_dayFormat;
    }

    public void SetTimeFormat(SimpleDateFormat format) {
        this.timeFormat = default_timeFormat;
    }

    public Date CalTimeIntervalDiff (Date time){
        //TODO
        return new Date(90);
    }

    public Date GetTimestamp () {
        return new Date(0);
    }

    public void SetTimezone (TimeZone tz) {
        timezone = tz;
    }
}

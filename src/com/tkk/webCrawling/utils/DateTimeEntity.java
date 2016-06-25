package com.tkk.webCrawling.utils;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tkingless on 6/25/16.
 */
public class DateTimeEntity {

    SimpleDateFormat dayFormat = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm:ss");
    Date day;
    Date time;
    TimeZone timezone;

    public static Date GetCurrentTime() {
        return null;
    }

    public static Date GetToday() {
        return null;
    }

    public DateTimeEntity(SimpleDateFormat dayFormat, SimpleDateFormat timeFormat) {
        this.dayFormat = dayFormat;
        this.timeFormat = timeFormat;
    }

    public DateTimeEntity() {
    }

    public void SetDayFormat(String format) {

    }

    public void SetTimeFormat(String format) {

    }

    public Time CalTimeIntervalDiff (Time time){
        return  new Time(90);
    }
}

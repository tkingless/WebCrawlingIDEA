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
        //TODO
        return null;
    }

    public static Date GetToday() {
        //TODO
        return null;
    }

    public DateTimeEntity(SimpleDateFormat dayFormat, SimpleDateFormat timeFormat) {
        this.dayFormat = dayFormat;
        this.timeFormat = timeFormat;
        //Set default timezone to HK
    }

    public DateTimeEntity() {
    }

    public void SetDayFormat(String format) {
        //TODO

    }

    public void SetTimeFormat(String format) {
        //TODO

    }

    public Time CalTimeIntervalDiff (Time time){
        //TODO
        return  new Time(90);
    }
}

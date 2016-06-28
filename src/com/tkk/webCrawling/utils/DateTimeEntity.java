package com.tkk.webCrawling.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tkingless on 6/25/16.
 */
public class DateTimeEntity {

    final static TimeZone default_timezone = TimeZone.getDefault();
    final static SimpleDateFormat default_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    final static SimpleDateFormat default_timeFormat = new SimpleDateFormat("HH:mm:ss"); //HH-> hh, 12hr format

    SimpleDateFormat dateFormat = default_dateFormat;
    SimpleDateFormat timeFormat = default_timeFormat;

    Date instant;
    //default timezone is the one where the program runs
    TimeZone timezone = default_timezone;

    public static String GetCurrentTime() {
        return default_timeFormat.format(new Date());
    }

    public static String GetToday() {
        return default_dateFormat.format(new Date());
    }

    public DateTimeEntity(String dateStr, String timeStr) throws ParseException {
        timezone = default_timezone;
        dateFormat.setTimeZone(timezone);
        timeFormat.setTimeZone(timezone);
        instant = dateFormat.parse(dateStr);
        instant = timeFormat.parse(timeStr);
    }

    public DateTimeEntity(String dateStr, String timeStr, SimpleDateFormat aDateFormat, SimpleDateFormat aTimeFormat) throws ParseException {
        dateFormat = aDateFormat;
        timeFormat = aTimeFormat;
        dateFormat.setTimeZone(timezone);
        timeFormat.setTimeZone(timezone);
        instant = dateFormat.parse(dateStr);
        instant = timeFormat.parse(timeStr);
    }

    public DateTimeEntity(){
        instant = new Date();
    }

    public void SetDayFormat(SimpleDateFormat format) {
        this.dateFormat = format;
    }

    public void SetTimeFormat(SimpleDateFormat format) {
        this.timeFormat = format;
    }

    //return timediff in milliseconds
    public long CalTimeIntervalDiff (Date time){
        return instant.getTime() - time.getTime();
    }

    public Date GetTheInstant () {
        return instant;
    }

    public void SetTimezone (TimeZone tz) {
        timezone = tz;
    }

    public TimeZone GetTimezone() {
        return timezone;
    }

    public String GetTheInstantDate (){
        return dateFormat.format(instant);
    }

    public String GetTheInstantTime() {
        return timeFormat.format(instant);
    }

    public String PrintGlobalDatetime() {
        SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        isoFormat.setTimeZone(timezone);
        return isoFormat.format(instant);
    }
}

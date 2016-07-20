package com.tkk.webCrawling.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by tkingless on 6/25/16.
 */
public class DateTimeEntity {

    final static TimeZone default_timezone = TimeZone.getDefault();
    final static SimpleDateFormat default_dateFormat = new SimpleDateFormat("dd-MM-yyyy");
    final static SimpleDateFormat default_timeFormat = new SimpleDateFormat("HH:mm:ss"); //HH-> hh, 12hr

    //Fatal*****, the simpleDateFormat is bug, but java itself shows nothing to this, make thread just jump whenever called this class
    //final static SimpleDateFormat default_parseInFormat = new SimpleDateFormat("dd/MM/yyyyTHH:mm:ss"); //HH-> hh, 12hr format

    final static SimpleDateFormat default_parseInFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy"); //HH-> hh, 12hr format

    SimpleDateFormat dateFormat = default_dateFormat;
    SimpleDateFormat timeFormat = default_timeFormat;

    Date instant;
    private ZonedDateTime zonedInstant;
    //default timezone is the one where the program runs
    TimeZone timezone = default_timezone;

    public static String GetCurrentTime() {
        return default_timeFormat.format(new Date());
    }

    public static String GetToday() {
        return default_dateFormat.format(new Date());
    }

    public static String GetCurrentYear() {
        return String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
    }

    public DateTimeEntity(String ParseInDatetime) throws ParseException {
        instant = default_parseInFormat.parse(ParseInDatetime);
        ConfigZoneDatetime();
    }

    public DateTimeEntity(String ParseInDatetime, SimpleDateFormat ParseInDateFormat) throws ParseException {
        instant = ParseInDateFormat.parse(ParseInDatetime);
        ConfigZoneDatetime();
    }

    public DateTimeEntity(){
        instant = new Date();
        ConfigZoneDatetime();
    }

    public DateTimeEntity(long timestampRectification){
        instant = new Date(timestampRectification);
        ConfigZoneDatetime();
    }

    void ConfigZoneDatetime(){
        LocalDateTime ldt = LocalDateTime.ofInstant(instant.toInstant(),timezone.toZoneId());
        zonedInstant = ZonedDateTime.of(ldt,timezone.toZoneId());
        ///System.out.printf("Original: %s %s\n",zonedInstant.toString(), zonedInstant.getZone().toString());
        ///System.out.printf("With Dubai: %s %s\n",zonedInstant.withZoneSameInstant(ZoneId.of("Asia/Dubai")),ZoneId.of("Asia/Dubai").toString() );
    }

    public void SetDayFormat(SimpleDateFormat format) {
        this.dateFormat = format;
    }

    public void SetTimeFormat(SimpleDateFormat format) {
        this.timeFormat = format;
    }

    //return timediff in milliseconds
    public long CalTimeIntervalDiff (DateTimeEntity time){
        return instant.getTime() - time.instant.getTime();
    }

    public Date GetTheInstant () {
        return instant;
    }

    public void SetTimezone (TimeZone tz) {
        timezone = tz;

        zonedInstant = ZonedDateTime.ofInstant(instant.toInstant(),timezone.toZoneId());
        System.out.printf("tz modified: %s %s\n",zonedInstant.toString(), zonedInstant.getZone().toString());

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

    @Override
    public String toString() {
        return zonedInstant.toString();
    }

}

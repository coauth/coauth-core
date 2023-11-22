package dev.coauth.module.reconfirm.utils;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public final class DateTimeUtils {

    public static final String DAYS = "DAYS";
    public static final String HOURS = "HRS";
    public static final String MINUTES = "MIN";
    public static final String SECONDS = "SEC";
    public static final long SECOND_MILLIS = 1000;
    public static final long MINUTE_MILLIS = SECOND_MILLIS * 60;
    public static final long HOUR_MILLIS = MINUTE_MILLIS * 60;
    public static final long DAY_MILLIS = HOUR_MILLIS * 24;
    public static final long YEAR_MILLIS = DAY_MILLIS * 365;

    private DateTimeUtils() {

    }

    public static Timestamp getCurrentDate() {
        Date today = new Date();
        return new Timestamp(today.getTime());
    }

    public static Timestamp addDays(Timestamp timestamp, int intDays) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(timestamp.getTime());
        c1.add(Calendar.DATE, intDays);
        Date date = c1.getTime();
        return new Timestamp(date.getTime());
    }

    public static Timestamp addHour(Timestamp timestamp, int intHour) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(timestamp.getTime());
        c1.add(Calendar.HOUR, intHour);
        Date date = c1.getTime();
        return new Timestamp(date.getTime());
    }

    public static Timestamp addMinutes(Timestamp timestamp, int intMinutes) {
        Calendar c1 = Calendar.getInstance();
        c1.setTimeInMillis(timestamp.getTime());
        c1.add(Calendar.MINUTE, intMinutes);
        Date date = c1.getTime();
        return new Timestamp(date.getTime());
    }

    public static Timestamp parseStringToDateTime(String strDate) throws ParseException {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date today = df.parse(strDate);
        return new Timestamp(today.getTime());

    }

    public static String stripTimeSec(Timestamp timestamp) {
        return timestamp.toString()
                .substring(0, 16);
    }

    public static LocalDateTime getCurrentLocalDateTime() {
        return getCurrentDate()
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

}
package com.fanstatic.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

@Service
public class DateUtils {

    static SimpleDateFormat formatter = new SimpleDateFormat();
    static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");

    public static Date toDate(String date, String pattern) {
        try {
            formatter.applyPattern(pattern);
            return formatter.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String toString(Date date, String pattern) {
        if (date == null) {
            return "";
        }
        formatter.applyPattern(pattern);
        return formatter.format(date);
    }

    public static Date addDays(Date date, long days) {
        date.setTime(date.getTime() + days * 24 * 60 * 60 * 1000);
        return date;
    }

    public static Date now() {
        return new Date();
    }

    public static Date getTimeFromString(String timeString) throws ParseException {
        return timeFormat.parse(timeString);
    }

    public static Date getStartOfToday(String startTime) {
        try {
            Date startOfDay = getStartOfDay(new Date());
            Date specifiedTime = getTimeFromString(startTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startOfDay);
            calendar.set(Calendar.HOUR_OF_DAY, getHourFromTime(specifiedTime));
            calendar.set(Calendar.MINUTE, getMinuteFromTime(specifiedTime));
            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    public static Date getEndOfToday(String endTime) {
        try {
            Date endOfDay = getEndOfDay(new Date());
            Date specifiedTime = getTimeFromString(endTime);

            Calendar calendar = Calendar.getInstance();
            calendar.setTime(endOfDay);
            calendar.set(Calendar.HOUR_OF_DAY, getHourFromTime(specifiedTime));
            calendar.set(Calendar.MINUTE, getMinuteFromTime(specifiedTime));
            // Nếu thời gian kết thúc là sau 12h00 AM, thì là ngày hôm sau
            if (getHourFromTime(specifiedTime) < 12) {
                calendar.add(Calendar.DAY_OF_MONTH, 1);
            }

            return calendar.getTime();
        } catch (Exception e) {
            return null;
        }
    }

    private static int getHourFromTime(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private static int getMinuteFromTime(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.MINUTE);
    }

    private static Date getStartOfDay(Date date) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateFormat.format(date));
    }

    // private static Date getEndOfDay(Date date) throws ParseException {
    // Calendar calendar = Calendar.getInstance();
    // calendar.setTime(date);
    // calendar.add(Calendar.DAY_OF_MONTH, 1);
    // calendar.add(Calendar.SECOND, -1);
    // return calendar.getTime();
    // }
    private static Date getEndOfDay(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }
}
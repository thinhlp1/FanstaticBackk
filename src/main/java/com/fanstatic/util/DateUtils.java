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

    public static int getHourFromTime(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public static int getMinuteFromTime(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        return calendar.get(Calendar.MINUTE);
    }

    public static Date getStartOfDay(Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return dateFormat.parse(dateFormat.format(date));
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getDateBefore(Integer day) {
        Date today = new Date(); // Ngày hiện tại
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(today);
        calendar.add(Calendar.DAY_OF_MONTH, -day); // Lùi về theo số ngày
        Date startDate = calendar.getTime();
        return startDate;
    }

    public static Date getDayBeforeTime(Integer hour) {
        Date date = new Date(System.currentTimeMillis() - (hour * 60 * 60 * 1000)); // Lùi về theo giờ
        return date;
    }

    public static Date getEndOfDay(Date date) throws ParseException {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        return calendar.getTime();
    }

    public static int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        return calendar.get(Calendar.YEAR);
    }
}
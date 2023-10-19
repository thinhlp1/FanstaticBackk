package com.fanstatic.util;



import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author nguye
 */

public class DateUtils {

    static SimpleDateFormat formatter = new SimpleDateFormat();

    public static Date toDate(String date, String pattern) {
        try {
            formatter.applyPattern(pattern);
            return formatter.parse(date);
        } catch (ParseException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String toString(Date date, String pattern) {
        if (date == null){
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
}
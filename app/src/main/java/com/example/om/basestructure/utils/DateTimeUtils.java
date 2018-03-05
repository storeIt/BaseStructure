package com.example.om.basestructure.utils;

import org.joda.time.LocalDate;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import java.text.SimpleDateFormat;

/**
 * Created by android on 17.02.18.
 */

public class DateTimeUtils {

    public static final String LOG_TAG = DateTimeUtils.class.getSimpleName();

    public static final String DEFAULT_DATE_FORMAT = "dd/MM/yyyy";
    public static final String DEFAULT_TIME_FORMAT = "HH:mm";

    private static SimpleDateFormat sdf = new SimpleDateFormat(DEFAULT_DATE_FORMAT);

    public static boolean isAfterToday(LocalDate validationDate) {
        return validationDate.isAfter(new LocalDate());
    }

    public static boolean isToday(LocalDate validationDate) {
        return validationDate.isEqual(new LocalDate());
    }

    public static boolean isBeforeToday(LocalDate validationDate) {
        return validationDate.isBefore(new LocalDate());
    }

    public static LocalDate parseStringToDate(String date) {
        LocalDate localDate = LocalDate.parse(date, DateTimeFormat.forPattern(DateTimeUtils.DEFAULT_DATE_FORMAT));
        return localDate;
    }

    public static String parseDateToString(LocalDate date) {
        return date.toString(DEFAULT_DATE_FORMAT);
    }

    public static LocalTime parseStringToTime(String time) {
        LocalTime localTime = LocalTime.parse(time, DateTimeFormat.forPattern(DateTimeUtils.DEFAULT_TIME_FORMAT));
        return localTime;
    }

    public static boolean isTimeBefore(LocalTime originTime) {
        return originTime.isBefore(new LocalTime());
    }

    public static boolean isTimeAfter(LocalTime originTime) {
        return originTime.isAfter(new LocalTime());
    }


//    public static String convertData() {
//        return
//    }
//
//    public static String convertTime() {
//        return
//    }
}

package com.lolibaba.base.delay.utils;

import java.util.Calendar;

public class CalendarUtils {
    public static long getCurrentTimeInMillis(int second) {
        Calendar cal = Calendar.getInstance();
        if (second > 0) {
            cal.add(Calendar.SECOND, second);
        }
        return cal.getTimeInMillis();
    }

    public static String getCurentTimeByStr(int second) {
        Calendar calendar = Calendar.getInstance();
        if (second > 0) {
            calendar.add(Calendar.SECOND, second);
        }
        String time = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        return time;
    }
}

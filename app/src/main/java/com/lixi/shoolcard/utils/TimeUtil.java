package com.lixi.shoolcard.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeUtil {
    private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public static String formatDate(){
        return simpleDateFormat.format(new Date());
    }

    public static String formatDate(int daysoff){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, daysoff); //当前时间增加/减少若干天
        return simpleDateFormat.format(calendar.getTime());
    }

    public static String formatDate(Date date,int daysoff){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, daysoff); //当前时间增加/减少若干天
        return simpleDateFormat.format(calendar.getTime());
    }

    public static boolean isToday(Date date){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        if(calendar.getTime().before(date)){
            return true;
        }
        return false;
    }
}

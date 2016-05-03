package com.oom.translatecommunication.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2015/7/27.
 * Mail: xzlight@outlook.com
 */
public class TimeUtils {
    private static SimpleDateFormat sf = null;

    public static String timeLongToString( long time, String type ) {
        Date d = new Date( time * 1000 );
        sf = new SimpleDateFormat( type );
        return sf.format( d );
    }

    public static final int WEEKDAYS = 7;

    public static String[] WEEK = { "Sun", "Mon", "Tue", "Wed", "Thu", "Fir", "Sat" };

    /**
     * 日期变量转成对应的星期字符串
     *
     * @param date
     * @return
     */
    public static String DateToWeek( Date date ) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime( date );
        int dayIndex = calendar.get( Calendar.DAY_OF_WEEK );
        if ( dayIndex < 1 || dayIndex > WEEKDAYS ) {
            return null;
        }

        return WEEK[ dayIndex - 1 ];
    }

    public static String MonthNumberToString( int monthNumber ) {
        String monthString = "";
        switch ( monthNumber ) {
            case 1:
                monthString = "一月";
                break;
            case 2:
                monthString = "二月";
                break;
            case 3:
                monthString = "三月";
                break;
            case 4:
                monthString = "四月";
                break;
            case 5:
                monthString = "五月";
                break;
            case 6:
                monthString = "六月";
                break;
            case 7:
                monthString = "七月";
                break;
            case 8:
                monthString = "八月";
                break;
            case 9:
                monthString = "九月";
                break;
            case 10:
                monthString = "十月";
                break;
            case 11:
                monthString = "十一月";
                break;
            case 12:
                monthString = "十二月";
                break;
        }
        return monthString;
    }

    public static long timeDValue( long timeA, long timeB ) {
        return ( timeA - timeB ) / ( 24 * 60 * 60 );
    }
}

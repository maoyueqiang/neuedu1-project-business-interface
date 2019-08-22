package com.neuedu.utils;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

public class DateUtils {
    private static final String STANDARD_FORMATE="yyyy-MM-dd HH:mm:ss";

    /**
     *
     * 将Date->字符串
     */
    public static String dateTOStr(Date date,String formate){
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(formate);
    }

    public static String dateTOStr(Date date){
        DateTime dateTime=new DateTime(date);
        return dateTime.toString(STANDARD_FORMATE);
    }

    /**
     *
     * 将字符串->Date
     */
    public static Date strToDate(String str){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(STANDARD_FORMATE);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

    public static Date strToDate(String str,String formate){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formate);
        DateTime dateTime = dateTimeFormatter.parseDateTime(str);
        return dateTime.toDate();
    }

}

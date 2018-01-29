package com.jiangyu.common.utils;

import android.os.SystemClock;
import android.text.TextUtils;

import java.text.DateFormat;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>Title: 时间工具类 </p>
 * <p> Description:</p>
 * <p>1.转换字符串为日期类型</p>
 * <p>2.转换当前日期为字符串</p>
 * <p>3.日期转换成毫秒</p>
 * <p>4.当前日期转换成毫秒字符串</p>
 * <p>5.毫秒转化为日期</p>
 * <p>6.取两个日期的时间差</p>
 * <p>7.取得一天后的时间</p>
 * <p>8.根据时间生成唯一标识</p>
 */
public class TimeUitls {
    /**
     * 时间格式
     */
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /**
     * 转换字符串为日期类型
     *
     * @param dateStr 日期字符串
     * @return 日期
     */
    public static Date string2Date(String dateStr) {
        return string2Date(dateStr, DATE_FORMAT);
    }

    /**
     * 转换字符串为日期类型
     *
     * @param dateStr    日期字符串
     * @param dateFormat 日期字符串格式
     * @return 日期
     */
    public static Date string2Date(String dateStr, String dateFormat) {
        Date result = null;
        try {
            DateFormat df = new SimpleDateFormat(dateFormat);
            result = df.parse(dateStr);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    /**
     * 转换当前日期为字符串
     *
     * @return
     */
    public static String date2String() {
        return date2String(new Date());
    }

    /**
     * 转换当前日期为字符串
     *
     * @param dateFormat
     * @return
     */
    public static String date2String(String dateFormat) {
        return date2String(new Date(), dateFormat);
    }

    /**
     * 转换日期为字符串
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String date2String(Date date) {
        return date2String(date, DATE_FORMAT);
    }

    /**
     * 转换日期为字符串
     *
     * @param date       日期
     * @param dateFormat 日期字符串格式
     * @return 日期字符串
     */
    public static String date2String(Date date, String dateFormat) {
        String dateStr = "";
        try {
            DateFormat df = new SimpleDateFormat(dateFormat);
            dateStr = df.format(date);
        } catch (Exception e) {
            dateStr = "";
        }
        return dateStr;
    }

    /**
     * 日期转换成毫秒
     *
     * @param date
     * @return
     */
    public static long date2Long(Date date) {
        return date.getTime();
    }

    /**
     * 当前日期转换成毫秒
     *
     * @return
     */
    public static long date2Long() {
        return new Date().getTime();
    }

    /**
     * 当前日期转换成毫秒字符串
     *
     * @return
     */
    public static String date2LongString() {
        return String.valueOf(date2Long());
    }

    /**
     * 毫秒转换成日期
     *
     * @param ms
     * @return
     */
    public static Date long2Date(long ms) {
        return new Date(ms);
    }

    /**
     * 将时间ms数转换为日期字符串
     *
     * @param ms
     * @return
     */
    public static String long2DateString(long ms) {
        return long2DateString(ms, DATE_FORMAT);
    }

    /**
     * 将时间ms数转换为日期字符串
     *
     * @param ms
     * @param dateFormat
     * @return
     */
    public static String long2DateString(long ms, String dateFormat) {
        String result = null;
        Date date = long2Date(ms);
        if (date != null) {
            result = date2String(date, dateFormat);
        }
        if (TextUtils.isEmpty(result)) {
            result = String.valueOf(ms);
        }
        return result;
    }

    /**
     * 日期字符串转换为ms值
     *
     * @param dateString
     * @return
     */
    public static long dateString2Long(String dateString) {
        return dateString2Long(dateString, DATE_FORMAT);
    }

    /**
     * 日期字符串转换为ms值
     *
     * @param dateString
     * @param dateFormat
     * @return
     */
    public static long dateString2Long(String dateString, String dateFormat) {
        Date date = string2Date(dateString, dateFormat);
        if (date != null) {
            return date2Long(date);
        }
        return 0;
    }

    /**
     * 比较时间差，如：相差5分钟
     *
     * @param time1 当前时间
     * @param time2 要比较的时间
     * @return
     */
    public static long compareTime(String time1, String time2) {
        long minutes = 0L;
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(time1);
            Date d2 = df.parse(time2);
            long diff = d1.getTime() - d2.getTime();
            minutes = diff / (60 * 1000);
        } catch (Exception e) {
        }
        return minutes;
    }

    /**
     * 用法：Date d = getDate("2013-01-18 16:16:43.0", "yyyy-MM-dd HH:mm:ss");
     *
     * @param dateStr
     * @param pattern
     * @return
     * @throws ParseException
     */
    public static Date getDate(String dateStr, String pattern) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.parse(dateStr);
    }

    /**
     * 用法: formatDate(d, "yyyy-MM-dd HH:mm:ss");
     *
     * @param currentDate
     * @param pattern
     * @return
     */
    public static String formatDate(Date currentDate, String pattern) {
        if (currentDate == null || pattern == null) {
            throw new NullPointerException("The arguments are null !");
        }
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(currentDate);
    }

    /**
     * 取得一天后的时间
     *
     * @return
     */
    public static String getTomorrowTime() {
        Format formatter = new SimpleDateFormat("yyyy-MM-dd hh:MM:ss");
        Calendar Cal = Calendar.getInstance();
        Cal.setTime(new Date());
        Cal.add(Calendar.HOUR_OF_DAY, 24);
        return formatter.format(Cal.getTime());
    }

    /**
     * 根据时间生成int类型的tag值
     *
     * @return
     */
    public static int createIntTag() {
        int result = -1;
        try {
            result = Long.valueOf(SystemClock.currentThreadTimeMillis() % Integer.MAX_VALUE).intValue();
        } catch (Exception e) {
            result = -1;
        }
        return result;
    }

    public static String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = "00:"+unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    public static String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10) {
            retStr = "0" + Integer.toString(i);
        } else {
            retStr = "" + i;
        }
        return retStr;
    }
}

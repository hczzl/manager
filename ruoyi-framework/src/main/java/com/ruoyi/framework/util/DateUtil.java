package com.ruoyi.framework.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期类型工具类
 *
 * @author zhongzhilong
 * @data 2020-03-01
 */
public class DateUtil {
    /**
     * 默认日期格式
     */
    public static String DEFAULT_FORMAT = "yyyy-MM-dd";


    /**
     * 格式化日期
     *
     * @param date 日期对象
     * @return String 日期字符串
     */
    public static String formatDate(Date date) {
        SimpleDateFormat f = new SimpleDateFormat(DEFAULT_FORMAT);
        String sDate = f.format(date);
        return sDate;
    }

    /**
     * 获取当年的第一天
     *
     * @param
     * @return
     */
    public static Date getCurrYearFirst() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }

    /**
     * 获取当年的最后一天
     *
     * @param
     * @return
     */
    public static Date getCurrYearLast() {
        Calendar currCal = Calendar.getInstance();
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }

    /**
     * 获取某年第一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }

    /**
     * 获取某年最后一天日期
     *
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year) {
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();

        return currYearLast;
    }

    /**
     * 获取当前月第一天
     *
     * @return
     */
    public static String getCurrentMonthFirstDay() {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, 0);
        // 1:本月第一天
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        String firstDay = format.format(calendar.getTime());
        return firstDay;
    }

    /**
     * 获取当前月份最后一天
     *
     * @return
     */
    public static String getCurrentMonthLastDay() {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        String lastDate = format.format(calendar.getTime());
        return lastDate;
    }


    /**
     * 获取上一个月的第一天
     *
     * @return
     */
    public static String getLastMonthLastDay() {
        SimpleDateFormat format = new SimpleDateFormat(DEFAULT_FORMAT);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 0);
        String lastDay = format.format(calendar.getTime());
        return lastDay;
    }

    public static Integer selectYear() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String year = format.format(date);
        return Integer.parseInt(year);
    }

    public static Integer selectMonth() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM");
        Date date = new Date();
        String year = format.format(date);
        String[] arr = year.split("-");
        String month = arr[1];
        return Integer.parseInt(month);
    }

}


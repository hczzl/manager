package com.ruoyi.web.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhongzhilong
 * @date 2019/8/26
 */
public class CalculateHours {
    /**
     * 日期格式化
     */
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    /**
     * 设置上班时间：该处时间可以根据实际情况进行调整
     * 上午上班时间,小时
     */
    int abh = 8;
    /**
     * 上午上班时间,分钟
     */
    int abm = 00;
    /**
     * 上午下班时间，小时
     */
    int aeh = 12;
    /**
     * 上午下班时间，分钟
     */
    int aem = 0;
    /**
     * 下午上班时间，小时
     */
    int pbh = 14;
    /**
     * 下午上班时间，分钟
     */
    int pbm = 00;
    /**
     * 下午下班时间，小时
     */
    int peh = 17;
    /**
     * 下午下班时间，分钟
     */
    int pem = 30;
    float h1 = abh + (float) abm / 60;
    float h2 = aeh + (float) aem / 60;
    float h3 = pbh + (float) pbm / 60;
    float h4 = peh + (float) pem / 60;
    /**
     * 每天上班小时数
     */
    float hoursPerDay = h2 - h1 + (h4 - h3);
    /**
     * 每周工作天数
     */
    int daysPerWeek = 5;
    /**
     * 每天的毫秒数
     */
    long milsecPerDay = 1000 * 60 * 60 * 24;
    /**
     * 每星期小时数
     */
    float hoursPerWeek = hoursPerDay * daysPerWeek;

    public float calculateHours(String beginTime, String endTime) {
        // 对输入的字符串形式的时间进行转换
        // 真实开始时间
        Date t1 = stringToDate(beginTime);
        // 真实结束时间
        Date t2 = stringToDate(endTime);
        // 对时间进行预处理
        t1 = processBeginTime(t1);
        t2 = processEndTime(t2);
        // 若开始时间晚于结束时间，返回0
        if (t1.getTime() > t2.getTime()) {
            return 0;
        }
        // 开始时间到结束时间的完整星期数
        int weekCount = (int) ((t2.getTime() - t1.getTime()) / (milsecPerDay * 7));
        // 计算总共的毫秒数，即时间秒数，最后得到一个月有多少个星期
        float totalHours = 0;
        // 计算一个月的有多少个小时
        totalHours += weekCount * hoursPerWeek;
        // 调整结束时间，使开始时间和结束时间在一个星期的周期之内
        // 结束时间毫秒数减去（总的周数*一周7天*一天多少秒），结果得到的就是
        t2.setTime(t2.getTime() - weekCount * 7 * milsecPerDay);
        // 记录开始时间和结束时间之间工作日天数
        int dayCounts = 0;
        // 调整开始时间，使得开始时间和结束时间在同一天，或者相邻的工作日内。
        while (t1.getTime() <= t2.getTime()) {
            Date temp = new Date(t1.getTime() + milsecPerDay);
            temp = processBeginTime(temp);
            temp.setHours(t1.getHours());
            temp.setMinutes(t1.getMinutes());
            if (temp.getTime() > t2.getTime()) {
                break;
            } else {
                t1 = temp;
                dayCounts++;
            }
        }
        totalHours += dayCounts * hoursPerDay;
        float hh1 = t1.getHours() + (float) t1.getMinutes() / 60;
        float hh2 = t2.getHours() + (float) t2.getMinutes() / 60;
        //处理开始结束是同一天的情况
        if (t1.getDay() == t2.getDay()) {
            float tt = 0;
            tt = hh2 - hh1;
            if (hh1 >= h1 && hh1 <= h2 && hh2 >= h3) {
                tt = tt - (h3 - h2);
            }
            totalHours += tt;
        } else {
            //处理开始结束不是同一天的情况
            float tt1 = h4 - hh1;
            float tt2 = hh2 - h1;
            if (hh1 <= h2) {
                tt1 = tt1 - (h3 - h2);
            }
            if (hh2 >= h3) {
                tt2 = tt2 - (h3 - h2);
            }
            totalHours += (tt1 + tt2);
        }
        return totalHours;
    }

    /**
     * 格式化输出时间： yyyy-mm-dd hh:mm:ss 星期x
     *
     * @param t
     * @return
     */
    private String printDate(Date t) {
        String str;
        String xingqi = null;
        switch (t.getDay()) {
            case 0:
                xingqi = "星期天";
                break;
            case 1:
                xingqi = "星期一";
                break;
            case 2:
                xingqi = "星期二";
                break;
            case 3:
                xingqi = "星期三";
                break;
            case 4:
                xingqi = "星期四";
                break;
            case 5:
                xingqi = "星期五";
                break;
            case 6:
                xingqi = "星期六";
                break;
            default:
                break;
        }
        str = format.format(t) + "  " + xingqi;
        return str;
    }

    /**
     * 对结束时间进行预处理，使其处于工作日内的工作时间段内
     *
     * @param t
     * @return
     */
    private Date processEndTime(Date t) {
        float h = t.getHours() + (float) t.getMinutes() / 60;
        //若结束时间晚于下午下班时间，将其设置为下午下班时间
        if (h >= h4) {
            t.setHours(peh);
            t.setMinutes(pem);
        } else {
            //若结束时间介于中午休息时间，那么设置为上午下班时间
            if (h >= h2 && h <= h3) {
                t.setHours(aeh);
                t.setMinutes(aem);
            } else {
                //若结束时间早于上午上班时间，日期向前推一天，并将时间设置为下午下班时间
                if (t.getHours() <= h1) {
                    t.setTime(t.getTime() - milsecPerDay);
                    t.setHours(peh);
                    t.setMinutes(pem);
                }
            }
        }
        //若结束时间是周末，那么将结束时间向前推移到最近的工作日的下午下班时间
        if (t.getDay() == 0) {
            t.setTime(t.getTime() - milsecPerDay * (t.getDay() == 6 ? 1 : 2));
            t.setHours(peh);
            t.setMinutes(pem);
        }
        if (t.getDay() == 6) {
            t.setTime(t.getTime() - milsecPerDay * (t.getDay() == 6 ? 1 : 2));
            t.setHours(peh);
            t.setMinutes(pem);
        }
        return t;
    }

    /**
     * 对开始时间进行预处理
     *
     * @param //t1
     * @return
     */
    private Date processBeginTime(Date t) {
        float h = t.getHours() + (float) t.getMinutes() / 60;
        //若开始时间晚于下午下班时间，将开始时间向后推一天
        if (h >= h4) {
            t.setTime(t.getTime() + milsecPerDay);
            t.setHours(abh);
            t.setMinutes(abm);
        } else {
            //若开始时间介于中午休息时间，那么设置为下午上班时间
            if (h >= h2 && h <= h3) {
                t.setHours(pbh);
                t.setMinutes(pbm);
            } else {
                //若开始时间早于上午上班时间，将hour设置为上午上班时间
                if (t.getHours() <= h1) {
                    t.setHours(abh);
                    t.setMinutes(abm);
                }
            }
        }
        //若开始时间是周末，那么将开始时间向后推移到最近的工作日的上午上班时间
        if (t.getDay() == 0) {
            t.setTime(t.getTime() + milsecPerDay * (t.getDay() == 6 ? 2 : 1));
            t.setHours(abh);
            t.setMinutes(abm);
        }
        if (t.getDay() == 6) {
            t.setTime(t.getTime() + milsecPerDay * (t.getDay() == 6 ? 2 : 1));
            t.setHours(abh);
            t.setMinutes(abm);
        }
        return t;
    }

    /**
     * 将字符串形式的时间转换成Date形式的时间
     *
     * @param time
     * @return
     */
    private Date stringToDate(String time) {
        try {
            return format.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 去除周末节假日工作小时
     *
     * @param beginTime
     * @param endTime
     * @return
     * @throws ParseException
     */
    public static float calculateHour(String beginTime, String endTime, List<String> list) throws ParseException {
        CalculateHours ch = new CalculateHours();
        float a = ch.calculateHours(beginTime, endTime);
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(FORMATTER.parse(beginTime));
        endDay.setTime(FORMATTER.parse(endTime));
        String[] workday = printDay(startDay, endDay);

        Calendar now = Calendar.getInstance();
        // 获取当前年份
        int year = now.get(Calendar.YEAR);

        String[] arr = list.toArray(new String[0]);
        //排除
        int holidays = arrContrast(workday, arr);
        float holidayHous = (float) (holidays * 7.5);
        float b = (float) (Math.round(a * 10)) / 10;
        float workHours = b - holidayHous;
        return workHours;
    }

    /**
     * 得到的是本月某任务所处的日期
     *
     * @param beginTime
     * @param endTime
     * @param list
     * @return
     * @throws ParseException
     */
    public static List<String> calculateHour1(String beginTime, String endTime, List<String> list) throws ParseException {
        CalculateHours ch = new CalculateHours();
        float a = ch.calculateHours(beginTime, endTime);
        Calendar startDay = Calendar.getInstance();
        Calendar endDay = Calendar.getInstance();
        startDay.setTime(FORMATTER.parse(beginTime));
        endDay.setTime(FORMATTER.parse(endTime));
        // 得到的是这个月中这个用户工作的日期，是一个数组
        String[] workday = printDay(startDay, endDay);

        Calendar now = Calendar.getInstance();
        // 获取当前年份
        int year = now.get(Calendar.YEAR);
        String[] arr = list.toArray(new String[0]);
        // 排除处于假期的日期
        List<String> holidays = arrContrast1(workday, arr);
        return holidays;
    }

    /**
     * 去除两个数组中相同日期
     *
     * @param arr1
     * @param arr2
     * @return
     */
    private static List<String> arrContrast1(String[] arr1, String[] arr2) {
        List<String> list = new LinkedList<>();
        for (String str : arr1) {
            // 处理第一个数组,list里面的值为1,2,3,4
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        // 如果第二个数组存在和第一个数组相同的值，就删除
        for (String str : arr2) {
            if (list.contains(str)) {
                list.remove(str);
                //++count;
            }
        }
        return list;
    }

    /**
     * 去除三个数组中相同日期
     */
    public static List<String> arrContrast2(List<String> arr1, List<String> arr2, List<String> arr3, List<String> arr4, List<String> arr5, List<String> arr6) {


        List<String> list = new ArrayList<>();
        if (!arr1.isEmpty()) {
            for (String str : arr1) {
                if (!list.contains(str)) {
                    list.add(str);
                }
            }
        }
        if (!arr2.isEmpty()) {
            for (String str : arr2) {
                if (!list.contains(str)) {
                    list.add(str);
                }
            }
        }
        if (!arr3.isEmpty()) {
            for (String str : arr3) {
                if (!list.contains(str)) {
                    list.add(str);
                }
            }
        }
        if (!arr4.isEmpty()) {
            for (String str : arr4) {
                if (!list.contains(str)) {
                    list.add(str);
                }
            }
        }
        if (!arr5.isEmpty()) {
            for (String str : arr5) {
                // 如果第四个数组存在和第一个数组相同的值，就删除
                if (!list.contains(str)) {
                    list.add(str);
                }
            }
        }
        // 如果第四个数组存在和第一个数组相同的值，就删除
        if (!arr6.isEmpty()) {
            for (String str : arr6) {
                if (!list.contains(str)) {
                    list.add(str);
                }
            }
        }
        if (list.isEmpty()) {
            list.add(null);
        }
        return list;
    }


    public static void main(String[] args) throws ParseException {
        String beginTime = "2019-9-1 8:00:00";
        String endTime = "2019-9-14 17:30:00";
        CalculateHours ch = new CalculateHours();
        float b = ch.calculateHours(beginTime, endTime);
        System.out.println(b);
        /*  float a=CalculateHours.CalculateHour(beginTime, endTime);*/
        /* System.out.println(a);*/
    }

    /**
     * 去除数组中相同日期
     *
     * @param arr1
     * @param arr2
     * @return
     */
    private static int arrContrast(String[] arr1, String[] arr2) {
        int count = 0;
        List<String> list = new LinkedList<>();
        for (String str : arr1) {
            // 处理第一个数组,list里面的值为1,2,3,4
            if (!list.contains(str)) {
                list.add(str);
            }
        }
        for (String str : arr2) {
            // 如果第二个数组存在和第一个数组相同的值，就删除
            if (list.contains(str)) {
                list.remove(str);
                ++count;
            }
        }
        return count;
    }

    private static final DateFormat FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    private static String[] printDay(Calendar startDay, Calendar endDay) {
        List<String> list = new ArrayList<String>();
        // 给出的日期开始日比终了日大则不执行打印
        if (startDay.compareTo(endDay) >= 0) {
            return new String[]{};
        }
        // 现在打印中的日期
        CalculateHours ch = new CalculateHours();
        Calendar currentPrintDay = startDay;
        while (true) {
            float a = ch.calculateHours(FORMATTER.format(currentPrintDay.getTime()) + " 8:00:00", FORMATTER.format(currentPrintDay.getTime()) + " 17:30:00");
            if (a > 0) {
                list.add(FORMATTER.format(currentPrintDay.getTime()));
            }
            // 日期加一
            // 日期加一后判断是否达到终了日，达到则终止打印
            if (currentPrintDay.compareTo(endDay) == 0) {

                break;
            }


            currentPrintDay.add(Calendar.DATE, 1);
        }
        String[] arr = list.toArray(new String[0]);
        return arr;
    }

    public static String getFinallyDate(String beginTime, String period) throws ParseException {
        Calendar startDay = Calendar.getInstance();
        startDay.setTime(FORMATTER.parse(beginTime));

        CalculateHours ch = new CalculateHours();
        Calendar currentPrintDay = Calendar.getInstance();
        currentPrintDay.setTime(FORMATTER.parse(beginTime));

        while (true) {
            float a = ch.calculateHours(FORMATTER.format(startDay.getTime()) + " 8:00:00", FORMATTER.format(currentPrintDay.getTime()) + " 17:30:00");
            if (a / 7.5 == Float.valueOf(period)) {
                break;
            }
            currentPrintDay.add(Calendar.DATE, 1);
        }
        return FORMATTER.format(currentPrintDay.getTime());
    }

    public static String getStringDate(Date now) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(now);
        return dateString;
    }
}
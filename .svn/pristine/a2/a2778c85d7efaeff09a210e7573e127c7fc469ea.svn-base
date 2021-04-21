package com.ruoyi.web.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class WorkTimeUtil {
    /**
     * @param map
     * @param startTime0
     * @param endTime0   获取两个时间段内的所有日期
     */
    static public void getWorkTime(HashMap map, Date startTime0, Date endTime0) {
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = format2.format(startTime0);
        String endTime = format2.format(endTime0);
        try {
            Date startTime1 = format2.parse(startTime);
            Date endTime1 = format2.parse(endTime);
            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(endTime1);
            while (startTime1.getTime() <= endTime1.getTime()) {
                map.put(format2.format(endTime1), "");
                tempStart.add(Calendar.DAY_OF_YEAR, -1);
                endTime1 = tempStart.getTime();
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

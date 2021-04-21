package com.ruoyi.common.utils;

import org.apache.poi.ss.usermodel.Chart;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFChart;
import org.openxmlformats.schemas.drawingml.x2006.chart.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Suqz
 * @version 1.0
 * @date 2019/12/12 10:10
 */

public class MyUtils {
    public static final Integer MAP_SIZE = 1000;
    static Map<Object, Object> map = new ConcurrentHashMap<>();

    private MyUtils() {

    }

    /**
     * 判断字符串是否为null 或者空串或者空格串
     *
     * @param strings 传入若干字符串
     * @return遍历传入的字符串，若有空则返回true
     */
    public static boolean isEmpty(String... strings) {
        for (String str : strings) {
            if (str == null || str.trim().length() == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 存入缓存
     *
     * @param key
     * @param value
     * @return
     */
    public static Object putMapCache(Object key, Object value) {
        if (map.size() > MAP_SIZE) {
            map.clear();
        }
        return map.put(key, value);
    }

    /**
     * 获取缓存
     *
     * @param object
     * @return
     */
    public static Object getMapCache(Object object) {
        return map.get(object);
    }

    /**
     * 清除缓存
     *
     * @param object
     * @return
     */
    public static Object removeMapCache(Object object) {
        return map.remove(object);
    }

    /**
     * length用户要求产生字符串的长度
     *
     * @param length
     * @return
     */
    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(str.length());
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    /**
     * 任务完成标识,-1:提交,0:未完成,1:任务完成,2:任务中止,5:任务完成审批中,6:任务中止审批中,7:任务变更审批中
     *
     * @param taskFinishflag
     * @return
     */
    public static String getTaskFinishFlagName(String taskFinishflag) {
        String taskFinishflagName;
        switch (taskFinishflag) {
            case "-1":
                taskFinishflagName = "提交";
                break;
            case "0":
                taskFinishflagName = "未完成";
                break;
            case "1":
                taskFinishflagName = "任务完成";
                break;
            case "2":
                taskFinishflagName = "任务中止";
                break;
            case "5":
                taskFinishflagName = "任务完成审批中";
                break;
            case "6":
                taskFinishflagName = "任务中止审批中";
                break;
            case "7":
                taskFinishflagName = "任务变更审批中";
                break;
            default:
                taskFinishflagName = "其它";
        }
        return taskFinishflagName;
    }

    /**
     * 立项成功标识，0：立项中，1：立项成功，2：立项失败,3:再次提交，4：立项撤回
     *
     * @param establishStatus
     * @return 立项状态具体名称
     */
    public static String getProjectEstablishStatusName(String establishStatus) {
        String establishStatusName;
        switch (establishStatus) {
            case "0":
                establishStatusName = "立项中";
                break;
            case "1":
                establishStatusName = "立项成功";
                break;
            case "2":
                establishStatusName = "立项失败";
                break;
            case "3":
                establishStatusName = "再次提交";
                break;
            case "4":
                establishStatusName = "立项撤回";
                break;
            default:
                establishStatusName = "其它";
        }
        return establishStatusName;
    }

    /**
     * 项目完成标识，0：未完成，1：项目完成，-1：项目完成审批中，2：项目中止,-2：项目中止审批中,
     *
     * @param finishFlag
     * @return 项目完成标识具体名称
     */
    public static String getProjectFinishFlagName(String finishFlag) {
        String finishFlagName;
        switch (finishFlag) {
            case "0":
                finishFlagName = "未完成";
                break;
            case "1":
                finishFlagName = "项目完成";
                break;
            case "2":
                finishFlagName = "项目中止";
                break;
            case "-1":
                finishFlagName = "项目完成审批中";
                break;
            case "-2":
                finishFlagName = "项目中止审批中";
                break;
            default:
                finishFlagName = "其它";
        }
        return finishFlagName;
    }


}

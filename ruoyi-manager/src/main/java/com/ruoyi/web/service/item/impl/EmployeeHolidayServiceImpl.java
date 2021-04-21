package com.ruoyi.web.service.item.impl;

import com.ruoyi.common.enums.TaskFinishFlagEnum;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.mapper.SysHolidaysMapper;
import com.ruoyi.web.service.EmployeeHolidayService;
import com.ruoyi.web.service.item.TaskTableService;
import com.ruoyi.web.util.WorkTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

/**
 * @author suqiuzhao
 * @date 2019/9/10
 */
@Service
public class EmployeeHolidayServiceImpl implements EmployeeHolidayService {
    @Autowired
    private SysHolidaysMapper sysHolidaysMapper;
    @Autowired
    private SysUserMapper sysUserMapper;
    @Autowired
    private TaskTableService taskTableService;

    private DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * 根据用户id以及任务指定时间段查询某个员工所有请假等不可工作的时间
     *
     * @param taskTable
     * @return 员工id、空闲状态、空闲的时间
     */
    @Override
    public LinkedHashMap seleEmployeeHolidayByIdAndByTaskTime(TaskTable taskTable) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        taskTable.setStartTime(new Date(taskTable.getStartTimes()));
        taskTable.setEndTime(new Date(taskTable.getEndTimes()));
        //将数据存到map发送至前端（key为用户id,value为空闲时间）
        LinkedHashMap userInfoMap = new LinkedHashMap();
        //用于记录任务时间段内的天数
        int dayCount = 0;
        //存储任务时间段内用户空闲的时间
        StringBuilder freeDays = new StringBuilder();
        //1存储用户不空闲的时间
        LinkedHashMap timeMap = new LinkedHashMap();
        //2存储任务指定的时间
        LinkedHashMap taskMap = new LinkedHashMap();
        //3获取节假日日期
        List<SysHolidays> sysHolidays = sysHolidaysMapper.selectSysHolidaysList(new SysHolidays());
        //4遍历去除不在这指定范围内的假期
        sysHolidays = sysHolidays.stream()
                .filter(item -> item.getHolidays().compareTo(format.format(taskTable.getStartTime())) >= 0)
                .filter(item -> item.getHolidays().compareTo(format.format(taskTable.getEndTime())) <= 0)
                .collect(Collectors.toList());
        //5将节假日日期添加到
        sysHolidays.forEach(item -> {
            timeMap.put(item.getHolidays(), "");
        });
        //6获取任务指定时间范围内的所有天数
        WorkTimeUtil.getWorkTime(taskMap, taskTable.getStartTime(), taskTable.getEndTime());
        dayCount = taskMap.size();
        //7获取用户的不空闲的时间段的所有天数并存在timeMap中
        List<TaskTable> mylists = getAllTaskById(taskTable);
        if (mylists != null) {
            for (int i = 0; i < mylists.size(); i++) {
                WorkTimeUtil.getWorkTime(timeMap, mylists.get(i).getStartTime(), mylists.get(i).getEndTime());
            }
        }
        //8任务指定的时间段内移除用户工作的时间，如果最后taskMap为空，则表示当前时间段内用户都没空，否则剩下的数表示用户在时间段内有空闲的日期
        for (AtomicInteger i = new AtomicInteger(); i.get() < taskMap.size(); i.getAndIncrement()) {
            timeMap.forEach((k, v) -> {
                if (taskMap.remove(k) != null) {
                    i.getAndDecrement();
                }
            });
        }
        taskMap.forEach((k, v) -> {
            freeDays.append(k + " ");
        });

        if (taskMap.size() == dayCount) {
            userInfoMap.put("uid", taskTable.getUserId());
            userInfoMap.put("status", "0");
            userInfoMap.put("msg", "");
        } else if (taskMap.size() == 0) {
            userInfoMap.put("uid", taskTable.getUserId());
            userInfoMap.put("status", "1");
            userInfoMap.put("msg", "");
        } else {
            userInfoMap.put("uid", taskTable.getUserId());
            userInfoMap.put("status", "1");
            String[] s = freeDays.toString().split(" ");
            LocalDate date;
            String bedate = "";
            StringBuilder newdates = new StringBuilder();
            for (int i = s.length - 1; i >= 0; i--) {
                if (i == s.length - 1) {
                    bedate = s[s.length - 1];
                }
                date = LocalDate.parse(s[i], fmt);
                if (i > 0 && !date.plusDays(1).toString().equals(s[i - 1])) {
                    if (bedate.compareTo(date.toString()) != 0) {
                        newdates.append(bedate + " ~ " + date + ",");
                    } else {
                        newdates.append(date + ",");
                    }
                    bedate = LocalDate.parse(s[i - 1], fmt).toString();
                    continue;
                }
                if (i == 0) {
                    if (bedate.compareTo(date.toString()) != 0) {
                        newdates.append(bedate + " ~ " + date);
                    } else {
                        newdates.append(date);
                    }
                }
            }
            //根据id获取员工的特长
            SysUser sysUser = sysUserMapper.selectUserById(taskTable.getUserId());
            userInfoMap.put("msg", newdates);
            userInfoMap.put("speciality", sysUser.getSpeciality());
        }
        return userInfoMap;
    }

    /**
     * 查询我的任务
     *
     * @param taskTable
     * @return
     */
    public List<TaskTable> getAllTaskById(TaskTable taskTable) {
        List<TaskTable> taskTables = taskTableService.selectTaskByUserId(taskTable.getUserId());
        taskTables = taskTables.stream()
                .filter(item -> !TaskFinishFlagEnum.COMMIT.getCode().equals(item.getTaskFinishflag()))
                .filter(item -> !TaskFinishFlagEnum.COMPLETE.getCode().equals(item.getTaskFinishflag()))
                .filter(item -> !TaskFinishFlagEnum.STOP.equals(item.getTaskFinishflag()))
                .filter(
                        item -> (item.getStartTime().compareTo(taskTable.getEndTime()) <= 0 && item.getEndTime().compareTo(taskTable.getEndTime()) >= 0)
                                || (item.getStartTime().compareTo(taskTable.getStartTime()) >= 0 && item.getEndTime().compareTo(taskTable.getEndTime()) <= 0)
                                || (item.getStartTime().compareTo(taskTable.getStartTime()) <= 0 && item.getEndTime().compareTo(taskTable.getStartTime()) >= 0)
                )
                .collect(Collectors.toList());
        return taskTables;
    }
}

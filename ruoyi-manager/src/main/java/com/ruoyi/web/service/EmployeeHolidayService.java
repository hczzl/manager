package com.ruoyi.web.service;

import com.ruoyi.web.domain.TaskTable;

import java.util.LinkedHashMap;

/**
 * @author zhongzhilong
 * @date 2019/9/10
 */
public interface EmployeeHolidayService {
    /**
     * 根据用户id以及任务指定时间段查询某个员工所有请假等不可工作的时间
     *
     * @param taskTable
     * @return
     */
    LinkedHashMap seleEmployeeHolidayByIdAndByTaskTime(TaskTable taskTable);
}

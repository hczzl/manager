package com.ruoyi.web.util;


import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.domain.vo.TaskQuashVo;
import org.apache.commons.beanutils.BeanUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author zhongzhilong
 * @date 2020/11/9 0009
 * @description mananger模块工具类
 */
public class UserUtil {

    public static SysUser getUser() {
        SysUser currentUser = ShiroUtils.getSysUser();
        return currentUser;
    }

    /**
     * 对象间相同属性复制
     * 存在利弊
     *
     * @param vo
     * @return
     */
    public static TaskTable copy(TaskQuashVo vo) {
        TaskTable taskTable = new TaskTable();
        try {
            BeanUtils.copyProperties(taskTable, vo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return taskTable;
    }

    public static TaskTable copyValue(TaskQuashVo vo) {
        TaskTable taskTable = new TaskTable();
        if (vo.getChargepeopleId() != null) {
            taskTable.setChargepeopleId(vo.getChargepeopleId());
        }
        if (ShiroUtils.isNotNull(vo.getChargePeopleName())) {
            taskTable.setChargePeopleName(vo.getChargePeopleName());
        }
        if (ShiroUtils.isNotNull(vo.getTaskTitle())) {
            taskTable.setTaskTitle(vo.getTaskTitle());
        }
        if (ShiroUtils.isNotNull(vo.getTaskDescribe())) {
            taskTable.setTaskDescribe(vo.getTaskDescribe());
        }
        if (ShiroUtils.isNotNull(vo.getUrgencyLevel())) {
            taskTable.setUrgencyLevel(vo.getUrgencyLevel());
        }
        if (vo.getStartTimes() != null) {
            taskTable.setStartTimes(vo.getStartTimes());
        }
        if (vo.getEndTimes() != null) {
            taskTable.setEndTimes(vo.getEndTimes());
        }
        if (vo.getPlanInOut() != null) {
            taskTable.setPlanInOut(vo.getPlanInOut());
        }
        taskTable.setWarnDays(vo.getWarnDays());
        taskTable.setRemindChargePeople(vo.getRemindChargePeople());
        taskTable.setRemindPanticiants(vo.getRemindPanticiants());
        taskTable.setFirstUserId(vo.getFirstUserId());
        taskTable.setPeriod(vo.getPeriod());
        taskTable.setParentId(vo.getParentId());
        taskTable.setWarnFlag(vo.getWarnFlag());
        taskTable.setCcIds(vo.getCcIds());
        taskTable.setProjectId(vo.getProjectId());
        return taskTable;
    }

    /**
     * 时间戳转换日期
     *
     * @param time
     * @return
     */
    public static String timeToDate(Long time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String stringTime = sdf.format(new Date(time));
        return stringTime;
    }
}

package com.ruoyi.web.controller.item;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.web.domain.SysHolidays;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.service.EmployeeHolidayService;
import com.ruoyi.web.service.SysHolidaysService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Suqz
 * @version 1.0
 * @date 2020/9/14 15:06
 */
@RestController
@Api(value = "EmployeeHolidayController", tags = "员工空闲时间相关接口")
public class EmployeeHolidayController {
    @Autowired
    private EmployeeHolidayService employeeHolidayService;
    @Autowired
    private SysHolidaysService sysHolidaysService;
    @Autowired
    private SysUserMapper sysUserMapper;


    /**
     * 根据用户id以及任务指定时间段查询某个员工所有请假等不可工作的时间，判断是否空闲
     */
    @ApiOperation("根据用户id以及任务指定时间段查询某个员工所有请假等不可工作的时间,判断是否空闲")
    @PostMapping(value = "seleEmployeeHolidayByIdAndByTaskTime")
    public LinkedHashMap seleEmployeeHolidayByIdAndByTaskTime(TaskTable taskTable) {
        return employeeHolidayService.seleEmployeeHolidayByIdAndByTaskTime(taskTable);
    }


    /**
     * 根据year的指定年，向数据库中插入指定年的所有周六周日的日期
     *
     * @param year
     */
    @ApiOperation("根据year的指定年，向数据库中插入指定年的所有周六周日的日期")
    @PostMapping("/test")
    public void test(int year) {
        SysHolidays sysHolidays = new SysHolidays();
        List<String> dateList = new ArrayList<String>();
        SimpleDateFormat simdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = new GregorianCalendar(year, 0, 1);
        int i = 1;
        while (calendar.get(Calendar.YEAR) < year + 1) {
            calendar.set(Calendar.WEEK_OF_YEAR, i++);
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            if (calendar.get(Calendar.YEAR) == year) {
                sysHolidays.setHolidays(simdf.format(calendar.getTime()));
                sysHolidaysService.insertSysHolidays(sysHolidays);
                System.out.println("周日：" + simdf.format(calendar.getTime()));
                dateList.add(simdf.format(calendar.getTime()));
            }
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
            if (calendar.get(Calendar.YEAR) == year) {
                sysHolidays.setHolidays(simdf.format(calendar.getTime()));
                sysHolidaysService.insertSysHolidays(sysHolidays);
                System.out.println("周六：" + simdf.format(calendar.getTime()));
                dateList.add(simdf.format(calendar.getTime()));
            }
        }
        System.out.println(dateList.size());
    }

    @ApiOperation("导出用户密码")
    @PostMapping("/rrtest")
    public AjaxResult rrtest() {
        List<SysUser> list = sysUserMapper.selectAllInitPassWord();
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "操作日志");
    }
}

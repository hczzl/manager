package com.ruoyi.web.controller.system;


import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.exception.BusinessException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.shiro.service.SysPasswordService;
import com.ruoyi.framework.util.DateUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.vo.SysUserStatisticsByMonthVo;
import com.ruoyi.system.domain.vo.SysUserStatisticsByYear;
import com.ruoyi.system.service.*;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.mapper.item.SysUserWorksMapper;
import com.ruoyi.web.mapper.item.SysYearMonthMapper;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.web.service.SysHolidaysService;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.audit.AuditFlowNodeRoleService;
import com.ruoyi.web.service.item.*;
import com.ruoyi.web.util.CalculateHours;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;


/**
 * @author zhongzhilong
 * @date 2019-09-01
 * @description 用户信息
 */
@Controller
@RequestMapping("/system/user")
public class SysUserController extends BaseController {

    private String prefix = "system/user";

    @Autowired
    private ISysUserService userService;
    @Autowired
    private ISysRoleService roleService;
    @Autowired
    private ISysPostService postService;
    @Autowired
    private SysPasswordService passwordService;
    @Autowired
    private AuditFlowNodeRoleService auditFlowNodeRoleService;
    @Autowired
    private SysFileInfoService sysFileInfoService;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private AuditFlowCurrentService auditFlowCurrentService;
    @Autowired
    private TaskUserService taskUserService;
    @Autowired
    private SysHolidaysService holidaysService;
    @Autowired
    private ISysRoleService sysRoleService;
    @Autowired
    private ISysUserTokenService userTokenService;
    @Autowired
    private TaskEcharService taskEcharService;
    @Autowired
    private SysUserWorksMapper sysUserWorksMapper;
    @Autowired
    private SysUserWorksService sysUserWorksService;
    @Autowired
    private SysYearMonthMapper sysYearMonthMapper;
    @Autowired
    private SysYearMonthService sysYearMonthService;

    @RequiresPermissions("system:user:view")
    @GetMapping()
    public String user() {
        return prefix + "/user";
    }

    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public List<SysUser> list(SysUser user) {
        if (user.getSearchFlowId() != null && !"".equals(user.getSearchFlowId())) {
            AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
            auditFlowNodeRole.setSearchFlowId(user.getSearchFlowId());
            List<AuditFlowNodeRole> auditFlowNodeRoleList = auditFlowNodeRoleService.selectFlowNodeRoleList(auditFlowNodeRole);//得到具有审批权限的用户角色
            String roleId = "";
            for (AuditFlowNodeRole auditFlowNodeRole1 : auditFlowNodeRoleList) {
                roleId += auditFlowNodeRole1.getRoleId() + ",";
            }
            if (roleId.length() > 0) {
                roleId = roleId.substring(0, roleId.length() - 1);
            }
            user.setSearchFlowId(roleId);
        }
        Boolean superAdmin = userService.superAdmin(ShiroUtils.getUserId());
        if (!superAdmin) {
            //普通用户只能看到未被禁用人员
            user.setStatus("0");
        }
        List<SysUser> resultList = userService.selectUserList(user);

        return resultList;
    }

    @ApiOperation("任务统计界面调用的接口")
    @RequestMapping(value = "/statistics", method = {RequestMethod.POST})
    @ResponseBody
    public List<SysUser> statisticsList(SysUser user) {
        if (user.getSearchFlowId() != null && !"".equals(user.getSearchFlowId())) {
            AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
            auditFlowNodeRole.setSearchFlowId(user.getSearchFlowId());
            List<AuditFlowNodeRole> auditFlowNodeRoleList = auditFlowNodeRoleService.selectFlowNodeRoleList(auditFlowNodeRole);//得到具有审批权限的用户角色
            String roleId = "";
            for (AuditFlowNodeRole auditFlowNodeRole1 : auditFlowNodeRoleList) {
                roleId += auditFlowNodeRole1.getRoleId() + ",";
            }
            if (roleId.length() > 0) {
                roleId = roleId.substring(0, roleId.length() - 1);
            }
            user.setSearchFlowId(roleId);
        }
        List<SysUser> userList = userService.selectStatisticUserList(user);
        return userList;
    }


    @Log(title = "统计导出", businessType = BusinessType.EXPORT)
    @PostMapping("/statisticsExport")
    @ResponseBody
    public AjaxResult statisticsExport(SysUser user) {
        NumberFormat format = NumberFormat.getPercentInstance();
        format.setMinimumFractionDigits(2);
        if (user.getMonth() != 13) {
            List<SysUserStatisticsByMonthVo> list = JSON.parseArray(JSON.toJSONString(statisticsList(user)), SysUserStatisticsByMonthVo.class);
            for (SysUserStatisticsByMonthVo vo : list) {
                String saturation = vo.getSaturation();
                saturation = format.format(Double.parseDouble(saturation));
                vo.setSaturation(saturation);
            }
            ExcelUtil<SysUserStatisticsByMonthVo> util = new ExcelUtil<SysUserStatisticsByMonthVo>(SysUserStatisticsByMonthVo.class);
            return util.exportExcel(list, user.getYear() + "年" + user.getMonth() + "月" + "任务统计数据");
        } else {
            List<SysUserStatisticsByYear> list = JSON.parseArray(JSON.toJSONString(statisticsList(user)), SysUserStatisticsByYear.class);
            ExcelUtil<SysUserStatisticsByYear> util = new ExcelUtil<SysUserStatisticsByYear>(SysUserStatisticsByYear.class);
            return util.exportExcel(list, user.getYear() + "年度统计数据");
        }
    }

    @Log(title = "个人统计导出", businessType = BusinessType.EXPORT)
    @PostMapping("/statisticsExportByUser")
    @ResponseBody
    public AjaxResult statisticsExportByUser(TaskEcharQueryInfo queryInfo, HttpServletResponse response) {
        NumberFormat format = NumberFormat.getPercentInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "";
        try {
            // 生成导出模板
            Workbook wb = new XSSFWorkbook();
            CellStyle style = wb.createCellStyle();
            // 左右居中
            style.setAlignment(HorizontalAlignment.CENTER);
            // 上下居中
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            // 设置表头样式
            CellStyle styleTitle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setFontName("宋体");
            // font.setColor((short) 5);
            // 字体加粗
            font.setBold(true);
            styleTitle.setFont(font);
            styleTitle.setFillForegroundColor((short) 13);
            styleTitle.setFillPattern(FillPatternType.forInt(13));
            styleTitle.setAlignment(HorizontalAlignment.CENTER);
            styleTitle.setVerticalAlignment(VerticalAlignment.CENTER);

            String userName = userService.getName(queryInfo.getUserId());
            Integer year = queryInfo.getYear();
            // Integer year = 2020;
            Integer month = queryInfo.getMonth();
            // Integer month = 13;
            String userId = queryInfo.getUserId().toString();
            // String userId = "224";
            if (month == null || year == null) {
                return AjaxResult.error("年份或者月份参数错误!");
            }
            if (month == 13) {
                Sheet yearSheet = wb.createSheet(userName + "_年度评分统计情况");
                yearSheet.setDefaultColumnWidth(20);
                AtomicReference<Integer> yearTitle = new AtomicReference<>(1);
                Row yearTitleRow = yearSheet.createRow(0);
                yearTitleRow.createCell(0).setCellValue("年度评分统计情况：");
                yearTitleRow.getCell(0).setCellStyle(styleTitle);
                // 显示的是年度、月度的所有评分情况
                AtomicReference<Integer> yearStatistic = new AtomicReference<>(2);
                Row yearFirstRow = yearSheet.createRow(1);
                List<String> titleList = Arrays.asList("年份(年)", "年度总评分", "年度绩效平均分", "年度表现分", "任务完成量", "任务未完成量", "任务逾期量", "任务未逾期量");
                for (int i = 0; i < titleList.size(); i++) {
                    yearFirstRow.createCell(i).setCellValue(titleList.get(i));
                    yearFirstRow.getCell(i).setCellStyle(styleTitle);
                }
                Map<String, Object> map = new HashMap<>();
                map.put("userId", userId);
                map.put("month", month);
                map.put("year", year);
                List<Map<String, Object>> list = sysUserWorksMapper.selectMonthScore(map);
                if (ShiroUtils.isNotEmpty(list)) {
                    for (Map<String, Object> map2 : list) {
                        Row row2 = yearSheet.createRow(yearStatistic.get());
                        row2.createCell(0).setCellValue(year);
                        if (map2.containsKey("yearAverageScore") && map2.get("yearAverageScore") != null) {
                            row2.createCell(1).setCellValue((Double) map2.get("yearAverageScore"));
                        } else {
                            row2.createCell(1).setCellValue(0.0);
                        }
                        if (map2.containsKey("yearScore") && map2.get("yearScore") != null) {
                            row2.createCell(2).setCellValue((Double) map2.get("yearScore"));
                        } else {
                            row2.createCell(2).setCellValue(0.0);
                        }
                        if (map2.containsKey("personalScore") && map2.get("personalScore") != null) {
                            row2.createCell(3).setCellValue((Double) map2.get("personalScore"));
                        } else {
                            row2.createCell(3).setCellValue(0.0);
                        }
                        row2.createCell(4).setCellValue((Integer) map2.get("taskAlreadyFinish"));
                        row2.createCell(5).setCellValue((Integer) map2.get("taskNotFinish"));
                        row2.createCell(6).setCellValue((Integer) map2.get("taskOverdue"));
                        row2.createCell(7).setCellValue((Integer) map2.get("taskNotOverdue"));
                        row2.getCell(0).setCellStyle(style);
                        row2.getCell(1).setCellStyle(style);
                        row2.getCell(2).setCellStyle(style);
                        row2.getCell(3).setCellStyle(style);
                        row2.getCell(4).setCellStyle(style);
                        row2.getCell(5).setCellStyle(style);
                        row2.getCell(6).setCellStyle(style);
                        row2.getCell(7).setCellStyle(style);
                        yearStatistic.set(yearStatistic.get() + 1);
                    }
                }
                AtomicReference<Integer> monthTitle = new AtomicReference<>(5);
                Row monthTitleRow = yearSheet.createRow(4);
                monthTitleRow.createCell(0).setCellValue("月度评分统计情况：");
                monthTitleRow.getCell(0).setCellStyle(styleTitle);
                AtomicReference<Integer> monthStatistic = new AtomicReference<>(6);
                Row monthFirstRow = yearSheet.createRow(5);
                List<String> monthTitleList = Arrays.asList("月份(月)", "月度绩效评分", "任务平均分", "月综合表现分", "当月工作天数", "当月工作任务天数", "工作饱和度", "当月发起任务数");
                for (int i = 0; i < monthTitleList.size(); i++) {
                    monthFirstRow.createCell(i).setCellValue(monthTitleList.get(i));
                    monthFirstRow.getCell(i).setCellStyle(styleTitle);
                }
                List<String> monthList = sysYearMonthService.selectMonthList(year);
                map.put("month", null);
                map.put("yearMonthList", monthList);
                list = sysUserWorksMapper.selectMonthScore(map);
                if (ShiroUtils.isNotEmpty(list)) {
                    for (Map<String, Object> map2 : list) {
                        Row row2 = yearSheet.createRow(monthStatistic.get());
                        row2.createCell(0).setCellValue((Integer) map2.get("month"));
                        if (map2.containsKey("monthScore") && map2.get("monthScore") != null) {
                            row2.createCell(1).setCellValue((Double) map2.get("monthScore"));
                        } else {
                            row2.createCell(1).setCellValue(0.0);
                        }
                        if (map2.containsKey("taskAverageScore") && map2.get("taskAverageScore") != null) {
                            row2.createCell(2).setCellValue((Double) map2.get("taskAverageScore"));
                        } else {
                            row2.createCell(2).setCellValue(0.0);
                        }
                        if (map2.containsKey("multipleMonthScore") && map2.get("multipleMonthScore") != null) {
                            row2.createCell(3).setCellValue((Double) map2.get("multipleMonthScore"));
                        } else {
                            row2.createCell(3).setCellValue(0.0);
                        }
                        row2.createCell(4).setCellValue((Integer) map2.get("workDays"));
                        row2.createCell(5).setCellValue((Integer) map2.get("period"));
                        Double saturation = (Double) map2.get("saturation");
                        if (saturation != null) {
                            row2.createCell(6).setCellValue(format.format(saturation));
                        }
                        row2.createCell(7).setCellValue((Integer) map2.get("createTaskNum"));
                        row2.getCell(0).setCellStyle(style);
                        row2.getCell(1).setCellStyle(style);
                        row2.getCell(2).setCellStyle(style);
                        row2.getCell(3).setCellStyle(style);
                        row2.getCell(4).setCellStyle(style);
                        row2.getCell(5).setCellStyle(style);
                        row2.getCell(6).setCellStyle(style);
                        row2.getCell(7).setCellStyle(style);
                        monthStatistic.set(monthStatistic.get() + 1);
                    }
                }
                // 第二张表-本年度参与所有的项目表
                Sheet projectSheet = wb.createSheet(userName + "_年度参与项目统计情况");
                projectSheet.setDefaultColumnWidth(30);
                AtomicReference<Integer> projectTitle = new AtomicReference<>(1);
                Row projectTitleRow = projectSheet.createRow(0);
                projectTitleRow.createCell(0).setCellValue("年度参与项目统计情况：");
                projectTitleRow.getCell(0).setCellStyle(styleTitle);
                AtomicReference<Integer> projectStatistic = new AtomicReference<>(2);
                Row projectRow = projectSheet.createRow(1);
                List<String> projectTitleList = Arrays.asList("序号", "项目名称", "参与任务数", "完成任务数", "未完成任务数", "项目类型", "项目负责人", "项目立项情况", "项目完成情况", "项目开始时间", "项目结束时间");
                for (int i = 0; i < projectTitleList.size(); i++) {
                    projectRow.createCell(i).setCellValue(projectTitleList.get(i));
                    projectRow.getCell(i).setCellStyle(styleTitle);
                }
                Map<String, Object> map2 = new HashMap<>();
                String start = DateUtils.convert2String(queryInfo.getStartTimes(), "");
                String[] arr = start.split("-");
                int paramyear = Integer.parseInt(arr[0]);
                int nowYear = DateUtil.selectYear();
                if (paramyear == nowYear) {
                    // 若获取的是今年，则从表中获取开始时间和结束时间，且判断是不是当前月
                    StringBuilder builder = new StringBuilder();
                    builder.append(nowYear).append("-");
                    // 表中数据今年第一天:yyyy-MM-dd
                    String firstDay = sysYearMonthMapper.selectMinDate(builder.toString());
                    // 表中数据今年最后一天
                    String lastDay = sysYearMonthMapper.selectMaxDate(builder.toString());
                    // 当前月份最后一天
                    String currentDay = DateUtil.getCurrentMonthLastDay();
                    // 当前月份为最大月份的情况
                    if (lastDay.equals(currentDay)) {
                        // 获取上个月的最后一天
                        String lastMonthLastDay = DateUtil.getLastMonthLastDay();
                        if (firstDay.compareTo(lastMonthLastDay) < 0) {
                            lastDay = lastMonthLastDay;
                        } else {
                            lastDay = firstDay;
                        }
                    }
                    map2.put("startTime", firstDay + " 00:00:00");
                    map2.put("endTime", lastDay + " 23:59:59");
                } else {
                    map2.put("startTime", DateUtils.convert2String(queryInfo.getStartTimes(), DateUtils.YYYY_MM_DD_HH_MM_SS));
                    map2.put("endTime", DateUtils.convert2String(queryInfo.getEndTimes(), DateUtils.YYYY_MM_DD_HH_MM_SS));
                }
                map2.put("chargepeopleId", userId);
                List<Map<String, Object>> resultList = taskTableService.selectJoinProjectList(map2);
                if (ShiroUtils.isNotEmpty(resultList)) {
                    int i = 1;
                    for (Map<String, Object> map3 : resultList) {
                        Integer projectId = (Integer) map3.get("projectId");
                        map2.put("projectId", projectId);
                        Map<String, Object> taskMap = taskTableService.selectTaskCount(map2);
                        Row row = projectSheet.createRow(projectStatistic.get());
                        row.createCell(0).setCellValue(i);
                        row.createCell(1).setCellValue((String) map3.get("title"));
                        row.createCell(2).setCellValue((Long)taskMap.get("taskCount"));
                        row.createCell(3).setCellValue((Long)taskMap.get("taskFinishCount"));
                        row.createCell(4).setCellValue((Long)taskMap.get("taskStopCount"));
                        Integer projectType = (Integer) map3.get("projectType");
                        if (projectType != null) {
                            if (projectType == 0) {
                                // 科研项目
                                row.createCell(5).setCellValue("科研项目");
                                row.createCell(6).setCellValue((String) map3.get("chargepeopleName"));
                            } else if (projectType == 1) {
                                // 市场项目
                                row.createCell(5).setCellValue("市场项目");
                                row.createCell(6).setCellValue((String) map3.get("techniqueName"));
                            }
                        }
                        row.createCell(7).setCellValue(MyUtils.getProjectEstablishStatusName((String) map3.get("establishStatus")));
                        row.createCell(8).setCellValue(MyUtils.getProjectFinishFlagName((String) map3.get("projectFinishFlag")));
                        Date startTime = (Date) map3.get("startTime");
                        Date endTime = (Date) map3.get("endTime");
                        row.createCell(9).setCellValue(sdf.format(startTime));
                        row.createCell(10).setCellValue(sdf.format(endTime));
                        row.getCell(0).setCellStyle(style);
                        row.getCell(1).setCellStyle(style);
                        row.getCell(2).setCellStyle(style);
                        row.getCell(3).setCellStyle(style);
                        row.getCell(4).setCellStyle(style);
                        row.getCell(5).setCellStyle(style);
                        row.getCell(6).setCellStyle(style);
                        row.getCell(7).setCellStyle(style);
                        row.getCell(8).setCellStyle(style);
                        row.getCell(9).setCellStyle(style);
                        row.getCell(10).setCellStyle(style);
                        ++i;
                        projectStatistic.set(projectStatistic.get() + 1);
                    }
                }
            } else {
                Sheet monthSheet = wb.createSheet(userName + "_" + month + "月份评分及参与项目统计情况");
                monthSheet.setDefaultColumnWidth(20);
                // 月度统计情况
                AtomicReference<Integer> monthTitle = new AtomicReference<>(1);
                Row monthTitleRow = monthSheet.createRow(0);
                monthTitleRow.createCell(0).setCellValue("月度评分统计情况：");
                monthTitleRow.getCell(0).setCellStyle(styleTitle);
                AtomicReference<Integer> monthStatistic = new AtomicReference<>(2);
                Row monthFirstRow = monthSheet.createRow(1);
                List<String> monthTitleList = Arrays.asList("月份(月)", "月度评分", "任务平均分", "月综合表现分", "当月工作天数", "当月工作任务天数", "工作饱和度", "当月发起任务数");
                for (int i = 0; i < monthTitleList.size(); i++) {
                    monthFirstRow.createCell(i).setCellValue(monthTitleList.get(i));
                    monthFirstRow.getCell(i).setCellStyle(styleTitle);
                }
                Map<String, Object> map = new HashMap<>();
                map.put("userId", userId);
                map.put("month", month);
                map.put("year", year);
                List<Map<String, Object>> list = sysUserWorksMapper.selectMonthScore(map);
                if (ShiroUtils.isNotEmpty(list)) {
                    for (Map<String, Object> map2 : list) {
                        Row row2 = monthSheet.createRow(monthStatistic.get());
                        row2.createCell(0).setCellValue((Integer) map2.get("month"));
                        if (map2.containsKey("monthScore") && map2.get("monthScore") != null) {
                            row2.createCell(1).setCellValue((Double) map2.get("monthScore"));
                        } else {
                            row2.createCell(1).setCellValue(0.0);
                        }
                        if (map2.containsKey("taskAverageScore") && map2.get("taskAverageScore") != null) {
                            row2.createCell(2).setCellValue((Double) map2.get("taskAverageScore"));
                        } else {
                            row2.createCell(2).setCellValue(0.0);
                        }
                        if (map2.containsKey("multipleMonthScore") && map2.get("multipleMonthScore") != null) {
                            row2.createCell(3).setCellValue((Double) map2.get("multipleMonthScore"));
                        } else {
                            row2.createCell(3).setCellValue(0.0);
                        }
                        row2.createCell(4).setCellValue((Integer) map2.get("workDays"));
                        row2.createCell(5).setCellValue((Integer) map2.get("period"));
                        Double saturation = (Double) map2.get("saturation");
                        if (saturation != null) {
                            row2.createCell(6).setCellValue(format.format(saturation));
                        }
                        row2.createCell(7).setCellValue((Integer) map2.get("createTaskNum"));
                        row2.getCell(0).setCellStyle(style);
                        row2.getCell(1).setCellStyle(style);
                        row2.getCell(2).setCellStyle(style);
                        row2.getCell(3).setCellStyle(style);
                        row2.getCell(4).setCellStyle(style);
                        row2.getCell(5).setCellStyle(style);
                        row2.getCell(6).setCellStyle(style);
                        row2.getCell(7).setCellStyle(style);
                        monthStatistic.set(monthStatistic.get() + 1);
                    }
                }
                AtomicReference<Integer> projectTitle = new AtomicReference<>(5);
                Row projectTitleRow = monthSheet.createRow(4);
                projectTitleRow.createCell(0).setCellValue("月度参与项目统计情况：");
                projectTitleRow.getCell(0).setCellStyle(styleTitle);

                AtomicReference<Integer> projectStatistic = new AtomicReference<>(6);
                Row projectRow = monthSheet.createRow(5);
                List<String> projectTitleList = Arrays.asList("序号", "项目名称","参与任务数", "完成任务数", "未完成任务数", "项目类型", "项目负责人", "项目立项情况", "项目完成情况", "项目开始时间", "项目结束时间");
                for (int i = 0; i < projectTitleList.size(); i++) {
                    projectRow.createCell(i).setCellValue(projectTitleList.get(i));
                    projectRow.getCell(i).setCellStyle(styleTitle);
                }
                Map<String, Object> map2 = new HashMap<>();
                map2.put("startTime", DateUtils.convert2String(queryInfo.getStartTimes(), DateUtils.YYYY_MM_DD_HH_MM_SS));
                map2.put("endTime", DateUtils.convert2String(DateUtils.getMonthEndTime(queryInfo.getStartTimes()), DateUtils.YYYY_MM_DD_HH_MM_SS));
                map2.put("chargepeopleId", userId);
                List<Map<String, Object>> resultList = taskTableService.selectJoinProjectList(map2);
                if (ShiroUtils.isNotEmpty(resultList)) {
                    int i = 1;
                    for (Map<String, Object> map3 : resultList) {
                        Integer projectId = (Integer) map3.get("projectId");
                        map2.put("projectId", projectId);
                        Map<String, Object> taskMap = taskTableService.selectTaskCount(map2);
                        Row row = monthSheet.createRow(projectStatistic.get());
                        row.createCell(0).setCellValue(i);
                        row.createCell(1).setCellValue((String) map3.get("title"));
                        row.createCell(2).setCellValue((Long)taskMap.get("taskCount"));
                        row.createCell(3).setCellValue((Long)taskMap.get("taskFinishCount"));
                        row.createCell(4).setCellValue((Long)taskMap.get("taskStopCount"));
                        Integer projectType = (Integer) map3.get("projectType");
                        if (projectType != null) {
                            if (projectType == 0) {
                                // 科研项目
                                row.createCell(5).setCellValue("科研项目");
                                row.createCell(6).setCellValue((String) map3.get("chargepeopleName"));
                            } else if (projectType == 1) {
                                // 市场项目
                                row.createCell(5).setCellValue("市场项目");
                                row.createCell(6).setCellValue((String) map3.get("techniqueName"));
                            }
                        }
                        row.createCell(7).setCellValue(MyUtils.getProjectEstablishStatusName((String) map3.get("establishStatus")));
                        row.createCell(8).setCellValue(MyUtils.getProjectFinishFlagName((String) map3.get("projectFinishFlag")));
                        Date startTime = (Date) map3.get("startTime");
                        Date endTime = (Date) map3.get("endTime");
                        row.createCell(9).setCellValue(sdf.format(startTime));
                        row.createCell(10).setCellValue(sdf.format(endTime));
                        row.getCell(0).setCellStyle(style);
                        row.getCell(1).setCellStyle(style);
                        row.getCell(2).setCellStyle(style);
                        row.getCell(3).setCellStyle(style);
                        row.getCell(4).setCellStyle(style);
                        row.getCell(5).setCellStyle(style);
                        row.getCell(6).setCellStyle(style);
                        row.getCell(7).setCellStyle(style);
                        row.getCell(8).setCellStyle(style);
                        row.getCell(9).setCellStyle(style);
                        row.getCell(10).setCellStyle(style);
                        ++i;
                        projectStatistic.set(projectStatistic.get() + 1);
                    }
                }
            }
            // 第三张表
            AtomicReference<Integer> taskEcharRowNum = new AtomicReference<>(1);
            List<TaskEchar> data = (List<TaskEchar>) taskEcharService.getTaskEchartsData(queryInfo).get("data");
            if (data != null && data.size() > 0) {
                TaskEchar taskEchar = data.get(0);
                Sheet taskOverSheet = wb.createSheet(userName + "_任务完成情况统计");
                //设置单元格宽度
                taskOverSheet.setDefaultColumnWidth(30);
                Row onenrow = taskOverSheet.createRow(0);
                //创建单元格并设置单元格内容
                List<String> overCellTitles = Arrays.asList("任务状态", "数量", "所占比例");
                List<String> fldNameArr = new ArrayList<>();
                List<Integer> dataList = new ArrayList<>();
                for (int i = 0; i < taskEchar.getStatus().size(); i++) {
                    if (i < overCellTitles.size()) {
                        onenrow.createCell(i).setCellValue(overCellTitles.get(i));
                        onenrow.getCell(i).setCellStyle(styleTitle);
                    }
                    Row row2 = taskOverSheet.createRow(taskEcharRowNum.get());
                    row2.createCell(0).setCellValue(taskEchar.getStatus().get(i));
                    fldNameArr.add(taskEchar.getStatus().get(i));
                    row2.createCell(1).setCellValue(taskEchar.getTaskStatusCount().get(i));
                    dataList.add(taskEchar.getTaskStatusCount().get(i));
                    Double taskStatusRate = Double.valueOf(String.format("%.2f", taskEchar.getTaskStatusRate().get(i)));
                    if (taskStatusRate != null) {
                        row2.createCell(2).setCellValue(taskStatusRate + "%");
                    }
                    row2.getCell(0).setCellStyle(style);
                    row2.getCell(1).setCellStyle(style);
                    row2.getCell(2).setCellStyle(style);
                    taskEcharRowNum.set(taskEcharRowNum.get() + 1);
                }
                // 实现饼图
                sysUserWorksService.buildPieChart(taskOverSheet, fldNameArr, dataList, 0, 3, 9, 22, 1);

                AtomicReference<Integer> taskOverEcharRowNum = new AtomicReference<>(24);
                Row row = taskOverSheet.createRow(23);
                TaskEchar taskOverEchar = data.get(1);
                List<String> taskOverTitle = Arrays.asList("逾期状态", "数量", "比例");
                List<String> fldNameArr2 = new ArrayList<>();
                List<Integer> dataList2 = new ArrayList<>();
                for (int i = 0; i < taskOverEchar.getStatus().size(); i++) {
                    if (i < taskOverTitle.size()) {
                        row.createCell(i).setCellValue(taskOverTitle.get(i));
                        row.getCell(i).setCellStyle(styleTitle);
                    }
                    Row row2 = taskOverSheet.createRow(taskOverEcharRowNum.get());
                    row2.createCell(0).setCellValue(taskOverEchar.getStatus().get(i));
                    fldNameArr2.add(taskOverEchar.getStatus().get(i));
                    row2.createCell(1).setCellValue(taskOverEchar.getTaskStatusCount().get(i));
                    dataList2.add(taskOverEchar.getTaskStatusCount().get(i));
                    Double taskStatusRate = Double.valueOf(String.format("%.2f", taskOverEchar.getTaskStatusRate().get(i)));
                    if (taskStatusRate != null) {
                        row2.createCell(2).setCellValue(taskStatusRate + "%");
                    }
                    row2.getCell(0).setCellStyle(style);
                    row2.getCell(1).setCellStyle(style);
                    row2.getCell(2).setCellStyle(style);
                    taskOverEcharRowNum.set(taskOverEcharRowNum.get() + 1);
                }
                sysUserWorksService.buildPieChart(taskOverSheet, fldNameArr2, dataList2, 0, 3, 29, 41, 24);
            }
            // 最后一张表
            // 建立新的sheet对象（excel的表单）
            Sheet taskSheet = wb.createSheet(userName + "_任务统计");
            // 设置单元格宽度
            taskSheet.setDefaultColumnWidth(30);
            AtomicReference<Integer> taskListTitle = new AtomicReference<>(1);
            Row taskListRow = taskSheet.createRow(0);
            if (month == 13) {
                taskListRow.createCell(0).setCellValue("年度参与任务列表：");
            } else {
                taskListRow.createCell(0).setCellValue("月度参与任务列表：");
            }
            taskListRow.getCell(0).setCellStyle(styleTitle);

            AtomicReference<Integer> rowNum = new AtomicReference<>(2);
            Row taskOnerow = taskSheet.createRow(1);
            // 创建单元格并设置单元格内容
            List<String> cellTitles = Arrays.asList("序号", "任务名称", "任务负责人", "任务进度", "所属项目", "任务状态", "任务评分");
            for (int i = 0; i < cellTitles.size(); i++) {
                taskOnerow.createCell(i).setCellValue(cellTitles.get(i));
                taskOnerow.getCell(i).setCellStyle(styleTitle);
            }
            TaskTable taskTable = new TaskTable();
            taskTable.setPeopleString(queryInfo.getUserId().toString());
            // 今年
            int nowYear = DateUtil.selectYear();
            if (nowYear == year && month == 13) {
                // 若获取的是今年，则从表中获取开始时间和结束时间，且判断是不是当前月
                StringBuilder builder = new StringBuilder();
                builder.append(nowYear).append("-");
                // 表中数据今年第一天:yyyy-MM-dd
                String firstDay = sysYearMonthMapper.selectMinDate(builder.toString());
                // 表中数据今年最后一天
                String lastDay = sysYearMonthMapper.selectMaxDate(builder.toString());
                // 当前月份最后一天
                String currentDay = DateUtil.getCurrentMonthLastDay();
                // 当前月份为最大月份的情况
                if (lastDay.equals(currentDay)) {
                    // 获取上个月的最后一天
                    String lastMonthLastDay = DateUtil.getLastMonthLastDay();
                    if (firstDay.compareTo(lastMonthLastDay) < 0) {
                        lastDay = lastMonthLastDay;
                    }
                }
                firstDay = firstDay + " 00:00:00";
                lastDay = lastDay + " 23:59:59";
                SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                taskTable.setStartTimes(sdf2.parse(firstDay).getTime());
                taskTable.setEndTimes(sdf2.parse(lastDay).getTime());
            } else {
                taskTable.setStartTimes(queryInfo.getStartTimes());
                Long endTime = DateUtils.getMonthEndTime(queryInfo.getStartTimes());
                taskTable.setEndTimes(endTime);
            }
            taskTable.setPageNumber(1);
            taskTable.setTotal(Integer.MAX_VALUE);
            List<TaskTable> taskList = taskTableService.lists(taskTable).getList();
            final int[] i = {1};
            taskList.forEach(item -> {
                Row row2 = taskSheet.createRow(rowNum.get());
                row2.createCell(0).setCellValue(i[0]);
                row2.createCell(1).setCellValue(item.getTaskTitle());
                row2.createCell(2).setCellValue(item.getChargePeopleName());
                row2.createCell(3).setCellValue(item.getScheduleRate() + "%");
                row2.createCell(4).setCellValue(item.getProjectName());
                row2.createCell(5).setCellValue(MyUtils.getTaskFinishFlagName(item.getTaskFinishflag()));
                if (item.getTaskScore() != null) {
                    row2.createCell(6).setCellValue(item.getTaskScore());
                    row2.getCell(6).setCellStyle(style);
                }
                row2.getCell(0).setCellStyle(style);
                row2.getCell(1).setCellStyle(style);
                row2.getCell(2).setCellStyle(style);
                row2.getCell(3).setCellStyle(style);
                row2.getCell(4).setCellStyle(style);
                row2.getCell(5).setCellStyle(style);
                i[0]++;
                rowNum.set(rowNum.get() + 1);
            });
            //输出Excel文件
            OutputStream out = null;
            filename = UUID.randomUUID() + "个人统计" + ".xlsx";
            out = new FileOutputStream(ExcelUtil.getAbsoluteFile(filename));
            wb.write(out);
            wb.close();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success(filename);
    }


    @Log(title = "用户管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "用户数据");
    }

    @Log(title = "用户管理", businessType = BusinessType.IMPORT)
    @RequiresPermissions("system:user:import")
    @PostMapping("/importData")
    @ResponseBody
    public AjaxResult importData(MultipartFile file, boolean updateSupport) throws Exception {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        List<SysUser> userList = util.importExcel(file.getInputStream());
        String operName = ShiroUtils.getSysUser().getLoginName();
        String message = userService.importUser(userList, updateSupport, operName);
        return AjaxResult.success(message);
    }

    @RequiresPermissions("system:user:view")
    @GetMapping("/importTemplate")
    @ResponseBody
    public AjaxResult importTemplate() {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.importTemplateExcel("用户数据");
    }

    /**
     * 新增用户
     */
    @GetMapping("/add")
    @ResponseBody
    public ModelMap add() {
        ModelMap mmap = new ModelMap();
        mmap.put("roles", roleService.selectRoleAll());
        mmap.put("posts", postService.selectPostAll());
        return mmap;
    }

    @Log(title = "用户管理:新增用户", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysUser user, SysUserRole sysUserRole) {
        if (StringUtils.isNotNull(user.getUserId()) && SysUser.isAdmin(user.getUserId())) {
            return error("不允许修改超级管理员用户");
        }
        String uniqueFlag = userService.checkLoginNameUnique(user.getLoginName());
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(uniqueFlag)) {
            return error("登录账号不能相同");
        }
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        //判断登录用户是否是管理员
        Long userId2 = ShiroUtils.getUserId();
        sysUserRole.setUserId(userId2);
        Integer roleId2 = sysRoleService.selectRoleId(sysUserRole);//登录用户id
        Long roleId1 = sysUserRole.getRoleId();
        if (roleId1 == null) {
            roleId1 = 0L;
        }
        //如果是超级管理员，则是可以添加任何的用户
        if (roleId2 == 2) {
            //添加用户
            userService.insertUser(user);
            //给用户添加角色
            sysUserRole.setUserId(user.getUserId());

            sysRoleService.insertUsers(sysUserRole);
        }
        //如果是管理员，则可以添加管理员和普通用户，普通用户不能添加用户
        if (roleId2 == 1) {
            if (roleId1 == 1 || roleId1 == 0) {
                //添加用户
                userService.insertUser(user);
                //给用户添加角色
                sysUserRole.setUserId(user.getUserId());

                sysRoleService.insertUsers(sysUserRole);
            }
        }
        //增加用户头像关联
        if (StringUtils.isNotNull(user.getFileId()) && StringUtils.isNotNull(user.getAvatar())) {
            SysFileInfo sysFileInfo = new SysFileInfo();
            sysFileInfo.setFileId(user.getFileId());
            sysFileInfo.setFileType(2);
            sysFileInfo.setWorkId(user.getUserId().intValue());
            sysFileInfoService.updateFileInfo(sysFileInfo);
        }
        MyUtils.removeMapCache("deptList");
        MyUtils.removeMapCache("userGroupSysDepts");
        return success();
    }

    /**
     * 修改用户
     */
    @GetMapping("/edit/{userId}")
    @ResponseBody
    public ModelMap edit(@PathVariable("userId") Long userId) {
        ModelMap mmap = new ModelMap();
        mmap.put("user", userService.selectUserById(userId));
        mmap.put("roles", roleService.selectRolesByUserId(userId));
        mmap.put("posts", postService.selectPostsByUserId(userId));
        return mmap;
    }

    @Log(title = "用户管理:用户编辑", businessType = BusinessType.UPDATE)
    @PostMapping("/editsave")
    @ResponseBody
    public AjaxResult editSave(@RequestBody SysUser user) {
        Boolean upRole = false;
        MyUtils.removeMapCache("deptList");
        MyUtils.removeMapCache("userGroupSysDepts");
        user.setUpdateBy(ShiroUtils.getUserId() + "");
        if (StringUtils.isNotNull(user.getFileId()) && StringUtils.isNotNull(user.getAvatar())) {
            //删除用户头像关联
            Long[] userIds = Convert.toLongArray(user.getUserId().toString());
            sysFileInfoService.updateFileInfoByUserId(userIds);
            //新增用户头像关联
            SysFileInfo sysFileInfo = new SysFileInfo();
            sysFileInfo.setFileId(user.getFileId());
            sysFileInfo.setFileType(2);
            sysFileInfo.setWorkId(user.getUserId().intValue());
            sysFileInfoService.updateFileInfo(sysFileInfo);
            user.setUpdateBy(ShiroUtils.getUserId() + "");
            user.setUpdateTime(new Date());
            //修改用户信息
            int a = userService.updateUser(user);
            return AjaxResult.success();
        }
        user.setUpdateBy(ShiroUtils.getUserId() + "");
        user.setUpdateTime(new Date());
        int a = userService.updateUser(user);
        //修改用户类型
        SysUserRole sysUserRole = new SysUserRole();
        //如果是管理员，可以直接修改角色
        Long roleId = user.getRoleId();//要修改的角色id
        SysUser sysUser = userService.selectUserById(user.getUserId());
        if (!sysUser.getRoleId().equals(user.getRoleId())) {
            upRole = true;
        }
        if (SysUser.isAdmin(ShiroUtils.getUserId())) {
            upRole = false;
            Long u = user.getUserId();//要修改的用户id
            if (roleId != null && u != null) {
                sysUserRole.setUserId(u);
                sysUserRole.setRoleId(roleId);
                a = sysRoleService.updateUsersRole(sysUserRole);
            }
        }
        Long userId1 = user.getUserId();
        if (userId1 == null) {
            userId1 = 0L;
        }
        sysUserRole.setUserId(userId1);
        Integer roleId1 = sysRoleService.selectRoleId(sysUserRole);//要修改的用户id
        Long userId2 = ShiroUtils.getUserId();
        sysUserRole.setUserId(userId2);
        Integer roleId2 = sysRoleService.selectRoleId(sysUserRole);//登录用户id
        //当登录用户不是超级管理员的情况
        if (!SysUser.isAdmin(userId2)) {
            if (roleId1 != null && roleId2 != null) {
                if (roleId2 == 1) {
                    if (roleId1 == 0) {
                        sysUserRole.setUserId(userId1);
                        sysUserRole.setRoleId(roleId);
                        a = sysRoleService.updateUsersRole(sysUserRole);
                    }
                    if (roleId1 == 1) {
                        return error("无权限修改管理员用户");
                    }
                }
                if (roleId2 == 0) {
                    return error("无权限修改管理员用户");
                }
            }
        }
        //用于给日志传参
        String info = ShiroUtils.getSysUser().getUserName() + " 修改了 " + user.getUserName() + " 的用户信息";
        MyUtils.putMapCache(ShiroUtils.getUserId() + "operLogInfo", user);
        if (upRole) {
            return AjaxResult.success("无权限修改管理员用户");
        }
        return toAjax(a);
    }

    /**
     * 修改用户的角色接口
     *
     * @return
     */
    @PostMapping("/updateUserRole")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateUsersRole(SysUserRole sysUserRole) {
        int a = 0;
        if (SysUser.isAdmin(ShiroUtils.getUserId())) {
            a = sysRoleService.updateUsersRole(sysUserRole);
        }

        long userId1 = sysUserRole.getUserId();
        sysUserRole.setUserId(userId1);
        Integer roleId1 = sysRoleService.selectRoleId(sysUserRole);//要修改的用户id

        long userId2 = ShiroUtils.getUserId();
        sysUserRole.setUserId(userId2);
        Integer roleId2 = sysRoleService.selectRoleId(sysUserRole);//登录用户id
        if (roleId2 == 1) {
            if (roleId1 == 0) {
                a = sysRoleService.updateUsersRole(sysUserRole);
            }
        }
        return toAjax(a);
    }


    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "用户管理:重置密码", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "用户管理:重置密码", businessType = BusinessType.UPDATE)
    @PostMapping("/resetPwdSave")
    @ResponseBody
    public AjaxResult resetPwdSave(SysUser user) {
        SysUser sysUser = userService.selectUserById(user.getUserId());
        String passWord = userService.selectAllInitPassWordById(user);
        user.setSalt(ShiroUtils.randomSalt());
        if (!MyUtils.isEmpty(passWord)) {
            user.setPassword(passWord);
        } else {
            user.setPassword("123456");
        }
        user.setPassword(passwordService.encryptPassword(sysUser.getLoginName(), user.getPassword(), user.getSalt()));
        return toAjax(userService.resetUserPwd(user));
    }

    @Log(title = "用户管理:修改密码", businessType = BusinessType.UPDATE)
    @PostMapping("/modifyPwdSave")
    @ResponseBody
    public AjaxResult modifyPwdSave(SysUser user) {
        SysUser sysUser = userService.selectUserById(user.getUserId());
        String password = passwordService.encryptPassword(sysUser.getLoginName(), user.getPassword(), sysUser.getSalt());
        if (sysUser.getPassword().equals(password)) {
            System.out.println("密码正确");
            //修改密码
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(sysUser.getLoginName(), user.getNewPassword(), user.getSalt()));
            userTokenService.deleteTokenByUserId(ShiroUtils.getUserId());
            return toAjax(userService.resetUserPwd(user));
        } else {
            System.out.println("密码错误");
            return AjaxResult.error("原密码错误");
        }

    }


    @Log(title = "用户管理:删除用户", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        MyUtils.removeMapCache("deptList");
        MyUtils.removeMapCache("userGroupSysDepts");
        try {
            Long[] userIds = Convert.toLongArray(ids);
            for (Long userId : userIds) {
                if (SysUser.isAdmin(userId)) {
                    throw new BusinessException("不允许删除超级管理员用户");
                }
            }
            sysFileInfoService.updateFileInfoByUserId(userIds);
            int a = userService.deleteUserByIds(ids);
            SysUserRole sysUserRole = new SysUserRole();
            //删除角色表中的用户
            for (int i = 0; i < userIds.length; i++) {
                sysUserRole.setUserId(userIds[i]);
                sysRoleService.deleteUserRole(sysUserRole);
            }
            return toAjax(a);
        } catch (Exception e) {
            return error(e.getMessage());
        }
    }

    /**
     * 校验用户名
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysUser user) {
        return userService.checkLoginNameUnique(user.getLoginName());
    }

    /**
     * 校验手机号码
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(SysUser user) {
        return userService.checkPhoneUnique(user);
    }

    /**
     * 校验email邮箱
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(SysUser user) {
        return userService.checkEmailUnique(user);
    }

    /**
     * 用户状态修改
     */
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysUser user) {
        return toAjax(userService.changeStatus(user));
    }

    @ApiOperation("用户名或者手机号模糊查询用户名字")
    @PostMapping("/selectUsers")
    @ResponseBody
    public List<SysUser> selectUsers(SysUser sysUser) {

        //SysUser[] sysUsers = null;
        //获得假期的list
        List<SysHolidays> holidaysList = holidaysService.selectSysHolidaysList(null);//假期类，即国家法定节假日
        List<String> list = new ArrayList<>();
        for (SysHolidays holidays : holidaysList) {
            list.add(holidays.getHolidays());//holidays是一个字符串对象
        }
        String userName = sysUser.getUserName();
        sysUser.setUserName(userName);
        List<SysUser> users = userService.selectUserList(sysUser);

        if (users != null & !users.isEmpty()) {
            for (int j = 0; j < users.size(); j++) {
                Integer userId = users.get(j).getUserId().intValue();
                if (userId == null) {
                    userId = 0;
                }
                List<Integer> p = saturation(userId, list);
                //遍历map,且显示在页面上
                for (int x = 0; x < p.size(); x++) {
                    Integer workDays = p.get(0);
                    Integer period = p.get(1);
                    Integer creatTaskNum = p.get(2);
                    if (workDays != null | period != null | creatTaskNum != null) {
                        double avg = (float) period / (float) workDays;
                        String str = String.format("%.4f", avg);
                        double saturation = Double.valueOf(str.toString());
                        users.get(j).setWorkDays(workDays);
                        users.get(j).setPeriod(period + "");
                        users.get(j).setCreatTaskNum(creatTaskNum);
                        users.get(j).setSaturation(saturation);
                    }
                }
            }
        }
        return users;
    }


    /**
     * @PostMapping("/taskUser")
     * @ResponseBody
     */
    public List<Integer> saturation(int userId, List<String> list) {
        //int userId=119;
        TaskTable taskTable = new TaskTable();
        TaskUser taskUser = new TaskUser();//任务参与人对象
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        TaskTable[] taskTableList = null;
        List<Integer> list1 = new ArrayList<>();
        int creatTaskNum = 0;
        taskTable.setStartTime(DateUtils.parseDate(DateUtils.getfirstday()));
        taskTable.setEndTime(DateUtils.parseDate(DateUtils.getlastday()));
        taskTable.setStartTimes(1L);//临时的时间对象
        taskTable.setEndTimes(1L);

        taskTable.setChargepeopleId((long) userId);//登录用户id
        List<TaskTable> taskTableList1 = taskTableService.selectListByChargeId(taskTable);//获取用户作为负责人
        taskUser.setUserId((long) userId);
        List<TaskUser> listTid1 = taskUserService.selectAllTid(taskUser);//获取用户作为参与人

        for (int i = 0; i < taskTableList1.size(); i++) {
            list1.add(taskTableList1.get(i).gettId().intValue());
        }
        for (int i = 0; i < listTid1.size(); i++) {
            Integer tId = listTid1.get(i).gettId().intValue();
            taskTable.settId(tId.longValue());
            String finishFlag = taskTableService.selectFinishFlagById(taskTable);
            if (finishFlag != null) {
                if ((Integer.parseInt(finishFlag) > -1) && (Integer.parseInt(finishFlag) != 2)) {
                    list1.add(tId);
                }
            }
        }
        taskTableList = new TaskTable[list1.size()];
        for (int i = 0; i < list1.size(); i++) {
            taskTable.settId(list1.get(i).longValue());
            taskTableList[i] = taskTableService.selectTaskByTid1(taskTable);
        }
        int period = 0;
        int p = 0;
        TaskTable[] taskList = removeArrayEmptyTextBackNewArray(taskTableList);
        for (TaskTable taskTable1 : taskList) {
            taskTable.settId(taskTable1.gettId());
            //String createBy = taskTableService.selectCreateId(taskTable);//任务发起人
            List<TaskTable> list3 = taskTableService.selectTaskByTid2(taskTable);
            String startTime = DateUtils.getfirstday(), endTime = DateUtils.getlastday();
            //如果结束时间小于等于开始时间，
            if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getEndTime())) <= DateUtils.getTimelong(DateUtils.getlastday())) {
                endTime = DateUtils.dateToString(taskTable1.getEndTime());
            }
            if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getStartTime())) >= DateUtils.getTimelong(DateUtils.getfirstday())) {
                startTime = DateUtils.dateToString(taskTable1.getStartTime());
            }
            try {
                //去除周末节假日工作小时,上班时间到下班时间，传入一个节假日的list,除以每天工作多少小时
                period += (int) (CalculateHours.calculateHour(startTime + " 8:00:00", endTime + " 17:30:00", list) / 7.5);

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        int workday = 0;
        try {
            workday = (int) (CalculateHours.calculateHour(DateUtils.getfirstday() + " 8:00:00", DateUtils.getlastday() + " 17:30:00", list) / 7.5);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        taskTable.setCreateBy(userId + "");
        List<TaskTable> createByList = taskTableService.selectCreateBy(taskTable);
        for (int i = 0; i < createByList.size(); i++) {
            auditFlowCurrent.setApplyId(createByList.get(i).gettId().intValue());
            Integer currentState = auditFlowCurrentService.selectCurrentState(auditFlowCurrent);
            if (currentState == null) {
                currentState = 0;
            }
            if (currentState != 3) {
                if (DateUtils.getTimelong(DateUtils.dateToString(createByList.get(i).getStartTime())) >= DateUtils.getTimelong(DateUtils.getfirstday())) {
                    if (DateUtils.getTimelong(DateUtils.dateToString(createByList.get(i).getStartTime())) <= DateUtils.getTimelong(DateUtils.getlastday())) {
                        creatTaskNum++;
                    }
                }
            }
        }
        List<Integer> listResult = new ArrayList<>();
        listResult.add(workday);
        listResult.add(period);
        listResult.add(creatTaskNum);

        return listResult;
    }

    private static TaskTable[] removeArrayEmptyTextBackNewArray(TaskTable[] strArray) {
        List<TaskTable> strList = Arrays.asList(strArray);
        List<TaskTable> strListNew = new ArrayList<>();
        for (int i = 0; i < strList.size(); i++) {
            if (ShiroUtils.isNotEmpty(strList)) {
                strListNew.add(strList.get(i));
            }
        }
        TaskTable[] strNewArray = strListNew.toArray(new TaskTable[strListNew.size()]);
        return strNewArray;
    }


    @ApiOperation("获取部门的所有产品经理")
    @PostMapping("/selectManagerId")
    @ResponseBody
    public List<SysUser> selectmanagerId(SysUser sysUser) {
        sysUser.setPostId(9L);
        return userService.selectmanagerId(sysUser);
    }

}
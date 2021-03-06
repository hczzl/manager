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
 * @description ????????????
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
            List<AuditFlowNodeRole> auditFlowNodeRoleList = auditFlowNodeRoleService.selectFlowNodeRoleList(auditFlowNodeRole);//???????????????????????????????????????
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
            //??????????????????????????????????????????
            user.setStatus("0");
        }
        List<SysUser> resultList = userService.selectUserList(user);

        return resultList;
    }

    @ApiOperation("?????????????????????????????????")
    @RequestMapping(value = "/statistics", method = {RequestMethod.POST})
    @ResponseBody
    public List<SysUser> statisticsList(SysUser user) {
        if (user.getSearchFlowId() != null && !"".equals(user.getSearchFlowId())) {
            AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
            auditFlowNodeRole.setSearchFlowId(user.getSearchFlowId());
            List<AuditFlowNodeRole> auditFlowNodeRoleList = auditFlowNodeRoleService.selectFlowNodeRoleList(auditFlowNodeRole);//???????????????????????????????????????
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


    @Log(title = "????????????", businessType = BusinessType.EXPORT)
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
            return util.exportExcel(list, user.getYear() + "???" + user.getMonth() + "???" + "??????????????????");
        } else {
            List<SysUserStatisticsByYear> list = JSON.parseArray(JSON.toJSONString(statisticsList(user)), SysUserStatisticsByYear.class);
            ExcelUtil<SysUserStatisticsByYear> util = new ExcelUtil<SysUserStatisticsByYear>(SysUserStatisticsByYear.class);
            return util.exportExcel(list, user.getYear() + "??????????????????");
        }
    }

    @Log(title = "??????????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/statisticsExportByUser")
    @ResponseBody
    public AjaxResult statisticsExportByUser(TaskEcharQueryInfo queryInfo, HttpServletResponse response) {
        NumberFormat format = NumberFormat.getPercentInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String filename = "";
        try {
            // ??????????????????
            Workbook wb = new XSSFWorkbook();
            CellStyle style = wb.createCellStyle();
            // ????????????
            style.setAlignment(HorizontalAlignment.CENTER);
            // ????????????
            style.setVerticalAlignment(VerticalAlignment.CENTER);
            // ??????????????????
            CellStyle styleTitle = wb.createCellStyle();
            Font font = wb.createFont();
            font.setFontName("??????");
            // font.setColor((short) 5);
            // ????????????
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
                return AjaxResult.error("??????????????????????????????!");
            }
            if (month == 13) {
                Sheet yearSheet = wb.createSheet(userName + "_????????????????????????");
                yearSheet.setDefaultColumnWidth(20);
                AtomicReference<Integer> yearTitle = new AtomicReference<>(1);
                Row yearTitleRow = yearSheet.createRow(0);
                yearTitleRow.createCell(0).setCellValue("???????????????????????????");
                yearTitleRow.getCell(0).setCellStyle(styleTitle);
                // ????????????????????????????????????????????????
                AtomicReference<Integer> yearStatistic = new AtomicReference<>(2);
                Row yearFirstRow = yearSheet.createRow(1);
                List<String> titleList = Arrays.asList("??????(???)", "???????????????", "?????????????????????", "???????????????", "???????????????", "??????????????????", "???????????????", "??????????????????");
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
                monthTitleRow.createCell(0).setCellValue("???????????????????????????");
                monthTitleRow.getCell(0).setCellStyle(styleTitle);
                AtomicReference<Integer> monthStatistic = new AtomicReference<>(6);
                Row monthFirstRow = yearSheet.createRow(5);
                List<String> monthTitleList = Arrays.asList("??????(???)", "??????????????????", "???????????????", "??????????????????", "??????????????????", "????????????????????????", "???????????????", "?????????????????????");
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
                // ????????????-?????????????????????????????????
                Sheet projectSheet = wb.createSheet(userName + "_??????????????????????????????");
                projectSheet.setDefaultColumnWidth(30);
                AtomicReference<Integer> projectTitle = new AtomicReference<>(1);
                Row projectTitleRow = projectSheet.createRow(0);
                projectTitleRow.createCell(0).setCellValue("?????????????????????????????????");
                projectTitleRow.getCell(0).setCellStyle(styleTitle);
                AtomicReference<Integer> projectStatistic = new AtomicReference<>(2);
                Row projectRow = projectSheet.createRow(1);
                List<String> projectTitleList = Arrays.asList("??????", "????????????", "???????????????", "???????????????", "??????????????????", "????????????", "???????????????", "??????????????????", "??????????????????", "??????????????????", "??????????????????");
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
                    // ???????????????????????????????????????????????????????????????????????????????????????????????????
                    StringBuilder builder = new StringBuilder();
                    builder.append(nowYear).append("-");
                    // ???????????????????????????:yyyy-MM-dd
                    String firstDay = sysYearMonthMapper.selectMinDate(builder.toString());
                    // ??????????????????????????????
                    String lastDay = sysYearMonthMapper.selectMaxDate(builder.toString());
                    // ????????????????????????
                    String currentDay = DateUtil.getCurrentMonthLastDay();
                    // ????????????????????????????????????
                    if (lastDay.equals(currentDay)) {
                        // ??????????????????????????????
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
                                // ????????????
                                row.createCell(5).setCellValue("????????????");
                                row.createCell(6).setCellValue((String) map3.get("chargepeopleName"));
                            } else if (projectType == 1) {
                                // ????????????
                                row.createCell(5).setCellValue("????????????");
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
                Sheet monthSheet = wb.createSheet(userName + "_" + month + "???????????????????????????????????????");
                monthSheet.setDefaultColumnWidth(20);
                // ??????????????????
                AtomicReference<Integer> monthTitle = new AtomicReference<>(1);
                Row monthTitleRow = monthSheet.createRow(0);
                monthTitleRow.createCell(0).setCellValue("???????????????????????????");
                monthTitleRow.getCell(0).setCellStyle(styleTitle);
                AtomicReference<Integer> monthStatistic = new AtomicReference<>(2);
                Row monthFirstRow = monthSheet.createRow(1);
                List<String> monthTitleList = Arrays.asList("??????(???)", "????????????", "???????????????", "??????????????????", "??????????????????", "????????????????????????", "???????????????", "?????????????????????");
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
                projectTitleRow.createCell(0).setCellValue("?????????????????????????????????");
                projectTitleRow.getCell(0).setCellStyle(styleTitle);

                AtomicReference<Integer> projectStatistic = new AtomicReference<>(6);
                Row projectRow = monthSheet.createRow(5);
                List<String> projectTitleList = Arrays.asList("??????", "????????????","???????????????", "???????????????", "??????????????????", "????????????", "???????????????", "??????????????????", "??????????????????", "??????????????????", "??????????????????");
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
                                // ????????????
                                row.createCell(5).setCellValue("????????????");
                                row.createCell(6).setCellValue((String) map3.get("chargepeopleName"));
                            } else if (projectType == 1) {
                                // ????????????
                                row.createCell(5).setCellValue("????????????");
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
            // ????????????
            AtomicReference<Integer> taskEcharRowNum = new AtomicReference<>(1);
            List<TaskEchar> data = (List<TaskEchar>) taskEcharService.getTaskEchartsData(queryInfo).get("data");
            if (data != null && data.size() > 0) {
                TaskEchar taskEchar = data.get(0);
                Sheet taskOverSheet = wb.createSheet(userName + "_????????????????????????");
                //?????????????????????
                taskOverSheet.setDefaultColumnWidth(30);
                Row onenrow = taskOverSheet.createRow(0);
                //???????????????????????????????????????
                List<String> overCellTitles = Arrays.asList("????????????", "??????", "????????????");
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
                // ????????????
                sysUserWorksService.buildPieChart(taskOverSheet, fldNameArr, dataList, 0, 3, 9, 22, 1);

                AtomicReference<Integer> taskOverEcharRowNum = new AtomicReference<>(24);
                Row row = taskOverSheet.createRow(23);
                TaskEchar taskOverEchar = data.get(1);
                List<String> taskOverTitle = Arrays.asList("????????????", "??????", "??????");
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
            // ???????????????
            // ????????????sheet?????????excel????????????
            Sheet taskSheet = wb.createSheet(userName + "_????????????");
            // ?????????????????????
            taskSheet.setDefaultColumnWidth(30);
            AtomicReference<Integer> taskListTitle = new AtomicReference<>(1);
            Row taskListRow = taskSheet.createRow(0);
            if (month == 13) {
                taskListRow.createCell(0).setCellValue("???????????????????????????");
            } else {
                taskListRow.createCell(0).setCellValue("???????????????????????????");
            }
            taskListRow.getCell(0).setCellStyle(styleTitle);

            AtomicReference<Integer> rowNum = new AtomicReference<>(2);
            Row taskOnerow = taskSheet.createRow(1);
            // ???????????????????????????????????????
            List<String> cellTitles = Arrays.asList("??????", "????????????", "???????????????", "????????????", "????????????", "????????????", "????????????");
            for (int i = 0; i < cellTitles.size(); i++) {
                taskOnerow.createCell(i).setCellValue(cellTitles.get(i));
                taskOnerow.getCell(i).setCellStyle(styleTitle);
            }
            TaskTable taskTable = new TaskTable();
            taskTable.setPeopleString(queryInfo.getUserId().toString());
            // ??????
            int nowYear = DateUtil.selectYear();
            if (nowYear == year && month == 13) {
                // ???????????????????????????????????????????????????????????????????????????????????????????????????
                StringBuilder builder = new StringBuilder();
                builder.append(nowYear).append("-");
                // ???????????????????????????:yyyy-MM-dd
                String firstDay = sysYearMonthMapper.selectMinDate(builder.toString());
                // ??????????????????????????????
                String lastDay = sysYearMonthMapper.selectMaxDate(builder.toString());
                // ????????????????????????
                String currentDay = DateUtil.getCurrentMonthLastDay();
                // ????????????????????????????????????
                if (lastDay.equals(currentDay)) {
                    // ??????????????????????????????
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
            //??????Excel??????
            OutputStream out = null;
            filename = UUID.randomUUID() + "????????????" + ".xlsx";
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


    @Log(title = "????????????", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysUser user) {
        List<SysUser> list = userService.selectUserList(user);
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        return util.exportExcel(list, "????????????");
    }

    @Log(title = "????????????", businessType = BusinessType.IMPORT)
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
        return util.importTemplateExcel("????????????");
    }

    /**
     * ????????????
     */
    @GetMapping("/add")
    @ResponseBody
    public ModelMap add() {
        ModelMap mmap = new ModelMap();
        mmap.put("roles", roleService.selectRoleAll());
        mmap.put("posts", postService.selectPostAll());
        return mmap;
    }

    @Log(title = "????????????:????????????", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysUser user, SysUserRole sysUserRole) {
        if (StringUtils.isNotNull(user.getUserId()) && SysUser.isAdmin(user.getUserId())) {
            return error("????????????????????????????????????");
        }
        String uniqueFlag = userService.checkLoginNameUnique(user.getLoginName());
        if (UserConstants.USER_NAME_NOT_UNIQUE.equals(uniqueFlag)) {
            return error("????????????????????????");
        }
        user.setSalt(ShiroUtils.randomSalt());
        user.setPassword(passwordService.encryptPassword(user.getLoginName(), user.getPassword(), user.getSalt()));
        user.setCreateBy(ShiroUtils.getLoginName());
        //????????????????????????????????????
        Long userId2 = ShiroUtils.getUserId();
        sysUserRole.setUserId(userId2);
        Integer roleId2 = sysRoleService.selectRoleId(sysUserRole);//????????????id
        Long roleId1 = sysUserRole.getRoleId();
        if (roleId1 == null) {
            roleId1 = 0L;
        }
        //????????????????????????????????????????????????????????????
        if (roleId2 == 2) {
            //????????????
            userService.insertUser(user);
            //?????????????????????
            sysUserRole.setUserId(user.getUserId());

            sysRoleService.insertUsers(sysUserRole);
        }
        //?????????????????????????????????????????????????????????????????????????????????????????????
        if (roleId2 == 1) {
            if (roleId1 == 1 || roleId1 == 0) {
                //????????????
                userService.insertUser(user);
                //?????????????????????
                sysUserRole.setUserId(user.getUserId());

                sysRoleService.insertUsers(sysUserRole);
            }
        }
        //????????????????????????
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
     * ????????????
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

    @Log(title = "????????????:????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/editsave")
    @ResponseBody
    public AjaxResult editSave(@RequestBody SysUser user) {
        Boolean upRole = false;
        MyUtils.removeMapCache("deptList");
        MyUtils.removeMapCache("userGroupSysDepts");
        user.setUpdateBy(ShiroUtils.getUserId() + "");
        if (StringUtils.isNotNull(user.getFileId()) && StringUtils.isNotNull(user.getAvatar())) {
            //????????????????????????
            Long[] userIds = Convert.toLongArray(user.getUserId().toString());
            sysFileInfoService.updateFileInfoByUserId(userIds);
            //????????????????????????
            SysFileInfo sysFileInfo = new SysFileInfo();
            sysFileInfo.setFileId(user.getFileId());
            sysFileInfo.setFileType(2);
            sysFileInfo.setWorkId(user.getUserId().intValue());
            sysFileInfoService.updateFileInfo(sysFileInfo);
            user.setUpdateBy(ShiroUtils.getUserId() + "");
            user.setUpdateTime(new Date());
            //??????????????????
            int a = userService.updateUser(user);
            return AjaxResult.success();
        }
        user.setUpdateBy(ShiroUtils.getUserId() + "");
        user.setUpdateTime(new Date());
        int a = userService.updateUser(user);
        //??????????????????
        SysUserRole sysUserRole = new SysUserRole();
        //?????????????????????????????????????????????
        Long roleId = user.getRoleId();//??????????????????id
        SysUser sysUser = userService.selectUserById(user.getUserId());
        if (!sysUser.getRoleId().equals(user.getRoleId())) {
            upRole = true;
        }
        if (SysUser.isAdmin(ShiroUtils.getUserId())) {
            upRole = false;
            Long u = user.getUserId();//??????????????????id
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
        Integer roleId1 = sysRoleService.selectRoleId(sysUserRole);//??????????????????id
        Long userId2 = ShiroUtils.getUserId();
        sysUserRole.setUserId(userId2);
        Integer roleId2 = sysRoleService.selectRoleId(sysUserRole);//????????????id
        //?????????????????????????????????????????????
        if (!SysUser.isAdmin(userId2)) {
            if (roleId1 != null && roleId2 != null) {
                if (roleId2 == 1) {
                    if (roleId1 == 0) {
                        sysUserRole.setUserId(userId1);
                        sysUserRole.setRoleId(roleId);
                        a = sysRoleService.updateUsersRole(sysUserRole);
                    }
                    if (roleId1 == 1) {
                        return error("??????????????????????????????");
                    }
                }
                if (roleId2 == 0) {
                    return error("??????????????????????????????");
                }
            }
        }
        //?????????????????????
        String info = ShiroUtils.getSysUser().getUserName() + " ????????? " + user.getUserName() + " ???????????????";
        MyUtils.putMapCache(ShiroUtils.getUserId() + "operLogInfo", user);
        if (upRole) {
            return AjaxResult.success("??????????????????????????????");
        }
        return toAjax(a);
    }

    /**
     * ???????????????????????????
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
        Integer roleId1 = sysRoleService.selectRoleId(sysUserRole);//??????????????????id

        long userId2 = ShiroUtils.getUserId();
        sysUserRole.setUserId(userId2);
        Integer roleId2 = sysRoleService.selectRoleId(sysUserRole);//????????????id
        if (roleId2 == 1) {
            if (roleId1 == 0) {
                a = sysRoleService.updateUsersRole(sysUserRole);
            }
        }
        return toAjax(a);
    }


    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "????????????:????????????", businessType = BusinessType.UPDATE)
    @GetMapping("/resetPwd/{userId}")
    public String resetPwd(@PathVariable("userId") Long userId, ModelMap mmap) {
        mmap.put("user", userService.selectUserById(userId));
        return prefix + "/resetPwd";
    }

    @RequiresPermissions("system:user:resetPwd")
    @Log(title = "????????????:????????????", businessType = BusinessType.UPDATE)
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

    @Log(title = "????????????:????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/modifyPwdSave")
    @ResponseBody
    public AjaxResult modifyPwdSave(SysUser user) {
        SysUser sysUser = userService.selectUserById(user.getUserId());
        String password = passwordService.encryptPassword(sysUser.getLoginName(), user.getPassword(), sysUser.getSalt());
        if (sysUser.getPassword().equals(password)) {
            System.out.println("????????????");
            //????????????
            user.setSalt(ShiroUtils.randomSalt());
            user.setPassword(passwordService.encryptPassword(sysUser.getLoginName(), user.getNewPassword(), user.getSalt()));
            userTokenService.deleteTokenByUserId(ShiroUtils.getUserId());
            return toAjax(userService.resetUserPwd(user));
        } else {
            System.out.println("????????????");
            return AjaxResult.error("???????????????");
        }

    }


    @Log(title = "????????????:????????????", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        MyUtils.removeMapCache("deptList");
        MyUtils.removeMapCache("userGroupSysDepts");
        try {
            Long[] userIds = Convert.toLongArray(ids);
            for (Long userId : userIds) {
                if (SysUser.isAdmin(userId)) {
                    throw new BusinessException("????????????????????????????????????");
                }
            }
            sysFileInfoService.updateFileInfoByUserId(userIds);
            int a = userService.deleteUserByIds(ids);
            SysUserRole sysUserRole = new SysUserRole();
            //???????????????????????????
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
     * ???????????????
     */
    @PostMapping("/checkLoginNameUnique")
    @ResponseBody
    public String checkLoginNameUnique(SysUser user) {
        return userService.checkLoginNameUnique(user.getLoginName());
    }

    /**
     * ??????????????????
     */
    @PostMapping("/checkPhoneUnique")
    @ResponseBody
    public String checkPhoneUnique(SysUser user) {
        return userService.checkPhoneUnique(user);
    }

    /**
     * ??????email??????
     */
    @PostMapping("/checkEmailUnique")
    @ResponseBody
    public String checkEmailUnique(SysUser user) {
        return userService.checkEmailUnique(user);
    }

    /**
     * ??????????????????
     */
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:user:edit")
    @PostMapping("/changeStatus")
    @ResponseBody
    public AjaxResult changeStatus(SysUser user) {
        return toAjax(userService.changeStatus(user));
    }

    @ApiOperation("????????????????????????????????????????????????")
    @PostMapping("/selectUsers")
    @ResponseBody
    public List<SysUser> selectUsers(SysUser sysUser) {

        //SysUser[] sysUsers = null;
        //???????????????list
        List<SysHolidays> holidaysList = holidaysService.selectSysHolidaysList(null);//????????????????????????????????????
        List<String> list = new ArrayList<>();
        for (SysHolidays holidays : holidaysList) {
            list.add(holidays.getHolidays());//holidays????????????????????????
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
                //??????map,?????????????????????
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
        TaskUser taskUser = new TaskUser();//?????????????????????
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        TaskTable[] taskTableList = null;
        List<Integer> list1 = new ArrayList<>();
        int creatTaskNum = 0;
        taskTable.setStartTime(DateUtils.parseDate(DateUtils.getfirstday()));
        taskTable.setEndTime(DateUtils.parseDate(DateUtils.getlastday()));
        taskTable.setStartTimes(1L);//?????????????????????
        taskTable.setEndTimes(1L);

        taskTable.setChargepeopleId((long) userId);//????????????id
        List<TaskTable> taskTableList1 = taskTableService.selectListByChargeId(taskTable);//???????????????????????????
        taskUser.setUserId((long) userId);
        List<TaskUser> listTid1 = taskUserService.selectAllTid(taskUser);//???????????????????????????

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
            //String createBy = taskTableService.selectCreateId(taskTable);//???????????????
            List<TaskTable> list3 = taskTableService.selectTaskByTid2(taskTable);
            String startTime = DateUtils.getfirstday(), endTime = DateUtils.getlastday();
            //?????????????????????????????????????????????
            if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getEndTime())) <= DateUtils.getTimelong(DateUtils.getlastday())) {
                endTime = DateUtils.dateToString(taskTable1.getEndTime());
            }
            if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getStartTime())) >= DateUtils.getTimelong(DateUtils.getfirstday())) {
                startTime = DateUtils.dateToString(taskTable1.getStartTime());
            }
            try {
                //?????????????????????????????????,??????????????????????????????????????????????????????list,??????????????????????????????
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


    @ApiOperation("?????????????????????????????????")
    @PostMapping("/selectManagerId")
    @ResponseBody
    public List<SysUser> selectmanagerId(SysUser sysUser) {
        sysUser.setPostId(9L);
        return userService.selectmanagerId(sysUser);
    }

}
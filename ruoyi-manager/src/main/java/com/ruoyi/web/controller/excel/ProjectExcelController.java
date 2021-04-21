package com.ruoyi.web.controller.excel;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.service.audit.AuditFlowNodeRoleService;
import com.ruoyi.web.service.audit.AuditFlowOperRecordService;
import com.ruoyi.web.service.item.*;
import io.swagger.annotations.*;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @Author SuQZ
 * @Date 2019/12/3 8:49
 * @Version 1.0
 * 市场项目EXCEL相关操作
 */
@Api(value = "市场项目EXCEL相关操作", tags = "市场项目EXCEL相关操作")
@RestController
public class ProjectExcelController {
    @Autowired
    private ProjectPlanService projectPlanService;

    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private SysProjectTableService sysProjectTableService;
    @Autowired
    private ProjectMarkStageService projectMarkStageService;
    @Autowired
    private ProjectMarkTypeService projectMarkTypeService;
    @Autowired
    private AuditFlowOperRecordService auditFlowOperRecordService;
    @Autowired
    private AuditFlowNodeRoleService auditFlowNodeRoleService;
    @Autowired
    private SysCompetitorService sysCompetitorService;


    @ApiOperation("导出市场项目EXCEL")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pid", value = "当前市场项目的ID", type = "Integer", paramType = "query", required = true),
    })
    @PostMapping(value = "/outPutMarkExcel")
    public AjaxResult outPutMarkExcel(HttpServletResponse response, int pid) {
        try {
            //获取任务审批人
            List<Integer> userIdByProjectIds = auditFlowNodeRoleService.getUserIdByProjectId(pid);
            //获取审批执行表
            List<AuditFlowOperRecord> auditFlowOperRecords = auditFlowOperRecordService.selectAuditOperRecordByProjectId(pid);
            Integer rowNum = 0;
            //根据Id获取项目信息
            SysProjectTable sysProjectTable = new SysProjectTable();
            sysProjectTable.setpId(pid);
            List<SysProjectTable> projectTableList = sysProjectTableService.selectAllInfo(sysProjectTable);
            SysProjectTable projectInfo = projectTableList.get(0);
            if (projectInfo == null) {
                System.out.println("当前项目id不存在");
                return AjaxResult.error("当前项目id不存在");
            }
            if (projectInfo.getProjectType() != 1) {
                System.out.println("当前项目不是市场项目");
                return AjaxResult.error("当前项目不是市场项目");
            }
            //根据id查询设备信息
            List<ProjectMarketStageTable> shebeiInfo = projectMarkStageService.selectById(pid);
            //根据id获取试用信息
            ProjectMarkTypeInfoTable projectMarkTypeInfoTable = new ProjectMarkTypeInfoTable();
            projectMarkTypeInfoTable.setProjectId(pid);
            List<ProjectMarkTypeInfoTable> shiyongInfo = projectMarkTypeService.selectAll(projectMarkTypeInfoTable);

            SimpleDateFormat format = new SimpleDateFormat("yyyy.MM.dd");
            //创建HSSFWorkbook对象(excel的文档对象)
            HSSFWorkbook wb = new HSSFWorkbook();
            CellStyle headStyle = wb.createCellStyle();
            CellStyle noticeStyle = wb.createCellStyle();
            CellStyle noticeTitleStyle = wb.createCellStyle();
            CellStyle titleStyle = wb.createCellStyle();
            CellStyle style = wb.createCellStyle();
            headStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
            headStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中

            style.setAlignment(HorizontalAlignment.CENTER);// 左右居中
            style.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
            style.setWrapText(true);

            titleStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
            titleStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
            titleStyle.setWrapText(true);

            noticeStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
            noticeStyle.setWrapText(true);

            noticeTitleStyle.setAlignment(HorizontalAlignment.CENTER);// 左右居中
            noticeTitleStyle.setVerticalAlignment(VerticalAlignment.CENTER);// 上下居中
            noticeTitleStyle.setWrapText(true);

            //建立新的sheet对象（excel的表单）
            HSSFSheet sheet = wb.createSheet("市场立项表");
            HSSFRow row1 = sheet.createRow(rowNum);
            //第一行文字描述
            row1.createCell(0).setCellValue("项目立项表");
            HSSFFont headFont = wb.createFont();
            //字体大小
            headFont.setFontHeightInPoints((short) 18);
            //文字加粗
            headFont.setBold(true);
            //设置字
            headStyle.setFont(headFont);

            HSSFFont titleFont = wb.createFont();
            //文字加粗
            titleFont.setBold(true);
            //设置字
            titleStyle.setFont(titleFont);

            //风险措施字体
            HSSFFont noticeFont = wb.createFont();
            //设置红色
            noticeFont.setColor(Font.COLOR_RED);
            //设置字
            noticeStyle.setFont(noticeFont);

            //风险措施字体
            HSSFFont noticeTitleFont = wb.createFont();
            //设置红色
            noticeTitleFont.setColor(Font.COLOR_RED);
            //加粗
            noticeTitleFont.setBold(true);
            //设置字
            noticeTitleStyle.setFont(noticeTitleFont);

            row1.getCell(0).setCellStyle(headStyle);
            //第一行标题合并单元格
            sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, 15));

            style.setBorderTop(BorderStyle.MEDIUM);
            style.setBorderBottom(BorderStyle.MEDIUM);
            style.setBorderLeft(BorderStyle.MEDIUM);
            style.setBorderRight(BorderStyle.MEDIUM);
            titleStyle.setBorderTop(BorderStyle.MEDIUM);
            titleStyle.setBorderBottom(BorderStyle.MEDIUM);
            titleStyle.setBorderLeft(BorderStyle.MEDIUM);
            titleStyle.setBorderRight(BorderStyle.MEDIUM);
            noticeStyle.setBorderTop(BorderStyle.MEDIUM);
            noticeStyle.setBorderBottom(BorderStyle.MEDIUM);
            noticeStyle.setBorderLeft(BorderStyle.MEDIUM);
            noticeStyle.setBorderRight(BorderStyle.MEDIUM);
            noticeTitleStyle.setBorderTop(BorderStyle.MEDIUM);
            noticeTitleStyle.setBorderBottom(BorderStyle.MEDIUM);
            noticeTitleStyle.setBorderLeft(BorderStyle.MEDIUM);
            noticeTitleStyle.setBorderRight(BorderStyle.MEDIUM);
            HSSFFont font = wb.createFont();
            font.setFontHeightInPoints((short) 11);
            style.setFont(font);

            //第二行
            HSSFRow row2 = sheet.createRow(++rowNum);
            //设置第二行样式
            System.out.println(row2.getLastCellNum());
            for (int i = 0; i <= 15; i++) {
                row2.createCell(i);
                row2.getCell(i).setCellStyle(style);
            }
            row2.getCell(0).setCellStyle(titleStyle);
            row2.getCell(0).setCellValue("商务负责人:");
            //商务负责人姓名
            row2.getCell(2).setCellValue(sysUserService.getName(projectInfo.getChargepeopleId()));
            //技术负责人
            row2.getCell(4).setCellStyle(titleStyle);
            row2.getCell(4).setCellValue("技术负责人:");
            row2.getCell(6).setCellValue(sysUserService.getName(projectInfo.getTechniquePeople()));
            row2.getCell(8).setCellStyle(titleStyle);
            row2.getCell(8).setCellValue("运作周期:");
            if (projectInfo.getPeriod() != null) {
                row2.getCell(10).setCellValue(projectInfo.getPeriod() + "天");
            }
            row2.getCell(12).setCellStyle(titleStyle);
            row2.getCell(12).setCellValue("启动时间:");
            row2.getCell(14).setCellValue((projectInfo.getStartTime() != null && !"".equals(projectInfo.getStartTime())) ? format.format(projectInfo.getStartTime()) : "");

            //第三行
            HSSFRow row3 = sheet.createRow(++rowNum);
            row3.setHeightInPoints(76);
            for (int i = 0; i <= 15; i++) {
                row3.createCell(i);
                row3.getCell(i).setCellStyle(style);
            }
            row3.getCell(0).setCellStyle(titleStyle);
            row3.getCell(0).setCellValue("商务人员填写");
            row3.getCell(1).setCellStyle(titleStyle);
            row3.getCell(1).setCellValue("项目基本信息");
            row3.getCell(2).setCellStyle(titleStyle);
            row3.getCell(2).setCellValue("项目名称");
            row3.getCell(3).setCellStyle(titleStyle);
            row3.getCell(3).setCellValue("客户名称");
            row3.getCell(4).setCellStyle(titleStyle);
            row3.getCell(6).setCellStyle(titleStyle);
            row3.getCell(8).setCellStyle(titleStyle);
            row3.getCell(10).setCellStyle(titleStyle);
            row3.getCell(12).setCellStyle(titleStyle);
            if (projectInfo.getCustomerIsEstablish() != null && projectInfo.getCustomerIsEstablish() == 1) {
                row3.getCell(4).setCellValue("立项√（是打√，填写建设周期；否，填写预计立项时间及预计建设周期）");
            } else {
                row3.getCell(4).setCellValue("立项□（是打√，填写建设周期；否，填写预计立项时间）");
            }
            if (projectInfo.getCustomerIsEstablish() != null && projectInfo.getCustomerIsBudget() == 1) {
                row3.getCell(6).setCellValue("预算√（有打√，并填写金额（万元），无，填写预算时间及金额（万元））");

            } else {
                row3.getCell(6).setCellValue("预算□（有打√，并填写金额（万元），无，填写预算时间及金额（万元））");
            }
            if (projectInfo.getCustomerIsEstablish() != null && projectInfo.getIsDianzi() == 1) {
                row3.getCell(8).setCellValue("垫资√（需要打√，并填写周期及金额）");
            } else {
                row3.getCell(8).setCellValue("垫资□（需要打√，并填写周期及金额）");
            }
            row3.getCell(10).setCellValue("项目类型（对应类型在□打√）");
            if (projectInfo.getIsShiyong() != null && projectInfo.getIsShiyong() == 1) {
                row3.getCell(12).setCellValue("试用√（需要打√）");
                if (projectInfo.getShiyongPeriod() != null) {
                    row3.getCell(15).setCellValue(projectInfo.getShiyongPeriod() + "(天)");
                }
            } else {
                row3.getCell(12).setCellValue("试用□（需要打√）");
            }
            row3.getCell(14).setCellValue("试用周期:");
            row3.getCell(14).setCellStyle(titleStyle);
            row3.getCell(15).setCellStyle(titleStyle);

            //第四行
            HSSFRow row4 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row4.createCell(i);
                row4.getCell(i).setCellStyle(style);
            }
            //项目名称
            if (projectInfo.getTitle() != null) {
                row4.getCell(2).setCellValue(projectInfo.getTitle());
            }
            //客户名称
            if (projectInfo.getDescribeProject() != null) {
                row4.getCell(3).setCellValue(projectInfo.getDescribeProject());
            }
            row4.getCell(4).setCellStyle(titleStyle);
            row4.getCell(4).setCellValue("建设周期/预计时间");
            //周期
            if (projectInfo.getCustomerIsEstablish() != null && projectInfo.getCustomerIsEstablish() == 1 && projectInfo.getBuildPeriod() != null) {
                row4.getCell(5).setCellValue(projectInfo.getBuildPeriod() + "天");
            }
            //预计时间
            if (projectInfo.getCustomerIsEstablish() != null && projectInfo.getCustomerIsEstablish() == 0 && projectInfo.getExpectTime() != null) {
                row4.getCell(5).setCellValue((projectInfo.getExpectTime() != null && !"".equals(projectInfo.getExpectTime())) ? format.format(projectInfo.getExpectTime()) : "");
            }
            row4.getCell(6).setCellStyle(titleStyle);
            row4.getCell(6).setCellValue("（预算时间）");
            //预算时间
            row4.getCell(7).setCellValue((projectInfo.getBudgetTime() != null && !"".equals(projectInfo.getBudgetTime())) ? format.format(projectInfo.getBudgetTime()) : "");
            row4.getCell(8).setCellStyle(titleStyle);
            row4.getCell(8).setCellValue("周期（天）");
            //周期
            if (projectInfo.getDianziPeriod() != null) {
                row4.getCell(9).setCellValue(projectInfo.getDianziPeriod());
            }
            row4.getCell(10).setCellStyle(titleStyle);
            row4.getCell(10).setCellValue("软件" + "\r\n" + "□");
            row4.getCell(12).setCellStyle(titleStyle);
            row4.getCell(12).setCellValue("试用情况");

            //第五行
            HSSFRow row5 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row5.createCell(i);
                row5.getCell(i).setCellStyle(style);
            }
            row5.getCell(4).setCellStyle(titleStyle);
            row5.getCell(4).setCellValue("预计建设周期(天)");
            if (projectInfo.getEstimatedBuildperiod() != null && projectInfo.getEstimatedBuildperiod() != 0 && projectInfo.getExpectTime() != null) {
                row5.getCell(5).setCellValue(projectInfo.getEstimatedBuildperiod());
            }
            row5.getCell(6).setCellStyle(titleStyle);
            row5.getCell(6).setCellValue("金额（万）");
            //预算金额
            if (projectInfo.getBudgetAmount() != null) {
                row5.getCell(7).setCellValue(projectInfo.getBudgetAmount());
            } else if (projectInfo.getBudgettimeAndAmcount() != null) {
                row5.getCell(7).setCellValue(projectInfo.getBudgettimeAndAmcount());
            }
            row5.getCell(8).setCellStyle(titleStyle);
            row5.getCell(8).setCellValue("金额（万）");
            //垫资金额
            if (projectInfo.getDianziMoney() != null) {
                row5.getCell(9).setCellValue(projectInfo.getDianziMoney());
            }
            row5.getCell(10).setCellValue("所需设备");
            row5.getCell(12).setCellValue("总数");
            row5.getCell(14).setCellValue("试用数量");
            //记录是否第一次
            boolean isone = false;
            for (int i = 0; i < shiyongInfo.size(); i++) {
                if (shiyongInfo.get(i).getIsType() != null && shiyongInfo.get(i).getIsType() == 1) {
                    if (!isone) {
                        row4.getCell(10).setCellValue("软件" + "\r\n" + "√");
                        row5.getCell(11).setCellValue(shiyongInfo.get(i).getHardwareName());
                        if (shiyongInfo.get(i).getShiyongAllCount() != null) {
                            row5.getCell(13).setCellValue(shiyongInfo.get(i).getShiyongAllCount());
                        }
                        if (shiyongInfo.get(i).getShiyongCount() != null) {
                            row5.getCell(15).setCellValue(shiyongInfo.get(i).getShiyongCount());
                        }
                        isone = true;
                    } else {
                        HSSFRow newrow = sheet.createRow(++rowNum);
                        for (int j = 0; j <= 15; j++) {
                            newrow.createCell(j);
                            newrow.getCell(j).setCellStyle(style);
                        }
                        newrow.getCell(10).setCellValue("所需设备");
                        if (shiyongInfo.get(i).getHardwareName() != null) {
                            newrow.getCell(11).setCellValue(shiyongInfo.get(i).getHardwareName());
                        }
                        newrow.getCell(12).setCellValue("总数");
                        if (shiyongInfo.get(i).getShiyongAllCount() != null) {
                            newrow.getCell(13).setCellValue(shiyongInfo.get(i).getShiyongAllCount());
                        }
                        newrow.getCell(14).setCellValue("试用数量");
                        if (shiyongInfo.get(i).getShiyongCount() != null) {
                            newrow.getCell(15).setCellValue(shiyongInfo.get(i).getShiyongCount());
                        }
                    }

                }
            }
            isone = false;
            //第六行
            HSSFRow row6 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row6.createCell(i);
                row6.getCell(i).setCellStyle(style);
            }

            row6.getCell(10).setCellStyle(titleStyle);
            row6.getCell(10).setCellValue("特种硬件  □");
            row6.getCell(12).setCellStyle(titleStyle);
            row6.getCell(12).setCellValue("试用情况");

            //第7行
            HSSFRow row7 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row7.createCell(i);
                row7.getCell(i).setCellStyle(style);
            }
            for (int i = 0; i < shiyongInfo.size(); i++) {
                if (shiyongInfo.get(i).getIsType() == 2) {
                    if (!isone) {
                        row6.getCell(10).setCellValue("特种硬件 √");
                        if (shiyongInfo.get(i).getHardwareName() != null) {
                            row7.getCell(11).setCellValue(shiyongInfo.get(i).getHardwareName());
                        }
                        if (shiyongInfo.get(i).getShiyongAllCount() != null && shiyongInfo.get(i).getShiyongAllCount() != null) {
                            row7.getCell(13).setCellValue(shiyongInfo.get(i).getShiyongAllCount());
                        }
                        if (shiyongInfo.get(i).getShiyongCount() != null && shiyongInfo.get(i).getShiyongCount() != null) {
                            row7.getCell(15).setCellValue(shiyongInfo.get(i).getShiyongCount());
                        }
                        isone = true;
                    } else {
                        HSSFRow newrow = sheet.createRow(++rowNum);
                        for (int j = 0; j <= 15; j++) {
                            newrow.createCell(j);
                            newrow.getCell(j).setCellStyle(style);
                        }
                        newrow.getCell(10).setCellValue("所需设备");
                        if (shiyongInfo.get(i).getHardwareName() != null) {
                            newrow.getCell(11).setCellValue(shiyongInfo.get(i).getHardwareName());
                        }
                        newrow.getCell(12).setCellValue("总数");
                        if (shiyongInfo.get(i).getShiyongAllCount() != null) {
                            newrow.getCell(13).setCellValue(shiyongInfo.get(i).getShiyongAllCount());
                        }
                        newrow.getCell(14).setCellValue("试用数量");
                        if (shiyongInfo.get(i).getShiyongCount() != null) {
                            newrow.getCell(15).setCellValue(shiyongInfo.get(i).getShiyongCount());
                        }
                    }
                }
            }
            isone = false;

            //第8行
            HSSFRow row8 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row8.createCell(i);
                row8.getCell(i).setCellStyle(style);
            }

            //第9行
            HSSFRow row9 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row9.createCell(i);
                row9.getCell(i).setCellStyle(style);
            }
            row7.getCell(10).setCellValue("所需设备");
            row7.getCell(12).setCellValue("总数");
            row7.getCell(14).setCellValue("试用数量");
            row8.getCell(10).setCellStyle(titleStyle);
            row8.getCell(10).setCellValue("集成" + "\r\n" + "□");
            row8.getCell(12).setCellStyle(titleStyle);
            row8.getCell(12).setCellValue("试用情况");
            row9.getCell(10).setCellValue("所需设备");
            row9.getCell(12).setCellValue("总数");
            row9.getCell(14).setCellValue("试用数量");
            for (int i = 0; i < shiyongInfo.size(); i++) {
                if (shiyongInfo.get(i).getIsType() == 3) {
                    if (!isone) {
                        row8.getCell(10).setCellValue("集成" + "\r\n" + "√");
                        if (shiyongInfo.get(i).getHardwareName() != null) {
                            row9.getCell(11).setCellValue(shiyongInfo.get(i).getHardwareName());
                        }
                        if (shiyongInfo.get(i).getShiyongAllCount() != null) {
                            row9.getCell(13).setCellValue(shiyongInfo.get(i).getShiyongAllCount());
                        }
                        if (shiyongInfo.get(i).getShiyongCount() != null) {
                            row9.getCell(15).setCellValue(shiyongInfo.get(i).getShiyongCount());
                        }
                        isone = true;
                    } else {
                        HSSFRow newrow = sheet.createRow(++rowNum);
                        for (int j = 0; j <= 15; j++) {
                            newrow.createCell(j);
                            newrow.getCell(j).setCellStyle(style);
                        }
                        newrow.getCell(10).setCellValue("所需设备");
                        if (shiyongInfo.get(i).getHardwareName() != null) {
                            newrow.getCell(11).setCellValue(shiyongInfo.get(i).getHardwareName());
                        }
                        newrow.getCell(12).setCellValue("总数）");
                        if (shiyongInfo.get(i).getShiyongAllCount() != null) {
                            newrow.getCell(13).setCellValue(shiyongInfo.get(i).getShiyongAllCount());
                        }
                        newrow.getCell(14).setCellValue("试用数量");
                        if (shiyongInfo.get(i).getShiyongCount() != null) {
                            newrow.getCell(15).setCellValue(shiyongInfo.get(i).getShiyongCount());
                        }
                    }
                }
            }
            isone = false;

            //垫资用途
            sheet.getRow(row5.getRowNum() + 1).getCell(2).setCellStyle(titleStyle);
            sheet.getRow(row5.getRowNum() + 1).getCell(2).setCellValue("垫资用途");
            if (projectInfo.getAdvancedfundsDescribe() != null) {
                sheet.getRow(row5.getRowNum() + 1).getCell(3).setCellValue(projectInfo.getAdvancedfundsDescribe());
            }

            //第10行
            HSSFRow row10 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row10.createCell(i);
                row10.getCell(i).setCellStyle(style);
            }

            row10.getCell(2).setCellStyle(titleStyle);
            row10.getCell(5).setCellStyle(titleStyle);
            row10.getCell(7).setCellStyle(titleStyle);
            row10.getCell(9).setCellStyle(titleStyle);
            row10.getCell(11).setCellStyle(titleStyle);
            row10.getCell(13).setCellStyle(titleStyle);
            row10.getCell(15).setCellStyle(titleStyle);
            row10.getCell(2).setCellValue("所需阶段");
            row10.getCell(5).setCellValue("方案□");
            row10.getCell(7).setCellValue("研发□");
            row10.getCell(9).setCellValue("工勘□");
            row10.getCell(11).setCellValue("安装□");
            row10.getCell(13).setCellValue("调试□");
            row10.getCell(15).setCellValue("交付□");
            shebeiInfo.forEach(item -> {
                switch (item.getStageType()) {
                    case 1:
                        row10.getCell(5).setCellValue("方案√");
                        break;
                    case 2:
                        row10.getCell(7).setCellValue("研发√");
                        break;
                    case 3:
                        row10.getCell(9).setCellValue("工勘√");
                        break;
                    case 4:
                        row10.getCell(11).setCellValue("安装√");
                        break;
                    case 5:
                        row10.getCell(13).setCellValue("调试√");
                        break;
                    case 6:
                        row10.getCell(15).setCellValue("交付√");
                        break;
                    default:
                }
            });

            //第11行
            HSSFRow row11 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row11.createCell(i);
                row11.getCell(i).setCellStyle(style);
            }

            //第12行
            HSSFRow row12 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row12.createCell(i);
                row12.getCell(i).setCellStyle(style);
            }
            row12.getCell(1).setCellStyle(titleStyle);
            row12.getCell(2).setCellStyle(titleStyle);
            row12.getCell(3).setCellStyle(titleStyle);
            row12.getCell(7).setCellStyle(titleStyle);
            row12.getCell(13).setCellStyle(titleStyle);
            row12.getCell(14).setCellStyle(titleStyle);
            row12.getCell(1).setCellValue("接触到的客户资源");
            row12.getCell(2).setCellValue("层级");
            row12.getCell(3).setCellValue("高层");
            row12.getCell(7).setCellValue("中层");
            row12.getCell(13).setCellValue("基层");
            row12.getCell(14).setCellValue("商务负责人确认签字：" + (sysUserService.getName(projectInfo.getChargepeopleId()) != null ? sysUserService.getName(projectInfo.getChargepeopleId()) : ""));

            //第13行
            HSSFRow row13 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row13.createCell(i);
                row13.getCell(i).setCellStyle(style);
            }
            row13.getCell(2).setCellStyle(titleStyle);
            row13.getCell(2).setCellValue("职务");
            if (projectInfo.getClientHighdirectorJob() != null && projectInfo.getClientHighdirectorJob() == 1) {
                row13.getCell(3).setCellValue("局长√");
            } else {
                row13.getCell(3).setCellValue("局长□");
            }
            if (projectInfo.getClientHighDupdirectorJob() != null && projectInfo.getClientHighDupdirectorJob() == 1) {
                row13.getCell(5).setCellValue("主管副局长√");
            } else {
                row13.getCell(5).setCellValue("主管副局长□");
            }
            if (projectInfo.getClientMidbranchleaderJob() != null && projectInfo.getClientMidbranchleaderJob() == 1) {
                row13.getCell(7).setCellValue("支队长√");
            } else {
                row13.getCell(7).setCellValue("支队长□");
            }
            if (projectInfo.getClientMidDupbranchleaderJob() != null && projectInfo.getClientMidDupbranchleaderJob() == 1) {
                row13.getCell(9).setCellValue("副支队长√");
            } else {
                row13.getCell(9).setCellValue("副支队长□");
            }
            if (projectInfo.getClientMidcommissarjob() != null && projectInfo.getClientMidcommissarjob() == 1) {
                row13.getCell(11).setCellValue("政委√：");
            } else {
                row13.getCell(11).setCellValue("政委□：");
            }
            row13.getCell(13).setCellValue("影响力：");

            //第14行
            HSSFRow row14 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row14.createCell(i);
                row14.getCell(i).setCellStyle(style);
            }
            row14.getCell(2).setCellStyle(titleStyle);
            row14.getCell(2).setCellValue("是否具有决策权");
            //局长
            if (projectInfo.getClientHighdirectorAuthority() != null && projectInfo.getClientHighdirectorAuthority() == 1) {
                row14.getCell(3).setCellValue("√");
            } else {
                row14.getCell(3).setCellValue("□");
            }
            //副局长
            if (projectInfo.getClientHighDupdirectorAuthority() != null && projectInfo.getClientHighDupdirectorAuthority() == 1) {
                row14.getCell(5).setCellValue("√");
            } else {
                row14.getCell(5).setCellValue("□");
            }
            //队长
            if (projectInfo.getClientMidbranchleaderAuthority() != null && projectInfo.getClientMidbranchleaderAuthority() == 1) {
                row14.getCell(7).setCellValue("√");
            } else {
                row14.getCell(7).setCellValue("□");
            }
            //副队长
            if (projectInfo.getClientMidDupbranchleaderAuthority() != null && projectInfo.getClientMidDupbranchleaderAuthority() == 1) {
                row14.getCell(9).setCellValue("√");
            } else {
                row14.getCell(9).setCellValue("□");
            }
            //政委
            if (projectInfo.getClientMidcommissarAnthority() != null && projectInfo.getClientMidcommissarAnthority() == 1) {
                row14.getCell(11).setCellValue("√");
            } else {
                row14.getCell(11).setCellValue("□");
            }
            //影响力
            if (projectInfo.getClientLowinfluence() != null && projectInfo.getClientLowinfluence() == 0) {
                row14.getCell(13).setCellValue("    弱√" + "\r\n" + "一般□" + "\r\n" + "   强□");
            } else if (projectInfo.getClientLowinfluence() != null && projectInfo.getClientLowinfluence() == 1) {
                row14.getCell(13).setCellValue("    弱□" + "\r\n" + "一般√" + "\r\n" + "   强□");
            } else if (projectInfo.getClientLowinfluence() != null && projectInfo.getClientLowinfluence() == 2) {
                row14.getCell(13).setCellValue("    弱□" + "\r\n" + "一般□" + "\r\n" + "   强√");
            } else {
                row14.getCell(13).setCellValue("    弱□" + "\r\n" + "一般□" + "\r\n" + "   强□");
            }

            //第15行
            HSSFRow row15 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row15.createCell(i);
                row15.getCell(i).setCellStyle(style);
            }
            row15.getCell(2).setCellStyle(titleStyle);
            row15.getCell(2).setCellValue("是否开展方案汇报");
            //局长
            if (projectInfo.getClientHighdirectorReporting() != null && projectInfo.getClientHighdirectorReporting() == 1) {
                row15.getCell(3).setCellValue("√");
            } else {
                row15.getCell(3).setCellValue("□");
            }
            //副局长
            if (projectInfo.getClientHighDupdirectorReporting() != null && projectInfo.getClientHighDupdirectorReporting() == 1) {
                row15.getCell(5).setCellValue("√");
            } else {
                row15.getCell(5).setCellValue("□");
            }
            //队长
            if (projectInfo.getClientMidbranchleaderReporting() != null && projectInfo.getClientMidbranchleaderReporting() == 1) {
                row15.getCell(7).setCellValue("√");
            } else {
                row15.getCell(7).setCellValue("□");
            }
            //副队长
            if (projectInfo.getClientMidDupbranchleaderReporting() != null && projectInfo.getClientMidDupbranchleaderReporting() == 1) {
                row15.getCell(9).setCellValue("√");
            } else {
                row15.getCell(9).setCellValue("□");
            }
            //政委
            if (projectInfo.getClientMidcommissarReporting() != null && projectInfo.getClientMidcommissarReporting() == 1) {
                row15.getCell(11).setCellValue("√");
            } else {
                row15.getCell(11).setCellValue("□");
            }

            //第16行
            HSSFRow row16 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row16.createCell(i);
                row16.getCell(i).setCellStyle(style);
            }
            row16.getCell(2).setCellStyle(titleStyle);
            row16.getCell(2).setCellValue("是否开展商务洽谈");
            //局长
            if (projectInfo.getClientHighdirectorMeet() != null && projectInfo.getClientHighdirectorMeet() == 1) {
                row16.getCell(3).setCellValue("√");
            } else {
                row16.getCell(3).setCellValue("□");
            }
            //副局长
            if (projectInfo.getClientHighDupdirectorMeet() != null && projectInfo.getClientHighDupdirectorMeet() == 1) {
                row16.getCell(5).setCellValue("√");
            } else {
                row16.getCell(5).setCellValue("□");
            }
            //队长
            if (projectInfo.getClientMidbranchleaderMeet() != null && projectInfo.getClientMidbranchleaderMeet() == 1) {
                row16.getCell(7).setCellValue("√");
            } else {
                row16.getCell(7).setCellValue("□");
            }
            //副队长
            if (projectInfo.getClientMidDupbranchleaderMeet() != null && projectInfo.getClientMidDupbranchleaderMeet() == 1) {
                row16.getCell(9).setCellValue("√");
            } else {
                row16.getCell(9).setCellValue("□");
            }
            //政委
            if (projectInfo.getClientMidcommissarMeet() != null && projectInfo.getClientMidcommissarMeet() == 1) {
                row16.getCell(11).setCellValue("√");
            } else {
                row16.getCell(11).setCellValue("□");
            }
            //第17行
            HSSFRow row17 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row17.createCell(i);
                row17.getCell(i).setCellStyle(style);
            }
            row17.getCell(2).setCellStyle(titleStyle);
            row17.getCell(2).setCellValue("是（能）否上传下达");
            //局长
            if (projectInfo.getClientHighdirectorTransmit() != null && projectInfo.getClientHighdirectorTransmit() == 1) {
                row17.getCell(3).setCellValue("√");
            } else {
                row17.getCell(3).setCellValue("□");
            }
            //副局长
            if (projectInfo.getClientHighDupdirectorTransmit() != null && projectInfo.getClientHighDupdirectorTransmit() == 1) {
                row17.getCell(5).setCellValue("√");
            } else {
                row17.getCell(5).setCellValue("□");
            }
            //队长
            if (projectInfo.getClientMidbranchleaderTransmit() != null && projectInfo.getClientMidbranchleaderTransmit() == 1) {
                row17.getCell(7).setCellValue("√");
            } else {
                row17.getCell(7).setCellValue("□");
            }
            //副队长
            if (projectInfo.getClientMidDupbranchleaderTransmit() != null && projectInfo.getClientMidDupbranchleaderTransmit() == 1) {
                row17.getCell(9).setCellValue("√");
            } else {
                row17.getCell(9).setCellValue("□");
            }
            //政委
            if (projectInfo.getClientMidcommissarTransmit() != null && projectInfo.getClientMidcommissarTransmit() == 1) {
                row17.getCell(11).setCellValue("√");
            } else {
                row17.getCell(11).setCellValue("□");
            }
            row17.getCell(14).setCellStyle(titleStyle);
            row17.getCell(14).setCellValue("市场部门领导意见签字:");

            //第18行
            HSSFRow row18 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row18.createCell(i);
                row18.getCell(i).setCellStyle(style);
            }
            row18.getCell(2).setCellStyle(titleStyle);
            row18.getCell(2).setCellValue("合作意愿");
            //局长
            if (projectInfo.getClientHighdirectorWill() != null && projectInfo.getClientHighdirectorWill() == 0) {
                row18.getCell(3).setCellValue("弱√  一般□  强□");
            } else if (projectInfo.getClientHighdirectorWill() != null && projectInfo.getClientHighdirectorWill() == 1) {
                row18.getCell(3).setCellValue("弱□  一般√  强□");
            } else if (projectInfo.getClientHighdirectorWill() != null && projectInfo.getClientHighdirectorWill() == 2) {
                row18.getCell(3).setCellValue("弱□  一般□  强√");
            } else {
                row18.getCell(3).setCellValue("弱□  一般□  强□");
            }
            //副局长
            if (projectInfo.getClientHighDupdirectorWill() != null && projectInfo.getClientHighDupdirectorWill() == 0) {
                row18.getCell(5).setCellValue("弱√  一般□  强□");
            } else if (projectInfo.getClientHighDupdirectorWill() != null && projectInfo.getClientHighDupdirectorWill() == 1) {
                row18.getCell(5).setCellValue("弱□  一般√  强□");
            } else if (projectInfo.getClientHighDupdirectorWill() != null && projectInfo.getClientHighDupdirectorWill() == 2) {
                row18.getCell(5).setCellValue("弱□  一般□  强√");
            } else {
                row18.getCell(5).setCellValue("弱□  一般□  强□");
            }
            //队长
            if (projectInfo.getClientMidbranchleaderWill() != null && projectInfo.getClientMidbranchleaderWill() == 0) {
                row18.getCell(7).setCellValue("弱√  一般□  强□");
            } else if (projectInfo.getClientMidbranchleaderWill() != null && projectInfo.getClientMidbranchleaderWill() == 1) {
                row18.getCell(7).setCellValue("弱□  一般√  强□");
            } else if (projectInfo.getClientMidbranchleaderWill() != null && projectInfo.getClientMidbranchleaderWill() == 2) {
                row18.getCell(7).setCellValue("弱□  一般□  强√");
            } else {
                row18.getCell(7).setCellValue("弱□  一般□  强□");
            }
            //副队长
            if (projectInfo.getClientMidDupbranchleaderWill() != null && projectInfo.getClientMidDupbranchleaderWill() == 0) {
                row18.getCell(9).setCellValue("弱√  一般□  强□");
            } else if (projectInfo.getClientMidDupbranchleaderWill() != null && projectInfo.getClientMidDupbranchleaderWill() == 1) {
                row18.getCell(9).setCellValue("弱□  一般√  强□");
            } else if (projectInfo.getClientMidDupbranchleaderWill() != null && projectInfo.getClientMidDupbranchleaderWill() == 2) {
                row18.getCell(9).setCellValue("弱□  一般□  强√");
            } else {
                row18.getCell(9).setCellValue("弱□  一般□  强□");
            }
            //政委
            if (projectInfo.getClientMidcommissarWill() != null && projectInfo.getClientMidcommissarWill() == 0) {
                row18.getCell(11).setCellValue("弱√  一般□  强□");
            } else if (projectInfo.getClientMidcommissarWill() != null && projectInfo.getClientMidcommissarWill() == 1) {
                row18.getCell(11).setCellValue("弱□  一般√  强□");
            } else if (projectInfo.getClientMidcommissarWill() != null && projectInfo.getClientMidcommissarWill() == 2) {
                row18.getCell(11).setCellValue("弱□  一般□  强√");
            } else {
                row18.getCell(11).setCellValue("弱□  一般□  强□");
            }

            //第19行
            HSSFRow row19 = sheet.createRow(++rowNum);
            Integer newRowNum = row19.getRowNum();
            for (int i = 0; i <= 15; i++) {
                row19.createCell(i);
                row19.getCell(i).setCellStyle(style);
            }
            row19.getCell(1).setCellStyle(titleStyle);
            row19.getCell(1).setCellValue("竞争对手");
            row19.getCell(2).setCellValue("对手名称");
            row19.getCell(2).setCellStyle(titleStyle);
            row19.getCell(5).setCellValue("对手描述");
            row19.getCell(5).setCellStyle(titleStyle);
            sheet.addMergedRegion(new CellRangeAddress(row19.getRowNum(), row19.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row19.getRowNum(), row19.getRowNum(), 6, 13));
            SysCompetitor competitor = new SysCompetitor();
            if (projectInfo.getpId() != null) {
                competitor.setProjectId(projectInfo.getpId());
            }
            List<SysCompetitor> sysCompetitors = sysCompetitorService.selectCompete(competitor);
            if (sysCompetitors != null && sysCompetitors.size() > 0) {
                for (int i = 0; i < sysCompetitors.size(); i++) {
                    if (i == 0) {
                        row19.getCell(3).setCellValue(sysCompetitors.get(i).getCompeteName());
                        row19.getCell(6).setCellValue(sysCompetitors.get(i).getCompeteDescribe());
                    } else {
                        HSSFRow newRow = sheet.createRow(++rowNum);
                        for (int j = 0; j <= 15; j++) {
                            newRow.createCell(j);
                            newRow.getCell(j).setCellStyle(style);
                        }
                        if (sysCompetitors.get(i).getCompeteName() != null) {
                            newRow.getCell(3).setCellValue(sysCompetitors.get(i).getCompeteName());
                        }
                        if (sysCompetitors.get(i).getCompeteDescribe() != null) {
                            newRow.getCell(6).setCellValue(sysCompetitors.get(i).getCompeteDescribe());
                        }
                        sheet.addMergedRegion(new CellRangeAddress(newRow.getRowNum(), newRow.getRowNum(), 3, 4));
                        sheet.addMergedRegion(new CellRangeAddress(newRow.getRowNum(), newRow.getRowNum(), 6, 13));
                        newRowNum = newRow.getRowNum();
                        sheet.addMergedRegion(new CellRangeAddress(row19.getRowNum(), newRowNum, 1, 1));
                        sheet.addMergedRegion(new CellRangeAddress(row19.getRowNum(), newRowNum, 2, 2));
                        sheet.addMergedRegion(new CellRangeAddress(row19.getRowNum(), newRowNum, 5, 5));
                    }
                }

            }

            //第20行
            HSSFRow row20 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row20.createCell(i);
                row20.getCell(i).setCellStyle(noticeStyle);
            }
            if (noticeTitleStyle != null) {
                row20.getCell(1).setCellStyle(noticeTitleStyle);
            }
            row20.getCell(1).setCellValue("风险及应对措施");

            //第21行
            HSSFRow row21 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row21.createCell(i);
                if (noticeStyle != null) {
                    row21.getCell(i).setCellStyle(noticeStyle);
                }
            }
            //商务风险描述
            row20.getCell(2).setCellValue("风险:" + ((projectInfo.getRiskDescribeBusiness() != null) ? projectInfo.getRiskDescribeBusiness() : ""));
            //商务风险应对措施
            row21.getCell(2).setCellValue("措施:" + ((projectInfo.getRiskSolutionsBusiness() != null) ? projectInfo.getRiskSolutionsBusiness() : ""));
            //第22行
            HSSFRow row22 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row22.createCell(i);
                row22.getCell(i).setCellStyle(style);
            }
            //第23行
            HSSFRow row23 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row23.createCell(i);
                row23.getCell(i).setCellStyle(titleStyle);
            }
            row23.getCell(0).setCellValue("技术人员填写");
            row23.getCell(1).setCellValue("项目需求是否明确");
            row23.getCell(3).setCellValue("项目背景");
            row23.getCell(5).setCellValue("应用场景");
            row23.getCell(7).setCellValue("技术理解");
            row23.getCell(9).setCellValue("技术可行性");
            row23.getCell(11).setCellValue("时间节点");
            row23.getCell(14).setCellValue("技术负责人确认签字：");

            //第24行
            HSSFRow row24 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row24.createCell(i);
                row24.getCell(i).setCellStyle(titleStyle);
            }
            //项目背景
            if (projectInfo.getNeedBackground() != null) {
                row24.getCell(3).setCellValue(projectInfo.getNeedBackground());
            }
            //应用场景
            if (projectInfo.getNeedApplication() != null) {
                row24.getCell(5).setCellValue(projectInfo.getNeedApplication());
            }
            //技术理解
            if (projectInfo.getNeedUnderstand() != null && projectInfo.getNeedUnderstand() == 0) {
                row24.getCell(7).setCellValue("理解□ 不理解√");
            } else if (projectInfo.getNeedUnderstand() != null && projectInfo.getNeedUnderstand() == 1) {
                row24.getCell(7).setCellValue("理解√ 不理解□");
            } else {
                row24.getCell(7).setCellValue("理解□ 不理解□");
            }
            //技术可行性
            if (projectInfo.getNeedFeasible() != null && projectInfo.getNeedFeasible() == 0) {
                row24.getCell(9).setCellValue("可行□ 不可行√");
            } else if (projectInfo.getNeedFeasible() != null && projectInfo.getNeedFeasible() == 1) {
                row24.getCell(9).setCellValue("可行√ 不可行□");
            } else {
                row24.getCell(9).setCellValue("可行□ 不可行□");
            }
            //时间节点
            row24.getCell(11).setCellValue((projectInfo.getNeedEndTime() != null && !"".equals(projectInfo.getNeedEndTime())) ? format.format(projectInfo.getNeedEndTime()) : "");

            //第25行
            HSSFRow row25 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row25.createCell(i);
                row25.getCell(i).setCellStyle(titleStyle);
            }
            row25.getCell(1).setCellValue("项目过程");
            row25.getCell(3).setCellValue("方案□");
            row25.getCell(5).setCellValue("研发□");
            row25.getCell(7).setCellValue("工勘□");
            row25.getCell(9).setCellValue("安装□");
            row25.getCell(11).setCellValue("调试□");
            row25.getCell(13).setCellValue("交付□");
            shebeiInfo.forEach(item -> {
                switch (item.getStageType()) {
                    case 1:
                        row25.getCell(3).setCellValue("方案√");
                        break;
                    case 2:
                        row25.getCell(5).setCellValue("研发√");
                        break;
                    case 3:
                        row25.getCell(7).setCellValue("工勘√");
                        break;
                    case 4:
                        row25.getCell(9).setCellValue("安装√");
                        break;
                    case 5:
                        row25.getCell(11).setCellValue("调试√");
                        break;
                    case 6:
                        row25.getCell(13).setCellValue("交付√");
                        break;
                    default:
                }
            });

            //第26行
            HSSFRow row26 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row26.createCell(i);
                row26.getCell(i).setCellStyle(titleStyle);
            }
            row26.getCell(1).setCellValue("项目所需资源");
            row26.getCell(2).setCellValue("人员");

            //第27行
            HSSFRow row27 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row27.createCell(i);
                row27.getCell(i).setCellStyle(titleStyle);
            }
            row27.getCell(2).setCellValue("时间周期");

            //第28行
            HSSFRow row28 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row28.createCell(i);
                row28.getCell(i).setCellStyle(titleStyle);
            }
            row28.getCell(2).setCellValue("设备");
            row28.getCell(14).setCellValue("技术部门领导意见签字：");


            //第29行
            HSSFRow row29 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row29.createCell(i);
                row29.getCell(i).setCellStyle(titleStyle);
            }
            row29.getCell(2).setCellValue("工具");

            //第30行
            HSSFRow row30 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row30.createCell(i);
                row30.getCell(i).setCellStyle(titleStyle);
            }
            row30.getCell(2).setCellValue("车辆");

            //第31行
            HSSFRow row31 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row31.createCell(i);
                row31.getCell(i).setCellStyle(titleStyle);
            }
            row31.getCell(2).setCellValue("场地");

            //第32行
            HSSFRow row32 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row32.createCell(i);
                row32.getCell(i).setCellStyle(titleStyle);
            }
            row32.getCell(2).setCellValue("合作伙伴");
            shebeiInfo.forEach(item -> {
                String names = "";
                String[] split = {};
                if (item.getChargePeople() != null) {
                    names = sysUserService.getName(item.getChargePeople()) + ",";
                }
                if (item.getParticipantsPeople() != null) {
                    split = item.getParticipantsPeople().split(",");
                }
                switch (item.getStageType()) {
                    case 1:
                        for (int i = 0; i < split.length; i++) {
                            String name = "";
                            System.out.println(split[i]);
                            if (split[i] != null && !"".equals(split[i])) {
                                name = sysUserService.getName(Long.parseLong(split[i]));
                            }
                            if (name != null && !"".equals(name)) {
                                names = names + name + ",";
                            }
                        }
                        if (names.length() > 1) {
                            //人员姓名
                            names = names.substring(0, names.length() - 1);
                            row26.getCell(3).setCellValue(names);
                        }
                        //时间周期
                        if (item.getWorkPeriod() != null) {
                            row27.getCell(3).setCellValue(item.getWorkPeriod());
                        }
                        //设备
                        row28.getCell(3).setCellValue(item.getDeviceList());
                        //工具
                        row29.getCell(3).setCellValue(item.getToolList());
                        //车辆
                        if (item.getIsCar() != null && item.getIsCar() == 1) {
                            row30.getCell(3).setCellValue("需要");
                        } else if (item.getIsCar() != null) {
                            row30.getCell(3).setCellValue("无");
                        }
                        //场地
                        row31.getCell(3).setCellValue(item.getPlace());
                        //合作伙伴
                        row32.getCell(3).setCellValue(item.getPartner());
                        break;
                    case 2:
                        for (int i = 0; i < split.length; i++) {
                            String name = "";
                            if (split[i] != null && !"".equals(split[i])) {
                                name = sysUserService.getName(Long.parseLong(split[i]));
                            }
                            if (name != null && !"".equals(name)) {
                                names = names + name + ",";
                            }
                        }
                        if (names.length() > 1) {
                            //人员姓名
                            names = names.substring(0, names.length() - 1);
                            row26.getCell(5).setCellValue(names);
                        }
                        //时间周期
                        if (item.getWorkPeriod() != null) {
                            row27.getCell(5).setCellValue(item.getWorkPeriod());
                        }
                        //设备
                        if (item.getDeviceList() != null) {
                            row28.getCell(5).setCellValue(item.getDeviceList());
                        }
                        //工具
                        if (item.getToolList() != null) {
                            row29.getCell(5).setCellValue(item.getToolList());
                        }
                        //车辆
                        if (item.getIsCar() != null && item.getIsCar() == 1) {
                            row30.getCell(5).setCellValue("需要");
                        } else if (item.getIsCar() != null) {
                            row30.getCell(5).setCellValue("无");
                        }
                        //场地
                        if (item.getPlace() != null) {
                            row31.getCell(5).setCellValue(item.getPlace());
                        }
                        //合作伙伴
                        if (item.getPartner() != null) {
                            row32.getCell(5).setCellValue(item.getPartner());
                        }
                        break;
                    case 3:
                        for (int i = 0; i < split.length; i++) {
                            String name = "";
                            if (split[i] != null && !"".equals(split[i])) {
                                name = sysUserService.getName(Long.parseLong(split[i]));
                            }
                            if (name != null && !"".equals(name)) {
                                names = names + name + ",";
                            }
                        }
                        if (names.length() > 1) {
                            //人员姓名
                            names = names.substring(0, names.length() - 1);
                            row26.getCell(7).setCellValue(names);
                        }
                        //时间周期
                        if (item.getWorkPeriod() != null) {
                            row27.getCell(7).setCellValue(item.getWorkPeriod());
                        }
                        //设备
                        if (item.getDeviceList() != null) {
                            row28.getCell(7).setCellValue(item.getDeviceList());
                        }
                        //工具
                        if (item.getToolList() != null) {
                            row29.getCell(7).setCellValue(item.getToolList());
                        }
                        //车辆
                        if (item.getIsCar() != null && item.getIsCar() == 1) {
                            row30.getCell(7).setCellValue("需要");
                        } else if (item.getIsCar() != null) {
                            row30.getCell(7).setCellValue("无");
                        }
                        //场地
                        if (item.getPlace() != null) {
                            row31.getCell(7).setCellValue(item.getPlace());
                        }
                        //合作伙伴
                        if (item.getPartner() != null) {
                            row32.getCell(7).setCellValue(item.getPartner());
                        }
                        break;
                    case 4:
                        for (int i = 0; i < split.length; i++) {
                            String name = "";
                            if (split[i] != null && !"".equals(split[i])) {
                                name = sysUserService.getName(Long.parseLong(split[i]));
                            }
                            if (name != null && !"".equals(name)) {
                                names = names + name + ",";
                            }
                        }
                        if (names.length() > 1) {
                            //人员姓名
                            names = names.substring(0, names.length() - 1);
                            row26.getCell(9).setCellValue(names);
                        }
                        //时间周期
                        if (item.getWorkPeriod() == null) {
                            row27.getCell(9).setCellValue("");
                            ;
                        } else if (item.getIsCar() != null) {
                            if (item.getWorkPeriod() != null) {
                                row27.getCell(9).setCellValue(item.getWorkPeriod());
                            }
                        }
                        //设备
                        if (item.getDeviceList() == null) {
                            row28.getCell(9).setCellValue("");
                        } else {
                            if (item.getDeviceList() != null) {
                                row28.getCell(9).setCellValue(item.getDeviceList());
                            }
                        }
                        //工具
                        if (item.getToolList() == null) {
                            row29.getCell(9).setCellValue("");
                        } else {
                            if (item.getToolList() != null) {
                                row29.getCell(9).setCellValue(item.getToolList());
                            }
                        }
                        //车辆
                        if (item.getIsCar() != null && item.getIsCar() == 1) {
                            row30.getCell(9).setCellValue("需要");
                        } else if (item.getIsCar() != null) {
                            row30.getCell(9).setCellValue("无");
                        }
                        //场地
                        if (item.getPlace() == null) {
                            row31.getCell(9).setCellValue("");
                        } else {
                            row31.getCell(9).setCellValue(item.getPlace());
                        }
                        //合作伙伴
                        if (item.getPartner() == null) {
                            row32.getCell(9).setCellValue("");
                        } else {
                            row32.getCell(9).setCellValue(item.getPartner());
                        }
                        break;
                    case 5:
                        for (int i = 0; i < split.length; i++) {
                            String name = "";
                            if (split[i] != null && !"".equals(split[i])) {
                                name = sysUserService.getName(Long.parseLong(split[i]));
                            }
                            if (name != null && !"".equals(name)) {
                                names = names + name + ",";
                            }
                        }
                        if (names.length() > 1) {
                            //人员姓名
                            names = names.substring(0, names.length() - 1);
                            row26.getCell(11).setCellValue(names);
                        }
                        //时间周期
                        if (item.getWorkPeriod() == null) {
                            row27.getCell(11).setCellValue("");
                            ;
                        } else {
                            row27.getCell(11).setCellValue(item.getWorkPeriod());
                        }
                        //设备
                        if (item.getDeviceList() == null) {
                            row28.getCell(11).setCellValue("");
                        } else {
                            row28.getCell(11).setCellValue(item.getDeviceList());
                        }
                        //工具
                        if (item.getToolList() == null) {
                            row29.getCell(11).setCellValue("");
                        } else {
                            row29.getCell(11).setCellValue(item.getToolList());
                        }
                        //车辆

                        if (item.getIsCar() != null && item.getIsCar() == 1) {
                            row30.getCell(11).setCellValue("需要");
                        } else if (item.getIsCar() != null) {
                            row30.getCell(11).setCellValue("无");
                        }

                        //场地
                        if (item.getPlace() == null) {
                            row31.getCell(11).setCellValue("");
                        } else {
                            row31.getCell(11).setCellValue(item.getPlace());
                        }
                        //合作伙伴
                        if (item.getPartner() == null) {
                            row32.getCell(11).setCellValue("");
                        } else {
                            row32.getCell(11).setCellValue(item.getPartner());
                        }
                        break;
                    case 6:
                        for (int i = 0; i < split.length; i++) {
                            String name = "";
                            if (split[i] != null && !"".equals(split[i])) {
                                name = sysUserService.getName(Long.parseLong(split[i]));
                            }
                            if (name != null && !"".equals(name)) {
                                names = names + name + ",";
                            }
                        }
                        if (names.length() > 1) {
                            //人员姓名
                            names = names.substring(0, names.length() - 1);
                            row26.getCell(13).setCellValue(names);
                        }
                        //时间周期
                        if (item.getWorkPeriod() == null) {
                            row27.getCell(13).setCellValue("");
                            ;
                        } else {
                            row27.getCell(13).setCellValue(item.getWorkPeriod());
                        }
                        //设备
                        if (item.getDeviceList() == null) {
                            row28.getCell(13).setCellValue("");
                        } else {
                            row28.getCell(13).setCellValue(item.getDeviceList());
                        }
                        //工具
                        if (item.getToolList() == null) {
                            row29.getCell(13).setCellValue("");
                        } else {
                            row29.getCell(13).setCellValue(item.getToolList());
                        }
                        //车辆

                        if (item.getIsCar() != null && item.getIsCar() == 1) {
                            row30.getCell(13).setCellValue("需要");
                        } else if (item.getIsCar() != null) {
                            row30.getCell(13).setCellValue("无");
                        }

                        //场地
                        if (item.getPlace() == null) {
                            row31.getCell(13).setCellValue("");
                        } else {
                            row31.getCell(13).setCellValue(item.getPlace());
                        }
                        //合作伙伴
                        if (item.getPartner() == null) {
                            row32.getCell(13).setCellValue("");
                        } else {
                            row32.getCell(13).setCellValue(item.getPartner());
                        }
                        break;
                    default:
                }
            });

            //第33行
            HSSFRow row33 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row33.createCell(i);
                row33.getCell(i).setCellStyle(noticeStyle);
            }
            row33.getCell(1).setCellStyle(noticeTitleStyle);
            row33.getCell(1).setCellValue("风险及应对措施");

            //第34行
            HSSFRow row34 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row34.createCell(i);
                row34.getCell(i).setCellStyle(noticeStyle);
            }
            //风险
            row33.getCell(2).setCellValue("风险:" + ((projectInfo.getRiskDescribeTechnique() != null) ? projectInfo.getRiskDescribeTechnique() : ""));
            //措施
            row34.getCell(2).setCellValue("措施:" + ((projectInfo.getRiskSolutionsTechnique() != null) ? projectInfo.getRiskSolutionsTechnique() : ""));

            //第35行
            HSSFRow row35 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row35.createCell(i);
                row35.getCell(i).setCellStyle(titleStyle);
            }
            row35.getCell(0).setCellValue("部门领导会签意见:");

            //第36行
            HSSFRow row36 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row36.createCell(i);
                row36.getCell(i).setCellStyle(titleStyle);
            }

            //第37行
            HSSFRow row37 = sheet.createRow(++rowNum);
            for (int i = 0; i <= 15; i++) {
                row37.createCell(i);
                row37.getCell(i).setCellStyle(titleStyle);
            }


            //填写审批时的内容
            for (int i = 0; i < auditFlowOperRecords.size(); i++) {
                switch (auditFlowOperRecords.get(i).getCurrentNodeId()) {
                    //市场部领导审核中
                    case 2:
                        if (auditFlowOperRecords.get(i).getOperMemo() != null && !"".equals(auditFlowOperRecords.get(i).getOperMemo())) {
                            row17.getCell(14).setCellValue(row17.getCell(14).getStringCellValue() + "\r\n" + (auditFlowOperRecords.get(i).getOperType() == 1 ? "通过," : "不通过,") + auditFlowOperRecords.get(i).getOperMemo() + "  " + sysUserService.getName(auditFlowOperRecords.get(i).getOperUserId()) + ";");
                        } else {
                            row17.getCell(14).setCellValue(row17.getCell(14).getStringCellValue() + "\r\n" + (auditFlowOperRecords.get(i).getOperType() == 1 ? "通过" : "不通过") + "  " + sysUserService.getName(auditFlowOperRecords.get(i).getOperUserId()) + ";");
                        }
                        break;
                    //技术总负责人审核中
                    case 3:
                        if (auditFlowOperRecords.get(i).getOperMemo() != null && !"".equals(auditFlowOperRecords.get(i).getOperMemo())) {
                            row23.getCell(14).setCellValue(row23.getCell(14).getStringCellValue() + "\r\n" + (auditFlowOperRecords.get(i).getOperType() == 1 ? "通过," : "不通过,") + auditFlowOperRecords.get(i).getOperMemo() + "  " + sysUserService.getName(auditFlowOperRecords.get(i).getOperUserId()) + ";");
                        } else {
                            row23.getCell(14).setCellValue(row23.getCell(14).getStringCellValue() + "\r\n" + (auditFlowOperRecords.get(i).getOperType() == 1 ? "通过" : "不通过") + "  " + sysUserService.getName(auditFlowOperRecords.get(i).getOperUserId()) + ";");
                        }
                        break;
                    //技术部门领导审核中
                    case 4:
                        if (auditFlowOperRecords.get(i).getOperMemo() != null && !"".equals(auditFlowOperRecords.get(i).getOperMemo())) {
                            row28.getCell(14).setCellValue(row28.getCell(14).getStringCellValue() + "\r\n" + (auditFlowOperRecords.get(i).getOperType() == 1 ? "通过," : "不通过,") + auditFlowOperRecords.get(i).getOperMemo() + "  " + sysUserService.getName(auditFlowOperRecords.get(i).getOperUserId()) + ";");
                        } else {
                            row28.getCell(14).setCellValue(row28.getCell(14).getStringCellValue() + "\r\n" + (auditFlowOperRecords.get(i).getOperType() == 1 ? "通过" : "不通过") + "  " + sysUserService.getName(auditFlowOperRecords.get(i).getOperUserId()) + ";");
                        }
                        break;
                    //公司领导审核中
                    case 5:
                        if (auditFlowOperRecords.get(i).getOperMemo() != null && !"".equals(auditFlowOperRecords.get(i).getOperMemo())) {
                            row35.getCell(0).setCellValue(row35.getCell(0).getStringCellValue() + "\r\n" + (auditFlowOperRecords.get(i).getOperType() == 1 ? "通过," : "不通过,") + auditFlowOperRecords.get(i).getOperMemo() + "  " + sysUserService.getName(auditFlowOperRecords.get(i).getOperUserId()) + ";");
                        } else {
                            row35.getCell(0).setCellValue(row35.getCell(0).getStringCellValue() + "\r\n" + (auditFlowOperRecords.get(i).getOperType() == 1 ? "通过" : "不通过") + "  " + sysUserService.getName(auditFlowOperRecords.get(i).getOperUserId()) + ";");
                        }
                        break;
                    default:
                }
            }
            //获取所有审核中的信息
            List<AuditFlowNodeRole> auditFlowNodeRoles = auditFlowNodeRoleService.selectAuditInfoStateInOneByProjectId(pid);
            //遍历设置表格数据
            for (int i = 0; i < auditFlowNodeRoles.size(); i++) {
                switch (auditFlowNodeRoles.get(i).getNodeId()) {
                    //市场部领导审核中
                    case 2:
                        row17.getCell(14).setCellValue(row17.getCell(14).getStringCellValue() + "\r\n" + sysUserService.getName(auditFlowNodeRoles.get(i).getUserId()) + ",未审批;");
                        break;
                    //技术总负责人审核中
                    case 3:
                        row23.getCell(14).setCellValue(row23.getCell(14).getStringCellValue() + "\r\n" + sysUserService.getName(auditFlowNodeRoles.get(i).getUserId()) + ",未审批;");
                        break;
                    //技术部门领导审核中
                    case 4:
                        row28.getCell(14).setCellValue(row28.getCell(14).getStringCellValue() + "\r\n" + sysUserService.getName(auditFlowNodeRoles.get(i).getUserId()) + ",未审批;");
                        break;
                    //公司领导审核中
                    case 5:
                        row35.getCell(0).setCellValue(row35.getCell(0).getStringCellValue() + "\r\n" + sysUserService.getName(auditFlowNodeRoles.get(i).getUserId()) + ",未审批;");
                        break;
                    default:
                }

            }


            //合并空的单元格
            //第一行

            //第二行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 0, 1));
            sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 4, 5));
            sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 6, 7));
            sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 8, 9));
            sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 10, 11));
            sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 12, 13));
            sheet.addMergedRegion(new CellRangeAddress(row2.getRowNum(), row2.getRowNum(), 14, 15));

            //第三行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row21.getRowNum(), 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 4, 5));
            sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 6, 7));
            sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 8, 9));
            sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 10, 11));
            sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row3.getRowNum(), 12, 13));
            sheet.addMergedRegion(new CellRangeAddress(row3.getRowNum(), row10.getRowNum(), 1, 1));

            //第四行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row4.getRowNum(), row4.getRowNum(), 12, 15));

            //第六行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row6.getRowNum(), row6.getRowNum(), 12, 15));

            //第8行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row8.getRowNum(), row8.getRowNum(), 12, 15));

            //第10行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row10.getRowNum(), row10.getRowNum(), 2, 4));

            //第12行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row12.getRowNum(), row17.getRowNum(), 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(row12.getRowNum(), row12.getRowNum(), 3, 6));
            sheet.addMergedRegion(new CellRangeAddress(row12.getRowNum(), row12.getRowNum(), 7, 12));
            sheet.addMergedRegion(new CellRangeAddress(row12.getRowNum(), row16.getRowNum(), 14, 15));

            //第13行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row13.getRowNum(), row13.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row13.getRowNum(), row13.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row13.getRowNum(), row13.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row13.getRowNum(), row13.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row13.getRowNum(), row13.getRowNum(), 11, 12));

            //第14行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row14.getRowNum(), row18.getRowNum(), 13, 13));

            //第17行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row17.getRowNum(), row21.getRowNum(), 14, 15));

            //第18行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row18.getRowNum(), row18.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row18.getRowNum(), row18.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row18.getRowNum(), row18.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row18.getRowNum(), row18.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row18.getRowNum(), row18.getRowNum(), 11, 12));

            //第20行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row20.getRowNum(), row21.getRowNum(), 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(row20.getRowNum(), row20.getRowNum(), 2, 13));
            sheet.addMergedRegion(new CellRangeAddress(row21.getRowNum(), row21.getRowNum(), 2, 13));

            //第23行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row23.getRowNum(), row24.getRowNum(), 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(row23.getRowNum(), row23.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row23.getRowNum(), row23.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row23.getRowNum(), row23.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row23.getRowNum(), row23.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row23.getRowNum(), row23.getRowNum(), 11, 13));
            sheet.addMergedRegion(new CellRangeAddress(row23.getRowNum(), row34.getRowNum(), 0, 0));
            sheet.addMergedRegion(new CellRangeAddress(row23.getRowNum(), row27.getRowNum(), 14, 15));

            //第24行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row24.getRowNum(), row24.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row24.getRowNum(), row24.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row24.getRowNum(), row24.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row24.getRowNum(), row24.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row24.getRowNum(), row24.getRowNum(), 11, 13));

            //第25行
            sheet.addMergedRegion(new CellRangeAddress(row25.getRowNum(), row25.getRowNum(), 1, 2));
            sheet.addMergedRegion(new CellRangeAddress(row25.getRowNum(), row25.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row25.getRowNum(), row25.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row25.getRowNum(), row25.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row25.getRowNum(), row25.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row25.getRowNum(), row25.getRowNum(), 11, 12));

            //第26行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row26.getRowNum(), row32.getRowNum(), 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(row26.getRowNum(), row26.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row26.getRowNum(), row26.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row26.getRowNum(), row26.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row26.getRowNum(), row26.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row26.getRowNum(), row26.getRowNum(), 11, 12));

            //第27行
            sheet.addMergedRegion(new CellRangeAddress(row27.getRowNum(), row27.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row27.getRowNum(), row27.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row27.getRowNum(), row27.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row27.getRowNum(), row27.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row27.getRowNum(), row27.getRowNum(), 11, 12));

            //第28行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row28.getRowNum(), row34.getRowNum(), 14, 15));
            sheet.addMergedRegion(new CellRangeAddress(row28.getRowNum(), row28.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row28.getRowNum(), row28.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row28.getRowNum(), row28.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row28.getRowNum(), row28.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row28.getRowNum(), row28.getRowNum(), 11, 12));

            //第29行
            sheet.addMergedRegion(new CellRangeAddress(row29.getRowNum(), row29.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row29.getRowNum(), row29.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row29.getRowNum(), row29.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row29.getRowNum(), row29.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row29.getRowNum(), row29.getRowNum(), 11, 12));

            //第30行
            sheet.addMergedRegion(new CellRangeAddress(row30.getRowNum(), row30.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row30.getRowNum(), row30.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row30.getRowNum(), row30.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row30.getRowNum(), row30.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row30.getRowNum(), row30.getRowNum(), 11, 12));

            //第31行
            sheet.addMergedRegion(new CellRangeAddress(row31.getRowNum(), row31.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row31.getRowNum(), row31.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row31.getRowNum(), row31.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row31.getRowNum(), row31.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row31.getRowNum(), row31.getRowNum(), 11, 12));

            //第32行
            sheet.addMergedRegion(new CellRangeAddress(row32.getRowNum(), row32.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row32.getRowNum(), row32.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row32.getRowNum(), row32.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row32.getRowNum(), row32.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row32.getRowNum(), row32.getRowNum(), 11, 12));

            //第33行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row33.getRowNum(), row34.getRowNum(), 1, 1));
            sheet.addMergedRegion(new CellRangeAddress(row33.getRowNum(), row33.getRowNum(), 2, 13));
            sheet.addMergedRegion(new CellRangeAddress(row34.getRowNum(), row34.getRowNum(), 2, 13));

            //第35行
            //合并单元格
            sheet.addMergedRegion(new CellRangeAddress(row35.getRowNum(), row37.getRowNum(), 0, 15));

            //接触到的客户资源单元格 是否具有决策权
            sheet.addMergedRegion(new CellRangeAddress(row14.getRowNum(), row14.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row14.getRowNum(), row14.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row14.getRowNum(), row14.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row14.getRowNum(), row14.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row14.getRowNum(), row14.getRowNum(), 11, 12));
            //是否开展方案汇报
            sheet.addMergedRegion(new CellRangeAddress(row15.getRowNum(), row15.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row15.getRowNum(), row15.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row15.getRowNum(), row15.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row15.getRowNum(), row15.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row15.getRowNum(), row15.getRowNum(), 11, 12));
            //是否开展商务洽谈
            sheet.addMergedRegion(new CellRangeAddress(row16.getRowNum(), row16.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row16.getRowNum(), row16.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row16.getRowNum(), row16.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row16.getRowNum(), row16.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row16.getRowNum(), row16.getRowNum(), 11, 12));
            //是（能）否上传下达
            sheet.addMergedRegion(new CellRangeAddress(row17.getRowNum(), row17.getRowNum(), 3, 4));
            sheet.addMergedRegion(new CellRangeAddress(row17.getRowNum(), row17.getRowNum(), 5, 6));
            sheet.addMergedRegion(new CellRangeAddress(row17.getRowNum(), row17.getRowNum(), 7, 8));
            sheet.addMergedRegion(new CellRangeAddress(row17.getRowNum(), row17.getRowNum(), 9, 10));
            sheet.addMergedRegion(new CellRangeAddress(row17.getRowNum(), row17.getRowNum(), 11, 12));

            sheet.addMergedRegion(new CellRangeAddress(row5.getRowNum(), row5.getRowNum(), 2, 3));
            sheet.addMergedRegion(new CellRangeAddress(row5.getRowNum() + 1, row10.getRowNum() - 1, 3, 9));
            sheet.addMergedRegion(new CellRangeAddress(row5.getRowNum() + 1, row10.getRowNum() - 1, 2, 2));
            sheet.addMergedRegion(new CellRangeAddress(row11.getRowNum(), row11.getRowNum(), 1, 15));
            sheet.addMergedRegion(new CellRangeAddress(row22.getRowNum(), row22.getRowNum(), 0, 15));
            //输出Excel文件
            OutputStream output = null;

            output = response.getOutputStream();

            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=Tasktemplate.xls");
            response.setContentType("application/msexcel");
            wb.write(output);
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }
}

package com.ruoyi.web.controller.excel;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.controller.excel.bean.DictData;
import com.ruoyi.web.controller.excel.bean.ExportDefinition;
import com.ruoyi.web.controller.excel.bean.RowCellIndex;
import com.ruoyi.web.domain.ProjectPlanTable;
import com.ruoyi.web.domain.SysProjectTable;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.service.item.TaskUserService;
import com.ruoyi.web.service.item.ProjectPlanService;
import com.ruoyi.web.service.item.SysProjectTableService;
import com.ruoyi.web.service.item.TaskTableService;
import com.ruoyi.web.util.constant.ManagerConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellRangeAddressList;
import org.apache.poi.ss.util.CellReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

@Controller
@Api(value = "TaskExcelController", tags = "任务导入导出相关接口")
public class TaskExcelController {
    private static final String DICT_SHEET = "DICT_SHEET";
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private TaskUserService taskUserService;
    @Autowired
    private SysProjectTableService sysProjectTableService;
    @Autowired
    private ProjectPlanService projectPlanService;
    @Autowired
    private ISysUserService sysUserService;

    /**
     * 导出任务Excel模块
     *
     * @param response
     * @return
     */
    @ApiOperation("导出任务Excel模块")
    @GetMapping("/outPutTaskExcel")
    public AjaxResult outPutTaskExcel(HttpServletResponse response) {
        try {
            // 1.准备需要生成excel模板的数据
            List<ExportDefinition> edList = new ArrayList<>(2);
            edList.add(new ExportDefinition("项目标题", "pj", "project-dict", "plant-dict", "pl"));
            edList.add(new ExportDefinition("里程碑标题", "pl", "plant-dict", "", ""));
            edList.add(new ExportDefinition("任务标题", "title", "", "", ""));
            edList.add(new ExportDefinition("任务详细描述(可空)", "info", "", "", ""));
            edList.add(new ExportDefinition("任务负责人", "principal", "user-dict", "", ""));
            edList.add(new ExportDefinition("周期(天)", "period", "", "", ""));
            edList.add(new ExportDefinition("开始日期(格式:yyyy/MM/dd)", "starttime", "", "", ""));
            edList.add(new ExportDefinition("结束日期(格式:yyyy/MM/dd)", "endtime", "", "", ""));
            edList.add(new ExportDefinition("紧急程度ID,0:正常,1:紧急(可空)", "exigency", "", "", ""));

            // 2.生成导出模板
            Workbook wb = new HSSFWorkbook();
            Sheet sheet = createExportSheet(edList, wb);
            createConsultSheet(wb);

            // 3.插入级联选项数据
            addDictData();

            // 4.创建数据字典sheet页
            createDictSheet(edList, wb);

            // 5.设置数据有效性
            setDataValidation(edList, sheet);

            // 6.输出Excel文件
            OutputStream output = null;

            output = response.getOutputStream();

            response.reset();
            response.setHeader("Content-disposition", "attachment; filename=Tasktemplate.xls");
            response.setContentType("application/msexcel");
            wb.write(output);
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
            return AjaxResult.error();
        }
        return AjaxResult.success();
    }

    /**
     * 批量任务导入
     *
     * @param file
     * @throws IOException
     */
    @ApiOperation("批量任务导入")
    @Log(title = "批量任务导入", businessType = BusinessType.INSERT)
    @ResponseBody
    @PostMapping("/loadExcel")

    public AjaxResult loadScoreInfo(@ApiParam(value = "文件", required = true) @RequestParam("file") MultipartFile file) throws IOException {
        List<TaskTable> tableList = new ArrayList<>();
        //任务添加成功数量
        int rowNum = 0;
        String taskTitle = "";
        String existTasks = "";
        try {
            String userids = "";
            SysProjectTable sysProjectTable = new SysProjectTable();
            //根据指定的文件输入流导入Excel从而产生Workbook对象
            Workbook wb = new HSSFWorkbook(file.getInputStream());
            //获取Excel文档中的第一个表单
            Sheet sht = wb.getSheetAt(0);
            //对Sheet中的每一行进行迭代
            int cellnum = 0;
            AtomicBoolean taskexist = new AtomicBoolean(false);
            boolean titleexist = false;
            for (Row r : sht) {
                if (isEmptyRow(r)) {
                    continue;
                }
                TaskTable task = new TaskTable();
                rowNum = r.getRowNum() + 1;
                //如果当前行的行号（从0开始）未达到2（第三行）则从新循环
                if (rowNum < 2) {
                    continue;
                }
                r.forEach(item -> {
                    int columnIndex = item.getColumnIndex();
                    //将每个单元格(除日期外)转为文本类型，防止报错
                    if (columnIndex != 6 && columnIndex != 7) {
                        item.setCellType(CellType.STRING);
                    }
                });

                //项目标题
                if (r.getCell(cellnum) != null) {
                    String title = r.getCell(cellnum).getStringCellValue();
                    int left = title.indexOf("_");
                    int right = title.indexOf("_", 1);
                    String pid = title.substring(left + 1, right);
                    task.setProjectId(Long.parseLong(pid));
                    sysProjectTable.setpId(Integer.parseInt(pid));
                } else {
                    return AjaxResult.error("第" + rowNum + "行的项目标题为空");
                }

                //里程碑标题
                if (r.getCell(++cellnum) != null) {
                    String title = r.getCell(cellnum).getStringCellValue();
                    ProjectPlanTable projectPlan = new ProjectPlanTable();
                    projectPlan.setProjectId(sysProjectTable.getpId());
                    projectPlan.setPlanTitle(title);
                    Integer planId = projectPlanService.selectProjectPlanTableIdByTitle(projectPlan);
                    task.setParentId(planId);
                } else {
                    return AjaxResult.error("第" + rowNum + "行的里程碑标题为空");
                }

                //任务标题
                if (r.getCell(++cellnum) != null) {
                    taskTitle = r.getCell(cellnum).getStringCellValue();
                    if ("".equals(taskTitle) || taskTitle.length() > 50) {
                        return AjaxResult.error("第" + rowNum + "行,的任务标题为空或超过50字,请修改");
                    }
                    task.setTaskTitle(r.getCell(cellnum).getStringCellValue());
                } else {
                    return AjaxResult.error("第" + rowNum + "行的标题为空");
                }
//                //判断任务是否重复（根据任务标题及负责人判断）
//                //1根据项目id获取项目所属里程碑的id以及标题
//                List<ProjectPlanTable> projectPlanTables = projectPlanService.selectAllByProjectId(sysProjectTable.getpId());
//                for(int j=0;j<projectPlanTables.size();j++){
//                    List<TaskTable> taskTables = taskTableService.selectTaskTableByPlantId(projectPlanTables.get(j).getPlanId());
//                    for (int k=0;k<taskTables.size();k++){
//                        if(taskTables.get(k).getTaskTitle().equals(taskTitle)){
//                            titleexist=true;
//                            break;
//                        }
//                    }
//                }

                if (r.getCell(++cellnum) != null) {
                    if (!"".equals(r.getCell(cellnum).getStringCellValue())) {
                        task.setTaskDescribe(r.getCell(cellnum).getStringCellValue());
                    }
                }

                //任务负责人
                if (r.getCell(++cellnum) != null) {
                    Long uid = sysUserService.getUid(r.getCell(cellnum).getStringCellValue());
                    if (uid == null || "".equals(uid)) {
                        return AjaxResult.error("第" + rowNum + "行，请检查负责人名字是否输错");
                    } else {
                        task.setChargepeopleId(uid);
                    }
                } else {
                    return AjaxResult.error("第" + rowNum + "行的负责人填写错误");
                }

                //周期
                if (r.getCell(++cellnum) != null) {
                    if (!"".equals(r.getCell(cellnum).getStringCellValue()) && r.getCell(cellnum).getStringCellValue() != null) {
                        if (Double.parseDouble(r.getCell(cellnum).getStringCellValue()) < 6) {
                            task.setPeriod(r.getCell(cellnum).getStringCellValue());
                        } else {
                            return AjaxResult.error("第" + rowNum + "行的周期不能大于5天");
                        }
                    }
                } else {
                    return AjaxResult.error("第" + rowNum + "行的周期填写错误");
                }

                //开始时间
                if (r.getCell(++cellnum) != null) {
                    if (CellType.STRING.equals(r.getCell(cellnum).getCellTypeEnum())) {
                        SimpleDateFormat spdf = new SimpleDateFormat("yyyy/MM/dd");
                        task.setStartTime(spdf.parse(r.getCell(cellnum).getStringCellValue()));
                    } else if (CellType.NUMERIC.equals(r.getCell(cellnum).getCellTypeEnum()) || DateUtil.isCellDateFormatted(r.getCell(cellnum))) {
                        task.setStartTime(r.getCell(cellnum).getDateCellValue());
                    } else {
                        return AjaxResult.error("第" + rowNum + "行的开始时间格式错误");
                    }
                } else {
                    return AjaxResult.error("第" + rowNum + "行的开始时间为空");
                }

                //结束时间
                if (r.getCell(++cellnum) != null) {
                    if (CellType.STRING.equals(r.getCell(cellnum).getCellTypeEnum())) {
                        SimpleDateFormat spdf = new SimpleDateFormat("yyyy/MM/dd");
                        task.setEndTime(spdf.parse(r.getCell(cellnum).getStringCellValue()));
                    } else if (CellType.NUMERIC.equals(r.getCell(cellnum).getCellTypeEnum()) || DateUtil.isCellDateFormatted(r.getCell(cellnum))) {
                        task.setEndTime(r.getCell(cellnum).getDateCellValue());
                    } else {
                        return AjaxResult.error("第" + rowNum + "行的结束时间格式错误");
                    }
                } else {
                    return AjaxResult.error("第" + rowNum + "行的结束时间为空");
                }

                //紧急程度ID，0：正常，1：紧急
                if (r.getCell(++cellnum) != null) {
                    if (!"".equals(r.getCell(cellnum).getStringCellValue())) {
                        task.setUrgencyLevel(r.getCell(cellnum).getStringCellValue());
                    }
                }

                cellnum = 0;
                //设置创建时间
                task.setCreateTime(new Date());
                //设置任务未完成标识
                task.setTaskFinishflag("0");
                //设置任务发起人(发起人为项目负责人)
                SysProjectTable sysProjectTable1 = sysProjectTableService.selectProjectById(task.getProjectId());
                Integer id = 0;
                //0科研项目,1市场项目
                if (sysProjectTable1.getProjectType() == 0) {
                    id = sysProjectTable1.getChargepeopleId();
                } else {
                    id = sysProjectTable1.getTechniquePeople();
                }
                task.setCreateBy(id.toString());
                task.setPlanInOut(0);
                task.setTaskImportedBy(ShiroUtils.getSysUser().getUserName());

                if (taskexist.get()) {
                    existTasks = existTasks + rowNum + "行:" + taskTitle + "的任务重复了请修改 ";
                }
                if (!taskexist.get()) {
                    //添加任务
                    tableList.add(task);
                }
                taskexist.set(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return AjaxResult.error(rowNum + "内容存在错误," + existTasks);
        }
        if (existTasks == null || existTasks.equals(ManagerConstant.IS_NULL)) {
            tableList.forEach(task -> {
                taskTableService.insertExcelTaskTableForPlan(task);
                int c = taskUserService.insertTaskUsers(task, task.getChargepeopleId());
            });
            return AjaxResult.success();
        }
        return AjaxResult.error(existTasks);
    }

    public void addDictData() {
        Map<String, Object> map = new HashMap<>();
        List<String> projectList = new ArrayList();
        Map<String, List<String>> subMap = new HashMap<>();
        //1查询所有存在里程碑的项目的项目id以及标题
        List<SysProjectTable> sysProjectTables = sysProjectTableService.selectExsitPlantProjet();
        //2根据项目id获取项目所属里程碑的id以及标题
        sysProjectTables.forEach(project -> {
            project.setTitle("_" + project.getpId() + "_" + project.getTitle());
            project.setTitle(project.getTitle().replaceAll("[^\\u2E80-\\u9FFF\\w]", "").replaceAll("、", ""));
            List<String> planList = new ArrayList();
            projectList.add(project.getTitle());
            List<ProjectPlanTable> projectPlanTables = projectPlanService.selectAllByProjectId(project.getpId());
            projectPlanTables.forEach(plan -> {
                planList.add(plan.getPlanTitle());
            });
            subMap.put(project.getTitle(), planList);
            map.put("plant-dict", subMap);
        });
        //3所有人员姓名
        List<String> userList = new ArrayList();
        List<SysUser> sysUsers = sysUserService.selectAllUserInfo();
        sysUsers.forEach(user -> {
            userList.add(user.getUserName());
        });
        map.put("user-dict", userList);
        map.put("project-dict", projectList);
        DictData.setDict(map);
    }

    public static void createDataValidateSubList(Sheet sheet, ExportDefinition ed) {
        int rowIndex = ed.getRowIndex();
        CellRangeAddressList cal;
        DVConstraint constraint;
        CellReference cr;
        DataValidation dataValidation;
        System.out.println(ed);
        for (int i = 0; i < 500; i++) {
            int tempRowIndex = ++rowIndex;
            cal = new CellRangeAddressList(tempRowIndex, tempRowIndex, ed.getCellIndex(), ed.getCellIndex());
            cr = new CellReference(rowIndex, ed.getCellIndex() - 1, true, true);
            constraint = DVConstraint.createFormulaListConstraint("INDIRECT(" + cr.formatAsString() + ")");
            dataValidation = new HSSFDataValidation(cal, constraint);
            dataValidation.setSuppressDropDownArrow(false);
            dataValidation.createPromptBox("操作提示", "请选择下拉选中的值");
            dataValidation.createErrorBox("错误提示", "请从下拉选中选择，不要随便输入");
            sheet.addValidationData(dataValidation);
        }
    }

    /**
     * @param edList
     * @param sheet
     */
    private static void setDataValidation(List<ExportDefinition> edList, Sheet sheet) {
        for (ExportDefinition ed : edList) {
            if (MyUtils.isEmpty(ed.getMainDict())) {
                continue;
            }
            // 说明是下拉选
            if (ed.isValidate()) {
                DVConstraint constraint = DVConstraint.createFormulaListConstraint(ed.getField());
                // 说明是一级下拉选
                if (null == ed.getRefName()) {
                    createDataValidate(sheet, ed, constraint);
                } else {
                    // 说明是二级下拉选
                    createDataValidateSubList(sheet, ed);
                }
            }
        }
    }

    /**
     * @param sheet
     * @param ed
     * @param constraint
     */
    private static void createDataValidate(Sheet sheet, ExportDefinition ed, DVConstraint constraint) {
        CellRangeAddressList regions = new CellRangeAddressList(ed.getRowIndex() + 1, ed.getRowIndex() + 500, ed.getCellIndex(), ed.getCellIndex());
        DataValidation dataValidation = new HSSFDataValidation(regions, constraint);
        dataValidation.setSuppressDropDownArrow(false);
        // 设置提示信息
        dataValidation.createPromptBox("操作提示", "请选择下拉选中的值");
        // 设置输入错误信息
        dataValidation.createErrorBox("错误提示", "请从下拉选中选择，不要随便输入");
        sheet.addValidationData(dataValidation);
    }

    /**
     * @param edList
     * @param wb
     */
    private static void createDictSheet(List<ExportDefinition> edList, Workbook wb) {
        Sheet sheet = wb.createSheet(DICT_SHEET);
        RowCellIndex rci = new RowCellIndex(0, -1);
        for (ExportDefinition ed : edList) {
            if (MyUtils.isEmpty(ed.getMainDict())) {
                continue;
            }
            String mainDict = ed.getMainDict();
            // 是第一个下拉选
            if (null != mainDict && null == ed.getRefName()) {
                List<String> mainDictList = (List<String>) DictData.getDict(mainDict);
                String refersToFormula = createDictAndReturnRefFormula(sheet, rci, mainDictList);
                // 创建 命名管理
                createName(wb, ed.getField(), refersToFormula);
                ed.setValidate(true);
            }
            // 联动时加载ed.getSubField()的数据
            if (null != mainDict && null != ed.getSubDict() && null != ed.getSubField()) {
                // 获取需要级联的那个字段
                ExportDefinition subEd = fiterByField(edList, ed.getSubField());
                if (null == subEd) {
                    continue;
                }
                // 保存主下拉选的位置
                subEd.setRefName(ed.getPoint());
                subEd.setValidate(true);
                Map<String, List<String>> subDictListMap = (Map<String, List<String>>) DictData.getDict(ed.getSubDict());
                for (Map.Entry<String, List<String>> entry : subDictListMap.entrySet()) {
                    String refersToFormula = createDictAndReturnRefFormula(sheet, rci, entry.getValue());
                    // 创建 命名管理
                    createName(wb, entry.getKey(), refersToFormula);
                }
            }
        }
    }

    /**
     * @param sheet
     * @param rci
     * @param
     * @return
     */
    private static String createDictAndReturnRefFormula(Sheet sheet, RowCellIndex rci, List<String> datas) {
        Row row = sheet.createRow(rci.incrementRowIndexAndGet());
        rci.setCellIndex(0);
        int startRow = rci.getRowIndex();
        int startCell = rci.getCellIndex();
        for (String dict : datas) {
            row.createCell(rci.incrementCellIndexAndGet()).setCellValue(dict);
        }
        int endRow = rci.getRowIndex();
        int endCell = rci.getCellIndex();
        String startName = new CellReference(DICT_SHEET, startRow, startCell, true, true).formatAsString();
        String endName = new CellReference(endRow, endCell, true, true).formatAsString();
        String refersToFormula = startName + ":" + endName;
        System.out.println(refersToFormula);
        return refersToFormula;
    }

    /**
     * @param wb
     * @param nameName        表示命名管理的名字
     * @param refersToFormula
     */
    private static void createName(Workbook wb, String nameName, String refersToFormula) {
        Name name = wb.createName();
        name.setNameName(nameName);
        name.setRefersToFormula(refersToFormula);
    }

    private static ExportDefinition fiterByField(List<ExportDefinition> edList, String field) {
        for (ExportDefinition ed : edList) {
            if (Objects.equals(ed.getField(), field)) {
                return ed;
            }
        }
        return null;
    }

    /**
     * @param edList
     * @param wb
     */
    private static Sheet createExportSheet(List<ExportDefinition> edList, Workbook wb) {
        CellStyle style = wb.createCellStyle();
        CellStyle redStyle = wb.createCellStyle();
        // 左右居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        redStyle.setAlignment(HorizontalAlignment.CENTER);
        // 左右居中
        redStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        // 上下居中
        style.setBorderBottom(BorderStyle.MEDIUM);
        redStyle.setBorderBottom(BorderStyle.MEDIUM);
        redStyle.setWrapText(true);
        Sheet sheet = wb.createSheet("任务表");
        // 设置单元格宽度
        sheet.setDefaultColumnWidth(28);
        HSSFFont redFont = (HSSFFont) wb.createFont();
        // 设置红色
        redFont.setColor(Font.COLOR_RED);
        // 设置字
        redStyle.setFont(redFont);
        RowCellIndex rci = new RowCellIndex(0, -1);
        Row row = sheet.createRow(rci.getRowIndex());
        CellReference cr = null;
        for (ExportDefinition ed : edList) {
            if (!"任务负责人".equals(ed)) {
                row.createCell(rci.incrementCellIndexAndGet()).setCellValue(ed.getTitle());
                ed.setRowIndex(rci.getRowIndex());
                ed.setCellIndex(rci.getCellIndex());
                cr = new CellReference(ed.getRowIndex() + 1, ed.getCellIndex(), true, true);
                ed.setPoint(cr.formatAsString());
            }
        }
        //设置单元格样式
        for (int i = 0; i <= 8; i++) {
            row.getCell(i).setCellStyle(style);
        }
        List<Integer> redTestCells = Arrays.asList(0, 1, 2, 4, 5, 6, 7);
        for (int i = 0; i < redTestCells.size(); i++) {
            row.getCell(redTestCells.get(i)).setCellStyle(redStyle);
        }
        return sheet;
    }

    private Sheet createConsultSheet(Workbook wb) {
        // 参照表
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        CellStyle style = wb.createCellStyle();
        // 左右居中
        style.setAlignment(HorizontalAlignment.CENTER);
        // 上下居中
        style.setVerticalAlignment(VerticalAlignment.CENTER);
        AtomicReference<Integer> rowNum = new AtomicReference<>(1);
        AtomicReference<Integer> firstRowNum = new AtomicReference<>(1);
        // 建立新的sheet对象（excel的表单）
        HSSFSheet referencerowSheet = (HSSFSheet) wb.createSheet("参照表");
        // 设置单元格宽度
        referencerowSheet.setDefaultColumnWidth(30);
        // 在sheet里创建第二行
        HSSFRow referencerow = referencerowSheet.createRow(0);
        // 创建单元格并设置单元格内容
        List<String> cellTitles = Arrays.asList("项目id", "项目标题", "里程碑id", "里程碑标题", "里程碑开始时间", "里程碑结束时间");
        for (int i = 0; i < cellTitles.size(); i++) {
            referencerow.createCell(i).setCellValue(cellTitles.get(i));
            referencerow.getCell(i).setCellStyle(style);
        }
        // 1查询所有存在里程碑的项目的项目id以及标题
        List<SysProjectTable> sysProjectTables = sysProjectTableService.selectExsitPlantProjet();
        //2根据项目id获取项目所属里程碑的id以及标题
        sysProjectTables.forEach(project -> {
            HSSFRow row = referencerowSheet.createRow(rowNum.get());
            List<ProjectPlanTable> projectPlanTables = projectPlanService.selectAllByProjectId(project.getpId());
            projectPlanTables.forEach(plan -> {
                HSSFRow row2 = referencerowSheet.createRow(rowNum.get());
                row2.createCell(2).setCellValue(plan.getPlanId());
                row2.createCell(3).setCellValue(plan.getPlanTitle());
                row2.createCell(4).setCellValue((plan.getPlanStartTime() != null && !"".equals(plan.getPlanStartTime())) ? format.format(plan.getPlanStartTime()) : "");
                row2.createCell(5).setCellValue((plan.getPlanEndTime() != null && !"".equals(plan.getPlanEndTime())) ? format.format(plan.getPlanEndTime()) : "");
                row2.getCell(2).setCellStyle(style);
                row2.getCell(3).setCellStyle(style);
                row2.getCell(4).setCellStyle(style);
                row2.getCell(5).setCellStyle(style);
                rowNum.set(rowNum.get() + 1);
            });
            row.createCell(0).setCellValue(project.getpId());
            row.createCell(1).setCellValue(project.getTitle());
            row.getCell(0).setCellStyle(style);
            row.getCell(1).setCellStyle(style);
            if (firstRowNum.get() != (rowNum.get() - 1)) {
                referencerowSheet.addMergedRegion(new CellRangeAddress(firstRowNum.get(), (rowNum.get() - 1), 0, 0));
                referencerowSheet.addMergedRegion(new CellRangeAddress(firstRowNum.get(), (rowNum.get() - 1), 1, 1));
            }
            firstRowNum.set(rowNum.get());
        });
        return referencerowSheet;
    }

    public static boolean isEmptyRow(Row row) {
        if (row == null || row.toString().isEmpty()) {
            return true;
        } else {
            boolean isEmpty = true;
            for (int c = row.getFirstCellNum(); c < row.getLastCellNum(); c++) {
                Cell cell = row.getCell(c);
                if (cell.getCellTypeEnum() == CellType.STRING) {
                    if (!MyUtils.isEmpty(cell.getStringCellValue())) {
                        isEmpty = false;
                        break;
                    }
                }
            }
            return isEmpty;
        }
    }
}

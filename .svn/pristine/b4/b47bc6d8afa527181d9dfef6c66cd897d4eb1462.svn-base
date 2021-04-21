package com.ruoyi.web.controller.item;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.SysFileInfo;
import com.ruoyi.web.domain.TaskSubnitPlan;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.item.TaskSubnitPlanService;
import com.ruoyi.web.service.item.TaskTableService;
import io.swagger.annotations.Api;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 提交进度Controller
 *
 * @author ruoyi
 * @date 2019-08-20
 */
@Controller
@RequestMapping("/task/plan")
@Api(value = "TaskSubnitPlanController", tags = "任务进度提交相关接口")
public class TaskSubnitPlanController extends BaseController {
    private String prefix = "system/plan";

    @Autowired
    private TaskSubnitPlanService taskSubnitPlanService;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private AuditFlowCurrentService auditFlowCurrentService;
    @Autowired
    private SysFileInfoService fileInfoService;
    @Value("${ruoyi.profile}")
    private String profile;

    @RequiresPermissions("system:plan:view")
    @GetMapping()
    public String plan() {
        return prefix + "/plan";
    }

    /**
     * 查询提交进度列表
     *
     * @param taskSubnitPlan
     * @return
     */
    @RequiresPermissions("system:plan:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(TaskSubnitPlan taskSubnitPlan) {
        startPage();
        List<TaskSubnitPlan> list = taskSubnitPlanService.selectTaskSubnitPlanList(taskSubnitPlan);
        return getDataTable(list);
    }

    /**
     * 导出提交进度列表
     *
     * @param taskSubnitPlan
     * @return
     */
    @RequiresPermissions("system:plan:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TaskSubnitPlan taskSubnitPlan) {
        List<TaskSubnitPlan> list = taskSubnitPlanService.selectTaskSubnitPlanList(taskSubnitPlan);
        ExcelUtil<TaskSubnitPlan> util = new ExcelUtil<TaskSubnitPlan>(TaskSubnitPlan.class);
        return util.exportExcel(list, "plan");
    }

    /**
     * 新增提交进度
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存提交进度
     *
     * @param taskSubnitPlan
     * @param file
     * @param sysFileInfo1
     * @return
     */
    @Log(title = "提交进度", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public Map<String, Object> addSave(TaskSubnitPlan taskSubnitPlan, MultipartFile[] file, SysFileInfo sysFileInfo1) {
        //更新任务详情
        ShiroUtils.getSysUser().getUserId();
        TaskTable task = new TaskTable();
        task.settId(taskSubnitPlan.gettId());
        task.setTaskDescribe(taskSubnitPlan.getTaskDescribe());
        task.setUpdateTime(new Date());
        taskTableService.updateTaskTableByFlag(task);
        //修改提醒
        List<TaskTable> taskTables = taskTableService.selectTaskByUserId(ShiroUtils.getUserId());
        MyUtils.putMapCache(ShiroUtils.getUserId()+"LogObject", JSON.toJSONString(taskTables));
        taskTables = taskTables.stream()
                .filter(item -> item.gettId().equals(taskSubnitPlan.gettId()))
                .filter(item -> item.getChargepeopleId().equals(ShiroUtils.getUserId()) || item.getCreateBy().equals(ShiroUtils.getUserId()))
                .collect(Collectors.toList());
        //如果是提交人或项目负责人，则可以更新提醒
        if (taskTables != null) {
            taskTableService.updateWarnInfo(taskSubnitPlan);
        }
        taskSubnitPlan.setTaskSubmittime(new Date());
        // 任务id
        sysFileInfo1.setWorkId(Math.toIntExact(taskSubnitPlan.gettId()));
        TaskTable taskTable = new TaskTable();
        taskTable.settId(taskSubnitPlan.gettId());
        // 进度时间为空，则获取进度，且更新数据库
        if (taskSubnitPlan.getScheduleRate() != null) {
            // 如果进度不为100则直接更新任务进度，否则需要审批
            if(taskSubnitPlan.getScheduleRate()!=100){
                taskTable.setScheduleRate(taskSubnitPlan.getScheduleRate());
                taskTable.setUpdateTime(new Date());
                taskTableService.updateTaskTable(taskTable);
            }
            List<TaskSubnitPlan> subnitPlan = taskSubnitPlanService.selectTaskSubnitPlanById(taskSubnitPlan.gettId());
            taskSubnitPlan.setCreateBy(ShiroUtils.getSysUser().getUserName());
            if (subnitPlan != null) {
                List<TaskSubnitPlan> collect = subnitPlan.stream()
                        .filter(item -> taskSubnitPlan.getScheduleRate().equals(item.getScheduleRate()))
                        .collect(Collectors.toList());
                if (collect!=null&&collect.size()!=0) {
                    if(!MyUtils.isEmpty(taskSubnitPlan.getMemo())){
                        taskSubnitPlanService.updateTaskSubnitPlanByIds(taskSubnitPlan);
                    }
                }else {
                    taskSubnitPlanService.insertTaskSubnitPlan(taskSubnitPlan);
                }
            }
        }
        Map<String, Object> listMap = new HashedMap();
        String[] fileIds = {};
        if (sysFileInfo1.getFileIds() != null && !"".equals(sysFileInfo1.getFileIds())) {
            fileIds = sysFileInfo1.getFileIds().split(",");
        }
        for (String s : fileIds) {

            SysFileInfo sysFileInfo = new SysFileInfo();
            sysFileInfo.setFileId(Long.parseLong(s));
            sysFileInfo.setWorkId(Math.toIntExact(taskSubnitPlan.gettId()));
            sysFileInfo.setFileType(0);
            fileInfoService.updateFileInfo(sysFileInfo);
        }

        if (!"".equals(fileIds)) {
            SysFileInfo sysFileInfo = new SysFileInfo();
            sysFileInfo.setWorkId(Math.toIntExact(taskSubnitPlan.gettId()));
            sysFileInfo.setFileType(0);
            listMap.put("code", 0);
            listMap.put("msg", "操作成功！");
            listMap.put("fileInfoList", fileInfoService.selectFileInfoList(sysFileInfo));
            listMap.put("taskSubnitPlanList", null);
        }
        if (taskSubnitPlan.getScheduleRate() != null) {
            TaskSubnitPlan taskSubnitPlan2 = new TaskSubnitPlan();
            taskSubnitPlan2.settId(taskSubnitPlan.gettId());
            listMap.put("code", 0);
            listMap.put("msg", "操作成功！");
            listMap.put("fileInfoList", null);
            listMap.put("taskSubnitPlanList", taskSubnitPlanService.selectTaskSubnitPlanList(taskSubnitPlan2));
        }

        return listMap;
    }

    /**
     * 修改提交进度
     *
     * @param tId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{tId}")
    public String edit(@PathVariable("tId") Long tId, ModelMap mmap) {
        List<TaskSubnitPlan> taskSubnitPlan = taskSubnitPlanService.selectTaskSubnitPlanById(tId);
        mmap.put("taskSubnitPlan", taskSubnitPlan.get(0));
        return prefix + "/edit";
    }

    /**
     * 修改保存提交进度
     *
     * @param taskSubnitPlan
     * @return
     */
    @RequiresPermissions("system:plan:edit")
    @Log(title = "提交进度", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(TaskSubnitPlan taskSubnitPlan) {

        taskSubnitPlan.setTaskSubmittime(new Date());
        return toAjax(taskSubnitPlanService.updateTaskSubnitPlan(taskSubnitPlan));
    }

    /**
     * 删除提交进度
     */
    @RequiresPermissions("system:plan:remove")
    @Log(title = "提交进度", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(taskSubnitPlanService.deleteTaskSubnitPlanByIds(ids));
    }
}

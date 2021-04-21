package com.ruoyi.web.controller.item;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.web.service.item.*;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;


/**
 * 里程碑信息
 *
 * @author ruoyi
 */

@Api(value = "ProjectTableController", tags = "任务里程碑相关")
@RestController
public class ProjectPlanController extends BaseController {

    @Autowired
    private ProjectPlanService projectPlanService;
    @Autowired
    SysFileInfoService sysFileInfoService;
    @Autowired
    SysProjectTableService sysProjectTableService;

    /**
     * 添加里程碑
     *
     * @param projectPlanTable
     * @return
     */
    @ApiOperation("添加里程碑")
    @PostMapping("/addPlan")
    @ResponseBody
    @Log(title = "添加里程碑", businessType = BusinessType.INSERT)
    public int addProjectPlan(ProjectPlanTable projectPlanTable) {
        List<ProjectPlanTable> list = projectPlanService.selectAllPlan(projectPlanTable);
        int planSn = 0;
        if (list.size() >= 1) {
            int maxPlan = list.get(0).getPlanSn();
            for (int i = 1; i < list.size(); i++) {
                if (maxPlan < list.get(i).getPlanSn()) {
                    maxPlan = list.get(i).getPlanSn();
                }
            }
            planSn = maxPlan + 1;
        } else {
            planSn += 1;
        }
        projectPlanTable.setPlanSn(planSn);
        return projectPlanService.insertPlan(projectPlanTable);
    }

    @ApiOperation("获取所有里程碑")
    @PostMapping("/getAllPlan")
    @ResponseBody
    public List<ProjectPlanTable> selectAllPlan(ProjectPlanTable projectPlanTable) {
        return projectPlanService.selectAllPlan(projectPlanTable);
    }

    @ApiOperation("删除里程碑标题")
    @PostMapping("/deletePlanTitle")
    @ResponseBody
    @Log(title = "删除里程碑标题", businessType = BusinessType.DELETE)
    public AjaxResult deletePlanTitle(ProjectPlanTable projectPlanTable) {
        int a = projectPlanService.deletePlanTitle(projectPlanTable);
        return toAjax(a);
    }

    /**
     * 项目文件上传
     *
     * @return
     */
    @Log(title = "项目文件上传", businessType = BusinessType.INSERT)
    @ApiOperation(value = "项目文件上传", notes = "把文件上传后点保存，将上传文件的id发送该接口，接口修改文件信息，完成项目文件上传")
    @PostMapping(value = "/addProjectFile")
    public AjaxResult addProjectFile(String fileIds, Integer pid) {
        return sysProjectTableService.addProjectFile(fileIds,pid);
    }
}
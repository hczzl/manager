package com.ruoyi.web.controller.item;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.ProjectMemo;
import com.ruoyi.web.service.item.ProjectMemoService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RequestMapping("/memo")
@Controller
@Api(value = "ProjectMarkTypeController", tags = "项目备注相关接口")
public class ProjectMemoController extends BaseController {

    @Autowired
    private ProjectMemoService projectMemoService;

    @PostMapping("/insertMemo")
    @ResponseBody
    public AjaxResult insertMemo(ProjectMemo projectMemo) {
        projectMemo.setCreateTime(new Date());
        return toAjax(projectMemoService.insertMemo(projectMemo));
    }

    /**
     * 更新状态
     *
     * @param projectMemo
     * @return
     */
    @PostMapping("/updateState")
    @ResponseBody
    public AjaxResult updateState(ProjectMemo projectMemo) {
        projectMemo.setUpdateTime(new Date());
        return toAjax(projectMemoService.updateState(projectMemo));
    }

    /**
     * 根据项目id获取项目的备注
     *
     * @param projectMemo
     * @return
     */
    @PostMapping("/selectByProjectId")
    @ResponseBody
    public List<ProjectMemo> selectByProjectId(ProjectMemo projectMemo) {
        return projectMemoService.selectByProjectId(projectMemo);
    }
}

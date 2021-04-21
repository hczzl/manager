package com.ruoyi.web.controller.item;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.ProjectMarkTypeInfoTable;
import com.ruoyi.web.service.item.ProjectMarkTypeService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/item/type")
@RestController
@Api(value = "ProjectMarkTypeController", tags = "项目所需软硬件相关接口")
public class ProjectMarkTypeController extends BaseController {
    @Autowired
    private ProjectMarkTypeService projectMarkTypeService;

    /**
     * 添加硬件信息
     *
     * @param projectMarkTypeInfoTable
     * @return
     */
    @PostMapping("/saveMarkType")
    @ResponseBody
    public int saveMarkType(ProjectMarkTypeInfoTable projectMarkTypeInfoTable) {
        return projectMarkTypeService.insertMarkType(projectMarkTypeInfoTable);
    }

    /**
     * 查询资源信息,根据项目id
     *
     * @param projectMarkTypeInfoTable
     * @return
     */
    @PostMapping("/getAllInfo")
    @ResponseBody
    public List<ProjectMarkTypeInfoTable> selectAll(ProjectMarkTypeInfoTable projectMarkTypeInfoTable) {
        return projectMarkTypeService.selectAll(projectMarkTypeInfoTable);
    }

    /**
     * 更新资源表信息
     *
     * @param p
     * @return
     */
    @ResponseBody
    @PostMapping("/updateInfo")
    public AjaxResult updateMarkInfo(ProjectMarkTypeInfoTable p) {
        int a = projectMarkTypeService.updateMarkInfo(p);
        return toAjax(a);
    }
}

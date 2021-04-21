package com.ruoyi.web.controller.item;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.web.domain.ProjectTaskTable;
import com.ruoyi.web.service.item.ProjectTaskTableService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequestMapping("/myEVent")
@Controller
@Api(value = "ProjectTaskTableController", tags = "任务、项目关联汇总相关接口")
public class ProjectTaskTableController extends BaseController {

    @Autowired
    private ProjectTaskTableService projectTaskTableService;

    @PostMapping("/selectInfos")
    @ResponseBody
    public List<ProjectTaskTable> selectInfos(ProjectTaskTable projectTaskTable) {
        return projectTaskTableService.selectInfos(projectTaskTable);
    }


}

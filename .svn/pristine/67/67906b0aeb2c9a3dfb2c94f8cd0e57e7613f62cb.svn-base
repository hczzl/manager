package com.ruoyi.web.controller.item;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.TaskEcharQueryInfo;
import com.ruoyi.web.service.item.TaskEcharService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Suqz
 * @version 1.0
 * @date 2020/1/9 0:22
 */
@Controller
@RequestMapping("/system/user")
@Api(value = "TaskEcharController", tags = "任务统计图相关接口")
public class TaskEcharController {
    @Autowired
    private TaskEcharService taskEcharService;
    /**
     * 统计模块统计图所用数据
     * @param queryInfo
     * @return
     */
    @ResponseBody
    @PostMapping(value = "/getTaskEchartsData")
    @ApiOperation("统计图所用数据")
    public AjaxResult getTaskEchartsData(TaskEcharQueryInfo queryInfo) {
        return taskEcharService.getTaskEchartsData(queryInfo);
    }
}

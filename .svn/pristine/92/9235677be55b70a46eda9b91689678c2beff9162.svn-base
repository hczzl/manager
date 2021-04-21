package com.ruoyi.web.controller.item;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.domain.ProblemFileInfo;
import com.ruoyi.web.service.item.ProblemFileInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Calendar;
import java.util.List;

@Controller
@RequestMapping("/ProblemFileInfo")
@Api(value = "ProblemFileInfoController", tags = "项目问题跟踪表相关接口")
public class ProblemFileInfoController extends BaseController {
    @Autowired
    private ProblemFileInfoService problemFileInfoService;

    /**
     * 查询文件上传列表
     */
    @ApiOperation("查询文件上传列表")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(ProblemFileInfo problemFileInfo)
    {
        startPage();
        List<ProblemFileInfo> list = problemFileInfoService.selectFileInfoList(problemFileInfo);
        return getDataTable(list);
    }

    /**
     * 导出文件上传列表
     */
    @ApiOperation("导出文件上传列表")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(ProblemFileInfo problemFileInfo)
    {
        List<ProblemFileInfo> list = problemFileInfoService.selectFileInfoList(problemFileInfo);
        ExcelUtil<ProblemFileInfo> util = new ExcelUtil<ProblemFileInfo>(ProblemFileInfo.class);
        return util.exportExcel(list, "Probleminfo");
    }

    /**
     * 新增保存文件上传
     */
    @ApiOperation("新增保存文件上传")
    @Log(title = "新增保存文件上传", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(ProblemFileInfo problemFileInfo) {
        int a = 0;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(problemFileInfo.getFiletime());
        //第几周
        int week = calendar.get(Calendar.WEEK_OF_MONTH);
        int month = calendar.get(Calendar.MONTH)+1;
        String mon=String.valueOf(month)+"月第"+String.valueOf(week)+"周";
        problemFileInfo.setWeek(mon);
        a = problemFileInfoService.insertProblemFollowTable(problemFileInfo);
        return toAjax(a);
    }


}

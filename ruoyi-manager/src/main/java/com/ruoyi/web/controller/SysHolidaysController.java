package com.ruoyi.web.controller;

import java.util.List;

import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.ruoyi.web.domain.SysHolidays;
import com.ruoyi.web.service.SysHolidaysService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 发定节假日Controller
 * 
 * @author ruoyi
 * @date 2019-08-28
 */
@Controller
@RequestMapping("/system/holidays")
@Api(value = "SysHolidaysController",tags = "假期获取相关接口")
public class SysHolidaysController extends BaseController
{
    private String prefix = "system/holidays";

    @Autowired
    private SysHolidaysService sysHolidaysService;

    @RequiresPermissions("system:holidays:view")
    @GetMapping()
    public String holidays()
    {
        return prefix + "/holidays";
    }

    /**
     * 查询发定节假日列表
     */
    @RequiresPermissions("system:holidays:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysHolidays sysHolidays)
    {
        startPage();
        List<SysHolidays> list = sysHolidaysService.selectSysHolidaysList(sysHolidays);
        return getDataTable(list);
    }

    /**
     * 导出发定节假日列表
     */
    @RequiresPermissions("system:holidays:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysHolidays sysHolidays)
    {
        List<SysHolidays> list = sysHolidaysService.selectSysHolidaysList(sysHolidays);
        ExcelUtil<SysHolidays> util = new ExcelUtil<SysHolidays>(SysHolidays.class);
        return util.exportExcel(list, "holidays");
    }

    /**
     * 新增发定节假日
     */
    @GetMapping("/add")
    public String add()
    {
        return prefix + "/add";
    }

    /**
     * 新增保存发定节假日
     */
    @RequiresPermissions("system:holidays:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysHolidays sysHolidays)
    {
        return toAjax(sysHolidaysService.insertSysHolidays(sysHolidays));
    }

    /**
     * 修改发定节假日
     */
    @GetMapping("/edit/{id}")
    public String edit(@PathVariable("id") Long id, ModelMap mmap)
    {
        SysHolidays sysHolidays = sysHolidaysService.selectSysHolidaysById(id);
        mmap.put("sysHolidays", sysHolidays);
        return prefix + "/edit";
    }

    /**
     * 修改保存发定节假日
     */
    @RequiresPermissions("system:holidays:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysHolidays sysHolidays)
    {
        return toAjax(sysHolidaysService.updateSysHolidays(sysHolidays));
    }

    /**
     * 删除发定节假日
     */
    @RequiresPermissions("system:holidays:remove")
    @PostMapping( "/remove")
    @ResponseBody
    public AjaxResult remove(String ids)
    {
        return toAjax(sysHolidaysService.deleteSysHolidaysByIds(ids));
    }
}

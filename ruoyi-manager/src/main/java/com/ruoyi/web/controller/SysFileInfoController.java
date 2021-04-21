package com.ruoyi.web.controller;

import java.util.List;

import com.ruoyi.common.utils.MyUtils;
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
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.web.domain.SysFileInfo;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 文件上传Controller
 *
 * @author ruoyi
 * @date 2019-08-26
 */
@Controller
@RequestMapping("/system/fileinfo")
@Api(value = "SysFileInfoController", tags = "文件上传相关接口")
public class SysFileInfoController extends BaseController {
    private String prefix = "system/info";

    @Autowired
    private SysFileInfoService fileInfoService;

    @RequiresPermissions("system:info:view")
    @GetMapping()
    public String info() {
        return prefix + "/info";
    }

    /**
     * 查询文件上传列表
     */
    @RequiresPermissions("system:info:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(SysFileInfo fileInfo) {
        startPage();
        List<SysFileInfo> list = fileInfoService.selectFileInfoList(fileInfo);
        return getDataTable(list);
    }

    /**
     * 导出文件上传列表
     */
    @RequiresPermissions("system:info:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(SysFileInfo fileInfo) {
        List<SysFileInfo> list = fileInfoService.selectFileInfoList(fileInfo);
        ExcelUtil<SysFileInfo> util = new ExcelUtil<SysFileInfo>(SysFileInfo.class);
        return util.exportExcel(list, "info");
    }

    /**
     * 新增文件上传
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存文件上传
     */
    @RequiresPermissions("system:info:add")
    @Log(title = "文件上传", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysFileInfo fileInfo) {
        return toAjax(fileInfoService.insertFileInfo(fileInfo));
    }

    /**
     * 修改文件上传
     */
    @GetMapping("/edit/{fileId}")
    public String edit(@PathVariable("fileId") Long fileId, ModelMap mmap) {
        SysFileInfo fileInfo = fileInfoService.selectFileInfoById(fileId);
        mmap.put("fileInfo", fileInfo);
        return prefix + "/edit";
    }

    /**
     * 修改保存文件上传
     */
    @RequiresPermissions("system:info:edit")
    @Log(title = "文件上传", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysFileInfo fileInfo) {
        return toAjax(fileInfoService.updateFileInfo(fileInfo));
    }

    /**
     * 删除文件上传
     *
     * @param ids
     * @return
     */
    @Log(title = "文件上传", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        MyUtils.removeMapCache("fileDoc");
        return toAjax(fileInfoService.deleteFileInfoByIds(ids));
    }
}

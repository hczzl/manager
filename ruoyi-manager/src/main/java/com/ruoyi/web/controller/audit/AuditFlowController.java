package com.ruoyi.web.controller.audit;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.domain.AuditFlow;
import com.ruoyi.web.service.audit.AuditFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批类别 信息操作处理
 *
 * @author ruoyi
 * @date 2019-07-31
 */
@RequestMapping("/audit/auditFlow")
@Controller
@Api(value = "AuditFlowController", tags = "审批类别相关接口")
public class AuditFlowController extends BaseController {
    private String prefix = "audit/auditFlow";

    @Autowired
    private AuditFlowService auditFlowService;

    @RequiresPermissions("system:auditFlow:view")
    @GetMapping()
    public String auditFlow() {
        return prefix + "/auditFlow";
    }

    @ApiOperation("查询审批类别列表")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AuditFlow auditFlow) {
        startPage();
        List<AuditFlow> list = auditFlowService.selectAuditFlowList(auditFlow);
        return getDataTable(list);
    }

    /**
     * 查询审批类别列表
     *
     * @param auditFlow
     * @return
     */
    @PostMapping("/typelist")
    @ResponseBody
    public TableDataInfo typelist(AuditFlow auditFlow) {
        startPage();
        List<AuditFlow> list = auditFlowService.selectTypeFlowList(auditFlow);
        return getDataTable(list);
    }

    /**
     * 导出审批类别列表
     *
     * @param auditFlow
     * @return
     */
    @RequiresPermissions("system:auditFlow:export")
    @PostMapping("/export")
    @ResponseBody
    @Log(title = "导出审批类别列表", businessType = BusinessType.EXPORT)
    public AjaxResult export(AuditFlow auditFlow) {
        List<AuditFlow> list = auditFlowService.selectAuditFlowList(auditFlow);
        ExcelUtil<AuditFlow> util = new ExcelUtil<AuditFlow>(AuditFlow.class);
        return util.exportExcel(list, "auditFlow");
    }

    /**
     * 新增审批类别
     *
     * @return
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存审批类别
     *
     * @param auditFlow
     * @return
     */
    @Log(title = "审批类别", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AuditFlow auditFlow) {

        return toAjax(auditFlowService.insertAuditFlow(auditFlow));
    }

    /**
     * 修改审批类别
     *
     * @param flowId
     * @param mmap
     * @return
     */
    @GetMapping("/edit/{flowId}")
    public String edit(@PathVariable("flowId") Integer flowId, ModelMap mmap) {
        AuditFlow auditFlow = auditFlowService.selectAuditFlowById(flowId);
        mmap.put("auditFlow", auditFlow);
        return prefix + "/edit";
    }

    /**
     * 修改保存审批类别
     *
     * @param auditFlow
     * @return
     */
    @RequiresPermissions("system:auditFlow:edit")
    @Log(title = "审批类别", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AuditFlow auditFlow) {
        return toAjax(auditFlowService.updateAuditFlow(auditFlow));
    }

    /**
     * 删除审批类别
     *
     * @param ids
     * @return
     */
    @RequiresPermissions("system:auditFlow:remove")
    @Log(title = "审批类别", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(auditFlowService.deleteAuditFlowByIds(ids));
    }

}

package com.ruoyi.web.controller.audit;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.domain.AuditFlowNode;
import com.ruoyi.web.service.audit.AuditFlowNodeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批节点 信息操作处理
 *
 * @author zhongzhilong
 * @date 2019-07-31
 */
@Controller
@RequestMapping("/audit/auditFlowNode")
@Api(value = "AuditFlowNodeController", tags = "审批节点相关接口")
public class AuditFlowNodeController extends BaseController {
    private String prefix = "audit/auditFlowNode";

    @Autowired
    private AuditFlowNodeService auditFlowNodeService;

    @RequiresPermissions("system:auditFlowNode:view")
    @GetMapping()
    public String auditFlowNode() {
        return prefix + "/auditFlowNode";
    }


    @ApiOperation("查询审批节点列表")
    @RequiresPermissions("system:auditFlowNode:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AuditFlowNode auditFlowNode) {
        startPage();
        List<AuditFlowNode> list = auditFlowNodeService.selectAuditFlowNodeList(auditFlowNode);
        return getDataTable(list);
    }


    @ApiOperation("导出审批节点列表")
    @RequiresPermissions("system:auditFlowNode:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AuditFlowNode auditFlowNode) {
        List<AuditFlowNode> list = auditFlowNodeService.selectAuditFlowNodeList(auditFlowNode);
        ExcelUtil<AuditFlowNode> util = new ExcelUtil<AuditFlowNode>(AuditFlowNode.class);
        return util.exportExcel(list, "auditFlowNode");
    }


    @ApiOperation("新增审批节点")
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }


    @ApiOperation("新增保存审批节点")
    @RequiresPermissions("system:auditFlowNode:add")
    @Log(title = "审批节点", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AuditFlowNode auditFlowNode) {
        return toAjax(auditFlowNodeService.insertAuditFlowNode(auditFlowNode));
    }


    @ApiOperation("修改审批节点")
    @GetMapping("/edit/{nodeId}")
    public String edit(@PathVariable("nodeId") Integer nodeId, ModelMap mmap) {
        AuditFlowNode auditFlowNode = auditFlowNodeService.selectAuditFlowNodeById(nodeId);
        mmap.put("auditFlowNode", auditFlowNode);
        return prefix + "/edit";
    }

    @ApiOperation("修改保存审批节点")
    @RequiresPermissions("system:auditFlowNode:edit")
    @Log(title = "审批节点", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AuditFlowNode auditFlowNode) {
        return toAjax(auditFlowNodeService.updateAuditFlowNode(auditFlowNode));
    }


    @ApiOperation("删除审批节点")
    @RequiresPermissions("system:auditFlowNode:remove")
    @Log(title = "审批节点", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(auditFlowNodeService.deleteAuditFlowNodeByIds(ids));
    }

}

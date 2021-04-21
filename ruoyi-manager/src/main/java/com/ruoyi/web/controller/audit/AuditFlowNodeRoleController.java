package com.ruoyi.web.controller.audit;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.domain.AuditFlowNodeRole;
import com.ruoyi.web.service.audit.AuditFlowNodeRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批角色验证 信息操作处理
 *
 * @author zhongzhilong
 * @date 2019-07-31
 */
@Controller
@RequestMapping("/audit/auditFlowNodeRole")
@Api(value = "AuditFlowNodeRoleController", tags = "审批权限相关接口")
public class AuditFlowNodeRoleController extends BaseController {
    private String prefix = "audit/auditFlowNodeRole";

    @Autowired
    private AuditFlowNodeRoleService auditFlowNodeRoleService;

    @RequiresPermissions("system:auditFlowNodeRole:view")
    @GetMapping()
    public String auditFlowNodeRole() {
        return prefix + "/auditFlowNodeRole";
    }


    @ApiOperation("验证更新state")
    @PostMapping("/updateState")
    @ResponseBody
    public int updateState(AuditFlowNodeRole auditFlowNodeRole) {
        auditFlowNodeRole.setCurrentId(54);
        auditFlowNodeRole.setNodeId(5);
        auditFlowNodeRole.setState(0);
        int a = auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole);
        return a;
    }


    @ApiOperation("得到user_id")
    @GetMapping("/getUserId")
    @ResponseBody
    public List<AuditFlowNodeRole> getUserId(AuditFlowNodeRole auditFlowNodeRole) {
        auditFlowNodeRole.setNodeId(2);
        auditFlowNodeRole.setState(1);
        auditFlowNodeRole.setCurrentId(56);
        List<AuditFlowNodeRole> a = auditFlowNodeRoleService.getUserIdByNodeId(auditFlowNodeRole);
        return a;
    }


    @ApiOperation("计算审批总数")
    @GetMapping("/getSumApproval/{currentId}")
    @ResponseBody
    public int getSumApproval(@PathVariable("currentId") int currentId) {
        int a = auditFlowNodeRoleService.getSumApproval(currentId);
        return a;
    }

    @ApiOperation("计算node_id个数")
    @GetMapping("/getSumNodeId")
    @ResponseBody
    public int getSumNodeId(AuditFlowNodeRole auditFlowNodeRole) {
        auditFlowNodeRole.setCurrentId(56);
        auditFlowNodeRole.setNodeId(2);
        auditFlowNodeRole.setState(1);
        int a = auditFlowNodeRoleService.getSumNodeId(auditFlowNodeRole);
        return a;
    }


    @PostMapping("/flowNodeRoleListSum")
    @ResponseBody
    public int selectAuditFlowNodeRoleListSum(AuditFlowNodeRole auditFlowNodeRole) {
        auditFlowNodeRole.setNodeId(4);
        auditFlowNodeRole.setCurrentId(260);
        int a = auditFlowNodeRoleService.selectAuditFlowNodeRoleListSum(auditFlowNodeRole);
        return a;
    }


    @ApiOperation("查询审批角色验证列表")
    @RequiresPermissions("system:auditFlowNodeRole:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AuditFlowNodeRole auditFlowNodeRole) {
        startPage();
        List<AuditFlowNodeRole> list = auditFlowNodeRoleService.selectAuditFlowNodeRoleList(auditFlowNodeRole);
        return getDataTable(list);
    }


    @ApiOperation("导出审批角色验证列表")
    @RequiresPermissions("system:auditFlowNodeRole:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AuditFlowNodeRole auditFlowNodeRole) {
        List<AuditFlowNodeRole> list = auditFlowNodeRoleService.selectAuditFlowNodeRoleList(auditFlowNodeRole);
        ExcelUtil<AuditFlowNodeRole> util = new ExcelUtil<AuditFlowNodeRole>(AuditFlowNodeRole.class);
        return util.exportExcel(list, "auditFlowNodeRole");
    }


    @ApiOperation("新增审批角色验证")
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存审批角色验证
     */
    @RequiresPermissions("system:auditFlowNodeRole:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AuditFlowNodeRole auditFlowNodeRole) {
        return toAjax(auditFlowNodeRoleService.insertAuditFlowNodeRole(auditFlowNodeRole));
    }

    /**
     * 修改审批角色验证
     */
    @GetMapping("/edit/{nodeRoleId}")
    public String edit(@PathVariable("nodeRoleId") Integer nodeRoleId, ModelMap mmap) {
        AuditFlowNodeRole auditFlowNodeRole = auditFlowNodeRoleService.selectAuditFlowNodeRoleById(nodeRoleId);
        mmap.put("auditFlowNodeRole", auditFlowNodeRole);
        return prefix + "/edit";
    }

    /**
     * 修改保存审批角色验证
     */
    @RequiresPermissions("system:auditFlowNodeRole:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AuditFlowNodeRole auditFlowNodeRole) {
        return toAjax(auditFlowNodeRoleService.updateAuditFlowNodeRole(auditFlowNodeRole));
    }

    /**
     * 删除审批角色验证
     */
    @RequiresPermissions("system:auditFlowNodeRole:remove")
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(auditFlowNodeRoleService.deleteAuditFlowNodeRoleByIds(ids));
    }

}

package com.ruoyi.web.controller.audit;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.web.domain.AuditFlowOperRecord;
import com.ruoyi.web.service.audit.AuditFlowOperRecordService;
import io.swagger.annotations.Api;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 审批记录 信息操作处理
 *
 * @author ruoyi
 * @date 2019-08-01
 */
@Controller
@RequestMapping("/audit/auditFlowOperRecord")
@Api(value = "AuditFlowOperRecordController", tags = "审批记录相关接口")
public class AuditFlowOperRecordController extends BaseController {
    private String prefix = "audit/auditFlowOperRecord";

    @Autowired
    private AuditFlowOperRecordService auditFlowOperRecordService;

    @RequiresPermissions("system:auditFlowOperRecord:view")
    @GetMapping()
    public String auditFlowOperRecord() {
        return prefix + "/auditFlowOperRecord";
    }

    /**
     * 查询审批记录列表
     *
     * @param auditFlowOperRecord
     * @return
     */
    @RequiresPermissions("system:auditFlowOperRecord:list")
    @PostMapping("/list")
    @ResponseBody
    public TableDataInfo list(AuditFlowOperRecord auditFlowOperRecord) {
        startPage();
        List<AuditFlowOperRecord> list = auditFlowOperRecordService.selectAuditFlowOperRecordList(auditFlowOperRecord);
        return getDataTable(list);
    }

    @PostMapping("/flowOperRecordSum")
    @ResponseBody
    public int selectAuditFlowOperRecordSum(AuditFlowOperRecord auditFlowOperRecord) {
        auditFlowOperRecord.setCurrentNodeId(4);
        auditFlowOperRecord.setCurrentId(260);
        int a = auditFlowOperRecordService.selectAuditFlowOperRecordSum(auditFlowOperRecord);
        return a;
    }

    /**
     * 导出审批记录列表
     */
    @RequiresPermissions("system:auditFlowOperRecord:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(AuditFlowOperRecord auditFlowOperRecord) {
        List<AuditFlowOperRecord> list = auditFlowOperRecordService.selectAuditFlowOperRecordList(auditFlowOperRecord);
        ExcelUtil<AuditFlowOperRecord> util = new ExcelUtil<AuditFlowOperRecord>(AuditFlowOperRecord.class);
        return util.exportExcel(list, "auditFlowOperRecord");
    }

    /**
     * 新增审批记录
     */
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }

    /**
     * 新增保存审批记录
     */
    @Log(title = "审批记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(AuditFlowOperRecord auditFlowOperRecord) {
        return toAjax(auditFlowOperRecordService.insertAuditFlowOperRecord(auditFlowOperRecord));
    }

    /**
     * 修改审批记录
     */
    @GetMapping("/edit/{recordId}")
    public String edit(@PathVariable("recordId") Integer recordId, ModelMap mmap) {
        AuditFlowOperRecord auditFlowOperRecord = auditFlowOperRecordService.selectAuditFlowOperRecordById(recordId);
        mmap.put("auditFlowOperRecord", auditFlowOperRecord);
        return prefix + "/edit";
    }

    /**
     * 修改保存审批记录
     */
    @RequiresPermissions("system:auditFlowOperRecord:edit")
    @Log(title = "审批记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AuditFlowOperRecord auditFlowOperRecord) {
        return toAjax(auditFlowOperRecordService.updateAuditFlowOperRecord(auditFlowOperRecord));
    }

    /**
     * 删除审批记录
     */
    @RequiresPermissions("system:auditFlowOperRecord:remove")
    @Log(title = "审批记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(auditFlowOperRecordService.deleteAuditFlowOperRecordByIds(ids));
    }

}

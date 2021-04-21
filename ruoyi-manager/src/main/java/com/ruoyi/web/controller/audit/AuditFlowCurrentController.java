package com.ruoyi.web.controller.audit;

import com.ruoyi.web.util.constant.ManagerConstant;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.domain.vo.AuditFlowCrurrentVo;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.domain.ResultAudit;
import com.ruoyi.web.mapper.audit.AuditFlowNodeRoleMapper;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.audit.AuditFlowNodeService;
import com.ruoyi.web.service.item.*;
import com.ruoyi.web.util.UserUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.*;


/**
 * 审批创建记录 信息操作处理
 *
 * @author zhongzhilong
 * @date 2019-08-01
 */
@Controller
@RequestMapping("/audit/auditFlowCurrent")
@Api(value = "AuditFlowCurrentController", tags = "查询审批相关接口")
public class AuditFlowCurrentController extends BaseController {

    private String prefix = "audit/auditFlowCurrent";
    @Autowired
    private AuditFlowCurrentService auditFlowCurrentService;
    @Autowired
    private AuditFlowNodeService auditFlowNodeService;
    @Autowired
    private SysProjectTableService sysProjectTableService;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private AuditFlowNodeRoleMapper auditFlowNodeRoleMapper;


    @RequiresPermissions("system:auditFlowCurrent:view")
    @GetMapping()
    public String auditFlowCurrent() {
        return prefix + "/auditFlowCurrent";
    }


    @GetMapping("/getCurrentNodeId/{currentId}")
    @ResponseBody
    @ApiOperation("根据current_id，得到current_node_id")
    public int getFlowCurrentNodeId(@PathVariable("currentId") int currentId) {
        return auditFlowCurrentService.getFlowCurrentNodeId(currentId);
    }


    @ApiOperation("待办审批列表-新增")
    @PostMapping("/needDealtList")
    @ResponseBody
    @ApiResponses({@ApiResponse(code = 0, message = "total:每页的条数,pages:数据总量,pageNumber:页码,list:分页后的数据集合")})
    public AjaxResult needDealtList(AuditFlowCurrent vo) {
        if (vo.getPageNumber() == null || vo.getTotal() == null) {
            return AjaxResult.error("分页参数错误");
        }
        vo.setNotcommission("1");
        vo.setUserId(ShiroUtils.getUserId());
        vo.setTimeType(0);
        PageEntity pageEntity = auditFlowCurrentService.selectApplyList(vo);
        return AjaxResult.success("查询成功", pageEntity);
    }

    @ApiOperation("已提交审批列表-新增")
    @PostMapping("submitList")
    @ResponseBody
    @ApiResponses({@ApiResponse(code = 0, message = "total:每页的条数,pages:数据总量,pageNumber:页码,list:分页后的数据集合")})
    public AjaxResult submitList(AuditFlowCurrent vo) {
        if (vo.getPageNumber() == null || vo.getTotal() == null) {
            return AjaxResult.error("分页参数错误");
        }
        vo.setMyCommittPart("1");
        vo.setCreateBy(ShiroUtils.getUserId().toString());
        vo.setTimeType(0);
        PageEntity pageEntity = auditFlowCurrentService.selectApplyList(vo);
        return AjaxResult.success("查询成功", pageEntity);
    }

    @ApiOperation("已办任务审批-新增")
    @PostMapping("doneApproveledTaskList")
    @ResponseBody
    @ApiResponses({@ApiResponse(code = 0, message = "total:每页的条数,pages:数据总量,pageNumber:页码,list:分页后的数据集合")})
    public AjaxResult doneApproveledTaskList(AuditFlowCurrent vo) {
        if (vo.getPageNumber() == null || vo.getTotal() == null) {
            return AjaxResult.error("分页参数错误");
        }
        vo.setOperUserId(ShiroUtils.getUserId().toString());
        vo.setType(0);
        vo.setTimeType(1);
        PageEntity pageEntity = auditFlowCurrentService.selectApplyList(vo);
        return AjaxResult.success("查询成功", pageEntity);
    }

    @ApiOperation("已办项目审批-新增")
    @PostMapping("/doneApproveledProjectList")
    @ResponseBody
    @ApiResponses({@ApiResponse(code = 0, message = "total:每页的条数,pages:数据总量,pageNumber:页码,list:分页后的数据集合")})
    public AjaxResult doneApproveledProjectList(AuditFlowCurrent vo) {
        if (vo.getPageNumber() == null || vo.getTotal() == null) {
            return AjaxResult.error("分页参数错误");
        }
        vo.setOperUserId(ShiroUtils.getUserId().toString());
        vo.setType(1);
        vo.setTimeType(1);
        PageEntity pageEntity = auditFlowCurrentService.selectApplyList(vo);
        return AjaxResult.success("查询成功", pageEntity);
    }

    @ApiOperation(value = "点击业务名称、审批按钮显示详情接口", response = AuditFlowCrurrentVo.class)
    @PostMapping("/selectApprovalDetail")
    @ResponseBody
    @ApiResponses({@ApiResponse(code = 0, message = "查询任务的详情，传tId参数;查询项目的详情，传pId参数")})
    public AjaxResult selectApprovalDetail(AuditFlowCrurrentVo vo) {
        return auditFlowCurrentService.selectApprovalDetail(vo);
    }

    @ApiOperation(value = "test")
    @RequestMapping(value = "/test", method = {RequestMethod.POST})
    @ResponseBody
    public List<String> test(AuditFlowCurrent vo) {
        List<String> list = auditFlowCurrentService.selectApplyUserIdList(vo.getCurrentId());
        return list;
    }

    @ApiOperation("查询审批创建记录列表")
    @RequestMapping(value = "/list", method = {RequestMethod.POST})
    @ResponseBody
    public PageEntity list(AuditFlowCurrent auditFlowCurrent) {
        List<ResultAudit> list = auditFlowCurrentService.list(auditFlowCurrent);
        List<ResultAudit> resultList = auditFlowCurrentService.selectUserNameBycId(list, auditFlowCurrent);
        return new PageEntity(resultList.size(), auditFlowCurrent.getPages(), (auditFlowCurrent.getPageNumber() / auditFlowCurrent.getTotal()), resultList);
    }

    @RequestMapping(value = "/commissionlist", method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation("查询已审批列表")
    public PageEntity commissionlist(AuditFlowCurrent auditFlowCurrent) {
        if (!MyUtils.isEmpty(auditFlowCurrent.getEndtime())) {
            String endtime = auditFlowCurrent.getEndtime() + " 24:00:00";
            auditFlowCurrent.setEndtime(endtime);
        }
        if (!MyUtils.isEmpty(auditFlowCurrent.getApprovalendtime())) {
            String endtime = auditFlowCurrent.getApprovalendtime() + " 24:00:00";
            auditFlowCurrent.setApprovalendtime(endtime);
        }
        auditFlowCurrent.setOperUserId(ShiroUtils.getUserId() + "");//
        return list(auditFlowCurrent);
    }

    @ApiOperation("查询待审批的列表")
    @RequestMapping(value = "/notcommissionlist", method = {RequestMethod.POST})
    @ResponseBody
    public PageEntity notcommissionlist(AuditFlowCurrent auditFlowCurrent) {
        auditFlowCurrent.setNotcommission("1");
        if (!MyUtils.isEmpty(auditFlowCurrent.getEndtime())) {
            String endtime = auditFlowCurrent.getEndtime() + " 24:00:00";
            auditFlowCurrent.setEndtime(endtime);
        }
        if (!MyUtils.isEmpty(auditFlowCurrent.getApprovalendtime())) {
            String endtime = auditFlowCurrent.getApprovalendtime() + " 24:00:00";
            auditFlowCurrent.setApprovalendtime(endtime);
        }
        return list(auditFlowCurrent);
    }

    @RequestMapping(value = "/mylist", method = {RequestMethod.POST})
    @ResponseBody
    @ApiOperation("我提交得审核")
    public PageEntity mylist(AuditFlowCurrent auditFlowCurrent) {
        auditFlowCurrent.setMyUser(ShiroUtils.getUserId() + "");//
        auditFlowCurrent.setMyCommittPart("1");
        auditFlowCurrent.setAllow(1);
        SysUser sysUser = UserUtil.getUser();
        auditFlowCurrent.setCreateBy(sysUser.getUserId().intValue() + "");
        if (!MyUtils.isEmpty(auditFlowCurrent.getEndtime())) {
            String endtime = auditFlowCurrent.getEndtime() + " 24:00:00";
            auditFlowCurrent.setEndtime(endtime);
        }
        if (!MyUtils.isEmpty(auditFlowCurrent.getApprovalendtime())) {
            String endtime = auditFlowCurrent.getApprovalendtime() + " 24:00:00";
            auditFlowCurrent.setApprovalendtime(endtime);
        }
        return list(auditFlowCurrent);
    }

    @PostMapping(value = "/commissionlistByTid")
    @ResponseBody
    @ApiOperation("查询任务模块中已经审批列表")
    public PageEntity commissionlistByTid(AuditFlowCurrent auditFlowCurrent) {
        if (!MyUtils.isEmpty(auditFlowCurrent.getEndtime())) {
            String endtime = auditFlowCurrent.getEndtime() + " 24:00:00";
            auditFlowCurrent.setEndtime(endtime);
        }
        if (!MyUtils.isEmpty(auditFlowCurrent.getApprovalendtime())) {
            String endtime = auditFlowCurrent.getApprovalendtime() + " 24:00:00";
            auditFlowCurrent.setApprovalendtime(endtime);
        }
        List<ResultAudit> list = auditFlowCurrentService.commissionlistByTid(auditFlowCurrent);
        List<ResultAudit> resultList = new ArrayList<>();
        if (ShiroUtils.isNotEmpty(list)) {
            resultList = auditFlowCurrentService.paging(auditFlowCurrent, list);
            for (ResultAudit resultAudit : resultList) {
                resultAudit.setCopyPeronName(auditFlowCurrentService.copyName(resultAudit));
            }
        }
        return new PageEntity(resultList.size(), auditFlowCurrent.getPages(), (auditFlowCurrent.getPageNumber() / auditFlowCurrent.getTotal()), resultList);
    }

    @ApiOperation(value = "查询项目模块中已经审批列表")
    @PostMapping("/commissionlistByProjectId")
    @ResponseBody
    public PageEntity commissionlistByProjectId(AuditFlowCurrent auditFlowCurrent) {
        if (!MyUtils.isEmpty(auditFlowCurrent.getEndtime())) {
            String endtime = auditFlowCurrent.getEndtime() + " 24:00:00";
            auditFlowCurrent.setEndtime(endtime);
        }
        if (!MyUtils.isEmpty(auditFlowCurrent.getApprovalendtime())) {
            String endtime = auditFlowCurrent.getApprovalendtime() + " 24:00:00";
            auditFlowCurrent.setApprovalendtime(endtime);
        }
        List<ResultAudit> list = auditFlowCurrentService.commissionlistByProjectId(auditFlowCurrent);
        List<ResultAudit> resultList = new ArrayList<>();
        if (ShiroUtils.isNotEmpty(list)) {
            resultList = auditFlowCurrentService.paging(auditFlowCurrent, list);
            for (ResultAudit resultAudit : resultList) {
                resultAudit.setCopyPeronName(auditFlowCurrentService.copyName(resultAudit));
            }
        }
        return new PageEntity(resultList.size(), auditFlowCurrent.getPages(), (auditFlowCurrent.getPageNumber() / auditFlowCurrent.getTotal()), resultList);
    }

    @GetMapping("/revokeApproval/{currentId}")
    @ResponseBody
    @Log(title = "已提交审批列表-删除操作", businessType = BusinessType.DELETE)
    @ApiOperation("已提交审批列表-删除操作")
    public AjaxResult deleteApproval(@PathVariable("currentId") int currentId) {
        List<Integer> stateList = auditFlowNodeRoleMapper.selectApplyState(currentId);
        if (stateList.contains(0)) {
            return AjaxResult.error("删除失败,该审批已被办理,正在为您刷新列表");
        }
        return toAjax(auditFlowCurrentService.deleteApprovalByCurrentId(currentId));
    }

    @RequiresPermissions("system:auditFlowCurrent:export")
    @PostMapping("/export")
    @ResponseBody
    @ApiOperation("导出审批创建记录列表")
    public AjaxResult export(AuditFlowCurrent auditFlowCurrent) {
        List<AuditFlowCurrent> list = auditFlowCurrentService.selectAuditFlowCurrentList(auditFlowCurrent);
        ExcelUtil<AuditFlowCurrent> util = new ExcelUtil<AuditFlowCurrent>(AuditFlowCurrent.class);
        return util.exportExcel(list, "auditFlowCurrent");
    }

    @ApiOperation("新增审批创建记录")
    @GetMapping("/add")
    public String add() {
        return prefix + "/add";
    }


    @Log(title = "审批创建记录", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    @ApiOperation("审批创建记录")
    public AjaxResult addSave(AuditFlowCurrent auditFlowCurrent) {
        AuditFlowNode auditFlowNode = new AuditFlowNode();
        auditFlowNode.setFlowId(auditFlowCurrent.getAuditId());
        List<AuditFlowNode> auditFlowNodeList = auditFlowNodeService.selectAuditFlowNodeList(auditFlowNode);
        if (auditFlowNodeList.size() < 0) {
            return AjaxResult.error("不存在此审批类型");
        } else {
            Integer id = auditFlowNodeList.get(0).getNodeId();
            for (AuditFlowNode auditFlowNode1 : auditFlowNodeList) {
                if (id >= auditFlowNode1.getNodeId()) {
                    id = auditFlowNode1.getNodeId();
                }
            }
            auditFlowCurrent.setCurrentNodeId(id);
            return toAjax(auditFlowCurrentService.insertAuditFlowCurrent(auditFlowCurrent));
        }
    }

    @ApiOperation("修改审批创建记录")
    @GetMapping("/edit/{currentId}")
    @ResponseBody
    public ModelMap edit(@PathVariable("currentId") Integer currentId) {
        ModelMap mmap = new ModelMap();
        AuditFlowCurrent auditFlowCurrent = auditFlowCurrentService.selectAuditFlowCurrentById(currentId);
        mmap.put("auditFlowCurrent", auditFlowCurrent);
        return mmap;
    }

    @ApiOperation("修改保存审批创建记录")
    @RequiresPermissions("system:auditFlowCurrent:edit")
    @Log(title = "审批创建记录", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(AuditFlowCurrent auditFlowCurrent) {
        return toAjax(auditFlowCurrentService.updateAuditFlowCurrent(auditFlowCurrent));
    }

    @ApiOperation("删除审批创建记录")
    @RequiresPermissions("system:auditFlowCurrent:remove")
    @Log(title = "审批创建记录", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(auditFlowCurrentService.deleteAuditFlowCurrentByIds(ids));
    }

    @PostMapping("/tbbyapplyid")
    @ResponseBody
    public AjaxResult getTbbyApplyId(String applyClassName, String applyTableName, String applyId) {
        if (applyTableName.equals(ManagerConstant.TASK_TABLE)) {
            TaskTable t = taskTableService.selectTaskTableById((long) Integer.parseInt(applyId));
            return AjaxResult.success("任务", t);
        }
        if (applyTableName.equals(ManagerConstant.PROJECT_TABLE)) {
            SysProjectTable p = sysProjectTableService.getProjectTable(Integer.parseInt(applyId));
            return AjaxResult.success("项目", p);
        } else {
            return null;
        }
    }

    @ApiOperation("根据审批流id查询对应的审批单")
    @ResponseBody
    @PostMapping("/listCurrentId")
    public TableDataInfo listCurrentId(AuditFlowCurrent auditFlowCurrent) {
        List<ResultAudit> list = auditFlowCurrentService.listCurrentId(auditFlowCurrent);
        if (ShiroUtils.isNotEmpty(list)) {
            for (ResultAudit resultAudit : list) {
                resultAudit.setCopyPeronName(auditFlowCurrentService.copyName(resultAudit));
            }
        }
        return getDataTable(list);
    }

}

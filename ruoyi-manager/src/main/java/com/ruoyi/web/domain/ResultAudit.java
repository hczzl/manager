package com.ruoyi.web.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.web.domain.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 审核单名称、ID、状态、审批人、时间
 * Created by jnv on 2019/8/16.
 * 实现Comparable接口，对list进行排序
 */
@ApiModel
public class ResultAudit extends BaseEntity implements Serializable {

    private static final long serialVersionUID = -4994078252225367105L;

    @ApiModelProperty("审批流程id")
    private Integer flowId;
    @ApiModelProperty("审批流程名称")
    private String flowName;
    @ApiModelProperty("审核大类id")
    private Integer typeId;
    @ApiModelProperty("申请单表名")
    private String applyTableName;
    @ApiModelProperty("实体类名")
    private String applyClassName;
    @ApiModelProperty("体流程申请单id")
    private Integer applyId;
    @ApiModelProperty()
    private String typeName;
    @ApiModelProperty("状态")
    private String currentNodeName;
    @ApiModelProperty("审批人集合")
    private List<SysUser> userList;
    @ApiModelProperty("审核单名称")
    private String auditName;
    @ApiModelProperty("时间")
    private Date time;
    @ApiModelProperty("审批时间")
    private Date approvalTime;
    @ApiModelProperty("审批记录列表")
    private List<AuditFlowOperRecord> auditFlowOperRecordSet;
    @ApiModelProperty("项目对象")
    private SysProjectTable projectTable;
    @ApiModelProperty("当前审批流id")
    private Integer currentId;
    @ApiModelProperty("任务表对象")
    private TaskTable taskTable;
    @ApiModelProperty("角色权限列表")
    private List<AuditFlowNodeRole> allFlowNodeRoleList;
    @ApiModelProperty("提交人")
    private String submitter;
    @ApiModelProperty("根据currentId获得对应的审批记录")
    private List<ResultApprovalRecord> resultApprovalRecord;
    @ApiModelProperty("审批创建人")
    private String approvalUserName;
    @ApiModelProperty("是否显示审批意见的操作:0是不显示，1是显示")
    private Integer opinionState;
    @ApiModelProperty("状态")
    private Integer state;
    @ApiModelProperty("抄送名")
    private String copyPeronName;


    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getCopyPeronName() {
        return copyPeronName;
    }

    public void setCopyPeronName(String copyPeronName) {
        this.copyPeronName = copyPeronName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getOpinionState() {
        return opinionState;
    }

    public void setOpinionState(Integer opinionState) {
        this.opinionState = opinionState;
    }

    public String getApprovalUserName() {
        return approvalUserName;
    }

    public void setApprovalUserName(String approvalUserName) {
        this.approvalUserName = approvalUserName;
    }

    public List<AuditFlowNodeRole> getAllFlowNodeRoleList() {
        return allFlowNodeRoleList;
    }

    public void setAllFlowNodeRoleList(List<AuditFlowNodeRole> allFlowNodeRoleList) {
        this.allFlowNodeRoleList = allFlowNodeRoleList;
    }

    public TaskTable getTaskTable() {
        return taskTable;
    }

    public void setTaskTable(TaskTable taskTable) {
        this.taskTable = taskTable;
    }

    public Integer getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public SysProjectTable getProjectTable() {
        return projectTable;
    }

    public void setProjectTable(SysProjectTable projectTable) {
        this.projectTable = projectTable;
    }

    public String getApplyTableName() {
        return applyTableName;
    }

    public void setApplyTableName(String applyTableName) {
        this.applyTableName = applyTableName;
    }

    public String getApplyClassName() {
        return applyClassName;
    }

    public void setApplyClassName(String applyClassName) {
        this.applyClassName = applyClassName;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public List<AuditFlowOperRecord> getAuditFlowOperRecordSet() {
        return auditFlowOperRecordSet;
    }

    public void setAuditFlowOperRecordSet(List<AuditFlowOperRecord> auditFlowOperRecordSet) {
        this.auditFlowOperRecordSet = auditFlowOperRecordSet;
    }

    public List<ResultApprovalRecord> getResultApprovalRecord() {
        return resultApprovalRecord;
    }

    public void setResultApprovalRecord(List<ResultApprovalRecord> resultApprovalRecord) {
        this.resultApprovalRecord = resultApprovalRecord;
    }

    public List<SysUser> getUserList() {
        return userList;
    }

    public void setUserList(List<SysUser> userList) {
        this.userList = userList;
    }

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getCurrentNodeName() {
        return currentNodeName;
    }

    public void setCurrentNodeName(String currentNodeName) {
        this.currentNodeName = currentNodeName;
    }

    public String getAuditName() {
        return auditName;
    }

    public void setAuditName(String auditName) {
        this.auditName = auditName;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }
}

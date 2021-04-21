package com.ruoyi.web.domain;

import com.ruoyi.system.domain.SysUser;
import com.ruoyi.web.domain.AuditFlowNodeRole;
import com.ruoyi.web.domain.AuditFlowOperRecord;
import com.ruoyi.web.domain.SysProjectTable;
import com.ruoyi.web.domain.TaskTable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 存储审批的记录，属于哪一级审批，审批状态，审批人，审批意见
 */
@ApiModel
public class ResultApprovalRecord implements Serializable {
    private static final long SERIAL_VERSION_UID = -8316649007417226752L;

    @ApiModelProperty("主键id")
    private Integer id;

    @ApiModelProperty("所属业务id")
    private Integer applyId;

    @ApiModelProperty("number属于哪一级")
    private Integer number;

    @ApiModelProperty("审批状态:0是审批没通过，1是审批通过，2是转推")
    private String approvalState;

    @ApiModelProperty("审批人Id")
    private Integer approvalUserId;

    @ApiModelProperty("审批人姓名")
    private String approvalUserName;

    @ApiModelProperty("审批意见")
    private String approvalMemo;

    @ApiModelProperty("审批流Id")
    private Integer currentId;

    @ApiModelProperty("审批类型")
    private Integer auditId;

    @ApiModelProperty("头像路径")
    private String avatar;

    @ApiModelProperty("审批类型名字")
    private String approvalTypeName;

    @ApiModelProperty("部门id")
    private Integer userDeptId;

    @ApiModelProperty("")
    private Integer state;

    @ApiModelProperty("审批时间")
    private Date approvalTime;

    @ApiModelProperty("1表示查询项目的审批类型，0表示查询任务的审批类型")
    private Integer approvalType;

    @ApiModelProperty("审批节点")
    private Integer currentNodeId;

    @ApiModelProperty("push==1时，将审批时间置为null")
    private Integer push;

    public Integer getPush() {
        return push;
    }

    public void setPush(Integer push) {
        this.push = push;
    }

    public Integer getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(Integer currentNodeId) {
        this.currentNodeId = currentNodeId;
    }


    public Integer getApprovalType() {
        return approvalType;
    }

    public void setApprovalType(Integer approvalType) {
        this.approvalType = approvalType;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getApprovalState() {
        return approvalState;
    }

    public void setApprovalState(String approvalState) {
        this.approvalState = approvalState;
    }

    public Integer getApprovalUserId() {
        return approvalUserId;
    }

    public void setApprovalUserId(Integer approvalUserId) {
        this.approvalUserId = approvalUserId;
    }

    public String getApprovalUserName() {
        return approvalUserName;
    }

    public void setApprovalUserName(String approvalUserName) {
        this.approvalUserName = approvalUserName;
    }

    public String getApprovalMemo() {
        return approvalMemo;
    }

    public void setApprovalMemo(String approvalMemo) {
        this.approvalMemo = approvalMemo;
    }

    public Integer getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getApprovalTypeName() {
        return approvalTypeName;
    }

    public void setApprovalTypeName(String approvalTypeName) {
        this.approvalTypeName = approvalTypeName;
    }

    public Integer getUserDeptId() {
        return userDeptId;
    }

    public void setUserDeptId(Integer userDeptId) {
        this.userDeptId = userDeptId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(Date approvalTime) {
        this.approvalTime = approvalTime;
    }
}

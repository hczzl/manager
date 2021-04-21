package com.ruoyi.web.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.web.util.ConstantUtil;
import com.ruoyi.web.util.constant.ManagerConstant;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;


/**
 * 审批创建记录表 audit_flow_current
 *
 * @author zhongzhilong
 * @date 2019-08-01
 */
public class AuditFlowCurrent extends BaseEntity implements Serializable {

    private static final long SERIAL_VERSION_UID = 4420838990196803594L;
    /**
     * 审批流程当前id
     */
    private Integer currentId;
    /**
     * 具体流程申请单id，如请假申请单
     */
    private Integer applyId;
    /**
     * 申请单标题
     */
    private String applytitle;
    /**
     * 所属审批流程id
     */
    private Integer auditId;
    /**
     * 当前节点id
     */
    private Integer currentNodeId;
    /**
     * 当前状态：1已提交,2审批中,3审批不通过，4审批通过
     */
    private String currentNodeName;

    private Integer currentState;
    /**
     * 第一个审批人Id
     */
    private Integer firstUserId;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 审批创建人id
     */
    private String createBy;
    /**
     * 状态：1有效，0无效，-1删除
     * 1是未审批，0是已审批
     */
    private Integer state;
    /**
     * 申请单表名
     */
    private String applyTableName;
    /**
     * 主键
     */
    private String applyClassName;
    /**
     * 已处理
     */
    private String operUserId;
    /**
     * 审批类型对象
     */
    private AuditFlow auditFlow;

    private AuditFlowNode auditFlowNode;

    private List<AuditFlowNodeRole> auditFlowNodeRole;

    private List<AuditFlowOperRecord> auditFlowOperRecordSet;

    private TaskTable taskTable;

    private SysProjectTable projectTable;

    private String roleId;

    private Long userId;

    private String notcommission;

    private String myCommittPart;

    private String myUser;

    private String typeId;

    private List<AuditFlowNodeRole> allFlowNodeRoleList;
    /**
     * 任务中止原因
     */
    private String stopMone;

    private String userIds;

    private Integer allow;

    private List<Integer> auditList;
    /**
     * 提交开始时间
     */
    private String starttime;
    /**
     * 提交结束时间
     */
    private String endtime;

    private String keyword;
    /**
     * 审批开始时间
     */
    private String approvalstarttime;
    /**
     * 审批结束时间
     */
    private String approvalendtime;

    @ApiModelProperty("类型：任务=0，项目=1")
    private Integer type;

    @ApiModelProperty("")
    private Integer timeType;

    public Integer getTimeType() {
        return timeType;
    }

    public void setTimeType(Integer timeType) {
        this.timeType = timeType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public List<Integer> getAuditList() {
        return auditList;
    }

    public void setAuditList(List<Integer> auditList) {
        this.auditList = auditList;
    }

    public Integer getAllow() {
        return allow;
    }

    public void setAllow(Integer allow) {
        this.allow = allow;
    }

    public String getApplytitle() {
        return applytitle;
    }

    public void setApplytitle(String applytitle) {
        this.applytitle = applytitle;
    }

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
    }


    public String getStopMone() {
        return stopMone;
    }

    public void setStopMone(String stopMone) {
        this.stopMone = stopMone;
    }

    @Override
    public String getCreateBy() {
        return createBy;
    }

    @Override
    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public List<AuditFlowNodeRole> getAllFlowNodeRoleList() {
        return allFlowNodeRoleList;
    }

    public void setAllFlowNodeRoleList(List<AuditFlowNodeRole> allFlowNodeRoleList) {
        this.allFlowNodeRoleList = allFlowNodeRoleList;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }

    public Integer getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

    public Integer getCurrentNodeId() {
        return currentNodeId;
    }

    public void setCurrentNodeId(Integer currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public Integer getCurrentState() {
        return currentState;
    }

    public void setCurrentState(Integer currentState) {
        this.currentState = currentState;
    }

    public Integer getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(Integer firstUserId) {
        this.firstUserId = firstUserId;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public AuditFlow getAuditFlow() {
        return auditFlow;
    }

    public void setAuditFlow(AuditFlow auditFlow) {
        this.auditFlow = auditFlow;
    }

    public AuditFlowNode getAuditFlowNode() {
        return auditFlowNode;
    }

    public void setAuditFlowNode(AuditFlowNode auditFlowNode) {
        this.auditFlowNode = auditFlowNode;
    }

    public List<AuditFlowNodeRole> getAuditFlowNodeRole() {
        return auditFlowNodeRole;
    }

    public void setAuditFlowNodeRole(List<AuditFlowNodeRole> auditFlowNodeRole) {
        this.auditFlowNodeRole = auditFlowNodeRole;
    }

    public List<AuditFlowOperRecord> getAuditFlowOperRecordSet() {
        return auditFlowOperRecordSet;
    }

    public void setAuditFlowOperRecordSet(List<AuditFlowOperRecord> auditFlowOperRecordSet) {
        this.auditFlowOperRecordSet = auditFlowOperRecordSet;
    }

    public TaskTable getTaskTable() {
        return taskTable;
    }

    public void setTaskTable(TaskTable taskTable) {
        this.taskTable = taskTable;
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

    public String getOperUserId() {
        return operUserId;
    }

    public void setOperUserId(String operUserId) {
        this.operUserId = operUserId;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getMyCommittPart() {
        return myCommittPart;
    }

    public void setMyCommittPart(String myCommittPart) {
        this.myCommittPart = myCommittPart;
    }

    public String getNotcommission() {
        return notcommission;
    }

    public void setNotcommission(String notcommission) {
        this.notcommission = notcommission;
    }

    public String getMyUser() {
        return myUser;
    }

    public void setMyUser(String myUser) {
        this.myUser = myUser;
    }

    public String getStarttime() {
        return starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getApprovalstarttime() {
        return approvalstarttime;
    }

    public void setApprovalstarttime(String approvalstarttime) {
        this.approvalstarttime = approvalstarttime;
    }

    public String getApprovalendtime() {
        return approvalendtime;
    }

    public void setApprovalendtime(String approvalendtime) {
        this.approvalendtime = approvalendtime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("currentId", getCurrentId())
                .append("applyId", getApplyId())
                .append("auditId", getAuditId())
                .append("currentNodeId", getCurrentNodeId())
                .append("currentState", getCurrentState())
                .append("firstUserId", getFirstUserId())
                .append("addTime", getAddTime())
                .append("state", getState())
                .toString();
    }

    /**
     * 审核状态1已提交,2审批中,3审批不通过，4审批通过,5审批全部通过,7已经撤销(科研项目和市场项目),8:审批中
     *
     * @return
     */
    public String getCurrentNodeName() {
        if (currentState == null) {
            return "";
        } else if (currentState.equals(ManagerConstant.APPROVAL_SUCCESS)) {
            return "审批全部通过";
        } else if (currentState.equals(ManagerConstant.APPROVAL_PUSH)) {
            return "转推";
        } else if (currentState.equals(ManagerConstant.APPROVAL_FAILURE)) {
            return "审核不通过";
        } else if (currentState.equals(ManagerConstant.APPROVAL_NOT_FINISH)) {
            return "审批中";
        } else if (currentState.equals(ManagerConstant.APPROVAL_RESCINDED)) {
            return "已撤销";
        } else {
            return auditFlowNode.getNodeName();
        }
    }

    public void setCurrentNodeName(String currentNodeName) {
        this.currentNodeName = currentNodeName;
    }
}

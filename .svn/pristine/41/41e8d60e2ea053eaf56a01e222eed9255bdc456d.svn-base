package com.ruoyi.web.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * 消息实体类
 *
 * @author zhongzhilong
 * @date 2019/9/15 0015
 */
@ApiModel(description = "消息表")
public class MsgEvtInfo extends BaseEntity implements Serializable {

    private static final long SERIAL_VERSION_UID = 6647687305518201370L;

    private long id;

    @ApiModelProperty(value = "消息类型")
    private Integer type;

    @ApiModelProperty(value = "项目id、任务id、审批流id")
    private long eventId;

    @ApiModelProperty(value = "提醒的用户id")
    private Integer userId;

    @ApiModelProperty(value = "已读或者未读状态")
    private Integer readMark;

    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    private Date updateTime;

    @ApiModelProperty(value = "用户名字")
    private String userName;

    @ApiModelProperty(value = "审批类型")
    private int auditId;

    @ApiModelProperty(value = "审批名字")
    private String approvalTypeName;

    private int taskType;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("项目名称")
    private String projectName;

    @ApiModelProperty("审批流id,即项目或者任务的名称")
    private String currentName;

    @ApiModelProperty("任务或者项目审批的发起人")
    private Integer createApproval;

    @ApiModelProperty("选择显示哪一串字符串:0是显示您参与的，1是显示您申请的")
    private Integer applyAndParticipateState;

    private Integer tIds;

    private Integer pIds;

    private List<Integer> typeList;

    @ApiModelProperty("审批流id")
    private Integer currentId;

    public List<Integer> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Integer> typeList) {
        this.typeList = typeList;
    }

    public Integer gettIds() {
        return tIds;
    }

    public void settIds(Integer tIds) {
        this.tIds = tIds;
    }

    public Integer getpIds() {
        return pIds;
    }

    public void setpIds(Integer pIds) {
        this.pIds = pIds;
    }

    public Integer getCreateApproval() {
        return createApproval;
    }

    public void setCreateApproval(Integer createApproval) {
        this.createApproval = createApproval;
    }

    public Integer getApplyAndParticipateState() {
        return applyAndParticipateState;
    }

    public void setApplyAndParticipateState(Integer applyAndParticipateState) {
        this.applyAndParticipateState = applyAndParticipateState;
    }

    public String getCurrentName() {
        return currentName;
    }

    public void setCurrentName(String currentName) {
        this.currentName = currentName;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getApprovalTypeName() {
        return approvalTypeName;
    }

    public void setApprovalTypeName(String approvalTypeName) {
        this.approvalTypeName = approvalTypeName;
    }

    public int getAuditId() {
        return auditId;
    }

    public void setAuditId(int auditId) {
        this.auditId = auditId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getReadMark() {
        return readMark;
    }

    public void setReadMark(Integer readMark) {
        this.readMark = readMark;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Integer getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }
}

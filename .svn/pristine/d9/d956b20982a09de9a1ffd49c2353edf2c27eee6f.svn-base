package com.ruoyi.web.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 审批类别表 audit_flow
 *
 * @author ruoyi
 * @date 2019-07-31
 */
@ApiModel
public class AuditFlow implements Serializable {

    private static final long SERIAL_VERSION_UID = 7503051199146148143L;
    /**
     * 审批流程id
     */
    @ApiModelProperty("审批流id")
    private Integer flowId;
    /**
     * 审批流程名称
     */
    private String flowName;

    /**
     * 审核大类
     */
    private Integer typeId;
    /**
     * 审核大类
     */
    private String typeName;
    /**
     * 申请单表名
     */
    private String applyTableName;
    /**
     * 实体类名
     */
    private String applyClassName;
    /**
     * 备注
     */
    private String memo;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 状态：1有效，0无效，-1删除
     */
    private Integer state;

    private String userIds;

    public String getUserIds() {
        return userIds;
    }

    public void setUserIds(String userIds) {
        this.userIds = userIds;
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

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public Integer getFlowId() {
        return flowId;
    }

    public void setFlowName(String flowName) {
        this.flowName = flowName;
    }

    public String getFlowName() {
        return flowName;
    }

    public void setApplyTableName(String applyTableName) {
        this.applyTableName = applyTableName;
    }

    public String getApplyTableName() {
        return applyTableName;
    }

    public void setApplyClassName(String applyClassName) {
        this.applyClassName = applyClassName;
    }

    public String getApplyClassName() {
        return applyClassName;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Integer getState() {
        return state;
    }


    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("flowId", getFlowId())
                .append("flowName", getFlowName())
                .append("applyTableName", getApplyTableName())
                .append("applyClassName", getApplyClassName())
                .append("memo", getMemo())
                .append("addTime", getAddTime())
                .append("state", getState())
                .toString();
    }
}

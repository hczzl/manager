package com.ruoyi.web.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;

/**
 * 审批记录表 audit_flow_oper_record
 *
 * @author ruoyi
 * @date 2019-08-01
 */
public class AuditFlowOperRecord implements Serializable {

    private static final long SERIAL_VERSION_UID = 8240978165182722453L;
    /**
     * id
     */
    private Integer recordId;
    /**
     * 审批流程申请单id
     */
    private Integer currentId;
    /**
     * 当前节点id
     */
    private Integer currentNodeId;
    /**
     * 操作人id
     */
    private Integer operUserId;
    /**
     * 操作类型：1通过，2不通过
     */
    private Integer operType;
    /**
     * 操作备注
     */
    private String operMemo;
    /**
     * 操作时间
     */
    private Date addTime;
    /**
     * 状态：1有效，0无效，-1删除
     */
    private Integer state;

    private String pushUsers;


    public String getPushUsers() {
        return pushUsers;
    }

    public void setPushUsers(String pushUsers) {
        this.pushUsers = pushUsers;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public Integer getCurrentId() {
        return currentId;
    }

    public void setCurrentNodeId(Integer currentNodeId) {
        this.currentNodeId = currentNodeId;
    }

    public Integer getCurrentNodeId() {
        return currentNodeId;
    }

    public void setOperUserId(Integer operUserId) {
        this.operUserId = operUserId;
    }

    public Integer getOperUserId() {
        return operUserId;
    }

    public void setOperType(Integer operType) {
        this.operType = operType;
    }

    public Integer getOperType() {
        return operType;
    }

    public void setOperMemo(String operMemo) {
        this.operMemo = operMemo;
    }

    public String getOperMemo() {
        return operMemo;
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
                .append("recordId", getRecordId())
                .append("currentId", getCurrentId())
                .append("currentNodeId", getCurrentNodeId())
                .append("operUserId", getOperUserId())
                .append("operType", getOperType())
                .append("operMemo", getOperMemo())
                .append("addTime", getAddTime())
                .append("state", getState())
                .toString();
    }

    public AuditFlowOperRecord(Integer currentId, Integer currentNodeId, Integer operUserId, Integer operType, String operMemo, Date addTime, Integer state) {
        this.currentId = currentId;
        this.currentNodeId = currentNodeId;
        this.operUserId = operUserId;
        this.operType = operType;
        this.operMemo = operMemo;
        this.addTime = addTime;
        this.state = state;


    }

    public AuditFlowOperRecord() {

    }

}

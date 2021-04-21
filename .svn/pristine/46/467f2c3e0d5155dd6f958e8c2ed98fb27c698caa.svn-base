package com.ruoyi.web.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 审批节点表 audit_flow_node
 *
 * @author ruoyi
 * @date 2019-07-31
 */
public class AuditFlowNode implements Serializable {

    private static final long serialVersionUID = 7100952524972963788L;
    /**
     * 节点id
     */
    private Integer nodeId;
    /**
     * 所属审批流程id
     */
    private Integer flowId;
    /**
     * 节点名称
     */
    private String nodeName;
    /**
     * 上一个节点id
     */
    private Integer prevNode;
    /**
     * 下一个节点id
     */
    private Integer nextNode;
    /**
     * 节点类型：1开始节点,2运行节点,3结束节点
     */
    private Integer nodeType;
    /**
     * 节点序号
     */
    private Integer nodeSeqno;
    /**
     * 添加时间
     */
    private Date addTime;
    /**
     * 状态：1有效，0无效，-1删除
     */
    private Integer state;
    private List<AuditFlowNodeRole> auditFlowNodeRole;

    public List<AuditFlowNodeRole> getAuditFlowNodeRole() {
        return auditFlowNodeRole;
    }

    public void setAuditFlowNodeRole(List<AuditFlowNodeRole> auditFlowNodeRole) {
        this.auditFlowNodeRole = auditFlowNodeRole;
    }

    public void setNodeId(Integer nodeId) {
        this.nodeId = nodeId;
    }

    public Integer getNodeId() {
        return nodeId;
    }

    public void setFlowId(Integer flowId) {
        this.flowId = flowId;
    }

    public Integer getFlowId() {
        return flowId;
    }

    public void setNodeName(String nodeName) {
        this.nodeName = nodeName;
    }

    public String getNodeName() {
        return nodeName;
    }

    public void setPrevNode(Integer prevNode) {
        this.prevNode = prevNode;
    }

    public Integer getPrevNode() {
        return prevNode;
    }

    public void setNextNode(Integer nextNode) {
        this.nextNode = nextNode;
    }

    public Integer getNextNode() {
        return nextNode;
    }

    public void setNodeType(Integer nodeType) {
        this.nodeType = nodeType;
    }

    public Integer getNodeType() {
        return nodeType;
    }

    public void setNodeSeqno(Integer nodeSeqno) {
        this.nodeSeqno = nodeSeqno;
    }

    public Integer getNodeSeqno() {
        return nodeSeqno;
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
                .append("nodeId", getNodeId())
                .append("flowId", getFlowId())
                .append("nodeName", getNodeName())
                .append("prevNode", getPrevNode())
                .append("nextNode", getNextNode())
                .append("nodeType", getNodeType())
                .append("nodeSeqno", getNodeSeqno())
                .append("addTime", getAddTime())
                .append("state", getState())
                .toString();
    }
}

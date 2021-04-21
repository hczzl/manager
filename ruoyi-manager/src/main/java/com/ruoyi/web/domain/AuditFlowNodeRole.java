package com.ruoyi.web.domain;

import com.ruoyi.system.domain.SysUser;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 审批角色验证表 audit_flow_node_role
 * 
 * @author ruoyi
 * @date 2019-07-31
 */
public class AuditFlowNodeRole implements Serializable
{

	private static final long serialVersionUID = 8745876152674443890L;
	/** id */
	private Integer nodeRoleId;
	/** 审批流程节点id */
	private Integer nodeId;
	/** 角色id */
	private Integer roleId;
	/** 添加时间 */
	private Date addTime;
	/** 状态：1有效，0无效，-1删除 */
	private Integer state;
	/** 状态：1有效，0无效，-1删除 */
	private Integer userId;
	/** 状态：1有效，0无效，-1删除 */
	private Integer currentId;

	/** 审核类型 */
	private String searchFlowId ;
    private List<SysUser> userList;
	private String	userName;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setNodeRoleId(Integer nodeRoleId)
	{
		this.nodeRoleId = nodeRoleId;
	}

	public Integer getNodeRoleId() 
	{
		return nodeRoleId;
	}
	public void setNodeId(Integer nodeId) 
	{
		this.nodeId = nodeId;
	}

	public Integer getNodeId() 
	{
		return nodeId;
	}
	public void setRoleId(Integer roleId) 
	{
		this.roleId = roleId;
	}

	public Integer getRoleId() 
	{
		return roleId;
	}
	public void setAddTime(Date addTime) 
	{
		this.addTime = addTime;
	}

	public Date getAddTime() 
	{
		return addTime;
	}
	public void setState(Integer state) 
	{
		this.state = state;
	}

	public Integer getState() 
	{
		return state;
	}

	public String getSearchFlowId() {
		return searchFlowId;
	}

	public void setSearchFlowId(String searchFlowId) {
		this.searchFlowId = searchFlowId;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public Integer getCurrentId() {
		return currentId;
	}

	public void setCurrentId(Integer currentId) {
		this.currentId = currentId;
	}

	public List<SysUser> getUserList() {
		return userList;
	}

	public void setUserList(List<SysUser> userList) {
		this.userList = userList;
	}
	@Override
	public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("nodeRoleId", getNodeRoleId())
            .append("nodeId", getNodeId())
            .append("roleId", getRoleId())
            .append("addTime", getAddTime())
            .append("state", getState())
            .toString();
    }


	public AuditFlowNodeRole(Integer nodeId,Integer userId, Date addTime,Integer currentId, Integer state ) {
		this.nodeId = nodeId;
		this.addTime = addTime;
		this.state = state;
		this.userId = userId;
		this.currentId = currentId;
	}

	public AuditFlowNodeRole() {

	}
}

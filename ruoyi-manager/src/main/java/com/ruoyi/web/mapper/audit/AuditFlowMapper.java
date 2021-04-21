package com.ruoyi.web.mapper.audit;

import com.ruoyi.web.domain.AuditFlow;

import java.util.List;

/**
 * 审批类别 数据层
 * 
 * @author ruoyi
 * @date 2019-07-31
 */
public interface AuditFlowMapper
{
	/**
     * 查询审批类别信息
     * 
     * @param flowId 审批类别ID
     * @return 审批类别信息
     */
	 AuditFlow selectAuditFlowById(Integer flowId);
	
	/**
     * 查询审批类别列表
     * 
     * @param auditFlow 审批类别信息
     * @return 审批类别集合
     */
	 List<AuditFlow> selectAuditFlowList(AuditFlow auditFlow);
	
	/**
     * 新增审批类别
     * 
     * @param auditFlow 审批类别信息
     * @return 结果
     */
	 int insertAuditFlow(AuditFlow auditFlow);
	
	/**
     * 修改审批类别
     * 
     * @param auditFlow 审批类别信息
     * @return 结果
     */
	 int updateAuditFlow(AuditFlow auditFlow);
	
	/**
     * 删除审批类别
     * 
     * @param flowId 审批类别ID
     * @return 结果
     */
	 int deleteAuditFlowById(Integer flowId);
	
	/**
     * 批量删除审批类别
     * 
     * @param flowIds 需要删除的数据ID
     * @return 结果
     */
	 int deleteAuditFlowByIds(String[] flowIds);

	/**
	 * 查询审批大类
	 * @param auditFlow
	 * @return
	 */
    List<AuditFlow> selectTypeFlowList(AuditFlow auditFlow);
}
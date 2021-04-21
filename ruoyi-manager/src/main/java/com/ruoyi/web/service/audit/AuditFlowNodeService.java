package com.ruoyi.web.service.audit;

import com.ruoyi.web.domain.AuditFlowNode;

import java.util.List;

/**
 * 审批节点 服务层
 *
 * @author ruoyi
 * @date 2019-07-31
 */
public interface AuditFlowNodeService {
    /**
     * 查询审批节点信息
     *
     * @param nodeId 审批节点ID
     * @return 审批节点信息
     */
    AuditFlowNode selectAuditFlowNodeById(Integer nodeId);

    /**
     * 查询审批节点列表
     *
     * @param auditFlowNode 审批节点信息
     * @return 审批节点集合
     */
    List<AuditFlowNode> selectAuditFlowNodeList(AuditFlowNode auditFlowNode);

    /**
     * 新增审批节点
     *
     * @param auditFlowNode 审批节点信息
     * @return 结果
     */
    int insertAuditFlowNode(AuditFlowNode auditFlowNode);

    /**
     * 修改审批节点
     *
     * @param auditFlowNode 审批节点信息
     * @return 结果
     */
    int updateAuditFlowNode(AuditFlowNode auditFlowNode);

    /**
     * 删除审批节点信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAuditFlowNodeByIds(String ids);

    /**
     * 查找最大的node_id
     *
     * @return
     */
    Integer selectMaxNodeId();

    /**
     * 根据flowId得到最大的node_id
     *
     * @param flowId
     * @return
     */
    Integer selectMaxNodeIdByFlowId(int flowId);

    /**
     * 获取特定的节点信息
     *
     * @param auditFlowNode
     * @return
     */
    List<AuditFlowNode> selectFlowId(AuditFlowNode auditFlowNode);

    /**
     * 获得node_id
     *
     * @param auditFlowNode
     * @return
     */
    Integer selectNodeIdBySepno(AuditFlowNode auditFlowNode);
}

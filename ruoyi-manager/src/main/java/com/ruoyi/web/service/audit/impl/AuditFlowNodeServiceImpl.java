package com.ruoyi.web.service.audit.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.web.domain.AuditFlowNode;
import com.ruoyi.web.mapper.audit.AuditFlowNodeMapper;
import com.ruoyi.web.service.audit.AuditFlowNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批节点 服务层实现
 *
 * @author ruoyi
 * @date 2019-07-31
 */
@Service
public class AuditFlowNodeServiceImpl implements AuditFlowNodeService {
    @Autowired
    private AuditFlowNodeMapper auditFlowNodeMapper;

    /**
     * 查询审批节点信息
     *
     * @param nodeId 审批节点ID
     * @return 审批节点信息
     */
    @Override
    public AuditFlowNode selectAuditFlowNodeById(Integer nodeId) {
        return auditFlowNodeMapper.selectAuditFlowNodeById(nodeId);
    }

    /**
     * 查询审批节点列表
     *
     * @param auditFlowNode 审批节点信息
     * @return 审批节点集合
     */
    @Override
    public List<AuditFlowNode> selectAuditFlowNodeList(AuditFlowNode auditFlowNode) {
        return auditFlowNodeMapper.selectAuditFlowNodeList(auditFlowNode);
    }

    /**
     * 新增审批节点
     *
     * @param auditFlowNode 审批节点信息
     * @return 结果
     */
    @Override
    public int insertAuditFlowNode(AuditFlowNode auditFlowNode) {
        return auditFlowNodeMapper.insertAuditFlowNode(auditFlowNode);
    }

    /**
     * 修改审批节点
     *
     * @param auditFlowNode 审批节点信息
     * @return 结果
     */
    @Override
    public int updateAuditFlowNode(AuditFlowNode auditFlowNode) {
        return auditFlowNodeMapper.updateAuditFlowNode(auditFlowNode);
    }

    /**
     * 删除审批节点对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAuditFlowNodeByIds(String ids) {
        return auditFlowNodeMapper.deleteAuditFlowNodeByIds(Convert.toStrArray(ids));
    }

    /**
     * 查找最大的node_id
     *
     * @return
     */
    @Override
    public Integer selectMaxNodeId() {
        return auditFlowNodeMapper.selectMaxNodeId();
    }

    /**
     * 根据flowId得到最大的node_id
     *
     * @param flowId
     * @return
     */
    @Override
    public Integer selectMaxNodeIdByFlowId(int flowId) {
        return auditFlowNodeMapper.selectMaxNodeIdByFlowId(flowId);
    }

    /**
     * 获取特定的节点信息
     *
     * @param auditFlowNode
     * @return
     */
    @Override
    public List<AuditFlowNode> selectFlowId(AuditFlowNode auditFlowNode) {
        return auditFlowNodeMapper.selectFlowId(auditFlowNode);
    }

    /**
     * 获得node_id
     *
     * @param auditFlowNode
     * @return
     */
    @Override
    public Integer selectNodeIdBySepno(AuditFlowNode auditFlowNode) {
        return auditFlowNodeMapper.selectNodeIdBySepno(auditFlowNode);
    }
}

package com.ruoyi.web.service.audit.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.web.domain.AuditFlow;
import com.ruoyi.web.mapper.audit.AuditFlowMapper;
import com.ruoyi.web.service.audit.AuditFlowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批类别 服务层实现
 *
 * @author ruoyi
 * @date 2019-07-31
 */
@Service
public class AuditFlowServiceImpl implements AuditFlowService {
    @Autowired
    private AuditFlowMapper auditFlowMapper;

    /**
     * 查询审批类别信息
     *
     * @param flowId 审批类别ID
     * @return 审批类别信息
     */
    @Override
    public AuditFlow selectAuditFlowById(Integer flowId) {
        return auditFlowMapper.selectAuditFlowById(flowId);
    }

    /**
     * 查询审批类别列表
     *
     * @param auditFlow 审批类别信息
     * @return 审批类别集合
     */
    @Override
    public List<AuditFlow> selectAuditFlowList(AuditFlow auditFlow) {
        return auditFlowMapper.selectAuditFlowList(auditFlow);
    }

    /**
     * 新增审批类别
     *
     * @param auditFlow 审批类别信息
     * @return 结果
     */
    @Override
    public int insertAuditFlow(AuditFlow auditFlow) {
        return auditFlowMapper.insertAuditFlow(auditFlow);
    }

    /**
     * 修改审批类别
     *
     * @param auditFlow 审批类别信息
     * @return 结果
     */
    @Override
    public int updateAuditFlow(AuditFlow auditFlow) {
        return auditFlowMapper.updateAuditFlow(auditFlow);
    }

    /**
     * 删除审批类别对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAuditFlowByIds(String ids) {
        return auditFlowMapper.deleteAuditFlowByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<AuditFlow> selectTypeFlowList(AuditFlow auditFlow) {
        return auditFlowMapper.selectTypeFlowList(auditFlow);
    }

}

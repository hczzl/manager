package com.ruoyi.web.service.audit.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.web.domain.AuditFlowOperRecord;
import com.ruoyi.web.mapper.audit.AuditFlowOperRecordMapper;
import com.ruoyi.web.service.audit.AuditFlowOperRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 审批记录 服务层实现
 *
 * @author ruoyi
 * @date 2019-08-01
 */
@Service
public class AuditFlowOperRecordServiceImpl implements AuditFlowOperRecordService {
    @Autowired
    private AuditFlowOperRecordMapper auditFlowOperRecordMapper;

    /**
     * 查询审批记录信息
     *
     * @param recordId 审批记录ID
     * @return 审批记录信息
     */
    @Override
    public AuditFlowOperRecord selectAuditFlowOperRecordById(Integer recordId) {
        return auditFlowOperRecordMapper.selectAuditFlowOperRecordById(recordId);
    }

    /**
     * 查询审批记录列表
     *
     * @param auditFlowOperRecord 审批记录信息
     * @return 审批记录集合
     */
    @Override
    public List<AuditFlowOperRecord> selectAuditFlowOperRecordList(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.selectAuditFlowOperRecordList(auditFlowOperRecord);
    }

    /**
     * 新增审批记录
     *
     * @param auditFlowOperRecord 审批记录信息
     * @return 结果
     */
    @Override
    public int insertAuditFlowOperRecord(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.insertAuditFlowOperRecord(auditFlowOperRecord);
    }

    /**
     * 修改审批记录
     *
     * @param auditFlowOperRecord 审批记录信息
     * @return 结果
     */
    @Override
    public int updateAuditFlowOperRecord(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.updateAuditFlowOperRecord(auditFlowOperRecord);
    }

    /**
     * 删除审批记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAuditFlowOperRecordByIds(String ids) {
        return auditFlowOperRecordMapper.deleteAuditFlowOperRecordByIds(Convert.toStrArray(ids));
    }

    /**
     * 查询审批创建表记录
     *
     * @param auditFlowOperRecord
     * @return
     */
    @Override
    public List<AuditFlowOperRecord> getAllAuditFlowOperRecord(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.getAllAuditFlowOperRecord(auditFlowOperRecord);
    }

    /**
     * 降序获得current_id
     *
     * @param auditFlowOperRecord
     * @return
     */
    @Override
    public List<AuditFlowOperRecord> selectAuditOperRecord(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.selectAuditOperRecord(auditFlowOperRecord);
    }

    @Override
    public Integer selectAuditFlowOperRecordSum(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.selectAuditFlowOperRecordSum(auditFlowOperRecord);
    }

    @Override
    public List<AuditFlowOperRecord> selectAuditOperRecordByProjectId(Integer pid) {
        return auditFlowOperRecordMapper.selectAuditOperRecordByProjectId(pid);
    }

    @Override
    public Integer deleteOperRecordByCurrentId(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.deleteOperRecordByCurrentId(auditFlowOperRecord);
    }

    @Override
    public List<AuditFlowOperRecord> selectOperType(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.selectOperType(auditFlowOperRecord);
    }

    @Override
    public List<AuditFlowOperRecord> selectInfoByOperType(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.selectInfoByOperType(auditFlowOperRecord);
    }

    @Override
    public List<AuditFlowOperRecord> selectOperTypeInfo(AuditFlowOperRecord auditFlowOperRecord) {
        return auditFlowOperRecordMapper.selectOperTypeInfo(auditFlowOperRecord);
    }
}

package com.ruoyi.web.service.audit;

import com.ruoyi.web.domain.AuditFlowOperRecord;

import java.util.List;

/**
 * 审批记录 服务层
 *
 * @author ruoyi
 * @date 2019-08-01
 */
public interface AuditFlowOperRecordService {
    /**
     * 查询审批记录信息
     *
     * @param recordId 审批记录ID
     * @return 审批记录信息
     */
    AuditFlowOperRecord selectAuditFlowOperRecordById(Integer recordId);

    /**
     * 查询审批记录列表
     *
     * @param auditFlowOperRecord 审批记录信息
     * @return 审批记录集合
     */
    List<AuditFlowOperRecord> selectAuditFlowOperRecordList(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * 新增审批记录
     *
     * @param auditFlowOperRecord 审批记录信息
     * @return 结果
     */
    int insertAuditFlowOperRecord(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * 修改审批记录
     *
     * @param auditFlowOperRecord 审批记录信息
     * @return 结果
     */
    int updateAuditFlowOperRecord(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * 删除审批记录信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAuditFlowOperRecordByIds(String ids);

    /**
     * 查询审批创建表记录
     */
    List<AuditFlowOperRecord> getAllAuditFlowOperRecord(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * 降序获得current_id
     *
     * @return
     */
    List<AuditFlowOperRecord> selectAuditOperRecord(AuditFlowOperRecord auditFlowOperRecord);

    Integer selectAuditFlowOperRecordSum(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * 根据项目ID返回审批内容
     *
     * @param pid
     * @return
     */
    List<AuditFlowOperRecord> selectAuditOperRecordByProjectId(Integer pid);

    Integer deleteOperRecordByCurrentId(AuditFlowOperRecord auditFlowOperRecord);


    List<AuditFlowOperRecord> selectOperType(AuditFlowOperRecord auditFlowOperRecord);

    List<AuditFlowOperRecord> selectInfoByOperType(AuditFlowOperRecord auditFlowOperRecord);

    List<AuditFlowOperRecord> selectOperTypeInfo(AuditFlowOperRecord auditFlowOperRecord);
}

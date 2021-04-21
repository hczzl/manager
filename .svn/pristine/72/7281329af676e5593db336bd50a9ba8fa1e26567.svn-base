package com.ruoyi.web.mapper.audit;

import com.ruoyi.web.domain.AuditFlowOperRecord;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 审批记录 数据层
 *
 * @author ruoyi
 * @date 2019-08-01
 */
@Component
public interface AuditFlowOperRecordMapper {
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
     * 删除审批记录
     *
     * @param recordId 审批记录ID
     * @return 结果
     */
    int deleteAuditFlowOperRecordById(Integer recordId);

    /**
     * 批量删除审批记录
     *
     * @param recordIds 需要删除的数据ID
     * @return 结果
     */
    int deleteAuditFlowOperRecordByIds(String[] recordIds);


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

    /**
     * @param auditFlowOperRecord
     * @return
     */
    Integer selectAuditFlowOperRecordSum(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * 根据项目ID返回审批内容
     *
     * @param pid
     * @return
     */
    List<AuditFlowOperRecord> selectAuditOperRecordByProjectId(Integer pid);

    /**
     * @param auditFlowOperRecord
     * @return
     */
    Integer deleteOperRecordByCurrentId(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * @param auditFlowOperRecord
     * @return
     */
    List<AuditFlowOperRecord> selectOperType(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * @param auditFlowOperRecord
     * @return
     */
    List<AuditFlowOperRecord> selectInfoByOperType(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * @param auditFlowOperRecord
     * @return
     */
    List<AuditFlowOperRecord> selectOperTypeInfo(AuditFlowOperRecord auditFlowOperRecord);

    /**
     * 获取评分意见
     *
     * @param map
     * @return
     */
    String selectMemo(Map map);

    /**
     * 获取长度
     *
     * @param vo
     * @return
     */
    Integer selectAuditFlowOperRecordListCount(AuditFlowOperRecord vo);

    /**
     * @param vo
     * @return
     */
    Integer selectAuditOperRecordCount(AuditFlowOperRecord vo);

    /**
     * @param vo
     * @return
     */
    List<Map<String, Object>> selectOperaRecord(AuditFlowOperRecord vo);

    /**
     * @param currentId
     * @param currentNodeId
     * @param operUserId
     * @return
     */
    Date selectApprovalTime(@Param("currentId") Integer currentId, @Param("currentNodeId") Integer currentNodeId, @Param("operUserId") Integer operUserId);

    /**
     * @param currentId
     * @return
     */
    String selectApprovalMemo(Integer currentId);
}
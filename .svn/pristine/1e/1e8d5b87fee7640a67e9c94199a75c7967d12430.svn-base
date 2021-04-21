package com.ruoyi.web.mapper.audit;

import com.ruoyi.web.domain.AuditFlowCurrent;
import com.ruoyi.web.domain.SysProjectTable;
import com.ruoyi.web.domain.TaskPostpone;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 审批创建记录 数据层
 *
 * @author ruoyi
 * @date 2019-08-01
 */
@Component
public interface AuditFlowCurrentMapper {
    /**
     * 查询审批创建记录信息
     *
     * @param currentId 审批创建记录ID
     * @return 审批创建记录信息
     */
    AuditFlowCurrent selectAuditFlowCurrentById(Integer currentId);

    /**
     * 查询审批创建记录列表
     *
     * @param auditFlowCurrent 审批创建记录信息
     * @return 审批创建记录集合
     */
    List<AuditFlowCurrent> selectAuditFlowCurrentList(AuditFlowCurrent auditFlowCurrent);

    /**
     * 新增审批创建记录
     *
     * @param auditFlowCurrent 审批创建记录信息
     * @return 结果
     */
    int insertAuditFlowCurrent(AuditFlowCurrent auditFlowCurrent);

    /**
     * 修改审批创建记录
     *
     * @param auditFlowCurrent 审批创建记录信息
     * @return 结果
     */
    int updateAuditFlowCurrent(AuditFlowCurrent auditFlowCurrent);

    /**
     * 删除审批创建记录
     *
     * @param currentId 审批创建记录ID
     * @return 结果
     */
    int deleteAuditFlowCurrentById(Integer currentId);

    /**
     * 批量删除审批创建记录
     *
     * @param currentIds 需要删除的数据ID
     * @return 结果
     */
    int deleteAuditFlowCurrentByIds(String[] currentIds);

    /**
     * @param sysProjectTable
     * @return
     */
    int insertAuditFlowCurrentProject(SysProjectTable sysProjectTable);

    /**
     * 提交项目中止审批方法
     *
     * @param sysProjectTable
     * @return
     */
    int updateAuditFlowCurrentProject(SysProjectTable sysProjectTable);

    /**
     * 查找审批流列表
     *
     * @param currentId
     * @return
     */
    Integer getFlowCurrentNodeId(Integer currentId);

    List<AuditFlowCurrent> selectCurrentId(AuditFlowCurrent auditFlowCurrent);

    /**
     * 根据当前节点得到相应的current_state
     *
     * @param auditFlowCurrent
     * @return
     */
    List<AuditFlowCurrent> getFlowCurrentStatre(AuditFlowCurrent auditFlowCurrent);

    /**
     * 获得audit_id
     *
     * @param auditFlowCurrent
     * @return
     */
    List<AuditFlowCurrent> selectAuditForApplyId(AuditFlowCurrent auditFlowCurrent);

    /**
     * 获得apply_id
     *
     * @param auditFlowCurrent
     * @return
     */
    List<AuditFlowCurrent> selectAllApplyId(AuditFlowCurrent auditFlowCurrent);

    /**
     * 根据审批流id，查询相应审批单信息
     *
     * @param auditFlowCurrent
     * @return
     */
    List<AuditFlowCurrent> selectAuditFlowCurrentList1(AuditFlowCurrent auditFlowCurrent);

    /**
     * 获得applyid
     *
     * @param id
     * @return
     */
    Integer selectApplyIdByCurrentId(int id);

    /**
     * @param auditFlowCurrent
     * @return
     */
    List<AuditFlowCurrent> selectAuditIdByCurrentId(AuditFlowCurrent auditFlowCurrent);

    /**
     * 获得审批状态
     *
     * @param auditFlowCurrent
     * @return
     */
    Integer selectCurrentState(AuditFlowCurrent auditFlowCurrent);

    /**
     * @param taskPostpone
     * @return
     */
    int insertAuditFlowCurrentsForPone(TaskPostpone taskPostpone);

    /**
     * @param auditFlowCurrent
     * @return
     */
    Integer deleteAuditFlowCurrentById(AuditFlowCurrent auditFlowCurrent);

    /**
     * @param auditFlowCurrent
     * @return
     */
    List<AuditFlowCurrent> selectCurrentInfo(AuditFlowCurrent auditFlowCurrent);

    /**
     * @param currentId
     * @return
     */
    Map<String, Object> selectList(Integer currentId);

    /**
     * @param vo
     * @return
     */
    List<Map<String, Object>> selectFlowCurrent(AuditFlowCurrent vo);

    /**
     * @param map
     * @return
     */
    int updateCurrentState(Map<String, Object> map);

    /**
     * @param currentId
     * @return
     */
    int deleteMultipleApproval(Integer currentId);

    /**
     * @param currentId
     * @return
     */
    Map<String, Object> selectFlowCurrentMap(Integer currentId);

    /**
     * @param auditId
     * @param applyId
     * @return
     */
    Map<String, Object> selectBuinessId(@Param("auditId") Integer auditId, @Param("applyId") Integer applyId);

    /**
     * @param currentId
     */
    void deleteMultipleTable(Integer currentId);

    /**
     * @param applyId
     * @param auditId
     * @param currentState
     * @return
     */
    Long selectCount(@Param("applyId") Integer applyId, @Param("auditId") Integer auditId, @Param("currentState") Integer currentState);

}
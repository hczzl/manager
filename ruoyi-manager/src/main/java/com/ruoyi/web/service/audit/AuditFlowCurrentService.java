package com.ruoyi.web.service.audit;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.web.domain.AuditFlowCurrent;
import com.ruoyi.web.domain.SysProjectTable;
import com.ruoyi.web.domain.TaskPostpone;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.domain.vo.AuditFlowCrurrentVo;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.domain.ResultAudit;

import java.util.List;
import java.util.Map;

/**
 * 审批创建记录 服务层
 *
 * @author ruoyi
 * @date 2019-08-01
 */
public interface AuditFlowCurrentService {
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
     * 根据审批流id，查询相应审批单信息
     *
     * @param auditFlowCurrent
     * @return
     */
    List<AuditFlowCurrent> selectAuditFlowCurrentList1(AuditFlowCurrent auditFlowCurrent);

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
     * 删除审批创建记录信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAuditFlowCurrentByIds(String ids);

    /**
     * @param taskTable
     * @return
     */
    int insertAuditFlowCurrents(TaskTable taskTable);

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
     */
    Integer getFlowCurrentNodeId(int currentId);

    /**
     * 根据当前节点得到相应的current_state
     */
    List<AuditFlowCurrent> getFlowCurrentStatre(AuditFlowCurrent auditFlowCurrent);

    /**
     * @param auditFlowCurrentd
     * @return
     */
    List<AuditFlowCurrent> selectCurrentId(AuditFlowCurrent auditFlowCurrentd);

    /**
     * 获得audit_id
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
    AjaxResult revokeApproval(Integer currentId);

    /**
     * 查询审批创建记录列表
     *
     * @param auditFlowCurrent
     * @return
     */
    List<ResultAudit> list(AuditFlowCurrent auditFlowCurrent);

    /**
     * 查询任务模块中已经审批的记录
     *
     * @param auditFlowCurrent
     * @return
     */
    List<ResultAudit> commissionlistByTid(AuditFlowCurrent auditFlowCurrent);

    /**
     * 查询项目模块中的已经审批的记录
     *
     * @param auditFlowCurrent
     * @return
     */
    List<ResultAudit> commissionlistByProjectId(AuditFlowCurrent auditFlowCurrent);

    /**
     * 根据审批流id查询对应的审批信息
     *
     * @param auditFlowCurrent
     * @return
     */
    List<ResultAudit> listCurrentId(AuditFlowCurrent auditFlowCurrent);

    /**
     * 对结果集list进行分页
     *
     * @param auditFlowCurrent
     * @param list
     * @return
     */
    List<ResultAudit> paging(AuditFlowCurrent auditFlowCurrent, List<ResultAudit> list);

    /**
     * @param list
     * @param auditFlowCurrent
     * @return
     */
    List<ResultAudit> selectUserNameBycId(List<ResultAudit> list, AuditFlowCurrent auditFlowCurrent);

    /**
     * @param resultAudit
     * @return
     */
    String copyName(ResultAudit resultAudit);

    /**
     * 查询审批列表
     *
     * @param auditFlowCurrent
     * @return
     */
    PageEntity selectApplyList(AuditFlowCurrent auditFlowCurrent);

    /**
     * @param currentId
     * @return
     */
    List<String> selectApplyUserIdList(Integer currentId);

    /**
     * @param vo
     * @return
     */
    AjaxResult selectApprovalDetail(AuditFlowCrurrentVo vo);

    /**
     * 审批单删除操作
     *
     * @param currentId
     * @return
     */
    int deleteApprovalByCurrentId(Integer currentId);


}

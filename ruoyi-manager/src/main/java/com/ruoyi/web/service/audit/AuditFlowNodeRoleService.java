package com.ruoyi.web.service.audit;

import com.ruoyi.web.domain.*;

import java.util.List;

/**
 * 审批角色验证 服务层
 *
 * @author ruoyi
 * @date 2019-07-31
 */
public interface AuditFlowNodeRoleService {
    /**
     * 查询审批角色验证信息
     *
     * @param nodeRoleId 审批角色验证ID
     * @return 审批角色验证信息
     */
    AuditFlowNodeRole selectAuditFlowNodeRoleById(Integer nodeRoleId);

    /**
     * 查询审批角色验证列表
     *
     * @param auditFlowNodeRole 审批角色验证信息
     * @return 审批角色验证集合
     */
    List<AuditFlowNodeRole> selectAuditFlowNodeRoleList(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 查询具有审核权限的列表
     *
     * @param auditFlowNodeRole 审批角色验证信息
     * @return 审批角色验证集合
     */
    List<AuditFlowNodeRole> selectFlowNodeRoleList(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 新增审批角色验证
     *
     * @param auditFlowNodeRole 审批角色验证信息
     * @return 结果
     */
    int insertAuditFlowNodeRole(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 修改审批角色验证
     *
     * @param auditFlowNodeRole 审批角色验证信息
     * @return 结果
     */
    int updateAuditFlowNodeRole(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 修改审批角色验证,根据当前几点 node_id
     *
     * @param auditFlowNodeRole
     * @return
     */
    int updateAuditFlowNodeRoleByNodeId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 删除审批角色验证信息
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteAuditFlowNodeRoleByIds(String ids);

    /**
     * @param auditFlowCurrent
     * @return
     */
    int insertAuditFlowNodeRoles(AuditFlowCurrent auditFlowCurrent);

    /**
     * @param auditFlowNodeRole
     * @return
     */
    List<AuditFlowNodeRole> selectAuditFlowNodeRoleLists(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据current_id来查找user_id
     */
    List<AuditFlowNodeRole> getAllAuditFlowNodeRole(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 计算该审批是几级审批
     */
    int getSumApproval(int currentId);

    /**
     * 跟据node_id，计算需要提醒人多少次
     */
    int getSumNodeId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据node_id得到user_id
     */
    List<AuditFlowNodeRole> getUserIdByNodeId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据current_id得到user_id
     */
    int getUserIdByCurrentId(int currentId, int state);

    /**
     * 获得唯一的current_id
     */
    List<AuditFlowNodeRole> getAllCurrentId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据current_id和userId，判断state
     */
    List<AuditFlowNodeRole> getStateByUserId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据current_id获得所有的用户
     */
    List<AuditFlowNodeRole> getAllUserByCurretnId(AuditFlowNodeRole auditFlowNodeRole);


    List<AuditFlowNodeRole> selectByNodeIdAndCurrentId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 获得node=4的个数
     *
     * @param auditFlowNodeRole
     * @return
     */
    Integer selectSumCurrentNodeId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * @param auditFlowNodeRole
     * @return
     */
    Integer selectAuditFlowNodeRoleListSum(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据ProjectId返回审批人ID
     *
     * @param pid
     * @return
     */
    List<Integer> getUserIdByProjectId(Integer pid);

    /**
     * @param auditFlowNodeRole
     * @return
     */
    Integer deleteFlowNodeRoleByCurrentId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * @param auditFlowNodeRole
     * @return
     */
    List<AuditFlowNodeRole> selectUsers(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据项目ID获取状态为1(审批中)的审批单
     *
     * @param pid
     * @return
     */
    List<AuditFlowNodeRole> selectAuditInfoStateInOneByProjectId(Integer pid);

    /**
     * @param applyId
     * @return
     */
    List<AuditFlowNodeRole> selectUserIds(Integer applyId);
}

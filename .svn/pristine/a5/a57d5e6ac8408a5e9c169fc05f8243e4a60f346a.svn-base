package com.ruoyi.web.mapper.audit;

import com.ruoyi.web.domain.AuditFlowNodeRole;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import javax.swing.*;
import java.util.List;
import java.util.Map;

/**
 * 审批角色验证 数据层
 *
 * @author ruoyi
 * @date 2019-07-31
 */
@Component
public interface AuditFlowNodeRoleMapper {
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
     * selectFlowNodeRoleList
     * 查询具有审批权限的角色列表
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
     * 删除审批角色验证
     *
     * @param nodeRoleId 审批角色验证ID
     * @return 结果
     */
    int deleteAuditFlowNodeRoleById(Integer nodeRoleId);

    /**
     * 批量删除审批角色验证
     *
     * @param nodeRoleIds 需要删除的数据ID
     * @return 结果
     */
    int deleteAuditFlowNodeRoleByIds(String[] nodeRoleIds);

    /**
     * @param auditFlowNodeRole
     * @return
     */
    List<AuditFlowNodeRole> selectAuditFlowNodeRoleLists(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据current_id来查找user_id
     *
     * @param auditFlowNodeRole
     * @return
     */
    List<AuditFlowNodeRole> getAllAuditFlowNodeRole(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 计算该审批是几级审批
     *
     * @param currentId
     * @return
     */
    int getSumApproval(int currentId);

    /**
     * 根据node_id得到user_id
     *
     * @param auditFlowNodeRole
     * @return
     */
    List<AuditFlowNodeRole> getUserIdByNodeId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据current_id得到user_id
     *
     * @param currentId
     * @param state
     * @return
     */
    int getUserIdByCurrentId(int currentId, int state);

    /**
     * 跟据node_id，计算需要提醒人多少次
     *
     * @param auditFlowNodeRole
     * @return
     */
    int getSumNodeId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 获得唯一的current_id
     *
     * @param auditFlowNodeRole
     * @return
     */
    List<AuditFlowNodeRole> getAllCurrentId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据current_id和userId，判断state
     *
     * @param auditFlowNodeRole
     * @return
     */
    List<AuditFlowNodeRole> getStateByUserId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据current_id获得所有的用户
     *
     * @param auditFlowNodeRole
     * @return
     */
    List<AuditFlowNodeRole> getAllUserByCurretnId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * @param auditFlowNodeRole
     * @return
     */
    List<AuditFlowNodeRole> selectByNodeIdAndCurrentId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 获得node=4的个数
     *
     * @param auditFlowNodeRole
     * @return
     */
    Integer selectSumCurrentNodeId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 获取某个审批节点的审批人数
     *
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
     * 删除审批信息
     *
     * @param auditFlowNodeRole
     * @return
     */
    Integer deleteFlowNodeRoleByCurrentId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 获取审批人
     *
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
     * 获取市场项目的审批信息
     *
     * @param applyId
     * @return
     */
    List<AuditFlowNodeRole> selectUserIds(Integer applyId);

    /**
     * 根据审批人获取用户的审批状态
     *
     * @param currentId
     * @return
     */
    List<Integer> selectState(Integer currentId);

    /**
     * 获取审批人列表
     *
     * @param auditFlowNodeRole
     * @return
     */
    List<Integer> selectApplyUserId(AuditFlowNodeRole auditFlowNodeRole);

    /**
     * 根据审批流id审批人以及审批节点
     *
     * @param currentId
     * @return
     */
    List<Map<String, Object>> selectUserNodeBycId(Integer currentId);

    /**
     * 获取审批人数
     *
     * @param vo
     * @return
     */
    Integer selectFlowNodeRoleCount(AuditFlowNodeRole vo);

    /**
     * 根据审批流id、节点id，审批状态获取审批人列表
     *
     * @param currentId
     * @param nodeId
     * @param state
     * @return
     */
    List<String> selectNodeRoleUserId(@Param("currentId") Integer currentId, @Param("nodeId") Integer nodeId, @Param("state") Integer state);

    /**
     * 获取审批状态
     *
     * @param currentId
     * @return
     */
    List<Integer> selectApplyState(Integer currentId);

}
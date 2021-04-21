package com.ruoyi.web.service.audit.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.web.domain.AuditFlowCurrent;
import com.ruoyi.web.domain.AuditFlowNodeRole;
import com.ruoyi.web.mapper.audit.AuditFlowNodeRoleMapper;
import com.ruoyi.web.service.audit.AuditFlowNodeRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * 审批角色验证 服务层实现
 *
 * @author ruoyi
 * @date 2019-07-31
 */
@Service
public class AuditFlowNodeRoleServiceImpl implements AuditFlowNodeRoleService {
    @Autowired
    private AuditFlowNodeRoleMapper auditFlowNodeRoleMapper;

    /**
     * 查询审批角色验证信息
     *
     * @param nodeRoleId 审批角色验证ID
     * @return 审批角色验证信息
     */
    @Override
    public AuditFlowNodeRole selectAuditFlowNodeRoleById(Integer nodeRoleId) {
        return auditFlowNodeRoleMapper.selectAuditFlowNodeRoleById(nodeRoleId);
    }

    /**
     * 查询审批角色验证列表
     *
     * @param auditFlowNodeRole 审批角色验证信息
     * @return 审批角色验证集合
     */
    @Override
    public List<AuditFlowNodeRole> selectAuditFlowNodeRoleList(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.selectAuditFlowNodeRoleList(auditFlowNodeRole);
    }

    @Override
    public List<AuditFlowNodeRole> selectFlowNodeRoleList(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.selectFlowNodeRoleList(auditFlowNodeRole);
    }

    /**
     * 新增审批角色验证
     *
     * @param auditFlowNodeRole 审批角色验证信息
     * @return 结果
     */
    @Override
    public int insertAuditFlowNodeRole(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.insertAuditFlowNodeRole(auditFlowNodeRole);
    }

    /**
     * 修改审批角色验证
     *
     * @param auditFlowNodeRole 审批角色验证信息
     * @return 结果
     */
    @Override
    public int updateAuditFlowNodeRole(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.updateAuditFlowNodeRole(auditFlowNodeRole);
    }

    /**
     * 删除审批角色验证对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAuditFlowNodeRoleByIds(String ids) {
        return auditFlowNodeRoleMapper.deleteAuditFlowNodeRoleByIds(Convert.toStrArray(ids));
    }
    @Override
    public int insertAuditFlowNodeRoles(AuditFlowCurrent auditFlowCurrent) {
        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
        auditFlowNodeRole.setCurrentId(auditFlowCurrent.getCurrentId());
        auditFlowNodeRole.setAddTime(new Date());
        auditFlowNodeRole.setState(1);
        auditFlowNodeRole.setUserId(auditFlowCurrent.getFirstUserId());
        auditFlowNodeRole.setNodeId(auditFlowCurrent.getCurrentNodeId());
        return auditFlowNodeRoleMapper.insertAuditFlowNodeRole(auditFlowNodeRole);

    }

    @Override
    public List<AuditFlowNodeRole> selectAuditFlowNodeRoleLists(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.selectAuditFlowNodeRoleLists(auditFlowNodeRole);
    }

    /**
     * 根据current_id来查找user_id
     *
     * @param auditFlowNodeRole
     * @return
     */
    @Override
    public List<AuditFlowNodeRole> getAllAuditFlowNodeRole(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.getAllAuditFlowNodeRole(auditFlowNodeRole);
    }

    /**
     * 修改审批角色验证,根据当前几点 node_id
     *
     * @param auditFlowNodeRole
     * @return
     */
    @Override
    public int updateAuditFlowNodeRoleByNodeId(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole);
    }

    /**
     * 计算该审批是几级审批
     *
     * @param currentId
     * @return
     */
    @Override
    public int getSumApproval(int currentId) {
        return auditFlowNodeRoleMapper.getSumApproval(currentId);
    }

    /**
     * 根据node_id得到user_id
     *
     * @param auditFlowNodeRole
     * @return
     */
    @Override
    public List<AuditFlowNodeRole> getUserIdByNodeId(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.getUserIdByNodeId(auditFlowNodeRole);
    }

    /**
     * 根据current_id得到user_id
     *
     * @param currentId
     * @param state
     * @return
     */
    @Override
    public int getUserIdByCurrentId(int currentId, int state) {
        return auditFlowNodeRoleMapper.getUserIdByCurrentId(currentId, state);
    }

    /**
     * 跟据node_id，计算需要提醒人多少次
     *
     * @param auditFlowNodeRole
     * @return
     */
    @Override
    public int getSumNodeId(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.getSumNodeId(auditFlowNodeRole);
    }

    /**
     * 获得唯一的current_id
     *
     * @param auditFlowNodeRole
     * @return
     */
    @Override
    public List<AuditFlowNodeRole> getAllCurrentId(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.getAllCurrentId(auditFlowNodeRole);
    }

    /**
     * 根据current_id和userId，判断state
     *
     * @param auditFlowNodeRole
     * @return
     */
    @Override
    public List<AuditFlowNodeRole> getStateByUserId(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.getStateByUserId(auditFlowNodeRole);
    }

    /**
     * 根据current_id获得所有的用户
     *
     * @param auditFlowNodeRole
     * @return
     */
    @Override
    public List<AuditFlowNodeRole> getAllUserByCurretnId(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.getAllUserByCurretnId(auditFlowNodeRole);
    }
    @Override
    public List<AuditFlowNodeRole> selectByNodeIdAndCurrentId(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.selectByNodeIdAndCurrentId(auditFlowNodeRole);
    }

    /**
     * 获得node=4的个数
     *
     * @param auditFlowNodeRole
     * @return
     */
    @Override
    public Integer selectSumCurrentNodeId(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.selectSumCurrentNodeId(auditFlowNodeRole);
    }

    @Override
    public Integer selectAuditFlowNodeRoleListSum(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.selectAuditFlowNodeRoleListSum(auditFlowNodeRole);
    }

    @Override
    public List<Integer> getUserIdByProjectId(Integer pid) {
        return auditFlowNodeRoleMapper.getUserIdByProjectId(pid);
    }

    @Override
    public Integer deleteFlowNodeRoleByCurrentId(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.deleteFlowNodeRoleByCurrentId(auditFlowNodeRole);
    }

    @Override
    public List<AuditFlowNodeRole> selectUsers(AuditFlowNodeRole auditFlowNodeRole) {
        return auditFlowNodeRoleMapper.selectUsers(auditFlowNodeRole);
    }

    @Override
    public List<AuditFlowNodeRole> selectAuditInfoStateInOneByProjectId(Integer pid) {
        return auditFlowNodeRoleMapper.selectAuditInfoStateInOneByProjectId(pid);
    }

    @Override
    public List<AuditFlowNodeRole> selectUserIds(Integer applyId) {
        return auditFlowNodeRoleMapper.selectUserIds(applyId);
    }
}

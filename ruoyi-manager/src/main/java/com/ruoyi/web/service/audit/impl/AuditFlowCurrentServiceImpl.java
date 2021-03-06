package com.ruoyi.web.service.audit.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.ApprovalTypes;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.domain.ResultApprovalRecord;
import com.ruoyi.web.domain.vo.AuditFlowCrurrentVo;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.domain.ResultAudit;
import com.ruoyi.web.mapper.audit.*;
import com.ruoyi.web.mapper.item.*;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.audit.AuditFlowNodeRoleService;
import com.ruoyi.web.service.audit.AuditFlowNodeService;
import com.ruoyi.web.service.audit.AuditFlowService;
import com.ruoyi.web.service.item.*;
import com.ruoyi.web.util.ConstantUtil;
import com.ruoyi.web.util.UserUtil;
import com.ruoyi.web.util.constant.ManagerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 审批创建记录 服务层实现,,即业务层
 *
 * @author ruoyiinserts
 * @date 2019-08-01
 */
@Service
public class AuditFlowCurrentServiceImpl implements AuditFlowCurrentService {
    @Autowired
    private AuditFlowCurrentMapper auditFlowCurrentMapper;
    @Autowired
    private AuditFlowNodeRoleService auditFlowNodeRoleService;
    @Autowired
    private AuditFlowNodeService auditFlowNodeService;
    @Autowired
    private ResultApprovalRecordService resultApprovalRecordService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;
    @Autowired
    private AuditFlowCurrentService auditFlowCurrentService;
    @Autowired
    private SysProjectTableService sysProjectTableService;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private TaskUserService taskUserService;
    @Autowired
    private AuditFlowNodeRoleMapper auditFlowNodeRoleMapper;
    @Autowired
    private AuditFlowService auditFlowService;
    @Autowired
    private AuditFlowOperRecordMapper auditFlowOperRecordMapper;
    @Autowired
    private AuditCopyMsgMapper auditCopyMsgMapper;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private TaskTableMapper taskTableMapper;
    @Autowired
    private SysProjectTableMapper sysProjectTableMapper;
    @Autowired
    private MsgEvtInfoMapper msgEvtInfoMapper;
    @Autowired
    private ParticipantsMapper participantsMapper;
    @Autowired
    private AuditFlowNodeMapper auditFlowNodeMapper;
    @Autowired
    private TaskStopMoneMapper taskStopMoneMapper;
    @Autowired
    private TaskPostponeMapper taskPostponeMapper;

    /**
     * 查询审批创建记录信息
     *
     * @param currentId 审批创建记录ID
     * @return 审批创建记录信息
     */
    @Override
    public AuditFlowCurrent selectAuditFlowCurrentById(Integer currentId) {
        return auditFlowCurrentMapper.selectAuditFlowCurrentById(currentId);
    }

    /**
     * 查询审批创建记录列表
     *
     * @param auditFlowCurrent 审批创建记录信息
     * @return 审批创建记录集合
     */
    @Override
    public List<AuditFlowCurrent> selectAuditFlowCurrentList(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.selectAuditFlowCurrentList(auditFlowCurrent);
    }

    /**
     * 新增审批创建记录
     *
     * @param auditFlowCurrent 审批创建记录信息
     * @return 结果
     */
    @Override
    public int insertAuditFlowCurrent(AuditFlowCurrent auditFlowCurrent) {
        auditFlowCurrent.setAddTime(new Date());
        auditFlowCurrent.setCurrentState(1);
        auditFlowCurrent.setState(1);
        return auditFlowCurrentMapper.insertAuditFlowCurrent(auditFlowCurrent);
    }

    /**
     * 修改审批创建记录
     *
     * @param auditFlowCurrent 审批创建记录信息
     * @return 结果
     */
    @Override
    public int updateAuditFlowCurrent(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.updateAuditFlowCurrent(auditFlowCurrent);
    }

    /**
     * 删除审批创建记录对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteAuditFlowCurrentByIds(String ids) {
        return auditFlowCurrentMapper.deleteAuditFlowCurrentByIds(Convert.toStrArray(ids));
    }

    /**
     * 任务延期，变更、顺延的添加审核人
     *
     * @param taskPostpone
     * @return
     */
    @Override
    public int insertAuditFlowCurrentsForPone(TaskPostpone taskPostpone) {
        // 创建审批流记录类对象
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        // 以下是将信息插入到audit_flow_current表中
        auditFlowCurrent.setAddTime(new Date());// 插入数据的时间
        SysUser sysUser = UserUtil.getUser();
        auditFlowCurrent.setCreateBy(sysUser.getUserId().intValue() + "");// 审批创建人，即审批提交人
        auditFlowCurrent.setCurrentState(1);
        auditFlowCurrent.setState(1);
        auditFlowCurrent.setCurrentState(2);
        // apply_id是插入实体类的id ,比如任务表的id
        auditFlowCurrent.setApplyId(Math.toIntExact(taskPostpone.getId()));
        // audit_id是所属审批流程id,即audit_flow表的flow_id字段，手动输入测试
        auditFlowCurrent.setAuditId(StringUtils.toInteger(taskPostpone.getFlowId()));
        // auditFlowCurrent.setFirstUserId(StringUtils.toInteger(taskTable.getFirstUserId()));
        // 审批节点对象
        AuditFlowNode auditFlowNode = new AuditFlowNode();
        // set进flow_id，根据flow_id，得到节点列表
        auditFlowNode.setFlowId(StringUtils.toInteger(taskPostpone.getFlowId()));
        // 获取节点列表
        List<AuditFlowNode> auditFlowNodeList = auditFlowNodeService.selectAuditFlowNodeList(auditFlowNode);
        auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(0).getNodeId());
        int a = auditFlowCurrentMapper.insertAuditFlowCurrent(auditFlowCurrent);
        Integer currentId = auditFlowCurrent.getCurrentId();
        if (currentId == null) {
            currentId = 0;
        }
        if (auditFlowNodeList.size() > 0) {
            auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(0).getNodeId());
            String[] as = taskPostpone.getFirstUserId().split(",");
            for (int i = 0; i < as.length; i++) {
                // 插入一级审批人的id
                auditFlowCurrent.setFirstUserId(StringUtils.toInteger(as[i]));
                int x = auditFlowNodeRoleService.insertAuditFlowNodeRoles(auditFlowCurrent);
                if (x == 1) {
                    insertApprovlInfo(currentId, StringUtils.toInteger(taskPostpone.getFlowId()), StringUtils.toInteger(taskPostpone.getFlowId()), 1, StringUtils.toInteger(as[i]), auditFlowCurrent.getCurrentNodeId());
                    // 审批提醒的消息:给该审批的第一用户
                    insertMsgInfo(currentId, Integer.parseInt(taskPostpone.getFlowId()), Integer.parseInt(as[i]));
                }
            }
        }
        String ccIds = taskPostpone.getCcIds();
        if (ccIds != null) {
            insertCopyUserId(ccIds, currentId);
        }
        return a;
    }

    /**
     * 添加任务审批人
     *
     * @param taskTable
     * @return
     */
    @Override
    public int insertAuditFlowCurrents(TaskTable taskTable) {
        // 创建审批流记录类对象
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        // 以下是将信息插入到audit_flow_current表中
        auditFlowCurrent.setAddTime(new Date());// 插入数据的时间
        SysUser sysUser = UserUtil.getUser();
        auditFlowCurrent.setCreateBy(sysUser.getUserId().intValue() + "");// 审批创建人，即审批提交人
        auditFlowCurrent.setCurrentState(1);
        auditFlowCurrent.setState(1);
        auditFlowCurrent.setCurrentState(2);
        // apply_id是插入实体类的id ,比如任务表的id
        auditFlowCurrent.setApplyId(Math.toIntExact(taskTable.gettId()));
        String title = taskTable.getTaskTitle();
        if (ShiroUtils.isNull(title) && taskTable.gettId() != null) {
            title = taskTableMapper.selectTitle(taskTable.gettId());
        }
        auditFlowCurrent.setApplytitle(title);
        // audit_id是所属审批流程id,即audit_flow表的flow_id字段，手动输入测试
        auditFlowCurrent.setAuditId(StringUtils.toInteger(taskTable.getFlowId()));
        // 审批节点对象
        AuditFlowNode auditFlowNode = new AuditFlowNode();
        // set进flow_id，根据flow_id，得到节点列表
        auditFlowNode.setFlowId(StringUtils.toInteger(taskTable.getFlowId()));
        // 获取节点列表
        List<AuditFlowNode> auditFlowNodeList = auditFlowNodeService.selectAuditFlowNodeList(auditFlowNode);

        auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(0).getNodeId());
        int a = auditFlowCurrentMapper.insertAuditFlowCurrent(auditFlowCurrent);
        Integer currentId = auditFlowCurrent.getCurrentId();
        if (currentId == null) {
            currentId = 0;
        }
        // 全局变量
        ShiroUtils.setCurrentId(currentId);
        if (auditFlowNodeList.size() > 0) {
            auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(0).getNodeId());
            String[] as = taskTable.getFirstUserId().split(",");
            for (int i = 0; i < as.length; i++) {
                if ("null".equals(as[i]) || "".equals(as[i])) {
                    continue;
                }
                // 插入一级审批人的id
                auditFlowCurrent.setFirstUserId(StringUtils.toInteger(as[i]));
                int x = auditFlowNodeRoleService.insertAuditFlowNodeRoles(auditFlowCurrent);
                if (x == 1) {
                    insertApprovlInfo(currentId, Math.toIntExact(taskTable.gettId()), StringUtils.toInteger(taskTable.getFlowId()), 1, StringUtils.toInteger(as[i]), auditFlowCurrent.getCurrentNodeId());
                    // 审批提醒的消息:给该审批的第一用户
                    insertMsgInfo(currentId, Integer.parseInt(taskTable.getFlowId()), Integer.parseInt(as[i]));
                }
            }
        }
        if (auditFlowNodeList.size() > 1) {
            auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(1).getNodeId());
            String[] as = taskTable.getSecondUserId().split(",");
            for (int i = 0; i < as.length; i++) {
                // 插入一级审批人的id
                auditFlowCurrent.setFirstUserId(StringUtils.toInteger(as[i]));
                int x = auditFlowNodeRoleService.insertAuditFlowNodeRoles(auditFlowCurrent);
                if (x == 1) {
                    insertApprovlInfo(currentId, Math.toIntExact(taskTable.gettId()), StringUtils.toInteger(taskTable.getFlowId()), 2, StringUtils.toInteger(as[i]), auditFlowCurrent.getCurrentNodeId());
                }
            }
        }
        String ccIds = taskTable.getCcIds();
        if (ccIds != null) {
            insertCopyUserId(ccIds, currentId);
        }
        return a;
    }


    /**
     * 市场,科研项目的审批方法，将项目信息插入到项目表的同时，还要将相关信息插入到审批相关表中
     *
     * @param sysProjectTable
     * @return
     */

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertAuditFlowCurrentProject(SysProjectTable sysProjectTable) {
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        auditFlowCurrent.setApplyId(sysProjectTable.getpId());
        String title = sysProjectTable.getTitle();
        if (ShiroUtils.isNull(title) && sysProjectTable.getpId() != null) {
            title = sysProjectTableMapper.selectApplyName(sysProjectTable.getpId());
        }
        auditFlowCurrent.setApplytitle(title);
        auditFlowCurrent.setAddTime(new Date());
        // 审批创建人、审批提交人、审批发起人
        auditFlowCurrent.setCreateBy(ShiroUtils.getUserId().toString());
        auditFlowCurrent.setCurrentState(1);
        auditFlowCurrent.setState(1);
        auditFlowCurrent.setCurrentState(2);
        auditFlowCurrent.setAuditId(sysProjectTable.getFlowId());
        // 根据审批类型获取到所有的审批节点
        AuditFlowNode auditFlowNode = new AuditFlowNode();
        auditFlowNode.setFlowId(sysProjectTable.getFlowId());
        List<AuditFlowNode> auditFlowNodeList = auditFlowNodeService.selectAuditFlowNodeList(auditFlowNode);
        auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(0).getNodeId());
        // 将一级节点插入审批流表中
        int a = auditFlowCurrentMapper.insertAuditFlowCurrent(auditFlowCurrent);
        Integer currentId = auditFlowCurrent.getCurrentId();
        if (auditFlowNodeList.size() > 0) {
            auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(0).getNodeId());
            String[] as = sysProjectTable.getFirstUserId().split(",");
            for (int i = 0; i < as.length; i++) {
                // 插入一级审批人的id
                auditFlowCurrent.setFirstUserId(StringUtils.toInteger(as[i]));
                int x = auditFlowNodeRoleService.insertAuditFlowNodeRoles(auditFlowCurrent);
                if (x == 1) {
                    // 审批记录
                    insertApprovlInfo(currentId, sysProjectTable.getpId(), sysProjectTable.getFlowId(), 1, StringUtils.toInteger(as[i]), auditFlowCurrent.getCurrentNodeId());
                    // 审批提醒的消息:给该审批的第一用户
                    insertMsgInfo(currentId, sysProjectTable.getFlowId(), Integer.parseInt(as[i]));
                }
            }
        }
        if (auditFlowNodeList.size() > 1) {
            auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(1).getNodeId());
            String[] as = sysProjectTable.getSecondUserId().split(",");
            for (int i = 0; i < as.length; i++) {
                // 插入二级审批人的id
                auditFlowCurrent.setFirstUserId(StringUtils.toInteger(as[i]));
                int x = auditFlowNodeRoleService.insertAuditFlowNodeRoles(auditFlowCurrent);
                if (x == 1) {
                    insertApprovlInfo(currentId, sysProjectTable.getpId(), sysProjectTable.getFlowId(), 2, StringUtils.toInteger(as[i]), auditFlowCurrent.getCurrentNodeId());
                }
            }
        }
        if (auditFlowNodeList.size() > 2) {
            auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(2).getNodeId());
            String[] as = sysProjectTable.getThirdUserId().split(",");
            for (int i = 0; i < as.length; i++) {
                // 插入三级审批人的id
                auditFlowCurrent.setFirstUserId(StringUtils.toInteger(as[i]));
                int x = auditFlowNodeRoleService.insertAuditFlowNodeRoles(auditFlowCurrent);
                if (x == 1) {
                    insertApprovlInfo(currentId, sysProjectTable.getpId(), sysProjectTable.getFlowId(), 3, StringUtils.toInteger(as[i]), auditFlowCurrent.getCurrentNodeId());
                }
            }

        }
        if (auditFlowNodeList.size() > 3) {
            auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(3).getNodeId());
            String[] as = sysProjectTable.getFourthUserId().split(",");
            for (int i = 0; i < as.length; i++) {
                // 插入四级审批人的id
                auditFlowCurrent.setFirstUserId(StringUtils.toInteger(as[i]));
                int x = auditFlowNodeRoleService.insertAuditFlowNodeRoles(auditFlowCurrent);
                if (x == 1) {
                    insertApprovlInfo(currentId, sysProjectTable.getpId(), sysProjectTable.getFlowId(), 4, StringUtils.toInteger(as[i]), auditFlowCurrent.getCurrentNodeId());
                }
            }
        }
        String ccIds = sysProjectTable.getCcIds();
        if (ccIds != null) {
            insertCopyUserId(ccIds, currentId);
        }
        return a;
    }

    public void insertCopyUserId(String ccIds, Integer currentId) {
        AuditCopyMsg auditCopyMsg = new AuditCopyMsg();
        // 存储抄送的人
        if (!StringUtils.isEmpty(ccIds)) {
            auditCopyMsg.setCurrentId(currentId);
            auditCopyMsg.setUserId(ccIds);
            List<AuditCopyMsg> list = auditCopyMsgMapper.selectInfos(auditCopyMsg);
            if (list.size() < 1) {
                auditCopyMsgMapper.insertCopys(auditCopyMsg);
            }
            if (list.size() > 0) {
                auditCopyMsgMapper.updateInfos(auditCopyMsg);
            }
        }
    }

    public void insertMsgInfo(Integer currentId, Integer auditId, Integer userId) {
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        msgEvtInfo.setType(3);
        msgEvtInfo.setEventId(currentId);
        msgEvtInfo.setAuditId(auditId);
        msgEvtInfo.setUserId(userId);
        List<MsgEvtInfo> l = msgEvtInfoService.selectOneMsgList(msgEvtInfo);
        if (l.size() < 1) {
            msgEvtInfo.setCreateTime(new java.util.Date());
            msgEvtInfoService.insertMessageInfo(msgEvtInfo);
        }
        if (l.size() >= 1) {
            for (int k = 0; k < l.size(); k++) {
                MsgEvtInfo m = new MsgEvtInfo();
                m.setId(l.get(k).getId());
                m.setReadMark(0);
                m.setUpdateTime(new Date());
                msgEvtInfoService.updateInfoById(m);
            }
        }
    }

    public void insertApprovlInfo(int currentId, int applyId, int auditId, int number, int userId, int currentNodeId) {
        ResultApprovalRecord resultApprovalRecord = new ResultApprovalRecord();
        resultApprovalRecord.setCurrentId(currentId);
        resultApprovalRecord.setApplyId(applyId);
        resultApprovalRecord.setAuditId(auditId);
        resultApprovalRecord.setNumber(number);
        resultApprovalRecord.setApprovalUserId(userId);
        resultApprovalRecord.setApprovalUserName(userService.getName(userId));
        resultApprovalRecord.setApprovalState("审批中");
        resultApprovalRecord.setApprovalMemo("");
        resultApprovalRecord.setCurrentNodeId(currentNodeId);
        resultApprovalRecordService.insertApprovalRecord(resultApprovalRecord);
    }

    /**
     * 提交项目中止审批方法
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public int updateAuditFlowCurrentProject(SysProjectTable sysProjectTable) {
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        auditFlowCurrent.setApplyId(sysProjectTable.getpId());
        auditFlowCurrent.setAddTime(new Date());
        SysUser sysUser = UserUtil.getUser();
        auditFlowCurrent.setCreateBy(sysUser.getUserId().intValue() + "");// 审批创建人，即审批提交人
        auditFlowCurrent.setCurrentState(1);
        auditFlowCurrent.setState(1);
        auditFlowCurrent.setCurrentState(2);
        auditFlowCurrent.setAuditId(sysProjectTable.getFlowId());
        // AuditFlowNode审批节点表
        AuditFlowNode auditFlowNode = new AuditFlowNode();
        auditFlowNode.setFlowId(sysProjectTable.getFlowId());
        // 审批节点list列表，根据flow_id查到相应FlowNode list列表
        List<AuditFlowNode> auditFlowNodeList = auditFlowNodeService.selectAuditFlowNodeList(auditFlowNode);
        // 将node_id的值赋予到current_node_id中，auditFlowNodeList.get(0).getNodeId()是得到第一个node_id，根据flow_id查到
        auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(0).getNodeId());
        // 将信息插入到audit_flow_node表中，关键是查到First_user_id、second_user_id、third_user_id,其他信息已经固定好
        int a = auditFlowCurrentMapper.insertAuditFlowCurrent(auditFlowCurrent);
        Integer currentId = auditFlowCurrent.getCurrentId();
        if (currentId == null) {
            currentId = 0;
        }
        if (auditFlowNodeList.size() > 0) {
            auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(0).getNodeId());
            String[] as = sysProjectTable.getFirstUserId().split(",");
            for (int i = 0; i < as.length; i++) {
                // 插入一级审批人的id
                auditFlowCurrent.setFirstUserId(StringUtils.toInteger(as[i]));
                int x = auditFlowNodeRoleService.insertAuditFlowNodeRoles(auditFlowCurrent);
                if (x == 1) {
                    insertApprovlInfo(currentId, sysProjectTable.getpId(), sysProjectTable.getFlowId(), 1, StringUtils.toInteger(as[i]), auditFlowCurrent.getCurrentNodeId());
                    // 审批提醒的消息:给该审批的第一用户
                    insertMsgInfo(currentId, sysProjectTable.getFlowId(), Integer.parseInt(as[i]));
                }
            }
        }
        if (auditFlowNodeList.size() > 1) {
            auditFlowCurrent.setCurrentNodeId(auditFlowNodeList.get(1).getNodeId());
            String[] as = sysProjectTable.getSecondUserId().split(",");
            for (int i = 0; i < as.length; i++) {
                // 插入一级审批人的id
                auditFlowCurrent.setFirstUserId(StringUtils.toInteger(as[i]));
                int x = auditFlowNodeRoleService.insertAuditFlowNodeRoles(auditFlowCurrent);
                if (x == 1) {
                    insertApprovlInfo(currentId, sysProjectTable.getpId(), sysProjectTable.getFlowId(), 2, StringUtils.toInteger(as[i]), auditFlowCurrent.getCurrentNodeId());
                }
            }
        }
        String ccIds = sysProjectTable.getCcIds();
        if (ccIds != null) {
            insertCopyUserId(ccIds, currentId);
        }
        return a;
    }

    /**
     * @param currentId
     * @return
     */
    @Override
    public Integer getFlowCurrentNodeId(int currentId) {
        Integer a = 0;
        if ((auditFlowCurrentMapper.getFlowCurrentNodeId(currentId) + "") != null) {
            a = auditFlowCurrentMapper.getFlowCurrentNodeId(currentId);
        }
        return a;
    }

    /**
     * 根据当前节点得到相应的current_state
     *
     * @param auditFlowCurrent
     * @return
     */
    @Override
    public List<AuditFlowCurrent> getFlowCurrentStatre(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.getFlowCurrentStatre(auditFlowCurrent);
    }

    @Override
    public List<AuditFlowCurrent> selectCurrentId(AuditFlowCurrent auditFlowCurrent) {
        List<AuditFlowCurrent> list = auditFlowCurrentMapper.selectCurrentId(auditFlowCurrent);

        return list;
    }

    /**
     * 获得audit_id
     *
     * @param auditFlowCurrent
     * @return
     */
    @Override
    public List<AuditFlowCurrent> selectAuditForApplyId(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.selectAuditForApplyId(auditFlowCurrent);
    }

    /**
     * 获得apply_id
     *
     * @param auditFlowCurrent
     * @return
     */
    @Override
    public List<AuditFlowCurrent> selectAllApplyId(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.selectAllApplyId(auditFlowCurrent);
    }

    /**
     * 根据审批流id，查询相应审批单信息
     *
     * @param auditFlowCurrent
     * @return
     */
    @Override
    public List<AuditFlowCurrent> selectAuditFlowCurrentList1(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.selectAuditFlowCurrentList1(auditFlowCurrent);
    }

    /**
     * 获得applyid
     *
     * @param id
     * @return
     */
    @Override
    public Integer selectApplyIdByCurrentId(int id) {
        return auditFlowCurrentMapper.selectApplyIdByCurrentId(id);
    }

    @Override
    public List<AuditFlowCurrent> selectAuditIdByCurrentId(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.selectAuditIdByCurrentId(auditFlowCurrent);
    }

    /**
     * 获得审批状态
     *
     * @param auditFlowCurrent
     * @return
     */
    @Override
    public Integer selectCurrentState(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.selectCurrentState(auditFlowCurrent);
    }

    @Override
    public Integer deleteAuditFlowCurrentById(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.deleteAuditFlowCurrentById(auditFlowCurrent);
    }

    @Override
    public List<AuditFlowCurrent> selectCurrentInfo(AuditFlowCurrent auditFlowCurrent) {
        return auditFlowCurrentMapper.selectCurrentInfo(auditFlowCurrent);
    }

    @Override
    public int deleteApprovalByCurrentId(Integer currentId) {
        Map<String, Object> resultMap = auditFlowCurrentMapper.selectFlowCurrentMap(currentId);
        if (resultMap == null || resultMap.size() < 1) {
            return 0;
        }
        Integer auditId = (Integer) resultMap.get("auditId");
        Integer applyId = (Integer) resultMap.get("applyId");
        Map<String, Object> map = new HashMap<>();
        int a = 0;
        if (auditId == 1) {
            // 任务删除
            a = taskTableMapper.deleteTaskTableById((applyId.longValue()));
        } else if (auditId == 3 || auditId == 7) {
            // 项目删除
            a = sysProjectTableMapper.deleteProject(applyId);
        }
        // 删除审批信息
        a = auditFlowCurrentMapper.deleteMultipleApproval(currentId);
        // 删除审批信息
        map.put("type", 3);
        map.put("eventId", currentId);
        msgEvtInfoMapper.deleteMsgEvt(map);
        return a;
    }

    @Override
    public AjaxResult revokeApproval(Integer currentId) {
        int a = 0;
        AuditFlowCurrent auditFlowCurrent = auditFlowCurrentService.selectAuditFlowCurrentById(currentId);
        if (auditFlowCurrent == null) {
            return AjaxResult.error("该审批不存在，正在为您刷新列表");
        }
        if (!auditFlowCurrent.getCurrentState().equals(ApprovalTypes.NOT_APPROVAL.getState())) {
            return AjaxResult.error("该审批已被受理，不能撤销，正在为您刷新列表");
        }
        Integer flowId = auditFlowCurrent.getAuditId();
        Integer applyId = auditFlowCurrent.getApplyId();
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        SysProjectTable sysProjectTable = new SysProjectTable();
        TaskTable taskTable = new TaskTable();
        sysProjectTable.setpId(applyId);
        sysProjectTable.setUpdateTime(new Date());
        // 只删除审批信息的情况
        if (flowId == 8 || flowId == 9 || flowId == 10 || flowId == 11 || flowId == 12) {
            // 只删除审批的信息
            deleteApproval(currentId);
            msgEvtInfo.setType(3);
            msgEvtInfo.setEventId(currentId);
            a = msgEvtInfoService.deleteForMsg(msgEvtInfo);
            if (flowId == 10 || flowId == 11) {
                sysProjectTable.setProjectFinishFlag("0");
                a = sysProjectTableService.updateProject(sysProjectTable);
            }
            if (flowId == 8 || flowId == 9 || flowId == 12) {
                taskTable.settId((long) applyId);
                taskTable.setUpdateTime(new Date());
                taskTable.setTaskFinishflag("0");
                a = taskTableService.updateTaskTableByFlag(taskTable);
            }
        }
        if (flowId == 1) {
            // 即删除任务的审批信息，也删除任务原始数据
            deleteApproval(currentId);
            // 删除任务的参与人
            taskUserService.deleteTaskUserById((long) applyId);
            // 删除任务
            a = taskTableService.deleteTaskTableById((long) applyId);
            // 删除任务消息
            msgEvtInfo.setType(3);
            msgEvtInfo.setEventId(currentId);
            a = msgEvtInfoService.deleteForMsg(msgEvtInfo);
            msgEvtInfo.setEventId(applyId);
            List<Integer> taskTypes = new ArrayList<>();
            taskTypes.add(0);// 任务到期
            taskTypes.add(1);// 任务逾期
            taskTypes.add(2);// 任务未开始
            msgEvtInfo.setType(null);
            msgEvtInfo.setTypeList(taskTypes);
            msgEvtInfoService.deleteForMsg(msgEvtInfo);
        }
        if (flowId == 3 || flowId == 7) {
            // 将审批状态更新为已撤销状态
            AuditFlowCurrent au = new AuditFlowCurrent();
            au.setCurrentId(currentId);
            au.setCurrentState(7);
            a = auditFlowCurrentService.updateAuditFlowCurrent(au);
            // 将消息置为已读
            msgEvtInfo.setType(3);
            msgEvtInfo.setReadMark(1);
            msgEvtInfo.setEventId(currentId);
            msgEvtInfoService.updateMessageInfo2(msgEvtInfo);
            if (flowId == 3) {
                msgEvtInfo.setType(null);
                List<Integer> typeList = new ArrayList<>();
                typeList.add(5);
                typeList.add(6);
                msgEvtInfo.setTypeList(typeList);
                msgEvtInfo.setEventId(applyId);
                msgEvtInfo.setUpdateTime(new Date());
                msgEvtInfoService.updateMessageInfo2(msgEvtInfo);
            }
            // 项目撤销，更新状态为立项失败
            sysProjectTable.setEstablishStatus("4");
            a = sysProjectTableService.updateProject(sysProjectTable);
        }
        if (a == 1) {
            return AjaxResult.success();
        }
        return AjaxResult.error();
    }

    public int deleteApproval(Integer currentId) {
        ResultApprovalRecord resultApprovalRecord = new ResultApprovalRecord();
        int a = 0;
        // 删除审批记录
        resultApprovalRecord.setCurrentId(currentId);
        a = resultApprovalRecordService.deleteApprovalRecord(resultApprovalRecord);
        // 删除审批流表
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        auditFlowCurrent.setCurrentId(currentId);
        a = auditFlowCurrentService.deleteAuditFlowCurrentById(auditFlowCurrent);
        // 删除权限
        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
        auditFlowNodeRole.setCurrentId(currentId);
        a = auditFlowNodeRoleMapper.deleteFlowNodeRoleByCurrentId(auditFlowNodeRole);
        // 删除审批过程详细表
        AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
        auditFlowOperRecord.setCurrentId(currentId);
        auditFlowOperRecordMapper.deleteOperRecordByCurrentId(auditFlowOperRecord);
        return a;
    }

    @Override
    public List<ResultAudit> list(AuditFlowCurrent auditFlowCurrent) {
        SysUser sysUser = UserUtil.getUser();
        // 审批节点表list对象
        List<AuditFlow> auditFlowList = auditFlowService.selectAuditFlowList(new AuditFlow());
        // 最终以ResultAudit 返回list
        List<ResultAudit> list = new ArrayList<>();
        // 增强for循环
        for (AuditFlow auditFlow : auditFlowList) {
            Integer flowId = auditFlow.getFlowId();
            if (auditFlow.getTypeId() != null) {
                // applyTableName申请单表名，applyClassName主键名
                auditFlowCurrent.setApplyClassName(auditFlow.getApplyClassName());
                auditFlowCurrent.setApplyTableName(auditFlow.getApplyTableName());
                auditFlowCurrent.setAuditId(auditFlow.getFlowId());// 类型id
                auditFlowCurrent.setUserId(sysUser.getUserId());
                List<AuditFlowCurrent> list1 = auditFlowCurrentService.selectAuditFlowCurrentList(auditFlowCurrent);
                // ResultAudit类,审核单名称、ID、状态、审批人、时间
                for (AuditFlowCurrent auditFlowCurrent1 : list1) {
                    Integer currentId = auditFlowCurrent1.getCurrentId();
                    if (currentId == null) {
                        currentId = 0;
                    }
                    List<SysUser> userList = selectUserList(currentId);
                    ResultAudit resultAudit = new ResultAudit();
                    if (flowId == 1 || flowId == 8 || flowId == 9 || flowId == 12) {
                        auditFlowCurrent1.setProjectTable(null);
                    }
                    if (flowId == 3 || flowId == 7 || flowId == 10 || flowId == 11 || flowId == 13 || flowId == 14) {
                        auditFlowCurrent1.setTaskTable(null);
                    }
                    // 任务表TaskTable对象,在实体类中构造该对象
                    if (auditFlowCurrent1.getTaskTable() != null && auditFlowCurrent1.getTaskTable().gettId() != null) {
                        String title = auditFlowCurrent1.getApplytitle();
                        if (MyUtils.isEmpty(title)) {
                            title = auditFlowCurrent1.getTaskTable().getTaskTitle();
                        }
                        resultAudit.setAuditName(title);
                        resultAudit.setProjectTable(auditFlowCurrent1.getTaskTable().getProjectTable());
                        resultAudit.setTaskTable(auditFlowCurrent1.getTaskTable());
                        resultAudit.getTaskTable().setProjectTable(null);
                    } else {
                        // 项目表project_table对象
                        if (auditFlowCurrent1.getProjectTable() != null) {
                            String title = auditFlowCurrent1.getApplytitle();
                            if (MyUtils.isEmpty(title)) {
                                title = auditFlowCurrent1.getProjectTable().getTitle();
                            }
                            resultAudit.setAuditName(title);
                            resultAudit.setApprovalUserName(userService.getName(Integer.parseInt(auditFlowCurrent1.getCreateBy())));
                            resultAudit.setProjectTable(auditFlowCurrent1.getProjectTable());
                        }
                    }
                    List<AuditFlowOperRecord> auditFlowOperRecordList = new ArrayList<>();
                    for (AuditFlowOperRecord auditFlowOperRecord : auditFlowCurrent1.getAuditFlowOperRecordSet()) {
                        if (auditFlowOperRecord.getRecordId() != null && auditFlowOperRecord.getOperUserId() != null) {
                            auditFlowOperRecordList.add(auditFlowOperRecord);
                        }
                    }
                    resultAudit.setCurrentId(auditFlowCurrent1.getCurrentId());
                    resultAudit.setTime(auditFlowCurrent1.getAddTime());// 创建时间
                    if (ShiroUtils.isNotNull(auditFlowCurrent1.getCreateBy())) {
                        resultAudit.setSubmitter(userService.getName(Integer.parseInt(auditFlowCurrent1.getCreateBy())));
                    }
                    resultAudit.setAuditFlowOperRecordSet(auditFlowOperRecordList);
                    resultAudit.setUserList(userList);
                    resultAudit.setAllFlowNodeRoleList(auditFlowCurrent1.getAllFlowNodeRoleList());
                    resultAudit.setCurrentNodeName(auditFlowCurrent1.getCurrentNodeName());
                    resultAudit.setFlowId(auditFlowCurrent1.getAuditFlow().getFlowId());
                    resultAudit.setFlowName(auditFlowCurrent1.getAuditFlow().getFlowName());
                    resultAudit.setTypeId(auditFlowCurrent1.getAuditFlow().getTypeId());
                    resultAudit.setTypeName(auditFlowCurrent1.getAuditFlow().getTypeName());
                    resultAudit.setTypeId(auditFlowCurrent1.getAuditFlow().getTypeId());
                    resultAudit.setTypeName(auditFlowCurrent1.getAuditFlow().getTypeName());
                    resultAudit.setApplyClassName(auditFlowCurrent1.getAuditFlow().getApplyClassName());
                    resultAudit.setApplyTableName(auditFlowCurrent1.getAuditFlow().getApplyTableName());
                    resultAudit.setApplyId(auditFlowCurrent1.getApplyId());
                    list.add(resultAudit);
                }
            }
        }
        if (!list.isEmpty() & list != null) {
            // 对list进行排序 降序排序
            try {
                Collections.sort(list, new Comparator<ResultAudit>() {
                    @Override
                    public int compare(ResultAudit o1, ResultAudit o2) {
                        Date a = o1.getTime();
                        Date b = o2.getTime();
                        int flag = o1.getTime().compareTo(o2.getTime());
                        if (flag == -1) {
                            flag = 1;
                        } else if (flag == 1) {
                            flag = -1;
                        }
                        return flag;
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    @Override
    public PageEntity selectApplyList(AuditFlowCurrent vo) {
        if (!ShiroUtils.isNull(vo.getStarttime())) {
            vo.setStarttime(vo.getStarttime() + " 00:00:00");
        }
        if (!ShiroUtils.isNull(vo.getEndtime())) {
            vo.setEndtime(vo.getEndtime() + " 23:59:59");
        }
        int pageIndex = vo.getPageNumber();
        int pageSize = vo.getTotal();
        pageSize = pageSize <= 0 ? 10 : pageSize;
        PageHelper.startPage(pageIndex, pageSize);
        List<Map<String, Object>> flowCurrentList = auditFlowCurrentMapper.selectFlowCurrent(vo);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(flowCurrentList);
        List<Map<String, Object>> resultList = pageInfo.getList();
        for (Map<String, Object> map : resultList) {
            Integer currentId = (Integer) map.get("currentId");
            // 抄送人
            String userIds = auditCopyMsgMapper.selectUseIds(currentId);
            Integer currentState = (Integer) map.get("currentState");
            if (ShiroUtils.isNotNull(userIds)) {
                List<String> nameList = userMapper.selectNameList(userIds.split(","));
                String copyName = String.join(",", nameList);
                map.put("copyPeronName", copyName);
            }
            // 审批人
            List<String> userList = selectApplyUserIdList(currentId);
            map.put("userList", userList);
            List<Integer> stateList = auditFlowNodeRoleMapper.selectApplyState(currentId);
            Integer auditId = (Integer) map.get("auditId");
            map.put("state", 0);
            if (currentState != 7 && !stateList.contains(0)) {
                // 表示能够撤销
                map.put("state", 1);
            }
            map.put("delete", 0);
            // 发起任务、发起项目才具备删除功能，且一旦审批，不能进行删除
            if ((auditId == 1 || auditId == 3 || auditId == 7) && !stateList.contains(0)) {
                map.put("delete", 1);
            }
            Integer currentNodeId = (Integer) map.get("currentNodeId");
            Date approvalTime = auditFlowOperRecordMapper.selectApprovalTime(currentId, currentNodeId, ShiroUtils.getUserId().intValue());
            map.put("approvalTime", approvalTime);
            String currentNodeName = (String) map.get("currentNodeName");
            if (currentState != null) {
                currentNodeName = getCurrentNodeName(currentNodeId, auditId, currentState, currentNodeName);
                map.put("currentNodeName", currentNodeName);
            }
            Integer applyId = (Integer) map.get("applyId");
            Integer typeId = (Integer) map.get("typeId");
            if (typeId != null && typeId == 0) {
                map.put("tId", applyId);
            } else if (typeId != null && typeId == 2) {
                map.put("pId", applyId);
            }
        }
        return new PageEntity(pageInfo);
    }

    @Override
    public AjaxResult selectApprovalDetail(AuditFlowCrurrentVo vo) {
        Map<String, Object> resultMap = new HashMap<>();
        Integer tId = vo.gettId();
        Integer pId = vo.getpId();
        Integer currentId = vo.getCurrentId();
        Map<String, Object> flowCurrentMap = new HashMap<>();
        if (currentId != null) {
            flowCurrentMap = auditFlowCurrentMapper.selectFlowCurrentMap(currentId);
            if (flowCurrentMap == null || flowCurrentMap.size() < 1) {
                return AjaxResult.error("该审批已被删除或者已被撤回，正在为您刷新该列表!");
            }
        }
        if (tId != null) {
            resultMap = taskTableMapper.selectApprovalDetail(vo);
            if (flowCurrentMap != null & flowCurrentMap.size() > 0) {
                resultMap = this.setValue(flowCurrentMap, resultMap);
            }
        } else if (pId != null) {
            resultMap = sysProjectTableMapper.selectApprovalDetail(vo);
            resultMap = ShiroUtils.toReplaceKeyLow(resultMap);
            Integer projectType = (Integer) resultMap.get("projectType");
            if (projectType != null && projectType == 0) {
                List<Map<String, Object>> userList = participantsMapper.selectUserName(pId);
                resultMap.put("participantsList", userList);
            }
        }
        return AjaxResult.success("查询成功", resultMap);
    }


    public Map<String, Object> setValue(Map<String, Object> flowCurrentMap, Map<String, Object> resultMap) {
        Integer auditId = (Integer) flowCurrentMap.get("auditId");
        Integer applyId = (Integer) flowCurrentMap.get("applyId");
        if (auditId == 1 || auditId == 8 || auditId == 9 || auditId == 12) {
            String userName = "";
            // 审批发起人：发起任务、任务完成、任务中止、任务变更审批、
            resultMap.put("submitter", flowCurrentMap.get("createName"));
            // 中止原因
            if (auditId == 8) {
                String stopMemo = taskStopMoneMapper.selectStopMemo(applyId.longValue());
                resultMap.put("stopMemo", stopMemo);
            }
            if (auditId == 12) {
                String changeMemo = taskPostponeMapper.selectChangeMemo(applyId.longValue());
                resultMap.put("changeMemo", changeMemo);
            }
        }
        return resultMap;
    }

    public List<SysUser> selectUserList(Integer currentId) {
        SysUser sysUser = new SysUser();
        List<SysUser> userList = new ArrayList<>();
        List<SysUser> userList1 = new ArrayList<>();
        AuditFlowNodeRole audit = new AuditFlowNodeRole();
        AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
        // 获得该审批流的对应的userId和nodeId
        audit.setCurrentId(currentId);
        List<AuditFlowNodeRole> listNodeRole = auditFlowNodeRoleMapper.getAllUserByCurretnId(audit);
        for (AuditFlowNodeRole a : listNodeRole) {
            Integer userId = a.getUserId();
            if (userId == null) {
                userId = 0;
            }
            sysUser.setUserId(userId.longValue());
            List<SysUser> users = userService.selectUserList(sysUser);
            for (SysUser s : users) {
                userList.add(s);
            }
        }
        // 拿到所有的审批人前执行去空的操作
        userList.removeAll(Collections.singleton(null));
        // 去除重复的userId
        LinkedHashSet<SysUser> hashSet = new LinkedHashSet<>(userList);
        ArrayList<SysUser> usersList2 = new ArrayList<>(hashSet);
        // 然后循环遍历，
        if (!usersList2.isEmpty() && usersList2 != null) {
            for (int i = 0; i < userList.size(); i++) {
                Integer u = usersList2.get(i).getUserId().intValue();
                audit.setCurrentId(currentId);
                audit.setUserId(u);
                List<AuditFlowNodeRole> nodeRoles = auditFlowNodeRoleMapper.selectByNodeIdAndCurrentId(audit);

                auditFlowOperRecord.setCurrentId(currentId);
                auditFlowOperRecord.setOperUserId(usersList2.get(i).getUserId().intValue());
                auditFlowOperRecord.setOperType(2);
                // 得到的是全部已经转推过的人
                List<AuditFlowOperRecord> list = auditFlowOperRecordMapper.selectAuditOperRecord(auditFlowOperRecord);
                auditFlowOperRecord.setOperType(0);
                List<AuditFlowOperRecord> list2 = auditFlowOperRecordMapper.selectAuditOperRecord(auditFlowOperRecord);
                auditFlowOperRecord.setOperType(1);
                List<AuditFlowOperRecord> list3 = auditFlowOperRecordMapper.selectAuditOperRecord(auditFlowOperRecord);
                if (!nodeRoles.isEmpty() && nodeRoles.size() >= 2) {
                    Integer number = nodeRoles.size();
                    if (list.size() < number) {
                        sysUser.setUserId((long) usersList2.get(i).getUserId());
                        List<SysUser> users = userService.selectUserList(sysUser);
                        for (SysUser s : users) {
                            userList1.add(s);
                        }
                    }

                }
                if (!nodeRoles.isEmpty() && nodeRoles.size() <= 1) {
                    // 该用户没有转推的情况
                    if (list.size() < 1) {
                        if (list2.size() > 0 || list3.size() > 0) {
                            sysUser.setUserId((long) usersList2.get(i).getUserId());
                            List<SysUser> users = userService.selectUserList(sysUser);
                            for (SysUser s : users) {
                                userList1.add(s);
                            }
                        }
                    }
                    // 该用户有转推，可是又有审批的情况
                    if (list.size() >= 1) {
                        if (list2.size() > 0 || list3.size() > 0) {
                            sysUser.setUserId((long) usersList2.get(i).getUserId());
                            List<SysUser> users = userService.selectUserList(sysUser);
                            for (SysUser s : users) {
                                userList1.add(s);
                            }
                        }
                    }
                    // 该用户还没有进行审批的情况
                    if (list.size() < 1 && (list2.size() < 1 && list3.size() < 1)) {
                        sysUser.setUserId((long) usersList2.get(i).getUserId());
                        List<SysUser> users = userService.selectUserList(sysUser);
                        for (SysUser s : users) {
                            userList1.add(s);
                        }
                    }
                }
            }
        }
        return userList1;
    }

    @Override
    public List<ResultAudit> commissionlistByTid(AuditFlowCurrent auditFlowCurrent) {
        // 默认登录用户id=1
        SysUser sysUser = UserUtil.getUser();
        // 审批节点表list对象
        List<AuditFlow> auditFlowList = auditFlowService.selectAuditFlowList(new AuditFlow());
        // 最终以ResultAudit 返回list
        List<ResultAudit> list = new ArrayList<>();
        // 增强for循环
        for (AuditFlow auditFlow : auditFlowList) {
            Integer flowId = auditFlow.getFlowId();
            if (flowId == 1 || flowId == 8 || flowId == 9 || flowId == 12) {
                if (auditFlow.getTypeId() != null) {
                    // applyTableName申请单表名，applyClassName主键名
                    auditFlowCurrent.setApplyClassName(auditFlow.getApplyClassName());
                    auditFlowCurrent.setApplyTableName(auditFlow.getApplyTableName());
                    auditFlowCurrent.setAuditId(auditFlow.getFlowId());//类型id
                    auditFlowCurrent.setOperUserId(ShiroUtils.getUserId() + "");
                    auditFlowCurrent.setUserId(sysUser.getUserId());
                    List<AuditFlowCurrent> list1 = auditFlowCurrentService.selectAuditFlowCurrentList(auditFlowCurrent);
                    // ResultAudit类,审核单名称、ID、状态、审批人、时间
                    for (AuditFlowCurrent auditFlowCurrent1 : list1) {
                        Integer currentId = auditFlowCurrent1.getCurrentId();
                        if (currentId == null) {
                            currentId = 0;
                        }
                        List<SysUser> userList = selectUserList(currentId);
                        ResultAudit resultAudit = new ResultAudit();
                        // 任务表TaskTable对象,在实体类中构造该对象
                        if (auditFlowCurrent1.getTaskTable() != null && auditFlowCurrent1.getTaskTable().gettId() != null) {
                            String title = auditFlowCurrent1.getApplytitle();
                            if (MyUtils.isEmpty(title)) {
                                title = auditFlowCurrent1.getTaskTable().getTaskTitle();
                            }
                            resultAudit.setAuditName(title);// 任务标题
                            resultAudit.setTime(auditFlowCurrent1.getAddTime());// 创建时间,
                            resultAudit.setProjectTable(auditFlowCurrent1.getTaskTable().getProjectTable());
                            resultAudit.setTaskTable(auditFlowCurrent1.getTaskTable());
                            // 查询审批记录的
                            resultAudit.getTaskTable().setProjectTable(null);
                        }
                        List<AuditFlowOperRecord> auditFlowOperRecordList = new ArrayList<>();
                        for (AuditFlowOperRecord auditFlowOperRecord : auditFlowCurrent1.getAuditFlowOperRecordSet()) {
                            if (auditFlowOperRecord.getRecordId() != null && auditFlowOperRecord.getOperUserId() != null) {
                                if (auditFlowOperRecord.getOperUserId() == Integer.parseInt(ShiroUtils.getUserId() + "")) {
                                    resultAudit.setApprovalTime(auditFlowOperRecord.getAddTime());
                                }
                                auditFlowOperRecordList.add(auditFlowOperRecord);
                            }
                        }
                        resultAudit.setCurrentId(auditFlowCurrent1.getCurrentId());
                        resultAudit.setTime(auditFlowCurrent1.getAddTime());// 创建时间,
                        resultAudit.setSubmitter(userService.getName(Integer.parseInt(auditFlowCurrent1.getCreateBy())));
                        resultAudit.setAuditFlowOperRecordSet(auditFlowOperRecordList);
                        resultAudit.setUserList(userList);
                        resultAudit.setAllFlowNodeRoleList(auditFlowCurrent1.getAllFlowNodeRoleList());
                        resultAudit.setCurrentNodeName(auditFlowCurrent1.getCurrentNodeName());
                        resultAudit.setFlowId(auditFlowCurrent1.getAuditFlow().getFlowId());
                        resultAudit.setFlowName(auditFlowCurrent1.getAuditFlow().getFlowName());
                        resultAudit.setTypeId(auditFlowCurrent1.getAuditFlow().getTypeId());
                        resultAudit.setTypeName(auditFlowCurrent1.getAuditFlow().getTypeName());
                        resultAudit.setTypeId(auditFlowCurrent1.getAuditFlow().getTypeId());
                        resultAudit.setTypeName(auditFlowCurrent1.getAuditFlow().getTypeName());
                        resultAudit.setApplyClassName(auditFlowCurrent1.getAuditFlow().getApplyClassName());
                        resultAudit.setApplyTableName(auditFlowCurrent1.getAuditFlow().getApplyTableName());
                        resultAudit.setApplyId(auditFlowCurrent1.getApplyId());

                        list.add(resultAudit);
                    }
                }
            }
        }

        if (!list.isEmpty() & list != null) {
            // 对list进行排序 降序排序
            Collections.sort(list, new Comparator<ResultAudit>() {
                @Override
                public int compare(ResultAudit o1, ResultAudit o2) {
                    Date a = o1.getApprovalTime();
                    Date b = o2.getApprovalTime();
                    int flag = o1.getApprovalTime().compareTo(o2.getApprovalTime());
                    if (flag == -1) {
                        flag = 1;
                    } else if (flag == 1) {
                        flag = -1;
                    }
                    return flag;
                }
            });
        }
        return list;
    }

    @Override
    public List<ResultAudit> commissionlistByProjectId(AuditFlowCurrent auditFlowCurrent) {

        // 默认登录用户id=1
        SysUser sysUser = UserUtil.getUser();
        SysUser sysUser2 = new SysUser();
        // 审批节点表list对象
        List<AuditFlow> auditFlowList = auditFlowService.selectAuditFlowList(new AuditFlow());
        // 最终以ResultAudit 返回list
        List<ResultAudit> list = new ArrayList<>();
        // 增强for循环
        for (AuditFlow auditFlow : auditFlowList) {
            Integer flowId = auditFlow.getFlowId();
            if (flowId == 3 || flowId == 7 || flowId == 10 || flowId == 11 || flowId == 13 || flowId == 14) {

                if (auditFlow.getTypeId() != null) {
                    // applyTableName申请单表名，applyClassName主键名
                    auditFlowCurrent.setApplyClassName(auditFlow.getApplyClassName());
                    auditFlowCurrent.setApplyTableName(auditFlow.getApplyTableName());
                    auditFlowCurrent.setAuditId(auditFlow.getFlowId());//类型id
                    // role_id，user_id，作为该表的字符串对象
                    auditFlowCurrent.setUserId(sysUser.getUserId());
                    // 根据这个条件来获得已经审批的列表
                    auditFlowCurrent.setOperUserId(ShiroUtils.getUserId() + "");
                    List<AuditFlowCurrent> list1 = auditFlowCurrentService.selectAuditFlowCurrentList(auditFlowCurrent);
                    // ResultAudit类,审核单名称、ID、状态、审批人、时间
                    for (AuditFlowCurrent auditFlowCurrent1 : list1) {
                        Integer currentId = auditFlowCurrent1.getCurrentId();
                        if (currentId == null) {
                            currentId = 0;
                        }
                        List<SysUser> userList = selectUserList(currentId);
                        ResultAudit resultAudit = new ResultAudit();
                        // 项目表project_table对象
                        if (auditFlowCurrent1.getProjectTable() != null) {
                            // resultAudit.set
                            String title = auditFlowCurrent1.getApplytitle();
                            if (MyUtils.isEmpty(title)) {
                                title = auditFlowCurrent1.getProjectTable().getTitle();
                            }
                            resultAudit.setAuditName(title);
                            resultAudit.setApprovalUserName(userService.getName(Integer.parseInt(auditFlowCurrent1.getCreateBy())));
                            resultAudit.setTime(auditFlowCurrent1.getAddTime());
                            // 在项目审批中添加项目审批信息
                            resultAudit.setProjectTable(auditFlowCurrent1.getProjectTable());
                        }
                        List<AuditFlowOperRecord> auditFlowOperRecordList = new ArrayList<>();
                        for (AuditFlowOperRecord auditFlowOperRecord : auditFlowCurrent1.getAuditFlowOperRecordSet()) {
                            if (auditFlowOperRecord.getRecordId() != null && auditFlowOperRecord.getOperUserId() != null) {
                                if (auditFlowOperRecord.getOperUserId() == Integer.parseInt(ShiroUtils.getUserId() + "")) {
                                    resultAudit.setApprovalTime(auditFlowOperRecord.getAddTime());
                                    resultAudit.setRemark(auditFlowOperRecord.getOperMemo());
                                }
                                auditFlowOperRecordList.add(auditFlowOperRecord);
                            }
                        }
                        resultAudit.setCurrentId(auditFlowCurrent1.getCurrentId());
                        resultAudit.setTime(auditFlowCurrent1.getAddTime());// 创建时间,
                        resultAudit.setSubmitter(userService.getName(Integer.parseInt(auditFlowCurrent1.getCreateBy())));
                        resultAudit.setAuditFlowOperRecordSet(auditFlowOperRecordList);
                        resultAudit.setUserList(userList);
                        resultAudit.setAllFlowNodeRoleList(auditFlowCurrent1.getAllFlowNodeRoleList());
                        resultAudit.setCurrentNodeName(auditFlowCurrent1.getCurrentNodeName());
                        resultAudit.setFlowId(auditFlowCurrent1.getAuditFlow().getFlowId());
                        resultAudit.setFlowName(auditFlowCurrent1.getAuditFlow().getFlowName());
                        resultAudit.setTypeId(auditFlowCurrent1.getAuditFlow().getTypeId());
                        resultAudit.setTypeName(auditFlowCurrent1.getAuditFlow().getTypeName());
                        resultAudit.setTypeId(auditFlowCurrent1.getAuditFlow().getTypeId());
                        resultAudit.setTypeName(auditFlowCurrent1.getAuditFlow().getTypeName());
                        resultAudit.setApplyClassName(auditFlowCurrent1.getAuditFlow().getApplyClassName());
                        resultAudit.setApplyTableName(auditFlowCurrent1.getAuditFlow().getApplyTableName());
                        resultAudit.setApplyId(auditFlowCurrent1.getApplyId());

                        list.add(resultAudit);
                    }
                }
            }
        }
        if (!list.isEmpty() & list != null) {
            // 对list进行排序 降序排序
            Collections.sort(list, new Comparator<ResultAudit>() {
                @Override
                public int compare(ResultAudit o1, ResultAudit o2) {
                    Date a = o1.getApprovalTime();
                    Date b = o2.getApprovalTime();
                    int flag = o1.getApprovalTime().compareTo(o2.getApprovalTime());
                    if (flag == -1) {
                        flag = 1;
                    } else if (flag == 1) {
                        flag = -1;
                    }
                    return flag;
                }
            });
        }
        return list;
    }

    @Override
    public List<ResultAudit> listCurrentId(AuditFlowCurrent auditFlowCurrent) {
        Integer currentId = auditFlowCurrent.getCurrentId();
        if (currentId == null) {
            currentId = 0;
        }
        SysUser sysUser2 = new SysUser();
        List<ResultAudit> list = new ArrayList<>();
        auditFlowCurrent.setCurrentId(currentId);
        List<AuditFlowCurrent> list1 = auditFlowCurrentService.selectAuditFlowCurrentList1(auditFlowCurrent);
        // 及时更新对应的审批记录
        AuditFlowCurrent a1 = new AuditFlowCurrent();
        // ResultAudit类,审核单名称、ID、状态、审批人、时间
        for (AuditFlowCurrent auditFlowCurrent1 : list1) {
            Integer auditId = auditFlowCurrent1.getAuditId();
            if (auditId == 3 || auditId == 7 || auditId == 10 || auditId == 11 | auditId == 13 || auditId == 14) {
                auditFlowCurrent1.setTaskTable(null);
            }
            if (auditId == 1 || auditId == 8 || auditId == 9 || auditId == 12) {
                auditFlowCurrent1.setProjectTable(null);
            }
            ResultAudit resultAudit = new ResultAudit();
            // 任务表TaskTable对象,在实体类中构造该对象
            if (auditFlowCurrent1.getTaskTable() != null && auditFlowCurrent1.getTaskTable().gettId() != null) {
                String title = auditFlowCurrent1.getApplytitle();
                if (MyUtils.isEmpty(title)) {
                    title = auditFlowCurrent1.getTaskTable().getTaskTitle();
                }
                resultAudit.setAuditName(title);// 任务标题
                resultAudit.setTime(auditFlowCurrent1.getAddTime());// 创建时间
                resultAudit.setProjectTable(auditFlowCurrent1.getTaskTable().getProjectTable());
                resultAudit.setTaskTable(auditFlowCurrent1.getTaskTable());
                // 查询审批记录的
                resultAudit.getTaskTable().setProjectTable(null);
            } else {
                // 项目表project_table对象
                if (auditFlowCurrent1.getProjectTable() != null) {
                    String title = auditFlowCurrent1.getApplytitle();
                    if (MyUtils.isEmpty(title)) {
                        title = auditFlowCurrent1.getProjectTable().getTitle();
                    }
                    resultAudit.setAuditName(title);
                    resultAudit.setApprovalUserName(userService.getName(Integer.parseInt(auditFlowCurrent1.getCreateBy())));
                    resultAudit.setTime(auditFlowCurrent1.getAddTime());
                    // 在项目审批中添加项目审批信息
                    resultAudit.setProjectTable(auditFlowCurrent1.getProjectTable());
                }
            }
            List<AuditFlowOperRecord> auditFlowOperRecordList = new ArrayList<>();
            for (AuditFlowOperRecord auditFlowOperRecord : auditFlowCurrent1.getAuditFlowOperRecordSet()) {
                if (auditFlowOperRecord.getRecordId() != null && auditFlowOperRecord.getOperUserId() != null) {
                    if (auditFlowOperRecord.getOperUserId() == Integer.parseInt(ShiroUtils.getUserId() + "")) {
                        resultAudit.setApprovalTime(auditFlowOperRecord.getAddTime());
                    }
                    auditFlowOperRecordList.add(auditFlowOperRecord);
                }
            }
            resultAudit.setCurrentId(auditFlowCurrent1.getCurrentId());
            resultAudit.setAuditFlowOperRecordSet(auditFlowOperRecordList);
            resultAudit.setSubmitter(userService.getName(Integer.parseInt(auditFlowCurrent1.getCreateBy())));
            resultAudit.setAllFlowNodeRoleList(auditFlowCurrent1.getAllFlowNodeRoleList());
            resultAudit.setCurrentNodeName(auditFlowCurrent1.getCurrentNodeName());
            resultAudit.setFlowId(auditFlowCurrent1.getAuditFlow().getFlowId());
            resultAudit.setFlowName(auditFlowCurrent1.getAuditFlow().getFlowName());
            resultAudit.setTypeId(auditFlowCurrent1.getAuditFlow().getTypeId());
            resultAudit.setTypeName(auditFlowCurrent1.getAuditFlow().getTypeName());
            resultAudit.setTypeId(auditFlowCurrent1.getAuditFlow().getTypeId());
            resultAudit.setTypeName(auditFlowCurrent1.getAuditFlow().getTypeName());
            resultAudit.setApplyClassName(auditFlowCurrent1.getAuditFlow().getApplyClassName());
            resultAudit.setApplyTableName(auditFlowCurrent1.getAuditFlow().getApplyTableName());
            resultAudit.setApplyId(auditFlowCurrent1.getApplyId());
            Integer opinionstate = selectOpinion(currentId, auditFlowCurrent, resultAudit);
            if (opinionstate != null) {
                resultAudit.setOpinionState(opinionstate);
            }
            list.add(resultAudit);
        }
        return list;
    }

    /**
     * 判断是否显示审批意见功能:0表示不显示，1表示显示
     *
     * @param currentId
     * @param auditFlowCurrent
     * @param resultAudit
     * @return
     */
    public Integer selectOpinion(Integer currentId, AuditFlowCurrent auditFlowCurrent, ResultAudit resultAudit) {
        Integer opinionstate = 0;
        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
        auditFlowNodeRole.setCurrentId(currentId);
        auditFlowNodeRole.setUserId(ShiroUtils.getUserId().intValue());
        List<AuditFlowNodeRole> listRole = auditFlowNodeRoleMapper.selectByNodeIdAndCurrentId(auditFlowNodeRole);
        auditFlowCurrent.setCurrentId(currentId);
        List<AuditFlowCurrent> list4 = auditFlowCurrentService.selectAuditIdByCurrentId(auditFlowCurrent);
        if (ShiroUtils.isNotEmpty(list4)) {
            if (ShiroUtils.isNotEmpty(listRole)) {
                for (AuditFlowNodeRole s : listRole) {
                    for (AuditFlowCurrent a : list4) {
                        if (a.getCurrentState() == 2 || a.getCurrentState() == 4 || a.getCurrentState() == 6 || a.getCurrentState() == 8) {
                            if (s.getNodeId().equals(a.getCurrentNodeId())) {
                                opinionstate = 1;
                            }
                        }
                    }
                }
            }
        }
        return opinionstate;
    }

    @Override
    public List<ResultAudit> paging(AuditFlowCurrent auditFlowCurrent, List<ResultAudit> list) {
        if (auditFlowCurrent.getTotal() == 0) {
            auditFlowCurrent.setTotal(ConstantUtil.TOTAL);
        }
        if (auditFlowCurrent.getPages() == 0) {
            auditFlowCurrent.setPages(list.size());// 获得的是数据库符合条件的总条数
        }
        if (list.isEmpty()) {
            return null;
        }
        if (list == null) {
            return null;
        }
        Integer count = auditFlowCurrent.getPages(); // 记录总数
        Integer pageCount = 0; // 页数，为总的页数
        if (count % auditFlowCurrent.getTotal() == 0) {//pageSize==total
            pageCount = count / auditFlowCurrent.getTotal();
        } else {
            pageCount = count / auditFlowCurrent.getTotal() + 1;
        }
        int fromIndex = 0; // 开始索引
        int toIndex = 0; // 结束索引
        if (!auditFlowCurrent.getPageNumber().equals(pageCount)) {
            fromIndex = (auditFlowCurrent.getPageNumber() - 1) * auditFlowCurrent.getTotal();
            toIndex = fromIndex + auditFlowCurrent.getTotal();
        } else {
            fromIndex = (auditFlowCurrent.getPageNumber() - 1) * auditFlowCurrent.getTotal();
            toIndex = count;
        }
        List<ResultAudit> resultList = list.subList(fromIndex, toIndex);
        return resultList;
    }

    @Override
    public List<ResultAudit> selectUserNameBycId(List<ResultAudit> list, AuditFlowCurrent auditFlowCurrent) {
        List<ResultAudit> resultList = new ArrayList<>();
        if (ShiroUtils.isNotEmpty(list)) {
            resultList = auditFlowCurrentService.paging(auditFlowCurrent, list);
            for (ResultAudit resultAudit : resultList) {
                resultAudit.setCopyPeronName(copyName(resultAudit));
            }
            if (auditFlowCurrent.getAllow() != null && auditFlowCurrent.getAllow() == 1) {
                for (ResultAudit r : resultList) {
                    r.setState(1);
                    List<Integer> list2 = auditFlowNodeRoleMapper.selectState(r.getCurrentId());
                    for (Integer state : list2) {
                        if ((state != null && state == 0) || r.getCurrentNodeName().equals(ManagerConstant.RESCINDED)) {
                            r.setState(0);
                            break;
                        }
                    }
                }
            }
        }
        return resultList;
    }

    @Override
    public String copyName(ResultAudit resultAudit) {
        String copyName = "";
        String userIds = auditCopyMsgMapper.selectUseIds(resultAudit.getCurrentId());
        if (ShiroUtils.isNotNull(userIds)) {
            List<String> nameList = userMapper.selectNameList(userIds.split(","));
            copyName = String.join(",", nameList);
        }
        return copyName;
    }

    /**
     * 获取审批人
     *
     * @param currentId
     * @return
     */
    @Override
    public List<String> selectApplyUserIdList(Integer currentId) {
        List<String> resultList = new ArrayList<>();
        AuditFlowNodeRole audit = new AuditFlowNodeRole();
        AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
        List<Map<String, Object>> userList = auditFlowNodeRoleMapper.selectUserNodeBycId(currentId);
        // 然后循环遍历，
        if (ShiroUtils.isNotEmpty(userList)) {
            for (Map<String, Object> map : userList) {
                Integer userId = (Integer) map.get("userId");
                String userName = userService.getName(userId);
                if (resultList.contains(userName)) {
                    continue;
                }
                audit.setCurrentId(currentId);
                audit.setUserId(userId);
                Integer nodeRoles = auditFlowNodeRoleMapper.selectFlowNodeRoleCount(audit);

                auditFlowOperRecord.setCurrentId(currentId);
                auditFlowOperRecord.setOperUserId(userId);
                auditFlowOperRecord.setOperType(2);
                // 得到的是全部已经转推过的人
                Integer push = auditFlowOperRecordMapper.selectAuditOperRecordCount(auditFlowOperRecord);
                auditFlowOperRecord.setOperType(0);
                Integer notpass = auditFlowOperRecordMapper.selectAuditOperRecordCount(auditFlowOperRecord);
                auditFlowOperRecord.setOperType(1);
                Integer pass = auditFlowOperRecordMapper.selectAuditOperRecordCount(auditFlowOperRecord);

                if (nodeRoles != null && nodeRoles >= 2 && push < nodeRoles) {
                    resultList.add(userName);
                }
                if (nodeRoles != null && nodeRoles <= 1) {
                    // 该用户没有转推的情况
                    if (push < 1 && (notpass > 0 || pass > 0)) {
                        resultList.add(userName);
                    }
                    // 该用户有转推，可是又有审批的情况

                    if (push >= 1 && (notpass > 0 || pass > 0)) {
                        resultList.add(userName);
                    }
                    // 该用户还没有进行审批的情况
                    if (push < 1 && (notpass < 1 && pass < 1)) {
                        resultList.add(userName);
                    }
                }
            }
        }
        return resultList;
    }

    /**
     * 审核状态1已提交,2审批中,3审批不通过，4审批通过,5审批全部通过,7已经撤销(科研项目和市场项目),8:审批中
     *
     * @param currentNodeId
     * @param auditId
     * @param currentState
     * @param currentNodeName
     * @return
     */
    public String getCurrentNodeName(Integer currentNodeId, Integer auditId, Integer currentState, String currentNodeName) {
        if (currentState == null) {
            currentNodeName = "";
        } else if (currentState == 3) {
            currentNodeName = "审核不通过";
        } else if (currentState == 5) {
            currentNodeName = "审批全部通过";
        } else if (currentState == 6) {
            currentNodeName = "转推";
        } else if (currentState == 7) {
            currentNodeName = "已撤销";
        } else if (currentState == 8) {
            // currentNodeName = "审批中";
            currentNodeName = auditFlowNodeMapper.selectFlowNodeName(auditId, currentNodeId);
            if (StringUtils.isEmpty(currentNodeName)) {
                currentNodeName = "审批中";
            }
        }
        return currentNodeName;
    }
}


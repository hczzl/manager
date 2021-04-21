package com.ruoyi.web.service.audit.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.domain.ResultApprovalRecord;
import com.ruoyi.web.domain.vo.SysProjectTableVo;
import com.ruoyi.web.mapper.audit.AuditCopyMsgMapper;
import com.ruoyi.web.mapper.audit.AuditFlowCurrentMapper;
import com.ruoyi.web.mapper.audit.AuditFlowNodeRoleMapper;
import com.ruoyi.web.mapper.audit.AuditFlowOperRecordMapper;
import com.ruoyi.web.mapper.item.MsgEvtInfoMapper;
import com.ruoyi.web.mapper.item.ResultApprovalRecordMapper;
import com.ruoyi.web.mapper.item.SysProjectTableMapper;
import com.ruoyi.web.service.audit.*;
import com.ruoyi.web.service.item.*;
import com.ruoyi.web.util.ConstantUtil;
import com.ruoyi.web.util.constant.ManagerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author zhongzhilong
 * @date 2019/8/10
 */
@Service
public class AuditServiceImpl implements AuditService {
    @Autowired
    private AuditFlowNodeService auditFlowNodeService;
    @Autowired
    private AuditFlowNodeRoleService auditFlowNodeRoleService;
    @Autowired
    private SysProjectTableMapper sysProjectTableMapper;
    @Autowired
    private AuditFlowCurrentService auditFlowCurrentService;
    @Autowired
    private AuditFlowOperRecordService auditFlowOperRecordService;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private SysProjectTableService sysProjectTableService;
    @Autowired
    private ProjectTaskTableService projectTaskTableService;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;
    @Autowired
    private TaskPostponeService taskPostponeService;
    @Autowired
    private TaskUserService taskUserService;
    @Autowired
    private ResultApprovalRecordService resultApprovalRecordService;
    @Autowired
    private AuditFlowOperRecordMapper auditFlowOperRecordMapper;
    @Autowired
    private AuditFlowNodeRoleMapper auditFlowNodeRoleMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private TaskStopMoneService taskStopMoneService;
    @Autowired
    private ProjectMemoService projectMemoService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ProduceTypeService produceTypeService;
    @Autowired
    private MsgEvtInfoMapper msgEvtInfoMapper;
    @Autowired
    private AuditFlowCurrentMapper auditFlowCurrentMapper;
    @Autowired
    private AuditCopyMsgMapper auditCopyMsgMapper;
    @Autowired
    private ResultApprovalRecordMapper resultApprovalRecordMapper;


    /**
     * 审批接口
     */
    @Override
    public AjaxResult audit(String currentId, String memo, Integer type, String userId, String score) {
        SysUser sysUser = new SysUser();
        // 虚拟得到登录用户的id
        sysUser.setUserId(ShiroUtils.getUserId());
        // 审批流表对象，根据流id，得到该流的对象
        TaskTable taskTable = new TaskTable();
        TaskPostpone taskPostpone = new TaskPostpone();
        AuditFlowNodeRole auditFlowNodeRole2 = new AuditFlowNodeRole();
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        ProjectTaskTable projectTaskTable = new ProjectTaskTable();
        AuditFlowCurrent auditFlowCurrent = auditFlowCurrentService.selectAuditFlowCurrentById(StringUtils.toInteger(currentId));
        if (auditFlowCurrent == null) {
            return AjaxResult.approvalNotExist();
        }
        if (auditFlowCurrent.getCurrentState().equals(ManagerConstant.APPROVAL_RESCINDED)) {
            return AjaxResult.error("该审批已被撤回");
        }
        // 审批类型id
        Integer auditId = auditFlowCurrent.getAuditId();
        Integer nodeRoleNumBer = 0;
        Integer nodeSum = 0;
        Integer p = 0;
        int info = 0;
        if (Arrays.asList(ManagerConstant.PROJECT_AUDIT_TYPE).contains(auditId)) {
            // 项目类型的审批
            AjaxResult ajaxResult = marketApproval(currentId, memo, type, userId, auditFlowCurrent, auditId);
            return ajaxResult;
        } else {
            // 任务类型的审批
            Integer currenNodeId = auditFlowCurrent.getCurrentNodeId();
            // 为了满足同一级节点多人审批的功能
            auditFlowNodeRole2.setNodeId(currenNodeId);
            auditFlowNodeRole2.setCurrentId(Integer.parseInt(currentId));
            nodeRoleNumBer = auditFlowNodeRoleService.selectAuditFlowNodeRoleListSum(auditFlowNodeRole2);
            AuditFlowOperRecord auditFlowOperRecord1 = new AuditFlowOperRecord();
            auditFlowOperRecord1.setCurrentId(StringUtils.toInteger(currentId));
            auditFlowOperRecord1.setCurrentNodeId(auditFlowCurrent.getCurrentNodeId());
            auditFlowOperRecord1.setOperUserId(ShiroUtils.getUserId().intValue());
            auditFlowOperRecord1.setOperType(1);//表示审核通过
            Integer passCount = auditFlowOperRecordMapper.selectAuditFlowOperRecordListCount(auditFlowOperRecord1);
            auditFlowOperRecord1.setOperType(2);//表示该用户转推该审核，自己不能再审核了
            Integer turnCount = auditFlowOperRecordMapper.selectAuditFlowOperRecordListCount(auditFlowOperRecord1);
            if (auditId == 1) {
                if (passCount.equals(nodeRoleNumBer)) {
                    return AjaxResult.success("您已经审核，不需要重新审核！");
                }
            }
            if (auditId != 1) {
                if (passCount > 0) {
                    return AjaxResult.success("您已经审核，不需要重新审核！");
                }
            }
            if (turnCount > ManagerConstant.TASK_MAX_PUSH) {
                return AjaxResult.success("您已经转推！");
            } else {
                List<AuditFlowNodeRole> auditFlowNodeRoleList3 = new ArrayList<>();
                auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
                auditFlowOperRecord1.setCurrentNodeId(currenNodeId);//
                // 得到审批记录表中已经审批了该审批流的条数
                nodeSum = auditFlowOperRecordService.selectAuditFlowOperRecordSum(auditFlowOperRecord1);
                auditFlowNodeRole2.setNodeId(currenNodeId);
                auditFlowNodeRole2.setCurrentId(Integer.parseInt(currentId));
                List<Integer> userIdList = auditFlowNodeRoleMapper.selectApplyUserId(auditFlowNodeRole2);
                // 判断是否还具有审批的权限
                if (nodeSum >= nodeRoleNumBer && nodeRoleNumBer > 0 && nodeSum > 0) {
                    return AjaxResult.error("没有审核权限！");
                } else {
                    boolean uesrYN = false;
                    Integer currentApplyId = sysUser.getUserId().intValue();
                    // 判断是否具有审核的资格
                    if (ShiroUtils.isNotEmpty(userIdList) && userIdList.contains(currentApplyId)) {
                        uesrYN = true;
                    }
                    // 有审核资格
                    if (uesrYN == true) {
                        // 审批节点对象
                        AuditFlowNode auditFlowNode = auditFlowNodeService.selectAuditFlowNodeById(auditFlowCurrent.getCurrentNodeId());
                        // 审批记录表对象
                        AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
                        // 插入数据到审批记录表当中
                        auditFlowOperRecord.setCurrentId(auditFlowCurrent.getCurrentId());
                        auditFlowOperRecord.setCurrentNodeId(auditFlowCurrent.getCurrentNodeId());
                        auditFlowOperRecord.setOperUserId(Math.toIntExact(sysUser.getUserId()));
                        auditFlowOperRecord.setOperType(type);
                        auditFlowOperRecord.setOperMemo(memo);
                        auditFlowOperRecord.setAddTime(new Date());
                        auditFlowOperRecord.setState(1);
                        // node_type---1是开始节点，2是运行节点，3是结束节点          //NODE_TYPR_THREE--如果是结束节点
                        if (auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE)) {
                            auditFlowCurrent.setCurrentNodeId(auditFlowNode.getNodeId());//保存为当前节点
                            if (type == 1) {// type 0不通过,1通过,2转推
                                AuditFlowNodeRole auditFlowNodeRole1 = new AuditFlowNodeRole();
                                auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                                auditFlowNodeRole1.setNodeId(auditFlowCurrent.getCurrentNodeId());
                                auditFlowNodeRole1.setState(0);
                                auditFlowNodeRole1.setUserId(Math.toIntExact(sysUser.getUserId()));
                                auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole1);
                                p = auditFlowOperRecordService.insertAuditFlowOperRecord(auditFlowOperRecord);
                                auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
                                auditFlowOperRecord1.setCurrentNodeId(currenNodeId);
                                nodeSum = auditFlowOperRecordService.selectAuditFlowOperRecordSum(auditFlowOperRecord1);
                                if (nodeSum >= nodeRoleNumBer) {
                                    auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FIVE);
                                    // 发起任务
                                    if (auditFlowCurrent.getAuditId() == 1) {
                                        taskTable.setTaskFinishflag("0");
                                        taskTable.settId((long) auditFlowCurrent.getApplyId());
                                    }
                                    remindApproval(StringUtils.toInteger(currentId), currenNodeId, auditFlowCurrent.getAuditId());
                                    // 审批通过，发送抄送信息
                                    msgCopy(auditFlowCurrent);
                                    info = 1;
                                }
                                info = 2;
                                // 任务中止
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_BREAK)) {
                                    taskTable.setTaskFinishflag("2");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                // 任务完成
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_FINISH)) {
                                    taskTable.setTaskFinishflag("1");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                // 执行任务变更：时间变更和执行人变更，顺延
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_CHANGE)) {
                                    taskPostpone.setState(1);
                                    taskTable.setTaskFinishflag("0");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                            }
                            if (type.equals(ManagerConstant.APPROVAL_TYPE_PUSH)) {// 转推
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_SIX);// 转推中
                                String[] userIds = userId.split(",");// 转推给与哪几个人权限进行审批
                                auditFlowOperRecord.setPushUsers(userId);
                                for (String s : userIds) {
                                    AuditFlowNodeRole auditFlowNodeRole1 = new AuditFlowNodeRole();
                                    // 先把进行转推人的state设为0，表示着该用户已经进行审批了，在审批提醒的时候会起作用
                                    auditFlowNodeRole1.setNodeId(auditFlowCurrent.getCurrentNodeId());
                                    auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                                    auditFlowNodeRole1.setState(0);
                                    auditFlowNodeRole1.setUserId(Math.toIntExact(sysUser.getUserId()));

                                    auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole1);
                                    // 插入入角色验证表
                                    auditFlowNodeRole1.setUserId(Integer.parseInt(s));
                                    auditFlowNodeRole1.setAddTime(new Date());
                                    auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                                    // 审批转推就会将audit_flow_node_role的state设置为1，
                                    auditFlowNodeRole1.setState(1);
                                    auditFlowNodeRoleService.insertAuditFlowNodeRole(auditFlowNodeRole1);
                                    insertApprovlInfo(auditFlowCurrent.getCurrentId(), auditFlowCurrent.getApplyId(), auditFlowCurrent.getAuditId(), Integer.parseInt(s), Math.toIntExact(sysUser.getUserId()), auditFlowCurrent.getCurrentNodeId());
                                    // 审批提醒
                                    insertMsgInfo(auditFlowCurrent.getCurrentId(), auditFlowCurrent.getAuditId(), Integer.parseInt(s));
                                }
                                // 当该用户进行审批，则将该用户的审批消息删掉
                                msgEvtInfo.setEventId(auditFlowCurrent.getCurrentId());
                                msgEvtInfo.setType(3);
                                msgEvtInfo.setUserId(ShiroUtils.getUserId().intValue());
                                msgEvtInfoService.deleteForMsg(msgEvtInfo);
                            }
                            if (type == 0) {
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_THREE);
                                updateApprovalState(auditFlowCurrent);
                                AuditFlowNodeRole auditFlowNodeRole1 = new AuditFlowNodeRole();
                                auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                                auditFlowNodeRole1.setNodeId(auditFlowCurrent.getCurrentNodeId());
                                auditFlowNodeRole1.setState(0);
                                auditFlowNodeRole1.setUserId(Math.toIntExact(sysUser.getUserId()));
                                auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole1);
                                // 当审批信息拒绝通过后，添加消息
                                Integer currentState = auditFlowCurrent.getCurrentState();
                                if (currentState.equals(ManagerConstant.APPROVAL_FAILURE) && Arrays.asList(ManagerConstant.TASK_AUDIT_TYPE).contains(auditId)) {
                                    ResultApprovalRecord resultApprovalRecord = new ResultApprovalRecord();
                                    resultApprovalRecord.setCurrentId(auditFlowCurrent.getCurrentId());
                                    // 排除了转推的情况
                                    resultApprovalRecord.setState(1);
                                    List<Integer> listUsers = resultApprovalRecordMapper.selectUserIdList(resultApprovalRecord);
                                    // 登录用户：即转推人
                                    int userId2 = ShiroUtils.getUserId().intValue();
                                    // 审批流发起人
                                    Integer createBy = Integer.parseInt(auditFlowCurrent.getCreateBy());
                                    MsgEvtInfo msg = new MsgEvtInfo();
                                    msg.setAuditId(auditId);
                                    msg.setType(9);
                                    msg.setEventId(auditFlowCurrent.getCurrentId());
                                    msg.setCreateApproval(Integer.parseInt(auditFlowCurrent.getCreateBy()));
                                    msg.setCreateTime(new Date());
                                    for (Integer approvalUserId : listUsers) {
                                        if (!approvalUserId.equals(createBy) && approvalUserId != userId2) {
                                            msg.setUserId(approvalUserId);
                                            msgEvtInfoService.insertMessageInfo(msg);
                                        }
                                    }
                                    if (createBy != userId2) {
                                        msg.setUserId(createBy);
                                        msgEvtInfoService.insertMessageInfo(msg);
                                    }
                                }
                                // 发起任务审批，且是拒批的情况
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_TASK) && currentState.equals(ManagerConstant.APPROVAL_FAILURE)) {
                                    projectTaskTable.setEventId(auditFlowCurrent.getApplyId());
                                    projectTaskTable.setTypeId(0);
                                    projectTaskTableService.deleteInfos(projectTaskTable);
                                }
                                // 任务中止失败
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_BREAK)) {
                                    taskTable.setTaskFinishflag("0");
                                    String stopMemo = auditFlowCurrent.getStopMone();
                                    taskTable.setStopMone(stopMemo);//任务中止失败的原因
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                // 任务完成失败
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_FINISH)) {
                                    taskTable.setTaskFinishflag("0");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                // 执行任务的时间变更失败，执行任务执行人变更失败
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_CHANGE)) {
                                    taskTable.setTaskFinishflag("0");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                info = 1;
                                // 当审批失败的时候，会将抄送删除
                                auditCopyMsgMapper.deleteCopyMsg(auditFlowCurrent.getCurrentId());
                            }
                        }
                        if (info != 0) {
                            // 当该审批的所有人都审批通过之后才将该信息置为已读
                            int q = 0;
                            if (info == 1) {
                                Integer eventId = auditFlowCurrent.getCurrentId();
                                Integer aId = auditFlowCurrent.getAuditId();
                                MsgEvtInfo msgEvtInfo2 = new MsgEvtInfo();
                                msgEvtInfo2.setEventId(eventId);
                                msgEvtInfo2.setType(3);
                                msgEvtInfo2.setReadMark(1);
                                msgEvtInfo2.setAuditId(aId);
                                msgEvtInfoService.updateMessageInfo(msgEvtInfo2);
                            }
                            if (info == 2) {
                                msgEvtInfo.setEventId(auditFlowCurrent.getCurrentId());
                                msgEvtInfo.setUserId(Math.toIntExact(sysUser.getUserId()));
                                msgEvtInfo.setAuditId(auditFlowCurrent.getAuditId());
                                msgEvtInfo.setType(3);
                                msgEvtInfo.setReadMark(1);
                                msgEvtInfoService.updateMessageInfo(msgEvtInfo);
                            }
                        }
                        int a = 0;
                        if (p != 1) {
                            a = auditFlowOperRecordService.insertAuditFlowOperRecord(auditFlowOperRecord);
                        }
                        if (p == 1) {
                            a = 1;
                        }
                        int b = auditFlowCurrentService.updateAuditFlowCurrent(auditFlowCurrent);
                        // 处理立项失败的接口
                        updateRoleOper(auditFlowCurrent.getCurrentId());
                        // 任务发起、任务中止审批通过
                        if (type == 1 && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_BREAK) || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_TASK)) && nodeSum >= nodeRoleNumBer) {
                            String stopMemo = null;
                            String stopUser = sysUserService.getName(ShiroUtils.getUserId());
                            taskTable.setStopUser(stopUser);
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_BREAK)) {
                                TaskStopMone taskStopMone = new TaskStopMone();
                                taskStopMone.settId(auditFlowCurrent.getApplyId());
                                List<TaskStopMone> listMone = taskStopMoneService.selectMax(taskStopMone);
                                for (TaskStopMone t : listMone) {
                                    stopMemo = t.getStopMone();
                                }
                                taskTable.setStopMone(stopMemo);// 任务中止的原因
                            }
                            taskTable.settId(auditFlowCurrent.getApplyId().longValue());
                            taskTable.setUpdateTime(new Date());
                            taskTable.setTaskOverdueState("1");
                            taskTableService.updateTaskTable(taskTable);
                        }
                        // 审批失败的原因
                        Integer[] auditIds = {8, 9, 12};
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && Arrays.asList(auditIds).contains(auditFlowCurrent.getAuditId())) {
                            taskTable.setUpdateTime(new Date());
                            taskTableService.updateTaskTable(taskTable);
                        }
                        // 任务完成审批通过
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS) && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_FINISH) && nodeSum >= nodeRoleNumBer) {
                            taskTable.setTaskScore(Double.parseDouble(score));
                            String taskRater = ShiroUtils.getSysUser().getUserName();
                            taskTable.setTaskRater(taskRater);
                            taskTableService.updateTaskTable(taskTable);
                            ShiroUtils.setTaskScore(0);
                            taskTable.settId((long) auditFlowCurrent.getApplyId());
                            // 根据任务id，获取项目id
                            int schedule = taskTableService.selectTaskSchedule(taskTable);
                            if (schedule != 100) {
                                taskTable.setScheduleRate(100);
                                taskTable.setUpdateTime(new Date());
                                taskTableService.updateTaskTable(taskTable);
                            }
                        }
                        // 任务变更
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS) && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_CHANGE)) && nodeSum >= nodeRoleNumBer) {
                            TaskPostpone taskPostpone1 = new TaskPostpone();
                            taskPostpone1.setState(1);
                            taskPostpone1.settId(auditFlowCurrent.getApplyId().longValue());
                            taskPostpone1.setCurrentId(auditFlowCurrent.getCurrentId());
                            taskPostponeService.updateTaskPostpone(taskPostpone1);
                            // 获得该id对应的所有数据,且该条数据的state不等于0
                            taskPostpone.settId(auditFlowCurrent.getApplyId().longValue());
                            taskPostpone.setCurrentId(auditFlowCurrent.getCurrentId());
                            taskPostpone.setIsNew(1);
                            taskPostpone.setState(1);
                            List<TaskPostpone> listPone = taskPostponeService.selectById(taskPostpone);
                            if (ShiroUtils.isNotEmpty(listPone)) {
                                for (TaskPostpone taskPostpone2 : listPone) {
                                    taskTable.settId(taskPostpone2.gettId());
                                    taskTable.setStartTime(taskPostpone2.getStartTime());
                                    taskTable.setEndTime(taskPostpone2.getEndTime());
                                    // 周期也随着进行改变
                                    taskTable.setPeriod(taskPostpone2.getPeriod());
                                    taskTable.setUpdateBy(ShiroUtils.getUserId() + "");
                                    taskTable.setUpdateTime(new Date());// 更新时间
                                }
                            }
                            Integer chargePeopleId = ShiroUtils.getChargeId();
                            if (chargePeopleId == null) {
                                chargePeopleId = 0;
                            }
                            String participants = ShiroUtils.getParticipant();
                            if (ShiroUtils.isNotNull(participants)) {
                                // 先删除后插入
                                taskUserService.deleteTaskUserById(auditFlowCurrent.getApplyId().longValue());
                                TaskUser taskUser = new TaskUser();
                                String[] arr = participants.split(",");
                                for (String tUser : arr) {
                                    if (Integer.parseInt(tUser) != chargePeopleId) {
                                        taskUser.settId(auditFlowCurrent.getApplyId().longValue());
                                        taskUser.setUserId(Long.parseLong(tUser));
                                        taskUserService.insertTaskUser(taskUser);
                                    }
                                }
                            }
                            taskTable.setChargepeopleId(chargePeopleId.longValue());
                            taskTable.setUpdateTime(new Date());
                            Integer isPostpone = ShiroUtils.getFlowId();
                            // 执行顺延
                            if (isPostpone != null && isPostpone == 1) {
                                taskPostponeService.taskPostpone(taskTable);
                            }
                            // 更新任务表内容
                            taskTable.setTaskOverdueState("1");
                            taskTableService.updateTaskTable(taskTable);
                            // 更新projectTaskTable表内容
                            updateContent((long) auditFlowCurrent.getApplyId(), chargePeopleId.longValue(), participants);
                        }
                        // 更新审批记录表的信息
                        if (auditFlowCurrent.getCurrentId() != null) {
                            updateApprovalRecord(auditFlowCurrent.getCurrentId());
                        }
                        if (a > 0 && b > 0) {
                            return AjaxResult.success("审批成功！");
                        } else {
                            return AjaxResult.error("系统异常！");
                        }
                    } else {
                        return AjaxResult.error("没有审核权限！");
                    }
                }
            }
        }
        /*if (code == 1) {
            return AjaxResult.success("审批成功！");
        } else if (code == 2) {
            return AjaxResult.error("系统异常！");
        } else if (code == 3 | code == 4) {
            return AjaxResult.error("没有审核权限！");
        } else if (code == 6) {
            return AjaxResult.success("您已经审核，不需要重新审核！");
        } else if (code == 5) {
            return AjaxResult.success("您已经转推！");
        } else {
            return AjaxResult.error("操作失败！");
        }*/
    }

    /**
     * 市场立项执行的接口
     *
     * @param currentId
     * @param memo
     * @param type
     * @param userId
     * @param auditFlowCurrent
     * @param auditId
     * @return
     */
    public AjaxResult marketApproval(String currentId, String memo, Integer type, String userId, AuditFlowCurrent auditFlowCurrent, Integer auditId) {
        SysProjectTable sysProjectTable = new SysProjectTable();
        AuditFlowNodeRole auditFlowNodeRole2 = new AuditFlowNodeRole();
        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        SysUser sysUser = new SysUser();
        sysUser.setUserId(ShiroUtils.getUserId());
        ProjectTaskTable projectTaskTable = new ProjectTaskTable();
        // 当前审批流的节点
        Integer currenNodeId = auditFlowCurrent.getCurrentNodeId();
        Integer nodeSeqno1 = 0;
        Integer nodeId = 0;
        Integer nodeRoleNumBer = 0;
        int a = 0;
        int b = 0;
        int info = 0;
        // 审批通过的数量
        Integer nodeIdPass = 0;
        // 审批不通过的数量
        Integer nodeIdNotPass = 0;
        boolean go = false;
        int p = 0;
        Integer nodeSum = 0;
        // 根据flowId获得对应的审批的级数
        AuditFlowNode auditFlowNode1 = new AuditFlowNode();
        auditFlowNode1.setFlowId(auditId);
        List<AuditFlowNode> list2 = auditFlowNodeService.selectFlowId(auditFlowNode1);
        Integer nodeSeqno = list2.get(0).getNodeSeqno();
        for (int i = 0; i < list2.size(); i++) {
            nodeSeqno1 = list2.get(i).getNodeSeqno();
            auditFlowNode1.setFlowId(auditId);
            auditFlowNode1.setNodeSeqno(nodeSeqno1);
            // 该级数对应的节点
            nodeId = auditFlowNodeService.selectNodeIdBySepno(auditFlowNode1);
            if (nodeId.equals(currenNodeId)) {
                auditFlowNodeRole2.setNodeId(nodeId);
                // 计算得到该节点有多少个审批人
                auditFlowNodeRole2.setCurrentId(StringUtils.toInteger(currentId));// 有审批流id
                nodeRoleNumBer = auditFlowNodeRoleService.selectAuditFlowNodeRoleListSum(auditFlowNodeRole2);
                break;
            }
        }
        AuditFlowOperRecord auditFlowOperRecord1 = new AuditFlowOperRecord();
        auditFlowOperRecord1.setCurrentId(StringUtils.toInteger(currentId));
        auditFlowOperRecord1.setCurrentNodeId(auditFlowCurrent.getCurrentNodeId());
        auditFlowOperRecord1.setOperType(1);// 表示审核通过
        Integer passCount = auditFlowOperRecordMapper.selectAuditFlowOperRecordListCount(auditFlowOperRecord1);
        auditFlowOperRecord1.setOperType(2);// 表示该用户转推该审核，自己不能再审核了
        Integer turnCount = auditFlowOperRecordMapper.selectAuditFlowOperRecordListCount(auditFlowOperRecord1);
        if (passCount.equals(nodeRoleNumBer)) {
            // code = 6;
            return AjaxResult.success("您已经审核，不需要重新审核！");
        }
        if (turnCount > ManagerConstant.TASK_MAX_PUSH) {
            // code = 5;
            return AjaxResult.success("您已经转推！");
        } else {
            // 计算该节点已经审批过的人数
            auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
            auditFlowOperRecord1.setCurrentNodeId(currenNodeId);
            nodeSum = auditFlowOperRecordService.selectAuditFlowOperRecordSum(auditFlowOperRecord1);
            // 判断当前用户是否有权限审批
            auditFlowNodeRole2.setNodeId(currenNodeId);
            auditFlowNodeRole2.setCurrentId(Integer.parseInt(currentId));
            List<Integer> userIdList = auditFlowNodeRoleMapper.selectApplyUserId(auditFlowNodeRole2);
            // 判断是否还具有审批的权限
            if (nodeSum >= nodeRoleNumBer && nodeRoleNumBer > 0 && nodeSum > 0) {
                // code = 4;
                return AjaxResult.error("没有审核权限！");
            } else {
                // 判断是否具有审核的资格
                boolean uesrYN = false;
                Integer currentApplyId = sysUser.getUserId().intValue();
                if (ShiroUtils.isNotEmpty(userIdList) && userIdList.contains(currentApplyId)) {
                    uesrYN = true;
                }
                // 有审核资格
                if (uesrYN == true) {
                    //审批节点对象
                    AuditFlowNode auditFlowNode = auditFlowNodeService.selectAuditFlowNodeById(auditFlowCurrent.getCurrentNodeId());
                    // 审批记录表对象
                    AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
                    auditFlowOperRecord.setCurrentId(auditFlowCurrent.getCurrentId());
                    auditFlowOperRecord.setCurrentNodeId(auditFlowCurrent.getCurrentNodeId());
                    auditFlowOperRecord.setOperUserId(Math.toIntExact(sysUser.getUserId()));
                    auditFlowOperRecord.setOperType(type);
                    auditFlowOperRecord.setOperMemo(memo);
                    auditFlowOperRecord.setAddTime(new Date());
                    auditFlowOperRecord.setState(1);
                    // node_type---1是开始节点，2是运行节点，3是结束节点
                    // 判断当前审批是否为最后一级审批
                    if (auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE)) {
                        // 保存为当前节点
                        auditFlowCurrent.setCurrentNodeId(auditFlowNode.getNodeId());
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS)) {// type=1 审核通过 type=0 审批不通过 type =2 转推
                            AuditFlowNodeRole auditFlowNodeRole1 = new AuditFlowNodeRole();
                            auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                            auditFlowNodeRole1.setNodeId(auditFlowCurrent.getCurrentNodeId());
                            auditFlowNodeRole1.setState(0);
                            auditFlowNodeRole1.setUserId(Math.toIntExact(sysUser.getUserId()));
                            auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole1);
                            // 存入至审批记录表中
                            p = auditFlowOperRecordService.insertAuditFlowOperRecord(auditFlowOperRecord);
                            auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
                            auditFlowOperRecord1.setCurrentNodeId(currenNodeId);
                            // 获取当前节点已审批人数
                            nodeSum = auditFlowOperRecordService.selectAuditFlowOperRecordSum(auditFlowOperRecord1);
                            // 市场立项最后一级审批和科研项目中止，两人通过即可
                            // 市场立项
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)
                                    // 市场项目中止
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)
                                    // 科研项目中止
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                auditFlowOperRecord1.setOperType(1);
                                nodeIdPass = auditFlowOperRecordMapper.selectAuditOperRecordCount(auditFlowOperRecord1);
                                // 满足条件，将审批状态更改为审批全部通过
                                if (nodeIdPass >= 2) {
                                    auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FIVE);
                                    // 市场立项
                                    if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)) {
                                        sysProjectTable.setEstablishStatus("1");
                                    }
                                    // 市场项目中止
                                    if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)) {
                                        sysProjectTable.setProjectFinishFlag("2");
                                    }
                                    // 科研项目中止
                                    if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                        sysProjectTable.setProjectFinishFlag("2");
                                    }
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                    // 将未审批的状态更改为立项完成
                                    updateFinish(auditFlowCurrent);
                                    // 进行抄送操作
                                    msgCopy(auditFlowCurrent);
                                    info = 1;
                                }
                            }
                            // 科研项目第二级审批处理流程
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && nodeRoleNumBer >= 3) {
                                auditFlowOperRecord1.setOperType(1);
                                nodeIdPass = auditFlowOperRecordMapper.selectAuditOperRecordCount(auditFlowOperRecord1);
                                // 当该级审批有超过2人情况，两人通过即可
                                if (nodeIdPass >= 2) {
                                    auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FIVE);
                                    sysProjectTable.setEstablishStatus("1");
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                    updateFinish(auditFlowCurrent);
                                    msgCopy(auditFlowCurrent);
                                    info = 1;
                                }
                            } else if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && nodeRoleNumBer < 3) {
                                // 当该级审批是小于等于2人情况，一人通过即可
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FIVE);
                                sysProjectTable.setEstablishStatus("1");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                info = 1;
                            }
                            // 满足审批通过后进行的处理操作
                            if (nodeSum >= nodeRoleNumBer & nodeSum > 0 & nodeRoleNumBer > 0) {
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FIVE);
                                // 市场项目完成
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH)) {
                                    sysProjectTable.setProjectFinishFlag("1");
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                }
                                // 科研项目完成
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH)) {
                                    sysProjectTable.setProjectFinishFlag("1");
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                }
                                // 当该节点已经都审批结束，才开始提醒下一个审批节点的用户；
                                remindApproval(StringUtils.toInteger(currentId), currenNodeId, auditFlowCurrent.getAuditId());
                                // 抄送操作
                                msgCopy(auditFlowCurrent);
                                info = 1;
                            }
                            info = 2;
                        }
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE)) {
                            // 市场立项、科研项目中止审批不通过的处理操作
                            // 市场立项
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)
                                    // 科研立项
                                    || (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && nodeRoleNumBer >= 3)
                                    // 市场项目中止
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)
                                    // 科研项目中止
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                p = auditFlowOperRecordService.insertAuditFlowOperRecord(auditFlowOperRecord);
                                auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
                                auditFlowOperRecord1.setCurrentNodeId(currenNodeId);
                                auditFlowOperRecord1.setOperType(0);
                                nodeIdNotPass = auditFlowOperRecordMapper.selectAuditOperRecordCount(auditFlowOperRecord1);
                                if (nodeIdNotPass >= 2) {
                                    // 中止失败
                                    if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)
                                            || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                        sysProjectTable.setProjectFinishFlag("0");
                                    } else if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)
                                            || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT)) {
                                        //立项失败
                                        sysProjectTable.setEstablishStatus("2");
                                    }
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                    auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_THREE);
                                    // 将未审批的操作更改为立项中止
                                    updateApprovalState(auditFlowCurrent);
                                    info = 1;
                                } else {
                                    auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_EIGHT);
                                    info = 2;
                                }
                            } else {
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_THREE);
                                updateApprovalState(auditFlowCurrent);
                            }
                            AuditFlowNodeRole auditFlowNodeRole1 = new AuditFlowNodeRole();
                            auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                            auditFlowNodeRole1.setNodeId(auditFlowCurrent.getCurrentNodeId());
                            auditFlowNodeRole1.setState(0);
                            auditFlowNodeRole1.setUserId(Math.toIntExact(sysUser.getUserId()));
                            auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole1);
                            Integer currentState = auditFlowCurrent.getCurrentState();
                            // 添加拒批消息
                            insertMsgNotApproval(auditFlowNodeRole, auditFlowCurrent.getCurrentId(), auditFlowCurrent, auditId, msgEvtInfo);
                            // 市场立项
                            if ((auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)
                                    // 科研立项
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT)) && currentState.equals(ManagerConstant.APPROVAL_FAILURE)) {
                                projectTaskTable.setEventId(auditFlowCurrent.getApplyId());
                                projectTaskTable.setTypeId(1);
                                projectTaskTableService.deleteInfos(projectTaskTable);
                                info = 1;
                            }
                            // 科研项目立项审批不通过
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && nodeRoleNumBer < 3) {
                                sysProjectTable.setEstablishStatus("2");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                info = 1;
                            }
                            /******市场项目*****/
                            // 市场项目完成审批不通过
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                info = 1;
                            }
                            /*******科研项目***/
                            // 科研项目完成
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                info = 1;
                            }
                            // 删除抄送信息
                            auditCopyMsgMapper.deleteCopyMsg(auditFlowCurrent.getCurrentId());
                        }
                    } else {
                        if (type == 1) {
                            AuditFlowNodeRole auditFlowNodeRole1 = new AuditFlowNodeRole();
                            auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                            auditFlowNodeRole1.setNodeId(auditFlowCurrent.getCurrentNodeId());
                            auditFlowNodeRole1.setState(0);
                            auditFlowNodeRole1.setUserId(Math.toIntExact(sysUser.getUserId()));
                            auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole1);
                            p = auditFlowOperRecordService.insertAuditFlowOperRecord(auditFlowOperRecord);
                            auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
                            auditFlowOperRecord1.setCurrentNodeId(currenNodeId);
                            nodeSum = auditFlowOperRecordService.selectAuditFlowOperRecordSum(auditFlowOperRecord1);
                            // 保存为下一个节点
                            if (nodeSum >= nodeRoleNumBer & nodeSum > 0 & nodeRoleNumBer > 0) {
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FOUR);
                                auditFlowCurrent.setCurrentNodeId(auditFlowNode.getNextNode());
                                // 获得技术负责人
                                if (auditId.equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && nodeSeqno == 1 && currenNodeId == 2) {
                                    Integer applyId = auditFlowCurrent.getApplyId();
                                    Map<String, Object> map = sysProjectTableService.selectTechniquePeopleId(applyId);
                                    Integer techniqueId = (Integer) map.get("techniquePeopleId");
                                    msgEvtInfo.setType(5);
                                    msgEvtInfo.setEventId(applyId);
                                    msgEvtInfo.setAuditId(3);
                                    msgEvtInfo.setUserId(techniqueId);
                                    msgEvtInfo.setCreateTime(new Date());
                                    msgEvtInfoService.insertMessageInfo(msgEvtInfo);
                                }
                                remindApproval(StringUtils.toInteger(currentId), currenNodeId, auditFlowCurrent.getAuditId());
                            }
                            info = 2;
                        }
                        if (type == 0) {
                            AuditFlowNodeRole auditFlowNodeRole1 = new AuditFlowNodeRole();
                            auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                            auditFlowNodeRole1.setNodeId(auditFlowCurrent.getCurrentNodeId());
                            auditFlowNodeRole1.setState(0);
                            auditFlowNodeRole1.setUserId(Math.toIntExact(sysUser.getUserId()));
                            auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole1);
                            // 市场立项技术总体审批不同诺的
                            if (auditId.equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currenNodeId == 3) {
                                // 将整条审批流的流向固定为审批中
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FOUR);
                                auditFlowCurrent.setCurrentNodeId(auditFlowNode.getNextNode());
                                go = true;
                            } else {
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_THREE);
                                updateApprovalState(auditFlowCurrent);
                            }
                            // 只要是拒批，都会发消息
                            insertMsgNotApproval(auditFlowNodeRole, auditFlowCurrent.getCurrentId(), auditFlowCurrent, auditId, msgEvtInfo);
                            // 市场项目立项
                            if (((auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currenNodeId != 3)
                                    // 科研项目立项
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT))) {
                                projectTaskTable.setEventId(auditFlowCurrent.getApplyId());
                                projectTaskTable.setTypeId(1);
                                projectTaskTableService.deleteInfos(projectTaskTable);
                            }
                            // 项目立项审批不通过
                            if ((auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currenNodeId != 3) || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT)) {
                                sysProjectTable.setEstablishStatus("2");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // 市场项目完成审批不通过
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // 市场项目中止审批不通过
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // 科研项目中止审批不通过
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // 科研项目完成审批不通过
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // 当审批失败的时候，会将抄送删除
                            auditCopyMsgMapper.deleteCopyMsg(auditFlowCurrent.getCurrentId());
                            info = 1;
                        }
                    }
                    if (info != 0) {
                        // 当该审批的所有人都审批通过之后才将该信息置为已读
                        int[] msgType = {3, 5, 6, 11};
                        List<Integer> typeList = Arrays.stream(msgType).boxed().collect(Collectors.toList());
                        if (info == 1) {
                            MsgEvtInfo msgEvtInfo2 = new MsgEvtInfo();
                            msgEvtInfo2.setTypeList(typeList);
                            msgEvtInfo2.setEventId(auditFlowCurrent.getCurrentId());
                            msgEvtInfo2.setReadMark(1);
                            msgEvtInfo2.setUpdateTime(new Date());
                            msgEvtInfoService.updateMessageInfo2(msgEvtInfo2);
                            msgEvtInfo2.setEventId(auditFlowCurrent.getApplyId());
                            msgEvtInfoService.updateMessageInfo2(msgEvtInfo2);
                        }
                        if (info == 2) {
                            if (auditId.equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currenNodeId == 2) {
                                Iterator<Integer> iterators = typeList.iterator();
                                while (iterators.hasNext()) {
                                    int content = iterators.next();
                                    if (content == 5) {
                                        iterators.remove();
                                    }
                                }
                            }
                            msgEvtInfo.setTypeList(typeList);
                            msgEvtInfo.setEventId(auditFlowCurrent.getCurrentId());
                            msgEvtInfo.setUserId(Math.toIntExact(sysUser.getUserId()));
                            msgEvtInfo.setReadMark(1);
                            msgEvtInfo.setUpdateTime(new Date());
                            msgEvtInfoService.updateMessageInfo2(msgEvtInfo);
                            msgEvtInfo.setEventId(auditFlowCurrent.getApplyId());
                            msgEvtInfoService.updateMessageInfo2(msgEvtInfo);
                        }
                    }
                    // 提醒产品经理
                    if (go == true && auditFlowCurrent.getApplyId() != null && auditFlowCurrent.getCurrentId() != null) {
                        remindManager(auditFlowCurrent.getApplyId(), auditFlowCurrent.getCurrentId());
                    }
                    if (p != 1) {
                        a = auditFlowOperRecordService.insertAuditFlowOperRecord(auditFlowOperRecord);
                    }
                    if (p == 1) {
                        a = 1;
                    }
                    b = auditFlowCurrentService.updateAuditFlowCurrent(auditFlowCurrent);
                    // 处理立项失败的接口
                    updateRoleOper(auditFlowCurrent.getCurrentId());
                    // 市场项目完成审批通过
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS) && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH) && nodeSum >= nodeRoleNumBer) {
                        sysProjectTable.setPlanRate(100);
                        sysProjectTableService.updateProject(sysProjectTable);
                        ProjectMemo projectMemo = new ProjectMemo();
                        projectMemo.setProjectId(auditFlowCurrent.getApplyId());
                        projectMemo.setType(0);
                        projectMemo.setState(1);
                        projectMemo.setUpdateTime(new Date());
                        projectMemoService.updateState(projectMemo);
                    }
                    if (type == 1 && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK) && nodeIdPass >= 2) {
                        sysProjectTableService.updateProject(sysProjectTable);
                        ProjectMemo projectMemo = new ProjectMemo();
                        projectMemo.setProjectId(auditFlowCurrent.getApplyId());
                        projectMemo.setType(1);
                        projectMemo.setState(1);
                        projectMemo.setUpdateTime(new Date());
                        projectMemoService.updateState(projectMemo);
                    }
                    // 市场立项,科研项目中止
                    if (type == 1 && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) && nodeIdPass >= 2) {
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                        // 删除第三个领导的消息提醒
                        updateMag(auditFlowNodeRole, currenNodeId, auditFlowCurrent, msgEvtInfo);
                    }
                    // 科研立项
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS) && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && currenNodeId == 7 && nodeIdPass >= 2) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                        updateMag(auditFlowNodeRole, currenNodeId, auditFlowCurrent, msgEvtInfo);
                    } else if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS) && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH)) && nodeSum >= nodeRoleNumBer) {
                        // 科研项目的完成审批、科研项目发起审批通过情况
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                    }
                    // 立项失败的标识
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) || (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currenNodeId != 3) && auditFlowCurrent.getCurrentNodeId() != 5)) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                    }
                    // 立项失败的标识
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && nodeIdNotPass >= 2 && auditFlowCurrent.getCurrentNodeId() == 5) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                    }
                    // 科研项目中止审批失败
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK) && nodeIdNotPass >= 2 && auditFlowCurrent.getCurrentNodeId() == 21) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                        updateMag(auditFlowNodeRole, currenNodeId, auditFlowCurrent, msgEvtInfo);
                    } else if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK) && auditFlowCurrent.getCurrentNodeId() != 21) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                    }
                    // 市场项目的完成审批失败和科研项目的完成审批失败，即将状态恢复回初始状态
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH) || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH))) {
                        sysProjectTableService.updateProject(sysProjectTable);//此时完成标识置为0
                    }
                    // 市场项目中止审批失败
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK) && nodeIdNotPass >= 2 && currenNodeId == 17)) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateProject(sysProjectTable);
                        // 删除第三个领导的审批提醒
                        updateMag(auditFlowNodeRole, currenNodeId, auditFlowCurrent, msgEvtInfo);
                    } else if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK) && currenNodeId != 17) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateProject(sysProjectTable);
                    }
                    // 更新审批记录表的信息
                    if (auditFlowCurrent.getCurrentId() != null) {
                        updateApprovalRecord(auditFlowCurrent.getCurrentId());
                    }
                    if (a > 0 && b > 0) {
                        // code = 1;
                        return AjaxResult.success("审批成功！");
                    } else {
                        // code = 2;
                        return AjaxResult.error("系统异常！");
                    }
                } else {
                    // code = 3;
                    return AjaxResult.error("没有审核权限！");
                }
            }
        }
    }

    /**
     * 拒批发消息的接口
     *
     * @param auditFlowNodeRole
     * @param cId
     * @param auditFlowCurrent
     * @param auditId
     * @param msgEvtInfo
     */
    public void insertMsgNotApproval(AuditFlowNodeRole auditFlowNodeRole, Integer cId, AuditFlowCurrent auditFlowCurrent, Integer auditId, MsgEvtInfo msgEvtInfo) {
        List<Map<String, Object>> listUsers = auditFlowNodeRoleMapper.selectUserNodeBycId(cId);
        long createBy = Integer.parseInt(auditFlowCurrent.getCreateBy());
        int uId = ShiroUtils.getUserId().intValue();
        msgEvtInfo.setAuditId(auditId);
        msgEvtInfo.setType(8);
        msgEvtInfo.setEventId(cId);
        msgEvtInfo.setCreateApproval(Integer.parseInt(auditFlowCurrent.getCreateBy()));
        msgEvtInfo.setCreateTime(new Date());
        if (ShiroUtils.isNotEmpty(listUsers)) {
            for (Map<String, Object> map : listUsers) {
                Integer userId = (Integer) map.get("userId");
                if (userId != null && userId != createBy && userId != uId) {
                    msgEvtInfo.setUserId(userId);
                    msgEvtInfoService.insertMessageInfo(msgEvtInfo);
                }
            }
        }
        if (createBy != uId) { // 当前用户不是发起人的情况
            msgEvtInfo.setUserId((int) createBy);
            msgEvtInfoService.insertMessageInfo(msgEvtInfo);
        }
    }

    /**
     * 删除第三个领导的消息提醒
     *
     * @param auditFlowNodeRole
     * @param currenNodeId
     * @param auditFlowCurrent
     * @param msgEvtInfo
     */
    public void updateMag(AuditFlowNodeRole auditFlowNodeRole, Integer currenNodeId, AuditFlowCurrent auditFlowCurrent, MsgEvtInfo msgEvtInfo) {
        AuditFlowNodeRole auditFlowNodeRole2 = new AuditFlowNodeRole();
        auditFlowNodeRole2.setState(1);
        auditFlowNodeRole2.setNodeId(currenNodeId);
        auditFlowNodeRole2.setCurrentId(auditFlowCurrent.getCurrentId());
        List<Integer> userIdList = auditFlowNodeRoleMapper.selectApplyUserId(auditFlowNodeRole2);
        for (Integer userId : userIdList) {
            msgEvtInfo.setType(3);
            msgEvtInfo.setEventId(auditFlowCurrent.getCurrentId());
            msgEvtInfo.setAuditId(3);
            msgEvtInfo.setUserId(userId);
            msgEvtInfoService.deleteForMsg(msgEvtInfo);
        }
    }

    /**
     * 当审批流中有拒批，则会将相应的审批信息置为审批中止
     *
     * @param auditFlowCurrent
     */
    public void updateApprovalState(AuditFlowCurrent auditFlowCurrent) {
        ResultApprovalRecord record = new ResultApprovalRecord();
        record.setCurrentId(auditFlowCurrent.getCurrentId());
        List<ResultApprovalRecord> recordList = resultApprovalRecordService.selectApprovalRecord(record);
        if (!ShiroUtils.isEmpty(recordList)) {
            for (ResultApprovalRecord records : recordList) {
                if (records.getApprovalState().equals(ManagerConstant.NOT_APPROVAL)) {
                    record.setCurrentId(records.getCurrentId());
                    record.setApplyId(records.getApplyId());
                    record.setAuditId(records.getAuditId());
                    record.setNumber(records.getNumber());
                    record.setApprovalState("审批中止");
                    record.setApprovalUserId(records.getApprovalUserId());
                    resultApprovalRecordService.updateApprovalRecord(record);
                }
            }
        }
    }

    /**
     * 市场立项部门领导哪一级，若存在审批中，则改为审批完成
     *
     * @param auditFlowCurrent
     */
    public void updateFinish(AuditFlowCurrent auditFlowCurrent) {
        Integer auditId = auditFlowCurrent.getAuditId();
        ResultApprovalRecord record = new ResultApprovalRecord();
        record.setCurrentId(auditFlowCurrent.getCurrentId());
        List<ResultApprovalRecord> recordList = resultApprovalRecordService.selectApprovalRecord(record);
        if (!ShiroUtils.isEmpty(recordList)) {
            for (ResultApprovalRecord records : recordList) {
                if (records.getApprovalState().equals(ManagerConstant.NOT_APPROVAL)) {
                    record.setCurrentId(records.getCurrentId());
                    record.setApplyId(records.getApplyId());
                    record.setAuditId(records.getAuditId());
                    record.setNumber(records.getNumber());
                    if (auditId == 3 || auditId == 7) {
                        record.setApprovalState("立项完成");
                    } else if (auditId == 10 || auditId == 13) {
                        record.setApprovalState("中止完成");
                    }
                    record.setApprovalUserId(records.getApprovalUserId());
                    resultApprovalRecordService.updateApprovalRecord(record);
                }
            }
        }
    }

    public void insertApprovlInfo(int currentId, int applyId, int auditId, int userId, Integer users, int currentNodeId) {
        ResultApprovalRecord resultApprovalRecord = new ResultApprovalRecord();
        resultApprovalRecord.setCurrentId(currentId);
        resultApprovalRecord.setApplyId(applyId);
        resultApprovalRecord.setAuditId(auditId);
        resultApprovalRecord.setApprovalUserId(users);
        resultApprovalRecord.setApprovalUserName(userService.getName(userId));
        resultApprovalRecord.setApprovalState("审批中");
        resultApprovalRecord.setApprovalMemo("");
        resultApprovalRecord.setCurrentNodeId(currentNodeId);
        Integer number = resultApprovalRecordService.selectNumber(resultApprovalRecord);

        resultApprovalRecordService.deleteApprovalRecord(resultApprovalRecord);

        if (number == null) {
            number = 0;
        }
        resultApprovalRecord.setNumber(number);
        resultApprovalRecord.setApprovalUserId(userId);
        List<ResultApprovalRecord> list = resultApprovalRecordService.selectApprovalInfo(resultApprovalRecord);
        if (list.size() < 1) {
            resultApprovalRecordService.insertApprovalRecord(resultApprovalRecord);
        } else {
            resultApprovalRecordService.updateApprovalRecord(resultApprovalRecord);
        }
    }

    /**
     * 审批通过后进行抄送提醒
     *
     * @param auditFlowCurrent
     */
    public void msgCopy(AuditFlowCurrent auditFlowCurrent) {
        MsgEvtInfo msg = new MsgEvtInfo();
        String copyMsg = auditCopyMsgMapper.selectUseIds(auditFlowCurrent.getCurrentId());
        if (!ShiroUtils.isNull(copyMsg)) {
            String[] arr = copyMsg.split(",");
            for (String userId : arr) {
                //抄送操作不抄送给自己
                if (Integer.parseInt(userId) != ShiroUtils.getUserId()) {
                    msg.setType(11);
                    msg.setEventId(auditFlowCurrent.getCurrentId());
                    msg.setAuditId(auditFlowCurrent.getAuditId());
                    msg.setUserId(Integer.parseInt(userId));
                    msg.setCreateTime(new Date());
                    msgEvtInfoService.insertMessageInfo(msg);
                }
            }
        }
    }

    public void updateRoleOper(Integer currentId) {
        Map<String, Object> map = auditFlowCurrentMapper.selectList(currentId);
        Integer currentState = (Integer) map.get("currentState");
        // 等于3表示这个是审批流是处于审批不通过的状态
        if (currentState.equals(ManagerConstant.APPROVAL_FAILURE)) {
            Integer auditId = (Integer) map.get("auditId");
            Integer maxNodeId = auditFlowNodeService.selectMaxNodeIdByFlowId(auditId);
            AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
            auditFlowCurrent.setCurrentNodeId(maxNodeId);
            auditFlowCurrentService.updateAuditFlowCurrent(auditFlowCurrent);
        }
    }

    /**
     * 更新审批信息
     *
     * @param currentId
     */
    public void updateApprovalRecord(Integer currentId) {
        AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
        Map<String, Object> map = auditFlowCurrentMapper.selectList(currentId);
        Integer auditId = (Integer) map.get("auditId");
        Integer applyId = (Integer) map.get("applyId");
        auditFlowNodeRole.setCurrentId(currentId);
        List<AuditFlowNodeRole> list = auditFlowNodeRoleMapper.getAllUserByCurretnId(auditFlowNodeRole);
        for (AuditFlowNodeRole flowNodeRole : list) {
            // 判断该条审批流是否操作过（包含审批通过、未通过的两种情况）
            auditFlowOperRecord.setCurrentId(currentId);
            auditFlowOperRecord.setCurrentNodeId(flowNodeRole.getNodeId());
            auditFlowOperRecord.setOperUserId(flowNodeRole.getUserId());
            List<AuditFlowOperRecord> list1 = auditFlowOperRecordMapper.getAllAuditFlowOperRecord(auditFlowOperRecord);
            if (ShiroUtils.isNotEmpty(list1)) {
                for (AuditFlowOperRecord flowOperRecord : list1) {
                    ResultApprovalRecord resultApprovalRecord = new ResultApprovalRecord();
                    resultApprovalRecord.setApprovalTime(flowOperRecord.getAddTime());
                    resultApprovalRecord.setCurrentNodeId(flowOperRecord.getCurrentNodeId());
                    resultApprovalRecord.setCurrentId(currentId);
                    resultApprovalRecord.setApplyId(applyId);
                    resultApprovalRecord.setAuditId(auditId);
                    resultApprovalRecord.setApprovalUserId(flowOperRecord.getOperUserId());
                    resultApprovalRecord.setApprovalUserName(userService.getName(flowOperRecord.getOperUserId()));
                    resultApprovalRecord.setApprovalMemo(flowOperRecord.getOperMemo());
                    List<ResultApprovalRecord> listApproval = resultApprovalRecordService.selectApprovalInfo(resultApprovalRecord);
                    if (ShiroUtils.isNotEmpty(listApproval)) {
                        if (flowOperRecord.getOperType() == 1) {
                            resultApprovalRecord.setApprovalState("审批通过");
                        }
                        if (flowOperRecord.getOperType() == 0) {
                            resultApprovalRecord.setApprovalState("审批未通过");
                        }
                        if (flowOperRecord.getOperType() == 2) {
                            auditFlowNodeRole.setCurrentId(currentId);
                            auditFlowNodeRole.setNodeId(flowNodeRole.getNodeId());
                            auditFlowNodeRole.setUserId(flowNodeRole.getUserId());
                            List<AuditFlowNodeRole> list2 = auditFlowNodeRoleMapper.selectByNodeIdAndCurrentId(auditFlowNodeRole);
                            int num1 = list2.size();
                            int num2 = list1.size();
                            if (num1 >= 2 && num2 < num1) {
                                resultApprovalRecord.setPush(1);
                                resultApprovalRecord.setApprovalMemo("");
                                resultApprovalRecord.setApprovalState("审批中");
                            }
                            if (num1 == num2) {
                                String pushUserIds = flowOperRecord.getPushUsers();
                                String name = "";
                                if (pushUserIds != null) {
                                    String[] arr = pushUserIds.split(",");
                                    for (int i = 0; i < arr.length; i++) {
                                        name = name + userService.getName(Integer.parseInt(arr[i])) + ",";
                                    }
                                }
                                if (ShiroUtils.isNotNull(name)) {
                                    name = name.substring(0, name.length() - 1);
                                }
                                resultApprovalRecord.setApprovalState(userService.getName(flowOperRecord.getOperUserId()) + "--转推审批至--" + name);
                            }
                        }
                        // 更新状态，通过或者不通过
                        resultApprovalRecordService.updateApprovalRecord(resultApprovalRecord);
                    }
                }
            }
        }
    }


    public void remindApproval(Integer currentId, Integer currentNodeId, Integer auditId) {
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        // 根据audit_id得到所有的是最大的node_id
        Integer maxNodeId = auditFlowNodeService.selectMaxNodeIdByFlowId(auditId);
        Integer nextNodeId = currentNodeId + 1;
        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
        auditFlowNodeRole.setCurrentId(currentId);
        auditFlowNodeRole.setNodeId(nextNodeId);
        List<AuditFlowNodeRole> list = auditFlowNodeRoleService.selectByNodeIdAndCurrentId(auditFlowNodeRole);
        // 如果节点还未审批结束的话，进行提醒下一个审批人
        if (!ShiroUtils.isEmpty(list)) {
            for (AuditFlowNodeRole flowNodeRole : list) {
                if (nextNodeId <= maxNodeId && flowNodeRole.getState() == 1) {
                    Integer userId = flowNodeRole.getUserId();
                    msgEvtInfo.setType(3);
                    msgEvtInfo.setEventId(currentId);
                    msgEvtInfo.setAuditId(auditId);
                    msgEvtInfo.setUserId(userId);
                    List<MsgEvtInfo> msgList = msgEvtInfoService.selectOneMsgList(msgEvtInfo);
                    if (msgList.size() < 1) {
                        msgEvtInfo.setCreateTime(new Date());
                        msgEvtInfoService.insertMessageInfo(msgEvtInfo);
                    } else if (msgList.size() > 0) {
                        for (int i = 0; i < msgList.size(); i++) {
                            MsgEvtInfo msg = new MsgEvtInfo();
                            msg.setId(msgList.get(i).getId());
                            msg.setReadMark(0);
                            msg.setUpdateTime(new Date());
                            msgEvtInfoService.updateInfoById(msg);
                        }
                    }
                }
            }
        }
        if (auditId.equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currentNodeId == 4) {
        } else {
            // 解决已经拒绝审批，但任然进行审批提醒的问题
            AuditFlowOperRecord vo = new AuditFlowOperRecord();
            vo.setOperType(0);
            vo.setCurrentId(currentId);
            List<Map<String, Object>> operaRecordList = auditFlowOperRecordMapper.selectOperaRecord(vo);
            for (Map<String, Object> map : operaRecordList) {
                Integer currentNodeId2 = (Integer) map.get("currentNodeId");
                auditFlowNodeRole.setCurrentId(currentId);
                auditFlowNodeRole.setNodeId(currentNodeId2);
                List<AuditFlowNodeRole> nodeRoleList = auditFlowNodeRoleMapper.selectUsers(auditFlowNodeRole);
                if (!ShiroUtils.isEmpty(nodeRoleList)) {
                    for (AuditFlowNodeRole nodeRole : nodeRoleList) {
                        msgEvtInfo.setType(3);
                        msgEvtInfo.setEventId(currentId);
                        msgEvtInfo.setUserId(nodeRole.getUserId());
                        msgEvtInfoService.deleteForMsg(msgEvtInfo);
                    }
                }
            }
        }
    }

    public void remindManager(Integer pId, Integer currentId) {
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        SysProjectTableVo sysProjectTableVo = new SysProjectTableVo();
        sysProjectTableVo.setpId(pId);
        Integer producetypeId = sysProjectTableMapper.selectTypeId(pId);
        String manager = produceTypeService.getprojectmanagerbyid(producetypeId);
        if (ShiroUtils.isNotNull(manager)) {
            String[] arr = manager.split(",");
            // 仅将消息发送给主产品经理，即第一个
            Integer userId = Integer.parseInt(arr[0]);
            // type=3 表示审批提醒
            msgEvtInfo.setType(3);
            msgEvtInfo.setEventId(currentId);
            msgEvtInfo.setAuditId(3);
            msgEvtInfo.setUserId(userId);
            Integer total = msgEvtInfoMapper.selectCount(msgEvtInfo);
            if (total < 1) {
                msgEvtInfo.setCreateTime(new java.util.Date());
                msgEvtInfoService.insertMessageInfo(msgEvtInfo);
            } else if (total > 0) {
                List<MsgEvtInfo> resultList = msgEvtInfoService.selectOneMsgList(msgEvtInfo);
                for (MsgEvtInfo msg : resultList) {
                    MsgEvtInfo msg2 = new MsgEvtInfo();
                    msg2.setId(msg.getId());
                    msg2.setReadMark(0);
                    msg2.setUpdateTime(new Date());
                    msgEvtInfoService.updateInfoById(msg2);
                }
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

    public void updateContent(Long tId, Long chargePeopleId, String participants) {
        ProjectTaskTable projectTaskTable = new ProjectTaskTable();
        TaskTable taskTable = new TaskTable();
        TaskUser taskUser = new TaskUser();
        if (chargePeopleId != null || participants != null) {
            projectTaskTable.setEventId(tId.intValue());
            projectTaskTable.setTypeId(0);
            projectTaskTableService.deleteInfos(projectTaskTable);
            taskTable.settId(tId);
            List<TaskTable> list = taskTableService.selectTaskByTid2(taskTable);
            if (!list.isEmpty() && list != null) {
                for (TaskTable t : list) {
                    projectTaskTable.setUserId(t.getChargepeopleId().intValue());
                    List<ProjectTaskTable> l = projectTaskTableService.selectInfos(projectTaskTable);
                    if (l.size() < 1) {
                        projectTaskTable.setCreateTime(new Date());
                        projectTaskTableService.insertProjectTask(projectTaskTable);
                    }
                    if (l.size() > 0) {
                        projectTaskTable.setUpdateTime(new Date());
                        projectTaskTableService.updateProjectTask(projectTaskTable);
                    }
                }
            }
            taskUser.settId(tId);
            List<TaskUser> list1 = taskUserService.selectTaskUserList(taskUser);
            if (!list1.isEmpty() && list1 != null) {
                for (TaskUser t : list1) {
                    projectTaskTable.setUserId(t.getUserId().intValue());
                    List<ProjectTaskTable> l = projectTaskTableService.selectInfos(projectTaskTable);
                    if (l.size() < 1) {
                        projectTaskTable.setCreateTime(new Date());
                        projectTaskTableService.insertProjectTask(projectTaskTable);
                    }
                    if (l.size() > 0) {
                        projectTaskTable.setUpdateTime(new Date());
                        projectTaskTableService.updateProjectTask(projectTaskTable);
                    }
                }
            }
        }
    }

}
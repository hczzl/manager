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
     * ????????????
     */
    @Override
    public AjaxResult audit(String currentId, String memo, Integer type, String userId, String score) {
        SysUser sysUser = new SysUser();
        // ???????????????????????????id
        sysUser.setUserId(ShiroUtils.getUserId());
        // ??????????????????????????????id????????????????????????
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
            return AjaxResult.error("?????????????????????");
        }
        // ????????????id
        Integer auditId = auditFlowCurrent.getAuditId();
        Integer nodeRoleNumBer = 0;
        Integer nodeSum = 0;
        Integer p = 0;
        int info = 0;
        if (Arrays.asList(ManagerConstant.PROJECT_AUDIT_TYPE).contains(auditId)) {
            // ?????????????????????
            AjaxResult ajaxResult = marketApproval(currentId, memo, type, userId, auditFlowCurrent, auditId);
            return ajaxResult;
        } else {
            // ?????????????????????
            Integer currenNodeId = auditFlowCurrent.getCurrentNodeId();
            // ????????????????????????????????????????????????
            auditFlowNodeRole2.setNodeId(currenNodeId);
            auditFlowNodeRole2.setCurrentId(Integer.parseInt(currentId));
            nodeRoleNumBer = auditFlowNodeRoleService.selectAuditFlowNodeRoleListSum(auditFlowNodeRole2);
            AuditFlowOperRecord auditFlowOperRecord1 = new AuditFlowOperRecord();
            auditFlowOperRecord1.setCurrentId(StringUtils.toInteger(currentId));
            auditFlowOperRecord1.setCurrentNodeId(auditFlowCurrent.getCurrentNodeId());
            auditFlowOperRecord1.setOperUserId(ShiroUtils.getUserId().intValue());
            auditFlowOperRecord1.setOperType(1);//??????????????????
            Integer passCount = auditFlowOperRecordMapper.selectAuditFlowOperRecordListCount(auditFlowOperRecord1);
            auditFlowOperRecord1.setOperType(2);//?????????????????????????????????????????????????????????
            Integer turnCount = auditFlowOperRecordMapper.selectAuditFlowOperRecordListCount(auditFlowOperRecord1);
            if (auditId == 1) {
                if (passCount.equals(nodeRoleNumBer)) {
                    return AjaxResult.success("??????????????????????????????????????????");
                }
            }
            if (auditId != 1) {
                if (passCount > 0) {
                    return AjaxResult.success("??????????????????????????????????????????");
                }
            }
            if (turnCount > ManagerConstant.TASK_MAX_PUSH) {
                return AjaxResult.success("??????????????????");
            } else {
                List<AuditFlowNodeRole> auditFlowNodeRoleList3 = new ArrayList<>();
                auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
                auditFlowOperRecord1.setCurrentNodeId(currenNodeId);//
                // ????????????????????????????????????????????????????????????
                nodeSum = auditFlowOperRecordService.selectAuditFlowOperRecordSum(auditFlowOperRecord1);
                auditFlowNodeRole2.setNodeId(currenNodeId);
                auditFlowNodeRole2.setCurrentId(Integer.parseInt(currentId));
                List<Integer> userIdList = auditFlowNodeRoleMapper.selectApplyUserId(auditFlowNodeRole2);
                // ????????????????????????????????????
                if (nodeSum >= nodeRoleNumBer && nodeRoleNumBer > 0 && nodeSum > 0) {
                    return AjaxResult.error("?????????????????????");
                } else {
                    boolean uesrYN = false;
                    Integer currentApplyId = sysUser.getUserId().intValue();
                    // ?????????????????????????????????
                    if (ShiroUtils.isNotEmpty(userIdList) && userIdList.contains(currentApplyId)) {
                        uesrYN = true;
                    }
                    // ???????????????
                    if (uesrYN == true) {
                        // ??????????????????
                        AuditFlowNode auditFlowNode = auditFlowNodeService.selectAuditFlowNodeById(auditFlowCurrent.getCurrentNodeId());
                        // ?????????????????????
                        AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
                        // ????????????????????????????????????
                        auditFlowOperRecord.setCurrentId(auditFlowCurrent.getCurrentId());
                        auditFlowOperRecord.setCurrentNodeId(auditFlowCurrent.getCurrentNodeId());
                        auditFlowOperRecord.setOperUserId(Math.toIntExact(sysUser.getUserId()));
                        auditFlowOperRecord.setOperType(type);
                        auditFlowOperRecord.setOperMemo(memo);
                        auditFlowOperRecord.setAddTime(new Date());
                        auditFlowOperRecord.setState(1);
                        // node_type---1??????????????????2??????????????????3???????????????          //NODE_TYPR_THREE--?????????????????????
                        if (auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE)) {
                            auditFlowCurrent.setCurrentNodeId(auditFlowNode.getNodeId());//?????????????????????
                            if (type == 1) {// type 0?????????,1??????,2??????
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
                                    // ????????????
                                    if (auditFlowCurrent.getAuditId() == 1) {
                                        taskTable.setTaskFinishflag("0");
                                        taskTable.settId((long) auditFlowCurrent.getApplyId());
                                    }
                                    remindApproval(StringUtils.toInteger(currentId), currenNodeId, auditFlowCurrent.getAuditId());
                                    // ?????????????????????????????????
                                    msgCopy(auditFlowCurrent);
                                    info = 1;
                                }
                                info = 2;
                                // ????????????
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_BREAK)) {
                                    taskTable.setTaskFinishflag("2");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                // ????????????
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_FINISH)) {
                                    taskTable.setTaskFinishflag("1");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                // ????????????????????????????????????????????????????????????
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_CHANGE)) {
                                    taskPostpone.setState(1);
                                    taskTable.setTaskFinishflag("0");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                            }
                            if (type.equals(ManagerConstant.APPROVAL_TYPE_PUSH)) {// ??????
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_SIX);// ?????????
                                String[] userIds = userId.split(",");// ??????????????????????????????????????????
                                auditFlowOperRecord.setPushUsers(userId);
                                for (String s : userIds) {
                                    AuditFlowNodeRole auditFlowNodeRole1 = new AuditFlowNodeRole();
                                    // ????????????????????????state??????0?????????????????????????????????????????????????????????????????????????????????
                                    auditFlowNodeRole1.setNodeId(auditFlowCurrent.getCurrentNodeId());
                                    auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                                    auditFlowNodeRole1.setState(0);
                                    auditFlowNodeRole1.setUserId(Math.toIntExact(sysUser.getUserId()));

                                    auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole1);
                                    // ????????????????????????
                                    auditFlowNodeRole1.setUserId(Integer.parseInt(s));
                                    auditFlowNodeRole1.setAddTime(new Date());
                                    auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                                    // ?????????????????????audit_flow_node_role???state?????????1???
                                    auditFlowNodeRole1.setState(1);
                                    auditFlowNodeRoleService.insertAuditFlowNodeRole(auditFlowNodeRole1);
                                    insertApprovlInfo(auditFlowCurrent.getCurrentId(), auditFlowCurrent.getApplyId(), auditFlowCurrent.getAuditId(), Integer.parseInt(s), Math.toIntExact(sysUser.getUserId()), auditFlowCurrent.getCurrentNodeId());
                                    // ????????????
                                    insertMsgInfo(auditFlowCurrent.getCurrentId(), auditFlowCurrent.getAuditId(), Integer.parseInt(s));
                                }
                                // ???????????????????????????????????????????????????????????????
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
                                // ?????????????????????????????????????????????
                                Integer currentState = auditFlowCurrent.getCurrentState();
                                if (currentState.equals(ManagerConstant.APPROVAL_FAILURE) && Arrays.asList(ManagerConstant.TASK_AUDIT_TYPE).contains(auditId)) {
                                    ResultApprovalRecord resultApprovalRecord = new ResultApprovalRecord();
                                    resultApprovalRecord.setCurrentId(auditFlowCurrent.getCurrentId());
                                    // ????????????????????????
                                    resultApprovalRecord.setState(1);
                                    List<Integer> listUsers = resultApprovalRecordMapper.selectUserIdList(resultApprovalRecord);
                                    // ???????????????????????????
                                    int userId2 = ShiroUtils.getUserId().intValue();
                                    // ??????????????????
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
                                // ??????????????????????????????????????????
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_TASK) && currentState.equals(ManagerConstant.APPROVAL_FAILURE)) {
                                    projectTaskTable.setEventId(auditFlowCurrent.getApplyId());
                                    projectTaskTable.setTypeId(0);
                                    projectTaskTableService.deleteInfos(projectTaskTable);
                                }
                                // ??????????????????
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_BREAK)) {
                                    taskTable.setTaskFinishflag("0");
                                    String stopMemo = auditFlowCurrent.getStopMone();
                                    taskTable.setStopMone(stopMemo);//???????????????????????????
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                // ??????????????????
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_FINISH)) {
                                    taskTable.setTaskFinishflag("0");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                // ?????????????????????????????????????????????????????????????????????
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_CHANGE)) {
                                    taskTable.setTaskFinishflag("0");
                                    taskTable.settId((long) auditFlowCurrent.getApplyId());
                                }
                                info = 1;
                                // ?????????????????????????????????????????????
                                auditCopyMsgMapper.deleteCopyMsg(auditFlowCurrent.getCurrentId());
                            }
                        }
                        if (info != 0) {
                            // ????????????????????????????????????????????????????????????????????????
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
                        // ???????????????????????????
                        updateRoleOper(auditFlowCurrent.getCurrentId());
                        // ???????????????????????????????????????
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
                                taskTable.setStopMone(stopMemo);// ?????????????????????
                            }
                            taskTable.settId(auditFlowCurrent.getApplyId().longValue());
                            taskTable.setUpdateTime(new Date());
                            taskTable.setTaskOverdueState("1");
                            taskTableService.updateTaskTable(taskTable);
                        }
                        // ?????????????????????
                        Integer[] auditIds = {8, 9, 12};
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && Arrays.asList(auditIds).contains(auditFlowCurrent.getAuditId())) {
                            taskTable.setUpdateTime(new Date());
                            taskTableService.updateTaskTable(taskTable);
                        }
                        // ????????????????????????
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS) && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_FINISH) && nodeSum >= nodeRoleNumBer) {
                            taskTable.setTaskScore(Double.parseDouble(score));
                            String taskRater = ShiroUtils.getSysUser().getUserName();
                            taskTable.setTaskRater(taskRater);
                            taskTableService.updateTaskTable(taskTable);
                            ShiroUtils.setTaskScore(0);
                            taskTable.settId((long) auditFlowCurrent.getApplyId());
                            // ????????????id???????????????id
                            int schedule = taskTableService.selectTaskSchedule(taskTable);
                            if (schedule != 100) {
                                taskTable.setScheduleRate(100);
                                taskTable.setUpdateTime(new Date());
                                taskTableService.updateTaskTable(taskTable);
                            }
                        }
                        // ????????????
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS) && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_TASK_CHANGE)) && nodeSum >= nodeRoleNumBer) {
                            TaskPostpone taskPostpone1 = new TaskPostpone();
                            taskPostpone1.setState(1);
                            taskPostpone1.settId(auditFlowCurrent.getApplyId().longValue());
                            taskPostpone1.setCurrentId(auditFlowCurrent.getCurrentId());
                            taskPostponeService.updateTaskPostpone(taskPostpone1);
                            // ?????????id?????????????????????,??????????????????state?????????0
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
                                    // ???????????????????????????
                                    taskTable.setPeriod(taskPostpone2.getPeriod());
                                    taskTable.setUpdateBy(ShiroUtils.getUserId() + "");
                                    taskTable.setUpdateTime(new Date());// ????????????
                                }
                            }
                            Integer chargePeopleId = ShiroUtils.getChargeId();
                            if (chargePeopleId == null) {
                                chargePeopleId = 0;
                            }
                            String participants = ShiroUtils.getParticipant();
                            if (ShiroUtils.isNotNull(participants)) {
                                // ??????????????????
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
                            // ????????????
                            if (isPostpone != null && isPostpone == 1) {
                                taskPostponeService.taskPostpone(taskTable);
                            }
                            // ?????????????????????
                            taskTable.setTaskOverdueState("1");
                            taskTableService.updateTaskTable(taskTable);
                            // ??????projectTaskTable?????????
                            updateContent((long) auditFlowCurrent.getApplyId(), chargePeopleId.longValue(), participants);
                        }
                        // ??????????????????????????????
                        if (auditFlowCurrent.getCurrentId() != null) {
                            updateApprovalRecord(auditFlowCurrent.getCurrentId());
                        }
                        if (a > 0 && b > 0) {
                            return AjaxResult.success("???????????????");
                        } else {
                            return AjaxResult.error("???????????????");
                        }
                    } else {
                        return AjaxResult.error("?????????????????????");
                    }
                }
            }
        }
        /*if (code == 1) {
            return AjaxResult.success("???????????????");
        } else if (code == 2) {
            return AjaxResult.error("???????????????");
        } else if (code == 3 | code == 4) {
            return AjaxResult.error("?????????????????????");
        } else if (code == 6) {
            return AjaxResult.success("??????????????????????????????????????????");
        } else if (code == 5) {
            return AjaxResult.success("??????????????????");
        } else {
            return AjaxResult.error("???????????????");
        }*/
    }

    /**
     * ???????????????????????????
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
        // ????????????????????????
        Integer currenNodeId = auditFlowCurrent.getCurrentNodeId();
        Integer nodeSeqno1 = 0;
        Integer nodeId = 0;
        Integer nodeRoleNumBer = 0;
        int a = 0;
        int b = 0;
        int info = 0;
        // ?????????????????????
        Integer nodeIdPass = 0;
        // ????????????????????????
        Integer nodeIdNotPass = 0;
        boolean go = false;
        int p = 0;
        Integer nodeSum = 0;
        // ??????flowId??????????????????????????????
        AuditFlowNode auditFlowNode1 = new AuditFlowNode();
        auditFlowNode1.setFlowId(auditId);
        List<AuditFlowNode> list2 = auditFlowNodeService.selectFlowId(auditFlowNode1);
        Integer nodeSeqno = list2.get(0).getNodeSeqno();
        for (int i = 0; i < list2.size(); i++) {
            nodeSeqno1 = list2.get(i).getNodeSeqno();
            auditFlowNode1.setFlowId(auditId);
            auditFlowNode1.setNodeSeqno(nodeSeqno1);
            // ????????????????????????
            nodeId = auditFlowNodeService.selectNodeIdBySepno(auditFlowNode1);
            if (nodeId.equals(currenNodeId)) {
                auditFlowNodeRole2.setNodeId(nodeId);
                // ??????????????????????????????????????????
                auditFlowNodeRole2.setCurrentId(StringUtils.toInteger(currentId));// ????????????id
                nodeRoleNumBer = auditFlowNodeRoleService.selectAuditFlowNodeRoleListSum(auditFlowNodeRole2);
                break;
            }
        }
        AuditFlowOperRecord auditFlowOperRecord1 = new AuditFlowOperRecord();
        auditFlowOperRecord1.setCurrentId(StringUtils.toInteger(currentId));
        auditFlowOperRecord1.setCurrentNodeId(auditFlowCurrent.getCurrentNodeId());
        auditFlowOperRecord1.setOperType(1);// ??????????????????
        Integer passCount = auditFlowOperRecordMapper.selectAuditFlowOperRecordListCount(auditFlowOperRecord1);
        auditFlowOperRecord1.setOperType(2);// ?????????????????????????????????????????????????????????
        Integer turnCount = auditFlowOperRecordMapper.selectAuditFlowOperRecordListCount(auditFlowOperRecord1);
        if (passCount.equals(nodeRoleNumBer)) {
            // code = 6;
            return AjaxResult.success("??????????????????????????????????????????");
        }
        if (turnCount > ManagerConstant.TASK_MAX_PUSH) {
            // code = 5;
            return AjaxResult.success("??????????????????");
        } else {
            // ???????????????????????????????????????
            auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
            auditFlowOperRecord1.setCurrentNodeId(currenNodeId);
            nodeSum = auditFlowOperRecordService.selectAuditFlowOperRecordSum(auditFlowOperRecord1);
            // ???????????????????????????????????????
            auditFlowNodeRole2.setNodeId(currenNodeId);
            auditFlowNodeRole2.setCurrentId(Integer.parseInt(currentId));
            List<Integer> userIdList = auditFlowNodeRoleMapper.selectApplyUserId(auditFlowNodeRole2);
            // ????????????????????????????????????
            if (nodeSum >= nodeRoleNumBer && nodeRoleNumBer > 0 && nodeSum > 0) {
                // code = 4;
                return AjaxResult.error("?????????????????????");
            } else {
                // ?????????????????????????????????
                boolean uesrYN = false;
                Integer currentApplyId = sysUser.getUserId().intValue();
                if (ShiroUtils.isNotEmpty(userIdList) && userIdList.contains(currentApplyId)) {
                    uesrYN = true;
                }
                // ???????????????
                if (uesrYN == true) {
                    //??????????????????
                    AuditFlowNode auditFlowNode = auditFlowNodeService.selectAuditFlowNodeById(auditFlowCurrent.getCurrentNodeId());
                    // ?????????????????????
                    AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
                    auditFlowOperRecord.setCurrentId(auditFlowCurrent.getCurrentId());
                    auditFlowOperRecord.setCurrentNodeId(auditFlowCurrent.getCurrentNodeId());
                    auditFlowOperRecord.setOperUserId(Math.toIntExact(sysUser.getUserId()));
                    auditFlowOperRecord.setOperType(type);
                    auditFlowOperRecord.setOperMemo(memo);
                    auditFlowOperRecord.setAddTime(new Date());
                    auditFlowOperRecord.setState(1);
                    // node_type---1??????????????????2??????????????????3???????????????
                    // ?????????????????????????????????????????????
                    if (auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE)) {
                        // ?????????????????????
                        auditFlowCurrent.setCurrentNodeId(auditFlowNode.getNodeId());
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS)) {// type=1 ???????????? type=0 ??????????????? type =2 ??????
                            AuditFlowNodeRole auditFlowNodeRole1 = new AuditFlowNodeRole();
                            auditFlowNodeRole1.setCurrentId(auditFlowCurrent.getCurrentId());
                            auditFlowNodeRole1.setNodeId(auditFlowCurrent.getCurrentNodeId());
                            auditFlowNodeRole1.setState(0);
                            auditFlowNodeRole1.setUserId(Math.toIntExact(sysUser.getUserId()));
                            auditFlowNodeRoleService.updateAuditFlowNodeRoleByNodeId(auditFlowNodeRole1);
                            // ???????????????????????????
                            p = auditFlowOperRecordService.insertAuditFlowOperRecord(auditFlowOperRecord);
                            auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
                            auditFlowOperRecord1.setCurrentNodeId(currenNodeId);
                            // ?????????????????????????????????
                            nodeSum = auditFlowOperRecordService.selectAuditFlowOperRecordSum(auditFlowOperRecord1);
                            // ????????????????????????????????????????????????????????????????????????
                            // ????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)
                                    // ??????????????????
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)
                                    // ??????????????????
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                auditFlowOperRecord1.setOperType(1);
                                nodeIdPass = auditFlowOperRecordMapper.selectAuditOperRecordCount(auditFlowOperRecord1);
                                // ?????????????????????????????????????????????????????????
                                if (nodeIdPass >= 2) {
                                    auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FIVE);
                                    // ????????????
                                    if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)) {
                                        sysProjectTable.setEstablishStatus("1");
                                    }
                                    // ??????????????????
                                    if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)) {
                                        sysProjectTable.setProjectFinishFlag("2");
                                    }
                                    // ??????????????????
                                    if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                        sysProjectTable.setProjectFinishFlag("2");
                                    }
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                    // ??????????????????????????????????????????
                                    updateFinish(auditFlowCurrent);
                                    // ??????????????????
                                    msgCopy(auditFlowCurrent);
                                    info = 1;
                                }
                            }
                            // ???????????????????????????????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && nodeRoleNumBer >= 3) {
                                auditFlowOperRecord1.setOperType(1);
                                nodeIdPass = auditFlowOperRecordMapper.selectAuditOperRecordCount(auditFlowOperRecord1);
                                // ????????????????????????2??????????????????????????????
                                if (nodeIdPass >= 2) {
                                    auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FIVE);
                                    sysProjectTable.setEstablishStatus("1");
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                    updateFinish(auditFlowCurrent);
                                    msgCopy(auditFlowCurrent);
                                    info = 1;
                                }
                            } else if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && nodeRoleNumBer < 3) {
                                // ??????????????????????????????2??????????????????????????????
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FIVE);
                                sysProjectTable.setEstablishStatus("1");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                info = 1;
                            }
                            // ??????????????????????????????????????????
                            if (nodeSum >= nodeRoleNumBer & nodeSum > 0 & nodeRoleNumBer > 0) {
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FIVE);
                                // ??????????????????
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH)) {
                                    sysProjectTable.setProjectFinishFlag("1");
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                }
                                // ??????????????????
                                if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH)) {
                                    sysProjectTable.setProjectFinishFlag("1");
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                }
                                // ????????????????????????????????????????????????????????????????????????????????????
                                remindApproval(StringUtils.toInteger(currentId), currenNodeId, auditFlowCurrent.getAuditId());
                                // ????????????
                                msgCopy(auditFlowCurrent);
                                info = 1;
                            }
                            info = 2;
                        }
                        if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE)) {
                            // ???????????????????????????????????????????????????????????????
                            // ????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)
                                    // ????????????
                                    || (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && nodeRoleNumBer >= 3)
                                    // ??????????????????
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)
                                    // ??????????????????
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                p = auditFlowOperRecordService.insertAuditFlowOperRecord(auditFlowOperRecord);
                                auditFlowOperRecord1.setCurrentId(Integer.parseInt(currentId));
                                auditFlowOperRecord1.setCurrentNodeId(currenNodeId);
                                auditFlowOperRecord1.setOperType(0);
                                nodeIdNotPass = auditFlowOperRecordMapper.selectAuditOperRecordCount(auditFlowOperRecord1);
                                if (nodeIdNotPass >= 2) {
                                    // ????????????
                                    if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)
                                            || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                        sysProjectTable.setProjectFinishFlag("0");
                                    } else if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)
                                            || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT)) {
                                        //????????????
                                        sysProjectTable.setEstablishStatus("2");
                                    }
                                    sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                    auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_THREE);
                                    // ??????????????????????????????????????????
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
                            // ??????????????????
                            insertMsgNotApproval(auditFlowNodeRole, auditFlowCurrent.getCurrentId(), auditFlowCurrent, auditId, msgEvtInfo);
                            // ????????????
                            if ((auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT)
                                    // ????????????
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT)) && currentState.equals(ManagerConstant.APPROVAL_FAILURE)) {
                                projectTaskTable.setEventId(auditFlowCurrent.getApplyId());
                                projectTaskTable.setTypeId(1);
                                projectTaskTableService.deleteInfos(projectTaskTable);
                                info = 1;
                            }
                            // ?????????????????????????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && nodeRoleNumBer < 3) {
                                sysProjectTable.setEstablishStatus("2");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                info = 1;
                            }
                            /******????????????*****/
                            // ?????????????????????????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                info = 1;
                            }
                            /*******????????????***/
                            // ??????????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                                info = 1;
                            }
                            // ??????????????????
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
                            // ????????????????????????
                            if (nodeSum >= nodeRoleNumBer & nodeSum > 0 & nodeRoleNumBer > 0) {
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FOUR);
                                auditFlowCurrent.setCurrentNodeId(auditFlowNode.getNextNode());
                                // ?????????????????????
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
                            // ??????????????????????????????????????????
                            if (auditId.equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currenNodeId == 3) {
                                // ?????????????????????????????????????????????
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_FOUR);
                                auditFlowCurrent.setCurrentNodeId(auditFlowNode.getNextNode());
                                go = true;
                            } else {
                                auditFlowCurrent.setCurrentState(ConstantUtil.CURRENT_STATE_THREE);
                                updateApprovalState(auditFlowCurrent);
                            }
                            // ?????????????????????????????????
                            insertMsgNotApproval(auditFlowNodeRole, auditFlowCurrent.getCurrentId(), auditFlowCurrent, auditId, msgEvtInfo);
                            // ??????????????????
                            if (((auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currenNodeId != 3)
                                    // ??????????????????
                                    || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT))) {
                                projectTaskTable.setEventId(auditFlowCurrent.getApplyId());
                                projectTaskTable.setTypeId(1);
                                projectTaskTableService.deleteInfos(projectTaskTable);
                            }
                            // ???????????????????????????
                            if ((auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currenNodeId != 3) || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT)) {
                                sysProjectTable.setEstablishStatus("2");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // ?????????????????????????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // ?????????????????????????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // ?????????????????????????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // ?????????????????????????????????
                            if (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH)) {
                                sysProjectTable.setProjectFinishFlag("0");
                                sysProjectTable.setpId(auditFlowCurrent.getApplyId());
                            }
                            // ?????????????????????????????????????????????
                            auditCopyMsgMapper.deleteCopyMsg(auditFlowCurrent.getCurrentId());
                            info = 1;
                        }
                    }
                    if (info != 0) {
                        // ????????????????????????????????????????????????????????????????????????
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
                    // ??????????????????
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
                    // ???????????????????????????
                    updateRoleOper(auditFlowCurrent.getCurrentId());
                    // ??????????????????????????????
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
                    // ????????????,??????????????????
                    if (type == 1 && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK)) && nodeIdPass >= 2) {
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                        // ????????????????????????????????????
                        updateMag(auditFlowNodeRole, currenNodeId, auditFlowCurrent, msgEvtInfo);
                    }
                    // ????????????
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS) && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) && currenNodeId == 7 && nodeIdPass >= 2) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                        updateMag(auditFlowNodeRole, currenNodeId, auditFlowCurrent, msgEvtInfo);
                    } else if (type.equals(ManagerConstant.APPROVAL_TYPE_PASS) && auditFlowNode.getNodeType().equals(ConstantUtil.NODE_TYPR_THREE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH)) && nodeSum >= nodeRoleNumBer) {
                        // ??????????????????????????????????????????????????????????????????
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                    }
                    // ?????????????????????
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT) || (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && currenNodeId != 3) && auditFlowCurrent.getCurrentNodeId() != 5)) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                    }
                    // ?????????????????????
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT) && nodeIdNotPass >= 2 && auditFlowCurrent.getCurrentNodeId() == 5) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                    }
                    // ??????????????????????????????
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK) && nodeIdNotPass >= 2 && auditFlowCurrent.getCurrentNodeId() == 21) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                        updateMag(auditFlowNodeRole, currenNodeId, auditFlowCurrent, msgEvtInfo);
                    } else if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_BREAK) && auditFlowCurrent.getCurrentNodeId() != 21) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateEstablishStatu(sysProjectTable);
                    }
                    // ?????????????????????????????????????????????????????????????????????????????????????????????????????????
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH) || auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_RESEARCH_PROJECT_FINISH))) {
                        sysProjectTableService.updateProject(sysProjectTable);//????????????????????????0
                    }
                    // ??????????????????????????????
                    if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && (auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK) && nodeIdNotPass >= 2 && currenNodeId == 17)) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateProject(sysProjectTable);
                        // ????????????????????????????????????
                        updateMag(auditFlowNodeRole, currenNodeId, auditFlowCurrent, msgEvtInfo);
                    } else if (type.equals(ManagerConstant.APPROVAL_TYPE_REFUSE) && auditFlowCurrent.getAuditId().equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK) && currenNodeId != 17) {
                        sysProjectTable.setUpdateTime(new Date());
                        sysProjectTableService.updateProject(sysProjectTable);
                    }
                    // ??????????????????????????????
                    if (auditFlowCurrent.getCurrentId() != null) {
                        updateApprovalRecord(auditFlowCurrent.getCurrentId());
                    }
                    if (a > 0 && b > 0) {
                        // code = 1;
                        return AjaxResult.success("???????????????");
                    } else {
                        // code = 2;
                        return AjaxResult.error("???????????????");
                    }
                } else {
                    // code = 3;
                    return AjaxResult.error("?????????????????????");
                }
            }
        }
    }

    /**
     * ????????????????????????
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
        if (createBy != uId) { // ????????????????????????????????????
            msgEvtInfo.setUserId((int) createBy);
            msgEvtInfoService.insertMessageInfo(msgEvtInfo);
        }
    }

    /**
     * ????????????????????????????????????
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
     * ???????????????????????????????????????????????????????????????????????????
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
                    record.setApprovalState("????????????");
                    record.setApprovalUserId(records.getApprovalUserId());
                    resultApprovalRecordService.updateApprovalRecord(record);
                }
            }
        }
    }

    /**
     * ??????????????????????????????????????????????????????????????????????????????
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
                        record.setApprovalState("????????????");
                    } else if (auditId == 10 || auditId == 13) {
                        record.setApprovalState("????????????");
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
        resultApprovalRecord.setApprovalState("?????????");
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
     * ?????????????????????????????????
     *
     * @param auditFlowCurrent
     */
    public void msgCopy(AuditFlowCurrent auditFlowCurrent) {
        MsgEvtInfo msg = new MsgEvtInfo();
        String copyMsg = auditCopyMsgMapper.selectUseIds(auditFlowCurrent.getCurrentId());
        if (!ShiroUtils.isNull(copyMsg)) {
            String[] arr = copyMsg.split(",");
            for (String userId : arr) {
                //??????????????????????????????
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
        // ??????3?????????????????????????????????????????????????????????
        if (currentState.equals(ManagerConstant.APPROVAL_FAILURE)) {
            Integer auditId = (Integer) map.get("auditId");
            Integer maxNodeId = auditFlowNodeService.selectMaxNodeIdByFlowId(auditId);
            AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
            auditFlowCurrent.setCurrentNodeId(maxNodeId);
            auditFlowCurrentService.updateAuditFlowCurrent(auditFlowCurrent);
        }
    }

    /**
     * ??????????????????
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
            // ???????????????????????????????????????????????????????????????????????????????????????
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
                            resultApprovalRecord.setApprovalState("????????????");
                        }
                        if (flowOperRecord.getOperType() == 0) {
                            resultApprovalRecord.setApprovalState("???????????????");
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
                                resultApprovalRecord.setApprovalState("?????????");
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
                                resultApprovalRecord.setApprovalState(userService.getName(flowOperRecord.getOperUserId()) + "--???????????????--" + name);
                            }
                        }
                        // ????????????????????????????????????
                        resultApprovalRecordService.updateApprovalRecord(resultApprovalRecord);
                    }
                }
            }
        }
    }


    public void remindApproval(Integer currentId, Integer currentNodeId, Integer auditId) {
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        // ??????audit_id???????????????????????????node_id
        Integer maxNodeId = auditFlowNodeService.selectMaxNodeIdByFlowId(auditId);
        Integer nextNodeId = currentNodeId + 1;
        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
        auditFlowNodeRole.setCurrentId(currentId);
        auditFlowNodeRole.setNodeId(nextNodeId);
        List<AuditFlowNodeRole> list = auditFlowNodeRoleService.selectByNodeIdAndCurrentId(auditFlowNodeRole);
        // ?????????????????????????????????????????????????????????????????????
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
            // ???????????????????????????????????????????????????????????????
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
            // ???????????????????????????????????????????????????
            Integer userId = Integer.parseInt(arr[0]);
            // type=3 ??????????????????
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

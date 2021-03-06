package com.ruoyi.quartz.task;


import com.ruoyi.web.domain.*;
import com.ruoyi.web.mapper.audit.AuditFlowNodeRoleMapper;
import com.ruoyi.web.mapper.audit.AuditFlowOperRecordMapper;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.audit.AuditFlowNodeRoleService;
import com.ruoyi.web.service.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 控制器（注入服务）
 */
@Component("projectWarnTask")
public class ProjectWarnTask {

    @Autowired
    private AuditFlowCurrentService auditFlowCurrentService;
    @Autowired
    private AuditFlowNodeRoleMapper auditFlowNodeRoleMapper;
    @Autowired
    private AuditFlowOperRecordMapper auditFlowOperRecordMapper;
    @Autowired
    private AuditFlowNodeRoleService auditFlowNodeRoleService;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;


    public void execute() {
        System.out.println("审批提醒开始");
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
        AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        remindApproval(auditFlowCurrent, auditFlowNodeRole, msgEvtInfo);
        test(auditFlowNodeRole, auditFlowOperRecord, msgEvtInfo);
        System.out.println("审批提醒结束");
    }

    public void remindApproval(AuditFlowCurrent auditFlowCurrent, AuditFlowNodeRole auditFlowNodeRole, MsgEvtInfo msgEvtInfo) {
        int q = 0;
        int y = 0;
        //得到所有的审批流id
        List<AuditFlowNodeRole> list = auditFlowNodeRoleMapper.getAllCurrentId(auditFlowNodeRole);
        for (int f = 0; f < list.size(); f++) {
            int b = list.get(f).getCurrentId();
            //拿到当前的节点和状态
            auditFlowCurrent.setCurrentId(b);
            List<AuditFlowCurrent> listState = auditFlowCurrentService.getFlowCurrentStatre(auditFlowCurrent);
            for (int t = 0; t < listState.size(); t++) {
                Integer currentState = listState.get(t).getCurrentState();
                if (currentState != null && currentState != 5 && currentState != 3) {

                    Integer currentNodeId = listState.get(t).getCurrentNodeId();
                    int auditId = listState.get(t).getAuditId();
                    if (currentNodeId == null) {
                        currentNodeId = 0;
                    }
                    auditFlowNodeRole.setCurrentId(b);
                    List<AuditFlowNodeRole> list1 = auditFlowNodeRoleMapper.getAllAuditFlowNodeRole(auditFlowNodeRole);
                    for (int i = 0; i < list1.size(); i++) {
                        if (list1.get(i).getState() != 0) {
                            if (auditFlowNodeRoleService.getSumApproval(b) >= 2) {
                                if (currentNodeId != 0) {
                                    // 计算node_id的总数
                                    int p = auditFlowNodeRoleService.getSumNodeId(auditFlowNodeRole);
                                    if (currentState != 6) {
                                        auditFlowNodeRole.setNodeId(currentNodeId);
                                        auditFlowNodeRole.setState(1);
                                        auditFlowNodeRole.setCurrentId(b);
                                        List<AuditFlowNodeRole> user = auditFlowNodeRoleService.getUserIdByNodeId(auditFlowNodeRole);
                                        for (int j = 0; j < user.size(); j++) {
                                            int userId = user.get(j).getUserId();
                                            //当第一次找到这个用户的时候需要马上提醒一次
                                            msgEvtInfo.setType(3);
                                            msgEvtInfo.setEventId(b);
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
                                        y++;
                                    }
                                    if (currentState == 6) {
                                        auditFlowNodeRole.setNodeId(currentNodeId);
                                        auditFlowNodeRole.setState(1);
                                        auditFlowNodeRole.setCurrentId(b);
                                        List<AuditFlowNodeRole> user = auditFlowNodeRoleService.getUserIdByNodeId(auditFlowNodeRole);
                                        for (int j = 0; j < user.size(); j++) {
                                            int userId = user.get(j).getUserId();
                                            msgEvtInfo.setType(3);
                                            msgEvtInfo.setEventId(b);
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
                                                    int c = msgEvtInfoService.updateInfoById(m);
                                                }
                                            }
                                        }
                                        q++;
                                    }
                                }
                            }
                            if (auditFlowNodeRoleService.getSumApproval(b) <= 1) {
                                auditFlowNodeRole.setNodeId(list1.get(i).getNodeId());
                                auditFlowNodeRole.setState(list1.get(i).getState());
                                auditFlowNodeRole.setCurrentId(b);
                                List<AuditFlowNodeRole> r = auditFlowNodeRoleService.getUserIdByNodeId(auditFlowNodeRole);
                                for (int c = 0; c < r.size(); c++) {
                                    int userId = r.get(c).getUserId();
                                    msgEvtInfo.setType(3);
                                    msgEvtInfo.setEventId(b);
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
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * 解决已经拒绝审批，但任然进行审批提醒的问题
     *
     * @param auditFlowNodeRole
     * @param auditFlowOperRecord
     * @param msgEvtInfo
     */
    public void test(AuditFlowNodeRole auditFlowNodeRole, AuditFlowOperRecord auditFlowOperRecord, MsgEvtInfo msgEvtInfo) {
        //在审批记录表中拿到审批皮不通过的信息
        auditFlowOperRecord.setOperType(0);
        List<AuditFlowOperRecord> list = auditFlowOperRecordMapper.selectAuditFlowOperRecordList(auditFlowOperRecord);
        for (int i = 0; i < list.size(); i++) {
            Integer b = list.get(i).getCurrentId();
            Integer currentNodeId = list.get(i).getCurrentNodeId();
            auditFlowNodeRole.setCurrentId(b);
            auditFlowNodeRole.setNodeId(currentNodeId);
            List<AuditFlowNodeRole> list3 = auditFlowNodeRoleMapper.selectUsers(auditFlowNodeRole);
            if (!list3.isEmpty() & list3 != null) {
                for (int g = 0; g < list3.size(); g++) {
                    msgEvtInfo.setType(3);
                    msgEvtInfo.setEventId(b);
                    msgEvtInfo.setUserId(list3.get(g).getUserId());
                    msgEvtInfoService.deleteForMsg(msgEvtInfo);
                }
            }

        }
    }

}
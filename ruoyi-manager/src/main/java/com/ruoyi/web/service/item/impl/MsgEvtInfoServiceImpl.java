package com.ruoyi.web.service.item.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.item.SysProjectTableService;
import com.ruoyi.web.service.item.TaskTableService;
import com.ruoyi.web.util.constant.ManagerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.domain.MsgEvtInfo;
import com.ruoyi.web.mapper.item.MsgEvtInfoMapper;
import com.ruoyi.web.service.item.MsgEvtInfoService;

/**
 * 消息模块实现类
 *
 * @author zhongzhilong
 * @date 2020/9/15 0015
 */
@Service
public class MsgEvtInfoServiceImpl implements MsgEvtInfoService {

    @Autowired
    private MsgEvtInfoMapper msgEvtInfoMapper;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;
    @Autowired
    private SysProjectTableService sysProjectTableService;
    @Autowired
    private AuditFlowCurrentService auditFlowCurrentService;
    @Autowired
    private TaskTableService taskTableService;

    @Override
    public List<MsgEvtInfo> selectMsgListByType(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.selectMsgListByType(msgEvtInfo);
    }

    @Override
    public List<MsgEvtInfo> selectMsgList(MsgEvtInfo msgEvtInfo) {
        List<MsgEvtInfo> list = msgEvtInfoMapper.selectMsgList(msgEvtInfo);
        msgEvtInfo.setPages(list.size());
        if (msgEvtInfo.getPageNumber() != null && msgEvtInfo.getPageNumber() != 0) {
            list = list.stream()
                    .skip((msgEvtInfo.getPageNumber() - 1) * msgEvtInfo.getTotal())
                    .limit(msgEvtInfo.getTotal())
                    .collect(Collectors.toList());
        }
        return list;
    }

    @Override
    public int update(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.update(msgEvtInfo);
    }

    @Override
    public int insertMessageInfo(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.insertMessageInfo(msgEvtInfo);
    }

    @Override
    public List<MsgEvtInfo> selectOneMsgList(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.selectOneMsgList(msgEvtInfo);
    }

    @Override
    public List<MsgEvtInfo> selectMsgForApproval(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.selectMsgForApproval(msgEvtInfo);
    }

    @Override
    public int deleteForMsg(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.deleteForMsg(msgEvtInfo);
    }

    @Override
    public int updateMessageInfo(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.updateMessageInfo(msgEvtInfo);
    }

    @Override
    public int selectCountReadMark(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.selectCountReadMark(msgEvtInfo);
    }

    @Override
    public void save(Map<String, Object> map) {
        msgEvtInfoMapper.save(map);
    }

    @Override
    public MsgEvtInfo selectMsgById(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.selectMsgById(msgEvtInfo);
    }


    @Override
    public List<MsgEvtInfo> selectMsgListByType1(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.selectMsgListByType1(msgEvtInfo);
    }

    @Override
    public int updateReadMark(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.updateReadMark(msgEvtInfo);
    }

    @Override
    public List<MsgEvtInfo> selectTypeForTen(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.selectTypeForTen(msgEvtInfo);
    }

    @Override
    public int updateInfoById(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.updateInfoById(msgEvtInfo);
    }

    @Override
    public int updateMessageInfo2(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.updateMessageInfo2(msgEvtInfo);
    }

    @Override
    public List<MsgEvtInfo> selectMsgLists(MsgEvtInfo msgEvtInfo) {
        return msgEvtInfoMapper.selectMsgLists(msgEvtInfo);
    }

    @Override
    public PageEntity selectMsgEvtList(MsgEvtInfo msgEvtInfo) {
        long userId = ShiroUtils.getUserId();
        List<MsgEvtInfo> list = new ArrayList<>();
        Integer types = msgEvtInfo.getType();
        Integer readMark = msgEvtInfo.getReadMark();
        if (types == null || readMark == null) {
            return null;
        }
        msgEvtInfo.setType(types);
        msgEvtInfo.setUserId((int) userId);
        if (types.equals(ManagerConstant.MSG_TASK)) {
            //0：任务到期，1：任务逾期，2：任务未开始
            msgEvtInfo.setReadMark(readMark);
            msgEvtInfo.setUserId((int) userId);
            list = msgEvtInfoService.selectMsgListByType(msgEvtInfo);
        } else if (types.equals(ManagerConstant.MSG_APPROVAL)) {
            //审批提醒
            msgEvtInfo.setReadMark(readMark);
            list = msgEvtInfoService.selectMsgList(msgEvtInfo);
        } else if (types.equals(ManagerConstant.MSG_WRITE)) {
            //填写、提交通知
            msgEvtInfo.setReadMark(readMark);
            msgEvtInfo.setUserId((int) userId);
            list = msgEvtInfoService.selectMsgListByType1(msgEvtInfo);
        } else if (types.equals(ManagerConstant.MSG_NOT_APPROVAL)) {
            //项目、任务拒批提醒
            msgEvtInfo.setReadMark(readMark);
            msgEvtInfo.setUserId((int) userId);
            list = msgEvtInfoService.selectTypeForTen(msgEvtInfo);
        } else if (types.equals(ManagerConstant.MSG_COPY)) {
            //查询抄送的信息
            msgEvtInfo.setReadMark(readMark);
            msgEvtInfo.setUserId((int) userId);
            list = msgEvtInfoService.selectMsgList(msgEvtInfo);
        } else if (types.equals(ManagerConstant.MSG_NO_PROGRESS)) {
            //查询项目无进展消息
            msgEvtInfo.setReadMark(readMark);
            msgEvtInfo.setUserId((int) userId);
            list = msgEvtInfoService.selectMsgList(msgEvtInfo);
        }
        if (!ShiroUtils.isEmpty(list)) {
            for (MsgEvtInfo msg : list) {
                // 审批流id
                long currentId = msg.getEventId();
                int auditId = msg.getAuditId();
                // 任务的为任务id,项目的为项目id
                long eventId = msg.getEventId();
                Integer createApprovalUserId = msg.getCreateApproval();
                if (createApprovalUserId == null) {
                    createApprovalUserId = 0;
                }
                int type = msg.getType();
                String name = null;
                String taskName = null;
                String projectName = null;
                Integer applyId = auditFlowCurrentService.selectApplyIdByCurrentId((int) currentId);
                if ((type == 3 || type == 11) && (auditId == 3 | auditId == 7 | auditId == 10 | auditId == 11 || auditId == 13 || auditId == 14)) {
                    name = sysProjectTableService.selectProjectNameById(applyId);
                    msg.setpIds(applyId);
                }
                if ((type == 3 || type == 11) && (auditId == 1 | auditId == 8 | auditId == 9 | auditId == 12)) {
                    if (type == 11 && applyId == null) {
                        name = taskTableService.selectTitle(eventId);
                        msg.settIds((int) eventId);
                    } else {
                        name = taskTableService.selectTitle(applyId);
                        msg.setCurrentId((int) currentId);
                        msg.settIds(applyId);
                    }
                }
                if (type == 0 | type == 1 | type == 2) {
                    taskName = taskTableService.selectTitle(eventId);
                }
                if (type == 9 && (auditId == 1 | auditId == 8 | auditId == 9 || auditId == 12)) {
                    taskName = taskTableService.selectTitle(applyId);
                }
                if (type == 5 | type == 6 | type == 12) {
                    // event为项目的id
                    projectName = sysProjectTableService.selectProjectNameById(eventId);
                }
                if (type == 8 && (auditId == 3 | auditId == 7 | auditId == 10 | auditId == 11)) {
                    projectName = sysProjectTableService.selectProjectNameById(applyId);
                }
                if (ShiroUtils.getUserId().intValue() == createApprovalUserId) {
                    msg.setApplyAndParticipateState(1);
                }
                if (ShiroUtils.getUserId().intValue() != createApprovalUserId) {
                    msg.setApplyAndParticipateState(0);
                }
                msg.setCurrentName(name);
                msg.setTaskName(taskName);
                msg.setProjectName(projectName);
            }
        }
        PageEntity pageEntity = new PageEntity(list.size(), msgEvtInfo.getPages(), msgEvtInfo.getPageNumber(), list);
        return pageEntity;
    }
}

package com.ruoyi.web.service.item.impl;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.mapper.audit.AuditFlowNodeRoleMapper;
import com.ruoyi.web.mapper.item.ProjectPlanMapper;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.audit.AuditFlowOperRecordService;
import com.ruoyi.web.service.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectPlanServiceImpl implements ProjectPlanService {
    @Autowired
    private ProjectPlanMapper projectPlanMapper;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private ProjectTaskTableService projectTaskTableService;
    @Autowired
    private TaskUserService taskUserService;
    @Autowired
    private AuditFlowCurrentService iAuditFlowCurrentService;
    @Autowired
    private AuditFlowOperRecordService auditFlowOperRecordService;
    @Autowired
    private AuditFlowNodeRoleMapper auditFlowNodeRoleMapper;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;
    @Autowired
    private ResultApprovalRecordService resultApprovalRecordService;

    /**
     * 添加里程碑
     *
     * @param projectPlanTable
     * @return
     */
    @Override
    public int insertPlan(ProjectPlanTable projectPlanTable) {
        return projectPlanMapper.insertPlan(projectPlanTable);
    }

    @Override
    public List<ProjectPlanTable> selectAllPlan(ProjectPlanTable projectPlanTable) {
        return projectPlanMapper.selectAllPlan(projectPlanTable);
    }

    /**
     * 删除里程碑标题
     */
    @Override
    public int deletePlanTitle(ProjectPlanTable projectPlanTable) {
        // 删除标题的同时删除旗下的所有任务
        deleteAllInfosBypId(projectPlanTable.getPlanId());
        return projectPlanMapper.deletePlanTitle(projectPlanTable);
    }

    /**
     * 删除所有信息的接口
     *
     * @param planId
     * @return
     */
    public int deleteAllInfosBypId(Integer planId) {
        int a = 0;
        List<Integer> auditList = new ArrayList<>();
        auditList.add(1);
        auditList.add(8);
        auditList.add(9);
        auditList.add(12);
        List<Integer> list = new ArrayList<>();
        list.add(3);
        list.add(5);
        list.add(6);
        list.add(8);
        list.add(11);
        // 根据plan获得所有的tId
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        List<TaskTable> taskTableList = taskTableService.selectTaskTableByPlantId(planId);
        if (ShiroUtils.isNotEmpty(taskTableList)) {
            for (TaskTable t : taskTableList) {
                Long tId = t.gettId();
                // 删除任务
                taskTableService.deleteTaskTableById(tId);
                // 删除中间表
                ProjectTaskTable projectTaskTable = new ProjectTaskTable();
                projectTaskTable.setEventId(tId.intValue());
                projectTaskTable.setTypeId(0);
                projectTaskTableService.deleteInfos(projectTaskTable);
                // 删除参与人表
                taskUserService.deleteTaskUserById(tId);
                auditFlowCurrent.setApplyId(tId.intValue());
                auditFlowCurrent.setAuditList(auditList);
                List<AuditFlowCurrent> currentList = iAuditFlowCurrentService.selectCurrentId(auditFlowCurrent);
                if (ShiroUtils.isNotEmpty(currentList)) {
                    for (AuditFlowCurrent c : currentList) {
                        Integer currentId = c.getCurrentId();
                        // 删除消息
                        msgEvtInfo.setEventId(currentId);
                        msgEvtInfo.setTypeList(list);
                        a = msgEvtInfoService.deleteForMsg(msgEvtInfo);
                        // 删除审批记录
                        ResultApprovalRecord resultApprovalRecord = new ResultApprovalRecord();
                        resultApprovalRecord.setCurrentId(currentId);
                        a = resultApprovalRecordService.deleteApprovalRecord(resultApprovalRecord);
                        // 删除审批流表
                        auditFlowCurrent.setCurrentId(currentId);
                        a = iAuditFlowCurrentService.deleteAuditFlowCurrentById(auditFlowCurrent);
                        // 删除权限
                        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
                        auditFlowNodeRole.setCurrentId(currentId);
                        a = auditFlowNodeRoleMapper.deleteFlowNodeRoleByCurrentId(auditFlowNodeRole);
                        // 删除审批过程详细表
                        AuditFlowOperRecord auditFlowOperRecord = new AuditFlowOperRecord();
                        auditFlowOperRecord.setCurrentId(currentId);
                        auditFlowOperRecordService.deleteOperRecordByCurrentId(auditFlowOperRecord);
                    }
                }
            }
        }
        return a;
    }

    /**
     * 根据项目id获取里程碑信息
     *
     * @param id
     * @return
     */
    @Override
    public List<ProjectPlanTable> selectAllByProjectId(Integer id) {
        return projectPlanMapper.selectAllByProjectId(id);
    }

    @Override
    public ProjectPlanTable selectProjectPlanTableById(Long planId) {
        return projectPlanMapper.selectProjectPlanTableById(planId);
    }

    @Override
    public Integer selectProjectPlanTableIdByTitle(ProjectPlanTable projectPlanTable) {
        return projectPlanMapper.selectProjectPlanTableIdByTitle(projectPlanTable);
    }

}

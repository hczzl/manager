package com.ruoyi.web.service.item.impl;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.mapper.item.ProjectMarkStageMapper;
import com.ruoyi.web.service.item.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProjectMarkStageServiceImpl implements ProjectMarkStageService {

    @Autowired
    ProjectMarkStageMapper projectMarkStageMapper;
    @Autowired
    private SysProjectTableService sysProjectTableService;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;
    @Autowired
    private ParticipantsService participantsService;
    @Autowired
    private ProjectTaskTableService projectTaskTableService;


    @Override
    public int insertStage(ProjectMarketStageTable projectMarketStageTable) {

        return projectMarkStageMapper.insertStage(projectMarketStageTable);
    }

    @Override
    public List<ProjectMarketStageTable> selectAll(ProjectMarketStageTable projectMarketStageTable) {
        return projectMarkStageMapper.selectAll(projectMarketStageTable);
    }

    @Override
    public int deleteStage(ProjectMarketStageTable projectMarketStageTable) {
        return projectMarkStageMapper.deleteStage(projectMarketStageTable);
    }

    @Override
    public List<ProjectMarketStageTable> selectById(int p) {
        return projectMarkStageMapper.selectById(p);
    }

    @Override
    public void addInfo(ProjectMarketStageTable projectMarketStageTable) {
        projectMarkStageMapper.addInfo(projectMarketStageTable);
    }

    @Override
    public void updateUserByName(ProjectMarketStageTable projectMarketStageTable) {
        projectMarkStageMapper.updateUserByName(projectMarketStageTable);
    }

    @Override
    public List<ProjectMarketStageTable> selectProjects() {
        return projectMarkStageMapper.selectProjects();
    }

    @Override
    public int openExcel() {
        int a = 0;
        try {
            Runtime.getRuntime().exec("cmd /c start D://import.xls");
            a = 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return a;
    }

    @Override
    public List<ProjectMarketStageTable> selectStage(ProjectMarketStageTable projectMarketStageTable) {
        return projectMarkStageMapper.selectStage(projectMarketStageTable);
    }

    @Override
    public ProjectMarketStageTable selectStageById(ProjectMarketStageTable p) {
        return projectMarkStageMapper.selectStageById(p);
    }

    @Override
    public int updateStage(ProjectMarketStageTable p) {
        return projectMarkStageMapper.updateStage(p);
    }

    @Override
    public List<ProjectMarketStageTable> selectAllStage(ProjectMarketStageTable projectMarketStageTable) {
        return projectMarkStageMapper.selectAllStage(projectMarketStageTable);
    }

    @Override
    public List<ProjectMarketStageTable> selectStageByProjectId(ProjectMarketStageTable p) {
        return projectMarkStageMapper.selectStageByProjectId(p);
    }

    @Override
    public int updateStageById(ProjectMarketStageTable projectMarketStageTable) {
        return projectMarkStageMapper.updateStageById(projectMarketStageTable);
    }

    /**
     * 调用更新负责人的接口
     *
     * @param p
     * @return
     */
    @Override
    public int updateStageBranch(@RequestBody ProjectMarketStageTable p) {
        int a = 0;
        int userId = ShiroUtils.getUserId().intValue();
        SysProjectTable sysProjectTable = new SysProjectTable();
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        int projectId = p.getProjectId();
        ParticipantsTable participantsTable = new ParticipantsTable();
        // 先删除参与人表对应信息
        participantsTable.setProjectId(projectId);
        int b = 0;
        b = participantsService.deletePeople(participantsTable);
        sysProjectTable.setpId(projectId);
        Map<String, Object> map = sysProjectTableService.selectTechniquePeopleId(projectId);
        Integer techniquePeople = (Integer) map.get("techniquePeopleId");
        Long chargePeople = (Long) map.get("chargepeopleId");
        if (techniquePeople == null) {
            techniquePeople = 0;
        }
        if (chargePeople == null) {
            chargePeople = 0L;
        }
        p.setUpdateBy(userId + "");
        p.setUpdateTime(new Date());
        p.setProjectId(projectId);
        ProjectMarketStageTable[] resources = p.getResources();
        for (int i = 0; i < resources.length; i++) {
            p.setStageType(resources[i].getStageType());
            Integer chargeId = resources[i].getChargePeople();
            p.setChargePeople(resources[i].getChargePeople());
            String people = resources[i].getParticipantsPeople();
            // 商务负责人、技术负责人能添加到阶段表，不能添加到参与人表
            p.setParticipantsPeople(people);
            deleteStage(projectId, resources[i].getStageType(), chargeId);
            a = projectMarkStageMapper.updateStage(p);
            // 当阶段添加进了阶段表，这个时候将会提醒对应的分管负责人,即根据阶段去提醒对应的分管负责人
            if (a == 1) {
                // 提醒阶段的负责人
                remindChargePeople(p, sysProjectTable, resources[i].getStageType(), projectId, resources[i].getChargePeople());
            }
            // 添加市场项目的参与人
            if (chargeId != null || people != null) {
                insertParticipants(resources[i].getStageType(), people, projectId, chargeId, techniquePeople, chargePeople.intValue());
            }
        }
        // 更新对于技术总负责人的消息提醒
        if (userId == techniquePeople && a == 1) {
            // 用户
            msgEvtInfo.setUserId(userId);
            // 项目
            msgEvtInfo.setEventId(projectId);
            msgEvtInfo.setReadMark(1);
            List<Integer> typeList = new ArrayList<>();
            typeList.add(5);
            msgEvtInfo.setTypeList(typeList);
            msgEvtInfo.setUpdateTime(new Date());
            msgEvtInfoService.updateMessageInfo2(msgEvtInfo);
        }


        return a;
    }

    /**
     * 将阶段的参与人和负责人添加到参与人表中
     *
     * @param projectId
     * @param chargeId
     */
    public void insertParticipants(Integer stageId, String people, Integer projectId, Integer chargeId, Integer techId, Integer chId) {
        List<Integer> list = new ArrayList<>();
        String[] arr = people.split(",");
        for (String a : arr) {
            if (a != null && a.length() > 0) {
                Integer uId = Integer.parseInt(a);
                if (!uId.equals(techId) && !uId.equals(chId)) {
                    list.add(uId);
                }
            }
        }
        if (!chargeId.equals(chId) && !chargeId.equals(techId)) {
            list.add(chargeId);
        }
        //去重
        List<Integer> newList = list.stream().distinct().collect(Collectors.toList());
        ParticipantsTable pant = new ParticipantsTable();
        //插入信息
        if (ShiroUtils.isNotEmpty(list)) {
            for (int i = 0; i < list.size(); i++) {
                pant.setUserId(list.get(i));
                pant.setProjectId(projectId);
                List<ParticipantsTable> pantList = participantsService.selectInfo(pant);
                if (pantList.size() < 1) {
                    // 插入参与人
                    participantsService.insertparticipants(pant);
                }
                inserts(projectId, i);
            }
        }
    }

    /**
     * 填写通知，当技术负责人填写各个chargePepelo，且提交更新的时候，这个时候进行提醒，即发消息，只发一次消息
     * 根据项目id,查询分管负责人
     *
     * @param p
     * @param sysProjectTable
     * @param stageType       阶段主键
     * @param projectId       项目id
     * @param chargeId        阶段负责人
     */
    public void remindChargePeople(ProjectMarketStageTable p, SysProjectTable sysProjectTable,
                                   int stageType, int projectId, int chargeId) {
        p.setProjectId(projectId);
        p.setStageType(stageType);
        List<ProjectMarketStageTable> list = projectMarkStageMapper.selectStageByProjectId(p);
        Integer warnState = 0;
        for (int i = 0; i < list.size(); i++) {
            warnState = list.get(i).getWarnState();
        }
        MsgEvtInfo m = new MsgEvtInfo();
        // 获得技术负责人
        sysProjectTable.setpId(projectId);
        Map<String, Object> map = sysProjectTableService.selectTechniquePeopleId(projectId);
        Integer techniquePeople = (Integer) map.get("techniquePeopleId");
        if (techniquePeople == null) {
            techniquePeople = 0;
        }
        // 5表示填写提醒
        m.setType(5);
        // eventId，对应着项目id
        m.setEventId(projectId);
        m.setAuditId(stageType);
        m.setUserId(chargeId);
        m.setCreateTime(new Date());
        if (techniquePeople != chargeId) {
            p.setProjectId(projectId);
            p.setStageType(stageType);
            p.setUpdateBy(ShiroUtils.getUserId() + "");
            p.setUpdateTime(new Date());
            // 在添加到数据库的时候判断是否已经存在且是否已经提醒过
            List<MsgEvtInfo> msgEvtInfoList = msgEvtInfoService.selectMsgLists(m);
            if (msgEvtInfoList.size() < 1) {
                int a = msgEvtInfoService.insertMessageInfo(m);
                if (a == 1) {
                    p.setWarnState(1);
                    int b = projectMarkStageMapper.updateStage(p);
                }
            }
            if (msgEvtInfoList.size() > 0 && warnState == 0) {
                p.setWarnState(1);
                int b = projectMarkStageMapper.updateStage(p);
            }
        }
    }

    /**
     * 删除原阶段负责人的消息
     *
     * @param projectId
     * @param stageType
     * @param chargeId
     */
    public void deleteStage(Integer projectId, Integer stageType, Integer chargeId) {
        ProjectMarketStageTable projectMarketStageTable = new ProjectMarketStageTable();
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        // 获得原阶段的负责人
        projectMarketStageTable.setProjectId(projectId);
        projectMarketStageTable.setStageType(stageType);
        List<ProjectMarketStageTable> list = projectMarkStageMapper.selectStageByProjectId(projectMarketStageTable);
        if (!list.isEmpty() && list != null) {
            for (ProjectMarketStageTable p : list) {
                Integer c = p.getChargePeople();
                // 当现在的负责人和原来的负责人不一样时，此时删除原来阶段的负责人对应的消息
                if (c != null && !c.equals(chargeId)) {
                    msgEvtInfo.setType(5);
                    msgEvtInfo.setEventId(projectId);
                    msgEvtInfo.setAuditId(stageType);
                    msgEvtInfo.setUserId(c);
                    msgEvtInfoService.deleteForMsg(msgEvtInfo);
                }
            }
        }
    }

    /**
     * 添加对应的用户id和项目id
     *
     * @param projectId
     * @param userId
     */
    public void inserts(Integer projectId, Integer userId) {
        ProjectTaskTable projectTaskTable = new ProjectTaskTable();
        projectTaskTable.setUserId(userId);
        projectTaskTable.setEventId(projectId);
        projectTaskTable.setTypeId(1);
        List<ProjectTaskTable> ps = projectTaskTableService.selectInfos(projectTaskTable);
        if (ps.size() < 1) {
            projectTaskTable.setCreateTime(new Date());
            projectTaskTableService.insertProjectTask(projectTaskTable);
        }
        if (ps.size() > 0) {
            projectTaskTable.setUpdateTime(new Date());
            projectTaskTableService.updateProjectTask(projectTaskTable);
        }
    }

}

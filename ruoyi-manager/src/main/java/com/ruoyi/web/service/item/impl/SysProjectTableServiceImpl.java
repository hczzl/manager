package com.ruoyi.web.service.item.impl;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.TaskProject;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.domain.ResultApprovalRecord;
import com.ruoyi.web.domain.vo.SysProjectTableTechnologyVo;
import com.ruoyi.web.domain.vo.SysProjectTableVo;
import com.ruoyi.web.domain.vo.TaskTableVo;
import com.ruoyi.web.util.constant.ManagerConstant;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.mapper.audit.AuditFlowCurrentMapper;
import com.ruoyi.web.mapper.audit.AuditFlowNodeRoleMapper;
import com.ruoyi.web.mapper.item.*;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.audit.AuditFlowOperRecordService;
import com.ruoyi.web.service.item.*;
import com.ruoyi.web.util.CalculateHours;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class SysProjectTableServiceImpl implements SysProjectTableService {

    @Autowired
    private SysProjectTableMapper sysProjectTableMapper;
    @Autowired
    private AuditFlowCurrentService iAuditFlowCurrentService;
    @Autowired
    private AuditFlowOperRecordService auditFlowOperRecordService;
    @Autowired
    private AuditFlowNodeRoleMapper auditFlowNodeRoleMapper;
    @Autowired
    private SysCompetitorService sysCompetitorService;
    @Autowired
    private ProjectMarkStageService projectMarkStageService;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private ParticipantsService participantsService;
    @Autowired
    private ResultApprovalRecordService resultApprovalRecordService;
    @Autowired
    private ProjectMarkTypeMapper projectMarkTypeMapper;
    @Autowired
    private ProjectTaskTableService projectTaskTableService;
    @Autowired
    private SysFileInfoService sysFileInfoService;
    @Autowired
    private SysCompetitorMapper sysCompetitorMappere;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ProduceTypeMapper produceTypeMapper;
    @Autowired
    private AuditFlowCurrentMapper auditFlowCurrentMapper;
    @Autowired
    private MsgEvtInfoMapper msgEvtInfoMapper;
    @Autowired
    private ProduceTypeService produceTypeService;
    @Autowired
    private ProjectMarkTypeService projectMarkTypeService;
    @Autowired
    private ISysUserService userService;

    /**
     * 添加市场项目
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public int insertMarkProject(SysProjectTable sysProjectTable) {
        int a = sysProjectTableMapper.insertMarkProject(sysProjectTable);
        sysProjectTable.setFlowId(3);
        sysProjectTable.setCreateBy(ShiroUtils.getUserId() + "");
        if (sysProjectTable.getProjectCache() == 0) {
            int b = iAuditFlowCurrentService.insertAuditFlowCurrentProject(sysProjectTable);
        }
        return a;
    }

    /**
     * 查找所有的id,遍历得到最大的id
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public List<SysProjectTable> getBigId(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.getBigId(sysProjectTable);
    }

    /**
     * 市场项目的查询-敏感信息做特殊处理
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public List<SysProjectTable> selectAllInfo(SysProjectTable sysProjectTable) {
        Integer pId = sysProjectTable.getpId();
        List<SysProjectTable> list = sysProjectTableMapper.selectUserIdsBypId(pId);
        // 获得第一页的所有参与人
        List<Integer> userIdsList = selectUserIds(list);
        Integer nowUserId = ShiroUtils.getUserId().intValue();
        if (userIdsList.contains(nowUserId)) {
            // 第一页的人能同时看到第一页和第二页的内容
            sysProjectTable.setParameter(1);
            SysProjectTable.setYes(1);
        } else {
            sysProjectTable.setParameter(0);
            SysProjectTable.setYes(0);
        }
        // 给前端返回该项目审批到哪个阶段了
        List<SysProjectTable> list3 = sysProjectTableMapper.selectAllInfo(sysProjectTable);
        for (SysProjectTable s : list3) {
            if (sysProjectTable.getParameter() == 1) {
                s.setIsFirstAuthority(1);
                s.setIsSecondAuthority(1);
            } else {
                s.setIsFirstAuthority(0);
                s.setIsSecondAuthority(1);
            }
            // 获取到审批到哪一个阶段
            // 获取到当前审批所有未审批的用户userId
            s = selectApprovalStage(s.getpId(), s);
        }
        return list3;
    }

    /**
     * 获得市场立项第一页人员id
     *
     * @param list
     * @return
     */
    public List<Integer> selectUserIds(List<SysProjectTable> list) {
        List<Integer> userIdsList = new ArrayList<>();
        List<Integer> newList = new ArrayList<>();
        for (SysProjectTable sysProjectTable : list) {
            // 商务负责人
            Integer chargeId = sysProjectTable.getChargepeopleId();
            if (chargeId != null) {
                userIdsList.add(chargeId);
            }
            Integer techniquePeople = sysProjectTable.getTechniquePeople();
            if (techniquePeople != null) {
                userIdsList.add(techniquePeople);
            }
            String businessParticipants = sysProjectTable.getBusinessParticipants();
            if (businessParticipants != null && businessParticipants != "") {
                String[] arr = businessParticipants.split(",");
                for (String a : arr) {
                    if (a != null && a.length() != 0) {
                        userIdsList.add(Integer.parseInt(a));
                    }
                }
            }
            List<AuditFlowNodeRole> list1 = auditFlowNodeRoleMapper.selectUserIds(sysProjectTable.getpId());
            for (AuditFlowNodeRole auditFlowNodeRole : list1) {
                userIdsList.add(auditFlowNodeRole.getUserId());
            }
            // 添加主管和领导的权限
            List<Integer> userList = selectMainPeople();
            for (Integer u : userList) {
                userIdsList.add(u);
            }
            userIdsList.add(Integer.parseInt(sysProjectTable.getCreateBy()));
            // 去重，java 8新特性
            if (!userIdsList.isEmpty() && userIdsList != null) {
                newList = userIdsList.stream().distinct().collect(Collectors.toList());
            }
        }
        return newList;
    }

    /**
     * 获取审批阶段
     *
     * @param pId
     * @param s
     * @return
     */
    public SysProjectTable selectApprovalStage(Integer pId, SysProjectTable s) {
        String currentApprovelUserIds = "";
        Integer approvalStage = 0;
        Integer currentId = 0;
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        AuditFlowNodeRole auditFlowNodeRole = new AuditFlowNodeRole();
        auditFlowCurrent.setApplyId(pId);
        auditFlowCurrent.setAuditId(3);
        List<AuditFlowCurrent> list = iAuditFlowCurrentService.selectCurrentId(auditFlowCurrent);
        if (!list.isEmpty() && list != null) {
            for (AuditFlowCurrent a : list) {
                // 审核状态:1已提交,2审批中,3审批不通过，4审批通过,5审批全部通过，6转推中
                approvalStage = a.getCurrentNodeId() - 1;
                currentId = a.getCurrentId();
                if (currentId != null) {
                    // 获得所有的未审批通过的用户id
                    auditFlowNodeRole.setCurrentId(currentId);
                    auditFlowNodeRole.setNodeId(a.getCurrentNodeId());
                    auditFlowNodeRole.setState(1);
                    List<AuditFlowNodeRole> userIdsList = auditFlowNodeRoleMapper.selectAuditFlowNodeRoleList(auditFlowNodeRole);
                    for (AuditFlowNodeRole role : userIdsList) {
                        currentApprovelUserIds = currentApprovelUserIds + role.getUserId() + ",";
                    }
                }

            }
        }
        s.setApprovalStage(approvalStage);
        s.setProjectCurrentId(currentId);
        // 去除最后的逗号
        if (currentApprovelUserIds.length() > 0) {
            currentApprovelUserIds = currentApprovelUserIds.substring(0, currentApprovelUserIds.length() - 1);
        }
        s.setCurrentApprovelUserIds(currentApprovelUserIds);
        return s;
    }

    public List<Integer> selectMainPeople() {
        List<Integer> list = new ArrayList<>();
        SysUser user = new SysUser();
        // 获得所有领导的userId
        user.setDeptId(107L);
        List<SysUser> userslist = userService.selectInfosByWxId(user);
        // 获得市场部主管userId
        user.setDeptId(102L);
        user.setRankId(28 + "");
        List<SysUser> userslist1 = userService.selectInfosByWxId(user);
        for (SysUser u : userslist) {
            list.add(u.getUserId().intValue());
        }
        for (SysUser u : userslist1) {
            list.add(u.getUserId().intValue());
        }
        return list;
    }

    @Override
    public List<SysProjectTable> selectUserIdsBypId(Integer pId) {
        return sysProjectTableMapper.selectUserIdsBypId(pId);
    }

    public List<String> removeNullStringArray(String[] arrayString) {
        List<String> list1 = new ArrayList<String>();
        for (int i = 0; i < arrayString.length; i++) {
            // 过滤掉数组arrayString里面的空字符串
            if (arrayString[i] != null && arrayString[i].length() != 0) {
                list1.add(arrayString[i]);
            }
        }
        return list1;
    }

    /**
     * 市场项目的删除
     *
     * @param id
     * @return
     */
    @Override
    public int deleteProject(int id) {
        return sysProjectTableMapper.deleteProject(id);
    }

    /**
     * 根据类型类查询项目
     *
     * @param projectType
     * @return
     */
    @Override
    public List<SysProjectTable> selectProjectByType(String projectType) {
        return sysProjectTableMapper.selectProjectByType(projectType);
    }

    /**
     * 根据项目名称关键字模糊查询立项列表
     *
     * @param title
     * @return
     */
    @Override
    public List<SysProjectTable> selectProjectByTitle(String title) {
        return sysProjectTableMapper.selectProjectByTitle(title);
    }

    /**
     * 根据立项状态查出立项列表
     *
     * @param status
     * @return
     */
    @Override
    public List<SysProjectTable> selectProjectByStatus(String status) {
        return sysProjectTableMapper.selectProjectByStatus(status);
    }

    /**
     * 三条件一起查询，满足其中一个条件就可以查找到
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public List<SysProjectTable> selectAllestablish(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.selectAllestablish(sysProjectTable);
    }

    /**
     * 查询我的项目
     *
     * @param id
     * @return
     */
    @Override
    public List<SysProjectTable> getMyProject(int id) {
        return sysProjectTableMapper.getMyProject(id);
    }

    /**
     * 根据id查询负责热人
     */
    @Override
    public int getChargeIdById(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.getChargeIdById(sysProjectTable);
    }

    @Override
    public List<SysProjectTable> selectProjectList(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.selectProjectList(sysProjectTable);
    }

    /**
     * 根据条件分页查询项目列表
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public List<SysProjectTable> selectAllProject(SysProjectTable sysProjectTable) {
        List<SysProjectTable> resultList = sysProjectTableMapper.selectAllProject(sysProjectTable);
        return resultList;
    }

    @Override
    public List<SysProjectTableVo> selectProjectVos(SysProjectTableVo sysProjectTableVo) {
        List<SysProjectTableVo> sysProjectTableVos = sysProjectTableMapper.selectProjectVos(sysProjectTableVo);
        sysProjectTableVos = sysProjectTableVos.stream()
                .sorted(Comparator.comparing(SysProjectTableVo::getProjectType).reversed())
                .collect(Collectors.toList());
        return sysProjectTableVos;
    }

    /**
     * 根据id获取该项目的详细信息
     *
     * @param pId
     * @return
     */
    @Override
    public SysProjectTable getProjectTable(int pId) {
        return sysProjectTableMapper.getProjectTable(pId);
    }

    @Override
    public SysProjectTable getProjectTableByid(int pId) {
        return sysProjectTableMapper.getProjectTableByid(pId);
    }

    /**
     * 添加立项，则establish_status默认为0
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    @Transactional
    public int insertProject(SysProjectTable sysProjectTable) {
        int a = sysProjectTableMapper.insertProject(sysProjectTable);
        sysProjectTable.setFlowId(7);
        sysProjectTable.setCreateBy(ShiroUtils.getUserId().toString());
        if (sysProjectTable.getProjectCache() == 0) {
            // 添加审批信息
            a = iAuditFlowCurrentService.insertAuditFlowCurrentProject(sysProjectTable);
        }
        return a;
    }

    /**
     * 查询立项列表
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public List<SysProjectTable> selectEstablish(SysProjectTable sysProjectTable) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!MyUtils.isEmpty(sysProjectTable.getStartTimes())) {
            sysProjectTable.setStartTimes(format.format(new Date(new Long(sysProjectTable.getStartTimes()))));
        }
        if (!MyUtils.isEmpty(sysProjectTable.getEndTimes())) {
            sysProjectTable.setEndTimes(format.format(new Date(new Long(sysProjectTable.getEndTimes()))));
        }
        String peopleString = sysProjectTable.getChargePeopleName();
        if (StringUtils.isNotEmpty(peopleString)) {
            String[] split = peopleString.split(",");
            sysProjectTable.setPeopleList(Arrays.asList(split));
        }
        // 如果关键词查询不为空,如果关键词是姓名，则不查标题
        String queryInfo = sysProjectTable.getQueryInfo();
        if (StringUtils.isNotEmpty(queryInfo)) {
            sysProjectTable.setTitle(queryInfo);
        }
        return sysProjectTableMapper.selectEstablish(sysProjectTable);
    }

    @Override
    public List<SysProjectTable> selectEstablishById(int id) {
        return sysProjectTableMapper.selectEstablishById(id);
    }

    /**
     * 终止项目更新,项目提交完成审批
     *
     * @param
     * @return
     */
    @Override
    @Transactional
    public int updateProject(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updateProject(sysProjectTable);
    }

    /**
     * 修改立项状态,使得能够完成立项审批
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public int updateEstablishStatus(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updateEstablishStatu(sysProjectTable);
    }

    /**
     * 查询已经关闭的项目列表
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public List<SysProjectTable> endProject(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.endProject(sysProjectTable);
    }

    /**
     * 获得项目进度
     *
     * @param p_id
     * @return
     */
    @Override
    public Double getPlanRate(int pId) {
        Double plan_rate = sysProjectTableMapper.getPlanRate(pId);
        if (plan_rate == null) {
            plan_rate = 0.0;
        }
        return plan_rate;
    }

    /**
     * 根据id来更新进度条
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public int updatePlan(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updatePlan(sysProjectTable);
    }

    /**
     * 更新项目参与人的名称
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public int updateParticipantsName(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updateParticipantsName(sysProjectTable);
    }

    /**
     * 根据id来获取参与人
     *
     * @param id
     * @return
     */
    @Override
    public String getParticipants(int id) {
        return sysProjectTableMapper.getParticipants(id);
    }

    @Override
    public int insertMarketProject(SysProjectTable sysProjectTable, ProjectMarketStageTable projectMarketStageTable, ProjectMarkTypeInfoTable projectMarkTypeInfoTable) {
        return 0;
    }

    @Override
    public int updateEstablishStatu(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updateEstablishStatu(sysProjectTable);
    }

    /**
     * 获得项目的审批记录
     *
     * @param projectId
     * @return
     */
    @Override
    public List<ResultApprovalRecord> selectByCurrentId(int projectId) {
        return sysProjectTableMapper.selectByCurrentId(projectId);
    }

    /**
     * 获得项目负责人
     *
     * @param projectId
     * @return
     */
    @Override
    public int selectChargePeopleById(int projectId) {
        return sysProjectTableMapper.selectChargePeopleById(projectId);
    }

    /**
     * 根据id查询项目2
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public List<SysProjectTable> getProjectTableByProjectId(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.getProjectTableByProjectId(sysProjectTable);
    }

    /**
     * 查询我的项目
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public SysProjectTable selectMyProject(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.selectMyProject(sysProjectTable);
    }

    @Override
    public List<SysProjectTable> selectByChargePeople(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.selectByChargePeople(sysProjectTable);
    }

    /**
     * 获得项目完成标识
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public String selectFinishByProjectId(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.selectFinishByProjectId(sysProjectTable);
    }

    /**
     * 更新用户得关注项目的状态
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public int updateUserAttention(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updateUserAttention(sysProjectTable);
    }

    /**
     * 更新技术负责人写的部分
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public int updateMarkProject(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updateMarkProject(sysProjectTable);
    }

    /**
     * 获取所有存在里程碑的项目的id以及名称
     *
     * @return
     */
    @Override
    public List<SysProjectTable> selectExsitPlantProjet() {
        return sysProjectTableMapper.selectExsitPlantProjet();
    }

    /**
     * 查询技术负责人id
     *
     * @param currentId
     * @return
     */
    @Override
    public Map<String, Object> selectTechniquePeopleId(Integer currentId) {
        return sysProjectTableMapper.selectTechniquePeopleId(currentId);
    }

    /**
     * 获取登录用户关注项目的状态
     *
     * @param u
     * @return
     */
    @Override
    public Integer selectUserAention(UserProjectAttention u) {
        return sysProjectTableMapper.selectUserAention(u);
    }

    /**
     * 修改商务负责人部分内容
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public int updateChargePeoplePart(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updateChargePeoplePart(sysProjectTable);
    }

    /**
     * 查询项目标题
     *
     * @param id
     * @return
     */
    @Override
    public String selectProjectNameById(long id) {
        return sysProjectTableMapper.selectProjectNameById(id);
    }

    /**
     * 更新市场立项成功的项目信息
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public Integer updateMarkProjectForDate(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updateMarkProjectForDate(sysProjectTable);
    }

    /**
     * 根据技术负责人id来获取对应的项目
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public List<SysProjectTable> selectByTechniquePeople(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.selectByTechniquePeople(sysProjectTable);
    }

    /**
     * 科研项目的修改
     *
     * @param sysProjectTable
     * @return
     */
    @Override
    public int updateByProjectId(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.updateByProjectId(sysProjectTable);
    }

    /**
     * 根据开始时间和和周期获得结束时间
     *
     * @param startTime
     * @param period
     * @return
     * @throws ParseException
     */
    @Override
    public String getFinallyDate(String startTime, String period) throws ParseException {
        startTime = DateUtils.convert2String(Long.parseLong(startTime), "");
        // 返回的是一个string类型的字符串
        return CalculateHours.getFinallyDate(startTime, period);
    }

    /**
     * 根据pid查询项目信息
     *
     * @param pid
     * @return
     */
    @Override
    public SysProjectTable selectProjectById(Long pid) {
        return sysProjectTableMapper.selectProjectById(pid);
    }

    @Override
    public List<SysProjectTable> selectProjectInfoByTitle(String title) {
        return sysProjectTableMapper.selectProjectInfoByTitle(title);
    }

    @Override
    public List<SysProjectTable> insertFailProject(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.insertFailProject(sysProjectTable);
    }

    @Override
    public List<SysProjectTable> selectInfosByProjectId(SysProjectTable sysProjectTable) {
        return sysProjectTableMapper.selectInfosByProjectId(sysProjectTable);
    }

    @Override
    public AjaxResult updateApprocalMarkProject(SysProjectTable sysProjectTable, ProjectMarketStageTable
            projectMarketStageTable) {
        Integer projectId1 = 0;
        int a = 0;
        // 传入项目的id
        Integer projectId = sysProjectTable.getpId();
        if (projectId == null) {
            projectId = 0;
        }
        sysProjectTable.setpId(projectId);
        List<SysProjectTable> list = sysProjectTableMapper.selectAllInfo(sysProjectTable);
        // 添加信息到项目表
        sysProjectTable.setCreateBy(ShiroUtils.getUserId() + "");
        sysProjectTable.setCreateTime(new Date());
        sysProjectTable.setProjectType(1);
        String title = sysProjectTable.getTitle();
        if (ShiroUtils.isNotNull(title)) {
            SysProjectTableTechnologyVo s = new SysProjectTableTechnologyVo();
            s.setTitle(title);
            List<SysProjectTableTechnologyVo> technologyVosList = sysProjectTableMapper.selectTitle(s);
            if (ShiroUtils.isNotEmpty(list) && list.size() > 0) {
                for (SysProjectTableTechnologyVo vos : technologyVosList) {
                    String stataus = vos.getEstablishStatus();
                    if (ShiroUtils.turnInteger(stataus) != 3 && ShiroUtils.turnInteger(stataus) != 2 && ShiroUtils.turnInteger(stataus) != 4) {
                        return AjaxResult.error("项目名和[" + title + "]重复了,请更改项目名");
                    }
                }
            }
        }
        a = sysProjectTableMapper.insertMarkProject(sysProjectTable);
        projectId1 = sysProjectTable.getpId();
        if (projectId1 == null) {
            projectId1 = 0;
        }
        // 还差一个商务参与人
        inserts(projectId1, sysProjectTable.getChargepeopleId());
        inserts(projectId1, sysProjectTable.getTechniquePeople());
        // 添加审批流
        sysProjectTable.setFlowId(3);
        sysProjectTable.setCreateBy(ShiroUtils.getUserId() + "");
        iAuditFlowCurrentService.insertAuditFlowCurrentProject(sysProjectTable);

        // 添加技术负责人部分内容
        for (int i = 0; i < list.size(); i++) {
            SysProjectTable sysProjectTable2 = new SysProjectTable();
            sysProjectTable2.setpId(projectId1);
            sysProjectTable2.setNeedBackground(list.get(i).getNeedBackground());
            sysProjectTable2.setNeedApplication(list.get(i).getNeedApplication());
            sysProjectTable2.setNeedUnderstand(list.get(i).getNeedUnderstand());
            sysProjectTable2.setNeedFeasible(list.get(i).getNeedFeasible());
            sysProjectTable2.setNeedEndTime(list.get(i).getNeedEndTime());
            // 描述
            sysProjectTable2.setRiskDescribeTechnique(list.get(i).getRiskDescribeTechnique());
            // 解决办法
            sysProjectTable2.setRiskSolutionsTechnique(list.get(i).getRiskDescribeTechnique());
            sysProjectTable2.setUpdateBy(ShiroUtils.getUserId() + "");
            sysProjectTable2.setUpdateTime(new Date());
            a = sysProjectTableMapper.updateMarkProject(sysProjectTable2);
        }
        if (a == 1) {
            SysCompetitor sysCompetitor = new SysCompetitor();
            // 获得json字符串
            String competeJson = sysProjectTable.getCompeteJson();
            if (competeJson != null && competeJson.length() > 0) {
                JSONArray jsonArray = JSONArray.parseArray(competeJson);
                if (!jsonArray.isEmpty() && jsonArray != null) {
                    for (int i = 0; i < jsonArray.size(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        sysCompetitor.setProjectId(projectId1);
                        sysCompetitor.setCompeteName(jsonObject.getString("competeName"));
                        sysCompetitor.setCompeteDescribe(jsonObject.getString("competeDescribe"));
                        a = sysCompetitorService.insertCompetitor(sysCompetitor);
                    }
                }
            }
            // 将阶段信息插入阶段表中，如果阶段信息已经在表中，则不予以删除，如果不在，则以空白进行显示
            int[] stageArray = projectMarketStageTable.getStageArray();
            int[] b = new int[stageArray.length];
            if (stageArray.length > 0) {
                for (int i = 0; i < stageArray.length; i++) {
                    if (stageArray[i] == 1) {
                        projectMarketStageTable.setProjectId(projectId1);
                        // 当数组值等于1的时候，则插入对应的阶段，1-6个阶段
                        projectMarketStageTable.setStageType(i + 1);
                        projectMarkStageService.insertStage(projectMarketStageTable);
                        // 得到的是即将要插入数据库的阶段
                        b[i] = i + 1;
                    }
                }
                // 根据阶段id得到之前存入数据库的内容
                if (b.length > 0) {
                    for (int i = 0; i < b.length; i++) {
                        Integer stage = b[i];
                        if (stage != null) {
                            ProjectMarketStageTable p1 = new ProjectMarketStageTable();
                            p1.setProjectId(projectId);
                            p1.setStageType(b[i]);
                            List<ProjectMarketStageTable> listStage = projectMarkStageService.selectStageByProjectId(p1);
                            if (listStage.size() > 0) {
                                for (ProjectMarketStageTable y : listStage) {
                                    projectMarketStageTable.setProjectId(projectId1);
                                    projectMarketStageTable.setStageType(b[i]);
                                    projectMarketStageTable.setChargePeople(y.getChargePeople());
                                    projectMarketStageTable.setParticipantsPeople(y.getParticipantsPeople());
                                    projectMarketStageTable.setWorkPeriod(y.getWorkPeriod());
                                    projectMarketStageTable.setDeviceList(y.getDeviceList());
                                    projectMarketStageTable.setToolList(y.getToolList());
                                    projectMarketStageTable.setIsCar(y.getIsCar());
                                    projectMarketStageTable.setPlace(y.getPlace());
                                    projectMarketStageTable.setPartner(y.getPartner());
                                    projectMarketStageTable.setRemindState(0);
                                    projectMarketStageTable.setWarnState(0);
                                    projectMarketStageTable.setUpdateBy(ShiroUtils.getUserId() + "");
                                    projectMarketStageTable.setUpdateTime(new Date());
                                    int c = projectMarkStageService.updateStage(projectMarketStageTable);
                                }
                            }
                        }
                    }
                }
            }
        }
        // 当编辑再次提交添加成功之后，删除原来存在的信息
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        auditFlowCurrent.setApplyId(projectId);
        auditFlowCurrent.setAuditId(3);
        List<AuditFlowCurrent> list1 = iAuditFlowCurrentService.selectCurrentId(auditFlowCurrent);
        for (AuditFlowCurrent f : list1) {
            Integer currentId = f.getCurrentId();
            if (currentId != null) {
                deleteAllInfosBypId(currentId, projectId);
            }

        }
        sysProjectTable.setpId(projectId1);
        if (a == 1) {
            return AjaxResult.success("操作成功", sysProjectTable);
        }
        return AjaxResult.error();
    }

    /**
     * 删除所有信息的接口
     *
     * @param currentId
     * @param projectId
     * @return
     */
    public int deleteAllInfosBypId(Integer currentId, Integer projectId) {
        int a = 0;
        AuditFlowCurrent auditFlowCurrent = iAuditFlowCurrentService.selectAuditFlowCurrentById(currentId);
        Integer flowId = auditFlowCurrent.getAuditId();
        if (flowId == null) {
            flowId = 0;
        }
        Integer applyId = auditFlowCurrent.getApplyId();
        if (applyId == null) {
            applyId = 0;
        }
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        ParticipantsTable participantsTable = new ParticipantsTable();
        Integer[] type = {3, 5, 6, 8, 11};
        //删除消息
        msgEvtInfo.setEventId(currentId);
        msgEvtInfo.setTypeList(Arrays.asList(type));
        a = msgEvtInfoService.deleteForMsg(msgEvtInfo);
        msgEvtInfo.setEventId(projectId);
        msgEvtInfo.setTypeList(Arrays.asList(type));
        a = msgEvtInfoService.deleteForMsg(msgEvtInfo);
        Integer[] flowIds = {3, 7, 10, 11};
        boolean yes = Arrays.asList(flowIds).contains(flowId);
        if (yes) {
            // 删除参与人
            participantsTable.setProjectId(applyId);
            participantsService.deletePeople(participantsTable);
            // 删除阶段表
            ProjectMarketStageTable projectMarketStageTable = new ProjectMarketStageTable();
            projectMarketStageTable.setProjectId(projectId);
            projectMarkStageService.deleteStage(projectMarketStageTable);
            // 删除资源表
            projectMarkTypeMapper.deleteType(projectId);
            // 删除中间表
            ProjectTaskTable projectTaskTable = new ProjectTaskTable();
            projectTaskTable.setEventId(projectId);
            projectTaskTable.setTypeId(1);
            projectTaskTableService.deleteInfos(projectTaskTable);
            // 删除项目
            a = sysProjectTableMapper.deleteProject(applyId);
            // 删除竞争对手
            sysCompetitorMappere.deleteCompete(projectId);
        }
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
        return a;
    }

    /**
     * 保留原来项目的操作
     *
     * @param sysProjectTable
     * @param projectMarketStageTable
     * @return
     */
    @Override
    public AjaxResult updateMarkProject(SysProjectTable sysProjectTable, ProjectMarketStageTable projectMarketStageTable) {
        Integer projectType = sysProjectTable.getProjectType();
        if (projectType == null) {
            return AjaxResult.error("项目类型参数错误");
        }
        if (projectType.equals(ManagerConstant.PROJECT_RESEARCH)) {
            // 科研项目编辑再次提交
            return this.insertResearch(sysProjectTable);
        } else if (projectType.equals(ManagerConstant.PROJECT_MARKET)) {
            // 市场项目编辑再次提交
            Integer projectId1 = 0;
            int a = 0;
            // 传入项目的id
            Integer projectId = sysProjectTable.getpId();
            if (projectId == null) {
                return AjaxResult.error("参数错误");
            }
            SysProjectTableTechnologyVo technologyVo = sysProjectTableMapper.selectTechnology(projectId);
            String establishStatus = technologyVo.getEstablishStatus();
            String title = sysProjectTable.getTitle();
            if (ShiroUtils.isNotNull(title)) {
                SysProjectTableTechnologyVo s = new SysProjectTableTechnologyVo();
                s.setTitle(title);
                List<SysProjectTableTechnologyVo> vos = sysProjectTableMapper.selectTitle(s);
                if (ShiroUtils.isNotEmpty(vos) && vos.size() > 0) {
                    for (SysProjectTableTechnologyVo vs : vos) {
                        String stataus = vs.getEstablishStatus();
                        if (ShiroUtils.turnInteger(stataus) != 3 && ShiroUtils.turnInteger(stataus) != 2 && ShiroUtils.turnInteger(stataus) != 4) {
                            return AjaxResult.error("项目名和[" + title + "]重复了,请更改项目名");
                        }
                    }
                }
            }
            sysProjectTable.setCreateBy(ShiroUtils.getUserId() + "");
            sysProjectTable.setCreateTime(new Date());
            sysProjectTable.setProjectType(1);
            a = sysProjectTableMapper.insertMarkProject(sysProjectTable);
            projectId1 = sysProjectTable.getpId();
            if (projectId1 == null) {
                projectId1 = 0;
            }
            // 更改项目名称
            if (technologyVo != null) {
                updateFlag(technologyVo, sysProjectTable);
            }
            // 还差一个商务参与人
            inserts(projectId1, sysProjectTable.getChargepeopleId());
            inserts(projectId1, sysProjectTable.getTechniquePeople());
            SysProjectTable projectTable = new SysProjectTable();
            // 添加审批流
            projectTable.setpId(projectId1);
            projectTable.setFirstUserId(sysProjectTable.getFirstUserId());
            projectTable.setSecondUserId(sysProjectTable.getSecondUserId());
            projectTable.setThirdUserId(sysProjectTable.getThirdUserId());
            projectTable.setFourthUserId(sysProjectTable.getFourthUserId());
            projectTable.setFlowId(3);
            projectTable.setCreateBy(ShiroUtils.getUserId() + "");
            iAuditFlowCurrentService.insertAuditFlowCurrentProject(projectTable);
            // 添加技术负责人部分内容
            SysProjectTable sysProjectTable2 = new SysProjectTable();
            sysProjectTable2.setpId(projectId1);
            sysProjectTable2.setNeedBackground(technologyVo.getNeedBackground());
            sysProjectTable2.setNeedApplication(technologyVo.getNeedApplication());
            sysProjectTable2.setNeedUnderstand(technologyVo.getNeedUnderstand());
            sysProjectTable2.setNeedFeasible(technologyVo.getNeedFeasible());
            sysProjectTable2.setNeedEndTime(technologyVo.getNeedEndTime());
            sysProjectTable2.setRiskDescribeTechnique(technologyVo.getRiskDescribeTechnique());
            sysProjectTable2.setRiskSolutionsTechnique(technologyVo.getRiskDescribeTechnique());
            sysProjectTable2.setUpdateBy(ShiroUtils.getUserId() + "");
            sysProjectTable2.setUpdateTime(new Date());
            a = sysProjectTableMapper.updateMarkProject(sysProjectTable2);
            if (a == 1) {
                SysCompetitor sysCompetitor = new SysCompetitor();
                // 获得json字符串
                String competeJson = sysProjectTable.getCompeteJson();
                if (competeJson.length() > 0) {
                    JSONArray jsonArray = JSONArray.parseArray(competeJson);
                    if (!jsonArray.isEmpty() && jsonArray != null) {
                        for (int i = 0; i < jsonArray.size(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            sysCompetitor.setProjectId(projectId1);
                            sysCompetitor.setCompeteName(jsonObject.getString("competeName"));
                            sysCompetitor.setCompeteDescribe(jsonObject.getString("competeDescribe"));
                            a = sysCompetitorService.insertCompetitor(sysCompetitor);
                        }
                    }
                }
                // 将阶段信息插入阶段表中，如果阶段信息已经在表中，则不予以删除，如果不在，则以空白进行显示
                int[] stageArray = projectMarketStageTable.getStageArray();
                int[] b = new int[stageArray.length];
                if (stageArray.length > 0) {
                    for (int i = 0; i < stageArray.length; i++) {
                        if (stageArray[i] == 1) {
                            projectMarketStageTable.setProjectId(projectId1);
                            // 当数组值等于1的时候，则插入对应的阶段，1-6个阶段
                            projectMarketStageTable.setStageType(i + 1);
                            projectMarkStageService.insertStage(projectMarketStageTable);
                            // 得到的是即将要插入数据库的阶段
                            b[i] = i + 1;
                        }
                    }
                    // 根据阶段id得到之前存入数据库的内容
                    if (b.length > 0) {
                        for (int i = 0; i < b.length; i++) {
                            Integer stage = b[i];
                            if (stage != null) {
                                ProjectMarketStageTable p1 = new ProjectMarketStageTable();
                                p1.setProjectId(projectId);
                                p1.setStageType(b[i]);
                                List<ProjectMarketStageTable> listStage = projectMarkStageService.selectStageByProjectId(p1);
                                if (listStage.size() > 0) {
                                    for (ProjectMarketStageTable y : listStage) {
                                        projectMarketStageTable.setProjectId(projectId1);
                                        projectMarketStageTable.setStageType(b[i]);
                                        projectMarketStageTable.setChargePeople(y.getChargePeople());
                                        projectMarketStageTable.setParticipantsPeople(y.getParticipantsPeople());
                                        projectMarketStageTable.setWorkPeriod(y.getWorkPeriod());
                                        projectMarketStageTable.setDeviceList(y.getDeviceList());
                                        projectMarketStageTable.setToolList(y.getToolList());
                                        projectMarketStageTable.setIsCar(y.getIsCar());
                                        projectMarketStageTable.setPlace(y.getPlace());
                                        projectMarketStageTable.setPartner(y.getPartner());
                                        projectMarketStageTable.setRemindState(0);
                                        projectMarketStageTable.setWarnState(0);
                                        projectMarketStageTable.setUpdateBy(ShiroUtils.getUserId() + "");
                                        projectMarketStageTable.setUpdateTime(new Date());
                                        projectMarkStageService.updateStage(projectMarketStageTable);
                                    }
                                }
                            }
                        }
                    }
                }
            }
            sysProjectTable.setpId(projectId1);
            if (a == 1) {
                // 项目撤回且再次提交，将原项目删除
                if (establishStatus.equals(ManagerConstant.PROJECT_ESTABLISH_QUASH)) {
                    // 删除项目及与项目关联的信息
                    sysProjectTableMapper.deleteRelationProjectMsg(projectId);
                    // 删除原审批信息
                    Map<String, Object> map = auditFlowCurrentMapper.selectBuinessId(ManagerConstant.APPROVAL_CREATE_MARKET_PROJECT, projectId);
                    if (map != null && map.size() > 0) {
                        Integer currentId = (Integer) map.get("currentId");
                        if (currentId != null) {
                            auditFlowCurrentMapper.deleteMultipleTable(currentId);
                        }
                    }
                }
                return AjaxResult.success("操作成功", sysProjectTable);
            }
            return AjaxResult.error();
        }
        return AjaxResult.error("操作失败");
    }

    /**
     * 科研项目编辑再次提交
     *
     * @param sysProjectTable 项目对象
     * @return
     */
    public AjaxResult insertResearch(SysProjectTable sysProjectTable) {
        // 原项目id
        Integer pId = sysProjectTable.getpId();
        sysProjectTable.setProjectType(0);
        sysProjectTable.setCreateBy(ShiroUtils.getUserId().toString());
        if (sysProjectTable.getProducetypeid() == null) {
            return AjaxResult.error("产品线参数错误!");
        }
        sysProjectTable.setProducetypeid(sysProjectTable.getProducetypeid());
        // 判断项目名是否重复
        String title = sysProjectTable.getTitle();
        if (ShiroUtils.isNotNull(title)) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            List<Map<String, Object>> resultList = sysProjectTableMapper.selectTitleBypId(map);
            if (ShiroUtils.isNotEmpty(resultList)) {
                for (Map<String, Object> map2 : resultList) {
                    String establishStatus = (String) map2.get("establishStatus");
                    if (Integer.parseInt(establishStatus) != 3 && Integer.parseInt(establishStatus) != 2 && Integer.parseInt(establishStatus) != 4) {
                        return AjaxResult.error("项目名和[" + title + "]重复了,请更改项目名");
                    }
                }
            }
        }
        // 添加项目信息
        int a = insertProject(sysProjectTable);
        int id = sysProjectTable.getpId();
        String participants = sysProjectTable.getParticipantsId();
        if (ShiroUtils.isNotNull(participants)) {
            int chargeId = sysProjectTable.getChargepeopleId();
            insertParticipants(id, chargeId, participants);
        }
        if (a > 0) {
            SysProjectTableTechnologyVo technologyVo = sysProjectTableMapper.selectTechnology(pId);
            String establishStatus = technologyVo.getEstablishStatus();
            // 项目撤回且再次提交，将原项目删除
            if (establishStatus.equals(ManagerConstant.PROJECT_ESTABLISH_QUASH)) {
                // 删除项目及与项目关联的信息
                sysProjectTableMapper.deleteRelationProjectMsg(pId);
                // 删除原审批信息
                Map<String, Object> map = auditFlowCurrentMapper.selectBuinessId(ManagerConstant.APPROVAL_CREATE_RESEARCH_PROJECT, pId);
                if (map != null && map.size() > 0) {
                    Integer currentId = (Integer) map.get("currentId");
                    if (currentId != null) {
                        auditFlowCurrentMapper.deleteMultipleTable(currentId);
                    }
                }
            }
            return AjaxResult.success("添加成功");
        }
        return AjaxResult.error("失败");
    }

    /**
     * 更改原项目的标识信息
     *
     * @param technologyVo
     * @param sysProjectTable
     */
    public void updateFlag(SysProjectTableTechnologyVo technologyVo, SysProjectTable sysProjectTable) {
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        Integer pId = technologyVo.getpId();
        String title = technologyVo.getTitle();
        sysProjectTable.setpId(pId);
        // 3标识改项目再次提交
        sysProjectTable.setEstablishStatus("3");
        sysProjectTable.setTitle(title);
        sysProjectTable.setUpdateTime(new Date());
        sysProjectTableMapper.updateByProjectId(sysProjectTable);
        // 将原项目的拒批提醒置为已读
        auditFlowCurrent.setApplyId(pId);
        auditFlowCurrent.setAuditId(3);
        List<AuditFlowCurrent> flowList = iAuditFlowCurrentService.selectCurrentId(auditFlowCurrent);
        if (ShiroUtils.isNotEmpty(flowList)) {
            for (AuditFlowCurrent a : flowList) {
                msgEvtInfo.setEventId(a.getCurrentId());
                msgEvtInfo.setType(8);
                msgEvtInfo.setUpdateTime(new Date());
                msgEvtInfo.setReadMark(1);
                msgEvtInfoService.updateMessageInfo2(msgEvtInfo);
            }
        }

    }

    /**
     * 项目列表的分页功能
     *
     * @param sysProjectTable
     * @param list
     * @return
     */
    @Override
    public List<SysProjectTable> paging(SysProjectTable sysProjectTable, List<SysProjectTable> list) {
        list = list.stream()
                .skip(sysProjectTable.getTotal() * (sysProjectTable.getPageNumber() - 1))
                .limit(sysProjectTable.getTotal())
                .collect(Collectors.toList());
        return list;
    }

    @Override
    public SysProjectTable selectInfoByTitle(String title) {
        return sysProjectTableMapper.selectInfoByTitle(title);
    }

    @Override
    public void deleteByIdEstablishProject(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        // 1.项目表
        sysProjectTableMapper.deleteByIdEstablishProject(idList);
        // 2.项目设备表
        sysProjectTableMapper.deleteByIdProjectInProjectMarketStageTable(idList);
        // 3.项目试用表
        sysProjectTableMapper.deleteByIdProjectInProjectMarkTypeInfoTable(idList);
        // 4.竞争对手
        sysProjectTableMapper.deleteByIdProjectInSysCompetitor(idList);


    }

    @Override
    public void deleteByIdEstablishProjectLinkInfo(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        //1.根据id删除与项目表有外键关联UserProjectAttention表的数据
        sysProjectTableMapper.deleteByIdEstablishProjectLinkUserProjectAttentionInfo(idList);
        //2.根据id删除与项目表有外键关联ProjectPlanTable表的数据
        sysProjectTableMapper.deleteByIdEstablishProjectLinkProjectPlanTableInfo(idList);
        //3.根据id删除与项目表有外键关联ParticipantsTable表的数据
        sysProjectTableMapper.deleteByIdEstablishProjectLinkParticipantsTableInfo(idList);
        //4.根据id删除与项目表有外键关联ProjectTaskTable表的数据
        sysProjectTableMapper.deleteByIdEstablishProjectLinkProjectTaskTableInfo(idList);
    }

    @Override
    public void deleteByIdEstablishProjectLinkAuditInfo(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        // 1.根据项目id获取audit_flow_current表审核的currentId
        List currentIdList = sysProjectTableMapper.selectAuditCurrentIdByApplyId(idList);
        if (currentIdList != null && currentIdList.size() > 0) {
            // 2.根据currentIds删除audit_flow_node_role表的数据
            sysProjectTableMapper.deleteByIdEstablishProjectLinkAuditFlowNodeRoleInfo(currentIdList);
            // 3.根据currentIds删除audit_flow_oper_record表的数据
            sysProjectTableMapper.deleteByIdEstablishProjectLinkAuditFlowOperRecordInfo(currentIdList);
            // 4.根据currentIds删除audit_flow_current表的数据
            sysProjectTableMapper.deleteByIdEstablishProjectLinkAuditFlowCurrentInfo(currentIdList);
        }
    }

    @Override
    public SysProjectTable selectInfosBypId(int pId) {
        return sysProjectTableMapper.selectInfosBypId(pId);
    }

    @Override
    public void deleteByIdProjectInMsg(String ids) {
        List<String> idList = Arrays.asList(ids.split(","));
        // 1.根据id查询audit_flow_current表获取current_id
        List<String> currentids = sysProjectTableMapper.selectAuditFlowCurrentByPid(idList);
        // 2.将以获取到的current_id，删除msg_evt_info表的消息，event_id=current_id
        if (currentids != null && currentids.size() > 0) {
            sysProjectTableMapper.deleteByIdProjectInMsg(currentids);
        }
        // 3.删除以项目为even_id为项目id的消息
        sysProjectTableMapper.deleteByIdProjectInMsgByType(idList);
    }

    @Override
    public List<SysProjectTable> selectAllCacheProject(SysProjectTable sysProjectTable) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!MyUtils.isEmpty(sysProjectTable.getStartTimes())) {
            sysProjectTable.setStartTimes(format.format(new Date(new Long(sysProjectTable.getStartTimes()))));
        }
        if (!MyUtils.isEmpty(sysProjectTable.getEndTimes())) {
            sysProjectTable.setEndTimes(format.format(new Date(new Long(sysProjectTable.getEndTimes()))));
        }
        String peopleString = sysProjectTable.getChargePeopleName();
        if (StringUtils.isNotEmpty(peopleString)) {
            String[] split = peopleString.split(",");
            sysProjectTable.setPeopleList(Arrays.asList(split));
        }
        // 如果关键词查询不为空,如果关键词是姓名，则不查标题
        String queryInfo = sysProjectTable.getQueryInfo();
        if (StringUtils.isNotEmpty(queryInfo)) {
            sysProjectTable.setTitle(queryInfo);
        }
        return sysProjectTableMapper.selectAllCacheProject();
    }

    /**
     * 添加用户
     *
     * @param projectId
     * @param userId
     */
    @Override
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

    /**
     * 判断项目是否可以进行项目中止审批
     * 1：可以进行，0：不可以
     *
     * @param projectId
     * @return
     */
    @Override
    public Integer selectState(Integer projectId) {
        Integer agree = 0;
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        TaskTableVo taskTableVo = new TaskTableVo();
        taskTableVo.setProjectId(projectId);
        List<TaskTableVo> list = taskTableService.selectTaskVos(taskTableVo);
        // 根据任务id去查询所有的currentId和对应的state
        // 循环遍历，判断currentState是否等于2,4,6
        // 如果存在着一个或者一个以上状态满足条件，则返回0，否则返回1
        List<AuditFlowCurrent> allList = new ArrayList<>();
        if (ShiroUtils.isNotEmpty(list)) {
            for (TaskTableVo t : list) {
                auditFlowCurrent.setApplyId(t.gettId().intValue());
                // 发起任务审批中的
                auditFlowCurrent.setAuditId(1);
                List<AuditFlowCurrent> list2 = iAuditFlowCurrentService.selectCurrentId(auditFlowCurrent);
                if (!list2.isEmpty() && list2 != null) {
                    allList.addAll(list2);
                }
                // 任务中止审批中的
                auditFlowCurrent.setAuditId(8);
                List<AuditFlowCurrent> list3 = iAuditFlowCurrentService.selectCurrentId(auditFlowCurrent);
                if (!list3.isEmpty() && list3 != null) {
                    allList.addAll(list3);
                }
                // 任务完成审批中的
                auditFlowCurrent.setAuditId(9);
                List<AuditFlowCurrent> list4 = iAuditFlowCurrentService.selectCurrentId(auditFlowCurrent);
                if (!list4.isEmpty() && list4 != null) {
                    allList.addAll(list4);
                }
                // 任务变更审批中的
                auditFlowCurrent.setAuditId(12);
                List<AuditFlowCurrent> list5 = iAuditFlowCurrentService.selectCurrentId(auditFlowCurrent);
                if (!list5.isEmpty() && list5 != null) {
                    allList.addAll(list5);
                }
                if (ShiroUtils.isNotEmpty(allList)) {
                    for (AuditFlowCurrent a : allList) {
                        Integer currentState = a.getCurrentState();
                        // 2审批中，4审批通过，6转推中，
                        if (currentState != null && (currentState == 2 || currentState == 4 || currentState == 6)) {
                            agree = 0;
                        } else {
                            agree = 1;
                        }
                    }
                } else {
                    // 当任务都为临时任务的时候，不存在者有无审批的情况
                    agree = 1;
                }
            }
        } else {
            // 当项目没有任务的时候，允许进行项目中止或者完成
            agree = 1;
        }
        return agree;
    }

    /**
     * 判断项目是否可以进行完成
     * 0：可以进行项目完成
     * 1：可以进行项目完成
     *
     * @param projectId
     * @return
     */
    @Override
    public Integer selectFinishState(Integer projectId) {
        Integer agree = 1;
        TaskTableVo taskTableVo = new TaskTableVo();
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        taskTableVo.setProjectId(projectId);
        List<TaskTableVo> list = taskTableService.selectTaskVos(taskTableVo);
        for (TaskTableVo t : list) {
            Integer taskFinishflag = Integer.parseInt(t.getTaskFinishflag());
            // 审批未成功的情况
            if (taskFinishflag == -1 || (taskFinishflag != 1 && taskFinishflag != 2)) {
                if (taskFinishflag == -1) {
                    // 判断是否处于审批流中
                    auditFlowCurrent.setApplyId(t.gettId().intValue());
                    // 发起任务审批中的
                    auditFlowCurrent.setAuditId(1);
                    List<AuditFlowCurrent> list2 = iAuditFlowCurrentService.selectCurrentId(auditFlowCurrent);
                    if (ShiroUtils.isNotEmpty(list2)) {
                        for (AuditFlowCurrent a : list2) {
                            Integer currentState = a.getCurrentState();
                            // 2审批中，4审批通过，6转推中，
                            if (currentState != null && (currentState == 2 || currentState == 6)) {
                                agree = 0;
                                return agree;
                            }
                        }
                    }
                }
                if (taskFinishflag != -1) {
                    // 表示项目处与审批中或者未完成
                    if (taskFinishflag != 1 && taskFinishflag != 2) {
                        agree = 0;
                        return agree;
                    }
                }
            }
        }
        return agree;
    }

    @Override
    public SysProjectTableTechnologyVo selectTechnology(Integer pId) {
        return sysProjectTableMapper.selectTechnology(pId);
    }

    @Override
    public Integer selectProjectVosCount(SysProjectTableVo sysProjectTableVo) {
        return sysProjectTableMapper.selectProjectVosCount(sysProjectTableVo);
    }

    @Override
    public Integer selectProjectCountByTitle(String title) {
        return sysProjectTableMapper.selectProjectCountByTitle(title);
    }

    @Override
    public List<SysProjectTableVo> selectManager(SysProjectTableVo sysProjectTableVo) {
        return sysProjectTableMapper.selectManager(sysProjectTableVo);
    }

    @Override
    public List<SysProjectTableTechnologyVo> selectTitle(SysProjectTableTechnologyVo sysProjectTableTechnologyVo) {
        return sysProjectTableMapper.selectTitle(sysProjectTableTechnologyVo);
    }

    @Override
    public AjaxResult isRepeat(String title) {
        if (ShiroUtils.isNotNull(title)) {
            SysProjectTableTechnologyVo s = new SysProjectTableTechnologyVo();
            s.setTitle(title);
            List<SysProjectTableTechnologyVo> list = sysProjectTableMapper.selectTitle(s);
            if (ShiroUtils.isNotEmpty(list)) {
                for (int i = 0; i < list.size(); i++) {
                    String status = list.get(i).getEstablishStatus();
                    if (ShiroUtils.turnInteger(status) != 3) {
                        return AjaxResult.error("该项目名和[" + title + "]的项目名冲突了，请更新修改");
                    }
                }
            }
        } else {
            return AjaxResult.error("项目名不能为空");
        }
        return AjaxResult.success("操作成功");
    }

    @Override
    public List<SysProjectTable> selectProjectInPlanList(TaskProject taskProject) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!MyUtils.isEmpty(taskProject.getStartTimes())) {
            taskProject.setStartTimes(format.format(new Date(new Long(taskProject.getStartTimes()))));
        }
        if (!MyUtils.isEmpty(taskProject.getEndTimes())) {
            taskProject.setEndTimes(format.format(new Date(new Long(taskProject.getEndTimes()))));
        }
        // 查询立项成功的
        if (taskProject.getEstablishStatus() == null) {
            taskProject.setEstablishStatus("1");
        }
        // 0:查询只含里程碑的
        if (taskProject.getIsPlan() == null) {
            taskProject.setIsPlan(1);
        }
        return sysProjectTableMapper.selectProjectInPlanList(taskProject);
    }

    @Override
    public AjaxResult addProjectFile(String fileIds, Integer pid) {
        try {
            String[] fileId = fileIds.split(",");
            for (int i = 0; i < fileId.length; i++) {
                SysFileInfo sysFileInfo = new SysFileInfo();
                sysFileInfo.setFileId(Long.parseLong(fileId[i]));
                sysFileInfo.setFileType(1);
                sysFileInfo.setWorkId(pid);
                sysFileInfoService.updateFileInfo(sysFileInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return AjaxResult.success();
    }

    @Override
    public int quashProjectByIds(String ids, Integer currentId) {
        int a = 0;
        Map<String, Object> flowCurrentMap = auditFlowCurrentMapper.selectFlowCurrentMap(currentId);
        Integer auditId = (Integer) flowCurrentMap.get("auditId");
        Map<String, Object> map = new HashMap<>();
        Integer[] auditIds = {3, 7};
        Integer[] auditIds2 = {10, 11, 13, 14};
        if (Arrays.asList(auditIds).contains(auditId)) {
            a = sysProjectTableMapper.quashProjectByIds(Convert.toStrArray(ids));
            map.put("currentId", currentId);
            map.put("currentState", 7);
            a = auditFlowCurrentMapper.updateCurrentState(map);
        } else if (Arrays.asList(auditIds2).contains(auditId)) {
            // 将项目状态改为未完成
            SysProjectTable sysProjectTable = new SysProjectTable();
            sysProjectTable.setProjectFinishFlag("0");
            sysProjectTable.setpId(Integer.parseInt(ids));
            a = sysProjectTableMapper.updateEstablishStatu(sysProjectTable);
            // 删除审批信息
            auditFlowCurrentMapper.deleteMultipleTable(currentId);
        }
        // 删除审批信息
        map.put("type", 3);
        map.put("eventId", currentId);
        msgEvtInfoMapper.deleteMsgEvt(map);
        return a;
    }

    @Override
    public int deleteProjectByIds(String ids) {
        return sysProjectTableMapper.deleteProjectByIds(Convert.toStrArray(ids));
    }

    @Override
    public List<SysProjectTable> querylist(String keyword, Long chargePeopleid) {
        return sysProjectTableMapper.querylist(keyword, chargePeopleid);
    }

    @Override
    public int updatetimeById(int id, String checktime, String finishMemo) {
        return sysProjectTableMapper.updatetimeById(id, checktime, finishMemo);
    }

    @Override
    public int updatestopcauseById(Integer pId, String stoptype, String stopcause) {
        return sysProjectTableMapper.updatestopcauseById(pId, stoptype, stopcause);
    }

    @Override
    public AjaxResult insertResearchProject(SysProjectTable sysProjectTable) {
        sysProjectTable.setProjectType(0);
        sysProjectTable.setCreateBy(ShiroUtils.getUserId().toString());
        if (sysProjectTable.getProducetypeid() == null) {
            return AjaxResult.error("产品线参数不能为空");
        }
        sysProjectTable.setProducetypeid(sysProjectTable.getProducetypeid());

       /* if (ShiroUtils.isNotNull(firstUserId)) {
            String[] arr = firstUserId.split(",");
            Integer produceId = produceTypeMapper.selectTypeByUserId(arr[0]);
            if (produceId == null) {
                return AjaxResult.error("产品经理选择错误，请重新选择");
            }
            sysProjectTable.setProducetypeid(produceId);
        }*/
        String title = sysProjectTable.getTitle();
        if (ShiroUtils.isNotNull(title)) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            List<Map<String, Object>> resultList = sysProjectTableMapper.selectTitleBypId(map);
            if (ShiroUtils.isNotEmpty(resultList)) {
                for (Map<String, Object> map2 : resultList) {
                    String establishStatus = (String) map2.get("establishStatus");
                    if (Integer.parseInt(establishStatus) != 3 && Integer.parseInt(establishStatus) != 2 && Integer.parseInt(establishStatus) != 4) {
                        return AjaxResult.error("项目名和[" + title + "]重复了,请更改项目名");
                    }
                }
            }
        }
        int a = insertProject(sysProjectTable);
        int id = sysProjectTable.getpId();
        String participants = sysProjectTable.getParticipantsId();
        if (ShiroUtils.isNotNull(participants)) {
            int chargeId = sysProjectTable.getChargepeopleId();
            insertParticipants(id, chargeId, participants);
        }
        if (a > 0) {
            return AjaxResult.success("添加成功");
        }
        return AjaxResult.error("失败");
    }

    /**
     * 添加项目的参与人
     *
     * @param pId
     * @param chargeId
     * @param participants
     * @return
     */
    @Transactional
    public int insertParticipants(int pId, int chargeId, String participants) {
        int d = 0;
        String[] arr;
        arr = participants.split(",");
        for (int i = 0; i < arr.length; i++) {
            ParticipantsTable vo = new ParticipantsTable();
            int id = Integer.parseInt(arr[i]);
            if (id != chargeId) {
                vo.setUserId(id);
                vo.setProjectId(pId);
                d = participantsService.insertparticipants(vo);
                inserts(pId, id);
            }
        }
        inserts(pId, chargeId);
        return d;
    }

    @Override
    public AjaxResult insertMarkProject(SysProjectTable sysProjectTable, ProjectMarketStageTable projectMarketStageTable) {
        int a = 0;
        int projectId = 0;
        sysProjectTable.setProjectType(1);
        sysProjectTable.setCreateTime(new Date());
        sysProjectTable.setCreateBy(ShiroUtils.getUserId() + "");
        String thirdUserId = sysProjectTable.getThirdUserId();
        if (sysProjectTable.getProducetypeid() == null) {
            return AjaxResult.error("产品线参数不能为空");
        }
        sysProjectTable.setProducetypeid(sysProjectTable.getProducetypeid());
        /*if (ShiroUtils.isNotNull(thirdUserId)) {
            String[] arr = thirdUserId.split(",");
            Integer produceId = produceTypeMapper.selectTypeByUserId(arr[0]);
            if (produceId == null) {
                return AjaxResult.error("产品经理选择错误，请重新选择");
            }
            sysProjectTable.setProducetypeid(produceId);
        }*/
        String title = sysProjectTable.getTitle();
        if (ShiroUtils.isNotNull(title)) {
            Map<String, Object> map = new HashMap<>();
            map.put("title", title);
            List<Map<String, Object>> resultList = sysProjectTableMapper.selectTitleBypId(map);
            if (ShiroUtils.isNotEmpty(resultList)) {
                for (Map<String, Object> map2 : resultList) {
                    String establishStatus = (String) map2.get("establishStatus");
                    if (Integer.parseInt(establishStatus) != 3 && Integer.parseInt(establishStatus) != 2 && Integer.parseInt(establishStatus) != 4) {
                        return AjaxResult.error("项目名和[" + title + "]重复了,请更改项目名");
                    }
                }
            }
        }
        a = insertMarkProject(sysProjectTable);
        if (a == 1) {
            projectId = sysProjectTable.getpId();
            inserts(projectId, sysProjectTable.getChargepeopleId());
            inserts(projectId, sysProjectTable.getTechniquePeople());
            SysCompetitor sysCompetitor = new SysCompetitor();
            String competeJson = sysProjectTable.getCompeteJson();
            JSONArray jsonArray = JSONArray.parseArray(competeJson);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                sysCompetitor.setProjectId(projectId);
                sysCompetitor.setCompeteName(jsonObject.getString("competeName"));
                sysCompetitor.setCompeteDescribe(jsonObject.getString("competeDescribe"));
                a = sysCompetitorService.insertCompetitor(sysCompetitor);
            }
            int[] stageArray = projectMarketStageTable.getStageArray();
            if (stageArray.length > 0) {
                for (int i = 0; i < stageArray.length; i++) {
                    if (stageArray[i] == 1) {
                        projectMarketStageTable.setProjectId(projectId);
                        projectMarketStageTable.setStageType(i + 1);
                        projectMarkStageService.insertStage(projectMarketStageTable);
                    }
                }
            }
        }
        sysProjectTable.setpId(projectId);
        if (a > 0) {
            return AjaxResult.success("操作成功", sysProjectTable);
        }
        return AjaxResult.error("添加失败");
    }

    @Override
    public PageEntity queryMyProject(SysProjectTable sysProjectTable) {
        Long userId = ShiroUtils.getUserId();
        List<Integer> idList = sysProjectTableMapper.selectIdList(userId);
        SysProjectTable[] sysProjectTables = new SysProjectTable[idList.size()];
        for (int i = 0; i < idList.size(); i++) {
            sysProjectTable.setpId(idList.get(i));
            if (StringUtils.isNotEmpty(sysProjectTable.getKeyword())) {
                sysProjectTable.setTitle(sysProjectTable.getKeyword());
            }
            if (StringUtils.isNotEmpty(sysProjectTable.getChargePeopleName())) {
                Long uid = sysUserService.getUid(sysProjectTable.getChargePeopleName());
                sysProjectTable.setChargepeopleId(uid.intValue());
            }
            if (StringUtils.isNotEmpty(sysProjectTable.getTechniquePeopleName())) {
                Long id = sysUserService.getUid(sysProjectTable.getTechniquePeopleName());
                sysProjectTable.setTechniquePeople(id.intValue());
            }
            sysProjectTables[i] = sysProjectTableMapper.selectMyProject(sysProjectTable);
        }
        List<ProduceType> porducelist = produceTypeService.selectproducetype();
        List<Map<String, Object>> list2 = new ArrayList<>();
        for (int i = 0; i < porducelist.size(); i++) {
            String name = porducelist.get(i).getProducename();
            int id = porducelist.get(i).getId();
            List<SysProjectTable> projectlist = new ArrayList<>();
            Map<String, Object> map = new HashMap<>();
            for (SysProjectTable projectTable : sysProjectTables) {
                if (projectTable != null) {
                    int producetypeid = projectTable.getProducetypeid();
                    if (producetypeid == id) {
                        projectlist.add(projectTable);
                    }
                }
            }
            map.put("name", name);
            map.put("list", projectlist);
            list2.add(map);
        }
        for (Map<String, Object> map : list2) {
            Integer state = 0;
            List<SysProjectTable> myList = (List<SysProjectTable>) map.get("list");
            for (int i = 0; i < myList.size(); i++) {
                state = sysProjectTableMapper.selectFollowUserId(ShiroUtils.getUserId(), myList.get(i).getpId());
                myList.get(i).setUserAttention(state);
            }
            myList.forEach(p -> {
                List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
                if (fileInfoList != null) {
                    p.setFileInfoList(fileInfoList);
                }
            });
        }
        return new PageEntity(sysProjectTable.getTotal(), idList.size(), sysProjectTable.getPageNumber(), list2);
    }


    @Override
    public int addMarkInfo(ProjectMarkTypeInfoTable projectMarkTypeInfoTable) {
        int a = 0;
        List<ProjectMarkTypeInfoTable> p1 = projectMarkTypeInfoTable.getSoftwareDevList();
        if (!p1.isEmpty()) {
            for (int i = 0; i < p1.size(); i++) {
                projectMarkTypeInfoTable.setHardwareName(p1.get(i).getHardwareName());
                projectMarkTypeInfoTable.setIsType(1);
                projectMarkTypeInfoTable.setIsShiyong(p1.get(i).getIsShiyong());
                projectMarkTypeInfoTable.setShiyongCount(p1.get(i).getShiyongCount());
                projectMarkTypeInfoTable.setShiyongAllCount(p1.get(i).getShiyongAllCount());
                a = projectMarkTypeService.insertMarkType(projectMarkTypeInfoTable);
            }
        }
        List<ProjectMarkTypeInfoTable> p2 = projectMarkTypeInfoTable.getSpHardwareDevList();
        if (!p2.isEmpty()) {
            for (int i = 0; i < p2.size(); i++) {
                projectMarkTypeInfoTable.setHardwareName(p2.get(i).getHardwareName());
                projectMarkTypeInfoTable.setIsType(2);
                projectMarkTypeInfoTable.setIsShiyong(p2.get(i).getIsShiyong());
                projectMarkTypeInfoTable.setShiyongAllCount(p2.get(i).getShiyongAllCount());
                projectMarkTypeInfoTable.setShiyongCount(p2.get(i).getShiyongCount());
                a = projectMarkTypeService.insertMarkType(projectMarkTypeInfoTable);
            }
        }
        List<ProjectMarkTypeInfoTable> p3 = projectMarkTypeInfoTable.getIntergratedDevList();
        if (!p3.isEmpty()) {
            for (int i = 0; i < p3.size(); i++) {
                projectMarkTypeInfoTable.setHardwareName(p3.get(i).getHardwareName());
                projectMarkTypeInfoTable.setIsType(3);
                projectMarkTypeInfoTable.setIsShiyong(p3.get(i).getIsShiyong());
                projectMarkTypeInfoTable.setShiyongAllCount(p3.get(i).getShiyongAllCount());
                projectMarkTypeInfoTable.setShiyongCount(p3.get(i).getShiyongCount());
                a = projectMarkTypeService.insertMarkType(projectMarkTypeInfoTable);
            }
        }
        return a;
    }

    @Override
    public int updateMarkInfo(ProjectMarkTypeInfoTable projectMarkTypeInfoTable) {
        int a = 0;
        projectMarkTypeInfoTable.setProjectId(201);
        List<ProjectMarkTypeInfoTable> p1 = projectMarkTypeInfoTable.getSoftwareDevList();
        if (!p1.isEmpty()) {
            for (int i = 0; i < p1.size(); i++) {
                projectMarkTypeInfoTable.setHardwareName(p1.get(i).getHardwareName());
                projectMarkTypeInfoTable.setIsType(1);
                projectMarkTypeInfoTable.setIsShiyong(p1.get(i).getIsShiyong());
                projectMarkTypeInfoTable.setShiyongAllCount(p1.get(i).getShiyongAllCount());
                projectMarkTypeInfoTable.setShiyongCount(p1.get(i).getShiyongCount());
                a = projectMarkTypeService.updateMarkInfo(projectMarkTypeInfoTable);
            }
        }
        List<ProjectMarkTypeInfoTable> p2 = projectMarkTypeInfoTable.getSpHardwareDevList();
        if (!p2.isEmpty()) {
            for (int i = 0; i < p2.size(); i++) {
                projectMarkTypeInfoTable.setHardwareName(p2.get(i).getHardwareName());
                projectMarkTypeInfoTable.setIsType(2);
                projectMarkTypeInfoTable.setIsShiyong(p2.get(i).getIsShiyong());
                projectMarkTypeInfoTable.setShiyongAllCount(p2.get(i).getShiyongAllCount());
                projectMarkTypeInfoTable.setShiyongCount(p2.get(i).getShiyongCount());
                a = projectMarkTypeService.updateMarkInfo(projectMarkTypeInfoTable);
            }
        }
        List<ProjectMarkTypeInfoTable> p3 = projectMarkTypeInfoTable.getIntergratedDevList();
        if (!p3.isEmpty()) {
            for (int i = 0; i < p3.size(); i++) {
                projectMarkTypeInfoTable.setHardwareName(p3.get(i).getHardwareName());
                projectMarkTypeInfoTable.setIsType(3);
                projectMarkTypeInfoTable.setIsShiyong(p3.get(i).getIsShiyong());
                projectMarkTypeInfoTable.setShiyongAllCount(p3.get(i).getShiyongAllCount());
                projectMarkTypeInfoTable.setShiyongCount(p3.get(i).getShiyongCount());
                a = projectMarkTypeService.updateMarkInfo(projectMarkTypeInfoTable);
            }
        }
        return a;
    }

    @Override
    public PageEntity attentionlist(SysProjectTable sysProjectTable) {
        int pageIndex = sysProjectTable.getPageNumber();
        int pageSize = sysProjectTable.getTotal();
        pageSize = pageSize <= 0 ? 10 : pageSize;
        PageHelper.startPage(pageIndex, pageSize);
        long userId = ShiroUtils.getUserId();
        sysProjectTable.setAttention(userId + "");
        List<SysProjectTable> projectList = sysProjectTableMapper.selectAllProject(sysProjectTable);
        PageInfo<SysProjectTable> pageInfo = new PageInfo<>(projectList);
        List<SysProjectTable> resultList = pageInfo.getList();
        if (!ShiroUtils.isEmpty(resultList)) {
            Integer state = 0;
            for (SysProjectTable project : resultList) {
                state = sysProjectTableMapper.selectFollowUserId(ShiroUtils.getUserId(), project.getpId());
                project.setUserAttention(state);
            }
            resultList.forEach(p -> {
                List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
                if (fileInfoList != null) {
                    p.setFileInfoList(fileInfoList);
                }
            });
        }
        return new PageEntity(pageInfo);
    }

    @Override
    public PageEntity establishList(SysProjectTable sysProjectTable) {
        int pageIndex = sysProjectTable.getPageNumber();
        int pageSize = sysProjectTable.getTotal();
        pageSize = pageSize <= 0 ? 10 : pageSize;
        PageHelper.startPage(pageIndex, pageSize);
        List<SysProjectTable> projectList = this.selectEstablish(sysProjectTable);
        PageInfo<SysProjectTable> pageInfo = new PageInfo<>(projectList);
        List<SysProjectTable> resultList = pageInfo.getList();
        if (!ShiroUtils.isEmpty(resultList)) {
            for (SysProjectTable projectTable : resultList) {
                Integer pId = projectTable.getpId();
                Integer auditId = 0;
                Integer projectType = projectTable.getProjectType();
                if (projectType != null && projectType == 0) {
                    auditId = 7;
                } else if (projectType != null && projectType == 1) {
                    auditId = 3;
                }
                // 获取审批流Id
                Map<String, Object> map = auditFlowCurrentMapper.selectBuinessId(auditId, pId);
                if (map == null || map.size() < 1) {
                    continue;
                }
                Integer currentId = (Integer) map.get("currentId");
                List<Integer> stateList = auditFlowNodeRoleMapper.selectApplyState(currentId);
                projectTable.setDeletable(true);
                if (ShiroUtils.isNotEmpty(stateList) && stateList.contains(0)) {
                    // 不显示删除按钮
                    projectTable.setDeletable(false);
                }
                projectTable.setProjectCurrentId(currentId);
                // 获取当前审批人
                Integer currentNodeId = (Integer) map.get("currentNodeId");
                List<String> userIdList = auditFlowNodeRoleMapper.selectNodeRoleUserId(currentId, currentNodeId, 1);
                if (!ShiroUtils.isEmpty(userIdList)) {
                    projectTable.setUserIds(String.join(",", userIdList));
                }
            }
            resultList.forEach(p -> {
                List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
                if (fileInfoList != null) {
                    p.setFileInfoList(fileInfoList);
                }
            });
        }
        // 添加审批记录显示(只显示3条）
        resultList.forEach(item -> {
            Integer projectCurrentId = item.getProjectCurrentId();
            if (projectCurrentId != null) {
                ResultApprovalRecord resultApprovalRecord = new ResultApprovalRecord();
                resultApprovalRecord.setCurrentId(projectCurrentId);
                List<ResultApprovalRecord> resultApprovalRecords = resultApprovalRecordService.selectApprovalRecord(resultApprovalRecord);
                resultApprovalRecords = resultApprovalRecords.stream()
                        .filter(result -> result.getApprovalMemo() != null)
                        .filter(result -> !StringUtils.isEmpty(result.getApprovalMemo()))
                        .sorted(Comparator.comparing(ResultApprovalRecord::getId).reversed())
                        .limit(3)
                        .collect(Collectors.toList());
                if (resultApprovalRecords != null) {
                    item.setResultApprovalRecordList(resultApprovalRecords);
                }
            }
        });
        return new PageEntity(pageInfo);
    }
}

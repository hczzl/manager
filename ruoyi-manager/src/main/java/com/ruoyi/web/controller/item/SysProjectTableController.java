package com.ruoyi.web.controller.item;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.exception.BizException;
import com.ruoyi.web.domain.vo.SysProjectTableVo;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.mapper.audit.AuditFlowNodeRoleMapper;
import com.ruoyi.web.mapper.item.SysProjectTableMapper;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.item.*;
import com.ruoyi.web.util.ConstantUtil;
import com.ruoyi.web.util.constant.ManagerConstant;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 项目模块
 */
@RestController
@Api(value = "SysProjectTableController", tags = "项目模块相关接口")
public class SysProjectTableController extends BaseController {
    @Autowired
    private SysProjectTableService sysProjectTableService;
    @Autowired
    private SysProjectTableMapper sysProjectTableMapper;
    @Autowired
    private ParticipantsService participantsService;
    @Autowired
    private ProjectMarkStageService projectMarkStageService;
    @Autowired
    private ProjectMarkTypeService projectMarkTypeService;
    @Autowired
    private AuditFlowCurrentService auditFlowCurrentService;
    @Autowired
    private AuditFlowCurrentService iAuditFlowCurrentService;
    @Autowired
    private AuditFlowNodeRoleMapper auditFlowNodeRoleMapper;
    @Autowired
    private SysFileInfoService sysFileInfoService;
    @Autowired
    private SysCompetitorService sysCompetitorService;
    @Autowired
    private ProjectMemoService projectMemoService;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;
    @Autowired
    private SysProjectTableService projectTableService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private ProduceTypeService produceTypeService;

    @ApiOperation("添加市场项目-商务负责人填写的部分")
    @PostMapping("addMarkProject")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "新增项目", businessType = BusinessType.INSERT)
    public AjaxResult saveAddMarkProject(SysProjectTable sysProjectTable, ProjectMarketStageTable projectMarketStageTable) {
        return sysProjectTableService.insertMarkProject(sysProjectTable, projectMarketStageTable);
    }

    @ApiOperation("添加资源信息的接口")
    @PostMapping("/addMarkInfo")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addMarkInfo(@RequestBody ProjectMarkTypeInfoTable projectMarkTypeInfoTable) {
        return toAjax(sysProjectTableService.addMarkInfo(projectMarkTypeInfoTable));
    }


    @ApiOperation("添加竞争对手")
    @PostMapping("/insertJson")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult inertCompeteJson(@RequestBody SysCompetitor sysCompetitor) {
        int a = 0;
        List<SysCompetitor> compete = sysCompetitor.getCompete();
        Integer projectId = sysCompetitor.getProjectId();
        if (projectId == null) {
            projectId = 0;
        }
        if (!compete.isEmpty() && compete != null) {
            for (int i = 0; i < compete.size(); i++) {
                sysCompetitor.setProjectId(projectId);
                sysCompetitor.setCompeteName(compete.get(i).getCompeteName());
                sysCompetitor.setCompeteDescribe(compete.get(i).getCompeteDescribe());
                a = sysCompetitorService.insertCompetitor(sysCompetitor);
            }
        }
        return toAjax(a);
    }

    @ApiOperation("添加竞争对手,可以有多个")
    @PostMapping("/insertCompetitor")
    @ResponseBody
    public AjaxResult insertCompetitor(@RequestBody SysCompetitor sysCompetitor) {
        Integer projectId = 13;
        if (projectId == null) {
            projectId = 0;
        }
        List<SysCompetitor> list = sysCompetitor.getCompete();
        int a = 0;
        if (!list.isEmpty() & list != null) {
            for (int i = 0; i < list.size(); i++) {
                sysCompetitor.setProjectId(projectId);
                sysCompetitor.setCompeteName(list.get(i).getCompeteName());
                sysCompetitor.setCompeteDescribe(list.get(i).getCompeteDescribe());
                a = sysCompetitorService.insertCompetitor(sysCompetitor);
            }
        }
        return toAjax(a);
    }

    @ApiOperation("更新资源表信息")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateMarkInfo(@RequestBody ProjectMarkTypeInfoTable projectMarkTypeInfoTable) {
        return toAjax(sysProjectTableService.updateMarkInfo(projectMarkTypeInfoTable));
    }


    @ApiOperation("技术负责人部分添加")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/updateMarkProject")
    public AjaxResult updateMarkProject(@RequestBody SysProjectTable sysProjectTable) {
        int userId = ShiroUtils.getUserId().intValue();
        int a = 0;
        sysProjectTable.setUpdateBy(userId + "");
        sysProjectTable.setUpdateTime(new Date());
        a = sysProjectTableService.updateMarkProject(sysProjectTable);
        Integer pId = sysProjectTable.getpId();
        ProjectMarketStageTable projectMarketStageTable = new ProjectMarketStageTable();
        projectMarketStageTable.setProjectId(pId);
        List<ProjectMarketStageTable> list = projectMarkStageService.selectStageByProjectId(projectMarketStageTable);
        if (list.size() < 1) {
            MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
            msgEvtInfo.setType(5);
            msgEvtInfo.setEventId(pId);
            msgEvtInfo.setAuditId(3);
            msgEvtInfo.setReadMark(1);
            msgEvtInfo.setUpdateTime(new Date());
            msgEvtInfoService.updateMessageInfo(msgEvtInfo);
        }
        return toAjax(a);
    }

    @ApiOperation("新增科研项目")
    @PostMapping("/addProjectTable")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addSysProjectTable(SysProjectTable sysProjectTable) {
        return sysProjectTableService.insertResearchProject(sysProjectTable);
    }

    @PostMapping("/updateProjectName")
    @ResponseBody
    @ApiOperation("/科研、市场拒批再次提交或者撤回再次提交")
    @Log(title = "科研、市场拒批再次提交或者撤回再次提交", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateMarkProject(SysProjectTable projectTable, ProjectMarketStageTable stageTable) {
        return sysProjectTableService.updateMarkProject(projectTable, stageTable);
    }

    @ApiOperation("修改商务负责人部分内容")
    @ResponseBody
    @PostMapping("/updateChargePeoplePart")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateChargePeoplePart(@RequestBody SysProjectTable sysProjectTable) {
        sysProjectTable.setUpdateBy(ShiroUtils.getUserId() + "");
        sysProjectTable.setUpdateTime(new Date());
        int a = sysProjectTableService.updateChargePeoplePart(sysProjectTable);
        return toAjax(a);
    }


    @ApiOperation("测试项目完成、项目中止")
    @PostMapping("/finishAndEndProject")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult endProjectByFinish(SysProjectTable sysProjectTable) {
        int lastAgree = 0;
        sysProjectTable.setCreateBy(ShiroUtils.getUserId() + "");
        sysProjectTable.setUpdateTime(new Date());
        Integer flowId = sysProjectTable.getFlowId();
        sysProjectTable.setpId(sysProjectTable.getpId());
        String finishFlag = sysProjectTableService.selectFinishByProjectId(sysProjectTable);
        String memo = sysProjectTable.getFinishMemo();
        // 市场项目中止
        if (flowId.equals(ManagerConstant.APPROVAL_MARKET_PROJECT_BREAK)) {
            if (finishFlag != ManagerConstant.PROJECT_NOT_STOP) {
                Integer agree = sysProjectTableService.selectState(sysProjectTable.getpId());
                if (agree == 1) {
                    lastAgree = agree;
                    sysProjectTable.setFlowId(flowId);
                    int b = iAuditFlowCurrentService.updateAuditFlowCurrentProject(sysProjectTable);
                    sysProjectTable.setProjectFinishFlag("-2");
                    finishFlag = "-2";
                    sysProjectTableService.updateProject(sysProjectTable);
                }
            }
            finishFlag = sysProjectTableService.selectFinishByProjectId(sysProjectTable);
            if (memo != null) {
                ProjectMemo projectMemo = new ProjectMemo();
                projectMemo.setProjectId(sysProjectTable.getpId());
                projectMemo.setType(1);
                projectMemo.setMemo(memo);
                projectMemo.setCreateTime(new Date());
                projectMemoService.insertMemo(projectMemo);
            }
        }
        // 项目完成审批
        if (flowId.equals(ManagerConstant.APPROVAL_MARKET_PROJECT_FINISH)) {
            if (finishFlag != ManagerConstant.PROJECT_NOT_FINISH) {
                Integer agree = sysProjectTableService.selectFinishState(sysProjectTable.getpId());
                if (agree == 1) {
                    lastAgree = agree;
                    sysProjectTable.setFlowId(flowId);
                    int b = iAuditFlowCurrentService.updateAuditFlowCurrentProject(sysProjectTable);
                    sysProjectTable.setProjectFinishFlag("-1");
                    finishFlag = "-1";
                    sysProjectTableService.updateProject(sysProjectTable);
                }
            }
            finishFlag = sysProjectTableService.selectFinishByProjectId(sysProjectTable);
            if (memo != null) {
                ProjectMemo projectMemo = new ProjectMemo();
                projectMemo.setProjectId(sysProjectTable.getpId());
                projectMemo.setType(0);
                projectMemo.setMemo(memo);
                projectMemo.setCreateTime(new Date());
                projectMemoService.insertMemo(projectMemo);
            }
        }
        sysProjectTable.setProjectFinishFlag(finishFlag);
        sysProjectTable.setAgree(lastAgree);
        if (lastAgree == 0) {
            return AjaxResult.notOperation();
        }
        return AjaxResult.success("操作成功", sysProjectTable);
    }


    /**
     * 根据项目id，查询对应的项目信息，包括审批记录
     *
     * @param sysProjectTable
     * @return
     */
    @ApiOperation("根据项目id，查询对应的项目信息，包括审批记录")
    @PostMapping("/getAllMarkProjectInfo")
    @ResponseBody
    public List<SysProjectTable> getAllProjectInfo(SysProjectTable sysProjectTable) {
        List<SysProjectTable> list1 = sysProjectTableService.selectAllInfo(sysProjectTable);
        list1.forEach(p -> {
            List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
            if (fileInfoList != null) {
                p.setFileInfoList(fileInfoList);
            }
        });
        return list1;
    }

    /**
     * 市场项目的删除
     *
     * @param id
     * @return
     */
    @ApiOperation("市场项目的删除")
    @GetMapping("/deleteProject/{id}")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    @Log(title = "删除市场项目", businessType = BusinessType.DELETE)
    public int deleteProject(@PathVariable("id") int id) {
        projectMarkTypeService.deleteType(id);
        ProjectMarketStageTable projectMarketStageTable = new ProjectMarketStageTable();
        projectMarketStageTable.setProjectId(id);
        projectMarkStageService.deleteStage(projectMarketStageTable);
        int a = sysProjectTableService.deleteProject(id);
        return a;
    }

    @ApiOperation("市场项目添加----测试")
    @PostMapping("addMark")
    @ResponseBody
    public int save(SysProjectTable sysProjectTable) {
        return sysProjectTableService.insertMarkProject(sysProjectTable);
    }

    @ApiOperation("根据项目类型查询项目--项目列表的")
    @GetMapping("/getProjectByType/{projectType}")
    @ResponseBody
    public List<SysProjectTable> getProjectByType(@PathVariable("projectType") String projectType) {
        List<SysProjectTable> sysProjectTables = sysProjectTableService.selectProjectByType(projectType);
        sysProjectTables.forEach(p -> {
            List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
            if (fileInfoList != null) {
                p.setFileInfoList(fileInfoList);
            }
        });
        return sysProjectTableService.selectProjectByType(projectType);
    }

    @ApiOperation("立项列表的关键字、负责人id、立项状态联合查询")
    @PostMapping("/findEstablishContext")
    @ResponseBody
    public List<SysProjectTable> getEstablishContext(SysProjectTable sysProjectTable) {
        List<SysProjectTable> sysProjectTables = sysProjectTableService.selectAllestablish(sysProjectTable);
        sysProjectTables.forEach(p -> {
            List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
            if (fileInfoList != null) {
                p.setFileInfoList(fileInfoList);
            }
        });
        return sysProjectTables;
    }

    @ApiOperation("根据项目名称关键字模糊查询立项列表")
    @GetMapping("getProjectByTitle/{title}")
    @ResponseBody
    public List<SysProjectTable> getProjectByTitle(@PathVariable("title") String title) {
        List<SysProjectTable> sysProjectTables = sysProjectTableService.selectProjectByTitle(title);
        sysProjectTables.forEach(p -> {
            List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
            if (fileInfoList != null) {
                p.setFileInfoList(fileInfoList);
            }
        });
        return sysProjectTables;
    }

    @ApiOperation("根据立项状态查出立项列表")
    @GetMapping("/getProjectByStatus/{status}")
    @ResponseBody
    public List<SysProjectTable> selectProjectByStatus(@PathVariable("status") String status) {
        List<SysProjectTable> sysProjectTables = sysProjectTableService.selectProjectByStatus(status);
        sysProjectTables.forEach(p -> {
            List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
            if (sysProjectTables != null) {
                p.setFileInfoList(fileInfoList);
            }
        });
        return sysProjectTables;
    }

    @ApiOperation("查询我的项目，立项成功的项目")
    @PostMapping("/myProject")
    @ResponseBody
    public PageEntity queryMyProject(SysProjectTable vo) {
        return sysProjectTableService.queryMyProject(vo);
    }


    @ApiOperation("我关注的项目列表")
    @PostMapping("/projectAttentionlist")
    @ResponseBody
    public PageEntity attentionlist(SysProjectTable sysProjectTable) {
        PageEntity pageEntity = null;
        try {
            if (sysProjectTable.getPageNumber() == null || sysProjectTable.getTotal() == null) {
                throw new BizException("分页参数错误");
            }
            pageEntity = sysProjectTableService.attentionlist(sysProjectTable);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pageEntity;
    }

    @ApiOperation("项目列表")
    @GetMapping("/listProject")
    @ResponseBody
    public TableDataInfo projectlist(SysProjectTable sysProjectTable) {
        startPage();
        List<ProduceType> porducelist = produceTypeService.selectproducetype();
        List<List<SysProjectTable>> lists = new ArrayList<>();
        List<Map<String, List<SysProjectTable>>> projectlists = new ArrayList<>();
        for (int i = 0; i < porducelist.size(); i++) {
            Integer type = porducelist.get(i).getId();
            sysProjectTable.setProducetypeid(type);
            List<SysProjectTable> list = sysProjectTableService.selectProjectList(sysProjectTable);
            lists.add(list);
            Map<String, List<SysProjectTable>> map = new HashMap<>();
            map.put(porducelist.get(i).getProducename(), list);
            projectlists.add(map);
        }
        for (int j = 0; j < lists.size(); j++) {
            List<SysProjectTable> list = lists.get(j);
            list.forEach(p -> {
                List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
                if (fileInfoList != null) {
                    p.setFileInfoList(fileInfoList);
                }
            });
        }
        return getDataTable(projectlists);
    }

    @ApiOperation("全部项目的查询-新接口")
    @PostMapping("/projectList")
    @ResponseBody
    public PageEntity list(SysProjectTableVo sysProjectTableVo) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        if (!MyUtils.isEmpty(sysProjectTableVo.getStartTimes())) {
            sysProjectTableVo.setStartTimes(format.format(new Date(new Long(sysProjectTableVo.getStartTimes()))));
        }
        if (!MyUtils.isEmpty(sysProjectTableVo.getEndTimes())) {
            sysProjectTableVo.setEndTimes(format.format(new Date(new Long(sysProjectTableVo.getEndTimes()))));
        }
        String peopleString = sysProjectTableVo.getPeopleString();
        if (StringUtils.isNotEmpty(peopleString)) {
            String[] split = peopleString.split(",");
            sysProjectTableVo.setPeopleList(Arrays.asList(split));
        }
        if (sysProjectTableVo.getTotal() == 0) {
            sysProjectTableVo.setTotal(ConstantUtil.TOTAL);
        }
        if (sysProjectTableVo.getPageNumber() != 0) {
            sysProjectTableVo.setPageNumber((sysProjectTableVo.getPageNumber() - 1) * sysProjectTableVo.getTotal());
        }
        if (StringUtils.isNotEmpty(sysProjectTableVo.getKeyword())) {
            sysProjectTableVo.setTitle(sysProjectTableVo.getKeyword());
        }
        if (StringUtils.isNotEmpty(sysProjectTableVo.getChargePeopleName())) {
            Long uid = sysUserService.getUid(sysProjectTableVo.getChargePeopleName());
            sysProjectTableVo.setChargepeopleId(uid.intValue());
        }
        if (StringUtils.isNotEmpty(sysProjectTableVo.getTechniquePeopleName())) {
            Long id = sysUserService.getUid(sysProjectTableVo.getTechniquePeopleName());
            sysProjectTableVo.setTechniquePeople(id.intValue());
        }
        //获取得到项目列表
        List<SysProjectTableVo> list = sysProjectTableService.selectProjectVos(sysProjectTableVo);
        //获取得到产品线列表
        List<ProduceType> porducelist = produceTypeService.selectproducetype();
        //最终返回结果集
        List<Map<String, Object>> resultList = new ArrayList<>();
        for (int i = 0; i < porducelist.size(); i++) {
            Integer type = porducelist.get(i).getId();
            List<SysProjectTableVo> typeList = new ArrayList<>();
            for (SysProjectTableVo vo : list) {
                Integer produceType = vo.getProducetypeid();
                if (type.equals(produceType)) {
                    typeList.add(vo);
                    continue;
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("name", porducelist.get(i).getProducename());
            map.put("list", typeList);
            resultList.add(map);
        }
        Integer pages = sysProjectTableService.selectProjectVosCount(sysProjectTableVo);
        if (ShiroUtils.isNotEmpty(resultList)) {
            for (Map<String, Object> map : resultList) {
                Integer state = 0;
                List<SysProjectTableVo> voList = (List<SysProjectTableVo>) map.get("list");
                for (SysProjectTableVo vo : voList) {
                    state = sysProjectTableMapper.selectFollowUserId(ShiroUtils.getUserId(), vo.getpId());
                    vo.setUserAttention(state);
                }
            }
        }
        return new PageEntity(sysProjectTableVo.getTotal(), pages, sysProjectTableVo.getPageNumber(), resultList);
    }

    @ApiOperation("更加登录用户和项目id获取项目的关注状态")
    @ResponseBody
    @PostMapping("/getUserAttention")
    public Integer getUserAttention(UserProjectAttention userProjectAttention) {
        userProjectAttention.setUserId(ShiroUtils.getUserId());
        Integer userAention = sysProjectTableService.selectUserAention(userProjectAttention);
        return userAention;
    }


    @ApiOperation("进度修改")
    @PostMapping("/updateRate")
    @ResponseBody
    @Log(title = "进度修改", businessType = BusinessType.UPDATE)
    public int planSave(SysProjectTable sysProjectTable) {
        return sysProjectTableService.updatePlan(sysProjectTable);
    }


    @ApiOperation("获取参与人id")
    @PostMapping("/updateName")
    @ResponseBody
    public int updateParticipantsName(SysProjectTable sysProjectTable) {
        return sysProjectTableService.updateParticipantsName(sysProjectTable);
    }

    /**
     * 待修改/getProject/{pId}
     *
     * @param p_id
     * @return
     */
    @ApiOperation("根据id来查询指定项目的信息")
    @GetMapping("/getProject/{p_id}")
    @ResponseBody
    public SysProjectTable getProjectTable(@PathVariable("p_id") int p_id) {
        SysProjectTable projectTable = sysProjectTableService.selectInfosBypId(p_id);
        List<ParticipantsTable> userList = participantsService.getUserIdByPid(p_id);
        String userIds = "";
        if (ShiroUtils.isNotEmpty(userList)) {
            for (ParticipantsTable p : userList) {
                userIds += p.getUserId() + ",";
            }
        }
        if (userIds.length() > 0) {
            userIds = userIds.substring(0, userIds.length() - 1);
        }
        projectTable.setParticipantsId(userIds);
        List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(projectTable.getpId());
        if (fileInfoList != null) {
            projectTable.setFileInfoList(fileInfoList);
        }
        return projectTable;
    }


    @ApiOperation("获取项目的负责人")
    @PostMapping("/getChargeId")
    @ResponseBody
    public int getChargeIdById(SysProjectTable sysProjectTable) {
        return sysProjectTableService.getChargeIdById(sysProjectTable);
    }

    @ApiOperation("查询立项列表")
    @PostMapping("/establish")
    @ResponseBody
    public PageEntity establishList(SysProjectTable sysProjectTable) {
        return sysProjectTableService.establishList(sysProjectTable);
    }

    @ApiOperation("根据项目id删除立项列表任务(多个,分开)")
    @PostMapping("/deleteByIdEstablishProject")
    @Log(title = "立项列表删除项目", businessType = BusinessType.DELETE)
    public AjaxResult deleteByIdEstablishProject(String ids) {
        if (StringUtils.isEmpty(ids)) {
            return AjaxResult.error("传入参数错误");
        }
        //1.删除与项目表有外键联系的数据
        sysProjectTableService.deleteByIdEstablishProjectLinkInfo(ids);
        //2.删除相关的提醒消息数据
        sysProjectTableService.deleteByIdProjectInMsg(ids);
        //3.删除项目表的数据
        sysProjectTableService.deleteByIdEstablishProject(ids);
        //4.删除项目关联审核表的数据
        sysProjectTableService.deleteByIdEstablishProjectLinkAuditInfo(ids);
        return AjaxResult.success();
    }

    @GetMapping("/getEstablishById/{id}")
    @ApiOperation("根据项目id获取项目立项信息")
    public List<SysProjectTable> selectEstablishById(@PathVariable("id") int id) {
        List<SysProjectTable> sysProjectTables = sysProjectTableService.selectEstablishById(id);
        sysProjectTables.forEach(p -> {
            List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
            if (fileInfoList != null) {
                p.setFileInfoList(fileInfoList);
            }
        });
        return sysProjectTables;
    }

    @ApiOperation("修改项目,终止项目的接口")
    @PostMapping("/modify")
    @ResponseBody
    public int updateFinishFlag(SysProjectTable sysProjectTable) {
        return sysProjectTableService.updateProject(sysProjectTable);
    }

    @ApiOperation("查询已经关闭的项目")
    @PostMapping("/endProject")
    @ResponseBody
    public PageEntity end(SysProjectTable sysProjectTable) {
        if (sysProjectTable.getTotal() == 0) {
            sysProjectTable.setTotal(ConstantUtil.TOTAL);
        }
        List<SysProjectTable> list = sysProjectTableService.endProject(sysProjectTable);
        sysProjectTable.setPages(list.size());
        list = sysProjectTableService.paging(sysProjectTable, list);
        //项目完成、终止说明
        ProjectMemo projectMemo = new ProjectMemo();
        for (SysProjectTable s : list) {
            Integer projectId = s.getpId();
            String flag = s.getProjectFinishFlag();
            //项目完成
            if (Integer.parseInt(flag) == 1) {
                projectMemo.setProjectId(projectId);
                projectMemo.setType(0);
                projectMemo.setState(1);
                List<ProjectMemo> memos = projectMemoService.selectByProjectId(projectMemo);
                String memo = null;
                for (int i = 0; i < memos.size(); i++) {
                    memo = memos.get(i).getMemo();
                }
                s.setMemo(memo);
            }
            //项目中止
            if (Integer.parseInt(flag) == 2) {
                projectMemo.setProjectId(projectId);
                projectMemo.setType(1);
                projectMemo.setState(1);
                List<ProjectMemo> memos = projectMemoService.selectByProjectId(projectMemo);
                String memo = null;
                for (int i = 0; i < memos.size(); i++) {
                    memo = memos.get(i).getMemo();
                }
                s.setMemo(memo);
            }
        }
        list.forEach(p -> {
            List<SysFileInfo> fileInfoList = sysFileInfoService.selectFileInfoByProjectId(p.getpId());
            if (fileInfoList != null) {
                p.setFileInfoList(fileInfoList);
            }
        });
        return new PageEntity(sysProjectTable.getTotal(), sysProjectTable.getPages(), sysProjectTable.getPageNumber(), list);
    }

    /**
     * 待修改/getPlan/{pId}
     *
     * @param p_id
     * @return
     */
    @ApiOperation("获得相应的项目进度")
    @GetMapping("/getPlan/{p_id}")
    @ResponseBody
    public Double getPlan(@PathVariable("p_id") int p_id) {
        Double planRate = sysProjectTableService.getPlanRate(p_id);
        return planRate;
    }


    @ApiOperation("更新市场立项成功的项目信息")
    @ResponseBody
    @PostMapping("/updateMarkProjectForDate")
    public AjaxResult updateMarkProjectForDate(SysProjectTable sysProjectTable) {
        sysProjectTable.setUpdateBy(ShiroUtils.getUserId() + "");
        sysProjectTable.setUpdateTime(new Date());
        int a = sysProjectTableMapper.updateMarkProjectForDate(sysProjectTable);
        return toAjax(a);
    }

    @ApiOperation("科研项目的修改")
    @PostMapping("/updateByProjectId")
    @ResponseBody
    @Log(title = "科研项目的修改", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateByProjectId(SysProjectTable sysProjectTable) {
        sysProjectTable.setUpdateBy(ShiroUtils.getUserId() + "");
        sysProjectTable.setUpdateTime(new Date());
        int a = sysProjectTableMapper.updateByProjectId(sysProjectTable);
        return toAjax(a);
    }


    @ApiOperation("市场立项的编辑再次提交")
    @PostMapping("/updateApprocalMarkProject")
    @ResponseBody
    @Log(title = "市场立项的编辑再次提交", businessType = BusinessType.UPDATE)
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateApprocalMarkProject(SysProjectTable sysProjectTable, ProjectMarketStageTable
            projectMarketStageTable) {
        return sysProjectTableService.updateApprocalMarkProject(sysProjectTable, projectMarketStageTable);
    }

    @PostMapping("/insertFailProject")
    @ResponseBody
    @ApiOperation("拒批的项目添加进指定的表中")
    public List<SysProjectTable> insertFailProject(SysProjectTable sysProjectTable) {
        return (sysProjectTableService.insertFailProject(sysProjectTable));
    }

    /**
     * 添加阶段表
     *
     * @param stageArray
     * @param project
     */
    @Transactional(rollbackFor = Exception.class)
    public void addMarkStage(int[] stageArray, int project) {
        ProjectMarketStageTable projectMarketStageTable = new ProjectMarketStageTable();
        //删除markStage表内容,
        projectMarketStageTable.setProjectId(project);
        projectMarkStageService.deleteStage(projectMarketStageTable);
        //先拿到该项目表中的所有阶段
        projectMarketStageTable.setProjectId(project);
        List<ProjectMarketStageTable> list = projectMarkStageService.selectStageByProjectId(projectMarketStageTable);
        for (int i = 0; i < stageArray.length; i++) {
            if (stageArray[i] == 1) {
                int stage = i + 1;
                for (int j = 0; j < list.size(); j++) {
                    //如果等于原内容，则返回原内容
                    if (list.get(j).getStageType() == stage) {
                        projectMarketStageTable.setStageType(stage);
                        projectMarketStageTable.setChargePeople(list.get(j).getChargePeople());
                        projectMarketStageTable.setParticipantsPeople(list.get(j).getParticipantsPeople());
                        projectMarketStageTable.setWorkPeriod(list.get(j).getWorkPeriod());
                        projectMarketStageTable.setStageTimeNode(list.get(j).getStageTimeNode());
                        projectMarketStageTable.setDeviceList(list.get(j).getDeviceList());
                        projectMarketStageTable.setToolList(list.get(j).getToolList());
                        projectMarketStageTable.setIsCar(list.get(j).getIsCar());
                        projectMarketStageTable.setPlace(list.get(j).getPlace());
                        projectMarketStageTable.setRemindState(list.get(j).getRemindState());
                        projectMarketStageTable.setWarnState(list.get(j).getWarnState());
                        projectMarketStageTable.setPartner(list.get(j).getPartner());
                        projectMarketStageTable.setUpdateBy(ShiroUtils.getUserId() + "");
                        projectMarketStageTable.setUpdateTime(new Date());
                        projectMarkStageService.insertStage(projectMarketStageTable);
                    }
                }
                projectMarketStageTable.setStageType(stage);
                List<ProjectMarketStageTable> list1 = projectMarkStageService.selectStageByProjectId(projectMarketStageTable);
                if (list1.size() < 1) {
                    projectMarketStageTable.setUpdateBy(ShiroUtils.getUserId() + "");
                    projectMarketStageTable.setUpdateTime(new Date());
                    projectMarkStageService.insertStage(projectMarketStageTable);
                }
            }
        }
    }


    @ApiOperation("根据开始时间和和周期获得结束时间")
    @PostMapping("/getFinallyDate")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult getFinallyDate(String startTime, String period) throws ParseException {
        String startTimes = DateUtils.date2TimeStamp(startTime, "yyyy-MM-dd");
        return AjaxResult.success("", sysProjectTableService.getFinallyDate(startTimes, period));
    }

    @ApiOperation(value = "项目文件上传", notes = "把文件上传后点保存，将上传文件的id发送该接口，接口修改文件信息，完成项目文件上传")
    @PostMapping(value = "/saveFile")
    @Log(title = "项目文件上传", businessType = BusinessType.INSERT)
    public AjaxResult saveFile(String fileIds, Integer workId) {
        return projectTableService.addProjectFile(fileIds, workId);
    }

    @ApiOperation("修改周期、开始时间、结束时间、项目的参与人、项目详情")
    @PostMapping("/updateByPid")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateByPid(SysProjectTable sysProjectTable) {
        ParticipantsTable participantsTable = new ParticipantsTable();
        Integer pId = sysProjectTable.getpId();
        if (pId == null) {
            pId = 0;
        }
        sysProjectTable.setUpdateBy(ShiroUtils.getUserId() + "");
        sysProjectTable.setUpdateTime(new Date());
        int a = sysProjectTableService.updateByProjectId(sysProjectTable);
        String peoples = sysProjectTable.getParticipantsId();
        participantsTable.setProjectId(pId);
        participantsService.deletePeople(participantsTable);
        if (ShiroUtils.isNotNull(peoples)) {
            String[] arr = peoples.split(",");
            for (int i = 0; i < arr.length; i++) {
                Integer u = ShiroUtils.turnInteger(arr[i]);
                participantsTable.setUserId(u);
                a = participantsService.insertparticipants(participantsTable);
            }
        }
        return toAjax(a);
    }


    @PostMapping("/selectInfosByProjectId")
    @ResponseBody
    public List<SysProjectTable> selectInfosByProjectId(SysProjectTable sysProjectTable) {
        return sysProjectTableService.selectInfosByProjectId(sysProjectTable);
    }

    @ApiOperation("暂存列表")
    @PostMapping(value = "/selectProjectCacheList")
    public PageEntity selectProjectCacheList(SysProjectTable sysProjectTable) {
        List<SysProjectTable> projectTableList = sysProjectTableService.selectAllCacheProject(sysProjectTable);
        projectTableList = sysProjectTableService.paging(sysProjectTable, projectTableList);
        return new PageEntity(sysProjectTable.getTotal(), projectTableList.size(), sysProjectTable.getPageNumber(), projectTableList);
    }


    @ResponseBody
    @ApiOperation("判新建的项目名是否重复")
    @GetMapping("/judgeName/{title}")
    public AjaxResult isRepeat(@PathVariable("title") String title) {
        AjaxResult ajaxResult = sysProjectTableService.isRepeat(title);
        return ajaxResult;
    }

    @ApiOperation("更新所有项目的结束时间，不排除节假日")
    @PostMapping("/updateEndtime")
    @ResponseBody
    public AjaxResult updateEndtime(SysProjectTable sysProjectTable) throws ParseException {
        int a = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        List<SysProjectTable> list = sysProjectTableService.selectAllInfo(sysProjectTable);
        for (SysProjectTable s : list) {
            sysProjectTable.setpId(s.getpId());
            Integer estimatedBuildperiod = s.getEstimatedBuildperiod();
            Integer period = 0;
            if (estimatedBuildperiod != null) {
                period = estimatedBuildperiod + Integer.parseInt(s.getPeriod()) - 1;
            }
            long startTime = s.getStartTime().getTime();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date(startTime));
            calendar.add(Calendar.DAY_OF_MONTH, +period);
            String endTime = sdf.format(calendar.getTime().getTime());
            try {
                Date f = sdf.parse(endTime);
                sysProjectTable.setEndTime(f);
                sysProjectTable.setUpdateTime(new Date());
                a = sysProjectTableService.updateByProjectId(sysProjectTable);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return toAjax(a);
    }

    @ApiOperation("任务列表项目信息")
    @PostMapping("/taskProjectList")
    public PageEntity taskProjectList(TaskProject taskProject) {
        List<SysProjectTable> projectTableList = sysProjectTableService.selectProjectInPlanList(taskProject);
        int size = projectTableList.size();
        projectTableList = projectTableList.stream()
                .skip(taskProject.getTotal() * (taskProject.getPageNumber() - 1))
                .limit(taskProject.getTotal())
                .collect(Collectors.toList());
        if (taskProject.getPageNumber() > (Math.ceil((double) size / taskProject.getTotal()))) {
            projectTableList = null;
        }
        return new PageEntity<>(taskProject.getTotal(), size, taskProject.getPageNumber(), projectTableList);
    }

    @ApiOperation("撤回立项")
    @Log(title = "撤回立项", businessType = BusinessType.UPDATE)
    @PostMapping("/quash")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "项目id", required = true),
            @ApiImplicitParam(name = "currentId", value = "审批流id", required = true)
    })
    public AjaxResult quash(String ids, Integer currentId) {
        List<Integer> stateList = auditFlowNodeRoleMapper.selectApplyState(currentId);
        if (stateList.contains(0)) {
            return AjaxResult.error("撤销失败,该审批已被办理,正在为您刷新列表");
        }
        return toAjax(sysProjectTableService.quashProjectByIds(ids, currentId));
    }

    @ApiOperation("删除立项")
    @Log(title = "删除立项", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    public AjaxResult remove(String ids) {
        return toAjax(sysProjectTableService.deleteProjectByIds(ids));
    }

    @ApiOperation("条件查询")
    @Log(title = "条件查询", businessType = BusinessType.QUERY)
    @PostMapping("/querylistbycondition")
    @ResponseBody
    public PageEntity querylistbycondition(String keyword, String chargePeopleName, int pagesize, int pagenumber) {
        List<SysProjectTable> projectlist = new ArrayList<>();
        Long uid = null;
        if (chargePeopleName != null && chargePeopleName != "") {
            uid = sysUserService.getUid(chargePeopleName);
        }
        projectlist = sysProjectTableService.querylist(keyword, uid);
        return new PageEntity(pagenumber, projectlist.size(), pagesize, projectlist);
    }

    @ApiOperation("项目完成提交申请")
    @Log(title = "项目完成提交申请", businessType = BusinessType.QUERY)
    @PostMapping("/finishaudit")
    @ResponseBody
    public AjaxResult finishaudit(int pid, String checktime, String finishMemo) {
        SysProjectTable sysProjectTable = sysProjectTableService.selectInfosBypId(pid);
        int result = 0;
        if (sysProjectTable != null) {
            if (sysProjectTable.getProjectFinishFlag().equals(ManagerConstant.PROJECT_FINISH) || sysProjectTable.getProjectFinishFlag().equals(ManagerConstant.PROJECT_NOT_FINISH)) {
                return AjaxResult.warn("该项目已完成/在完成审批中！");
            } else {
                int state = sysProjectTableService.selectFinishState(pid);
                if (state == 0) {
                    return AjaxResult.warn("项目任务处于审批中/任务未完成（或未中止），不能提交项目完成！");
                }
                int type = sysProjectTable.getProjectType();
                if (type == 0) {
                    Integer userId = ShiroUtils.getUserId().intValue();
                    sysProjectTableService.updatetimeById(pid, checktime, finishMemo);
                    Integer chargePeopleid = sysProjectTable.getChargepeopleId();
                    if (chargePeopleid.equals(userId)) {
                        Integer id = sysProjectTable.getProducetypeid();
                        String manager = produceTypeService.getprojectmanagerbyid(id);
                        sysProjectTable.setFirstUserId(manager);
                        sysProjectTable.setFlowId(14);
                        result = auditFlowCurrentService.insertAuditFlowCurrentProject(sysProjectTable);
                        if (result > 0) {
                            sysProjectTable.setProjectFinishFlag("-1");
                            sysProjectTableService.updateProject(sysProjectTable);
                        }
                    } else {
                        return AjaxResult.warn("您没有该权限！");
                    }
                } else {
                    Integer userId = ShiroUtils.getUserId().intValue();
                    Integer techniquePeopleid = sysProjectTable.getTechniquePeople();
                    sysProjectTableService.updatetimeById(pid, checktime, finishMemo);
                    if (techniquePeopleid.equals(userId)) {//进入审批流
                        sysProjectTable.setFlowId(11);
                        sysProjectTable.setFirstUserId(String.valueOf(sysProjectTable.getChargepeopleId()));
                        Integer id = sysProjectTable.getProducetypeid();
                        String manager = produceTypeService.getprojectmanagerbyid(id);
                        sysProjectTable.setSecondUserId(manager);
                        result = auditFlowCurrentService.insertAuditFlowCurrentProject(sysProjectTable);
                        if (result > 0) {
                            sysProjectTable.setProjectFinishFlag("-1");
                            sysProjectTableService.updateProject(sysProjectTable);
                        }
                    } else {
                        return AjaxResult.warn("您没有该权限！");
                    }
                }
            }
        }
        return toAjax(result);
    }

    @ApiOperation("项目中止提交申请")
    @Log(title = "项目中止提交申请", businessType = BusinessType.QUERY)
    @PostMapping("/stopaudit")
    @ResponseBody
    public AjaxResult stopaudit(int pid, String stoptype, String cause) {
        SysProjectTable sysProjectTable = sysProjectTableService.selectInfosBypId(pid);
        int result = 0;
        int type = sysProjectTable.getProjectType();
        if (sysProjectTable != null) {
            if (sysProjectTable.getProjectFinishFlag().equals(ManagerConstant.PROJECT_STOP) || sysProjectTable.getProjectFinishFlag().equals(ManagerConstant.PROJECT_NOT_STOP)) {
                return AjaxResult.warn("该项目已中止/在中止审批中！");
            } else {
                int state = sysProjectTableService.selectFinishState(pid);
                if (state == 0) {
                    return AjaxResult.warn("项目任务处于审批中/任务未完成（或未中止），不能提交项目中止！！");
                }
                if (type == 0) {
                    Integer userId = ShiroUtils.getUserId().intValue();
                    Integer chargePeopleid = sysProjectTable.getChargepeopleId();
                    if (chargePeopleid.equals(userId)) {
                        Integer id = sysProjectTable.getProducetypeid();
                        String manager = produceTypeService.getprojectmanagerbyid(id);
                        sysProjectTable.setFirstUserId(manager);
                        sysProjectTable.setFlowId(13);
                        SysUser user = new SysUser();
                        user.setDeptId(107L);
                        List<SysUser> userslist = sysUserService.selectInfosByWxId(user);
                        String ids = "";
                        for (SysUser u : userslist) {
                            if (ids != "") {
                                ids += ",";
                            }
                            ids += u.getUserId();
                        }
                        sysProjectTable.setSecondUserId(ids);
                        result = auditFlowCurrentService.insertAuditFlowCurrentProject(sysProjectTable);
                        if (result > 0) {
                            sysProjectTable.setProjectFinishFlag("-2");
                            sysProjectTable.setStoptype(stoptype);
                            sysProjectTable.setStopcause(cause);
                            sysProjectTableService.updateProject(sysProjectTable);
                        }
                    } else {
                        return AjaxResult.warn("您没有该权限！");
                    }
                } else {
                    Integer userId = ShiroUtils.getUserId().intValue();
                    Integer techniquePeopleid = sysProjectTable.getTechniquePeople();
                    Integer chargePeopleid = sysProjectTable.getChargepeopleId();
                    if (techniquePeopleid.equals(userId)) {
                        sysProjectTable.setFirstUserId(String.valueOf(chargePeopleid));
                    } else if (chargePeopleid.equals(userId)) {
                        sysProjectTable.setFirstUserId(String.valueOf(techniquePeopleid));
                    } else {
                        return AjaxResult.warn("您没有该权限！");
                    }
                    sysProjectTable.setFlowId(10);
                    Integer id = sysProjectTable.getProducetypeid();
                    String manager = produceTypeService.getprojectmanagerbyid(id);
                    sysProjectTable.setSecondUserId(manager);
                    SysUser user = new SysUser();
                    user.setDeptId(107L);
                    List<SysUser> userslist = sysUserService.selectInfosByWxId(user);
                    String ids = "";
                    for (SysUser u : userslist) {
                        if (ids != "") {
                            ids += ",";
                        }
                        ids += u.getUserId();
                    }
                    sysProjectTable.setThirdUserId(ids);
                    result = auditFlowCurrentService.insertAuditFlowCurrentProject(sysProjectTable);
                    if (result > 0) {
                        sysProjectTable.setProjectFinishFlag("-2");
                        sysProjectTable.setStoptype(stoptype);
                        sysProjectTable.setStopcause(cause);
                        sysProjectTableService.updateProject(sysProjectTable);
                    }
                }
            }
        }
        return toAjax(result);
    }

}
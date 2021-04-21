package com.ruoyi.web.controller.item;


import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.service.item.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 项目资源需要控制表
 */
@RequestMapping("/item/stage")
@RestController
@Api(value = "ProjectMarketStageController", tags = "项目所需资源相关接口")
public class ProjectMarketStageController extends BaseController {

    @Autowired
    private ProjectMarkStageService projectMarkStageService;
    @Autowired
    private MsgEvtInfoService msgEvtInfoService;
    @Autowired
    private SysProjectTableService sysProjectTableService;

    /**
     * 添加阶段
     *
     * @param projectMarketStageTable
     * @return
     */
    @PostMapping("/saveStage")
    @ResponseBody
    public AjaxResult insertStage(ProjectMarketStageTable projectMarketStageTable) {
        int a = projectMarkStageService.insertStage(projectMarketStageTable);
        return toAjax(a);
    }

    /**
     * 查询阶段
     *
     * @param projectMarketStageTable
     * @return
     */
    @GetMapping("/getAll")
    @ResponseBody
    public List<ProjectMarketStageTable> getStage(ProjectMarketStageTable projectMarketStageTable) {

        return projectMarkStageService.selectAll(projectMarketStageTable);
    }

    @GetMapping("/getProjects")
    @ResponseBody
    public List<ProjectMarketStageTable> getStageProjects() {
        return projectMarkStageService.selectProjects();
    }


    @GetMapping("/openExcel")
    @ResponseBody
    public int openExcel() {
        projectMarkStageService.openExcel();
        return 1;
    }


    @Log(title = "查询用户", businessType = BusinessType.OTHER)
    @RequestMapping(value = "/index", method = {RequestMethod.POST})
    public String showUser(Model model) {
        List<ProjectMarketStageTable> users = projectMarkStageService.selectProjects();
        model.addAttribute("user", users);
        return "index";
    }

    /**
     * 查询阶段
     *
     * @param projectMarketStageTable
     * @return
     */
    @ResponseBody
    @PostMapping("/selectStage")
    public ProjectMarketStageTable[] selectStage(ProjectMarketStageTable projectMarketStageTable) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<ProjectMarketStageTable> list = projectMarkStageService.selectStage(projectMarketStageTable);
        if (!list.isEmpty() && list != null) {
            for (ProjectMarketStageTable p : list) {
                Date date = p.getStageTimeNode();
                if (date != null) {
                    p.setTimeNode(format.format(date));
                }
            }
        }
        ProjectMarketStageTable[] stageTables = new ProjectMarketStageTable[list.size()];
        stageTables = list.toArray(stageTables);
        return stageTables;
    }

    /**
     * 更新阶段分管负责人接口，
     *
     * @param p
     * @return
     */
    @ResponseBody
    @PostMapping("/updateChargePeople")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateStage(@RequestBody ProjectMarketStageTable p) {
        return toAjax(projectMarkStageService.updateStageBranch(p));
    }


    @ApiOperation("更新阶段分管负责人之外的阶段内容")
    @ResponseBody
    @PostMapping("/updateStageOthers")
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult updateStageOthers(@RequestBody ProjectMarketStageTable p) {
        MsgEvtInfo msgEvtInfo = new MsgEvtInfo();
        int a = 0, b = 0;
        Integer userId = ShiroUtils.getUserId().intValue();
        SysProjectTable sysProjectTable = new SysProjectTable();
        Integer projectId = p.getProjectId();
        p.setUpdateBy(userId + "");
        p.setUpdateTime(new Date());
        p.setProjectId(projectId);
        ProjectMarketStageTable[] resources = p.getResources();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获取前端传入的json数组
        for (int i = 0; i < resources.length; i++) {
            p.setStageType(resources[i].getStageType());
            p.setWorkPeriod(resources[i].getWorkPeriod());
            String timeNode = resources[i].getTimeNode();
            if (timeNode != null && timeNode != "") {
                try {
                    Date date = sdf.parse(timeNode);
                    p.setStageTimeNode(date);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            p.setDeviceList(resources[i].getDeviceList());
            p.setToolList(resources[i].getToolList());
            p.setIsCar(resources[i].getIsCar());
            p.setPlace(resources[i].getPlace());
            p.setPartner(resources[i].getPartner());
            a = projectMarkStageService.updateStage(p);
            // 该用户提交更新完成了，之后就对应的阶段的所有信息置为未读
            if (a == 1) {
                // 将提交完成的消息发给技术总负责人
                remindTechniquePeople(p, sysProjectTable, resources[i].getStageType(), projectId);
                // 用户
                msgEvtInfo.setUserId(userId);
                // 阶段
                msgEvtInfo.setAuditId(resources[i].getStageType());
                // 项目
                msgEvtInfo.setEventId(projectId);
                msgEvtInfo.setReadMark(1);
                msgEvtInfo.setUpdateTime(new Date());
                b = msgEvtInfoService.updateReadMark(msgEvtInfo);
            }
        }
        // 当分管负责人填写提交了信息之后，会实时将数据库信息发送到技术总负责人中
        return toAjax(a);
    }


    @ResponseBody
    @PostMapping("/hello")
    public int updateStageType() {
        ProjectMarketStageTable p = new ProjectMarketStageTable();
        p.setWarnState(1);
        p.setId(113);
        p.setUpdateBy(ShiroUtils.getUserId() + "");
        p.setUpdateTime(new Date());
        int b = projectMarkStageService.updateStage(p);
        return b;
    }

    /**
     * 将提交完成的消息给技术总负责人
     *
     * @param p
     * @param sysProjectTable
     * @param stageType
     * @param projectId
     */
    public void remindTechniquePeople(ProjectMarketStageTable p, SysProjectTable sysProjectTable, int stageType, int projectId) {
        p.setProjectId(projectId);
        p.setStageType(stageType);
        //获得对应阶段的负责人
        List<ProjectMarketStageTable> list = projectMarkStageService.selectStageByProjectId(p);
        Integer chargePeopleId = 0;
        Integer remindState = 0;
        for (int i = 0; i < list.size(); i++) {
            chargePeopleId = list.get(i).getChargePeople();
            if (chargePeopleId == null) {
                chargePeopleId = 0;
            }
            remindState = list.get(i).getRemindState();
        }
        int a = 0;
        Integer userId = ShiroUtils.getUserId().intValue();
        MsgEvtInfo m = new MsgEvtInfo();
        //获得技术总分负责人
        sysProjectTable.setpId(projectId);
        Map<String, Object> map = sysProjectTableService.selectTechniquePeopleId(projectId);
        Integer techniquePeople = (Integer) map.get("techniquePeopleId");
        if (techniquePeople == null) {
            techniquePeople = 0;
        }
        m.setType(6);
        m.setEventId(projectId);
        m.setAuditId(stageType);
        m.setUserId(techniquePeople);
        m.setCreateBy(ShiroUtils.getUserId() + "");
        m.setCreateTime(new Date());
        //如果登录用户不等于技术总负责人，且登录用户等于对应的分管负责人，则允许发消息
        if (!userId.equals(techniquePeople) && userId.equals(chargePeopleId)) {
            p.setProjectId(projectId);
            p.setStageType(stageType);
            p.setUpdateBy(ShiroUtils.getUserId() + "");
            p.setUpdateTime(new Date());
            List<MsgEvtInfo> msgList = msgEvtInfoService.selectMsgLists(m);
            if (msgList.size() < 1) {
                a = msgEvtInfoService.insertMessageInfo(m);
                if (a == 1) {
                    p.setRemindState(1);
                    projectMarkStageService.updateStage(p);
                }
            }
            if (msgList.size() > 0 && remindState == 0) {
                p.setRemindState(1);
                int b = projectMarkStageService.updateStage(p);
            }
        }
    }

    @PostMapping("/selectStageByProjectId")
    @ResponseBody
    public List<ProjectMarketStageTable> selectStageByProjectId(ProjectMarketStageTable p) {
        List<ProjectMarketStageTable> list = projectMarkStageService.selectStageByProjectId(p);
        return list;
    }


}

package com.ruoyi.web.controller.system;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.vo.SysDeptVo;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.*;
import com.ruoyi.system.domain.TaskUserFree;
import com.ruoyi.web.service.SysHolidaysService;
import com.ruoyi.web.service.audit.AuditFlowNodeRoleService;
import com.ruoyi.web.service.item.TaskTableService;
import com.ruoyi.web.service.item.TaskUserFreeService;
import com.ruoyi.web.service.item.TaskUserService;
import com.ruoyi.web.util.CalculateHours;
import com.ruoyi.common.utils.MyUtils;
import io.swagger.annotations.ApiOperation;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.service.ISysDeptService;

/**
 * 部门信息
 *
 * @author ruoyi
 */
@Controller
@RequestMapping("/system/dept")
public class SysDeptController extends BaseController {

    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private AuditFlowNodeRoleService auditFlowNodeRoleService;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private SysHolidaysService holidaysService;
    @Autowired
    private TaskUserFreeService taskUserFreeService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private TaskUserService taskUserService;
    private String prefix = "system/dept";

    public static final String DATA_LIST = "deptList";
    public static final Long DEPT_ID = 100L;

    @RequiresPermissions("system:dept:view")
    @GetMapping()
    public String dept() {
        return prefix + "/dept";
    }

    @PostMapping("/list")
    @ResponseBody
    public List<SysDept> list(SysDept dept) {
        /// if (MyUtils.getMapCache(DATA_LIST) != null) {
        ///    return (List<SysDept>) MyUtils.getMapCache(DATA_LIST);
        /// }
        List<SysDept> deptList = deptService.selectDeptList(dept);
        deptList = deptList.stream().limit(1).collect(Collectors.toList());
        MyUtils.putMapCache(DATA_LIST, deptList);
        return deptList;
    }


    @ApiOperation("获取所有的部门id和对应的名字")
    @PostMapping("/selectAllDeptId")
    @ResponseBody
    public List<SysDeptVo> selectAllDeptId(SysDeptVo sysDeptVo) {
        return deptService.selectAllDeptId(sysDeptVo);
    }

    @ApiOperation("获取主管及其以上职位的人员")
    @PostMapping("/leaderLists")
    @ResponseBody
    public List<SysDept> leaderLists(SysDept dept) {
        dept.setSuperior(1);
        List<SysDept> list = deptService.selectDeptList(dept);
        return list;
    }

    @ApiOperation("选择上下级的接口方法")
    @RequestMapping(value = "/leadership", method = {RequestMethod.POST})
    @ResponseBody
    public List<SysDept> selectLeadershipDeptList(SysDept dept, SysProjectTable sysProjectTable, TaskTable taskTable) {
        // 获得p_id，t_d,然后根据p_id,t_id来获得相应的审批主管
        dept.setSearchUser(ShiroUtils.getUserId() + "");
        List<SysDept> deptList = deptService.selectLeadershipDeptList(dept);
        return deptList;
    }

    @RequestMapping(value = "/selectDeptRoleList", method = {RequestMethod.POST})
    @ResponseBody
    public List<SysDept> selectFlowNodeRoleList(AuditFlowNodeRole auditFlowNodeRole) {
        List<AuditFlowNodeRole> auditFlowNodeRoleList = auditFlowNodeRoleService.selectFlowNodeRoleList(auditFlowNodeRole);//得到具有审批权限的用户角色
        String roleId = "";
        for (AuditFlowNodeRole auditFlowNodeRole1 : auditFlowNodeRoleList) {
            roleId += auditFlowNodeRole1.getRoleId() + ",";
        }
        if (roleId.length() > 0) {
            roleId = roleId.substring(0, roleId.length() - 1);
        }
        SysDept dept = new SysDept();
        dept.setSearchRoleId(roleId);
        List<SysDept> list = deptService.selectDeptList(dept);
        return list;
    }

    @ApiOperation("新增部门")
    @GetMapping("/add/{parentId}")
    public String add(@PathVariable("parentId") Long parentId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(parentId));
        return prefix + "/add";
    }

    @ApiOperation("新增保存部门")
    @Log(title = "部门管理", businessType = BusinessType.INSERT)
    @RequiresPermissions("system:dept:add")
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult addSave(SysDept dept) {
        dept.setCreateBy(ShiroUtils.getLoginName());
        MyUtils.removeMapCache("deptList");
        MyUtils.removeMapCache("userGroupSysDepts");
        return toAjax(deptService.insertDept(dept));
    }

    @ApiOperation("修改")
    @GetMapping("/edit/{deptId}")
    public String edit(@PathVariable("deptId") Long deptId, ModelMap mmap) {
        SysDept dept = deptService.selectDeptById(deptId);
        if (StringUtils.isNotNull(dept) && DEPT_ID.equals(deptId)) {
            dept.setParentName("无");
        }
        mmap.put("dept", dept);
        return prefix + "/edit";
    }

    @ApiOperation("保存")
    @Log(title = "部门管理", businessType = BusinessType.UPDATE)
    @RequiresPermissions("system:dept:edit")
    @PostMapping("/edit")
    @ResponseBody
    public AjaxResult editSave(SysDept dept) {
        dept.setUpdateBy(ShiroUtils.getLoginName());
        MyUtils.removeMapCache("deptList");
        MyUtils.removeMapCache("userGroupSysDepts");
        return toAjax(deptService.updateDept(dept));
    }

    @ApiOperation("删除")
    @Log(title = "部门管理", businessType = BusinessType.DELETE)
    @RequiresPermissions("system:dept:remove")
    @GetMapping("/remove/{deptId}")
    @ResponseBody
    public AjaxResult remove(@PathVariable("deptId") Long deptId) {
        if (deptService.selectDeptCount(deptId) > 0) {
            return AjaxResult.warn("存在下级部门,不允许删除");
        }
        if (deptService.checkDeptExistUser(deptId)) {
            return AjaxResult.warn("部门存在用户,不允许删除");
        }
        MyUtils.removeMapCache("deptList");
        MyUtils.removeMapCache("userGroupSysDepts");
        return toAjax(deptService.deleteDeptById(deptId));
    }

    /**
     * 校验部门名称
     */
    @PostMapping("/checkDeptNameUnique")
    @ResponseBody
    public String checkDeptNameUnique(SysDept dept) {
        return deptService.checkDeptNameUnique(dept);
    }

    /**
     * 选择部门树
     */
    @GetMapping("/selectDeptTree/{deptId}")
    public String selectDeptTree(@PathVariable("deptId") Long deptId, ModelMap mmap) {
        mmap.put("dept", deptService.selectDeptById(deptId));
        return prefix + "/tree";
    }

    /**
     * 加载部门列表树
     */
    @GetMapping("/treeData")
    @ResponseBody
    public List<Ztree> treeData() {
        List<Ztree> ztrees = deptService.selectDeptTree(new SysDept());
        return ztrees;
    }

    /**
     * 加载角色部门（数据权限）列表树
     */
    @GetMapping("/roleDeptTreeData")
    @ResponseBody
    public List<Ztree> deptTreeData(SysRole role) {
        List<Ztree> ztrees = deptService.roleDeptTreeData(role);
        return ztrees;
    }

    public void getFreeTimeByUserId(String beginDate, String endDate) {
        //String beginDate = "2019-10-15";
        //String endDate = "2019-10-16";
        TaskTable taskTable = new TaskTable();
        TaskUser taskUser = new TaskUser();
        TaskUserFree taskUserFree = new TaskUserFree();
        //在更新之前，先删除历史记录
        taskUserFreeService.deleteAllInfo(taskUserFree);
        //拿到所有的用户
        SysUser sysUser = new SysUser();
        List<SysUser> userlist = userService.selectUserList(sysUser);
        //遍历用户,你拿到用户id
        for (int i = 0; i < userlist.size(); i++) {
            List<Integer> list = new ArrayList<>();
            List<String> listJob = new ArrayList<>();
            List<String> listJob1 = new ArrayList<>();
            List<String> listJob2 = new ArrayList<>();
            List<String> listJob3 = new ArrayList<>();
            List<String> listJob4 = new ArrayList<>();
            List<String> listJob5 = new ArrayList<>();
            TaskTable[] taskTableList = null;
            long userId = userlist.get(i).getUserId();//用户id
            //获取用户作为负责人
            taskTable.setChargepeopleId(userId);
            List<TaskTable> taskTableList1 = taskTableService.selectListByChargeId(taskTable);
            //获取用户作为参与人
            taskUser.setUserId(userId);
            List<TaskUser> listTid1 = taskUserService.selectAllTid(taskUser);
            //拿到所有的用户参与的任务Id
            for (int j = 0; j < taskTableList1.size(); j++) {
                int tId = taskTableList1.get(j).gettId().intValue();
                list.add(tId);
            }
            for (int x = 0; x < listTid1.size(); x++) {
                Integer tId = listTid1.get(x).gettId().intValue();
                taskTable.settId(tId.longValue());
                String finishFlag = taskTableService.selectFinishFlagById(taskTable);
                if ((Integer.parseInt(finishFlag) > -1) && (Integer.parseInt(finishFlag) != 2)) {
                    list.add(tId);
                }
            }
            if (list.isEmpty()) {
                taskUserFree.setUserId((int) userId);
                taskUserFree.setFreeTime("1");//1表示这段时间都有空
                List<TaskUserFree> listFree = taskUserFreeService.selectUserFreeTime(taskUserFree);
                if (listFree.size() <= 0) {
                    taskUserFree.setCreateTime(new Date());
                    taskUserFreeService.insertTaskUserFree(taskUserFree);
                }
                if (listFree.size() > 0) {
                    taskUserFree.setUpdateTime(new Date());
                    taskUserFreeService.updateTaskUserFree(taskUserFree);
                }
            }
            if (!list.isEmpty()) {
                // 组装成一个对象数组
                taskTableList = new TaskTable[list.size()];
                for (int y = 0; y < list.size(); y++) {
                    taskTable.settId(list.get(y).longValue());
                    taskTableList[y] = taskTableService.selectTaskByTid1(taskTable);
                }
                // 封装假期成listHolidy
                List<SysHolidays> holidaysList = holidaysService.selectSysHolidaysList(null);//假期类，即国家法定节假日
                List<String> listHolidy = new ArrayList<>();
                for (SysHolidays holidays : holidaysList) {
                    listHolidy.add(holidays.getHolidays());//holidays是一个字符串对象
                }
                for (TaskTable taskTable1 : taskTableList) {
                    // 获得当月的第一天和最后一天,字符串的形式
                    // 判断任务的开始时间和结束时间是否在传入的时间段之内
                    String startTime = null, endTime = null;
                    if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getStartTime())) >= DateUtils.getTimelong(beginDate)) {
                        startTime = DateUtils.dateToString(taskTable1.getStartTime());
                        if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getEndTime())) <= DateUtils.getTimelong(endDate)) {
                            endTime = DateUtils.dateToString(taskTable1.getEndTime());
                            try {
                                //去除周末节假日,得到相应的空闲时间
                                List<String> jobDate = CalculateHours.calculateHour1(beginDate + " 8:00:00", startTime + " 8:00:00", listHolidy);
                                //将得到的工作日期存储到一个字符串数组中
                                for (int a = 0; a < jobDate.size(); a++) {
                                    if (!jobDate.get(a).equals(startTime)) {
                                        listJob.add(jobDate.get(a));//得到的结果已经是排除
                                    }
                                }
                                List<String> jobDate1 = CalculateHours.calculateHour1(endTime + " 17:30:00", endDate + " 17:30:00", listHolidy);
                                for (int a = 0; a < jobDate1.size(); a++) {
                                    if (!jobDate1.get(a).equals(endTime)) {
                                        listJob1.add(jobDate1.get(a));//得到的结果已经是排除
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getEndTime())) >= DateUtils.getTimelong(endDate)) {
                            try {
                                List<String> jobDate = CalculateHours.calculateHour1(beginDate + " 8:00:00", startTime + " 8:00:00", listHolidy);
                                for (int a = 0; a < jobDate.size(); a++) {
                                    if (!jobDate.get(a).equals(startTime)) {
                                        listJob2.add(jobDate.get(a));//得到的结果已经是排除
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getStartTime())) < DateUtils.getTimelong(beginDate)) {
                        if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getEndTime())) <= DateUtils.getTimelong(beginDate)) {
                            endTime = DateUtils.dateToString(taskTable1.getEndTime());
                            try {
                                List<String> jobDate = CalculateHours.calculateHour1(beginDate + " 17:30:00", endDate + " 17:30:00", listHolidy);
                                for (int a = 0; a < jobDate.size(); a++) {
                                    if (!jobDate.get(a).equals(endTime)) {
                                        listJob3.add(jobDate.get(a));//得到的结果已经是排除
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                        if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getEndTime())) >= DateUtils.getTimelong(beginDate) && (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getEndTime())) <= DateUtils.getTimelong(endDate))) {
                            endTime = DateUtils.dateToString(taskTable1.getEndTime());
                            try {
                                List<String> jobDate = CalculateHours.calculateHour1(endTime + " 17:30:00", endDate + " 17:30:00", listHolidy);
                                for (int a = 0; a < jobDate.size(); a++) {
                                    if (!jobDate.get(a).equals(endTime)) {
                                        listJob4.add(jobDate.get(a));//得到的结果已经是排除
                                    }
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    if (DateUtils.getTimelong(DateUtils.dateToString(taskTable1.getStartTime())) >= DateUtils.getTimelong(endDate)) {
                        try {
                            List<String> jobDate = CalculateHours.calculateHour1(beginDate + " 17:30:00", endDate + " 17:30:00", listHolidy);
                            for (int a = 0; a < jobDate.size(); a++) {
                                if (!jobDate.get(a).equals(endTime)) {
                                    listJob5.add(jobDate.get(a));//得到的结果已经是排除
                                }
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }

                    }
                    //在这里合成一个list,并且存到数据库中，得到一个用户在某一个时间段有空的时间
                    List<String> dateList = CalculateHours.arrContrast2(listJob, listJob1, listJob2, listJob3, listJob4, listJob5);
                    for (String free : dateList) {
                        taskUserFree.setUserId((int) userId);
                        taskUserFree.setFreeTime(free);
                        List<TaskUserFree> listFree = taskUserFreeService.selectUserFreeTime(taskUserFree);
                        if (listFree.size() <= 0) {
                            taskUserFree.setCreateTime(new Date());
                            taskUserFreeService.insertTaskUserFree(taskUserFree);
                        }
                        if (listFree.size() > 0) {
                            taskUserFree.setUpdateTime(new Date());
                            taskUserFreeService.updateTaskUserFree(taskUserFree);
                        }
                    }

                }//在这里循环插入到某一个表中

            }
        }
    }
}

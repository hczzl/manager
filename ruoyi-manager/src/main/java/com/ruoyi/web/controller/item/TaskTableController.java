package com.ruoyi.web.controller.item;

import com.alibaba.fastjson.JSON;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.framework.util.DateUtil;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.TaskUserFree;
import com.ruoyi.system.mapper.SysDeptMapper;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.vo.TaskQuashVo;
import com.ruoyi.web.mapper.audit.AuditFlowNodeRoleMapper;
import com.ruoyi.web.mapper.item.SysUserWorksMapper;
import com.ruoyi.web.mapper.item.SysYearMonthMapper;
import com.ruoyi.web.mapper.item.TaskTableMapper;
import com.ruoyi.web.service.SysHolidaysService;
import com.ruoyi.web.service.item.TaskUserFreeService;
import com.ruoyi.system.service.impl.SysDeptServiceImpl;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.util.constant.ManagerConstant;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.service.item.*;
import com.ruoyi.web.util.ConstantUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.collections.map.HashedMap;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * ????????????Controller
 *
 * @author zhongzhilong
 * @date 2019-08-14
 */
@Controller
@RequestMapping("/tasktable")
@Api(value = "TaskTableController", tags = "????????????????????????")
public class TaskTableController extends BaseController {

    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private TaskSubnitPlanService taskSubnitPlanService;
    @Autowired
    private TaskPostponeService taskPostponeService;
    @Autowired
    private SysDeptMapper deptMapper;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private TaskUserService taskUserService;
    @Autowired
    private TaskUserFreeService taskUserFreeService;
    @Autowired
    private AuditFlowNodeRoleMapper auditFlowNodeRoleMapper;
    @Autowired
    private SysUserWorksService sysUserWorksService;
    @Autowired
    private RedisTemplate redisTemplate;


    private String prefix = "system/table";

    @RequiresPermissions("system:table:view")
    @GetMapping()
    public String table() {
        return prefix + "/table";
    }

    @ApiOperation("?????????????????????????????????????????????????????????")
    @PostMapping("/list")
    @ResponseBody
    public PageEntity lists(TaskTable taskTable) {
        return taskTableService.lists(taskTable);
    }

    /**
     * ???????????????????????????
     *
     * @param u
     * @return
     */
    @ResponseBody
    @PostMapping("/selectUserAttention")
    public Integer selectUserAttention(UserTaskAttention u) {
        u.setUserId(ShiroUtils.getUserId());
        Integer userAttention = taskTableService.selectUserAttention(u);
        return userAttention;
    }

    @ApiOperation("????????????id???????????????????????????????????????????????????")
    @PostMapping("/selectTaksChange")
    @ResponseBody
    public List<TaskPostpone> selectTaksChange(TaskPostpone taskPostpone) {
        List<Integer> typeList = new ArrayList<>();
        typeList.add(1);
        typeList.add(2);
        taskPostpone.setTypeList(typeList);
        List<TaskPostpone> list = taskPostponeService.selectTaskChange(taskPostpone);
        for (TaskPostpone t : list) {
            Integer chargeId = t.getChargeId();
            if (chargeId == null) {
                chargeId = 0;
            }
            t.setChargeName(userService.getName(chargeId));
            String particiants = t.getParticiants();
            if (particiants != null && particiants.length() > 0) {
                String[] arr = particiants.split(",");
                String particiantsName = "";
                for (int i = 0; i < arr.length; i++) {
                    particiantsName = particiantsName + userService.getName(Integer.parseInt(arr[i])) + ",";
                }
                if (particiantsName.length() > 0) {
                    particiantsName = particiantsName.substring(0, particiantsName.length() - 1);
                }
                t.setParticiantsName(particiantsName);
            }
        }
        // ??????????????????????????????????????????????????????????????????????????????????????????????????????
        list = list.stream()
                .filter(item -> !item.getState().equals(0))
                .sorted(Comparator.comparing(TaskPostpone::getId).reversed())
                .collect(Collectors.toList());
        if (list != null && list.size() > 0 && list.get(list.size() - 1).getType() == null) {
            list.get(list.size() - 1).setType("?????????");
            list.get(list.size() - 1).setCreateTime(null);
        }
        return list;
    }

    @ApiOperation("??????????????????")
    @PostMapping("/getLowerTask")
    @ResponseBody
    public TaskTable[] getLowerTask(TaskTable taskTable) {
        long userId = ShiroUtils.getUserId();
        SysUser user = new SysUser();
        TaskUser taskUser = new TaskUser();
        List<Integer> listInt = new ArrayList<>();
        List<Integer> listUserId1 = new ArrayList<>();
        List<Integer> listTid1 = new ArrayList<>();
        Map<Integer, String> maptId = new HashedMap();
        List<TaskUser> listUser1 = taskUserService.selectAllUser(taskUser);
        for (int i = 0; i < listUser1.size(); i++) {
            listInt.add(listUser1.get(i).getUserId().intValue());
        }
        List<TaskTable> listTask = taskTableService.selectAllChargePeople(taskTable);
        for (int i = 0; i < listTask.size(); i++) {
            listInt.add(listTask.get(i).getChargepeopleId().intValue());
        }
        user.setUserId(userId);
        List<SysUser> list1 = userService.selectDetptIdAndPostId(user);
        for (int i = 0; i < list1.size(); i++) {
            long deptId = list1.get(i).getDeptId().intValue();
            user.setDeptId(deptId);
            List<SysUser> listUser = userService.selectAllDetptUser(user);
            for (int y = 0; y < listInt.size(); y++) {
                for (int x = 0; x < listUser.size(); x++) {
                    if (listInt.get(y) == listUser.get(x).getUserId().intValue()) {
                        listUserId1.add(listInt.get(y));
                    }
                }
            }
        }
        for (Integer key : listUserId1) {
            taskTable.setChargepeopleId(key.longValue());
            List<TaskTable> list3 = taskTableService.selectAllTid(taskTable);
            for (int i = 0; i < list3.size(); i++) {
                listTid1.add(list3.get(i).gettId().intValue());
            }
            taskUser.setUserId(key.longValue());
            List<TaskUser> list4 = taskUserService.selectAllTid(taskUser);
            for (int i = 0; i < list4.size(); i++) {
                listTid1.add(list4.get(i).gettId().intValue());
            }
        }
        for (int i = 0; i < listTid1.size(); i++) {
            maptId.put(listTid1.get(i), "tId");
        }
        int f = 0;
        TaskTable[] taskTables = new TaskTable[maptId.size()];
        for (Integer key : maptId.keySet()) {
            taskTables[f] = taskTableService.getLowerTask(key);
            f++;
        }
        return taskTables;
    }

    @PostMapping("/taskList")
    @ResponseBody
    @ApiOperation("?????????????????????????????????????????????")
    public List<TaskTable> getAllTaskTable(TaskTable taskTable) {
        List<TaskTable> list = taskTableService.getAllTaskTable(taskTable);
        return list;
    }

    @PostMapping("/selectTemporary")
    @ResponseBody
    @ApiOperation("????????????????????????")
    public List<TaskTable> selectTemporaryTask(TaskTable taskTable) {
        List<TaskTable> list = taskTableService.selectTemporaryTask(taskTable);
        for (int i = 0; i < list.size(); i++) {
        }
        return list;
    }

    @PostMapping("/mylist")
    @ResponseBody
    @ApiOperation("???????????????????????????")
    public PageEntity mylists(TaskTable taskTable) {
        if (taskTable.getUuId() == null) {
            taskTable.setPeopleString(ShiroUtils.getUserId().toString());
        } else {
            taskTable.setPeopleString(taskTable.getUuId().toString());
        }
        return lists(taskTable);
    }

    /**
     * ???????????????
     *
     * @param taskTable
     * @param list
     * @return
     */
    public List<TaskTable> paging(TaskTable taskTable, List<TaskTable> list) {
        list = list.stream()
                .skip(taskTable.getTotal() * (taskTable.getPageNumber() - 1))
                .limit(taskTable.getTotal())
                .collect(Collectors.toList());
        return list;
    }

    @ApiOperation("????????????id??????????????????")
    @ResponseBody
    @PostMapping("/selectTaskByTid")
    public List<TaskTable> selectTaskByTid(TaskTable taskTable) {
        List<TaskTable> list = taskTableService.selectTaskByTid2(taskTable);
        return list;
    }

    @ApiOperation("????????????????????????")
    @Log(title = "????????????:????????????????????????", businessType = BusinessType.EXPORT)
    @RequiresPermissions("system:table:export")
    @PostMapping("/export")
    @ResponseBody
    public AjaxResult export(TaskTable taskTable) {
        List<TaskTable> list = taskTableService.selectTaskTableList(taskTable);
        ExcelUtil<TaskTable> util = new ExcelUtil<TaskTable>(TaskTable.class);
        return util.exportExcel(list, "table");
    }

    @ApiOperation("????????????")
    @Log(title = "????????????", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    @ResponseBody
    public AjaxResult saveTask(TaskTable taskTable) {
        Integer period = sysUserWorksService.selectWorkDays(taskTable.getStartTimes(), taskTable.getChargepeopleId());
        if (ShiroUtils.isNotNull(taskTable.getPeriod())) {
            if (Integer.parseInt(taskTable.getPeriod()) + period > ManagerConstant.MAX_PERIOD) {
                return AjaxResult.failure("??????????????????????????????25?????????????????????????????????!");
            }
        }
        return toAjax(taskTableService.saveTask(taskTable));
    }

    @ApiOperation("??????????????????????????????")
    @Log(title = "??????????????????????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/insertQushTask")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult insertQushTask(TaskQuashVo vo) {
        Integer period = sysUserWorksService.selectWorkDays(vo.getStartTimes(), vo.getChargepeopleId());
        if (ShiroUtils.isNotNull(vo.getPeriod())) {
            if (Integer.parseInt(vo.getPeriod()) + period > ManagerConstant.MAX_PERIOD) {
                return AjaxResult.failure("??????????????????????????????25?????????????????????????????????!");
            }
        }
        return toAjax(taskTableService.insertQushTask(vo));
    }


    @ApiOperation("?????????????????????")
    @PostMapping("/addTaskForPlan")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult addTaskForPlan(TaskTable taskTable) {
        int a = 0;
        try {
            taskTable.setCreateTime(new Date());
            taskTable.setTaskFinishflag("0");
            a = taskTableService.insertTaskTableForPlan(taskTable);
            long chargePeopleId = taskTable.getChargepeopleId();
            taskUserService.insertTaskUsers(taskTable, chargePeopleId);
            if (a == 1) {
                redisTemplate.delete("holidaysList");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return toAjax(a);
    }

    @ApiOperation("????????????")
    @GetMapping("/edit/{tId}")
    public String edit(@PathVariable("tId") Long tId, ModelMap mmap) {
        TaskTable taskTable = taskTableService.selectTaskTableById(tId);
        mmap.put("taskTable", taskTable);
        return prefix + "/edit";
    }


    @ApiOperation("????????????")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/stoptask")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult editTask(TaskTable taskTable) {
        List<TaskTable> tables = taskTableService.selectTaskByTid2(taskTable);
        MyUtils.putMapCache(ShiroUtils.getUserId() + "LogObject", JSON.toJSONString(tables));
        return toAjax(taskTableService.editTask(taskTable));
    }

    @ApiOperation("????????????")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/accomplish")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult accomplish(TaskTable taskTable, SysFileInfo sysFileInfo1) {
        MyUtils.putMapCache(ShiroUtils.getUserId() + "LogObject", JSON.toJSONString(taskTableService.selectTaskByTid2(taskTable)));
        return toAjax(taskTableService.accomplish(taskTable, sysFileInfo1));
    }

    @ApiOperation("????????????????????????")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/info/save")
    @ResponseBody
    public AjaxResult editSave(TaskTable taskTable, TaskSubnitPlan taskSubnitPlan) {
        MyUtils.putMapCache(ShiroUtils.getUserId() + "LogObject", JSON.toJSONString(taskTableService.selectTaskByTid2(taskTable)));
        int a = taskSubnitPlanService.insertTaskSubnitPlan(taskSubnitPlan);
        taskTable.setUpdateTime(new Date());
        int b = taskTableService.updateTaskTable(taskTable);
        return toAjax(b);
    }


    @ApiOperation("???????????????????????????????????????")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/changePeriod")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult changePeriod(TaskTable taskTable, TaskPostpone taskPostpone) {
        MyUtils.putMapCache(ShiroUtils.getUserId() + "LogObject", JSON.toJSONString(taskTableService.selectTaskByTid2(taskTable)));
        List<TaskTable> tables = taskTableService.selectTaskByTid2(taskTable);
        MyUtils.putMapCache(ShiroUtils.getUserId() + "LogObject", JSON.toJSONString(tables));
        return toAjax(taskTableService.changePeriod(taskTable, taskPostpone));
    }

    @ApiOperation("??????????????????")
    @RequiresPermissions("system:table:remove")
    @Log(title = "????????????", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public AjaxResult remove(@RequestParam String ids) {
        return toAjax(taskTableService.deleteTaskTableByIds(ids));
    }

    @ApiOperation("??????????????????")
    @Log(title = "????????????", businessType = BusinessType.UPDATE)
    @PostMapping("/quash")
    @ResponseBody
    @ApiImplicitParams({
            @ApiImplicitParam(name = "ids", value = "??????id", required = true),
            @ApiImplicitParam(name = "currentId", value = "?????????id", required = true)
    })
    public AjaxResult quash(@RequestParam String ids, @RequestParam Integer currentId) {
        List<Integer> stateList = auditFlowNodeRoleMapper.selectApplyState(currentId);
        if (stateList.contains(0)) {
            return AjaxResult.error("????????????,?????????????????????,????????????????????????");
        }
        return toAjax(taskTableService.quashTaskByIds(ids, currentId));
    }

    @PostMapping("/findAll")
    @ResponseBody
    @ApiOperation("?????????????????????")
    public PageEntity listTask(TaskTable taskTable) {
        if (taskTable.getTotal() == 0) {
            taskTable.setTotal(ConstantUtil.TOTAL);
        }
        if (taskTable.getPageNumber() != 0) {
            taskTable.setPageNumber((taskTable.getPageNumber() - 1) * taskTable.getTotal());
        }
        if (taskTable.getPages() == 0) {
            List<TaskTable> list1 = taskTableService.selectAllTask(taskTable);
            taskTable.setPages(list1.size());
        }
        List<TaskTable> list = taskTableService.selectAllTask(taskTable);
        return new PageEntity(list.size(), taskTable.getPages(), (taskTable.getPageNumber() / taskTable.getTotal() + 1), list);
    }

    @GetMapping("getAllTitle")
    @ResponseBody
    @ApiOperation("??????????????????")
    public int getAllTitle(TaskTable taskTable) {
        return taskTableService.getAllTitle(taskTable);
    }

    @PostMapping("/getTaskTitle")
    @ResponseBody
    public String[][] getTaskTitle(TaskTable taskTable) {
        List<TaskTable> list1 = taskTableService.selectAllTask(taskTable);
        int maxParentId = list1.get(0).getParentId();
        for (int k = 1; k < list1.size(); k++) {
            if (maxParentId < list1.get(k).getParentId()) {
                maxParentId = list1.get(k).getParentId();
            }
        }
        int taskNumber = taskTableService.getAllTitle(taskTable);
        //??????hashmap??????
        HashMap<String, List<String>> hm = new HashMap<String, List<String>>();
        int size = list1.size();
        String[][] title = new String[size][size];

        int id = list1.get(0).getParentId();
        for (int i = 0; i < list1.size(); i++) {
            for (int j = 0; j < list1.size(); j++) {
                if (id == list1.get(j).getParentId()) {
                    title[i][0] =
                            title[i][j] = list1.get(j).getTaskTitle();
                }
            }
            id++;
            if (id <= maxParentId) {
                System.out.println("parentId=" + id);
            }
        }
        return title;
    }


    @ApiOperation("????????????????????????????????????")
    @PostMapping("/getBusy")
    @ResponseBody
    public AjaxResult getBusy(String startTime, String endTime) {
        return AjaxResult.success("", taskTableService.getBusy(startTime, endTime));

    }

    @ApiOperation("?????????????????????????????????????????????????????????")
    @PostMapping("/getworkday")
    @ResponseBody
    public Integer getworkday(String startTime, String endTime) {
        return taskTableService.getworkday(startTime, endTime);
    }


    @ApiOperation("?????????????????????????????????????????????")
    @PostMapping("/getFinallyDate")
    @ResponseBody
    public AjaxResult getFinallyDate(String startTime, String period) throws ParseException {
        String startTimes = DateUtils.date2TimeStamp(startTime, "yyyy-MM-dd");
        return AjaxResult.success("", taskTableService.getFinallyDate(startTimes, period));
    }

    @ApiOperation("????????????????????????????????????")
    @PostMapping("/saturation")
    @ResponseBody
    @Transactional(rollbackFor = Exception.class)
    public Map saturation(TaskTable taskTable) {
        return taskTableService.saturation(taskTable);
    }

    @PostMapping("/dept/list")
    @ResponseBody
    public List<SysDept> selectDeptList(SysDept dept) {
        List<SysDept> deptList = deptMapper.selectDeptList(dept);
        Map<String, List<String>> listMap = (Map<String, List<String>>) taskTableService.getBusy("2017-05-06", "2019-06-05");
        for (SysDept sysDept : deptList) {
            for (SysUser sysUser : sysDept.getUsers()) {
                for (Map.Entry<String, List<String>> entry : listMap.entrySet()) {
                    if (sysUser.getUserId().equals(Long.valueOf(entry.getKey()))) {
                        sysUser.setNotbusyList(entry.getValue());
                    } else {
                        sysUser.setNotbusyList(DateUtils.getBetweenDate("2017-05-06", "2019-06-05"));
                    }

                }
            }
        }
        List<SysDept> sysDeptList = new SysDeptServiceImpl().getChildPerms(deptList);
        return sysDeptList;
    }

    @ResponseBody
    @PostMapping("/selectAllInfo")
    public List<TaskUserFree> selectAllInfo(TaskUserFree taskUserFree) {
        List<TaskUserFree> listAll = taskUserFreeService.selectUserFreeTime(taskUserFree);
        return listAll;
    }


    @ApiOperation("?????????????????????")
    @PostMapping("/updateTaskById")
    @ResponseBody
    public AjaxResult updateTaskTable(TaskTable taskTable) {
        taskTable.setUpdateTime(new Date());
        int a = taskTableService.updateTaskTable(taskTable);
        return toAjax(a);
    }


    @GetMapping("/selectTaskChanges/{currentId}")
    @ResponseBody
    public List<TaskChange> selectTaskChanes(@PathVariable("currentId") Integer currentId) {

        return taskTableService.selectTaskChanes(currentId);
    }

    @PostMapping("/attentionlist")
    @ResponseBody
    public PageEntity selectAttentionlist(TaskTable taskTable) {
        taskTable.setUserId(ShiroUtils.getUserId());

        List<TaskTable> resultList = taskTableService.selectAttentionlist(taskTable);
        List<TaskTable> list = paging(taskTable, resultList);
        for (int i = 0; i < list.size(); i++) {
            long tId = list.get(i).gettId().intValue();
            Integer state = taskTableService.isAttention(tId);
            list.get(i).setUserAttention(state);
        }
        return new PageEntity(list.size(), resultList.size(), (taskTable.getPageNumber() / taskTable.getTotal()), list);
    }


}

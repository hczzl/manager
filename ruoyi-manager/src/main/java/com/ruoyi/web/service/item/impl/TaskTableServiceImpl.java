package com.ruoyi.web.service.item.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.MyUtils;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.vo.SysUserVo;
import com.ruoyi.system.mapper.SysUserMapper;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.*;
import com.ruoyi.web.domain.vo.SysProjectTableVo;
import com.ruoyi.web.domain.vo.TaskQuashVo;
import com.ruoyi.web.domain.vo.TaskTableVo;
import com.ruoyi.web.util.pageutils.PageEntity;
import com.ruoyi.web.mapper.audit.AuditFlowCurrentMapper;
import com.ruoyi.web.mapper.audit.AuditFlowOperRecordMapper;
import com.ruoyi.web.mapper.item.*;
import com.ruoyi.web.service.SysFileInfoService;
import com.ruoyi.web.service.SysHolidaysService;
import com.ruoyi.web.service.audit.AuditCopyMsgService;
import com.ruoyi.web.service.audit.AuditFlowCurrentService;
import com.ruoyi.web.service.item.*;
import com.ruoyi.web.util.CalculateHours;
import com.ruoyi.web.util.ConstantUtil;
import com.ruoyi.web.util.UserUtil;
import com.ruoyi.web.util.constant.ManagerConstant;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 发起任务Service业务层处理
 *
 * @author ruoyi
 * @date 2019-08-14
 */
@Service
public class TaskTableServiceImpl implements TaskTableService {
    @Autowired
    private TaskTableMapper taskTableMapper;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private AuditFlowCurrentService auditFlowCurrentService;
    @Autowired
    private TaskUserService taskUserService;
    @Autowired
    private SysHolidaysService holidaysService;
    @Autowired
    private TaskPostponeService taskPostponeService;
    @Autowired
    private ISysUserService userService;
    @Autowired
    private TaskStopMoneService taskStopMoneService;
    @Autowired
    private SysFileInfoService fileInfoService;
    @Autowired
    private ProjectTaskTableService projectTaskTableService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private AuditFlowOperRecordMapper auditFlowOperRecordMapper;
    @Autowired
    private ProduceTypeService produceTypeService;
    @Autowired
    private SysUserMapper userMapper;
    @Autowired
    private TaskStopMoneMapper taskStopMoneMapper;
    @Autowired
    private TaskPostponeMapper taskPostponeMapper;
    @Autowired
    private AuditFlowCurrentMapper auditFlowCurrentMapper;
    @Autowired
    private MsgEvtInfoMapper msgEvtInfoMapper;
    @Autowired
    private AuditCopyMsgService auditCopyMsgService;
    @Autowired
    private SysProjectTableMapper sysProjectTableMapper;
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public PageEntity lists(TaskTable taskTable) {
        Long userId = ShiroUtils.getUserId();
        taskTable.setUserId(userId);
        if ("1".equals(taskTable.getSearchDepeId())) {
            SysUser sysUser = UserUtil.getUser();
            taskTable.setSearchDepeId(sysUser.getDeptId().toString());
        }
        // 完成任务的查询
        if (taskTable.getEndTimes() != 0) {
            String endTime = UserUtil.timeToDate(taskTable.getEndTimes());
            taskTable.setEndTime(DateUtils.parseDate(DateUtils.convert2String(taskTable.getEndTimes(), "")));
        }
        if (taskTable.getStartTimes() != 0) {
            String startTime = UserUtil.timeToDate(taskTable.getStartTimes());
            taskTable.setStartTime(DateUtils.parseDate(DateUtils.convert2String(taskTable.getStartTimes(), "")));
        }
        if (taskTable.getStartTime() == null && taskTable.getEndTime() != null) {
            taskTable.setStartTime(new Date(0));
        }
        if (taskTable.getEndTime() == null && taskTable.getStartTime() != null) {
            taskTable.setEndTime(new Date(Long.MAX_VALUE));
        }
        // 完成任务逾期功能
        Integer overdue = taskTable.getOverdue();
        if (overdue == null) {
            overdue = 0;
        }
        if (overdue == 1) {
            taskTable = setOverdue(taskTable);
        }
        if (taskTable.getTotal() == 0) {
            taskTable.setTotal(ConstantUtil.TOTAL);// 初始化显示条数,1000条
        }
        String people = taskTable.getPeopleString();// 包含参与人和负责人的id
        // 实现任务关键字查询
        if (people != null && people.length() > 0) {
            // 根据用户id获取任务id
            taskTable.setIsKey(1);
            List<TaskTable> allTaskById = getAllTaskById(taskTable);
            List<Integer> id = new ArrayList<>();
            allTaskById.forEach(item -> {
                id.add(Integer.parseInt(item.gettId().toString()));
            });
            taskTable.setPeopleList(id);
            // 如果为0表示该用户没有任务
            if (taskTable.getPeopleList() == null || taskTable.getPeopleList().size() == 0) {
                return new PageEntity(taskTable.getTotal(), 0, (taskTable.getPageNumber() / taskTable.getTotal()), new ArrayList());
            }
        }
        if (taskTable.getTotal() == 0) {
            taskTable.setTotal(ConstantUtil.TOTAL);
        }
        if (taskTable.getPageNumber() != 0) {
            taskTable.setPageNumber((taskTable.getPageNumber() - 1) * taskTable.getTotal());
        }
        List<TaskTable> taskLists = taskTableService.selectTaskTableList(taskTable);
        taskLists = setValue(taskLists);
        if (taskLists.isEmpty() && taskLists == null) {
            return new PageEntity(taskTable.getTotal(), 0, (taskTable.getPageNumber() / taskTable.getTotal()), null);
        }
        Integer pages = taskTableService.selectTaskTableListCount(taskTable);
        return new PageEntity(taskTable.getTotal(), pages, (taskTable.getPageNumber() / taskTable.getTotal()), taskLists);
    }

    public List<TaskTable> setValue(List<TaskTable> list) {
        for (TaskTable task : list) {
            SysProjectTable sysProjectTable = task.getProjectTable();
            if (sysProjectTable != null) {
                Integer id = sysProjectTable.getProducetypeid();
                String projectManagers = produceTypeService.getprojectmanagerbyid(id);
                if (ShiroUtils.isNotNull(projectManagers)) {
                    List<SysUserVo> usersList = userMapper.selectUserListByUserId(projectManagers.split(","));
                    task.setManagersList(usersList);
                }
            }
            Map<String, Object> map = new HashMap<>();
            String userName = "";
            map.put("applyId", task.gettId());
            if (task.getTaskFinishflag().equals(ManagerConstant.TASK_NOT_FINISH) || task.getTaskFinishflag().equals(ManagerConstant.TASK_FINISH)) {//申请完成
                map.put("auditId", 9);
            } else if (task.getTaskFinishflag().equals(ManagerConstant.TASK_NOT_STOP) || task.getTaskFinishflag().equals(ManagerConstant.TASK_STOP)) {//申请中止
                map.put("auditId", 8);
                String stopMemo = taskStopMoneMapper.selectStopMemo(task.gettId());//中止原因
                task.setTaskStopMemo(stopMemo);
            } else if (task.getTaskFinishflag().equals(ManagerConstant.TASK_CHANGE)) {//申请变更
                map.put("auditId", 12);
                String changeMemo = taskPostponeMapper.selectChangeMemo(task.gettId());
                task.setChangeTimeMemo(changeMemo);
            }
            userName = taskTableMapper.selectApprovalCreate(map);
            //项目负责人
            if (ShiroUtils.isNull(userName) && task.getTaskFinishflag().equals(ManagerConstant.TASK_STOP) && task.getProjectId() != null && task.getProjectId() != 0) {
                Long count = auditFlowCurrentMapper.selectCount(task.gettId().intValue(), 8, 5);
                if (count == null || count < 1) {
                    Map<String, Object> projectMap = sysProjectTableMapper.selectChargeName(task.getProjectId().intValue());
                    if (projectMap != null && projectMap.size() > 1) {
                        Integer projectType = (Integer) projectMap.get("projectType");
                        if (projectType != null && projectType == 0) {
                            userName = (String) projectMap.get("chargePeopleName");
                        } else if (projectType != null && projectType == 1) {
                            userName = (String) projectMap.get("techniquePeopleName");
                        }
                    }
                }
            }
            task.setApprovalName(userName);
            Map<String, Object> map2 = new HashMap<>();
            map2.put("applyId", task.gettId());
            if (task.getTaskFinishflag().equals(ManagerConstant.TASK_FINISH)) {// 任务完成
                map2.put("auditId", 9);
            } else if (task.getTaskFinishflag().equals(ManagerConstant.TASK_STOP)) {// 任务中止
                map2.put("auditId", 8);
                String stopMemo = taskStopMoneMapper.selectStopMemo(task.gettId());// 中止原因
                task.setTaskStopMemo(stopMemo);
            }
            List<String> memoList = taskTableMapper.selectApprovalMemo(map2);
            if (ShiroUtils.isNotEmpty(memoList)) {
                task.setApprovalMemo(String.join(",", memoList));
            }
        }
        return list;
    }

    /**
     * 根据用户id查询任务
     *
     * @param taskTable
     * @return
     */
    public List<TaskTable> getAllTaskById(TaskTable taskTable) {
        List<TaskTable> taskTableList = new ArrayList<>();
        String[] peopleId = taskTable.getPeopleString().split(",");
        for (int i = 0; i < peopleId.length; i++) {
            List<TaskTable> taskTables = taskTableService.selectTaskByUserId(Long.parseLong(peopleId[i]));
            if (taskTable.getStartTime() == null && taskTable.getEndTime() == null) {
                taskTableList.addAll(taskTables);
                continue;
            }
            if (taskTable.getStartTime() == null) {
                taskTable.setStartTime(new Date(0));
            }
            if (taskTable.getEndTime() == null) {
                taskTable.setEndTime(new Date(0));
            }
            taskTables = taskTables.stream()
                    .filter(item -> !item.getTaskFinishflag().equals(ManagerConstant.TASK_NOT_APPROVAL))
                    .filter(
                            item -> (item.getStartTime().compareTo(taskTable.getEndTime()) <= 0 && item.getEndTime().compareTo(taskTable.getEndTime()) >= 0)
                                    || (item.getStartTime().compareTo(taskTable.getStartTime()) >= 0 && item.getEndTime().compareTo(taskTable.getEndTime()) <= 0)
                                    || (item.getStartTime().compareTo(taskTable.getStartTime()) <= 0 && item.getEndTime().compareTo(taskTable.getStartTime()) >= 0)
                    )
                    .collect(Collectors.toList());
            taskTableList.addAll(taskTables);
        }
        return taskTableList;
    }

    /**
     * 实现逾期的功能
     *
     * @param taskTable
     * @return
     */
    public TaskTable setOverdue(TaskTable taskTable) {
        String b = DateUtils.getfirstday();//自动获取当月的数据
        String e = DateUtils.getlastday();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date begin = sdf.parse(b);
            Date end = sdf.parse(e);
            taskTable.setBegin(begin);
            taskTable.setEnd(end);
        } catch (ParseException ex) {
            ex.printStackTrace();
        }
        return taskTable;
    }

    @Override
    public List<TaskTable> selectTaskTableByPlantId(Integer parentid) {
        return taskTableMapper.selectTaskTableByPlantId(parentid);
    }

    /**
     * 查询发起任务
     *
     * @param tId 发起任务ID
     * @return 发起任务
     */
    @Override
    public TaskTable selectTaskTableById(Long tId) {
        return taskTableMapper.selectTaskTableById(tId);
    }

    /**
     * 计算任务总数
     *
     * @param taskTable
     * @return
     */
    @Override
    public int getAllTitle(TaskTable taskTable) {
        return taskTableMapper.getAllTitle(taskTable);
    }

    @Override
    public List<TaskTable> selectTaskTableList(TaskTable taskTable) {
        List<TaskTable> list = taskTableMapper.selectTaskTableList(taskTable);
        // 排序
        if (taskTable.getIsKey() != null && taskTable.getIsKey().equals(0)) {
            list = list.stream()
                    .sorted(Comparator.comparing(TaskTable::getChargepeopleId))
                    .sorted(Comparator.comparing(TaskTable::getStartTime))
                    .collect(Collectors.toList());
        } else {
            list = list.stream()
                    .sorted(Comparator.comparing(TaskTable::getStartTime))
                    .collect(Collectors.toList());
        }
        for (TaskTable t : list) {
            Integer state = isAttention(t.gettId());
            t.setUserAttention(state);
            List<SysUserVo> usersList = new ArrayList<>();
            SysProjectTable sysProjectTable = t.getProjectTable();
            if (sysProjectTable != null) {
                Integer techniqueId = sysProjectTable.getTechniquePeople();
                if (techniqueId != null) {
                    SysUser sysUser = userService.selectUserInfo((long) techniqueId);
                    t.getProjectTable().setTechniqueUsers(sysUser);
                }
                Integer id = sysProjectTable.getProducetypeid();
                String projectManagers = produceTypeService.getprojectmanagerbyid(id);
                if (!MyUtils.isEmpty(projectManagers)) {
                    String[] arr = projectManagers.split(",");
                    for (String a : arr) {
                        long uId = Long.parseLong(a);
                        SysUserVo sysUser = userService.selectUserInfos(uId);
                        usersList.add(sysUser);
                    }
                    t.setManagersList(usersList);
                }
            }
        }
        return list;
    }

    /**
     * 新增发起任务
     *
     * @param taskTable 发起任务
     * @return 结果
     */
    @Override
    public int insertTaskTable(TaskTable taskTable) {
        taskTable.setStartTime(DateUtils.parseDate(DateUtils.convert2String(taskTable.getStartTimes(), "")));
        taskTable.setEndTime(DateUtils.parseDate(DateUtils.convert2String(taskTable.getEndTimes(), "")));
        taskTable.setTaskFinishsubmittime(DateUtils.parseDate(taskTable.getTaskFinishsubmittimes()));
        int a = taskTableMapper.insertTaskTable(taskTable);
        return a;
    }

    /**
     * 新增发起任务
     * 里程碑中的添加任务，不要进行审批
     *
     * @param taskTable
     * @return
     */
    @Override
    public int insertTaskTableForPlan(TaskTable taskTable) {
        taskTable.setStartTime(DateUtils.parseDate(DateUtils.convert2String(taskTable.getStartTimes(), "")));
        taskTable.setEndTime(DateUtils.parseDate(DateUtils.convert2String(taskTable.getEndTimes(), "")));
        taskTable.setTaskFinishsubmittime(DateUtils.parseDate(taskTable.getTaskFinishsubmittimes()));
        int a = taskTableMapper.insertTaskTableForPlan(taskTable);
        return a;
    }

    @Override
    public int insertExcelTaskTableForPlan(TaskTable taskTable) {
        taskTable.setTaskFinishsubmittime(DateUtils.parseDate(taskTable.getTaskFinishsubmittimes()));
        int a = taskTableMapper.insertTaskTableForPlan(taskTable);
        return a;
    }

    /**
     * 修改发起任务
     *
     * @param taskTable 发起任务
     * @return 结果
     */
    @Override
    public int updateTaskTable(TaskTable taskTable) {
        return taskTableMapper.updateTaskTable(taskTable);
    }

    /**
     * 删除发起任务对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTaskTableByIds(String ids) {
        return taskTableMapper.deleteTaskTableByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除发起任务信息
     *
     * @param tId 发起任务ID
     * @return 结果
     */
    @Override
    public int deleteTaskTableById(Long tId) {
        return taskTableMapper.deleteTaskTableById(tId);
    }

    @Override
    public int quashTaskByIds(String ids, Integer currentId) {
        // 将任务状态更改为任务撤销状态
        int a = 0;
        Map<String, Object> flowCurrentMap = auditFlowCurrentMapper.selectFlowCurrentMap(currentId);
        Integer auditId = (Integer) flowCurrentMap.get("auditId");
        Map<String, Object> map = new HashMap<>();
        Integer[] auditIds = {1};
        Integer[] auditIds2 = {8, 9, 12};
        if (Arrays.asList(auditIds).contains(auditId)) {
            // 发起任务-将审批状态更改为已撤销状态
            a = taskTableMapper.quashTaskByIds(Convert.toStrArray(ids));
            map.put("currentId", currentId);
            map.put("currentState", 7);
            a = auditFlowCurrentMapper.updateCurrentState(map);
        } else if (Arrays.asList(auditIds2).contains(auditId)) {
            // 更新任务信息
            map.put("taskFinishflag", "0");
            map.put("tId", ids);
            taskTableMapper.update(map);
            // 删除审批信息
            auditFlowCurrentMapper.deleteMultipleTable(currentId);
        }
        // 删除审批信息
        map.put("type", 3);
        map.put("eventId", currentId);
        a = msgEvtInfoMapper.deleteMsgEvt(map);
        return a;
    }

    @Override
    public List<TaskTable> selectAllTask(TaskTable taskTable) {
        List<TaskTable> list = taskTableMapper.selectAllTask(taskTable);
        for (TaskTable t : list) {
            List<SysUserVo> usersList = new ArrayList<>();
            SysProjectTable sysProjectTable = t.getProjectTable();
            if (sysProjectTable != null) {
                Integer techniqueId = sysProjectTable.getTechniquePeople();
                if (techniqueId != null) {
                    SysUser sysUser = userService.selectUserInfo((long) techniqueId);
                    t.getProjectTable().setTechniqueUsers(sysUser);
                }
                Integer id = sysProjectTable.getProducetypeid();
                String projectManagers = produceTypeService.getprojectmanagerbyid(id);
                if (!MyUtils.isEmpty(projectManagers)) {
                    String[] arr = projectManagers.split(",");
                    for (String a : arr) {
                        long uId = Long.parseLong(a);
                        SysUserVo sysUser = userService.selectUserInfos(uId);
                        usersList.add(sysUser);
                    }
                    t.setManagersList(usersList);
                }
            }
            Map<String, Object> map = new HashMap<>();
            map.put("applyId", t.gettId());
            map.put("userId", t.getTaskRater());
            String taskFinishMemo = auditFlowOperRecordMapper.selectMemo(map);
            t.setTaskStopMemo(taskFinishMemo);
        }
        return list;
    }

    @Override
    public Map<String, List<String>> getBusy(String startTime, String endTime) {
        List<String> introduction = DateUtils.getBetweenDate(startTime, endTime);
        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        TaskTable taskTable = new TaskTable();
        taskTable.setTaskFinishflag("0");
        List<TaskTable> taskTableList = taskTableMapper.selectTaskTableList(taskTable);
        Map<String, List<String>> listMap = new HashedMap();
        long chargepeopleId = taskTableList.get(0).getChargepeopleId();
        for (TaskTable taskTable2 : taskTableList) {
            if (taskTable2.getChargepeopleId() == chargepeopleId) {
                List<String> introduction1 = DateUtils.getBetweenDate(DateFormatUtils.format(taskTable2.getStartTime(), "yyyy-MM-dd"), DateFormatUtils.format(taskTable2.getEndTime(), "yyyy-MM-dd"));
                for (String s : introduction1) {
                    list.add(s);
                }
            } else {
                for (int i = 0; i < introduction.size(); i++) {
                    int p = 0;
                    for (String ss : list) {
                        if (!introduction.get(i).equals(ss)) {
                            p++;

                        }
                    }
                    if (p == list.size()) {
                        list1.add(introduction.get(i));
                    }
                }
                listMap.put(chargepeopleId + "", list1);
                list = new ArrayList<>();
                list1 = new ArrayList<>();
                List<String> introduction1 = DateUtils.getBetweenDate(DateFormatUtils.format(taskTable2.getStartTime(), "yyyy-MM-dd"), DateFormatUtils.format(taskTable2.getEndTime(), "yyyy-MM-dd"));
                for (String s : introduction1) {
                    list.add(s);
                }
            }
        }
        return listMap;
    }

    @Override
    public Integer getworkday(String startTime, String endTime) {
        Integer period = 0;
        List<SysHolidays> holidaysList = holidaysService.selectSysHolidaysList(null);// 假期类，即国家法定节假日
        List<String> list = new ArrayList<>();
        for (SysHolidays holidays : holidaysList) {
            list.add(holidays.getHolidays());// holidays是一个字符串对象
        }
        try {
            period = (int) (CalculateHours.calculateHour(startTime + " 8:00:00", endTime + " 17:30:00", list) / 7.5);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return period;
    }

    @Override
    public String getFinallyDate(String startTime, String period) throws ParseException {
        startTime = DateUtils.convert2String(Long.parseLong(startTime), "");// 返回的是一个string类型的字符串
        return CalculateHours.getFinallyDate(startTime, period);
    }

    @Override
    public Map saturation(TaskTable taskTable) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        int workday = 0;
        final String[] period = {"0"};
        int creatTaskNum = 0;
        taskTable.setStartTime(DateUtils.parseDate(DateUtils.getfirstday()));
        taskTable.setEndTime(DateUtils.parseDate(DateUtils.getlastday()));
        // 计算当月工作天数
        List<SysHolidays> sysHolidays = holidaysService.selectSysHolidaysList(null);
        sysHolidays = sysHolidays.stream()
                .filter(item -> item.getHolidays().compareTo(format.format(taskTable.getStartTime())) >= 0)
                .filter(item -> item.getHolidays().compareTo(format.format(taskTable.getEndTime())) <= 0)
                .collect(Collectors.toList());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(taskTable.getStartTime());
        workday = calendar.getActualMaximum(Calendar.DAY_OF_MONTH) - sysHolidays.size();

        // 获取我的任务
        List<TaskTable> taskTables = taskTableService.selectTaskByUserId(ShiroUtils.getUserId());
        taskTables = taskTables.stream()
                .filter(item -> !item.getTaskFinishflag().equals(ManagerConstant.TASK_NOT_APPROVAL))
                .filter(item -> !item.getTaskFinishflag().equals(ManagerConstant.TASK_STOP))
                .filter(
                        item -> (item.getStartTime().compareTo(taskTable.getEndTime()) <= 0 && item.getEndTime().compareTo(taskTable.getEndTime()) >= 0)
                                || (item.getStartTime().compareTo(taskTable.getStartTime()) >= 0 && item.getEndTime().compareTo(taskTable.getEndTime()) <= 0)
                                || (item.getStartTime().compareTo(taskTable.getStartTime()) <= 0 && item.getEndTime().compareTo(taskTable.getStartTime()) >= 0)
                )
                .collect(Collectors.toList());
        // 计算工作周期
        if (taskTables != null && taskTables.size() > 0) {
            taskTables.forEach(item -> period[0] = Double.parseDouble(period[0]) + Double.parseDouble(item.getPeriod()) + "");
        }

        taskTable.setCreateBy(ShiroUtils.getUserId() + "");
        List<TaskTable> createByList = taskTableMapper.selectCreateBy(taskTable);
        for (int i = 0; i < createByList.size(); i++) {
            auditFlowCurrent.setApplyId(createByList.get(i).gettId().intValue());
            Integer currentState = auditFlowCurrentService.selectCurrentState(auditFlowCurrent);
            if (currentState == null) {
                currentState = 0;
            }
            if (currentState != 3) {
                if (DateUtils.getTimelong(DateUtils.dateToString(createByList.get(i).getStartTime())) >= DateUtils.getTimelong(DateUtils.getfirstday())) {
                    if (DateUtils.getTimelong(DateUtils.dateToString(createByList.get(i).getStartTime())) <= DateUtils.getTimelong(DateUtils.getlastday())) {
                        creatTaskNum++;
                    }
                }
            }
        }
        Map<String, Object> map = new HashedMap();
        map.put("workday", workday);
        map.put("period", period[0]);
        // redisTemplate.opsForValue().set("workday", workday);
        map.put("taskSize", creatTaskNum);
        return map;
    }

    /**
     * 任务到期提醒
     *
     * @param response
     * @param request
     * @param taskTable
     * @return
     */
    @Override
    public int taskMessageRemind(HttpServletResponse response, HttpServletRequest request, TaskTable taskTable) {
        return taskTableMapper.taskMessageRemind(response, request, taskTable);
    }

    /**
     * 获取taskTable的list
     *
     * @param taskTable
     * @return
     */
    @Override
    public List<TaskTable> getAllTaskRemind(TaskTable taskTable) {
        return taskTableMapper.getAllTaskRemind(taskTable);
    }

    /**
     * 获取负责人id
     *
     * @param tId
     * @return
     */
    @Override
    public Integer selectChargePeopleId(int tId) {
        Integer a = taskTableMapper.selectChargePeopleId(tId);
        if (a == null) {
            a = 0;
        }
        return a;

    }

    /**
     * 临时任务接口方法
     *
     * @param taskTable
     * @return
     */
    @Override
    public List<TaskTable> selectTemporaryTask(TaskTable taskTable) {
        return taskTableMapper.selectTemporaryTask(taskTable);
    }

    /**
     * 任务审批记录接口
     *
     * @param taskTable
     * @return
     */
    @Override
    public List<TaskTable> selectTaskByTid(TaskTable taskTable) {
        return taskTableMapper.selectTaskByTid(taskTable);
    }

    @Override
    public TaskTable selectTaskByTid1(TaskTable taskTable) {
        return taskTableMapper.selectTaskByTid1(taskTable);
    }

    /**
     * 根据负责人查任务
     *
     * @param taskTable
     * @return
     */
    @Override
    public List<TaskTable> selectTaskByChargePeoleId(TaskTable taskTable) {
        return taskTableMapper.selectTaskByChargePeoleId(taskTable);
    }

    /**
     * 获得任务完成标识
     *
     * @param taskTable
     * @return
     */
    @Override
    public String selectFinishFlagById(TaskTable taskTable) {
        return taskTableMapper.selectFinishFlagById(taskTable);
    }

    @Override
    public List<Map<String, Object>> selectWarnList(Map<String, Object> map) {
        return taskTableMapper.selectWarnList(map);
    }

    @Override
    public void update(Map<String, Object> map) {
        taskTableMapper.update(map);
    }

    @Override
    public List<TaskTable> getAllTaskTable(TaskTable taskTable) {
        return taskTableMapper.getAllTaskTable(taskTable);
    }

    /**
     * 获得项目id
     *
     * @param taskTable
     * @return
     */
    @Override
    public int selectProjectId(TaskTable taskTable) {
        return taskTableMapper.selectProjectId(taskTable);
    }

    /**
     * 获得任务发起人
     *
     * @param taskTable
     * @return
     */
    @Override
    public String selectCreateId(TaskTable taskTable) {
        return taskTableMapper.selectCreateId(taskTable);
    }

    /**
     * 获得下属的任务
     *
     * @param userId
     * @return
     */
    @Override
    public TaskTable getLowerTask(int userId) {
        return taskTableMapper.getLowerTask(userId);
    }

    /**
     * 获得所有的负责人
     *
     * @param taskTable
     * @return
     */
    @Override
    public List<TaskTable> selectAllChargePeople(TaskTable taskTable) {
        return taskTableMapper.selectAllChargePeople(taskTable);
    }

    /**
     * 获得t_id
     *
     * @param taskTable
     * @return
     */
    @Override
    public List<TaskTable> selectAllTid(TaskTable taskTable) {
        return taskTableMapper.selectAllTid(taskTable);
    }

    /**
     * 获得开始时间
     *
     * @param taskTable
     * @return
     */
    @Override
    public Date getStartTime(TaskTable taskTable) {
        return taskTableMapper.getStartTime(taskTable);
    }

    /**
     * 获取任务进度
     *
     * @param taskTable
     * @return
     */
    @Override
    public int selectTaskSchedule(TaskTable taskTable) {
        return taskTableMapper.selectTaskSchedule(taskTable);
    }

    /**
     * 更新任务完成标识
     *
     * @param taskTable
     * @return
     */
    @Override
    public int updateTaskTableByFlag(TaskTable taskTable) {
        return taskTableMapper.updateTaskTableByFlag(taskTable);
    }

    /**
     * 根据负责人得到list,完成饱和度的计算
     *
     * @param taskTable
     * @return
     */
    @Override
    public List<TaskTable> selectListByChargeId(TaskTable taskTable) {
        return taskTableMapper.selectListByChargeId(taskTable);
    }

    /**
     * 根据id查询任务，并且合并
     *
     * @param taskTable
     * @return
     */
    @Override
    public List<TaskTable> selectTaskByTid2(TaskTable taskTable) {
        List<TaskTable> list = taskTableMapper.selectTaskByTid2(taskTable);
        for (TaskTable table : list) {
            Map<String, Object> map = auditFlowCurrentMapper.selectFlowCurrentMap(taskTable.getCurrentId());
            if (map != null && map.size() > 0) {
                String createName = (String) map.get("createName");
                table.setApprovalName(createName);
                Integer currentId = (Integer) map.get("currentId");
                String approvalMemo = auditFlowOperRecordMapper.selectApprovalMemo(currentId);
                table.setApprovalMemo(approvalMemo);
            }
        }
        return list;
    }

    /**
     * 根据用户id和任务id得到用户关注任务的状态
     *
     * @param userTaskAttention
     * @return
     */
    @Override
    public Integer selectUserAttention(UserTaskAttention userTaskAttention) {
        return taskTableMapper.selectUserAttention(userTaskAttention);
    }

    /**
     * 根据id查询任务标题
     *
     * @param id
     * @return
     */
    @Override
    public String selectTitle(long id) {
        return taskTableMapper.selectTitle(id);
    }

    @Override
    public List<TaskTable> selectCreateBy(TaskTable taskTable) {
        return taskTableMapper.selectCreateBy(taskTable);
    }

    @Override
    public List<TaskTable> selectTaskTableByName(String title) {
        return taskTableMapper.selectTaskTableByName(title);
    }

    @Override
    public List<TaskTable> selectTaskTableByProjectId(Integer pid) {
        return taskTableMapper.selectTaskTableByProjectId(pid);
    }

    /**
     * 获得对应的总数
     *
     * @return
     */
    @Override
    public Integer selectRows() {
        return taskTableMapper.selectRows();
    }

    @Override
    public List<TaskTable> selectInfoByProjectId(TaskTable taskTable) {
        return taskTableMapper.selectInfoByProjectId(taskTable);
    }

    @Override
    public List<TaskTable> selectTaskByTitleAndPid(TaskTable taskTable) {
        return taskTableMapper.selectTaskByTitleAndPid(taskTable);
    }

    @Override
    public List<TaskTable> selectTaskByUserId(Long userId) {
        List<TaskTable> taskTableList = taskTableMapper.selectTaskByUserId(userId);
        return taskTableList;
    }

    @Override
    public void updateWarnInfo(TaskSubnitPlan taskSubnitPlan) {
        if (!MyUtils.isEmpty(taskSubnitPlan.getWarnDays())) {
            taskTableMapper.updateWarnInfo(taskSubnitPlan);
        }
    }

    @Override
    public List<TaskTable> selectCountTask(TaskTable taskTable) {
        return taskTableMapper.selectCountTask(taskTable);
    }

    @Override
    public List<TaskTableVo> selectTaskVos(TaskTableVo taskTableVo) {
        return taskTableMapper.selectTaskVos(taskTableVo);
    }

    @Override
    public List<TaskTable> selectNewInfo(TaskTable taskTable) {
        return taskTableMapper.selectNewInfo(taskTable);
    }

    @Override
    public List<TaskChange> selectTaskChanes(Integer currentId) {
        List<TaskChange> list = new ArrayList<>();
        TaskPostpone postpone = new TaskPostpone();
        TaskChange taskChange = new TaskChange();
        // 任务变更
        AuditFlowCurrent auditFlowCurrent = new AuditFlowCurrent();
        auditFlowCurrent.setCurrentId(currentId);
        Integer tId = auditFlowCurrentService.selectApplyIdByCurrentId(currentId);
        if (tId == null) {
            tId = 0;
        }
        // 变更前记录
        postpone.setCurrentId(currentId);
        postpone.setIsNew(0);
        List<TaskPostpone> list1 = taskPostponeService.selectTaskChange(postpone);
        // 变更后记录
        postpone.setIsNew(1);
        List<TaskPostpone> list2 = taskPostponeService.selectTaskChange(postpone);
        taskChange.settId(tId);
        for (TaskPostpone t1 : list1) {
            taskChange.setStartTime(t1.getStartTime());
            taskChange.setEndTime(t1.getEndTime());
            taskChange.setChargeId(t1.getChargeId());
            taskChange.setChargeName(userService.getName(t1.getChargeId()));
            taskChange.setPeriod(t1.getPeriod());
            taskChange.setParticiants(t1.getParticiants());
            taskChange.setParticiantsName(selectParticiants(t1.getParticiants()));
            taskChange.setCreateChangeUser(t1.getCreateChangeUser());
            taskChange.setIsPostpone(t1.getIsPostpone());
            taskChange.setMemo(t1.getMemo());
        }
        for (TaskPostpone t2 : list2) {
            taskChange.setNewStartTime(t2.getStartTime());
            taskChange.setNewEndTime(t2.getEndTime());
            taskChange.setNewPeriod(t2.getPeriod());
            taskChange.setNewChargeId(t2.getChargeId());
            if (t2.getChargeId() != null) {
                taskChange.setNewChargeName(userService.getName(t2.getChargeId()));
            }
            taskChange.setNewParticiants(t2.getParticiants());
            taskChange.setNewParticiantsName(selectParticiants(t2.getParticiants()));
            taskChange.setNewCreateChangeUser(t2.getCreateChangeUser());
            taskChange.setNewIsPostpone(t2.getIsPostpone());
            taskChange.setNewMemo(t2.getMemo());
        }
        list.add(taskChange);
        return list;
    }

    public String selectParticiants(String p) {
        String people = "";
        if (p != null && p.length() > 0) {
            String[] arr = p.split(",");
            for (int i = 0; i < arr.length; i++) {
                int userId = Integer.parseInt(arr[i]);
                people = people + userService.getName(userId) + ",";
            }
            if (people.length() > 0) {
                people = people.substring(0, people.length() - 1);
            }
        }
        return people;
    }

    /**
     * 任务中止
     *
     * @param taskTable
     * @return
     */
    @Override
    public int editTask(TaskTable taskTable) {
        taskTable.settId(taskTable.gettId());
        Integer chargeId = taskTableMapper.selectChargePeopleId(taskTable.gettId().intValue());
        int a = 0;
        TaskStopMone taskStopMone = new TaskStopMone();
        //获得终止的原因
        String stopMemo = taskTable.getStopMone();
        String createBy = "";
        long taskChargeId = 0;
        String finishFlag = "";
        Integer projectId = 0;
        List<TaskTable> taskLists = taskTableMapper.selectTaskInfos(taskTable.gettId());
        for (TaskTable t : taskLists) {
            createBy = t.getCreateBy();
            taskChargeId = t.getChargepeopleId();
            finishFlag = t.getTaskFinishflag();
            projectId = t.getProjectId().intValue();
        }
        long nowUserId = ShiroUtils.getUserId();
        String firstUserId = taskTable.getFirstUserId();
        if (!finishFlag.equals(ManagerConstant.TASK_NOT_STOP)) {
            // 满足其中一个条件，则可以不用进行审批，直接进行更新状态
            if (ShiroUtils.isNotNull(firstUserId) && firstUserId.equals(ManagerConstant.TASK_APPROVAL)) {
                taskTable.setStopUser(sysUserService.getName(nowUserId));
                taskTable.setTaskFinishflag("2");
                taskTable.setUpdateTime(new Date());
                a = taskTableMapper.updateTaskTableByFlag(taskTable);
            } else {
                taskTable.setFlowId("8");
                taskTable.setCreateBy(ShiroUtils.getUserId() + "");
                a = auditFlowCurrentService.insertAuditFlowCurrents(taskTable);
                // 表示该任务已经在任务中止审批中，并且没有审批结束
                taskTable.setTaskFinishflag("6");
                taskTable.setUpdateTime(new Date());
                a = taskTableMapper.updateTaskTableByFlag(taskTable);
            }
            taskTable.settId(taskTable.gettId());
            finishFlag = taskTableMapper.selectFinishFlagById(taskTable);
        }
        if (stopMemo != null) {
            taskStopMone.settId(taskTable.gettId().intValue());
            taskStopMone.setStopMone(stopMemo);
            taskStopMone.setCreateBy(ShiroUtils.getUserId() + "");
            taskStopMone.setCreateTime(new Date());
            taskStopMoneService.insertTaskStopMone(taskStopMone);
        }

        return a;
    }

    /**
     * 任务完成操作
     *
     * @param taskTable
     * @param sysFileInfo1
     * @return
     */
    @Override
    public int accomplish(TaskTable taskTable, SysFileInfo sysFileInfo1) {
        String[] fileIds = {};
        if (sysFileInfo1.getFileIds() != null && !"".equals(sysFileInfo1.getFileIds())) {
            fileIds = sysFileInfo1.getFileIds().split(",");
        }
        for (String s : fileIds) {
            SysFileInfo sysFileInfo = new SysFileInfo();
            sysFileInfo.setFileId(Long.parseLong(s));
            sysFileInfo.setWorkId(Math.toIntExact(taskTable.gettId()));
            sysFileInfo.setFileType(0);
            fileInfoService.updateFileInfo(sysFileInfo);
        }
        taskTable.settId(taskTable.gettId());
        Integer charge = taskTableMapper.selectChargePeopleId(taskTable.gettId().intValue());//获得项目的负责人
        if (charge == null) {
            charge = 0;
        }
        int a = 0;
        String createBy = "";
        long taskChargeId = 0;
        String finishFlag = "";
        Integer projectId = 0;
        List<TaskTable> taskLists = taskTableMapper.selectTaskInfos(taskTable.gettId());
        for (TaskTable t : taskLists) {
            createBy = t.getCreateBy();
            taskChargeId = t.getChargepeopleId();
            finishFlag = t.getTaskFinishflag();
            projectId = t.getProjectId().intValue();
        }
        String firstUserId = taskTable.getFirstUserId();
        //任务完成审批，临时任务是发起人审批，项目任务项目负责人、产品经理审批
        long nowUserId = ShiroUtils.getUserId();
        if (!finishFlag.equals(ManagerConstant.TASK_NOT_FINISH)) {
            if (ShiroUtils.isNotNull(firstUserId) && firstUserId.equals(ManagerConstant.TASK_APPROVAL)) {
                taskTable.setTaskFinishflag("1");
                taskTable.setUpdateTime(new Date());
                a = taskTableMapper.updateTaskTableByFlag(taskTable);
            } else {
                taskTable.setFlowId("9");
                taskTable.setCreateBy(ShiroUtils.getUserId() + "");
                a = auditFlowCurrentService.insertAuditFlowCurrents(taskTable);
                taskTable.setUpdateTime(new Date());
                //将表示更新为5,表示该条任务正处于完成审批中
                taskTable.setTaskFinishflag("5");
                a = taskTableMapper.updateTaskTableByFlag(taskTable);
            }
            taskTable.settId(taskTable.gettId());
            finishFlag = taskTableMapper.selectFinishFlagById(taskTable);
        }
        return a;
    }


    @Override
    public List<TaskTable> selectTaskInfos(long tId) {
        return taskTableMapper.selectTaskInfos(tId);
    }

    /**
     * 任务变更审批
     *
     * @param taskTable
     * @param taskPostpone
     * @return
     */
    @Override
    public int changePeriod(TaskTable taskTable, TaskPostpone taskPostpone) {
        // 根据任务id获取开始时间
        taskTable.settId(taskTable.gettId());
        taskTable.setUpdateTime(new Date());
        int a = 0;
        String f1 = taskTable.getFlowId();
        if (f1 == null) {
            f1 = "13";
        }
        Integer flowId = Integer.parseInt(f1);
        //获得项目的负责人,这里的项目负责人指的是技术的负责人
        Integer chargeId = taskTableMapper.selectTechniqueUserId(taskTable.gettId().intValue());
        if (chargeId == null) {
            chargeId = 0;
        }
        String createBy = "";
        long taskChargeId = 0;
        String finishFlag = "";
        Integer projectId = 0;
        List<TaskTable> taskLists = taskTableMapper.selectTaskInfos(taskTable.gettId());
        for (TaskTable t : taskLists) {
            createBy = t.getCreateBy();
            taskChargeId = t.getChargepeopleId();
            finishFlag = t.getTaskFinishflag();
            projectId = t.getProjectId().intValue();
        }
        Date startTime = taskTableMapper.getStartTime(taskTable);
        if (DateUtils.dateToString(taskPostpone.getStartTime()) == null) {
            // 表示用户只修改了开始时间，需要进行延期的时间变更
            taskPostpone.setStartTime(startTime);
        }
        TaskUser taskUser = new TaskUser();
        taskUser.settId(taskTable.gettId());
        // 获得任务的参与人
        List<TaskUser> listUser = taskUserService.selectTaskUserList(taskUser);
        // 在第一次变更前，将任务的原始数据存入到数据表中，
        insertInfosBytId(taskTable.gettId(), listUser);
        TaskTable t2 = new TaskTable();
        t2.settId(taskTable.gettId());
        List<TaskTable> list = taskTableMapper.selectNewInfo(t2);
        if (f1.equals(ManagerConstant.TASK_NOT_PUTOFF)) {
            // 无顺延
            taskPostpone.setIsPostpone(0);
        }
        if (f1.equals(ManagerConstant.TASK_PUTOFF)) {
            // 顺延
            taskPostpone.setIsPostpone(1);
        }
        // 将变更后的数据存入到变更记录表中
        taskPostpone.setCreateChangeUser(userService.getName(ShiroUtils.getUserId()));
        taskPostpone.setChargeId(taskTable.getChargepeopleId().intValue());
        taskPostpone.setCreateTime(new Date());
        taskPostpone = comparaTo(taskTable, taskPostpone, listUser, list, Integer.parseInt(f1));
        long nowUserId = ShiroUtils.getUserId();
        String firstUserId = taskTable.getFirstUserId();
        if (!finishFlag.equals(ManagerConstant.TASK_CHANGE)) {
            if (ShiroUtils.isNotNull(firstUserId) && firstUserId.equals(ManagerConstant.TASK_APPROVAL)) {
                taskTable.setTaskOverdueState("1");
                taskTable.setTaskFinishflag("0");
                taskTable.setUpdateTime(new Date());
                a = taskTableMapper.updateTaskTableByFlag(taskTable);
                // 添加原始数据到变更表中
                taskPostpone.setIsNew(1);
                // 不用审批的情况
                taskPostpone.setState(1);
                // 更新参与人表和任务表
                notApproval(taskTable, flowId);
            } else {
                taskTable.setFlowId("12");
                taskTable.setCreateBy(ShiroUtils.getUserId() + "");
                a = auditFlowCurrentService.insertAuditFlowCurrents(taskTable);
                // 审批流id
                Integer currentId = ShiroUtils.getCurrentId();
                // 也可能没有审批流
                taskPostpone.setCurrentId(currentId);
                taskPostpone.setIsNew(1);
                taskPostpone.setState(0);
                // 表示该任务已经在任务中止审批中，并且没有
                taskTable.setTaskFinishflag("7");
                taskTable.setUpdateTime(new Date());
                a = taskTableMapper.updateTaskTableByFlag(taskTable);
            }
            // 如果周期没变那么移除周期信息
            List<TaskTable> tables = taskTableService.selectTaskByTid2(taskTable);
            if (tables != null && tables.get(0).getPeriod().equals(taskPostpone.getPeriod())) {
                taskPostpone.setPeriod(null);
            }
            // 开始以及结束时间没变移除时间
            if (tables != null && tables.get(0).getStartTime().equals(taskPostpone.getStartTime()) && tables.get(0).getEndTime().equals(taskPostpone.getEndTime())) {
                taskPostpone.setStartTime(null);
                taskPostpone.setEndTime(null);
            }
            // 添加变更后的数据到表中
            taskPostponeService.insertTaskPostpone(taskPostpone);
            taskTable.settId(taskTable.gettId());
            finishFlag = taskTableMapper.selectFinishFlagById(taskTable);
        }
        // 将获得到的值存入全局变量中
        if (flowId.toString().equals(ManagerConstant.TASK_PUTOFF)) {
            ShiroUtils.setValue(taskPostpone.getChargeId(), taskPostpone.getParticiants(), 1);
        }
        if (flowId.toString().equals(ManagerConstant.TASK_NOT_PUTOFF)) {
            ShiroUtils.setValue(taskPostpone.getChargeId(), taskPostpone.getParticiants(), 0);
        }
        return a;
    }

    /**
     * 当不用审批，执行该方法
     *
     * @param taskTable
     * @param flowId
     */
    public int notApproval(TaskTable taskTable, int flowId) {
        int a = 0;
        long tId = taskTable.gettId();
        // 获得负责人
        Long chargePeopleId = taskTable.getChargepeopleId();
        if (chargePeopleId != null) {
            taskTable.setChargepeopleId(chargePeopleId);
        }
        taskTable.setUpdateTime(new Date());
        taskTable.setUpdateBy(ShiroUtils.getUserId() + "");
        taskTable.setTaskFinishflag("0");
        if (flowId == Integer.parseInt(ManagerConstant.TASK_PUTOFF)) {
            // 执行顺延接口
            taskPostponeService.taskPostpone(taskTable);
        }
        a = taskTableMapper.updateTaskTable(taskTable);
        //更新projectTaskTable表中内容
        updateContent(tId, chargePeopleId);
        return a;
    }

    public void updateContent(Long tId, Long chargePeopleId) {
        ProjectTaskTable projectTaskTable = new ProjectTaskTable();
        TaskTable taskTable = new TaskTable();
        TaskUser taskUser = new TaskUser();
        if (chargePeopleId != null) {
            projectTaskTable.setEventId(tId.intValue());
            projectTaskTable.setTypeId(0);
            projectTaskTableService.deleteInfos(projectTaskTable);
            taskTable.settId(tId);
            List<TaskTable> list = taskTableMapper.selectTaskByTid2(taskTable);
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


    public TaskPostpone comparaTo(TaskTable taskTable, TaskPostpone taskPostpone, List<TaskUser> listUser, List<TaskTable> list, int f1) {
        int x = 0, y = 0, w = 0;// 分别代表着时间变更、负责人变更、参与人变更，当该值等于1时，表示该对应状态发生了改变
        String p = "";
        for (TaskUser t3 : listUser) {
            p = p + t3.getUserId() + ",";
        }
        if (p.length() > 0) {
            p = p.substring(0, p.length() - 1);
        }
        for (TaskTable t : list) {
            // 当三个都数据都跟原来的一样时，此时不会将空的数据插入到数据库
            if (DateUtils.getTimelong(DateUtils.dateToString(t.getStartTime())) == DateUtils.getTimelong(DateUtils.dateToString(taskTable.getStartTime()))) {
                if (DateUtils.getTimelong(DateUtils.dateToString(t.getEndTime())) == DateUtils.getTimelong(DateUtils.dateToString(taskTable.getEndTime()))) {
                    if (t.getPeriod() == taskTable.getPeriod()) {
                        taskPostpone.setStartTime(null);
                        taskPostpone.setPeriod(null);
                        taskPostpone.setEndTime(null);
                    } else {
                        x = 1;
                    }
                } else {
                    x = 1;
                }
            } else {
                x = 1;
            }
            Integer c1 = t.getChargepeopleId().intValue();
            Integer c2 = taskTable.getChargepeopleId().intValue();
            if (c1.equals(c2)) {
                taskPostpone.setChargeId(null);
            } else {
                y = 1;
            }
        }
        // 生成任务变更类型
        taskPostpone = selectType(taskPostpone, f1, x, y, w);
        return taskPostpone;
    }

    /**
     * f1=14表示顺延
     *
     * @param taskPostpone
     * @param f1
     * @param x
     * @param y
     * @param w
     * @return
     */
    public TaskPostpone selectType(TaskPostpone taskPostpone, int f1, int x, int y, int w) {
        if (f1 == Integer.parseInt(ManagerConstant.TASK_PUTOFF)) {
            if (x == 1) {
                if (y == 1) {
                    if (w == 1) {
                        taskPostpone.setType("顺延-时间-负责人-参与人变更");
                    } else {
                        taskPostpone.setType("顺延-时间-负责人变更");
                    }
                } else if (w == 1) {
                    taskPostpone.setType("顺延-参与人变更");
                } else {
                    taskPostpone.setType("顺延");
                }
            } else if (y == 1) {
                if (w == 1) {
                    taskPostpone.setType("顺延-负责人-参与人变更");
                } else {
                    taskPostpone.setType("顺延-负责人变更");
                }
            } else if (w == 1) {
                taskPostpone.setType("顺延-参与人变更");
            } else {
                taskPostpone.setType("顺延");
            }
        }
        if (f1 == Integer.parseInt(ManagerConstant.TASK_NOT_PUTOFF)) {
            if (x == 1) {
                if (y == 1) {
                    if (w == 1) {
                        taskPostpone.setType("时间-负责人-参与人变更");
                    } else {
                        taskPostpone.setType("时间-负责人变更");
                    }
                } else if (w == 1) {
                    taskPostpone.setType("时间-参与人变更");
                } else {
                    taskPostpone.setType("时间变更");
                }
            } else if (y == 1) {
                if (w == 1) {
                    taskPostpone.setType("负责人-参与人变更");
                } else {
                    taskPostpone.setType("负责人变更");
                }
            } else if (w == 1) {
                taskPostpone.setType("参与人变更");
            }
        }
        return taskPostpone;
    }

    @Override
    public List<TaskTable> selectAttentionlist(TaskTable taskTable) {
        return taskTableMapper.selectAttentionlist(taskTable);
    }

    @Override
    public void upTaskOverdueState(Integer state, Long id) {
        taskTableMapper.upTaskOverdueState(state, id);
    }

    /**
     * 查询任务的原始数据
     *
     * @param tId
     * @param listUser
     */
    public void insertInfosBytId(long tId, List<TaskUser> listUser) {
        String particiants = "";
        for (TaskUser t : listUser) {
            particiants = particiants + t.getUserId() + ",";
        }
        if (particiants.length() > 0) {
            particiants = particiants.substring(0, particiants.length() - 1);
        }
        TaskPostpone taskPostpone = new TaskPostpone();
        TaskTable taskTable = new TaskTable();
        taskPostpone.setIsNew(2);
        taskPostpone.settId(tId);
        List<TaskPostpone> lsit2 = taskPostponeService.selectTaskChange(taskPostpone);
        if (lsit2.size() < 1) {
            taskTable.settId(tId);
            List<TaskTable> list = taskTableMapper.selectNewInfo(taskTable);
            for (int i = 0; i < list.size(); i++) {
                taskPostpone.setChargeId(list.get(i).getChargepeopleId().intValue());
                taskPostpone.setStartTime(list.get(i).getStartTime());
                taskPostpone.setEndTime(list.get(i).getEndTime());
                taskPostpone.setPeriod(list.get(i).getPeriod());
                taskPostpone.setParticiants(particiants);
                taskPostpone.setCreateTime(new Date());
                taskPostpone.setState(1);
                int a = taskPostponeService.insertTaskPostpone(taskPostpone);
            }
        }

    }

    @Override
    public Integer selectTechniqueUserId(Integer tId) {
        return taskTableMapper.selectTechniqueUserId(tId);
    }

    @Override
    public Integer selectTaskTableListCount(TaskTable taskTable) {
        return taskTableMapper.selectTaskTableListCount(taskTable);
    }

    /**
     * 根据任务id
     *
     * @param tId
     * @return
     */
    @Override
    public List<SysProjectTableVo> queryProjectBytId(Long tId) {
        return taskTableMapper.queryProjectBytId(tId);
    }

    @Override
    public Integer isAttention(Long tId) {
        Integer isAttention = 0;
        UserTaskAttention u = new UserTaskAttention();
        u.settId(tId);
        u.setUserId(ShiroUtils.getUserId());
        Integer state = taskTableService.selectUserAttention(u);
        if (state != null && state == 1) {
            isAttention = 1;
        }
        return isAttention;
    }

    @Override
    public int saveTask(TaskTable taskTable) {
        int a = 0;
        Integer userId = ShiroUtils.getUserId().intValue();
        taskTable.setCreateBy(ShiroUtils.getUserId().toString());
        taskTable.setTaskFinishflag("-1");
        taskTable.setCreateTime(new Date());
        a = taskTableService.insertTaskTable(taskTable);
        // 获得所属项目的负责人--技术负责人
        List<SysProjectTableVo> voList = taskTableService.queryProjectBytId(taskTable.gettId());
        Integer projectType = 0;
        Integer chargePeople = 0;
        Integer techniquePeople = 0;
        if (ShiroUtils.isNotEmpty(voList)) {
            for (SysProjectTableVo projectTableVo : voList) {
                projectType = projectTableVo.getProjectType();
                chargePeople = projectTableVo.getChargepeopleId();
                techniquePeople = projectTableVo.getTechniquePeople();
            }
        }
        if (projectType == 0) {
            // 项目的负责人不等于创建人的话，进入审批流,科研项目
            if (!chargePeople.equals(userId)) {
                taskTable.setFirstUserId(taskTable.getFirstUserId());
                taskTable.setFlowId("1");// 任务发起审批
                auditFlowCurrentService.insertAuditFlowCurrents(taskTable);
            } else if (chargePeople.equals(userId)) {
                // 项目的负责人不等于创建人(当前用户)的话，直接不用审批
                taskTable.settId(taskTable.gettId());
                taskTable.setUpdateTime(new Date());
                taskTable.setTaskFinishflag("0");
                a = taskTableService.updateTaskTable(taskTable);
                auditCopyMsgService.insertCopysBytId(taskTable.getCcIds(), taskTable.gettId(), 1);
            }
        } else if (projectType == 1) {
            // 市场任务
            if (!techniquePeople.equals(userId)) {
                taskTable.setFirstUserId(taskTable.getFirstUserId());
                // 任务发起审批
                taskTable.setFlowId("1");
                a = auditFlowCurrentService.insertAuditFlowCurrents(taskTable);
            } else if (techniquePeople.equals(userId)) {
                taskTable.settId(taskTable.gettId());
                taskTable.setUpdateTime(new Date());
                taskTable.setTaskFinishflag("0");
                a = taskTableService.updateTaskTable(taskTable);
                auditCopyMsgService.insertCopysBytId(taskTable.getCcIds(), taskTable.gettId(), 1);
            }
        }
        // 获得任务负责人
        long chargePeopleId = taskTable.getChargepeopleId();
        taskUserService.insertTaskUsers(taskTable, chargePeopleId);
        return a;
    }

    @Override
    public int insertQushTask(TaskQuashVo vo) {
        // TaskTable taskTable = UserUtil.copy(vo);
        TaskTable taskTable = UserUtil.copyValue(vo);
        int a = this.saveTask(taskTable);
        if (a > 0) {
            Long tId = vo.gettId();
            // 删除原任务
            taskTableService.deleteTaskTableById(tId);
            // 删除原审批信息
            Map<String, Object> map = auditFlowCurrentMapper.selectBuinessId(ManagerConstant.APPROVAL_CREATE_TASK, tId.intValue());
            if (map != null && map.size() > 0) {
                Integer currentId = (Integer) map.get("currentId");
                if (currentId != null) {
                    auditFlowCurrentMapper.deleteMultipleTable(currentId);
                }
            }
        }
        return a;
    }

    @Override
    public List<Map<String, Object>> selectJoinProjectList(Map<String, Object> map) {
        return taskTableMapper.selectJoinProjectList(map);
    }

    @Override
    public Map<String, Object> selectTaskCount(Map<String, Object> map) {
        Map<String, Object> resultMap = new HashMap<>();
        Map<String,Object> param = new HashMap<>();
        param.put("startTime",map.get("startTime"));
        param.put("endTime",map.get("endTime"));
        param.put("chargepeopleId",map.get("chargepeopleId"));
        param.put("projectId",map.get("projectId"));
        // 参与的任务总数
        Long taskCount = taskTableMapper.selectTaskCount(param);
        if (taskCount == null) {
            taskCount = 0L;
        }
        resultMap.put("taskCount", taskCount);
        // 完成任务数
        param.put("taskFinishFlagList", Arrays.asList("1"));
        Long taskFinishCount = taskTableMapper.selectTaskCount(param);
        if (taskFinishCount == null) {
            taskFinishCount = 0L;
        }
        resultMap.put("taskFinishCount", taskFinishCount);
        // 未完成任务数
        param.put("taskFinishFlagList", Arrays.asList("0","5","6","7"));
        Long taskStopCount = taskTableMapper.selectTaskCount(param);
        if (taskStopCount == null) {
            taskStopCount = 0L;
        }
        resultMap.put("taskStopCount", taskStopCount);
        return resultMap;
    }
}

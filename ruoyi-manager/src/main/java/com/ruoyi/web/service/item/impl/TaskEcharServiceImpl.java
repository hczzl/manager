package com.ruoyi.web.service.item.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.TaskFinishFlagEnum;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.DateUtil;
import com.ruoyi.web.domain.TaskEchar;
import com.ruoyi.web.domain.TaskEcharQueryInfo;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.mapper.item.SysYearMonthMapper;
import com.ruoyi.web.service.item.TaskEcharService;
import com.ruoyi.web.service.item.TaskTableService;
import com.ruoyi.web.util.constant.ManagerConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

/**
 * @author Suqz
 * @version 1.0
 * @date 2020/5/5 22:33
 */
@Service
public class TaskEcharServiceImpl implements TaskEcharService {
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private SysYearMonthMapper sysYearMonthMapper;
    @Autowired
    private static final String TASK_STATUS = "taskStatus";
    private static final String TASK_OVER_STATUS = "taskOverStatus";
    @Value("${server.port}")
    String serverPortNum;

    @Override
    public AjaxResult getTaskEchartsData(TaskEcharQueryInfo queryInfo) {
        List list = new ArrayList();
        TaskEchar taskEchar = new TaskEchar();
        TaskEchar taskOverEchar = new TaskEchar();
        // 1.获取任务数据
        List<TaskTable> taskTables = getAllTaskInfo(queryInfo);
        if (taskTables == null || taskTables.size() == 0) {
            return AjaxResult.success("暂无任务", new ArrayList<>());
        }
        // // 2.获取指定时间内的任务
        String startTime = DateUtils.convert2String(queryInfo.getStartTimes(), "");
        String[] arr = startTime.split("-");
        int paramyear = Integer.parseInt(arr[0]);
        int nowYear = DateUtil.selectYear();
        int month = queryInfo.getMonth();
        if (paramyear == nowYear && month == 13) {
            // 若获取的是今年，则从表中获取开始时间和结束时间，且判断是不是当前月
            StringBuilder builder = new StringBuilder();
            builder.append(nowYear).append("-");
            // 表中数据今年第一天:yyyy-MM-dd
            String firstDay = sysYearMonthMapper.selectMinDate(builder.toString());
            // 表中数据今年最后一天
            String lastDay = sysYearMonthMapper.selectMaxDate(builder.toString());
            // 当前月份最后一天
            String currentDay = DateUtil.getCurrentMonthLastDay();
            // 当前月份为最大月份的情况
            if (lastDay.equals(currentDay)) {
                // 获取上个月的最后一天
                // String lastMonthLastDay = DateUtil.getLastMonthLastDay();
                // 获取当前月第一天
                String currentMonthFirstDay = DateUtil.getCurrentMonthFirstDay();
                if (firstDay.compareTo(currentMonthFirstDay) < 0) {
                    lastDay = currentMonthFirstDay;
                } else {
                    return AjaxResult.success("执行成功", list);
                }
            }
            queryInfo.setStartTime(DateUtils.parseDate(firstDay));
            queryInfo.setEndTime(DateUtils.parseDate(lastDay));
        } else {
            // 若获取的不是今年或者统计月度的时候，则直接根据参数时间获取数据
            queryInfo.setStartTime(DateUtils.parseDate(DateUtils.convert2String(queryInfo.getStartTimes(), "")));
            queryInfo.setEndTime(DateUtils.parseDate(DateUtils.convert2String(queryInfo.getEndTimes(), "")));
        }
        taskTables = (List<TaskTable>) getDataByTime(taskTables, queryInfo);
        if (taskTables.size() == 0) {
            return AjaxResult.success("暂无任务", new ArrayList<>());
        }
        // 3.获取任务完成状态的统计数据
        Map<String, List> taskEcharMap = getStatsInfo(taskTables, taskEchar, TASK_STATUS);
        // 4.获取是否逾期的统计数据
        Map<String, List> taskOverEcharMap = getStatsInfo(taskTables, taskOverEchar, TASK_OVER_STATUS);
        // 5.将状态的个数为0的数据导入
        list.add(setStatusCountIsZeroInfo(new String[]{"执行中", "已完成", "已中止", "申请完成", "申请中止", "申请变更"}, taskEchar, taskEcharMap));
        list.add(setStatusCountIsZeroInfo(new String[]{"逾期(未完成)", "逾期(完成)", "未逾期"}, taskOverEchar, taskOverEcharMap));
        return AjaxResult.success("执行成功", list);
    }

    public List<TaskTable> getAllTaskInfo(TaskEcharQueryInfo queryInfo) {
        List<TaskTable> taskTables = taskTableService.selectTaskByUserId(queryInfo.getUserId());
        return taskTables;
    }

    public <T> Object getDataByTime(List<T> list, Object query) {
        String typeName = "";
        if (list.size() > 0) {
            typeName = list.get(0).getClass().getSimpleName();
        }
        if (ManagerConstant.TASK_NAME.equals(typeName)) {
            TaskEcharQueryInfo queryInfo = (TaskEcharQueryInfo) query;
            List<TaskTable> tables = (List<TaskTable>) list;
            tables = tables.stream()
                    .filter(item -> item.getTaskFinishflag() != null)
                    .filter(item -> item.getStartTime() != null)
                    .filter(item -> item.getEndTime() != null)
                    .filter(item -> !TaskFinishFlagEnum.COMMIT.getCode().equals(item.getTaskFinishflag()))
                    .filter(
                            item ->
                                    (item.getStartTime().compareTo(queryInfo.getEndTime()) < 0 && item.getEndTime().compareTo(queryInfo.getEndTime()) >= 0)
                                            || (item.getStartTime().compareTo(queryInfo.getStartTime()) >= 0 && item.getEndTime().compareTo(queryInfo.getEndTime()) < 0)
                                            || (item.getStartTime().compareTo(queryInfo.getStartTime()) <= 0 && item.getEndTime().compareTo(queryInfo.getStartTime()) >= 0))
                    .sorted(Comparator.comparing(TaskTable::gettId))
                    .collect(Collectors.toList());
            return tables;
        }
        return null;
    }

    public Map<String, List> getStatsInfo(List<TaskTable> taskTables, TaskEchar taskEchar, String taskType) {
        // 逾期统计不包括已中止的
        if (TASK_OVER_STATUS.equals(taskType)) {
            taskTables = taskTables.stream()
                    .filter(item -> !TaskFinishFlagEnum.STOP.getCode().equalsIgnoreCase(item.getTaskFinishflag()))
                    .collect(Collectors.toList());
        }
        taskEchar.setTaskTotal(taskTables.size());
        Map<String, List> taskEcharMap = new HashMap<>(10);
        AtomicReference<String> taskStatus = new AtomicReference<>("");
        taskTables.stream()
                .collect(Collectors.groupingBy(
                        item -> {
                            if (TASK_STATUS.equals(taskType)) {
                                return item.getTaskFinishflag();
                            } else if (TASK_OVER_STATUS.equals(taskType)) {
                                String flag = "false";
                                if (TaskFinishFlagEnum.COMPLETE.getCode().equals(item.getTaskFinishflag())) {
                                    flag = "true";
                                }
                                return item.getTaskOverdueState() + flag;
                            } else {
                                return null;
                            }
                        }
                        , Collectors.counting()))
                .forEach((statu, count) -> {
                    //状态
                    if (TASK_STATUS.equals(taskType)) {
                        switch (statu) {
                            case "0":
                                taskStatus.set("执行中");
                                break;
                            case "1":
                                taskStatus.set("已完成");
                                break;
                            case "2":
                                taskStatus.set("已中止");
                                break;
                            case "5":
                                taskStatus.set("申请完成");
                                break;
                            case "6":
                                taskStatus.set("申请中止");
                                break;
                            case "7":
                                taskStatus.set("申请变更");
                                break;
                            default:
                                taskStatus.set("其它");
                        }
                    } else if (TASK_OVER_STATUS.equals(taskType)) {
                        switch (statu) {
                            case "0false":
                                taskStatus.set("逾期(未完成)");
                                break;
                            case "0true":
                                taskStatus.set("逾期(完成)");
                                break;
                            default:
                                taskStatus.set("未逾期");
                        }
                    }
                    if (taskEcharMap.get(taskStatus.get()) != null) {
                        count = count + (Long) taskEcharMap.get(taskStatus.get()).get(0);
                        taskEcharMap.put(taskStatus.get(), Arrays.asList(count, countTaskStatusRate(taskEchar, count)));
                    } else {
                        taskEcharMap.put(taskStatus.get(), Arrays.asList(count, countTaskStatusRate(taskEchar, count)));
                    }
                });
        return taskEcharMap;
    }

    public double countTaskStatusRate(TaskEchar taskEchar, Long count) {
        return ((double) count * 100 / taskEchar.getTaskTotal());
    }

    /**
     * 插入数量为0的相关数据
     *
     * @param statusArray
     * @param taskEchar
     * @return
     */
    public TaskEchar setStatusCountIsZeroInfo(String[] statusArray, TaskEchar taskEchar, Map<String, List> taskEcharMap) {
        Map<String, List> taskAllStatusEcharMap = new LinkedHashMap<>(10);
        for (int i = 0; i < statusArray.length; i++) {
            taskAllStatusEcharMap.put(statusArray[i], Arrays.asList(0, 0.00));
        }
        taskEcharMap.forEach((k, v) -> {
            taskAllStatusEcharMap.replace(k, v);
        });
        taskAllStatusEcharMap.forEach((k, v) -> {
            List<String> status = taskEchar.getStatus();
            List<Integer> taskStatusCount = taskEchar.getTaskStatusCount();
            List<Double> taskStatusRate = taskEchar.getTaskStatusRate();
            if (status == null || taskStatusCount == null || taskStatusRate == null) {
                status = new ArrayList<>();
                taskStatusCount = new ArrayList<>();
                taskStatusRate = new ArrayList<>();
            }
            status.add(k);
            taskStatusCount.add(Integer.parseInt(v.get(0).toString()));
            taskStatusRate.add(Double.parseDouble(v.get(1).toString()));
            taskEchar.setStatus(status);
            taskEchar.setTaskStatusCount(taskStatusCount);
            taskEchar.setTaskStatusRate(taskStatusRate);
        });
        return taskEchar;
    }
}

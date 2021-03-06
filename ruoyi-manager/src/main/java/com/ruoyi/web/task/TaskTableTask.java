package com.ruoyi.web.task;

import com.ruoyi.common.enums.TaskFinishFlagEnum;
import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.mapper.item.TaskTableMapper;
import com.ruoyi.web.service.item.TaskTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 遍历任务表，更新各个任务的逾期相关情况
 *
 * @author Suqz
 * @version 1.0
 * @date 2020/1/14 15:06
 */
@Component
@Async
public class TaskTableTask {
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private TaskTableMapper taskTableMapper;
    /**
     * 允许延后判断的时间
     */
    private final static Integer OVER_DAY = 2;

    /**
     * 每小时执行一次
     *
     * @Scheduled(cron = "0 0 0/1 1/1 * ?")
     */
    @Scheduled(cron = "0 0/30 * * * ?")
    public void uptateTaskOverState() {
        try {
            int page = 1;
            int limit = 100;
            Map<String, Object> map = new HashMap<>();
            // 中止的任务，统一将其任务逾期状态改为未逾期状态
            taskTableMapper.modifyOverdueState();
            while (true) {
                map.put("start", (page - 1) * limit);
                map.put("end", limit);
                // 排除了发起任务但未审批通过的、任务撤回的、任务中止的数据
                List<Map<String, Object>> resultList = taskTableMapper.selectPageTaskList(map);
                if (ShiroUtils.isEmpty(resultList)) {
                    break;
                }
                for (Map<String, Object> map2 : resultList) {
                    Integer tId = (Integer) map2.get("tId");
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    // 任务结束时间
                    Date endTime = (Date) map2.get("endTime");
                    endTime = Date.from(endTime.toInstant().atZone(ZoneId.systemDefault()).plusDays(OVER_DAY).toInstant());
                    String end = sdf.format(endTime);
                    endTime = sdf.parse(end);
                    // 当前时间
                    Date nowTime = new Date();
                    String now = sdf.format(nowTime);
                    nowTime = sdf.parse(now);
                    // 任务未完成，且当前时间大于任务的结束时间
                    if (endTime.compareTo(nowTime) < 0) {
                        taskTableService.upTaskOverdueState(0, tId.longValue());
                    } else {
                        taskTableService.upTaskOverdueState(1, tId.longValue());
                    }
                }
                page++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @Scheduled(cron = "0/20 * * * * ?")
     */
    public void taskOverTimeCheck() {
        List<TaskTable> allTaskTable = taskTableService.getAllTaskTable(new TaskTable());
        allTaskTable.stream()
                .filter(item ->
                        TaskFinishFlagEnum.STOP.getCode().equals(item.getTaskFinishflag())
                ).collect(Collectors.toList())
                .forEach(item -> {
                    taskTableService.upTaskOverdueState(1, item.gettId());//中止的任务都改为未逾期状态
                });
        ;
        allTaskTable.stream()
                // 执行中
                .filter(task -> TaskFinishFlagEnum.UNDONE.getCode().equals(task.getTaskFinishflag()))
                .collect(Collectors.toList())
                .forEach(item -> {
                    Date endDate = Date.from(item.getEndTime().toInstant().atZone(ZoneId.systemDefault()).plusDays(OVER_DAY).toInstant());
                    Date date = new Date();
                    if (endDate.compareTo(new Date()) <= 0) {
                        taskTableService.upTaskOverdueState(0, item.gettId());
                    } else {
                        taskTableService.upTaskOverdueState(1, item.gettId());
                    }
                });
    }
}

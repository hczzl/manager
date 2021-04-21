package com.ruoyi.web.service.item.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import com.ruoyi.common.enums.TaskFinishFlagEnum;
import com.ruoyi.web.domain.SysHolidays;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.mapper.SysHolidaysMapper;
import com.ruoyi.web.mapper.item.TaskTableMapper;
import com.ruoyi.web.service.item.TaskTableService;
import com.ruoyi.web.util.WorkTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.mapper.item.TaskPostponeMapper;
import com.ruoyi.web.domain.TaskPostpone;
import com.ruoyi.web.service.item.TaskPostponeService;
import com.ruoyi.common.core.text.Convert;

/**
 * 延期Service业务层处理
 *
 * @author ruoyi
 * @date 2019-08-27
 */
@Service
public class TaskPostponeServiceImpl implements TaskPostponeService {
    @Autowired
    private TaskPostponeMapper taskPostponeMapper;
    @Autowired
    private TaskTableMapper taskTableMapper;
    @Autowired
    private TaskTableService taskTableService;
    @Autowired
    private SysHolidaysMapper holidaysMapper;

    /**
     * 查询延期
     *
     * @param id 延期ID
     * @return 延期
     */
    @Override
    public TaskPostpone selectTaskPostponeById(Long id) {
        return taskPostponeMapper.selectTaskPostponeById(id);
    }

    /**
     * 查询延期列表
     *
     * @param taskPostpone 延期
     * @return 延期
     */
    @Override
    public List<TaskPostpone> selectTaskPostponeList(TaskPostpone taskPostpone) {
        return taskPostponeMapper.selectTaskPostponeList(taskPostpone);
    }

    /**
     * 新增延期
     *
     * @param taskPostpone 延期
     * @return 结果
     */
    @Override
    public int insertTaskPostpone(TaskPostpone taskPostpone) {

        return taskPostponeMapper.insertTaskPostpone(taskPostpone);
    }

    /**
     * 修改延期
     *
     * @param taskPostpone 延期
     * @return 结果
     */
    @Override
    public int updateTaskPostpone(TaskPostpone taskPostpone) {
        return taskPostponeMapper.updateTaskPostpone(taskPostpone);
    }

    /**
     * 删除延期对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTaskPostponeByIds(String ids) {
        return taskPostponeMapper.deleteTaskPostponeByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除延期信息
     *
     * @param id 延期ID
     * @return 结果
     */
    @Override
    public int deleteTaskPostponeById(Long id) {
        return taskPostponeMapper.deleteTaskPostponeById(id);
    }

    @Override
    public Integer selectMaxId(TaskPostpone taskPostpone) {
        return taskPostponeMapper.selectMaxId(taskPostpone);
    }

    @Override
    public List<TaskPostpone> selectById(TaskPostpone taskPostpone) {
        return taskPostponeMapper.selectById(taskPostpone);
    }

    /**
     * 根据任务id和是否延期状态来获取对应的信息
     *
     * @param taskPostpone
     * @return
     */
    @Override
    public List<TaskPostpone> selectTaskChange(TaskPostpone taskPostpone) {
        return taskPostponeMapper.selectTaskChange(taskPostpone);
    }

    /**
     * 任务顺延
     *
     * @param taskTable
     */
    @Override
    public void taskPostpone(TaskTable taskTable) {
        try {
            // 获取变更前的的原任务信息
            TaskTable taskTableInfo = taskTableMapper.selectTaskTableById(taskTable.gettId());
            // 用于记录点击顺延任务的开始时间（后面的任务要大于它的才能执行顺延）
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            // 获取当前任务的负责人以及参与人id
            List<Long> userIds = taskTableMapper.selectTaskUserId(taskTable.gettId());
            for (int idNum = 0; idNum < userIds.size(); idNum++) {
                // 获取我的任务的所有信息(idNum为0表示当前用户是负责人)
                List<TaskTable> mylists = new ArrayList<>();
                mylists.addAll(getAllTaskById(taskTableInfo, userIds.get(idNum), idNum == 0 ? true : false));
                // 获取所有假期以及周末的时间
                List<SysHolidays> sysHolidays = holidaysMapper.selectSysHolidaysList(new SysHolidays());
                // 存储每个任务原起止时间的日期
                List<LinkedHashMap> taskDays = new ArrayList<>();
                // 用于记录变更后所包含的节假日时间
                List<LinkedHashMap> newTaskDays = new ArrayList<>();
                // 存储任务原起止时间的天数
                AtomicInteger daysCount = new AtomicInteger();
                Date startTime = new Date();
                for (AtomicInteger i = new AtomicInteger(0); i.get() < mylists.size(); i.getAndIncrement()) {
                    if (mylists.get(i.get()).gettId().equals(taskTable.gettId())) {
                        mylists.get(i.get()).setStartTime(taskTable.getStartTime());
                        mylists.get(i.get()).setEndTime(taskTable.getEndTime());
                    }
                    // 获取所有假期以及周末的时间(备份，用于后面假期修改的执行)
                    List<SysHolidays> newSysHolidays = new ArrayList<>();
                    newSysHolidays.addAll(sysHolidays);
                    taskDays.add(new LinkedHashMap());
                    newTaskDays.add(new LinkedHashMap());
                    if (0 == i.get()) {
                        startTime = dateFormat.parse(LocalDate.parse(dateFormat.format(taskTable.getEndTime()), DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(1).toString());
                    }
                    // 插入原变更记录
                    addInitTaskPostpone(mylists.get(i.get()));
                    // 获取原来任务起止时间的天数
                    double ceil = Math.ceil(Double.parseDouble(mylists.get(i.get()).getPeriod())) - 1;
                    daysCount.set((int) ceil);
                    taskDays.get(i.get()).clear();
                    // 将当前任务的开始时间设置为上个任务的结束时间
                    mylists.get(i.get()).setStartTime(startTime);
                    // 将当前任务的结束时间设置为开始时间+原来的任务天数
                    mylists.get(i.get()).setEndTime(dateFormat.parse(LocalDate.parse(dateFormat.format(startTime), DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(daysCount.get()).toString()));
                    // 获取原开始任务的时间到变更后开始任务时间间的天数
                    WorkTimeUtil.getWorkTime(taskDays.get(i.get()), dateFormat.parse(LocalDate.parse(dateFormat.format(startTime), DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(1).toString()), mylists.get(i.get()).getEndTime());

                    // 如果遇到节假日日期会向后移
                    taskDays.get(i.get()).forEach((k, v) -> {
                        sysHolidays.forEach(day -> {
                            if (k.equals(day.getHolidays())) {
                                daysCount.getAndIncrement();
                                newTaskDays.get(i.get()).put(k, v);
                            }
                        });
                    });
                    // 用于记录变更后是否还存在新的节假日的天数，用于退出循环的判断依据
                    while (true) {
                        AtomicInteger count = new AtomicInteger(0);
                        // 将当前任务的开始时间设置为上个任务的结束时间
                        mylists.get(i.get()).setStartTime(startTime);
                        // 设置往后延期的结束时间（字符串->date->字符串->date） 如果遇到节假日日期会向后移，没有则不变
                        mylists.get(i.get()).setEndTime(dateFormat.parse(LocalDate.parse(dateFormat.format(startTime), DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(daysCount.get()).toString()));
                        // 获取原开始任务的时间到变更后开始任务时间间的天数
                        WorkTimeUtil.getWorkTime(taskDays.get(i.get()), mylists.get(i.get()).getStartTime(), mylists.get(i.get()).getEndTime());
                        // 将节假日中的日期中移除掉newTaskDays中存在的日期，以便于时间延后后的再次判断
                        newTaskDays.get(i.get()).forEach((k, v) -> {
                            Iterator<SysHolidays> iterator = newSysHolidays.iterator();
                            while (iterator.hasNext()) {
                                SysHolidays next = iterator.next();
                                if (next.getHolidays().equals(k)) {
                                    iterator.remove();
                                }
                            }
                        });
                        // 如果遇到节假日日期会向后移
                        taskDays.get(i.get()).forEach((k, v) -> {
                            newSysHolidays.forEach(day -> {
                                if (k.equals(day.getHolidays())) {
                                    count.getAndIncrement();
                                    daysCount.getAndIncrement();
                                    newTaskDays.get(i.get()).put(k, v);
                                }
                            });
                        });
                        // 如果重新执行后没有新的节假日那么退出循环
                        if (count.get() == 0) {
                            break;
                        }
                    }
                    // 更新任务
                    taskTable = mylists.get(i.get());
                    // 添加变更后的数据到表中
                    addTaskPostpone(taskTable);
                    List<SysHolidays> sysHoliday = holidaysMapper.selectSysHolidaysList(new SysHolidays());
                    checkStartDay(taskTable, sysHoliday);
                    // 更新时间
                    taskTable.setUpdateTime(new Date());
                    taskTableMapper.updateTaskTable(taskTable);
                    startTime = dateFormat.parse(LocalDate.parse(dateFormat.format(taskTable.getEndTime()), DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(1).toString());
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取当前任务后的任务
     *
     * @param taskTable
     * @param userId
     * @param isChargePeople
     * @return
     */
    public List<TaskTable> getAllTaskById(TaskTable taskTable, Long userId, Boolean isChargePeople) {
        List<TaskTable> tables = taskTableService.selectTaskByUserId(userId);
        TaskTable finalTaskTable = taskTable;
        tables = tables.stream()
                .filter(item -> !item.gettId().equals(taskTable.gettId()))
                .filter(item -> !TaskFinishFlagEnum.COMMIT.getCode().equals(item.getTaskFinishflag()))
                .filter(item -> !TaskFinishFlagEnum.COMPLETE.getCode().equals(item.getTaskFinishflag()))
                .filter(item -> !TaskFinishFlagEnum.STOP.equals(item.getTaskFinishflag()))
                .filter(
                        item -> (item.getStartTime().compareTo(finalTaskTable.getStartTime()) >= 0))
                .sorted(Comparator.comparing(TaskTable::getStartTime))
                .collect(Collectors.toList());
        if (!isChargePeople) {
            tables.stream()
                    .filter(item -> !item.gettId().equals(finalTaskTable.gettId()))
                    .collect(Collectors.toList());
        }
        return tables;
    }

    void checkStartDay(TaskTable taskTable, List<SysHolidays> sysHolidays) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String day = dateFormat.format(taskTable.getStartTime());
        List<SysHolidays> collect = sysHolidays.stream().filter(item -> day.equals(item.getHolidays())).collect(Collectors.toList());
        if (collect.size() != 0) {
            Date parse = dateFormat.parse(LocalDate.parse(dateFormat.format(taskTable.getStartTime()), DateTimeFormatter.ofPattern("yyyy-MM-dd")).plusDays(1).toString());
            taskTable.setStartTime(parse);
            checkStartDay(taskTable, sysHolidays);
        }
    }

    void addInitTaskPostpone(TaskTable taskTable) {
        // 添加变更后的数据到表中
        TaskPostpone taskPostpone = new TaskPostpone();
        taskPostpone.settId(taskTable.gettId());
        taskPostpone.setIsNew(2);
        taskPostpone.setStartTime(taskTable.getStartTime());
        taskPostpone.setChargeId(Integer.parseInt(taskTable.getChargepeopleId().toString()));
        taskPostpone.setEndTime(taskTable.getEndTime());
        taskPostpone.setState(1);
        taskPostpone.setPeriod(taskTable.getPeriod());
        taskPostpone.setCreateTime(new Date());
        taskPostponeMapper.insertInitTaskPostpone(taskPostpone);
    }

    void addTaskPostpone(TaskTable taskTable) {
        // 添加变更后的数据到表中
        TaskPostpone taskPostpone = new TaskPostpone();
        taskPostpone.setType("顺延时间变更");
        taskPostpone.settId(taskTable.gettId());
        taskPostpone.setIsNew(1);
        taskPostpone.setStartTime(taskTable.getStartTime());
        taskPostpone.setEndTime(taskTable.getEndTime());
        taskPostpone.setState(1);
        taskPostpone.setPeriod(taskTable.getPeriod());
        taskPostpone.setCreateTime(new Date());
        taskPostponeMapper.insertTaskPostpone(taskPostpone);
    }
}

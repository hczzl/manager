package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.TaskSubnitPlan;

import java.util.List;

/**
 * 提交进度Mapper接口
 *
 * @author ruoyi
 * @date 2019-08-20
 */
public interface TaskSubnitPlanMapper {

    List<TaskSubnitPlan> selectTaskSubnitPlanById(Long tId);

    List<TaskSubnitPlan> selectTaskSubnitPlanList(TaskSubnitPlan taskSubnitPlan);

    int insertTaskSubnitPlan(TaskSubnitPlan taskSubnitPlan);

    int updateTaskSubnitPlan(TaskSubnitPlan taskSubnitPlan);

    int deleteTaskSubnitPlanById(Long tId);

    int deleteTaskSubnitPlanByIds(String[] tIds);

    Integer selectMaxId(TaskSubnitPlan taskSubnitPlan);

    String selectMaxMemo(TaskSubnitPlan taskSubnitPlan);

    void updateTaskSubnitPlanByIds(TaskSubnitPlan taskSubnitPlan);
}

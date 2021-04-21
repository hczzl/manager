package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.TaskSubnitPlan;

import java.util.List;

/**
 * 提交进度Service接口
 *
 * @author ruoyi
 * @date 2019-08-20
 */
public interface TaskSubnitPlanService {
    /**
     * 查询提交进度
     *
     * @param tId 提交进度ID
     * @return 提交进度
     */
    List<TaskSubnitPlan> selectTaskSubnitPlanById(Long tId);

    /**
     * 查询提交进度列表
     *
     * @param taskSubnitPlan 提交进度
     * @return 提交进度集合
     */
    List<TaskSubnitPlan> selectTaskSubnitPlanList(TaskSubnitPlan taskSubnitPlan);

    /**
     * 新增提交进度
     *
     * @param taskSubnitPlan 提交进度
     * @return 结果
     */
    int insertTaskSubnitPlan(TaskSubnitPlan taskSubnitPlan);

    /**
     * 修改提交进度
     *
     * @param taskSubnitPlan 提交进度
     * @return 结果
     */
    int updateTaskSubnitPlan(TaskSubnitPlan taskSubnitPlan);

    /**
     * 批量删除提交进度
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteTaskSubnitPlanByIds(String ids);

    /**
     * 删除提交进度信息
     *
     * @param tId 提交进度ID
     * @return 结果
     */
    int deleteTaskSubnitPlanById(Long tId);

    /**
     * 根据max的id，得到最新的进度信息
     *
     * @param taskSubnitPlan
     * @return
     */
    Integer selectMaxId(TaskSubnitPlan taskSubnitPlan);

    /**
     * 获取最新的备注
     *
     * @param taskSubnitPlan
     * @return
     */
    String selectMaxMemo(TaskSubnitPlan taskSubnitPlan);

    /**
     * 修改进度说明
     *
     * @param taskSubnitPlan
     */
    void updateTaskSubnitPlanByIds(TaskSubnitPlan taskSubnitPlan);
}

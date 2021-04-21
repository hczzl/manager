package com.ruoyi.web.service.item.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.mapper.item.TaskSubnitPlanMapper;
import com.ruoyi.web.domain.TaskSubnitPlan;
import com.ruoyi.web.service.item.TaskSubnitPlanService;
import com.ruoyi.common.core.text.Convert;

/**
 * 提交进度Service业务层处理
 *
 * @author ruoyi
 * @date 2019-08-20
 */
@Service
public class TaskSubnitPlanServiceImpl implements TaskSubnitPlanService {
    @Autowired
    private TaskSubnitPlanMapper taskSubnitPlanMapper;

    /**
     * 查询提交进度
     *
     * @param tId 提交进度ID
     * @return 提交进度
     */
    @Override
    public List<TaskSubnitPlan> selectTaskSubnitPlanById(Long tId) {
        return taskSubnitPlanMapper.selectTaskSubnitPlanById(tId);
    }

    /**
     * 查询提交进度列表
     *
     * @param taskSubnitPlan 提交进度
     * @return 提交进度
     */
    @Override
    public List<TaskSubnitPlan> selectTaskSubnitPlanList(TaskSubnitPlan taskSubnitPlan) {
        return taskSubnitPlanMapper.selectTaskSubnitPlanList(taskSubnitPlan);
    }

    /**
     * 新增提交进度
     *
     * @param taskSubnitPlan 提交进度
     * @return 结果
     */
    @Override
    public int insertTaskSubnitPlan(TaskSubnitPlan taskSubnitPlan) {
        return taskSubnitPlanMapper.insertTaskSubnitPlan(taskSubnitPlan);
    }

    /**
     * 修改提交进度
     *
     * @param taskSubnitPlan 提交进度
     * @return 结果
     */
    @Override
    public int updateTaskSubnitPlan(TaskSubnitPlan taskSubnitPlan) {
        return taskSubnitPlanMapper.updateTaskSubnitPlan(taskSubnitPlan);
    }

    /**
     * 删除提交进度对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTaskSubnitPlanByIds(String ids) {
        return taskSubnitPlanMapper.deleteTaskSubnitPlanByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除提交进度信息
     *
     * @param tId 提交进度ID
     * @return 结果
     */
    @Override
    public int deleteTaskSubnitPlanById(Long tId) {
        return taskSubnitPlanMapper.deleteTaskSubnitPlanById(tId);
    }

    @Override
    public Integer selectMaxId(TaskSubnitPlan taskSubnitPlan) {
        return taskSubnitPlanMapper.selectMaxId(taskSubnitPlan);
    }

    @Override
    public String selectMaxMemo(TaskSubnitPlan taskSubnitPlan) {
        return taskSubnitPlanMapper.selectMaxMemo(taskSubnitPlan);
    }

    @Override
    public void updateTaskSubnitPlanByIds(TaskSubnitPlan taskSubnitPlan) {
        taskSubnitPlanMapper.updateTaskSubnitPlanByIds(taskSubnitPlan);
    }

}

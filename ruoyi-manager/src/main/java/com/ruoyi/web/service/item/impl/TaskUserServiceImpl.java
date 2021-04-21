package com.ruoyi.web.service.item.impl;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ruoyi.web.domain.ProjectTaskTable;
import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.service.item.ProjectTaskTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.mapper.item.TaskUserMapper;
import com.ruoyi.web.domain.TaskUser;
import com.ruoyi.web.service.item.TaskUserService;
import com.ruoyi.common.core.text.Convert;

/**
 * 任务参与人Service业务层处理
 *
 * @author ruoyi
 * @date 2019-08-20
 */
@Service
public class TaskUserServiceImpl implements TaskUserService {
    @Autowired
    private TaskUserMapper taskUserMapper;
    @Autowired
    private ProjectTaskTableService projectTaskTableService;

    /**
     * 查询任务参与人
     *
     * @param tId 任务参与人ID
     * @return 任务参与人
     */
    @Override
    public TaskUser selectTaskUserById(Long tId) {
        return taskUserMapper.selectTaskUserById(tId);
    }

    /**
     * 查询任务参与人列表
     *
     * @param taskUser 任务参与人
     * @return 任务参与人
     */
    @Override
    public List<TaskUser> selectTaskUserList(TaskUser taskUser) {
        return taskUserMapper.selectTaskUserList(taskUser);
    }

    /**
     * 新增任务参与人
     *
     * @param taskUser 任务参与人
     * @return 结果
     */
    @Override
    public int insertTaskUser(TaskUser taskUser) {
        return taskUserMapper.insertTaskUser(taskUser);
    }

    /**
     * 修改任务参与人
     *
     * @param taskUser 任务参与人
     * @return 结果
     */
    @Override
    public int updateTaskUser(TaskUser taskUser) {
        return taskUserMapper.updateTaskUser(taskUser);
    }

    /**
     * 删除任务参与人对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteTaskUserByIds(String ids) {
        return taskUserMapper.deleteTaskUserByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除任务参与人信息
     *
     * @param tId 任务参与人ID
     * @return 结果
     */
    @Override
    public int deleteTaskUserById(Long tId) {
        return taskUserMapper.deleteTaskUserById(tId);
    }

    @Override
    public int insertTaskUsers(TaskTable taskTable, long chargePeopleId) {
        // 插入参与人
        int a = 0;
        // 插入负责人
        inserts(taskTable.gettId().intValue(), (int) chargePeopleId);
        return a;
    }

    public void inserts(Integer taskId, Integer userId) {
        ProjectTaskTable projectTaskTable = new ProjectTaskTable();
        projectTaskTable.setUserId(userId);
        projectTaskTable.setEventId(taskId);
        projectTaskTable.setTypeId(0);
        List<ProjectTaskTable> ps = projectTaskTableService.selectInfos(projectTaskTable);
        if (ps.size() < 1) {
            projectTaskTable.setCreateTime(new Date());
            projectTaskTableService.insertProjectTask(projectTaskTable);
        }
        if (ps.size() > 0) {
            projectTaskTable.setUpdateTime(new Date());
            projectTaskTableService.updateProjectTask(projectTaskTable);
        }
    }

    /**
     * 查询我的任务
     *
     * @param taskUser
     * @return
     */
    @Override
    public List<TaskUser> selectMyTask(TaskUser taskUser) {
        return taskUserMapper.selectMyTask(taskUser);
    }

    @Override
    public List<String> getUserByTid(Map<String, Object> map) {
        return taskUserMapper.getUserByTid(map);
    }

    /**
     * 获得所有用户id
     *
     * @param taskUser
     * @return
     */
    @Override
    public List<TaskUser> selectAllUser(TaskUser taskUser) {
        return taskUserMapper.selectAllUser(taskUser);
    }

    @Override
    public List<TaskUser> selectAllTid(TaskUser taskUser) {
        return taskUserMapper.selectAllTid(taskUser);
    }
}

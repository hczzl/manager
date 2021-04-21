package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.TaskTable;
import com.ruoyi.web.domain.TaskUser;

import java.util.List;
import java.util.Map;

/**
 * 任务参与人Service接口
 *
 * @author ruoyi
 * @date 2019-08-20
 */
public interface TaskUserService {
    /**
     * 查询任务参与人
     *
     * @param tId 任务参与人ID
     * @return 任务参与人
     */
    TaskUser selectTaskUserById(Long tId);

    /**
     * 查询任务参与人列表
     *
     * @param taskUser 任务参与人
     * @return 任务参与人集合
     */
    List<TaskUser> selectTaskUserList(TaskUser taskUser);

    /**
     * 新增任务参与人
     *
     * @param taskUser 任务参与人
     * @return 结果
     */
    int insertTaskUser(TaskUser taskUser);

    /**
     * 修改任务参与人
     *
     * @param taskUser 任务参与人
     * @return 结果
     */
    int updateTaskUser(TaskUser taskUser);

    /**
     * 批量删除任务参与人
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteTaskUserByIds(String ids);

    /**
     * 删除任务参与人信息
     *
     * @param tId 任务参与人ID
     * @return 结果
     */
    int deleteTaskUserById(Long tId);

    int insertTaskUsers(TaskTable taskTable, long chargePeopleId);

    /**
     * 查询我的任务
     *
     * @param taskUser
     * @return
     */
    List<TaskUser> selectMyTask(TaskUser taskUser);

    /**
     * 获得用户id
     *
     * @param map
     * @return
     */
    List<String> getUserByTid(Map<String, Object> map);

    /**
     * 获得所有用户id
     *
     * @param taskUser
     * @return
     */
    List<TaskUser> selectAllUser(TaskUser taskUser);

    /**
     * 获取到所有的任务tId
     *
     * @param taskUser
     * @return
     */
    List<TaskUser> selectAllTid(TaskUser taskUser);

}

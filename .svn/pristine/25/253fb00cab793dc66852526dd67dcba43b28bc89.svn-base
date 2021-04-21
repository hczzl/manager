package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.TaskUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * 任务参与人Mapper接口
 *
 * @author ruoyi
 * @date 2019-08-20
 */
@Component
public interface TaskUserMapper {
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
     * 删除任务参与人
     *
     * @param tId 任务参与人ID
     * @return 结果
     */
    int deleteTaskUserById(Long tId);

    /**
     * 批量删除任务参与人
     *
     * @param tIds 需要删除的数据ID
     * @return 结果
     */
    int deleteTaskUserByIds(String[] tIds);

    List<String> getUserByTid(Map<String, Object> map);

    /**
     * 查询我的任务
     *
     * @param taskUser
     * @return
     */

    List<TaskUser> selectMyTask(TaskUser taskUser);

    //获得所有用户id

    List<TaskUser> selectAllUser(TaskUser taskUser);

    List<TaskUser> selectAllTid(TaskUser taskUser);


}

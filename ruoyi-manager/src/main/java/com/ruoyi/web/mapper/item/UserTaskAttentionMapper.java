package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.UserTaskAttention;

import java.util.List;

/**
 * 关注任务Mapper接口
 *
 * @author ruoyi
 * @date 2019-08-20
 */
public interface UserTaskAttentionMapper {
    /**
     * 查询关注任务
     *
     * @param userId 关注任务ID
     * @return 关注任务
     */
    UserTaskAttention selectUserTaskAttentionById(Long userId);

    /**
     * 查询关注任务列表
     *
     * @param userTaskAttention 关注任务
     * @return 关注任务集合
     */
    List<UserTaskAttention> selectUserTaskAttentionList(UserTaskAttention userTaskAttention);

    /**
     * 新增关注任务
     *
     * @param userTaskAttention 关注任务
     * @return 结果
     */
    int insertUserTaskAttention(UserTaskAttention userTaskAttention);

    /**
     * 修改关注任务
     *
     * @param userTaskAttention 关注任务
     * @return 结果
     */
    int updateUserTaskAttention(UserTaskAttention userTaskAttention);

    /**
     * 删除关注任务
     *
     * @param userId 关注任务ID
     * @return 结果
     */
    int deleteUserTaskAttentionById(Long userId);

    /**
     * 批量删除关注任务
     *
     * @param userIds 需要删除的数据ID
     * @return 结果
     */
    int deleteUserTaskAttentionByIds(String[] userIds);

    //取消关注

    int deleteUserAttention(UserTaskAttention u);
}

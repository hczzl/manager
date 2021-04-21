package com.ruoyi.web.service.item;

import com.ruoyi.web.domain.UserTaskAttention;

import java.util.List;

/**
 * 关注任务Service接口
 *
 * @author ruoyi
 * @date 2019-08-20
 */
public interface UserTaskAttentionService {
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
     * 批量删除关注任务
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteUserTaskAttentionByIds(String ids);

    /**
     * 删除关注任务信息
     *
     * @param userId 关注任务ID
     * @return 结果
     */
    int deleteUserTaskAttentionById(Long userId);

    /**
     * 取消关注
     *
     * @param u
     * @return
     */
    int deleteUserAttention(UserTaskAttention u);

}

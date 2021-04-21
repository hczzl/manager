package com.ruoyi.web.service.item.impl;

import com.ruoyi.common.core.text.Convert;
import com.ruoyi.web.domain.UserTaskAttention;
import com.ruoyi.web.mapper.item.UserTaskAttentionMapper;
import com.ruoyi.web.service.item.UserTaskAttentionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 关注任务Service业务层处理
 *
 * @author ruoyi
 * @date 2019-08-20
 */
@Service
public class UserTaskAttentionServiceImpl implements UserTaskAttentionService {
    @Autowired
    private UserTaskAttentionMapper userTaskAttentionMapper;

    /**
     * 查询关注任务
     *
     * @param userId 关注任务ID
     * @return 关注任务
     */
    @Override
    public UserTaskAttention selectUserTaskAttentionById(Long userId) {
        return userTaskAttentionMapper.selectUserTaskAttentionById(userId);
    }

    /**
     * 查询关注任务列表
     *
     * @param userTaskAttention 关注任务
     * @return 关注任务
     */
    @Override
    public List<UserTaskAttention> selectUserTaskAttentionList(UserTaskAttention userTaskAttention) {
        return userTaskAttentionMapper.selectUserTaskAttentionList(userTaskAttention);
    }

    /**
     * 新增关注任务
     *
     * @param userTaskAttention 关注任务
     * @return 结果
     */
    @Override
    public int insertUserTaskAttention(UserTaskAttention userTaskAttention) {
        return userTaskAttentionMapper.insertUserTaskAttention(userTaskAttention);
    }

    /**
     * 修改关注任务
     *
     * @param userTaskAttention 关注任务
     * @return 结果
     */
    @Override
    public int updateUserTaskAttention(UserTaskAttention userTaskAttention) {
        return userTaskAttentionMapper.updateUserTaskAttention(userTaskAttention);
    }

    /**
     * 删除关注任务对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteUserTaskAttentionByIds(String ids) {
        return userTaskAttentionMapper.deleteUserTaskAttentionByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除关注任务信息
     *
     * @param userId 关注任务ID
     * @return 结果
     */
    @Override
    public int deleteUserTaskAttentionById(Long userId) {
        return userTaskAttentionMapper.deleteUserTaskAttentionById(userId);
    }

    /**
     * 取消关注
     *
     * @param u
     * @return
     */
    @Override
    public int deleteUserAttention(UserTaskAttention u) {
        return userTaskAttentionMapper.deleteUserAttention(u);
    }
}

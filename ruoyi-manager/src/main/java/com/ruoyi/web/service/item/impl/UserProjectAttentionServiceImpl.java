package com.ruoyi.web.service.item.impl;

import com.ruoyi.web.domain.UserProjectAttention;
import com.ruoyi.web.mapper.item.UserProjectAttentionMapper;
import com.ruoyi.web.service.item.UserProjectAttentionService;
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
public class UserProjectAttentionServiceImpl implements UserProjectAttentionService {
    @Autowired
    private UserProjectAttentionMapper userProjectAttentionMapper;

    /**
     * 查询我关注项目
     *
     * @param userId
     * @return
     */
    @Override
    public UserProjectAttention selectUserProjectAttentionById(Long userId) {
        return userProjectAttentionMapper.selectUserProjectAttentionById(userId);
    }

    /**
     * 查询关注项目列表
     *
     * @return 关注任务集合
     */
    @Override
    public List<UserProjectAttention> selectUserProjectAttentionList(UserProjectAttention userProjectAttention) {
        return userProjectAttentionMapper.selectUserProjectAttentionList(userProjectAttention);
    }

    /**
     * 新增关注项目
     *
     * @return 结果
     */
    @Override
    public int insertUserProjectAttention(UserProjectAttention userProjectAttention) {
        return userProjectAttentionMapper.insertUserProjectAttention(userProjectAttention);
    }

    /**
     * 获取登录用户关注项目的状态
     *
     * @param userProjectAttention
     * @return
     */
    @Override
    public String selectUserAention(UserProjectAttention userProjectAttention) {
        return userProjectAttentionMapper.selectUserAention(userProjectAttention);
    }

    /**
     * 取消关注
     *
     * @param u
     * @return
     */
    @Override
    public int deleteUserAttention(UserProjectAttention u) {
        return userProjectAttentionMapper.deleteUserAttention(u);
    }
}

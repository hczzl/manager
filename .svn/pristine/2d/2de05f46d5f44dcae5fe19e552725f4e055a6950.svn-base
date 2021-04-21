package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.UserProjectAttention;


import java.util.List;

/**
 * 关注项目Mapper接口
 *
 * @author ruoyi
 * @date 2019-08-20
 */
public interface UserProjectAttentionMapper {
    /**
     * 查询我关注项目
     */
    UserProjectAttention selectUserProjectAttentionById(Long userId);

    /**
     * 查询关注项目列表
     *
     * @return 关注任务集合
     */
    List<UserProjectAttention> selectUserProjectAttentionList(UserProjectAttention userProjectAttention);

    /**
     * 新增关注项目
     *
     * @return 结果
     */
    int insertUserProjectAttention(UserProjectAttention userProjectAttention);

    /**
     * 获取登录用户关注项目的状态
     *
     * @param userProjectAttention
     * @return
     */
    String selectUserAention(UserProjectAttention userProjectAttention);

    /**
     * 取消关注
     *
     * @param u
     * @return
     */
    int deleteUserAttention(UserProjectAttention u);
}

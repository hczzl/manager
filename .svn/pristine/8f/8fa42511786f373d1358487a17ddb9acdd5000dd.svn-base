package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.UserGroup;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserGroupMapper {
    /**
     * 1.创建分组
     */
    void createdGroup(UserGroup userGroup);

    /**
     * 2.删除分组
     */
    void removeGroup(UserGroup userGroup);

    /**
     * 3.修改分组
     */
    void upGroup(UserGroup userGroup);

    /**
     * 4.查询分组
     */
    List<UserGroup> selectGroup();

    /**
     * 5.用户插入分组
     */
    void addUserGroup(UserGroup userGroup);

    /**
     * 6.根据分组Id获取分组与用户的关系
     */
    List<UserGroup> selectGroupInfoById(Integer userGroup);

    /**
     * 7.将用户移出分组
     */
    void removeUserGroup(UserGroup userGroup);
}

package com.ruoyi.web.service.item;

import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.web.domain.UserGroup;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;


public interface UserGroupService {
    /**
     * 创建分组
     */
     void createdGroup(UserGroup userGroup);

    /**
     * 删除分组
     */
     void removeGroup(UserGroup userGroup);

    /**
     * 修改分组
     */
     void upGroup(UserGroup userGroup);

    /**
     * 查询所有用户组信息
     * @return
     */
    public List<SysDept> selectAllGroup();

    /**
     * 根据用户组Id查询组员信息
     * @return
     */
    List<SysUser> selectUserByGroupId(UserGroup userGroup);

    /**
     * 批量替换组内的成员
     */
     void replaceGroupUsers(UserGroup userGroup);

    /**
     * 将用户移出分组
     */
    void removeUserGroup(UserGroup userGroup);
}

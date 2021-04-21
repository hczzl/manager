package com.ruoyi.web.service.item.impl;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.service.ISysDeptService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.web.domain.UserGroup;
import com.ruoyi.web.mapper.item.UserGroupMapper;
import com.ruoyi.web.service.item.UserGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserGroupServiceImpl implements UserGroupService {
    @Autowired
    private UserGroupMapper userGroupMapper;
    @Autowired
    private ISysDeptService deptService;
    @Autowired
    private ISysUserService userService;

    /**
     * 创建用户组
     *
     * @return
     */
    @Override
    public void createdGroup(UserGroup userGroup) {
        userGroup.setCreateBy(ShiroUtils.getLoginName());
        userGroupMapper.createdGroup(userGroup);
    }

    /**
     * 移除用户组
     *
     * @return
     */
    @Override
    public void removeGroup(UserGroup userGroup) {
        userGroupMapper.removeGroup(userGroup);
        // 删除指定组的所有成员信息
        userGroupMapper.removeUserGroup(userGroup);
    }

    /**
     * 修改用户组名
     *
     * @return
     */
    @Override
    public void upGroup(UserGroup userGroup) {
        userGroup.setUpdateBy(ShiroUtils.getLoginName());
        userGroupMapper.upGroup(userGroup);
    }

    /**
     * 查询分组信息
     *
     * @return
     */
    @Override
    public List<SysDept> selectAllGroup() {
        SysDept dept = new SysDept();
        // 存储每个分组内的用户信息
        Map<Object, List<SysUser>> usersMap = new HashMap();
        // 获取部门列表
        List<SysDept> deptList = deptService.selectDeptList(dept);
        // 获取分组列表
        List<SysDept> groupList = deptService.selectGroupDeptList(dept);
        // 获取分组下的所有组
        List<SysDept> childDepts = groupList.get(0).getChildDept();
        childDepts.forEach(childDept -> {
            List<SysUser> sysUserList = new ArrayList<>();
            // 获取分组id
            Long deptId = childDept.getDeptId();
            // 根据用户组Id查询所有符合条件的用户id
            List<UserGroup> userGroups = userGroupMapper.selectGroupInfoById(Integer.parseInt(deptId.toString()));
            // 根据用户id查询用户信息
            userGroups.forEach(userGroup -> {
                SysUser sysUser = userService.selectUserInfoById(userGroup.getUserId());
                if (sysUser != null) {
                    SysDept dept1 = sysUser.getDept();
                    dept1.setDeptName(userService.selectDeptNameByUserId(userGroup.getUserId()));
                    sysUser.setDept(dept1);
                    sysUserList.add(sysUser);
                }
            });
            usersMap.put(deptId, sysUserList);
            // 将用户信息插入分组内
            childDept.setUsers(usersMap.get(deptId));
        });
        deptList.addAll(1, groupList);
        return deptList;
    }

    @Override
    public List<SysUser> selectUserByGroupId(UserGroup userGroup) {
        List<SysUser> sysUserList = new ArrayList<>();
        // 根据用户组Id查询所有符合条件的用户id
        List<UserGroup> userGroups = userGroupMapper.selectGroupInfoById(userGroup.getGroupId());
        // 根据用户id查询用户信息
        userGroups.forEach(userGroupinfo -> {
            SysUser sysUser = userService.selectUserInfoById(userGroupinfo.getUserId());
            if (sysUser != null) {
                SysDept dept = sysUser.getDept();
                dept.setDeptName(userService.selectDeptNameByUserId(userGroupinfo.getUserId()));
                sysUser.setDept(dept);
                sysUserList.add(sysUser);
            }
        });
        return sysUserList;
    }

    /**
     * 替换组内的成员
     *
     * @param userGroup
     */
    @Override
    public void replaceGroupUsers(UserGroup userGroup) {
        // 删除指定组的所有成员信息
        userGroupMapper.removeUserGroup(userGroup);
        // 重新插入组员
        String[] userId = userGroup.getUserIds().split(",");
        for (int i = 0; i < userId.length; i++) {
            userGroup.setUserId(Integer.parseInt(userId[i]));
            userGroupMapper.addUserGroup(userGroup);
        }
    }

    /**
     * 将用户移出分组
     *
     * @param userGroup
     */
    @Override
    public void removeUserGroup(UserGroup userGroup) {
        String[] split = userGroup.getUserIds().split(",");
        for (int i = 0; i < split.length; i++) {
            userGroup.setUserId(Integer.parseInt(split[i]));
            userGroupMapper.removeUserGroup(userGroup);
        }
    }
}

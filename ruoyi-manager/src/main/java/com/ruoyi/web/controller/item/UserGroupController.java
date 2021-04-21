package com.ruoyi.web.controller.item;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysDept;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.web.domain.UserGroup;
import com.ruoyi.web.service.item.UserGroupService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api(value = "UserGroupController",tags = "分组模块相关接口")
@RestController
@RequestMapping(value = "/userGroup")
public class UserGroupController {
    @Autowired
    private UserGroupService userGroupService;

    @Log(title = "分组模块:创建用户组", businessType = BusinessType.INSERT)
    @PostMapping(value = "/createdGroup")
    public AjaxResult createdGroup(UserGroup userGroup){
        userGroupService.createdGroup(userGroup);
        return AjaxResult.success();
    }

    @Log(title = "分组模块:移除用户组", businessType = BusinessType.DELETE)
    @ApiOperation("移除用户组")
    @PostMapping(value = "/removeGroup")
    public AjaxResult removeGroup(UserGroup userGroup){
        userGroupService.removeGroup(userGroup);
        return AjaxResult.success();
    }

    @Log(title = "分组模块:修改用户组名", businessType = BusinessType.UPDATE)
    @ApiOperation("修改用户组名")
    @PostMapping(value = "/upGroup")
    public AjaxResult upGroup(UserGroup userGroup){
        userGroupService.upGroup(userGroup);
        return AjaxResult.success();
    }

    @ApiOperation("查询所有用户组信息")
    @GetMapping(value = "/selectAllGroup")
    public List<SysDept> selectAllGroup(){
        List<SysDept> sysDepts = userGroupService.selectAllGroup();
        return sysDepts;
    }

    @ApiOperation("根据用户组Id查询组员信息")
    @PostMapping(value = "/selectUserByGroupId")
    public List<SysUser> selectUserByGroupId(UserGroup userGroup){
        return userGroupService.selectUserByGroupId(userGroup);
    }

    @ApiOperation("批量替换组内的成员")
    @PostMapping(value = "/replaceGroupUsers")
    public AjaxResult replaceGroupUsers(UserGroup userGroup){
        userGroupService.replaceGroupUsers(userGroup);
        return AjaxResult.success();
    }

    @Log(title = "分组模块:将用户移出分组", businessType = BusinessType.DELETE)
    @PostMapping(value = "/removeUserGroup")
    public AjaxResult removeUserGroup(UserGroup userGroup){
        userGroupService.removeUserGroup(userGroup);
        return AjaxResult.success();
    }
}

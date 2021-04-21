package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.SysUserToken;

/**
 * 系统用户Token
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:07
 */
public interface SysUserTokenMapper {

    SysUserToken queryByToken(String token);

    SysUserToken selectById(long userId);

    void save(SysUserToken tokenEntity);

    void updateById(SysUserToken tokenEntity);

    /**
     * 删除token
     *
     * @param userId
     * @return
     */
    public int deleteTokenByUserId(long userId);

    public Integer selectRoleId(SysUserRole sysUserRole);
}

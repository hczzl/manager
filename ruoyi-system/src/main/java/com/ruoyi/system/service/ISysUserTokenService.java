package com.ruoyi.system.service;

import java.util.Map;

import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.SysUserToken;

/**
 * 用户Token
 *
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2017-03-23 15:22:07
 */
public interface ISysUserTokenService {

    /**
     * 生成token
     *
     * @param userId 用户ID
     * @throws Exception
     */
    Map<String, Object> createToken(long userId) throws Exception;

    /**
     * 退出，修改token值
     *
     * @param userId 用户ID
     * @throws Exception
     */
    void logout(long userId) throws Exception;

    /**
     * @param token
     * @return
     */
    SysUserToken queryByToken(String token);

    /**
     * @param userId
     * @return
     */
    int deleteTokenByUserId(long userId);

    /**
     * @param sysUserRole
     * @return
     */
    Integer selectRoleId(SysUserRole sysUserRole);
}

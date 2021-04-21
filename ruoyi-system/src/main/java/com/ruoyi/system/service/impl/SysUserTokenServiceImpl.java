package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.TokenGenerator;
import com.ruoyi.system.domain.SysUser;
import com.ruoyi.system.domain.SysUserRole;
import com.ruoyi.system.domain.SysUserToken;
import com.ruoyi.system.mapper.SysUserTokenMapper;
import com.ruoyi.system.service.ISysUserTokenService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Service("sysUserTokenService")
public class SysUserTokenServiceImpl implements ISysUserTokenService {
    /**
     * 12小时后过期
     */
    private final static int EXPIRE = 3600 * 12;

    @Autowired
    private SysUserTokenMapper sysUserTokenMapper;


    @Override
    public Map<String, Object> createToken(long userId) throws Exception {
        //生成一个token,框架自带的一个token
        String token = TokenGenerator.generateValue();
        //当前时间
        Date now = new Date();
        //过期时间
        Date expireTime = new Date(now.getTime() + EXPIRE * 1000);
        //判断是否生成过token
        SysUserToken tokenEntity = sysUserTokenMapper.selectById(userId);

        Map<String, Object> map = new HashMap<>();
        //判断当前登录的用户是否是超级管理员，如果是则允许多点登录
        if (SysUser.isAdmin(userId)) {
            if (tokenEntity == null) {
                tokenEntity = new SysUserToken();
                tokenEntity.setUserId(userId);
                tokenEntity.setToken(token);
                tokenEntity.setUpdateTime(now);
                tokenEntity.setExpireTime(expireTime);
                //保存token
                sysUserTokenMapper.save(tokenEntity);

                map.put("token", token);
                map.put("expire", EXPIRE);
            }
            if (tokenEntity != null) {
                Date expireTime1 = tokenEntity.getExpireTime();
                String token1 = tokenEntity.getToken();
                //token不等于空的情况,先判断token是否失效
                if (now.getTime() > expireTime1.getTime()) {
                    tokenEntity.setToken(token);
                    tokenEntity.setUpdateTime(now);
                    tokenEntity.setExpireTime(expireTime);
                    //更新token
                    sysUserTokenMapper.updateById(tokenEntity);
                    map.put("token", token);
                    map.put("expire", EXPIRE);
                } else {
                    map.put("token", token1);
                    map.put("expire", EXPIRE);
                }
            }
        }
        if (!SysUser.isAdmin(userId)) {
            if (tokenEntity == null) {
                tokenEntity = new SysUserToken();
                tokenEntity.setUserId(userId);
                tokenEntity.setToken(token);
                tokenEntity.setUpdateTime(now);
                tokenEntity.setExpireTime(expireTime);
                //保存token
                sysUserTokenMapper.save(tokenEntity);
            } else {
                tokenEntity.setToken(token);
                tokenEntity.setUpdateTime(now);
                tokenEntity.setExpireTime(expireTime);
                //更新token
                sysUserTokenMapper.updateById(tokenEntity);
            }
            map.put("token", token);
            map.put("expire", EXPIRE);
        }
        return map;
    }

    @Override
    public void logout(long userId) throws Exception {
        //生成一个token
        String token = TokenGenerator.generateValue();
        //修改token
        SysUserToken tokenEntity = new SysUserToken();
        tokenEntity.setUserId(userId);
        tokenEntity.setToken(token);
        sysUserTokenMapper.updateById(tokenEntity);
    }

    @Override
    public SysUserToken queryByToken(String token) {
        return sysUserTokenMapper.queryByToken(token);
    }

    /**
     * 删除token
     *
     * @param userId
     * @return
     */
    @Override
    public int deleteTokenByUserId(long userId) {
        return sysUserTokenMapper.deleteTokenByUserId(userId);
    }

    @Override
    public Integer selectRoleId(SysUserRole sysUserRole) {
        return sysUserTokenMapper.selectRoleId(sysUserRole);
    }
}

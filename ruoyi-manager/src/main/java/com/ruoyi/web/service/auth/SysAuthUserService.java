package com.ruoyi.web.service.auth;

import com.ruoyi.web.domain.auth.SysAuthUser;

import java.util.List;
import java.util.Map;

/**
 * @author zhongzhilong
 * @date 2020/11/9 0009
 * @description 服务层
 */
public interface SysAuthUserService {
    /**
     * 获取权限列表
     *
     * @return
     */
    Map<String, Object> selectList(SysAuthUser sysAuthUser);
}

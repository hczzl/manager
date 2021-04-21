package com.ruoyi.web.mapper.auth;

import com.ruoyi.web.domain.SysUserWorks;
import com.ruoyi.web.domain.auth.SysAuthUser;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * @author zhongzhilong
 * @date 2020/11/9 0009
 * @description 权限类Mappe接口
 */
@Component
public interface SysAuthUserMapper {
    /**
     * 获取权限列表
     *
     * @return
     */
    List<Map<String, Object>> selectList(SysAuthUser sysAuthUser);
}

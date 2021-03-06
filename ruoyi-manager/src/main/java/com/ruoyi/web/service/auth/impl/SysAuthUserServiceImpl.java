package com.ruoyi.web.service.auth.impl;

import com.ruoyi.framework.util.ShiroUtils;
import com.ruoyi.web.domain.auth.SysAuthUser;
import com.ruoyi.web.mapper.auth.SysAuthUserMapper;
import com.ruoyi.web.service.auth.SysAuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author zhongzhilong
 * @date 2020/11/9 0009
 * @description 接口实现类
 */
@Service
public class SysAuthUserServiceImpl implements SysAuthUserService {

    @Autowired
    private SysAuthUserMapper sysAuthRankMapper;

    @Override
    public Map<String, Object> selectList(SysAuthUser sysAuthUser) {
        Map<String, Object> resultMap = new HashMap<>();
        List<Map<String, Object>> list = sysAuthRankMapper.selectList(sysAuthUser);
        if (ShiroUtils.isNotEmpty(list)) {
            for (Map<String, Object> map : list) {
                String authNo = (String) map.get("authNo");
                Long deptId = (Long) map.get("deptId");
                resultMap.put(authNo, deptId);
            }
        }
        return resultMap;
    }
}

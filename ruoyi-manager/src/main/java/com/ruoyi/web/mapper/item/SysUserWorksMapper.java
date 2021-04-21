package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.SysUserWorks;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;


@Component
public interface SysUserWorksMapper {

    int insertUserWorks(SysUserWorks sysUserWorks);

    int updateUserWorks(SysUserWorks sysUserWorks);

    List<SysUserWorks> selectUserWorks(SysUserWorks sysUserWorks);

    Integer selectCount(Map map);

    List<Map<String, Object>> selectMonthScore(Map<String, Object> map);

    Map<String, Object> selectScoreByTime(Map<String, Object> map);

    /**
     * 获取每个月的实际工作天数
     *
     * @param map
     * @return
     */
    Integer selectWorkDays(Map<String, Object> map);
}

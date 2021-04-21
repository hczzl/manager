package com.ruoyi.web.mapper.item;

import com.ruoyi.web.domain.SysYearMonth;

import java.util.List;
import java.util.Map;

public interface SysYearMonthMapper {
    List<SysYearMonth> selectInfos(SysYearMonth sysYearMonth);

    int insertDate(SysYearMonth sysYearMonth);

    List<Integer> selectYearList();

    Integer selectMonthCount(String year);

    String selectMinDate(String year);

    String selectMaxDate(String year);

    /**
     * 根据年份获取所有的月份
     *
     * @param map
     * @return
     */
    List<String> selectMonthList(Map<String, Object> map);

}

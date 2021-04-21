package com.ruoyi.web.service.item;


import com.ruoyi.web.domain.SysYearMonth;

import java.util.List;
import java.util.Map;


public interface SysYearMonthService {
    /**
     * 查询日期列表
     *
     * @param sysYearMonth
     * @return
     */
    List<SysYearMonth> selectInfos(SysYearMonth sysYearMonth);

    /**
     * 添加日期
     *
     * @param sysYearMonth
     * @return
     */
    int insertDate(SysYearMonth sysYearMonth);

    /**
     * 根据年份获取所有月份
     *
     * @param year 年份
     * @return
     */
    List<String> selectMonthList(int year);

}

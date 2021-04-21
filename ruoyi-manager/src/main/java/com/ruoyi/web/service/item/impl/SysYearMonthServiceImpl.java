package com.ruoyi.web.service.item.impl;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.framework.util.DateUtil;
import com.ruoyi.web.domain.SysYearMonth;
import com.ruoyi.web.mapper.item.SysYearMonthMapper;
import com.ruoyi.web.service.item.SysYearMonthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @date 2019-08-20
 */
@Service
public class SysYearMonthServiceImpl implements SysYearMonthService {

    @Autowired
    private SysYearMonthMapper sysYearMonthMapper;

    @Override
    public List<SysYearMonth> selectInfos(SysYearMonth sysYearMonth) {
        return sysYearMonthMapper.selectInfos(sysYearMonth);
    }

    /**
     * 将本月的第一天和最后一天存入数据库中
     *
     * @param sysYearMonth
     * @return
     */
    @Override
    public int insertDate(SysYearMonth sysYearMonth) {
        int a = 0;
        sysYearMonth.setStartTime(DateUtils.getfirstday());
        sysYearMonth.setEndTime(DateUtils.getlastday());
        List<SysYearMonth> list = sysYearMonthMapper.selectInfos(sysYearMonth);
        if (list.size() < 1) {
            a = sysYearMonthMapper.insertDate(sysYearMonth);
        }
        return a;
    }

    @Override
    public List<String> selectMonthList(int year) {
        StringBuilder builder = new StringBuilder();
        builder.append(year).append("_");
        // 开始日期
        String startTime = sysYearMonthMapper.selectMinDate(builder.toString());
        // 该年份最大日期，如果是今年，则是表中最大的那个日期
        String lastDay = sysYearMonthMapper.selectMaxDate(builder.toString());
        // 当前月份最后一天
        String currentDay = DateUtil.getCurrentMonthLastDay();
        // 当前月份为最大月份的情况
        if (lastDay.equals(currentDay)) {
            // 获取上个月的最后一天
            String lastMonthLastDay = DateUtil.getLastMonthLastDay();
            if (startTime.compareTo(lastMonthLastDay) < 0) {
                lastDay = lastMonthLastDay;
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("startTime", startTime);
        map.put("endTime", lastDay);
        List<String> resultList = sysYearMonthMapper.selectMonthList(map);
        return resultList;
    }
}

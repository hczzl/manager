package com.ruoyi.web.service;

import com.ruoyi.web.domain.SysHolidays;

import java.util.List;

/**
 * 发定节假日Service接口
 *
 * @author ruoyi
 * @date 2019-08-28
 */
public interface SysHolidaysService {
    /**
     * 查询发定节假日
     *
     * @param id 发定节假日ID
     * @return 发定节假日
     */
    SysHolidays selectSysHolidaysById(Long id);

    /**
     * 查询发定节假日列表
     *
     * @param sysHolidays 发定节假日
     * @return 发定节假日集合
     */
    List<SysHolidays> selectSysHolidaysList(SysHolidays sysHolidays);

    /**
     * 新增发定节假日
     *
     * @param sysHolidays 发定节假日
     * @return 结果
     */
    int insertSysHolidays(SysHolidays sysHolidays);

    /**
     * 修改发定节假日
     *
     * @param sysHolidays 发定节假日
     * @return 结果
     */
    int updateSysHolidays(SysHolidays sysHolidays);

    /**
     * 批量删除发定节假日
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    int deleteSysHolidaysByIds(String ids);

    /**
     * 删除发定节假日信息
     *
     * @param id 发定节假日ID
     * @return 结果
     */
    int deleteSysHolidaysById(Long id);
}

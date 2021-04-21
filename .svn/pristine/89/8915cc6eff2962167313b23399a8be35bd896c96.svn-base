package com.ruoyi.web.service.imp;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.web.mapper.SysHolidaysMapper;
import com.ruoyi.web.domain.SysHolidays;
import com.ruoyi.web.service.SysHolidaysService;
import com.ruoyi.common.core.text.Convert;

/**
 * 发定节假日Service业务层处理
 *
 * @author ruoyi
 * @date 2019-08-28
 */
@Service
public class SysHolidaysServiceImpl implements SysHolidaysService {
    @Autowired
    private SysHolidaysMapper sysHolidaysMapper;

    /**
     * 查询发定节假日
     *
     * @param id 发定节假日ID
     * @return 发定节假日
     */
    @Override
    public SysHolidays selectSysHolidaysById(Long id) {
        return sysHolidaysMapper.selectSysHolidaysById(id);
    }

    /**
     * 查询发定节假日列表
     *
     * @param sysHolidays 发定节假日
     * @return 发定节假日
     */
    @Override
    public List<SysHolidays> selectSysHolidaysList(SysHolidays sysHolidays) {
        return sysHolidaysMapper.selectSysHolidaysList(sysHolidays);
    }

    /**
     * 新增发定节假日
     *
     * @param sysHolidays 发定节假日
     * @return 结果
     */
    @Override
    public int insertSysHolidays(SysHolidays sysHolidays) {
        return sysHolidaysMapper.insertSysHolidays(sysHolidays);
    }

    /**
     * 修改发定节假日
     *
     * @param sysHolidays 发定节假日
     * @return 结果
     */
    @Override
    public int updateSysHolidays(SysHolidays sysHolidays) {
        return sysHolidaysMapper.updateSysHolidays(sysHolidays);
    }

    /**
     * 删除发定节假日对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysHolidaysByIds(String ids) {
        return sysHolidaysMapper.deleteSysHolidaysByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除发定节假日信息
     *
     * @param id 发定节假日ID
     * @return 结果
     */
    @Override
    public int deleteSysHolidaysById(Long id) {
        return sysHolidaysMapper.deleteSysHolidaysById(id);
    }
}

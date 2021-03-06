package com.ruoyi.web.service.item;

import com.ruoyi.system.domain.SysUser;
import com.ruoyi.web.domain.SysUserWorks;
import org.apache.poi.ss.usermodel.Sheet;

import java.util.List;

/**
 * @author zhongzhilong
 * @date 2020-07-30
 * @description 用户工作情况统计实现类
 */

public interface SysUserWorksService {
    /**
     * @param sysUserWorks
     * @return
     */
    int insertUserWorks(SysUserWorks sysUserWorks);

    /**
     * @param sysUserWorks
     * @return
     */
    int updateUserWorks(SysUserWorks sysUserWorks);

    /**
     * @param sysUserWorks
     * @return
     */
    List<SysUserWorks> selectUserWorks(SysUserWorks sysUserWorks);

    /**
     * 获取每个月实际的工作天数
     *
     * @param startTime
     * @return
     */
    Integer selectWorkDays(Long startTime, Long userId);

    /**
     * 实现excel表中画饼图的功能
     *
     * @param sheet      excel表工作薄
     * @param fldNameArr 饼图各个模块名称
     * @param dataList   数据集合
     * @param col        开始列
     * @param col2       结束列
     * @param row        开始行
     * @param row2       结束行
     */
    void buildPieChart(Sheet sheet, List<String> fldNameArr, List<Integer> dataList, int col, int col2, int row, int row2, int firstRow);
}

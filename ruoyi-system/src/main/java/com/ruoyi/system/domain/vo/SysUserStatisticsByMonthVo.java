package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.system.domain.SysDept;

/**
 * @author Suqz
 * @version 1.0
 * @date 2020/5/5 16:16
 */
public class SysUserStatisticsByMonthVo {
    @Excel(name = "年份")
    private Integer year;

    @Excel(name = "月份")
    private Integer month;

    @Excel(name = "用户名称")
    private String userName;

    @Excel(name = "部门名称", targetAttr = "deptName", type = Excel.Type.EXPORT)
    private SysDept dept;

    @Excel(name = "月绩效得分")
    private Double monthScore;

    @Excel(name = "月任务平均分")
    private Double taskAverageScore;

    @Excel(name = "月综合表现分")
    private Double multipleMonthScore;

    @Excel(name = "本月应工作天数")
    private Integer workDays;

    @Excel(name = "实际任务天数")
    private String period;

    @Excel(name = "工作饱和度")
    private String saturation;

    @Excel(name = "发起任务数")
    private Integer creatTaskNum;


    public SysDept getDept() {
        return dept;
    }

    public void setDept(SysDept dept) {
        this.dept = dept;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getWorkDays() {
        return workDays;
    }

    public void setWorkDays(Integer workDays) {
        this.workDays = workDays;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getSaturation() {
        return saturation;
    }

    public void setSaturation(String saturation) {
        this.saturation = saturation;
    }

    public Integer getCreatTaskNum() {
        return creatTaskNum;
    }

    public void setCreatTaskNum(Integer creatTaskNum) {
        this.creatTaskNum = creatTaskNum;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Double getTaskAverageScore() {
        return taskAverageScore;
    }

    public void setTaskAverageScore(Double taskAverageScore) {
        this.taskAverageScore = taskAverageScore;
    }

    public Double getMultipleMonthScore() {
        return multipleMonthScore;
    }

    public void setMultipleMonthScore(Double multipleMonthScore) {
        this.multipleMonthScore = multipleMonthScore;
    }

    public Double getMonthScore() {
        return monthScore;
    }

    public void setMonthScore(Double monthScore) {
        this.monthScore = monthScore;
    }
}

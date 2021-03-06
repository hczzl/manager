package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.system.domain.SysDept;

/**
 * @author Suqz
 * @version 1.0
 * @date 2020/5/5 16:34
 */
public class SysUserStatisticsByYear {
    @Excel(name = "年份")
    private Integer year;

    @Excel(name = "用户名称")
    private String userName;

    @Excel(name = "部门名称", targetAttr = "deptName", type = Excel.Type.EXPORT)
    private SysDept dept;

    @Excel(name = "年度总评分")
    private Double yearAverageScore;

    @Excel(name = "年度绩效平均分")
    private Double yearScore;

    @Excel(name = "年度表现分")
    private Double personalScore;

    @Excel(name = "年度任务完成量")
    private Integer taskAlreadyFinish;

    @Excel(name = "年度任务未完成量")
    private Integer taskNotFinish;

    @Excel(name = "年度逾期任务量")
    private Integer taskOverdue;

    @Excel(name = "年度未逾期任务量")
    private Integer taskNotOverdue;


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

    public Integer getTaskAlreadyFinish() {
        return taskAlreadyFinish;
    }

    public void setTaskAlreadyFinish(Integer taskAlreadyFinish) {
        this.taskAlreadyFinish = taskAlreadyFinish;
    }

    public Integer getTaskNotFinish() {
        return taskNotFinish;
    }

    public void setTaskNotFinish(Integer taskNotFinish) {
        this.taskNotFinish = taskNotFinish;
    }

    public Integer getTaskOverdue() {
        return taskOverdue;
    }

    public void setTaskOverdue(Integer taskOverdue) {
        this.taskOverdue = taskOverdue;
    }

    public Integer getTaskNotOverdue() {
        return taskNotOverdue;
    }

    public void setTaskNotOverdue(Integer taskNotOverdue) {
        this.taskNotOverdue = taskNotOverdue;
    }

    public Double getYearAverageScore() {
        return yearAverageScore;
    }

    public void setYearAverageScore(Double yearAverageScore) {
        this.yearAverageScore = yearAverageScore;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getPersonalScore() {
        return personalScore;
    }

    public void setPersonalScore(Double personalScore) {
        this.personalScore = personalScore;
    }

    public Double getYearScore() {
        return yearScore;
    }

    public void setYearScore(Double yearScore) {
        this.yearScore = yearScore;
    }
}

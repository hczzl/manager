package com.ruoyi.web.domain;

import com.ruoyi.common.core.domain.BaseEntity;

import java.util.Date;


public class SysUserWorks extends BaseEntity {
    private static final long SERIAL_VERSION_UID = 1L;

    private int id;

    private Integer userId;

    private Integer year;

    private Integer month;

    private Double taskAverageScore;

    private Double multipleMonthScore;

    private Double monthScore;

    private Integer workDays;

    private String period;

    private Integer createTaskNum;

    private Double saturation;

    private String name;

    private Date createTime;

    private Date updateTime;

    private Integer taskAlreadyFinish;

    private Integer taskNotFinish;

    private Integer taskOverdue;

    private Integer taskNotOverdue;

    private Double yearScore;

    private Double personalScore;

    private Double yearAverageScore;
    /**
     * 临时参数，以后删除
     */
    private Double taskMonthScore;

    public Double getTaskMonthScore() {
        return taskMonthScore;
    }

    public void setTaskMonthScore(Double taskMonthScore) {
        this.taskMonthScore = taskMonthScore;
    }

    public Double getMonthScore() {
        return monthScore;
    }

    public void setMonthScore(Double monthScore) {
        this.monthScore = monthScore;
    }

    public Double getMultipleMonthScore() {
        return multipleMonthScore;
    }

    public void setMultipleMonthScore(Double multipleMonthScore) {
        this.multipleMonthScore = multipleMonthScore;
    }

    public Integer getTaskNotOverdue() {
        return taskNotOverdue;
    }

    public void setTaskNotOverdue(Integer taskNotOverdue) {
        this.taskNotOverdue = taskNotOverdue;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public Integer getCreateTaskNum() {
        return createTaskNum;
    }

    public void setCreateTaskNum(Integer createTaskNum) {
        this.createTaskNum = createTaskNum;
    }

    public Double getSaturation() {
        return saturation;
    }

    public void setSaturation(Double saturation) {
        this.saturation = saturation;
    }

    @Override
    public Date getCreateTime() {
        return createTime;
    }

    @Override
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public Date getUpdateTime() {
        return updateTime;
    }

    @Override
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Double getYearScore() {
        return yearScore;
    }

    public void setYearScore(Double yearScore) {
        this.yearScore = yearScore;
    }

    public Double getPersonalScore() {
        return personalScore;
    }

    public void setPersonalScore(Double personalScore) {
        this.personalScore = personalScore;
    }

    public Double getYearAverageScore() {
        return yearAverageScore;
    }

    public void setYearAverageScore(Double yearAverageScore) {
        this.yearAverageScore = yearAverageScore;
    }
}

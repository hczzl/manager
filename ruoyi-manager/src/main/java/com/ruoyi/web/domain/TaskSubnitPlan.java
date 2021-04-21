package com.ruoyi.web.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;

/**
 * 提交进度对象
 *
 * @author zhongzhilong
 * @date 2019/8/20 0015
 */
public class TaskSubnitPlan extends BaseEntity implements Serializable {

    private static final long SERIAL_VERSION_UID = -2843515333515775785L;

    private Integer id;

    @ApiModelProperty("任务id")
    @Excel(name = "任务id")
    private Long tId;

    @ApiModelProperty("进度")
    @Excel(name = "进度")
    private Integer scheduleRate;

    @ApiModelProperty("提交时间")
    @Excel(name = "提交时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date taskSubmittime;

    @ApiModelProperty("提交时间戳")
    private long taskSubmittimes;

    @ApiModelProperty("提交备注")
    @Excel(name = "提交备注")
    private String memo;

    @ApiModelProperty("提醒给负责人")
    private String remindChargePeople;

    @ApiModelProperty("提醒给参与人")
    private String remindPanticiants;

    @ApiModelProperty("是否需要提前提醒，0：否，1：是")
    private String warnFlag;

    @ApiModelProperty("提前提醒天数")
    private String warnDays;

    @ApiModelProperty("任务详情")
    private String taskDescribe;

    public String getTaskDescribe() {
        return taskDescribe;
    }

    public void setTaskDescribe(String taskDescribe) {
        this.taskDescribe = taskDescribe;
    }

    public String getRemindChargePeople() {
        return remindChargePeople;
    }

    public void setRemindChargePeople(String remindChargePeople) {
        this.remindChargePeople = remindChargePeople;
    }

    public String getRemindPanticiants() {
        return remindPanticiants;
    }

    public void setRemindPanticiants(String remindPanticiants) {
        this.remindPanticiants = remindPanticiants;
    }

    public String getWarnFlag() {
        return warnFlag;
    }

    public void setWarnFlag(String warnFlag) {
        this.warnFlag = warnFlag;
    }

    public String getWarnDays() {
        return warnDays;
    }

    public void setWarnDays(String warnDays) {
        this.warnDays = warnDays;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public long getTaskSubmittimes() {
        return taskSubmittimes;
    }

    public void setTaskSubmittimes(long taskSubmittimes) {
        this.taskSubmittimes = taskSubmittimes;
    }

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public Integer getScheduleRate() {
        return scheduleRate;
    }

    public void setScheduleRate(Integer scheduleRate) {
        this.scheduleRate = scheduleRate;
    }

    public void setTaskSubmittime(Date taskSubmittime) {
        this.taskSubmittime = taskSubmittime;
    }

    public Date getTaskSubmittime() {
        return taskSubmittime;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getMemo() {
        return memo;
    }

}

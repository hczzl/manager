package com.ruoyi.web.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 任务变更记录实体类
 *
 * @author zhongzhilong
 * @date 2019/8/27 0015
 */
public class TaskPostpone extends BaseEntity implements Serializable {

    private static final long SERIAL_VERSION_UID = 1669115747018731924L;

    private Long id;

    @ApiModelProperty("任务id")
    @Excel(name = "任务id")
    private Long tId;

    @ApiModelProperty("开始时间")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    @ApiModelProperty("结束时间")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    @ApiModelProperty("周期")
    @Excel(name = "周期")
    private String period;

    @ApiModelProperty("备注")
    @Excel(name = "备注")
    private String memo;

    @ApiModelProperty("开始时间")
    private String startTimes;

    @ApiModelProperty("结束时间")
    private String endTimes;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @ApiModelProperty("状态")
    private Integer state;

    @ApiModelProperty("firstUserId")
    private String firstUserId;

    @ApiModelProperty("审批类型")
    private String flowId;

    @ApiModelProperty("负责人id")
    private Integer chargeId;

    @ApiModelProperty("负责人姓名")
    private String chargeName;

    @ApiModelProperty("参与人")
    private String particiants;

    @ApiModelProperty("参与人姓名")
    private String particiantsName;

    @ApiModelProperty("审批流id")
    private Integer currentId;

    @ApiModelProperty("是否是新增数据")
    private Integer isNew;

    @ApiModelProperty("ccIds")
    private String ccIds;

    private String createChangeUser;

    private Integer isPostpone;

    private String type;

    private List<Integer> typeList;

    public List<Integer> getTypeList() {
        return typeList;
    }

    public void setTypeList(List<Integer> typeList) {
        this.typeList = typeList;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreateChangeUser() {
        return createChangeUser;
    }

    public void setCreateChangeUser(String createChangeUser) {
        this.createChangeUser = createChangeUser;
    }

    public Integer getIsPostpone() {
        return isPostpone;
    }

    public void setIsPostpone(Integer isPostpone) {
        this.isPostpone = isPostpone;
    }

    public String getCcIds() {
        return ccIds;
    }

    public void setCcIds(String ccIds) {
        this.ccIds = ccIds;
    }

    public Integer getIsNew() {
        return isNew;
    }

    public void setIsNew(Integer isNew) {
        this.isNew = isNew;
    }

    public Integer getCurrentId() {
        return currentId;
    }

    public void setCurrentId(Integer currentId) {
        this.currentId = currentId;
    }

    public Integer getChargeId() {
        return chargeId;
    }

    public void setChargeId(Integer chargeId) {
        this.chargeId = chargeId;
    }

    public String getParticiants() {
        return particiants;
    }

    public void setParticiants(String particiants) {
        this.particiants = particiants;
    }


    public String getFirstUserId() {
        return firstUserId;
    }

    public void setFirstUserId(String firstUserId) {
        this.firstUserId = firstUserId;
    }

    public String getFlowId() {
        return flowId;
    }

    public void setFlowId(String flowId) {
        this.flowId = flowId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long gettId() {
        return tId;
    }

    public void settId(Long tId) {
        this.tId = tId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getStartTimes() {
        return startTimes;
    }

    public void setStartTimes(String startTimes) {
        this.startTimes = startTimes;
    }

    public String getEndTimes() {
        return endTimes;
    }

    public void setEndTimes(String endTimes) {
        this.endTimes = endTimes;
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

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public String getParticiantsName() {
        return particiantsName;
    }

    public void setParticiantsName(String particiantsName) {
        this.particiantsName = particiantsName;
    }

    @Override
    public String toString() {
        return "TaskPostpone{" +
                "id=" + id +
                ", tId=" + tId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", period=" + period +
                ", memo='" + memo + '\'' +
                ", startTimes='" + startTimes + '\'' +
                ", endTimes='" + endTimes + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", state=" + state +
                '}';
    }
}

package com.ruoyi.web.domain;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * 阶段类型表
 */
public class ProjectMarketStageTable implements Serializable {
    private static final long SERIAL_VERSION_UID = -2820437112610573595L;
    /**
     * 表id
     */
    private int id;
    /**
     * 所属项目id
     */
    private Integer projectId;
    /**
     * 阶段类型
     * 阶段类型：1：方案，2研发：，3：工勘，4：安装，5：调试，6：交付
     */
    private Integer stageType;
    /**
     * 阶段负责人
     */
    private Integer chargePeople;
    /**
     * 人员id,多人以逗号隔开
     */
    private String participantsPeople;
    /**
     * 工作周期，单位为天
     */
    private Integer workPeriod;
    /**
     * 所需设备列表信息
     */
    private String deviceList;
    /**
     * 所需工具列表
     */
    private String toolList;
    /**
     * 是否需要车辆
     */
    private Integer isCar;
    /**
     * 场地信息
     */
    private String place;
    /**
     * 合作伙伴信息
     */
    private String partner;

    private Integer remindState;

    private Integer warnState;

    private String updateBy;

    private Date updateTime;
    /**
     * 存储阶段的临时数组
     */
    private int[] stageArray = new int[6];

    private ProjectMarketStageTable[] resources;

    private Date stageTimeNode;

    private String timeNode;

    public String getTimeNode() {
        return timeNode;
    }

    public void setTimeNode(String timeNode) {
        this.timeNode = timeNode;
    }



    public Date getStageTimeNode() {
        return stageTimeNode;
    }

    public void setStageTimeNode(Date stageTimeNode) {
        this.stageTimeNode = stageTimeNode;
    }

    public ProjectMarketStageTable[] getResources() {
        return resources;
    }

    public void setResources(ProjectMarketStageTable[] resources) {
        this.resources = resources;
    }

    public Integer getRemindState() {
        return remindState;
    }

    public void setRemindState(Integer remindState) {
        this.remindState = remindState;
    }

    public Integer getWarnState() {
        return warnState;
    }

    public void setWarnState(Integer warnState) {
        this.warnState = warnState;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public int[] getStageArray() {
        return stageArray;
    }

    public void setStageArray(int[] stageArray) {
        this.stageArray = stageArray;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public Integer getStageType() {
        return stageType;
    }

    public void setStageType(Integer stageType) {
        this.stageType = stageType;
    }

    public Integer getChargePeople() {
        return chargePeople;
    }

    public void setChargePeople(Integer chargePeople) {
        this.chargePeople = chargePeople;
    }

    public Integer getWorkPeriod() {
        return workPeriod;
    }

    public void setWorkPeriod(Integer workPeriod) {
        this.workPeriod = workPeriod;
    }

    public String getParticipantsPeople() {
        return participantsPeople;
    }

    public void setParticipantsPeople(String participantsPeople) {
        this.participantsPeople = participantsPeople;
    }

    public Integer getIsCar() {
        return isCar;
    }

    public void setIsCar(Integer isCar) {
        this.isCar = isCar;
    }

    public String getDeviceList() {
        return deviceList;
    }

    public void setDeviceList(String deviceList) {
        this.deviceList = deviceList;
    }

    public String getToolList() {
        return toolList;
    }

    public void setToolList(String toolList) {
        this.toolList = toolList;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    @Override
    public String toString() {
        return "ProjectMarketStageTable{" +
                "id=" + id +
                ", projectId=" + projectId +
                ", stageType=" + stageType +
                ", chargePeople=" + chargePeople +
                ", participantsPeople=" + participantsPeople +
                ", workPeriod='" + workPeriod + '\'' +
                ", deviceList='" + deviceList + '\'' +
                ", toolList='" + toolList + '\'' +
                ", isCar=" + isCar +
                ", place='" + place + '\'' +
                ", partner='" + partner + '\'' +
                ", stageArray=" + Arrays.toString(stageArray) +
                '}';
    }
}

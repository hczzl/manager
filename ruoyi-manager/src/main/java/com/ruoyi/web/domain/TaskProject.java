package com.ruoyi.web.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Suqz
 * @version 1.0
 * @date 2020/4/6 8:08
 */
@ApiModel
public class TaskProject implements Serializable {

    @ApiModelProperty("项目id")
    private Integer pId;

    @ApiModelProperty("项目标题")
    private String title;

    @ApiModelProperty("项目类型，0：科研项目，1：市场项目")
    private Integer projectType;

    @ApiModelProperty("负责人id")
    private Integer chargepeopleId;

    @ApiModelProperty("开始时间(时间戳)")
    private String startTimes;

    @ApiModelProperty("结束时间(时间戳)")
    private String endTimes;

    @ApiModelProperty("部门id")
    private Integer departmentId;

    @ApiModelProperty("默认为0,项目完成标识，0：未完成，1：项目完成，-1：项目完成审批中，2：项目中止,-2：项目中止审批中")
    private String projectFinishFlag;

    @ApiModelProperty("默认为1,立项成功标识，0：立项中，1：立项成功，2：立项失败,3:再次提交")
    private String establishStatus;

    @ApiModelProperty("默认为0,是否只查含里程碑的项目(0:查询里程碑的 1:查询所有)")
    private Integer isPlan;

    @ApiModelProperty("每页显示的条数")
    private int total;

    @ApiModelProperty("页码")
    private int pageNumber;

    @ApiModelProperty("总共的数量")
    private int pages;

    public Integer getpId() {
        return pId;
    }

    public void setpId(Integer pId) {
        this.pId = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getProjectType() {
        return projectType;
    }

    public void setProjectType(Integer projectType) {
        this.projectType = projectType;
    }

    public Integer getChargepeopleId() {
        return chargepeopleId;
    }

    public void setChargepeopleId(Integer chargepeopleId) {
        this.chargepeopleId = chargepeopleId;
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

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public String getProjectFinishFlag() {
        return projectFinishFlag;
    }

    public void setProjectFinishFlag(String projectFinishFlag) {
        this.projectFinishFlag = projectFinishFlag;
    }

    public String getEstablishStatus() {
        return establishStatus;
    }

    public void setEstablishStatus(String establishStatus) {
        this.establishStatus = establishStatus;
    }

    public Integer getIsPlan() {
        return isPlan;
    }

    public void setIsPlan(Integer isPlan) {
        this.isPlan = isPlan;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }
}
